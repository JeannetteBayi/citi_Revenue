/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "transactions", catalog = "mobitill_demo_revenue", schema = "temp")
@XmlRootElement
@JsonIgnoreProperties({"stamp"})
@NamedQueries({
    @NamedQuery(name = "Transactions.findAll", query = "SELECT t FROM Transactions t ORDER BY t.id DESC")
    , @NamedQuery(name = "Transactions.findById", query = "SELECT t FROM Transactions t WHERE t.id = :id")
    , @NamedQuery(name = "Transactions.findByMerchantpin", query = "SELECT t FROM Transactions t WHERE t.merchantpin = :merchantpin")
    , @NamedQuery(name = "Transactions.findByClientpin", query = "SELECT t FROM Transactions t WHERE t.clientpin = :clientpin")
    , @NamedQuery(name = "Transactions.findByStampSummary", query = "SELECT  NEW com.mobitill.citirevenue.service.TaxSummary(SUM(t.tax1),SUM(t.tax2),SUM(t.tax3),SUM(t.tax4)) FROM Transactions t WHERE t.stamp >= :startstamp AND t.stamp <= :stopstamp")
    , @NamedQuery(name = "Transactions.findByStampSummaryAndMerchant", query = "SELECT  NEW com.mobitill.citirevenue.service.TaxSummary(SUM(t.tax1),SUM(t.tax2),SUM(t.tax3),SUM(t.tax4)) FROM Transactions t WHERE t.stamp >= :startstamp AND t.stamp <= :stopstamp AND LOWER(t.merchantpin) = LOWER(:pin)")
    , @NamedQuery(name = "Transactions.findByMerchantStampSummary", query = "SELECT  NEW com.mobitill.citirevenue.service.MerchantSummary(SUM(t.amount1),SUM(t.amount2),SUM(t.amount3),SUM(t.amount4),SUM(t.tax1),SUM(t.tax2),SUM(t.tax3),SUM(t.tax4)) FROM Transactions t WHERE t.merchantpin = :merchantpin AND (t.stamp >= :startstamp AND t.stamp <= :stopstamp)")
    , @NamedQuery(name = "Transactions.findByClientStampSummary", query = "SELECT t FROM Transactions t WHERE t.clientpin = :clientpin AND (t.stamp >= :startstamp AND t.stamp <= :stopstamp)")
    , @NamedQuery(name = "Transactions.findByStamp", query = "SELECT t FROM Transactions t WHERE t.stamp >= :startstamp AND t.stamp <= :stopstamp ORDER BY t.stamp ASC")
    , @NamedQuery(name = "Transactions.findByStampAndMerchant", query = "SELECT t FROM Transactions t WHERE t.stamp >= :startstamp AND t.stamp <= :stopstamp AND LOWER(t.merchantpin) = LOWER(:pin) ORDER BY t.stamp ASC")
    , @NamedQuery(name = "Transactions.findByStampMerchants", query = "SELECT DISTINCT(t.merchantpin) FROM Transactions t WHERE  t.stamp >= :startstamp AND t.stamp <= :stopstamp")
    , @NamedQuery(name = "Transactions.findByStampClientMerchants", query = "SELECT DISTINCT(t.merchantpin) FROM Transactions t WHERE t.clientpin = :clientPin AND (t.stamp >= :startstamp AND t.stamp <= :stopstamp)")
    , @NamedQuery(name = "Transactions.findByStampMerchantpin", query = "SELECT t FROM Transactions t WHERE LOWER(t.merchantpin) = LOWER(:pin) AND (t.stamp >= :startstamp AND t.stamp <= :stopstamp) ORDER BY t.stamp DESC")
    , @NamedQuery(name = "Transactions.findByStampClientpin", query = "SELECT t FROM Transactions t WHERE LOWER(t.clientpin) = LOWER(:pin) AND (t.stamp >= :startstamp AND t.stamp <= :stopstamp) ORDER BY t.stamp DESC")
    , @NamedQuery(name = "Transactions.findByReceiptnumber", query = "SELECT CASE WHEN(COUNT(t.id) > 0L) THEN TRUE ELSE FALSE END FROM Transactions t WHERE LOWER(t.receiptnumber) = LOWER(:receiptnumber) AND LOWER(t.merchantpin) = LOWER(:pin)")
    , @NamedQuery(name = "Transactions.findByPaymentmode", query = "SELECT t FROM Transactions t WHERE t.paymentmode = :paymentmode")
    , @NamedQuery(name = "Transactions.findByAmount1", query = "SELECT t FROM Transactions t WHERE t.amount1 = :amount1")
    , @NamedQuery(name = "Transactions.findByAmount2", query = "SELECT t FROM Transactions t WHERE t.amount2 = :amount2")
    , @NamedQuery(name = "Transactions.findByAmount3", query = "SELECT t FROM Transactions t WHERE t.amount3 = :amount3")
    , @NamedQuery(name = "Transactions.findByAmount4", query = "SELECT t FROM Transactions t WHERE t.amount4 = :amount4")
    , @NamedQuery(name = "Transactions.findByTax1", query = "SELECT t FROM Transactions t WHERE t.tax1 = :tax1")
    , @NamedQuery(name = "Transactions.findByTax2", query = "SELECT t FROM Transactions t WHERE t.tax2 = :tax2")
    , @NamedQuery(name = "Transactions.findByTax3", query = "SELECT t FROM Transactions t WHERE t.tax3 = :tax3")
    , @NamedQuery(name = "Transactions.findByTax4", query = "SELECT t FROM Transactions t WHERE t.tax4 = :tax4")
    , @NamedQuery(name = "Transactions.findByEtr", query = "SELECT t FROM Transactions t WHERE t.etr = :etr")
    , @NamedQuery(name = "Transactions.findByEsd", query = "SELECT t FROM Transactions t WHERE t.esd = :esd")
    , @NamedQuery(name = "Transactions.findByDeviceserial", query = "SELECT t FROM Transactions t WHERE t.deviceserial = :deviceserial")})
public class Transactions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "merchantpin")
    private String merchantpin;
    @Size(max = 100)
    @Column(name = "clientpin")
    private String clientpin;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "stamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stamp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "receiptnumber")
    private String receiptnumber;
    @Size(max = 100)
    @Column(name = "paymentmode")
    private String paymentmode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount1")
    private BigDecimal amount1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount2")
    private BigDecimal amount2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount3")
    private BigDecimal amount3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount4")
    private BigDecimal amount4;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tax1")
    private BigDecimal tax1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tax2")
    private BigDecimal tax2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tax3")
    private BigDecimal tax3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tax4")
    private BigDecimal tax4;
    @Size(max = 2147483647)
    @Column(name = "etr")
    private String etr;
    @Size(max = 2147483647)
    @Column(name = "esd")
    private String esd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "deviceserial")
    private String deviceserial;
    @Column(name = "categorytype")
    private String categorytype;
    @Size(max = 2147483647)
    @Column(name = "receiptinfo")
    private String receiptinfo;
    @Column(name = "devicetype")
    private String devicetype;
    
    @Transient
    private String timestamp;

    public Transactions() {
    }

    public Transactions(Integer id) {
        this.id = id;
    }

    public Transactions(Integer id, String merchantpin, Date stamp, String receiptnumber, BigDecimal amount1, BigDecimal amount2, BigDecimal amount3, BigDecimal amount4, BigDecimal tax1, BigDecimal tax2, BigDecimal tax3, BigDecimal tax4, String deviceserial) {
        this.id = id;
        this.merchantpin = merchantpin;
        this.stamp = stamp;
        this.receiptnumber = receiptnumber;
        this.amount1 = amount1;
        this.amount2 = amount2;
        this.amount3 = amount3;
        this.amount4 = amount4;
        this.tax1 = tax1;
        this.tax2 = tax2;
        this.tax3 = tax3;
        this.tax4 = tax4;
        this.deviceserial = deviceserial;
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

    public String getClientpin() {
        return clientpin;
    }

    public void setClientpin(String clientpin) {
        this.clientpin = clientpin;
    }

    public Date getStamp() {
        return stamp;
    }

    public void setStamp(Date stamp) {
        this.stamp = stamp;
    }

    public String getReceiptnumber() {
        return receiptnumber;
    }

    public void setReceiptnumber(String receiptnumber) {
        this.receiptnumber = receiptnumber;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public BigDecimal getAmount1() {
        return amount1;
    }

    public void setAmount1(BigDecimal amount1) {
        this.amount1 = amount1;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    public BigDecimal getAmount3() {
        return amount3;
    }

    public void setAmount3(BigDecimal amount3) {
        this.amount3 = amount3;
    }

    public BigDecimal getAmount4() {
        return amount4;
    }

    public void setAmount4(BigDecimal amount4) {
        this.amount4 = amount4;
    }

    public BigDecimal getTax1() {
        return tax1;
    }

    public void setTax1(BigDecimal tax1) {
        this.tax1 = tax1;
    }

    public BigDecimal getTax2() {
        return tax2;
    }

    public void setTax2(BigDecimal tax2) {
        this.tax2 = tax2;
    }

    public BigDecimal getTax3() {
        return tax3;
    }

    public void setTax3(BigDecimal tax3) {
        this.tax3 = tax3;
    }

    public BigDecimal getTax4() {
        return tax4;
    }

    public void setTax4(BigDecimal tax4) {
        this.tax4 = tax4;
    }

    public String getEtr() {
        return etr;
    }

    public void setEtr(String etr) {
        this.etr = etr;
    }

    public String getEsd() {
        return esd;
    }

    public void setEsd(String esd) {
        this.esd = esd;
    }

    public String getDeviceserial() {
        return deviceserial;
    }

    public void setDeviceserial(String deviceserial) {
        this.deviceserial = deviceserial;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCategorytype() {
        return categorytype;
    }

    public void setCategorytype(String categorytype) {
        this.categorytype = categorytype;
    }

    public String getReceiptinfo() {
        return receiptinfo;
    }

    public void setReceiptinfo(String receiptinfo) {
        this.receiptinfo = receiptinfo;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
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
        if (!(object instanceof Transactions)) {
            return false;
        }
        Transactions other = (Transactions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mobitill.citirevenue.entity.Transactions[ id=" + id + " ]";
    }
    
}
