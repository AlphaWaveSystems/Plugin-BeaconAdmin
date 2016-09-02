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
public class Campaign {

    private Integer id;
    private Integer id_company;
    private String name;
    private String notification;
    private String image;
    private String detail;
    private String short_url;
    private String create_date;
    private String update_date;
    private String start_date;
    private String end_date;
    private Integer relationId;
    

    public Campaign(Integer id, Integer id_company, String name, String notification, String image, String detail, String short_url, String create_date, String update_date) {
        this.id = id;
        this.id_company = id_company;
        this.name = name;
        this.notification = notification;
        this.image = image;
        this.detail = detail;
        this.short_url = short_url;
        this.create_date = create_date;
        this.update_date = update_date;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getShort_url() {
        return short_url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setRelationsID(Integer id) {
        this.relationId = id;
    }
    
    public Integer getRelationsID() {
        return this.relationId;
    }
    
    

}
