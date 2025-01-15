package com.example.zerowastehero.Main.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsProfileFragment extends Fragment {

    private TextView TVStatisticTotalRecycledItem, TVStatisticTotalTrashCollected, TVStatisticTotalPointCollected;
    private ProgressBar PBStatisticsProfile;
    private LinearLayout LLStatisticsProfile;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private UserModel user;
    private FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticsProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment statisticprofile.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsProfileFragment newInstance(String param1, String param2) {
        StatisticsProfileFragment fragment = new StatisticsProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_statistics_profile, container, false);

        // Initialize view
        TVStatisticTotalRecycledItem = view.findViewById(R.id.TVStatisticTotalRecycledItem);
        TVStatisticTotalTrashCollected = view.findViewById(R.id.TVStatisticTotalTrashCollected);
        TVStatisticTotalPointCollected = view.findViewById(R.id.TVStatisticTotalPointCollected);
        PBStatisticsProfile = view.findViewById(R.id.PBStatisticsProfile);
        LLStatisticsProfile = view.findViewById(R.id.LLStatisticsProfile);

        fetchStatisticData();
        return view;
    }

    private void fetchStatisticData() {
        if (firebaseUser == null) {
            Log.e("Firestore", "User is not logged in!");
            return;
        }

        String userID = firebaseUser.getUid();
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        if (user != null) {
                            int totalPoint = user.getTotalPoint() != null ? user.getTotalPoint() : 0;
                            int totalTrash = user.getTotalTrash() != null ? user.getTotalTrash() : 0;
                            int totalRecycle = user.getTotalRecycle() != null ? user.getTotalRecycle() : 0;

                            TVStatisticTotalPointCollected.setText(String.valueOf(totalPoint));
                            TVStatisticTotalTrashCollected.setText(String.valueOf(totalTrash));
                            TVStatisticTotalRecycledItem.setText(String.valueOf(totalRecycle));
                        } else {
                            Log.e("Firestore", "Error parsing user data.");
                        }
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e))
                .addOnCompleteListener(task -> {
                    PBStatisticsProfile.setVisibility(View.GONE);
                    LLStatisticsProfile.setVisibility(View.VISIBLE);
                });
    }
}