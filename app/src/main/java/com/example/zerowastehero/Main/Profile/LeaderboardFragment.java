package com.example.zerowastehero.Main.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.Main.Profile.Adapter.LeaderboardAdapter;
import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private int displayCount = 100;
    private RecyclerView RVLeaderboard;
    private ArrayList<UserModel> userModels;
    private LeaderboardAdapter adapter;
    private UserModel currentUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderboardFragment newInstance(String param1, String param2) {
        LeaderboardFragment fragment = new LeaderboardFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        // Initialize view
        RVLeaderboard = view.findViewById(R.id.RVLeaderboard);

        // Fetch leaderboard data
        userModels = new ArrayList<>();
        fetchLeaderboard();

        // Set up RecyclerView for leaderboard
        RVLeaderboard.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LeaderboardAdapter(getContext(), userModels);
        RVLeaderboard.setAdapter(adapter);

        return view;
    }

    private void fetchLeaderboard() {
        db.collection("users")
                .orderBy("point", Query.Direction.DESCENDING) // Primary order by points (descending)
                .orderBy("username", Query.Direction.ASCENDING) // Secondary order by username (ascending)
                .limit(displayCount) // Limit based on user selection
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userModels.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        UserModel user = document.toObject(UserModel.class);
                        userModels.add(user);
                    }
                    adapter.notifyDataSetChanged();
                    fetchCurrentUser(); // Fetch current user ranking
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching leaderboard", e));
    }


    private void fetchCurrentUser() {
        String currentUserID = mAuth.getCurrentUser().getUid();
        db.collection("users").document(currentUserID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentUser = documentSnapshot.toObject(UserModel.class);
                        displayCurrentUserIfNotInLeaderboard();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching current user", e));
    }

    private void displayCurrentUserIfNotInLeaderboard() {
        boolean isCurrentUserInLeaderboard = userModels.stream()
                .anyMatch(user -> user.getUserID().equals(currentUser.getUserID()));

        if (!isCurrentUserInLeaderboard) {
            db.collection("users")
                    .orderBy("point", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        int rank = 1;
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            UserModel user = document.toObject(UserModel.class);
                            if (user.getUserID().equals(currentUser.getUserID())) {
                                Toast.makeText(getContext(),
                                        "Your Rank: " + rank + " | Points: " + currentUser.getPoint(),
                                        Toast.LENGTH_LONG).show();
                                break;
                            }
                            rank++;
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error fetching current user rank", e));
        }
    }
}