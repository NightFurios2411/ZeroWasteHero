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
    private boolean isPointClaimed1, isPointClaimed2, isPointClaimed3;
    private Integer recycleProofCount, trashCollectProofCount;

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
        isPointClaimed1 = false;
        isPointClaimed2 = false;
        isPointClaimed3 = false;
        recycleProofCount = 0;
        trashCollectProofCount = 0;
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
    public boolean isPointClaimed1() { return isPointClaimed1; }
    public void setPointClaimed1(boolean pointClaimed1) { isPointClaimed1 = pointClaimed1; }
    public boolean isPointClaimed2() { return isPointClaimed2; }
    public void setPointClaimed2(boolean pointClaimed2) { isPointClaimed2 = pointClaimed2; }
    public boolean isPointClaimed3() { return isPointClaimed3; }
    public void setPointClaimed3(boolean pointClaimed3) { isPointClaimed3 = pointClaimed3; }
    public Integer getRecycleProofCount() { return recycleProofCount; }
    public void setRecycleProofCount(Integer recycleProofCount) { this.recycleProofCount = recycleProofCount; }
    public Integer getTrashCollectProofCount() { return trashCollectProofCount; }
    public void setTrashCollectProofCount(Integer trashCollectProofCount) { this.trashCollectProofCount = trashCollectProofCount; }
}
