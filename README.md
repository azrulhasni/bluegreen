Addressing the elephant in the blue-green room: Data synchronisation for blue-green deployment using CockroachDB
================================================================================================================

 

Azrul MADISA

 

09-JAN-2021

 

Introduction
------------

Blue green deployment is one of the ideal pattern (if I can call it that) of
continuous  deployment. Martin Fowler describes the workings of blue green
deployment well [<https://martinfowler.com/bliki/BlueGreenDeployment.html>].
Citing him:

 

>    The blue-green deployment approach does this by ensuring you have two
>   production environments, as identical as possible. At any time one of them,
>   let's say blue for the example, is live. As you prepare a new release of
>   your software you do your final stage of testing in the green environment.
>   Once the software is working in the green environment, you switch the router
>   so that all incoming requests go to the green environment - the blue one is
>   now idle.

 

Sounds simple enough. But Martin Fowler also describes the challenges of having
to synchronise data across blue and green

 

>   Databases can often be a challenge with this technique, particularly when
>   you need to change the schema to support a new version of the software

 

The challenge here is three folds: For one, how would you update the database
schema of a software that is still running?

So, imagine you have blue and green production environment. Let us say that blue
is live and green is being updated - and the update could involved the database
also. Once green is fully deployed, it will be tested in production - usually
with real production usage. In which case, (and our second challenge) how would
we make sure the data and transactions captured by green is also available in
blue? Remember, this is production environment. We cannot just ’throw away’ data
as it will show up in audit. In a regulated industry (banking for example),
throwing away data from green could be a regulatory violation.

Thirdly, while green is being tested, blue is actually live. New data
transactions are coming to it all the time. How would we make sure that data
committed to blue, is also available in green since green will be the future
live system?

 

Of course, one easy way to solve the database problem is to work with a single
database and both blue and green would talk to this single database. This
approach seems risky. If any update to the database actually breaks it, we will
end up affecting the live (blue) application also. So we propose distributing
the database across blue and green environment.

 

In this article we will try to solve this database distribution problem using
pattern of database replication called Primary-Primary topology. We find that,
CockroachDB offers an excellent implementation of this pattern and also allow us
to make changes to the schema on the fly and thus solving our first challenge
above.

 

Primary-Primary (or Multi-Primary) vs. Primary-Replicant
--------------------------------------------------------

There are two main topology to distribute a database. Primary-Primary vs.
Primary-Replicant

In a primary-replicant setup, a replicant database is created. The replicant
database would only receive read queries. Any update, create or delete query
will go to the primary. Changes that happens to the primary database is
propagated to the replicant database.

 

![](README.images/dAgtNA.jpg)

 

As you can see, this will allow us to distribute read I/O across multiple
replicant databases and thus lighten the burden of the primary database. Given
that most queries are “read” in nature, this makes perfect sense.

 

A primary-primary setup on the other hand allow both read and write to happen in
both instances of the database. The primaries will update each other on the
changes.

 

As you can see, a primary-primary  setup is more challenging to implement as
updates can go both ways between the primaries. But, this is perfect for our
blue green deployment.

 

Primary-primary and blue-green deployment
-----------------------------------------

![](README.images/nocQ4V.jpg)

 

In a blue-green setup, we will have the architecture proposed above.

 

1) Blue is live and is currently serving our users.

2) Green is being updated through automated deployment (in actual fact, it
doesn’t have to be automated). The update will involved both database and
application

3) Note that while deployment to green is happening, actual data and
transactions are coming in through blue. These will be sync up to green
automatically

4) At the same time, any update to the database schema in green is propagated to
blue directly. Because of this, the **update done to the database in green must
be backward compatible**

5) Once update is ready, a production tester (usually a subset of actual users)
are going to test the application using real production data/transactions (not
dummy data)

6) Note that the real production test data coming from green will be
synchronised to blue directly

7) We will need to fix any production related issues and redeploy. This means
that we may need to be careful with the production test cases we ran at green.
If that data is committed, we may (or may not) care about application state.
Imagine that the application is a workflow application. The production test data
that we committed at green, move the workflow from step A to step B. If things
go wrong and we need to retest, we may need to take into account that the
workflow is now is step B - which may not be our assumption.

8) Once we are happy, we will use our router to route the traffic from blue to
green - and green will be, in turn, live

9) In our next deployment, blue will be updated just as green is this time
around.

 

In this article, we will concentrate on the database update part. We will not
address the router switching bit as there are many articles addressing this
already.

 

CockroachDB
-----------

 

CockroachDB by Cockroach Labs [<https://www.cockroachlabs.com/>] is a very
resilient RDBMS inspired by Google Spanner. It allows global level multi primary
replication. Primary-primary replication is considered first class citizen
rather than some additional afterthought that needs some super expensive plugins
to implement. Things we like about CockroachDB that makes it ideal for
blue-green setup

 

1) Database is distributed by default - primary-primary topology

2) Very resilient to online schema change

 

In addition, CockroachDB is:

 

1) Fully SQL

2) CP (as in CAP theorem)

3) Support TDE

4) Support TLS communication

5) Open source

 

What will we do in this article
-------------------------------

The goal of this article is to show how to run a blue-green deployment -
concentrating on data and the database.

We will first setup a blue part of our infra-structure. We will then setup
monitoring to see the baseline performance of our app. Once this established,
under the same monitoring, we will enact green deployment (including schema
change) and see what is the impact of that deployment in blue. Finally, we will
run both blue and green in parallel to see how one affect the other.

 

Setting up the database
-----------------------

In this article, we are setting up 3 instances of CockroachDB in the same
machine (my humble laptop). They will work on different ports but having the
same hostname. Because of this, we will share the same TLS certificate. In a
real world setup, there will be 3 servers (which could be
multi-availability-zones or multi-regions), each with a different hostname. And
therefore we will need 3 different certificates.

 

### Directory structure 

Our directory structure will look like below:

\$BLUEGREEN

\|—certs

\|—my-safe-directory

\|—node1

\|—node2

\|—node3

 

### CockroachDB installation

-   Firstly, we will need to install CockroachDB. You may want to follow the
    tutorial published on Cockroach Lab’s site. I am using a Mac so I will be
    following the steps here
    <https://www.cockroachlabs.com/docs/v20.2/install-cockroachdb-mac.html>.
    There are also tutorials for Windows, Linux, Kubernetes and Docker.

-   Next, follow the tutorial here
    <https://www.cockroachlabs.com/docs/stable/secure-a-cluster.html> to create
    a TLS secured cluster. Note that the directories node1, node2 and node3
    above are for the respective nodes in the cluster

-   Once done, run each node of the cluster. Point our command line console to
    \$BLUEGREEN and run:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
> cockroach start \
--certs-dir=certs \
--store=node1 \
--listen-addr=localhost:26257 \
--http-addr=localhost:8080 \
--join=localhost:26257,localhost:26258,localhost:26259 \
--background
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
> cockroach start \
--certs-dir=certs \
--store=node2 \
--listen-addr=localhost:26258 \
--http-addr=localhost:8081 \
--join=localhost:26257,localhost:26258,localhost:26259 \
--background
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
> cockroach start \
--certs-dir=certs \
--store=node3 \
--listen-addr=localhost:26259 \
--http-addr=localhost:8082 \
--join=localhost:26257,localhost:26258,localhost:26259 \
--background
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

-   Create a user named banking. Firstly, point your command line console to
    \$BLUEGREEN and launch the CockroachDB client:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
> cockroach sql --certs-dir=certs --host=localhost:26257
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

-   You will get the CockroachDB client. Run the command to create ‘banking’
    user:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
root@:26257/defaultdb> CREATE USER banking WITH LOGIN PASSWORD 'somepassword'
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

-   Create database ’banking'

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
root@:26257/defaultdb> CREATE DATABASE banking
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

-   Assign the database ‘banking’ to the user ‘banking'

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
root@:26257/defaultdb> alter database banking owner to banking
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

-   Log out, add certs for banking. The cert is specific to a user

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 > cockroach cert create-client \
    banking \
    --certs-dir=certs \
    --ca-key=my-safe-directory/ca.key
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

-   Create pk8 file:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
> openssl pkcs8 -topk8 -inform PEM -outform DER -in certs/client.banking.key -out certs/client.banking.pk8
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If asked for pwd, just put something

 

 

 

### CockroachDB setup

-   We use DBeaver <https://dbeaver.io/> as a GUI client to access CockroachDB.
    Download it, install and run

-   Under DBeaver, click on File -\> New. Choose DBeaver -\> Database Connection

-   Choose CockroachDB, and click Next

![](README.images/nc8Gu0.jpg)

-   In the settings:

    -   host = localhost

    -   database = banking

    -   username = banking

    -   password = somepassword

![](README.images/weq8aL.jpg)

-   We can test the connection with the ’Test Connection …’ button. Once done,
    click OK

-   Next, we will land on the main interface of DBeaver. Then, choose the New
    SQL Editor from the main menu

![](README.images/ovRuQy.jpg)

-   You will get an SQL Editor. If the password is asked, then just enter it

![](README.images/dkzH8L.jpg)

 

-   In the SQL Editor, run the DDL script below:

 

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE TABLE public.account (
        accountnumber VARCHAR(20) NOT NULL,
        product VARCHAR(20) NULL,
        friendlyname VARCHAR(100) NULL,
        balance DECIMAL(10,2) NULL,
        CONSTRAINT account_pk PRIMARY KEY (accountnumber ASC),
        FAMILY "primary" (accountnumber, product, friendlyname, balance)
);

CREATE TABLE public.transaction (
            transactionid VARCHAR(40) NOT NULL,
            fromaccount VARCHAR(20) NULL,
            toaccount VARCHAR(20) NULL,
            amount DECIMAL(10,2) NULL,
            datetime TIMESTAMP(0) NULL,
            CONSTRAINT "primary" PRIMARY KEY (transactionid ASC),
            CONSTRAINT transaction_fk FOREIGN KEY (fromaccount) REFERENCES public.account(accountnumber),
            CONSTRAINT transaction_fk_1 FOREIGN KEY (toaccount) REFERENCES public.account(accountnumber),
            INDEX transaction_datetime_idx (datetime ASC),
            FAMILY "primary" (transactionid, rowid, fromaccount, toaccount, amount, datetime)
);
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

-   This will create the account and transaction tables. Our database is now
    setup for use

 

Setting up blue micro-service
-----------------------------
