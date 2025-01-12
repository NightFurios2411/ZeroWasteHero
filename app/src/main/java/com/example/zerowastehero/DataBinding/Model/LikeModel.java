package com.example.zerowastehero.DataBinding.Model;

import com.google.firebase.Timestamp;

public class LikeModel {
    private String userID;
    private String postID;
    private Timestamp createdAt;

    public LikeModel() {}

    public LikeModel(String userID, String postID, Timestamp createdAt) {
        this.userID = userID;
        this.postID = postID;
        this.createdAt = createdAt;
    }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public String getPostID() { return postID; }
    public void setPostID(String postID) { this.postID = postID; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

}
