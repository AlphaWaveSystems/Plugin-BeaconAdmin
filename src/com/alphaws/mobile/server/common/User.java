/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphaws.mobile.server.common;

import java.sql.Timestamp;

/**
 *
 * @author patrick
 */
public class User {
    
    private Integer id = null;
    private String name = null;
    private String user = null;
    private String password = null;
    private Integer profile = null;
    private Company company = null;
    private Timestamp lastLogin = null;
    private Timestamp createDate = null;
    private Timestamp updateDate = null;
    private Boolean success = Boolean.FALSE;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setProfile(Integer profile) {
        this.profile = profile;
    }

    public Integer getProfile() {
        return profile;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public User(Integer id, String name, Integer profile) {
        this.id = id;
        this.name = name;
        this.profile = profile;
    }

    
    
    
}
