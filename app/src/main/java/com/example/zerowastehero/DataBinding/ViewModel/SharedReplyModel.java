package com.example.zerowastehero.DataBinding.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zerowastehero.DataBinding.Model.ReplyModel;

import java.util.List;

public class SharedReplyModel extends ViewModel {
    private MutableLiveData<List<ReplyModel>> replies = new MutableLiveData<>();

    public LiveData<List<ReplyModel>> getReplies() { return replies; }
    public void setReplies(List<ReplyModel> newReplies) { replies.setValue(newReplies); }
}
