package com.example.zerowastehero.DataBinding.Model;

import com.google.firebase.Timestamp;

public class ReportModel {

    private String reportID; // PK
    private String title;
    private String description;
    private String userID;
    private String userName;
    private String status;
    private String[] imageURLs;
    private Timestamp createdAt;

    // No-argument constructor required by Firestore
    public ReportModel() {}

    public ReportModel(String title, String description, String userID, String userName, String status, String[] imageURLs, Timestamp createdAt) {
        this.reportID = "";
        this.title = title;
        this.description = description;
        this.userID = userID;
        this.userName = userName;
        this.status = status;
        this.imageURLs = imageURLs;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(String[] imageURLs) {
        this.imageURLs = imageURLs;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }
}
