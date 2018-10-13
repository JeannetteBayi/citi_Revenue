/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.entity;

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
@Table(name = "taxregister", catalog = "mobitill_demo_revenue", schema = "temp")
@XmlRootElement
@JsonIgnoreProperties({"statusStamp","statusMessage","issuedate"})
@NamedQueries({
    @NamedQuery(name = "Taxregister.findAll", query = "SELECT t FROM Taxregister t ORDER BY t.id")
    , @NamedQuery(name = "Taxregister.findById", query = "SELECT t FROM Taxregister t WHERE t.id = :id")
    , @NamedQuery(name = "Taxregister.findByMerchantpin", query = "SELECT t FROM Taxregister t WHERE t.merchantpin = :merchantpin")
    , @NamedQuery(name = "Taxregister.findByDeviceid", query = "SELECT t FROM Taxregister t WHERE LOWER(t.deviceid) = LOWER(:deviceid)"),
      @NamedQuery(name = "Taxregister.existByDeviceid", query = "SELECT CASE WHEN (COUNT(t.id) > 0L) THEN TRUE ELSE FALSE END FROM Taxregister t WHERE LOWER(t.deviceid) = LOWER(:deviceid)")
    , @NamedQuery(name = "Taxregister.findByDevicetype", query = "SELECT t FROM Taxregister t WHERE t.devicetype = :devicetype")
    , @NamedQuery(name = "Taxregister.findByIssuedate", query = "SELECT t FROM Taxregister t WHERE t.issuedate = :issuedate")
    , @NamedQuery(name = "Taxregister.findByStatusStamp", query = "SELECT t FROM Taxregister t WHERE t.statusStamp = :statusStamp")
    , @NamedQuery(name = "Taxregister.findByStatusMessage", query = "SELECT t FROM Taxregister t WHERE t.statusMessage = :statusMessage")})
public class Taxregister implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull(message = "merchantpin cannot be empty")
    @Size(min = 10, max = 15, message = "allowed length for merchantpin {min} - {max}")
    @Column(name = "merchantpin")
    private String merchantpin;
    @Basic(optional = false)
    @Size(min = 0, max = 100)
    @Column(name = "deviceid",updatable = false)
    private String deviceid;
    @Basic(optional = false)
    @NotNull(message = "devicetype cannot be empty")
    @Size(min = 3, max = 100,message = "allowed length for devicetype {min}-{max}")
    @Column(name = "devicetype",updatable = false)
    private String devicetype;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "issuedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issuedate;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "status_stamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date statusStamp;
    @Size(max = 2147483647)
    @Column(name = "status_message")
    private String statusMessage;
    
    @Transient
    private String dateissued;

    public Taxregister() {
    }

    public Taxregister(Integer id) {
        this.id = id;
    }

    public Taxregister(Integer id, String merchantpin, String deviceid, String devicetype, Date issuedate, Date statusStamp) {
        this.id = id;
        this.merchantpin = merchantpin;
        this.deviceid = deviceid;
        this.devicetype = devicetype;
        this.issuedate = issuedate;
        this.statusStamp = statusStamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantpin() {
        return merchantpin;
    }

    public void setMerchantpin(String merchantpin) {
        this.merchantpin = merchantpin;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public Date getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(Date issuedate) {
        this.issuedate = issuedate;
    }

    public Date getStatusStamp() {
        return statusStamp;
    }

    public void setStatusStamp(Date statusStamp) {
        this.statusStamp = statusStamp;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getDateissued() {
        return dateissued;
    }

    public void setDateissued(String dateissued) {
        this.dateissued = dateissued;
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
        if (!(object instanceof Taxregister)) {
            return false;
        }
        Taxregister other = (Taxregister) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mobitill.citirevenue.entity.Taxregister[ id=" + id + " ]";
    }

}
