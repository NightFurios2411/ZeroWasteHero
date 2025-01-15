package com.example.zerowastehero.Main.Map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zerowastehero.DataBinding.Model.ReportModel;
import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.Main.Map.Adapter.ReportAdapter;
import com.example.zerowastehero.Main.Map.Interface.ReportInterface;
import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportListFragment extends Fragment implements ReportInterface {

    private ReportAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private UserModel user;

    private ProgressBar PBReportList;
    private ArrayList<ReportModel> reportModels;
    private TextView TVCleanUpRequest, TVReportTitle, TVReportDescription, TVReportStatus;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportListFragment newInstance(String param1, String param2) {
        ReportListFragment fragment = new ReportListFragment();
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
        firebaseUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);

        // Initialize View
        TVCleanUpRequest = view.findViewById(R.id.TVCleanUpRequest);
        PBReportList = view.findViewById(R.id.PBReportList);

        // Initialize Report Models
        reportModels = new ArrayList<>();
        fetchReports();

        // Set up recycleView
        RecyclerView RVReportView = view.findViewById(R.id.RVReportView);
        RVReportView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReportAdapter(getContext(), reportModels, this);
        RVReportView.setAdapter(adapter);

        TVCleanUpRequest.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.DestReportRequest));

        return view;
    }

    private void fetchReports() {
        db.collection("reports")
                .whereEqualTo("userID", firebaseUser.getUid())
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reportModels.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        ReportModel report = doc.toObject(ReportModel.class);
                        reportModels.add(report);
                    }
                    adapter.notifyDataSetChanged();
                    Log.d("Report List Fragment", "Number of reports: " + reportModels.size());
                })
                .addOnFailureListener(e -> Log.e("Report List Fragment", "Error fetching reports: " + e.getMessage()))
                .addOnCompleteListener(task -> PBReportList.setVisibility(View.GONE));
    }

    @Override
    public void onReportClick(int position) {
        Bundle bundle = new Bundle();
        Log.d("Report List Fragment", "Position: " + position);
        String reportID = reportModels.get(position).getReportID();
        Log.d("Report List Fragment", "Report ID: " + reportID);
        if (reportID.isEmpty() || reportID == null) {
            Log.e("Report List Fragment", "Report ID is empty or null");
            return;
        }
        bundle.putString("reportID", reportID);
        Log.d("Report List Fragment", "Passed reportID to Post View: " + bundle.getString("reportID"));
        Navigation.findNavController(requireView()).navigate(R.id.DestReportView, bundle);
    }
}