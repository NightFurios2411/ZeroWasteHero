package com.example.zerowastehero.DataBinding.Model;

import com.google.firebase.Timestamp;

public class ProofModel {
    private String proofID;
    private String userID;
    private String userName;
    private String imageURL;
    private String habitType;
    private Timestamp createdAt;

    public ProofModel() {}

    public ProofModel(String userID, String userName, String imageURL, Timestamp createdAt, String habitType) {
        this.proofID = "";
        this.userID = userID;
        this.userName = userName;
        this.imageURL = imageURL;
        this.createdAt = createdAt;
        this.habitType = habitType;
    }

    public String getHabitType() {
        return habitType;
    }

    public void setHabitType(String habitType) {
        this.habitType = habitType;
    }

    public String getProofID() {
        return proofID;
    }

    public void setProofID(String proofID) {
        this.proofID = proofID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
