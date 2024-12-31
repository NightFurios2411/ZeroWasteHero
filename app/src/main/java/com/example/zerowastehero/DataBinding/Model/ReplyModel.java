package com.example.zerowastehero.DataBinding.Model;

import java.util.UUID;

public class ReplyModel {

    private final String replyID; // PK
    private final String postID; // FK
    private final String userID; // FK
    private String replyDescription;
    private String createdAt;

    public ReplyModel(String description, String postID, String userID) {
        this.replyID = UUID.randomUUID().toString();
        this.postID = postID;
        this.userID = userID;
        this.replyDescription = description;
        this.createdAt = String.valueOf(System.currentTimeMillis());
    }

    public String getReplyDescription() { return replyDescription; }
    public String getCreatedAt() { return createdAt; }
}
