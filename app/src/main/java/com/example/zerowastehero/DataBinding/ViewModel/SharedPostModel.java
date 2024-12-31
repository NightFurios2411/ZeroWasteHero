package com.example.zerowastehero.DataBinding.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zerowastehero.DataBinding.Model.PostModel;

import java.util.List;

public class SharedPostModel extends ViewModel {
    private MutableLiveData<List<PostModel>> posts = new MutableLiveData<>();

    public LiveData<List<PostModel>> getPosts() { return posts; }
    public void setPosts(List<PostModel> newPosts) { posts.setValue(newPosts); }
}
