/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Francis
 */
@XmlRootElement
@JsonIgnoreProperties({"createstamp"})
public class MerchantCredentials extends Merchant{
    
    private static final long serialVersionUID = 1L;
    @Size(max = 20,message = "username maximum length is 20 characters")
    @Transient
    private String username;
    @Size(max = 20,message = "password maximum length is 20 characters")
    @Transient
    private String password;
    @Transient
    @NotNull(message = "email address cannot be null")
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
