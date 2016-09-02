/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphaws.mobile.server.common;

/**
 *
 * @author patrick
 */
public class Relation {
    private Integer id;
    private Integer id_company;
    private Integer id_campaign;
    private Integer id_beacon;
    private Integer dist;
    private String start_date;
    private String end_date;

    public Relation(Integer id, Integer id_company, Integer id_campaign, Integer id_beacon, Integer dist, String start_date, String end_date) {
        this.id = id;
        this.id_company = id_company;
        this.id_campaign = id_campaign;
        this.id_beacon = id_beacon;
        this.dist = dist;
        this.start_date = start_date;
        this.end_date = end_date;
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

    public Integer getId_campaign() {
        return id_campaign;
    }

    public void setId_campaign(Integer id_campaign) {
        this.id_campaign = id_campaign;
    }

    public Integer getId_beacon() {
        return id_beacon;
    }

    public void setId_beacon(Integer id_beacon) {
        this.id_beacon = id_beacon;
    }

    public Integer getDist() {
        return dist;
    }

    public void setDist(Integer dist) {
        this.dist = dist;
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
    
    
    
    
}
