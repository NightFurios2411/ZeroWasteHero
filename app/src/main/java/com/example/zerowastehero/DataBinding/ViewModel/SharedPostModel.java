package com.example.zerowastehero.DataBinding.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zerowastehero.DataBinding.Model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class SharedPostModel extends ViewModel {
    private MutableLiveData<List<PostModel>> posts = new MutableLiveData<>();

    public LiveData<List<PostModel>> getPosts() { return posts; }
    public void setPosts(List<PostModel> newPosts) { posts.setValue(newPosts); }
    public void addPost(PostModel newPost) {
        MutableLiveData<List<PostModel>> postsLiveData = (MutableLiveData<List<PostModel>>) getPosts();

        if (postsLiveData.getValue() != null) {
            List<PostModel> updatedPosts = new ArrayList<>(postsLiveData.getValue());
            updatedPosts.add(newPost);
            postsLiveData.setValue(updatedPosts); // Notify observers
        } else {
            List<PostModel> newPosts = new ArrayList<>();
            newPosts.add(newPost);
            postsLiveData.setValue(newPosts); // Initialize LiveData with the new post
        }
    }
}
