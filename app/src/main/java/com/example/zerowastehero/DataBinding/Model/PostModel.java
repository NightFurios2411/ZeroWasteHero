package com.example.zerowastehero.DataBinding.Model;

import com.google.firebase.Timestamp;

public class PostModel {

    private String postID; // PK
    private String userID; // FK
    private String userName;
    private Timestamp createdAt;
    private String postDescription;
    private String postImageURL;
    private String proofBeforeImageURL;
    private String proofAfterImageURL;
    private String postType;
    private int likeCount;
    private int replyCount;
    private boolean isBookmarked;

    // No-argument constructor required by Firestore
    public PostModel() {}

    public PostModel(String description, String userID, String userName, String postImageURL, String proofBeforeImageURL, String proofAfterImageURL, String postType, Timestamp createdAt) {
        this.postID = "";
        this.userName = userName;
        this.userID = userID;
        this.postDescription = description;
        this.postImageURL = postImageURL;
        this.proofBeforeImageURL = proofBeforeImageURL;
        this.proofAfterImageURL = proofAfterImageURL;
        this.postType = postType;
        this.createdAt = createdAt;
        this.likeCount = 0;
        this.replyCount = 0;
        this.isBookmarked = false;
    }

    public String getPostID() { return postID; }
    public void setPostID(String postID) { this.postID = postID; }
    public String getUserID() { return userID; }
    public String getUserName() { return userName; }
    public String getPostDescription() { return postDescription; }
    public void setPostDescription(String postDescription) { this.postDescription = postDescription; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setPostImageURL(String postImageURL) { this.postImageURL = postImageURL; }
    public String getPostImageURL() { return postImageURL; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public int getReplyCount() { return replyCount; }
    public void setReplyCount(int replyCount) { this.replyCount = replyCount; }
    public boolean isBookmarked() { return isBookmarked; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }
    public void setUserID(String userID) { this.userID = userID; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public String getProofBeforeImageURL() { return proofBeforeImageURL; }
    public void setProofBeforeImageURL(String proofBeforeImageURL) { this.proofBeforeImageURL = proofBeforeImageURL; }
    public String getProofAfterImageURL() { return proofAfterImageURL; }
    public void setProofAfterImageURL(String proofAfterImageURL) { this.proofAfterImageURL = proofAfterImageURL; }
    public String getPostType() { return postType; }
    public void setPostType(String postType) { this.postType = postType; }
}
