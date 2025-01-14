package com.example.zerowastehero.Main.Profile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private FirebaseFirestore db;

    private ArrayList<UserModel> userModels;
    private Context context;

    public LeaderboardAdapter(Context context, ArrayList<UserModel> userModels) {
        this.context = context;
        this.userModels = userModels;
    }

    @NonNull
    @Override
    public LeaderboardAdapter.LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_leaderboard_card, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.LeaderboardViewHolder holder, int position) {
        // Get the current user model
        UserModel user = userModels.get(position);

        // Bind data to the view holder
        holder.leaderboardBind(user);

        // Set the ranking number
        holder.TVLeaderboardRankingNumber.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        private TextView TVLeaderboardUsername, TVLeaderboardPoint, TVLeaderboardRankingNumber;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);

            TVLeaderboardUsername = itemView.findViewById(R.id.TVLeaderboardUsername);
            TVLeaderboardPoint = itemView.findViewById(R.id.TVLeaderboardPoint);
            TVLeaderboardRankingNumber = itemView.findViewById(R.id.TVLeaderboardRankingNumber);
        }

        public void leaderboardBind(UserModel user) {
            TVLeaderboardUsername.setText(user.getUsername());
            TVLeaderboardPoint.setText(String.valueOf(user.getPoint()));
        }
    }
}
