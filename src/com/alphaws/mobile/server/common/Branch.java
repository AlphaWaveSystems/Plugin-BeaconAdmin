/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphaws.mobile.server.common;

import java.util.ArrayList;

/**
 *
 * @author patrick
 */
public class Branch {
    
    private Integer id;
    private Integer id_company;
    private String name;
    private String direction;
    private Integer lat;
    private Integer lng;
    private String update_date;
    private ArrayList<Beacon> beacons = new ArrayList<>();

    public Branch(Integer id, Integer id_company, String name, String direction, Integer lat, Integer lng, String update_date) {
        this.id = id;
        this.id_company = id_company;
        this.name = name;
        this.direction = direction;
        this.lat = lat;
        this.lng = lng;
        this.update_date = update_date;
    }

    public void addBeacon(Beacon b){
        if(beacons == null){
            beacons = new ArrayList<>();
        }
        
        beacons.add(b);
    }
    
      public ArrayList<Beacon> getBeacons() {
        return beacons;
    }

    public void setBeacons(ArrayList<Beacon> beacons) {
        this.beacons = beacons;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getLat() {
        return lat;
    }

    public void setLat(Integer lat) {
        this.lat = lat;
    }

    public Integer getLng() {
        return lng;
    }

    public void setLng(Integer lng) {
        this.lng = lng;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    


    
}
