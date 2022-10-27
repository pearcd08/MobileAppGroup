package com.example.mobileappgroup.Models;

public class Post {

    private String username, location, blurb, imageURL, profileURL;

    public Post() {

    }

    public Post(String username, String location, String blurb, String postImageURL, String profileURL) {
        this.username = username;
        this.location = location;
        this.blurb = blurb;
        this.imageURL = postImageURL;
        this.profileURL = profileURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
