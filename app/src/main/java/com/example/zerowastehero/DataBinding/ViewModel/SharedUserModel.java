package com.example.zerowastehero.DataBinding.ViewModel;

import androidx.lifecycle.ViewModel;

import com.example.zerowastehero.DataBinding.Model.UserModel;

import java.util.ArrayList;

public class SharedUserModel extends ViewModel {
    private ArrayList<UserModel> users = new ArrayList<>();

    

    public ArrayList<UserModel> getUsers() { return users; }
    public void setUsers(ArrayList<UserModel> users) { this.users = users; }
}
