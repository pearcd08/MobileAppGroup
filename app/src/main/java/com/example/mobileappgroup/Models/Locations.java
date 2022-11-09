package com.example.mobileappgroup.Models;

import java.util.List;

public class Locations {
    List<Location> locations;
    public List<Location> getValue(){
        return this.locations;
    }
    public Location getValue(int index){
        return this.locations.get(index);
    }
}
