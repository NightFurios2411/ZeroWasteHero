package com.example.zerowastehero.DataBinding.Model;

public class PostModel {
    public String title;
    public String description;

    public PostModel() {}

    public PostModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
}
