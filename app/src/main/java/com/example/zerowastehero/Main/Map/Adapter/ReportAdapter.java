package com.example.zerowastehero.Main.Map.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zerowastehero.DataBinding.Model.ReportModel;
import com.example.zerowastehero.Main.Map.Interface.ReportInterface;
import com.example.zerowastehero.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private ReportInterface reportInterface;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Context context;
    private ArrayList<ReportModel> reportModels;

    public ReportAdapter(Context context, ArrayList<ReportModel> reportModels, ReportInterface reportInterface) {
        this.context = context;
        this.reportModels = reportModels;
        this.reportInterface = reportInterface;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ReportAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ReportAdapter.ReportViewHolder(inflater.inflate(R.layout.item_report_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ReportViewHolder holder, int position) {
        ReportModel report = reportModels.get(position);

        holder.reportBind(report);

        holder.itemView.setOnClickListener(v -> {
            if (reportInterface != null) {
                reportInterface.onReportClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportModels.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {

        private TextView TVReportDate, TVReportStatus, TVReportTitle;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views in the item layout
            TVReportDate = itemView.findViewById(R.id.TVReportDate);
            TVReportStatus = itemView.findViewById(R.id.TVReportStatus);
            TVReportTitle = itemView.findViewById(R.id.TVReportTitle);
        }

        public void reportBind(ReportModel report) {
            TVReportTitle.setText(report.getTitle());
            TVReportStatus.setText(report.getStatus());
            TVReportDate.setText(setDateFormatted(report.getCreatedAt()));
        }

        public String setDateFormatted(Timestamp createdAt) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            return sdf.format(createdAt.toDate());
        }
    }
}
