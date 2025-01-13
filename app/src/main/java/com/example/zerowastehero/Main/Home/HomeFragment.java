package com.example.zerowastehero.Main.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zerowastehero.Auth.AuthActivity;
import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private UserModel user;
    private FirebaseFirestore db;

    private ProgressBar PBCircular;
    private int progress = 0;
    private Button BtnLogOut;
    private TextView TVUsernameHome, TVViewAllView, TVHomeDayTracker, TVHomePoints;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        BtnLogOut = view.findViewById(R.id.BtnLogOut);
        TVUsernameHome = view.findViewById(R.id.TVHomeUsername);
        TVViewAllView = view.findViewById(R.id.TVViewAllView);
        PBCircular = view.findViewById(R.id.PBCircular);
        TVHomeDayTracker = view.findViewById(R.id.TVHomeDayTracker);
        TVHomePoints = view.findViewById(R.id.TVHomePoints);

        fetchUser();
        updateLoginDate();

        if (firebaseUser == null) {
            Intent intent = new Intent(getContext(), AuthActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        BtnLogOut.setOnClickListener(v -> logOutAlert());
        TVViewAllView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.DestHabitTracker));

        return view;
    }

    private void fetchUser() {
        String userID = firebaseUser.getUid();
        String email = firebaseUser.getEmail();
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        user = documentSnapshot.toObject(UserModel.class);
                        String username = user.getUsername();
                        String point = String.valueOf(user.getPoint());
                        TVUsernameHome.setText(username);
                        TVHomePoints.setText(point);

                        // Fetch login dates
                        Map<String, Boolean> loginDates = (Map<String, Boolean>) documentSnapshot.get("loginDates");
                        updateStreak(loginDates);
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }

    private void updateStreak(Map<String, Boolean> loginDates) {
        if (loginDates == null) {
            TVHomeDayTracker.setText("No activity tracked yet.");
            PBCircular.setProgress(0);
            return;
        }

        // Get the current streak
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(new Date());
        int streak = 0;

        for (int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            String day = dateFormat.format(calendar.getTime());

            if (Boolean.TRUE.equals(loginDates.get(day))) {
                streak++;
            } else {
                break; // Stop streak if a day is missing
            }
        }

        // Update the UI
        TVHomeDayTracker.setText("7-Day Streak: " + streak + " days");
        PBCircular.setProgress(streak);
    }

    private void updateLoginDate() {
        String userID = firebaseUser.getUid();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Map<String, Object> update = new HashMap<>();
        update.put("loginDates." + today, true);

        db.collection("users").document(userID).update(update)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Login date updated"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating login date", e));
    }



    private void logOutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    signOut();
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Successfully logged out.", Toast.LENGTH_SHORT).show();

        // Optionally, redirect the user to the login screen
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void createRecycleCenter() {

    }
}