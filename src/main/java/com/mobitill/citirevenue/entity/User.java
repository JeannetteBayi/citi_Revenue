/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Francis
 */
@Entity
@Table(name = "users", catalog = "mobitill_demo_revenue", schema = "temp")
@XmlRootElement
@JsonIgnoreProperties({"hashedPassword","roleList","pin"})
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u ORDER BY u.id")
    , @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id")
    , @NamedQuery(name = "User.findByUsername", query = "SELECT CASE WHEN(COUNT(u.id) > 0L) THEN TRUE ELSE FALSE END FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    , @NamedQuery(name = "User.findUsername", query = "SELECT u FROM User u INNER JOIN u.roleList r WHERE u.username = :username")
    , @NamedQuery(name = "User.findByHashvalue", query = "SELECT u FROM User u WHERE u.hashedPassword = :hashvalue"),
    @NamedQuery(name = "User.findPinByUsername", query = "SELECT u.pin FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByUsernameHashValue", query = "SELECT u FROM User u WHERE u.username = :username AND u.hashedPassword = :passhash")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",updatable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull(message = "field username cannot be empty")
    @Size(min = 6, max = 20,message = "required username length {min}-{max}")
    @Column(name = "username",updatable = false)
    private String username;
    
    @Size(min = 8,max = 20,message = "required password length {min}-{max}")
    @Transient
    private String password;
    @Basic(optional = false)
    @Size(min = 0, max = 255)
    @Column(name = "password")
    private String hashedPassword;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appuser")
    private List<Role> roleList;
    
    @Column(name = "pin")
    private String pin;
    
    @Column(name = "email")
    private String email;
    
    @Transient
    private String role;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String username, String password, String hashvalue) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.hashedPassword = hashvalue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @XmlTransient
    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mobitill.citirevenue.entity.User[ id=" + id + " ]";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
