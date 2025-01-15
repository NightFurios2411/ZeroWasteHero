package com.example.zerowastehero.Main.Map;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zerowastehero.DataBinding.Model.ReportModel;
import com.example.zerowastehero.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportViewFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private ProgressBar PBReportView;
    private ConstraintLayout CLReportView;
    private TextView TVReportViewTitle, TVReportViewDescription, TVReportViewDate, TVReportViewStatus;

    //  Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //  Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportViewFragment.
     */
    //  Rename and change types and number of parameters
    public static ReportViewFragment newInstance(String param1, String param2) {
        ReportViewFragment fragment = new ReportViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_view, container, false);

        // Initialize view
        TVReportViewTitle = view.findViewById(R.id.TVReportViewTitle);
        TVReportViewDescription = view.findViewById(R.id.TVReportViewDescription);
        TVReportViewDate = view.findViewById(R.id.TVReportViewDate);
        TVReportViewStatus = view.findViewById(R.id.TVReportViewStatus);
        PBReportView = view.findViewById(R.id.PBReportView);
        CLReportView = view.findViewById(R.id.CLReportView);

        // Fetch bundle
        Bundle bundle = this.getArguments();
        String reportID = null;
        if (bundle != null) {
            reportID = bundle.getString("reportID");
        }

        fetchReport(reportID);

        return view;
    }

    private void fetchReport(String reportID) {
        if (reportID == null || reportID.isEmpty()) {
            Log.e("ReportViewFragment", "Invalid ReportID: " + reportID);
            return;
        }

        db.collection("reports")
                .document(reportID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ReportModel report = documentSnapshot.toObject(ReportModel.class);

                        if (report != null) {
                            Log.d("FetchReport", "Report fetched successfully: " + report.getReportID());
                            PBReportView.setVisibility(View.GONE);
                            CLReportView.setVisibility(View.VISIBLE);

                            TVReportViewTitle.setText(report.getTitle());
                            TVReportViewDescription.setText(report.getDescription());
                            TVReportViewDate.setText(setDateFormatted(report.getCreatedAt()));
                            TVReportViewStatus.setText(report.getStatus());
                        } else {
                            Log.e("FetchReport", "Report object is null");
                        }
                    } else {
                        Log.e("FetchReport", "No such document exists");
                    }
                })
                .addOnFailureListener(e -> Log.e("FetchReport", "Error fetching report", e));
    }

    private String setDateFormatted(Timestamp createdAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(createdAt.toDate());
    }
}