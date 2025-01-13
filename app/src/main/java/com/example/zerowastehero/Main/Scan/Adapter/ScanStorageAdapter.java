package com.example.zerowastehero.Main.Scan.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScanStorageAdapter extends RecyclerView.Adapter<ScanStorageAdapter.ScanStorageViewHolder>{
    @NonNull
    @Override
    public ScanStorageAdapter.ScanStorageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ScanStorageAdapter.ScanStorageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ScanStorageViewHolder extends RecyclerView.ViewHolder{

        public ScanStorageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
