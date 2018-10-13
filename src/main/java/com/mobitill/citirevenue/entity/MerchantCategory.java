/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Francis
 */
@Entity
@Table(name = "merchantcategory", catalog = "mobitill_demo_revenue", schema = "temp")
@XmlRootElement
@JsonIgnoreProperties({"id"})
@NamedQueries({
    @NamedQuery(name = "MerchantCategory.findAll", query = "SELECT m FROM MerchantCategory m"),
    @NamedQuery(name = "MerchantCategory.findByMcc", query = "SELECT m FROM MerchantCategory m WHERE UPPER(m.mcc) = :mcc"),
    @NamedQuery(name = "MerchantCategory.findMccExist", query = "SELECT CASE WHEN(COUNT(m.id) > 0L) THEN TRUE ELSE FALSE END FROM MerchantCategory m WHERE UPPER(m.mcc) = UPPER(:mcc)")
})
public class MerchantCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(length = 10, name = "mcc", nullable = false, updatable = false, unique = true)
    private String mcc;

    @Column(name = "description", nullable = false)
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof MerchantCategory)) {
            return false;
        }
        MerchantCategory other = (MerchantCategory) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (mcc != null && !mcc.trim().isEmpty()) {
            result += "mcc: " + mcc;
        }
        if (description != null && !description.trim().isEmpty()) {
            result += ", description: " + description;
        }
        return result;
    }

}
