package com.example.zerowastehero.DataBinding.Model;

import java.util.UUID;

public class UserModel {

    private String userID; // PK
    private String joinDate;
    private String username;
    private String email;
    private String bio;
    private int point;
    private String profilePictureURL;

    public UserModel() {}

    public UserModel(String username, String bio, String email, String joinDate) {
        this.userID = UUID.randomUUID().toString();
        this.username = username;
        this.bio = bio;
        this.email = email;
        this.joinDate = joinDate;
        point = 0;
        profilePictureURL = "";
    }

    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getJoinDate() { return joinDate; }
    public int getPoint() { return point; }
    public void setPoint(int point) { this.point = point; }
    public String getProfilePictureURL() { return profilePictureURL; }
    public void setProfilePictureURL(String profilePictureURL) { this.profilePictureURL = profilePictureURL; }
}
