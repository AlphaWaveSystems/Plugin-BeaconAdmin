/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphaws.mobile.server.common;

import java.util.ArrayList;

/**
 *
 * @author patrick
 */
public class Location {
    
    private int cid;
    private double lat;
    private double lng;
    private String name;
    private String direction;

    public Location(int cid, double lat, double lng, String name, String direction) {
        this.cid = cid;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.direction = direction;
    }

  

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
   
    
    
    
}
