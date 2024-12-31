package com.example.zerowastehero.DataBinding.Model;

import java.util.UUID;

public class UserModel {

    private final String userID; // PK
    private String username;
    private String email;
    private String bio;
    private final String createdAt;

    public UserModel(String username, String bio, String email) {
        this.userID = UUID.randomUUID().toString();
        this.username = username;
        this.bio = bio;
        this.email = email;
        this.createdAt = String.valueOf(System.currentTimeMillis());
    }

    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCreatedAt() { return createdAt; }

}
