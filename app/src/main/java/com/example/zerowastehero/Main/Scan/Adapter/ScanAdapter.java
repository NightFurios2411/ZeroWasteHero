package com.example.zerowastehero.Main.Scan.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.ScanViewHolder> {

    private Context context;
    private int[] images;

    @NonNull
    @Override
    public ScanAdapter.ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ScanAdapter.ScanViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class ScanViewHolder extends RecyclerView.ViewHolder{

        public ScanViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
