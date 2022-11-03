package com.example.mobileappgroup.Models;

import java.util.HashMap;

public class Location {
    //          "place_id": "333050836868",
////            "osm_type": "node",
////            "osm_id": "8855790143",
////            "licence": "https://locationiq.com/attribution",
////            "lat": "-36.910879",
////            "lon": "174.674828",
////            "display_name": "2, Arawa Street, New Lynn, Auckland, Auckland Region, New Zealand",
////            "boundingbox": [
////        "-36.910879",
////                "-36.910879",
////                "174.674828",
////                "174.674828"
////    ],
////        "importance": 0.2,
////            "address": {
////        "house_number": "2",
////                "road": "Arawa Street",
////                "city": "New Lynn",
////                "county": "Auckland",
////                "state": "Auckland Region",
////                "country": "New Zealand",
////                "country_code": "nz"
////    }
////    }

    HashMap<String, Object> address;
    Double lon; // longitude
    Double lat; // latitude
    String house_number;
    String road;
    String city;
    String county;
    String state;
    String country;
    String country_code;

    public Location(){}

    public Location(HashMap<String, Object> address){
        this.address = address;
    }

    public Location(Double lon, Double lat, String house_number, String road, String city, String county,
                    String state, String country, String country_code) {
        this.lon = lon;
        this.lat = lat;
        this.house_number = house_number;
        this.road = road;
        this.city = city;
        this.county = county;
        this.state = state;
        this.country = country;
        this.country_code = country_code;
    }

    public HashMap<String, Object> getAddress() {
        return address;
    }

    public void setAddress(HashMap<String, Object> address) {
        this.address = address;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
