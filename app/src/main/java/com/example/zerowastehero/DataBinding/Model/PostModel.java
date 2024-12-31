package com.example.zerowastehero.DataBinding.Model;

import java.util.UUID;

public class PostModel {

    private final String postID; // PK
    private final String userID; // FK
    private final String userName;
    private String postTitle;
    private String postDescription;
    private final String createdAt;

    public PostModel(String title, String description, String userID, String userName) {
        this.postID = UUID.randomUUID().toString();
        this.userName = userName;
        this.userID = userID;
        this.postTitle = title;
        this.postDescription = description;
        this.createdAt = String.valueOf(System.currentTimeMillis());
    }

    public String getPostID() { return postID; }
    public String getUserID() { return userID; }
    public String getPostTitle() { return postTitle; }
    public String getUserName() { return userName; }
    public void setPostTitle(String postTitle) { this.postTitle = postTitle; }
    public String getPostDescription() { return postDescription; }
    public void setPostDescription(String postDescription) { this.postDescription = postDescription; }
    public String getCreatedAt() { return createdAt; }

}
