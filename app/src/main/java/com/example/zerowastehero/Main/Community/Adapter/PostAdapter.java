package com.example.zerowastehero.Main.Community.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private Context context;
    private ArrayList<PostModel> postsModels;

    public PostAdapter(Context context, ArrayList<PostModel> postsModels) {
        this.context = context;
        this.postsModels = postsModels;
    }

    @NonNull
    @Override
    public PostAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_card, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostHolder holder, int position) {
        // Bind data to the views in each item

        holder.TVPostTitle.setText(postsModels.get(position).getPostTitle());
        holder.TVPostDescription.setText(postsModels.get(position).getPostDescription());
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the RecyclerView
        return postsModels.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        // Define views in the item layout

        private TextView TVPostTitle, TVPostDescription;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            TVPostTitle = itemView.findViewById(R.id.TVPostTitle);
            TVPostDescription = itemView.findViewById(R.id.TVPostDescription);
        }
    }
}