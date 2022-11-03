package com.example.mobileappgroup.Models;

public class User {

    private String UID, email, username, profileURL;

    public User() {

    }

    public User(String UID, String email, String username, String profileURL) {
        this.UID = UID;
        this.email = email;
        this.username = username;
        this.profileURL = profileURL;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
