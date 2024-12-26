package com.example.zerowastehero.Main.Community.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zerowastehero.DataBinding.Model.ReplyModel;
import com.example.zerowastehero.R;

import java.util.ArrayList;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyHolder> {

    // Constants for view types
    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_REPLY = 1;

    private Context context;
    private ArrayList<ReplyModel> replyModels;

    public ReplyAdapter(Context context, ArrayList<ReplyModel> replyModels) {
        this.context = context;
        this.replyModels = replyModels;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_POST;
        else return VIEW_TYPE_REPLY;
    }

    @NonNull
    @Override
    public ReplyAdapter.ReplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate Post Card
        if (viewType == VIEW_TYPE_POST) return new ReplyHolder(inflater.inflate(R.layout.item_post_card, parent, false));
        // Inflate Reply Card
        else return new ReplyHolder(inflater.inflate(R.layout.item_reply_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyAdapter.ReplyHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_REPLY) {
            int adjustedPosition = position - 1; // Offset for Post card
            ReplyModel reply = replyModels.get(adjustedPosition);
            holder.TVReplyDescription.setText(reply.getReplyDescription());
        }
        // No binding needed for VIEW_TYPE_POST
    }

    @Override
    public int getItemCount() {
        return Math.max(1,replyModels.size() + 1);
    }

    public static class ReplyHolder extends RecyclerView.ViewHolder {

        private TextView TVReplyDescription, TVReplyDate;

        public ReplyHolder(@NonNull View itemView) {
            super(itemView);

            TVReplyDescription = itemView.findViewById(R.id.TVReplyDescription);
        }
    }
}
