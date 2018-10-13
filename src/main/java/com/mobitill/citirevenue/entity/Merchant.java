/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Francis
 */
@Entity
@Table(name = "merchant", catalog = "mobitill_demo_revenue", schema = "temp")
@XmlRootElement
@JsonIgnoreProperties({"createstamp"})
@NamedQueries({
    @NamedQuery(name = "Merchant.findAll", query = "SELECT m FROM Merchant m ORDER BY m.id")
    , @NamedQuery(name = "Merchant.findById", query = "SELECT m FROM Merchant m WHERE m.id = :id")
    , @NamedQuery(name = "Merchant.findByPin", query = "SELECT m FROM Merchant m WHERE m.pin = :pin")
    , @NamedQuery(name = "Merchant.findByVat", query = "SELECT m FROM Merchant m WHERE m.vat = :vat")
    , @NamedQuery(name = "Merchant.findByName", query = "SELECT m FROM Merchant m WHERE m.name = :name")
    , @NamedQuery(name = "Merchant.findByLocation", query = "SELECT m FROM Merchant m WHERE m.location = :location")
    , @NamedQuery(name = "Merchant.findByCreatestamp", query = "SELECT m FROM Merchant m WHERE m.createstamp = :createstamp"),
    @NamedQuery(name = "Merchant.findByPinVat", query = "SELECT CASE WHEN(COUNT(m.id) > 0L) THEN TRUE ELSE FALSE END FROM Merchant m WHERE LOWER(m.pin) = LOWER(:pin) OR LOWER(m.vat) = LOWER(:vat)"),
    @NamedQuery(name = "Merchant.findPinVat", query = "SELECT m FROM Merchant m WHERE LOWER(m.pin) = LOWER(:pin) OR LOWER(m.vat) = LOWER(:vat)"),
    @NamedQuery(name = "Merchant.findNameCategory", query = "SELECT NEW com.mobitill.citirevenue.service.MerchantDescription(m.name,m.categorytype,m.categoryname) FROM Merchant m WHERE LOWER(m.pin) = LOWER(:pin) OR LOWER(m.vat) = LOWER(:vat)"),
    @NamedQuery(name = "Merchant.getMerchantsPin", query = "SELECT m.pin FROM Merchant m")
})
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    //@NotNull(message = "field pin cannot be empty")
    @Size(min = 0, max = 15,message = "allowed length for pin 1 - {max}")
    @Column(name = "pin")
    private String pin;
    @Basic(optional = false)
    //@NotNull(message = "field vat cannot be empty")
    @Size(min = 0,max = 15, message = "allowed length vat 1 - {max}")
    @Column(name = "vat")
    private String vat;
    @Basic(optional = false)
    @NotNull(message = "field name cannot be empty")
    @Size(min = 6, max = 100, message = "allowed length name {min} - {max}")
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull(message = "field location cannot be empty")
    @Size(min = 5, max = 100,message = "allowed length location  {min} - {max}")
    @Column(name = "location")
    private String location;
    @Column(name = "createstamp")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createstamp;
    
    @Size(min = 5, max = 100,message = "allowed length category name  {min} - {max}")
    @Column(name = "categoryname")
    private String categoryname;
    @Basic(optional = false)
    @NotNull(message = "field categorytype cannot be empty")
    @Column(name = "categorytype")
    private String categorytype;
    
    @Basic(optional = false)
    @NotNull(message = "field phone number cannot be empty")
    @Size(min =12  , max = 12, message = "phone number allowed length{max}")
    @Column(name = "phonenumber")
    private String phonenumber;
    
    @Transient
    private String datecreated;

    public Merchant() {
    }

    public Merchant(Integer id) {
        this.id = id;
    }

    public Merchant(Integer id, String pin, String vat, String name, String location, Date createstamp) {
        this.id = id;
        this.pin = pin;
        this.vat = vat;
        this.name = name;
        this.location = location;
        this.createstamp = createstamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreatestamp() {
        return createstamp;
    }

    public void setCreatestamp(Date createstamp) {
        this.createstamp = createstamp;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategorytype() {
        return categorytype;
    }

    public void setCategorytype(String categorytype) {
        this.categorytype = categorytype;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Merchant)) {
            return false;
        }
        Merchant other = (Merchant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mobitill.citirevenue.entity.Merchant[ id=" + id + " ]";
    }
    
}
