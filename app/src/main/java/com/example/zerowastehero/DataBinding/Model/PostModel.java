package com.example.zerowastehero.DataBinding.Model;

public class PostModel {
    private String mTitle;
    private String mDescription;

    public PostModel() {}

    public PostModel(String title, String description) {
        this.mTitle = title;
        this.mDescription = description;
    }

    public String getmTitle() { return mTitle; }

    public void setmTitle(String mTitle) { this.mTitle = mTitle; }

    public String getmDescription() { return mDescription; }

    public void setmDescription(String mDescription) { this.mDescription = mDescription; }
}
