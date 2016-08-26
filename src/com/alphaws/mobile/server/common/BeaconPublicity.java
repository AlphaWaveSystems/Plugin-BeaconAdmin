/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphaws.mobile.server.common;

/**
 *
 * @author patrick
 */
public class BeaconPublicity {
    
    private Integer beaconId    = null;
    private Integer type        = null;
    private String  action      = null;
    private Long    startDate   = null;
    private Long    endDate     = null;
    private String name     = null;
    private String titlePublicity     = null;
    private String imagePath     = null;
    private String imageName     = null;
    private Integer idCompany     = null;
    private String detail     = null;
    private Integer idMarketing     = null; 
    private Company company = null;
    private Boolean hasPublicity = Boolean.FALSE;
    private String  alias = null;
    private String  uuid = null;
    private Integer major = null;
    private Integer minor = null;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getBeaconString() {
        return alias;
    }
    
    public void setHasPublicity(Boolean hasPublicity) {
        this.hasPublicity = hasPublicity;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitlePublicity() {
        return titlePublicity;
    }

    public void setTitlePublicity(String titlePublicity) {
        this.titlePublicity = titlePublicity;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Integer idCompany) {
        this.idCompany = idCompany;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getIdMarketing() {
        return idMarketing;
    }

    public void setIdMarketing(Integer idMarketing) {
        this.idMarketing = idMarketing;
    }

    
    
    
    public Integer getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(Integer beaconId) {
        this.beaconId = beaconId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return action + "|" + beaconId + "|" + startDate + "|" + endDate + "|" + type; 
    }

    
}
