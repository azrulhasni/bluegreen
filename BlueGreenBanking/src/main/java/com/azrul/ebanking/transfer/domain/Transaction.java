/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.ebanking.transfer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author azrul
 */
@Entity
@Table(name = "transaction")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "Transaction.findAll", query = "SELECT t FROM Transaction t"),
//    @NamedQuery(name = "Transaction.findByTransactionid", query = "SELECT t FROM Transaction t WHERE t.transactionid = :transactionid"),
//    @NamedQuery(name = "Transaction.findByRowid", query = "SELECT t FROM Transaction t WHERE t.rowid = :rowid"),
//    @NamedQuery(name = "Transaction.findByAmount", query = "SELECT t FROM Transaction t WHERE t.amount = :amount"),
//    @NamedQuery(name = "Transaction.findByDatetime", query = "SELECT t FROM Transaction t WHERE t.datetime = :datetime")})
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
   
    
    @Id
    @Basic(optional = false)
    @Column(name = "transactionid")
    private String transactionid;
    @Basic(optional = false)
    @Column(name = "rowid")
    private long rowid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;
    @JoinColumn(name = "fromaccount", referencedColumnName = "accountnumber")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account fromaccount;
    @JoinColumn(name = "toaccount", referencedColumnName = "accountnumber")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account toaccount;

    public Transaction() {
    }

    public Transaction(String transactionid, BigDecimal amount, Date datetime, Account fromaccount, Account toaccount) {
        this.transactionid = transactionid;
        this.amount = amount;
        this.datetime = datetime;
        this.fromaccount = fromaccount;
        this.toaccount = toaccount;
    }
    
    

    public Transaction(String transactionid) {
        this.transactionid = transactionid;
    }

    public Transaction(String transactionid, long rowid) {
        this.transactionid = transactionid;
        this.rowid = rowid;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public long getRowid() {
        return rowid;
    }

    public void setRowid(long rowid) {
        this.rowid = rowid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Account getFromaccount() {
        return fromaccount;
    }

    public void setFromaccount(Account fromaccount) {
        this.fromaccount = fromaccount;
    }

    public Account getToaccount() {
        return toaccount;
    }

    public void setToaccount(Account toaccount) {
        this.toaccount = toaccount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionid != null ? transactionid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        if ((this.transactionid == null && other.transactionid != null) || (this.transactionid != null && !this.transactionid.equals(other.transactionid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.azrul.ebanking.transfer.domain.Transaction[ transactionid=" + transactionid + " ]";
    }
    
}
