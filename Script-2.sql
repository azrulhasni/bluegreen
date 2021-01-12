
 ALTER TABLE public.account
    ADD COLUMN accounttype integer NOT NULL DEFAULT 0;
    

    update account set accounttype = 1 where product='Saving account';
    

    ALTER TABLE account ADD CONSTRAINT account_fk FOREIGN KEY (accounttype) REFERENCES accounttype (accounttypeid)