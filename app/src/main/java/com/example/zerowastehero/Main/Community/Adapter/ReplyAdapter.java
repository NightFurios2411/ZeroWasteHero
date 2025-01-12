package com.example.zerowastehero.Main.Community.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zerowastehero.DataBinding.Model.LikeModel;
import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.DataBinding.Model.ReplyModel;
import com.example.zerowastehero.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Constants for view types
    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_REPLY = 1;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Context context;
    private ArrayList<ReplyModel> replyModels;
    private PostModel post;

    public ReplyAdapter(Context context, ArrayList<ReplyModel> replyModels, PostModel post) {
        this.context = context;
        this.replyModels = replyModels;
        this.post = post;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    // Method to update the PostModel and notify the adapter
    public void setPost(PostModel newPost) {
        this.post = newPost;
        notifyItemChanged(0); // Refresh the Post item at position 0
    }

    public void setReplyModels(ArrayList<ReplyModel> newReplyModels) {
        this.replyModels = replyModels;
        notifyDataSetChanged();
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
        if (viewType == VIEW_TYPE_POST) return new PostViewHolder(inflater.inflate(R.layout.item_post_card, parent, false));
        // Inflate Reply card for VIEW_TYPE_REPLY
        else return new ReplyViewHolder(inflater.inflate(R.layout.item_reply_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (post == null) {
            Log.e("ReplyAdapter", "Post object is null");
            return;
        }

        int viewType = getItemViewType(position);

        // Bind Post data
        if (viewType == VIEW_TYPE_POST) {
            PostViewHolder postViewHolder = (PostViewHolder) holder;

            postViewHolder.postBind(post);

            String currentUserID = mAuth.getCurrentUser().getUid();

            // Check if the user has liked this post
            CollectionReference likesRef = db.collection("likes");
            String likeDocId = post.getPostID() + "_" + currentUserID;

            likesRef.document(likeDocId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && task.getResult().exists()) {
                                postViewHolder.IVPostLike.setImageResource(R.drawable.icon_heart_liked); // Set as liked
                            } else {
                                postViewHolder.IVPostLike.setImageResource(R.drawable.icon_heart_empty); // Set as unliked
                            }
                        } else {
                            // Default to unliked state in case of error
                            postViewHolder.IVPostLike.setImageResource(R.drawable.icon_heart_empty);
                        }
                    });

            // Set click listener for like button
            postViewHolder.IVPostLike.setOnClickListener(v -> togglePostLike(post, postViewHolder, currentUserID));
        }

        // Bind Reply data
        if (viewType == VIEW_TYPE_REPLY) {
            int adjustedPosition = position - 1; // Adjust for the Post card
            ReplyModel reply = replyModels.get(adjustedPosition);
            ReplyViewHolder replyViewHolder = (ReplyViewHolder) holder;

            replyViewHolder.replyBind(reply);

            String currentUserID = mAuth.getCurrentUser().getUid();

            // Check if the user has liked this post
            CollectionReference likesRef = db.collection("likes");
            String likeDocId = reply.getReplyID() + "_" + currentUserID;

            likesRef.document(likeDocId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && task.getResult().exists()) {
                                replyViewHolder.IVReplyLike.setImageResource(R.drawable.icon_heart_liked); // Set as liked
                            } else {
                                replyViewHolder.IVReplyLike.setImageResource(R.drawable.icon_heart_empty); // Set as unliked
                            }
                        } else {
                            // Default to unliked state in case of error
                            replyViewHolder.IVReplyLike.setImageResource(R.drawable.icon_heart_empty);
                        }
                    });

            // Set click listener for like button
            replyViewHolder.IVReplyLike.setOnClickListener(v -> toggleReplyLike(reply, replyViewHolder, currentUserID));
        }
    }

    @Override
    public int getItemCount() {
        return Math.max(1, replyModels.size() + 1); // +1 for the Post item
    }

    private void togglePostLike(PostModel post, PostViewHolder holder, String userID) {
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

    private void toggleReplyLike(ReplyModel reply, ReplyViewHolder holder, String userID) {
        CollectionReference likesRef = db.collection("likes");
        DocumentReference replyRef = db.collection("replies").document(reply.getReplyID());

        // Generate a unique document ID using postId and userId
        String likeDocId = reply.getReplyID() + "_" + userID;

        // Disable the like button to prevent multiple clicks
        holder.IVReplyLike.setEnabled(false);

        likesRef.document(likeDocId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            // User has already liked the post; remove the like
                            likesRef.document(likeDocId).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        int newLikeCount = reply.getLikeCount() - 1;
                                        reply.setLikeCount(newLikeCount);
                                        holder.TVReplyLikeCount.setText(String.valueOf(newLikeCount));
                                        holder.IVReplyLike.setImageResource(R.drawable.icon_heart_empty);

                                        // Update likeCount in Firestore
                                        replyRef.update("likeCount", newLikeCount)
                                                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update like count", Toast.LENGTH_SHORT).show());

                                        holder.IVReplyLike.setEnabled(true);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Failed to unlike", Toast.LENGTH_SHORT).show();
                                        holder.IVReplyLike.setEnabled(true);
                                    });
                        } else {
                            // User has not liked the post; add a like
                            LikeModel like = new LikeModel(reply.getReplyID(), userID, new Timestamp(new Date()));
                            likesRef.document(likeDocId).set(like)
                                    .addOnSuccessListener(aVoid -> {
                                        int newLikeCount = reply.getLikeCount() + 1;
                                        reply.setLikeCount(newLikeCount);
                                        holder.TVReplyLikeCount.setText(String.valueOf(newLikeCount));
                                        holder.IVReplyLike.setImageResource(R.drawable.icon_heart_liked);

                                        // Update likeCount in Firestore
                                        replyRef.update("likeCount", newLikeCount)
                                                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update like count", Toast.LENGTH_SHORT).show());

                                        holder.IVReplyLike.setEnabled(true);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Failed to like", Toast.LENGTH_SHORT).show();
                                        holder.IVReplyLike.setEnabled(true);
                                    });
                        }
                    } else {
                        Toast.makeText(context, "Failed to process like action", Toast.LENGTH_SHORT).show();
                        holder.IVReplyLike.setEnabled(true);
                    }
                });
    }

    // ViewHolder for Post data
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView TVPostDescription, TVUserName, TVPostDate, TVPostLikeCount, TVReplyCount;
        private ImageView  IVPostLike;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            TVPostDescription = itemView.findViewById(R.id.TVPostDescription);
            TVUserName = itemView.findViewById(R.id.TVUserName);
            TVPostDate = itemView.findViewById(R.id.TVPostDate);
            IVPostLike = itemView.findViewById(R.id.IVPostLike);
            TVPostLikeCount = itemView.findViewById(R.id.TVPostLikeCount);
            TVReplyCount = itemView.findViewById(R.id.TVPostReplyCount);
        }

        public void postBind(PostModel post) {
            TVPostDescription.setText(post.getPostDescription());
            TVUserName.setText(post.getUserName());
            TVPostDate.setText(setDateFormatted(post.getCreatedAt()));
            TVPostLikeCount.setText(String.valueOf(post.getLikeCount()));
            TVReplyCount.setText(String.valueOf(post.getReplyCount()));
        }

        public String setDateFormatted(Timestamp createdAt) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            return sdf.format(createdAt.toDate());
        }
    }

    // ViewHolder for Reply data
    public static class ReplyViewHolder extends RecyclerView.ViewHolder {
        private TextView TVReplyDescription, TVReplyUserName, TVReplyDate, TVReplyLikeCount;
        private ImageView IVReplyLike;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            TVReplyDescription = itemView.findViewById(R.id.TVReplyDescription);
            TVReplyUserName = itemView.findViewById(R.id.TVReplyUserName);
            TVReplyDate = itemView.findViewById(R.id.TVReplyDate);
            IVReplyLike = itemView.findViewById(R.id.IVReplyLike);
            TVReplyLikeCount = itemView.findViewById(R.id.TVReplyLikeCount);
        }

        public void replyBind(ReplyModel reply) {
            TVReplyDescription.setText(reply.getReplyDescription());
            TVReplyUserName.setText(reply.getUserName());
            TVReplyDate.setText(setDateFormatted(reply.getCreatedAt()));
            TVReplyLikeCount.setText(String.valueOf(reply.getLikeCount()));
        }

        public String setDateFormatted(Timestamp createdAt) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            return sdf.format(createdAt.toDate());
        }
    }
}
