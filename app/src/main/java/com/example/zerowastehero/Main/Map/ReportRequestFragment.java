package com.example.zerowastehero.Main.Map;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.DataBinding.Model.ReportModel;
import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportRequestFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private UserModel user;

    private ProgressBar PBReportRequest;
    private ImageButton BtnReportRequestSubmit;
    private TextView TVListOfRequest;
    private EditText ETReportRequestTitle, ETReportRequestDescription;
    private String[] reportImageURLs;
    private ArrayList<ReportModel> reportModels;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportRequestFragment newInstance(String param1, String param2) {
        ReportRequestFragment fragment = new ReportRequestFragment();
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
        View view = inflater.inflate(R.layout.fragment_report_request, container, false);

        // Initialize view
        TVListOfRequest = view.findViewById(R.id.TVListOfRequest);
        ETReportRequestTitle = view.findViewById(R.id.ETReportRequestTitle);
        ETReportRequestDescription = view.findViewById(R.id.ETReportRequestDescription);
        BtnReportRequestSubmit = view.findViewById(R.id.BtnReportRequestSubmit);
        PBReportRequest = view.findViewById(R.id.PBReportRequest);

        fetchUser();

        BtnReportRequestSubmit.setOnClickListener(v -> submitReport(view));
        TVListOfRequest.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.DestRepostList));

        return view;
    }

    private void loading() {
        BtnReportRequestSubmit.setEnabled(false);
        BtnReportRequestSubmit.setVisibility(View.GONE);
        PBReportRequest.setVisibility(View.VISIBLE);
    }

    private void doneLoading(View view) {
        BtnReportRequestSubmit.setEnabled(true);
        BtnReportRequestSubmit.setVisibility(View.VISIBLE);
        PBReportRequest.setVisibility(View.GONE);
        Navigation.findNavController(view).navigate(R.id.DestRepostList);
    }

    private void submitReport(View view) {
        // Disable button to avoid multiple submissions
        loading();

        String title = ETReportRequestTitle.getText().toString().trim();
        String description = ETReportRequestDescription.getText().toString().trim();
        String userID = firebaseUser.getUid();
        String email = user.getEmail();

        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        String username = user.getUsername();
                        createReport(view, title, description, userID, username);
                        // Use these details in your activity
                        Log.d("Firestore", "User Name: " + username + ", Email: " + email);
                    } else {
                        Log.e("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e))
                .addOnCompleteListener(task -> doneLoading(view));
    }

    private void createReport(View view, String title, String description, String userID, String userName) {
        if (user == null) {
            Toast.makeText(getContext(), "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e("ReportRequestFragment", "FirebaseUser is null. Can't create a report.");
            return;
        }

        DocumentReference newReportRef = db.collection("reports").document();
        String reportID = newReportRef.getId();

        ReportModel newReport = new ReportModel(title, description, userID, userName, "pending", reportImageURLs, new Timestamp(new Date()));
        newReport.setReportID(reportID);

        // Add the post to Firestore
        newReportRef.set(newReport)
                .addOnSuccessListener(aVoid -> {
                    Log.d("ReportRequestFragment", "Report successfully added to Firestore with ID: " + reportID);
                })
                .addOnFailureListener(e -> {
                    Log.e("CreatePostFragment", "Error adding post to Firestore: ", e);

                    // Handle the error (e.g., show a message to the user)
                });
    }

    private void fetchUser() {
        if (firebaseUser == null) {
            Log.e("Firestore", "User is not logged in!");
            return;
        }

        String userID = firebaseUser.getUid();
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Map the document to a UserModel object
                        user = documentSnapshot.toObject(UserModel.class);
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }
}