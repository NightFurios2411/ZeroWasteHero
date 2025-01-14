package com.example.zerowastehero.DataBinding.Model;

import java.util.UUID;

public class UserModel {

    private String userID; // PK
    private String joinDate;
    private String username;
    private String email;
    private String bio;
    private Integer point;
    private Integer totalPoint;
    private Integer totalTrash;
    private Integer totalRecycle;
    private String profilePictureURL;

    public UserModel() {}

    public UserModel(String username, String bio, String email, String joinDate) {
        this.userID = UUID.randomUUID().toString();
        this.username = username;
        this.bio = bio;
        this.email = email;
        this.joinDate = joinDate;
        point = 0;
        totalPoint = 0;
        totalTrash = 0;
        totalRecycle = 0;
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
    public Integer getTotalPoint() { return totalPoint; }
    public void setTotalPoint(Integer totalPoint) { this.totalPoint = totalPoint; }
    public Integer getTotalTrash() { return totalTrash; }
    public void setTotalTrash(Integer totalTrash) { this.totalTrash = totalTrash; }
    public Integer getTotalRecycle() { return totalRecycle; }
    public void setTotalRecycle(Integer totalRecycle) { this.totalRecycle = totalRecycle; }
}
