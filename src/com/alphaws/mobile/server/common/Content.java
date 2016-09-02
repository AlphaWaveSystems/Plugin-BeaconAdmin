/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphaws.mobile.server.common;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author patrick
 */
public class Content {

    private Collection<Branch> locations; 

    public Content(Collection<Branch> locations) {
        this.locations = locations;
    }
    
    public Collection<Branch> getLocations() {
        return locations;
    }

    public void setLocations(Collection<Branch> locations) {
        this.locations = locations;
    }

    
    
}
