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
public class Beacon {
    
  private Integer id = null;
  private Integer id_company = null;
  private Integer id_location = null;
  private String code = null;
  private String name = null;
  private Integer type = null;
  private String uuid = null;
  private Integer major = null;
  private Integer minor = null;
  private String tx = null;
  private String version = null;
  private Timestamp update_date = null;
  private Integer battery = null;

  public Beacon(Integer id, Integer idComp, Integer idLoc, String code, String name,
          Integer type, String uuid, Integer major, Integer minor, String tx,
          String version, Timestamp update_date, Integer battery) {
  
      this.id = id;
      this.id_company = idComp;
      this.id_location = idLoc;
      this.code = code;
      this.name = name;
      this.type = type;
      this.uuid = uuid;
      this.major = major;
      this.minor = minor;
      this.tx = tx;
      this.version = version;
      this.update_date = update_date;
      this.battery = battery;
      
  }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_company() {
        return id_company;
    }

    public void setId_company(Integer id_company) {
        this.id_company = id_company;
    }

    public Integer getId_location() {
        return id_location;
    }

    public void setId_location(Integer id_location) {
        this.id_location = id_location;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

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

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Timestamp getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Timestamp update_date) {
        this.update_date = update_date;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }
  
    
}
