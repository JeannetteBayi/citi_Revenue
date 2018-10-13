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
@Table(name = "message", catalog = "mobitill_demo_revenue", schema = "temp")
@XmlRootElement
@JsonIgnoreProperties({"id","stamp"})
@NamedQueries({
    @NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m ORDER BY m.id DESC")
    , @NamedQuery(name = "Message.findByPin", query = "SELECT m FROM Message m WHERE LOWER(m.pin) = LOWER(:pin) ORDER BY m.id DESC")})
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @NotNull(message = "subject can not be null")
    @Size(min = 2,message = "subject minimum length 2 characters")
    @Column(name = "subject")
    private String subject;
    @NotNull(message = "subject can not be null")
    @Size(max = 11,message = "pin max length 11 characters")
    @Column(name = "pin")
    private String pin;
    @NotNull(message = "content can not be null")
    @Size(min = 2,message = "content minimum length 2 characters")
    @Column(name = "content")
    private String content;
    @Column(name = "stamp")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date stamp;
    
    @Transient
    private String timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getStamp() {
        return stamp;
    }

    public void setStamp(Date stamp) {
        this.stamp = stamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    
}
