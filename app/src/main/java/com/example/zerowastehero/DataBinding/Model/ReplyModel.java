package com.example.zerowastehero.DataBinding.Model;

import com.google.firebase.Timestamp;

public class ReplyModel {

    private String replyID; // PK
    private String postID; // FK
    private String userID; // FK
    private String userName;
    private Timestamp createdAt;
    private String replyDescription;
    private int likeCount;

    public ReplyModel() {}

    public ReplyModel(String description, String postID, String userID, String userName, Timestamp createdAt) {
        this.replyDescription = description;
        this.postID = postID;
        this.userID = userID;
        this.userName = userName;
        this.createdAt = createdAt;
        this.likeCount = 0;
    }

    public String getReplyDescription() { return replyDescription; }
    public Timestamp getCreatedAt() { return createdAt; }
    public String getReplyID() { return replyID; }
    public String getPostID() { return postID; }
    public String getUserID() { return userID; }
    public String getUserName() { return userName; }
    public void setReplyDescription(String replyDescription) { this.replyDescription = replyDescription; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public void setReplyID(String replyID) { this.replyID = replyID; }
    public void setPostID(String postID) { this.postID = postID; }
    public void setUserID(String userID) { this.userID = userID; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
