package com.example.zerowastehero.DataBinding.Model;

public class ReplyModel {
    private String replyDescription;
    private String replyDate;

    public ReplyModel() {}

    public ReplyModel(String description) {
        this.replyDescription = description;
    }

    public String getReplyDescription() { return replyDescription; }
    public String getReplyDate() { return replyDate; }
}
