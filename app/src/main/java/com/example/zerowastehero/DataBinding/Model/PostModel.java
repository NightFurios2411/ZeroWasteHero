package com.example.zerowastehero.DataBinding.Model;

public class PostModel {
    private String postTitle;
    private String postDescription;
    private String postDate;

    public PostModel() {}

    public PostModel(String title, String description) {
        this.postTitle = title;
        this.postDescription = description;
    }

    public String getPostTitle() { return postTitle; }
    public String getPostDescription() { return postDescription; }
    public String getPostDate() { return postDate; }
}
