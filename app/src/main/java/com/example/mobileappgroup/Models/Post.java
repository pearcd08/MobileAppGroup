package com.example.mobileappgroup.Models;

public class Post {


    private String username, userID, location, blurb, imageURL, profileURL;
    Double longitude, latitude;


    public Post() {

    }


    public Post(String username, String userID, String location, String blurb, String postImageURL, String profileURL, Double longitude, Double latitude) {
        this.username = username;
        this.userID = userID;
        this.location = location;
        this.blurb = blurb;
        this.imageURL = postImageURL;
        this.profileURL = profileURL;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setPostImageURL(String postImageURL) {
        this.imageURL = postImageURL;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

}
