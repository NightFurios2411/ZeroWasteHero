package com.example.zerowastehero.DataBinding.Model;

public class PostModel {
    public String postTitle;
    public String postDescription;

    public PostModel() {}

    public PostModel(String title, String description) {
        this.postTitle = title;
        this.postDescription = description;
    }

    public String getPostTitle() { return postTitle; }
    public String getPostDescription() { return postDescription; }
}
