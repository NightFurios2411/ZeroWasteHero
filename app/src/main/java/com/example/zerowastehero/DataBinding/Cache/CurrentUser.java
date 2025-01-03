package com.example.zerowastehero.DataBinding.Cache;

import com.example.zerowastehero.DataBinding.Model.UserModel;

public class CurrentUser {
    private static CurrentUser instance;
    private UserModel user;

    private CurrentUser() {}

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public void setUser(UserModel user) { this.user = user; }
    public UserModel getUser() { return user; }
}
