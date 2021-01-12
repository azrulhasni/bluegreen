/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.ebanking.transfer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author azrul
 */
@Entity
@Table(name = "account")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
//    @NamedQuery(name = "Account.findByAccountnumber", query = "SELECT a FROM Account a WHERE a.accountnumber = :accountnumber"),
//    @NamedQuery(name = "Account.findByProduct", query = "SELECT a FROM Account a WHERE a.product = :product"),
//    @NamedQuery(name = "Account.findByFriendlyname", query = "SELECT a FROM Account a WHERE a.friendlyname = :friendlyname"),
//    @NamedQuery(name = "Account.findByBalance", query = "SELECT a FROM Account a WHERE a.balance = :balance")})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "accountnumber")
    private String accountnumber;
    @Column(name = "product")
    private String product;
    @Column(name = "friendlyname")
    private String friendlyname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "balance")
    private BigDecimal balance;
    
    @JsonIgnore
    @OneToMany(mappedBy = "fromaccount", fetch = FetchType.LAZY)
    private Collection<Transaction> fromTransactions;
    
    @JsonIgnore
    @OneToMany(mappedBy = "toaccount", fetch = FetchType.LAZY)
    private Collection<Transaction> toTransactions;

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
        return "Account{" + "accountnumber=" + accountnumber + ", product=" + product + ", friendlyname=" + friendlyname + ", balance=" + balance + '}';
    }

    /**
     * @return the fromTransactions
     */
    @XmlTransient
    public Collection<Transaction> getFromTransactions() {
        return fromTransactions;
    }

    /**
     * @param fromTransactions the fromTransactions to set
     */
    public void setFromTransactions(Collection<Transaction> fromTransactions) {
        this.fromTransactions = fromTransactions;
    }

    /**
     * @return the toTransactions
     */
    @XmlTransient
    public Collection<Transaction> getToTransactions() {
        return toTransactions;
    }

    /**
     * @param toTransactions the toTransactions to set
     */
    public void setToTransactions(Collection<Transaction> toTransactions) {
        this.toTransactions = toTransactions;
    }

    
    
}
