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
public class Company {
    private Integer id   = null;
    private String  name = null;
    private String  direction   = null;
    private String  logo        = null;
    private String  color       = null;
    private String  email       = null;
    private Boolean active      = null;
    private Timestamp createDate = null;
    private Timestamp updateDate = null;
    private String  apiKey       = null;

    public Company(Integer idCompany, String name, String dir, String logo, String color, 
            String email, Boolean active, Timestamp createDate, Timestamp updateDate, String apiKey) {
        
        this.id = idCompany;
        this.name = name;
        this.direction = dir;
        this.logo = logo;
        this.color = color;
        this.email = email;
        this.active = active;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }    

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer idCompany) {
        this.id = idCompany;
    }

    public String getName() {
        return name;
    }

    public void setName(String companyName) {
        this.name = companyName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }
    
    
    
    
}
