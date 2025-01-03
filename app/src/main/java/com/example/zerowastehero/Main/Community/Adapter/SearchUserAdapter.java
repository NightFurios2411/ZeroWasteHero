package com.example.zerowastehero.Main.Community.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.Main.Community.Interface.SearchUserInterface;
import com.example.zerowastehero.R;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder> {

    private List<UserModel> userModels;

    public SearchUserAdapter(List<UserModel> userModels, SearchUserInterface searchUserInterface) {
        this.userModels = userModels;
    }

    @NonNull
    @Override
    public SearchUserAdapter.SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user_card, parent, false);
        return new SearchUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserAdapter.SearchUserViewHolder holder, int position) {
        UserModel user = userModels.get(position);
        holder.searchUserBind(user);
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public void updateList(List<UserModel> newUserModels) {
        userModels = newUserModels;
        notifyDataSetChanged();
    }

    public static class SearchUserViewHolder extends RecyclerView.ViewHolder {

        private TextView TVSearchUserUsername;
        private ImageView IVSearchUserProfilePicture;

        public SearchUserViewHolder(@NonNull View itemView) {
            super(itemView);

            TVSearchUserUsername = itemView.findViewById(R.id.TVSearchUserUsername);
            IVSearchUserProfilePicture = itemView.findViewById(R.id.IVSearchUserProfilePicture);
        }

        public void searchUserBind(UserModel user) {
            TVSearchUserUsername.setText(user.getUsername());
        }
    }
}
