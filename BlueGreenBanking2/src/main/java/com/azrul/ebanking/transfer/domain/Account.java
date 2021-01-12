/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.ebanking.transfer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author azrul
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findByAccountnumber", query = "SELECT a FROM Account a WHERE a.accountnumber = :accountnumber"),
    @NamedQuery(name = "Account.findByProduct", query = "SELECT a FROM Account a WHERE a.product = :product"),
    @NamedQuery(name = "Account.findByFriendlyname", query = "SELECT a FROM Account a WHERE a.friendlyname = :friendlyname"),
    @NamedQuery(name = "Account.findByBalance", query = "SELECT a FROM Account a WHERE a.balance = :balance")})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private String accountnumber;
    private String product;
    private String friendlyname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private BigDecimal balance;
    
    @JoinColumn(name = "accounttype", referencedColumnName = "accounttypeid")
    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    private Accounttype accounttype;

    public Account() {
    }

    public Account(String accountnumber) {
        this.accountnumber = accountnumber;
    }

  

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getFriendlyname() {
        return friendlyname;
    }

    public void setFriendlyname(String friendlyname) {
        this.friendlyname = friendlyname;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

  

    public Accounttype getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(Accounttype accounttype) {
        this.accounttype = accounttype;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountnumber != null ? accountnumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.accountnumber == null && other.accountnumber != null) || (this.accountnumber != null && !this.accountnumber.equals(other.accountnumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.azrul.ebanking.transfer.domain.Account[ accountnumber=" + accountnumber + " ]";
    }
    
}
