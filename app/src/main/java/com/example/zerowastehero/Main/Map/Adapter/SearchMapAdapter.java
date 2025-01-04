package com.example.zerowastehero.Main.Map.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zerowastehero.Main.Map.Interface.RecycleLocationInterface;
import com.example.zerowastehero.R;

public class SearchMapAdapter extends RecyclerView.Adapter<SearchMapAdapter.SearchMapViewHolder> {

    private RecycleLocationInterface recycleLocationInterface;
    private Context context;

    public SearchMapAdapter(Context context, RecycleLocationInterface recycleLocationInterface) {
        this.recycleLocationInterface = recycleLocationInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchMapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_details_card, parent, false);
        return new SearchMapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchMapViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            if (recycleLocationInterface != null) {
                recycleLocationInterface.onRecycleLocationClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void updateList() {

    }

    public static class SearchMapViewHolder extends RecyclerView.ViewHolder {

        public SearchMapViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void searchMapBind() {

        }
    }

}
