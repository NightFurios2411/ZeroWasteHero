package com.example.zerowastehero.Main.Community.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.DataBinding.Model.ReplyModel;
import com.example.zerowastehero.R;

import java.util.ArrayList;

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Constants for view types
    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_REPLY = 1;

    private Context context;
    private ArrayList<ReplyModel> replyModels;
    private PostModel postModel;

    public ReplyAdapter(Context context, ArrayList<ReplyModel> replyModels, PostModel postModel) {
        this.context = context;
        this.replyModels = replyModels;
        this.postModel = postModel;
    }

    @Override
    public int getItemViewType(int position) {
        // Position 0 will display the Post item
        if (position == 0) return VIEW_TYPE_POST;
        else return VIEW_TYPE_REPLY;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate Post card for VIEW_TYPE_POST
        if (viewType == VIEW_TYPE_POST) {
            return new PostViewHolder(inflater.inflate(R.layout.item_post_card, parent, false));
        }
        // Inflate Reply card for VIEW_TYPE_REPLY
        else {
            return new ReplyViewHolder(inflater.inflate(R.layout.item_reply_card, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        // Bind Post data
        if (viewType == VIEW_TYPE_POST) {
            PostViewHolder postViewHolder = (PostViewHolder) holder;
            postViewHolder.TVPostTitle.setText(postModel.getPostTitle());
            postViewHolder.TVPostDescription.setText(postModel.getPostDescription());
        }

        // Bind Reply data
        if (viewType == VIEW_TYPE_REPLY) {
            int adjustedPosition = position - 1; // Adjust for the Post card
            ReplyModel reply = replyModels.get(adjustedPosition);
            ReplyViewHolder replyViewHolder = (ReplyViewHolder) holder;
            replyViewHolder.TVReplyDescription.setText(reply.getReplyDescription());
        }
    }

    @Override
    public int getItemCount() {
        return Math.max(1, replyModels.size() + 1); // +1 for the Post item
    }

    // ViewHolder for Post data
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView TVPostTitle, TVPostDescription;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            TVPostTitle = itemView.findViewById(R.id.TVPostTitle);
            TVPostDescription = itemView.findViewById(R.id.TVPostDescription);
        }
    }

    // ViewHolder for Reply data
    public static class ReplyViewHolder extends RecyclerView.ViewHolder {
        private TextView TVReplyDescription;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            TVReplyDescription = itemView.findViewById(R.id.TVReplyDescription);
        }
    }
}
