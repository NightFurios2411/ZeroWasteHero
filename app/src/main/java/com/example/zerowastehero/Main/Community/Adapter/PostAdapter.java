package com.example.zerowastehero.Main.Community.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.Main.Community.Interface.PostInterface;
import com.example.zerowastehero.R;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    // Constants for view types
    private static final int VIEW_TYPE_CHALLENGE = 0;
    private static final int VIEW_TYPE_TIPS = 1;
    private static final int VIEW_TYPE_POST = 2;

    private PostInterface postInterface;

    private Context context;
    private static ArrayList<PostModel> postModels;
    private AlertDialog.Builder builder;

    public PostAdapter(Context context, ArrayList<PostModel> postsModels, PostInterface postInterface) {
        this.context = context;
        postModels = postsModels;
        this.postInterface = postInterface;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_CHALLENGE;
        else if (position == 1) return VIEW_TYPE_TIPS;
        else return VIEW_TYPE_POST;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate Challenge Card
        if (viewType == VIEW_TYPE_CHALLENGE) return new PostViewHolder(inflater.inflate(R.layout.item_challenge_card, parent, false), postInterface);
        // Inflate Tips Card
        else if (viewType == VIEW_TYPE_TIPS) return new PostViewHolder(inflater.inflate(R.layout.item_tips_card, parent, false), postInterface);
        // Inflate Post Card
        else return new PostViewHolder(inflater.inflate(R.layout.item_post_card, parent, false), postInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_CHALLENGE) {
            holder.itemView.setOnClickListener(v -> {
                if (postInterface != null) {
                    postInterface.onChallengeClick();
                }
            });
        } else if (viewType == VIEW_TYPE_TIPS) {
            holder.itemView.setOnClickListener(v -> {
                if (postInterface != null) {
                }
            });
        } else if (viewType == VIEW_TYPE_POST) {
            int adjustedPosition = position - 2; // Offset for Challenge and Tips cards
            PostModel post = postModels.get(adjustedPosition);

            holder.postBind(post);

            holder.itemView.setOnClickListener(v -> {
                if (postInterface != null) {
                    postInterface.onPostClick(adjustedPosition);
                }
            });
        }
        // No binding needed for VIEW_TYPE_CHALLENGE and VIEW_TYPE_TIPS
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the RecyclerView
        return Math.max(2, postModels.size() + 2);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        // Define views in the item layout

        private TextView TVPostTitle, TVPostDescription, TVPostDate, TVUserName;

        public PostViewHolder(@NonNull View itemView, PostInterface postInterface) {
            super(itemView);

            // Initialize views in the item layout
            TVPostTitle = itemView.findViewById(R.id.TVPostTitle);
            TVPostDescription = itemView.findViewById(R.id.TVPostDescription);
            TVUserName = itemView.findViewById(R.id.TVUserName);
            TVPostDate = itemView.findViewById(R.id.TVPostDate);

            itemView.setOnClickListener(v -> {
                if (postInterface != null) {
                    int position = getAbsoluteAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        postInterface.onPostClick(position);
                    }
                }
            });
        }

        public void postBind(PostModel post) {
            TVPostTitle.setText(post.getPostTitle());
            TVPostDescription.setText(post.getPostDescription());
            TVUserName.setText(post.getUserName());
            TVPostDate.setText(setDateFormatted(post.getCreatedAt()));
        }

        public String setDateFormatted(Timestamp createdAt) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            return sdf.format(createdAt.toDate());
        }
    }
}