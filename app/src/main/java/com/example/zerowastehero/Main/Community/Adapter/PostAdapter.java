package com.example.zerowastehero.Main.Community.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zerowastehero.DataBinding.Model.LikeModel;
import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.Main.Community.Interface.PostInterface;
import com.example.zerowastehero.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    // Constants for view types
    private static final int VIEW_TYPE_CHALLENGE = 0;
    private static final int VIEW_TYPE_TIPS = 1;
    private static final int VIEW_TYPE_POST = 2;

    private PostInterface postInterface;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private Context context;
    private static ArrayList<PostModel> postModels;
    private AlertDialog.Builder builder;

    public PostAdapter(Context context, ArrayList<PostModel> postsModels, PostInterface postInterface) {
        this.context = context;
        postModels = postsModels;
        this.postInterface = postInterface;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
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

            String currentUserID = mAuth.getCurrentUser().getUid();

            // Bind image to post
            holder.IVPostImage.setVisibility(View.GONE);
            String postImageURL = post.getPostImageURL(); // Assuming getPostImageURL() returns the image URL
            if (postImageURL != null && !postImageURL.isEmpty()) {
                // Load the image using Glide
                holder.IVPostImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(postImageURL)
                        .into(holder.IVPostImage);
            }

            // Check if the user has liked this post
            CollectionReference likesRef = db.collection("likes");
            String likeDocId = post.getPostID() + "_" + currentUserID;

            likesRef.document(likeDocId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && task.getResult().exists()) {
                                holder.IVPostLike.setImageResource(R.drawable.icon_heart_liked); // Set as liked
                            } else {
                                holder.IVPostLike.setImageResource(R.drawable.icon_heart_empty); // Set as unliked
                            }
                        } else {
                            // Default to unliked state in case of error
                            holder.IVPostLike.setImageResource(R.drawable.icon_heart_empty);
                        }
                    });

            // Fetch and update reply count
            CollectionReference repliesRef = db.collection("posts").document(post.getPostID()).collection("replies");
            repliesRef.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot repliesSnapshot = task.getResult();
                            if (repliesSnapshot != null) {
                                int replyCount = post.getReplyCount();
                                post.setReplyCount(replyCount);
                                holder.TVPostReplyCount.setText(String.valueOf(replyCount));
                                System.out.println(replyCount);
                            }
                        } else {
                            // Handle failure (optional)
                            Toast.makeText(context, "Failed to fetch reply count", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Navigate to the next fragment when TVPostDescription is clicked
            holder.TVPostDescription.setOnClickListener(v -> {
                if (postInterface != null) {
                    postInterface.onPostClick(adjustedPosition);
                }
            });

            // Set click listener for like button
            holder.IVPostLike.setOnClickListener(v -> toggleLike(post, holder, currentUserID));
        }
        // No binding needed for VIEW_TYPE_CHALLENGE and VIEW_TYPE_TIPS
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the RecyclerView
        return Math.max(2, postModels.size() + 2);
    }

    private void toggleLike(PostModel post, PostViewHolder holder, String userID) {
        CollectionReference likesRef = db.collection("likes");
        DocumentReference postRef = db.collection("posts").document(post.getPostID());

        // Generate a unique document ID using postId and userId
        String likeDocId = post.getPostID() + "_" + userID;

        // Disable the like button to prevent multiple clicks
        holder.IVPostLike.setEnabled(false);

        likesRef.document(likeDocId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            // User has already liked the post; remove the like
                            likesRef.document(likeDocId).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        int newLikeCount = post.getLikeCount() - 1;
                                        post.setLikeCount(newLikeCount);
                                        holder.TVPostLikeCount.setText(String.valueOf(newLikeCount));
                                        holder.IVPostLike.setImageResource(R.drawable.icon_heart_empty);

                                        // Update likeCount in Firestore
                                        postRef.update("likeCount", newLikeCount)
                                                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update like count", Toast.LENGTH_SHORT).show());

                                        holder.IVPostLike.setEnabled(true);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Failed to unlike", Toast.LENGTH_SHORT).show();
                                        holder.IVPostLike.setEnabled(true);
                                    });
                        } else {
                            // User has not liked the post; add a like
                            LikeModel like = new LikeModel(post.getPostID(), userID, new Timestamp(new Date()));
                            likesRef.document(likeDocId).set(like)
                                    .addOnSuccessListener(aVoid -> {
                                        int newLikeCount = post.getLikeCount() + 1;
                                        post.setLikeCount(newLikeCount);
                                        holder.TVPostLikeCount.setText(String.valueOf(newLikeCount));
                                        holder.IVPostLike.setImageResource(R.drawable.icon_heart_liked);

                                        // Update likeCount in Firestore
                                        postRef.update("likeCount", newLikeCount)
                                                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update like count", Toast.LENGTH_SHORT).show());

                                        holder.IVPostLike.setEnabled(true);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Failed to like", Toast.LENGTH_SHORT).show();
                                        holder.IVPostLike.setEnabled(true);
                                    });
                        }
                    } else {
                        Toast.makeText(context, "Failed to process like action", Toast.LENGTH_SHORT).show();
                        holder.IVPostLike.setEnabled(true);
                    }
                });
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        // Define views in the item layout

        private TextView TVPostDescription, TVPostDate, TVUserName, TVPostLikeCount, TVPostReplyCount;
        private ImageView IVPostLike, IVPostImage;

        public PostViewHolder(@NonNull View itemView, PostInterface postInterface) {
            super(itemView);

            // Initialize views in the item layout
            TVPostDescription = itemView.findViewById(R.id.TVPostDescription);
            TVUserName = itemView.findViewById(R.id.TVUserName);
            TVPostDate = itemView.findViewById(R.id.TVPostDate);
            IVPostLike = itemView.findViewById(R.id.IVPostLike);
            TVPostLikeCount = itemView.findViewById(R.id.TVPostLikeCount);
            TVPostReplyCount = itemView.findViewById(R.id.TVPostReplyCount);
            IVPostImage = itemView.findViewById(R.id.IVPostImage);
        }

        public void postBind(PostModel post) {
            TVPostDescription.setText(post.getPostDescription());
            TVUserName.setText(post.getUserName());
            TVPostDate.setText(setDateFormatted(post.getCreatedAt()));
            TVPostLikeCount.setText(String.valueOf(post.getLikeCount()));
            TVPostReplyCount.setText(String.valueOf(post.getReplyCount()));
        }

        public String setDateFormatted(Timestamp createdAt) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            return sdf.format(createdAt.toDate());
        }
    }
}