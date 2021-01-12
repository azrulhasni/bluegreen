/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.ebanking.transfer.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author azrul
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Accounttype.findAll", query = "SELECT a FROM Accounttype a"),
    @NamedQuery(name = "Accounttype.findByAccounttypeid", query = "SELECT a FROM Accounttype a WHERE a.accounttypeid = :accounttypeid"),
    @NamedQuery(name = "Accounttype.findByDescription", query = "SELECT a FROM Accounttype a WHERE a.description = :description")})
public class Accounttype implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private Long accounttypeid;
    private String description;
   
    public Accounttype() {
    }

    public Accounttype(Long accounttypeid) {
        this.accounttypeid = accounttypeid;
    }

    public Long getAccounttypeid() {
        return accounttypeid;
    }

    public void setAccounttypeid(Long accounttypeid) {
        this.accounttypeid = accounttypeid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accounttypeid != null ? accounttypeid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Accounttype)) {
            return false;
        }
        Accounttype other = (Accounttype) object;
        if ((this.accounttypeid == null && other.accounttypeid != null) || (this.accounttypeid != null && !this.accounttypeid.equals(other.accounttypeid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.azrul.ebanking.transfer.domain.Accounttype[ accounttypeid=" + accounttypeid + " ]";
    }
    
}
