package com.example.zerowastehero.Main.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePageFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private UserModel user;
    private FirebaseFirestore db;

    private TextView TVProfileUserName, TVProfileUserBio, TVProfileUserPoint;

    private ImageButton IBChallengesView, IBLoyaltyView, IBRedeemedView, IBMyStatsView, IBLeaderboardView;
    private ImageButton IBPointClaim1, IBPointClaim2, IBPointClaim3, IBPointClaimUser;
    private LinearLayout LLPointClaim1, LLPointClaim2, LLPointClaim3;
    private int selectedPoints = 0;
    private boolean isPointClaim1Selected = false;
    private boolean isPointClaim2Selected = false;
    private boolean isPointClaim3Selected = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfilePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profilepage.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilePageFragment newInstance(String param1, String param2) {
        ProfilePageFragment fragment = new ProfilePageFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        // Initialize view
        IBChallengesView = view.findViewById(R.id.IBChallengesView);
        IBLoyaltyView = view.findViewById(R.id.IBLoyaltyView);
        IBRedeemedView = view.findViewById(R.id.IBRedeemedView);
        IBMyStatsView = view.findViewById(R.id.IBMyStatsView);
        IBLeaderboardView = view.findViewById(R.id.IBLeaderboardView);
        TVProfileUserName = view.findViewById(R.id.TVProfileUserName);
        TVProfileUserBio = view.findViewById(R.id.TVProfileUserBio);
        TVProfileUserPoint = view.findViewById(R.id.TVProfileUserPoint);
        IBPointClaim1 = view.findViewById(R.id.IBPointClaim1);
        IBPointClaim2 = view.findViewById(R.id.IBPointClaim2);
        IBPointClaim3 = view.findViewById(R.id.IBPointClaim3);
        IBPointClaimUser = view.findViewById(R.id.IBPointClaimUser);
        LLPointClaim1 = view.findViewById(R.id.LLPointClaim1);
        LLPointClaim2 = view.findViewById(R.id.LLPointClaim2);
        LLPointClaim3 = view.findViewById(R.id.LLPointClaim3);

        fetchUser();

        // Navigation
        IBChallengesView.setOnClickListener(v -> viewNavigation(v, R.id.DestChallenges));
        IBLoyaltyView.setOnClickListener(v -> viewNavigation(v, R.id.DestLoyalty));
        IBRedeemedView.setOnClickListener(v -> viewNavigation(v, R.id.DestRedeemed));
        IBMyStatsView.setOnClickListener(v -> viewNavigation(v, R.id.DestMyStats));
        IBLeaderboardView.setOnClickListener(v -> viewNavigation(v, R.id.DestLeaderboard));

        // Point claim button click listeners
        IBPointClaim1.setOnClickListener(v -> togglePointClaim(3000, IBPointClaim1, 1));  // Select 3000 points
        IBPointClaim2.setOnClickListener(v -> togglePointClaim(5000, IBPointClaim2, 2));  // Select 5000 points
        IBPointClaim3.setOnClickListener(v -> togglePointClaim(10000, IBPointClaim3, 3));  // Select 10000 points

        // Claim points button click listener
        IBPointClaimUser.setEnabled(false);
        IBPointClaimUser.setOnClickListener(v -> {
            if (selectedPoints > 0) {
                pointClaim(selectedPoints);  // Claim the selected points
                resetPointClaims();
            } else {
                Toast.makeText(getContext(), "Please select a point option first!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void togglePointClaim(int point, ImageButton imageButton, int claimNumber) {
        if (claimNumber == 1 && isPointClaim1Selected) {
            // Untick: Remove points and reset button background
            selectedPoints -= point;
            isPointClaim1Selected = false;
            imageButton.setBackgroundResource(R.drawable.image_unticked);  // Reset to default background
        } else if (claimNumber == 1) {
            // Tick: Add points and change button background
            selectedPoints += point;
            isPointClaim1Selected = true;
            imageButton.setBackgroundResource(R.drawable.image_ticked);  // Show ticked background
        } else if (claimNumber == 2 && isPointClaim2Selected) {
            // Untick: Remove points and reset button background
            selectedPoints -= point;
            isPointClaim2Selected = false;
            imageButton.setBackgroundResource(R.drawable.image_unticked);  // Reset to default background
        } else if (claimNumber == 2) {
            // Tick: Add points and change button background
            selectedPoints += point;
            isPointClaim2Selected = true;
            imageButton.setBackgroundResource(R.drawable.image_ticked);  // Show ticked background
        } else if (claimNumber == 3 && isPointClaim3Selected) {
            // Untick: Remove points and reset button background
            selectedPoints -= point;
            isPointClaim3Selected = false;
            imageButton.setBackgroundResource(R.drawable.image_unticked);  // Reset to default background
        } else if (claimNumber == 3) {
            // Tick: Add points and change button background
            selectedPoints += point;
            isPointClaim3Selected = true;
            imageButton.setBackgroundResource(R.drawable.image_ticked);  // Show ticked background
        }

        // Enable the claim points button if points are selected
        IBPointClaimUser.setEnabled(selectedPoints > 0);
    }

    private void pointClaim(int point) {
        String userID = firebaseUser.getUid();

        // Get the user's document reference
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the current points
                        user = documentSnapshot.toObject(UserModel.class);
                        int currentPoints = user.getPoint();

                        // Add the claimed points to the current points
                        int updatedPoints = currentPoints + point;

                        // Update the points in Firestore
                        db.collection("users").document(userID)
                                .update("point", updatedPoints)
                                .addOnSuccessListener(aVoid -> {
                                    // Update the UI to show the new points
                                    TVProfileUserPoint.setText(String.valueOf(updatedPoints));
                                    Log.d("Firestore", "Points updated successfully");
                                })
                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating points", e));
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }

    private void resetPointClaims() {
        // Reset the selected points and untick all point claim buttons
        selectedPoints = 0;
        if (isPointClaim1Selected) {
            isPointClaim1Selected = false;
            LLPointClaim1.setVisibility(View.GONE);

        }
        isPointClaim2Selected = false;
        isPointClaim3Selected = false;

        // Reset all buttons to their default state
        IBPointClaim1.setBackgroundResource(R.drawable.image_unticked);
        IBPointClaim2.setBackgroundResource(R.drawable.image_unticked);
        IBPointClaim3.setBackgroundResource(R.drawable.image_unticked);

        // Disable the claim points button since no points are selected
        IBPointClaimUser.setEnabled(false);
    }

    private void fetchUser() {
        String userID = firebaseUser.getUid();
        String email = firebaseUser.getEmail();
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        user = documentSnapshot.toObject(UserModel.class);
                        String username = user.getUsername();
                        String bio = user.getBio();
                        String point = String.valueOf(user.getPoint());
                        TVProfileUserName.setText(username);
                        TVProfileUserBio.setText(bio);
                        TVProfileUserPoint.setText(point);
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }

    private void viewNavigation(View view, int destID) {
        Navigation.findNavController(view).navigate(destID);
    }
}