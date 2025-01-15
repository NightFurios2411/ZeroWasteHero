package com.example.zerowastehero.Main.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private TextView TVUsernameHome, TVViewAllView, TVHomeDayTracker, TVHomePoints, TVHomeDailyProgress, TVHomeProgressHabit1, TVHomeProgressHabit2;

    //  Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //  Rename and change types of parameters
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
    //  Rename and change types and number of parameters
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
        TVHomeDailyProgress = view.findViewById(R.id.TVHomeDailyProgress);
        TVHomeProgressHabit1 = view.findViewById(R.id.TVHomeProgressHabit1);
        TVHomeProgressHabit2 = view.findViewById(R.id.TVHomeProgressHabit2);

        // Fetch the LinearLayouts for the calendar
        LinearLayout[] calendarLayouts = new LinearLayout[]{
                view.findViewById(R.id.LLHomeCalendar1),
                view.findViewById(R.id.LLHomeCalendar2),
                view.findViewById(R.id.LLHomeCalendar3),
                view.findViewById(R.id.LLHomeCalendar4),
                view.findViewById(R.id.LLHomeCalendar5),
                view.findViewById(R.id.LLHomeCalendar6),
                view.findViewById(R.id.LLHomeCalendar7)
        };

        // Fetch the TextViews for the days and numbers
        TextView[] calendarNumbers = new TextView[]{
                view.findViewById(R.id.TVCalendarNo1),
                view.findViewById(R.id.TVCalendarNo2),
                view.findViewById(R.id.TVCalendarNo3),
                view.findViewById(R.id.TVCalendarNo4),
                view.findViewById(R.id.TVCalendarNo5),
                view.findViewById(R.id.TVCalendarNo6),
                view.findViewById(R.id.TVCalendarNo7)
        };

        TextView[] calendarDays = new TextView[]{
                view.findViewById(R.id.TVCalendarDate1),
                view.findViewById(R.id.TVCalendarDate2),
                view.findViewById(R.id.TVCalendarDate3),
                view.findViewById(R.id.TVCalendarDate4),
                view.findViewById(R.id.TVCalendarDate5),
                view.findViewById(R.id.TVCalendarDate6),
                view.findViewById(R.id.TVCalendarDate7)
        };

        fetchUser();
        updateLoginDate();

        if (firebaseUser == null) {
            Intent intent = new Intent(getContext(), AuthActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        // Set background dynamically
        highlightToday(calendarLayouts, calendarNumbers, calendarDays);

        BtnLogOut.setOnClickListener(v -> logOutAlert());
        TVViewAllView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.DestHabitTracker));

        return view;
    }

    private void highlightToday(LinearLayout[] calendarLayouts, TextView[] calendarNumbers, TextView[] calendarDays) {
        // Get today's date and day
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.getDefault());
        String todayDate = dateFormat.format(new Date());

        // Define colors
        int colorToday = Color.parseColor("#e8a9a97a"); // Highlight color for today
        int colorDefault = Color.parseColor("#000000"); // Default color for other days

        for (int i = 0; i < calendarNumbers.length; i++) {
            String calendarNumber = calendarNumbers[i].getText().toString();
            if (todayDate.equals(calendarNumber)) {
                // Set filled background for today's layout
                calendarLayouts[i].setBackgroundResource(R.drawable.home_calendar_filled);
                calendarNumbers[i].setTextColor(colorToday);
                calendarDays[i].setTextColor(colorToday);
            } else {
                // Set not filled background for other layouts
                calendarLayouts[i].setBackgroundResource(R.drawable.home_calendar_not_filled);
                calendarNumbers[i].setTextColor(colorDefault);
                calendarDays[i].setTextColor(colorDefault);
            }
        }
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
                        if (user != null) {
                            String username = user.getUsername();
                            String point = String.valueOf(user.getPoint());

                            int habitTracker = 0;
                            TVUsernameHome.setText(username);
                            TVHomePoints.setText(point);
                            int trashCollectCount = user.getTrashCollectProofCount() != null ? user.getTrashCollectProofCount() : 0;
                            int recycleCount = user.getRecycleProofCount() != null ? user.getRecycleProofCount() : 0;

                            TVHomeProgressHabit1.setText(String.valueOf(trashCollectCount + " / 5 Trash"));
                            TVHomeProgressHabit2.setText(String.valueOf(recycleCount + " / 5 Items"));
                            if (trashCollectCount == 5) habitTracker += 1;
                            if (recycleCount == 5) habitTracker += 1;
                            TVHomeDailyProgress.setText(String.valueOf(habitTracker + " of 2 completed"));
                        }

                        // Fetch loginDates safely
                        Object loginDatesObj = documentSnapshot.get("loginDates");
                        if (loginDatesObj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Boolean> loginDates = (Map<String, Boolean>) loginDatesObj;
                            updateStreak(loginDates);
                        } else {
                            Log.d("Firestore", "loginDates is not of expected type Map<String, Boolean>.");
                        }
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

        // Reference to the user's document
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Check if the login date for today already exists
                        Map<String, Object> loginDates = (Map<String, Object>) documentSnapshot.get("loginDates");
                        if (loginDates != null && loginDates.containsKey(today)) {
                            Log.d("Firestore", "Login date already updated for today");
                        } else {
                            // Update loginDates and increment points
                            Map<String, Object> update = new HashMap<>();
                            update.put("loginDates." + today, true);

                            // Increment points
                            long currentPoint = documentSnapshot.getLong("point") != null ? documentSnapshot.getLong("point") : 0;
                            long totalPoint = documentSnapshot.getLong("totalPoint") != null ? documentSnapshot.getLong("totalPoint") : 0;
                            update.put("totalPoint", totalPoint + 100);
                            update.put("point", currentPoint + 100);
                            TVHomePoints.setText(String.valueOf(currentPoint));

                            // Perform update
                            db.collection("users").document(userID).update(update)
                                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Login date and points updated"))
                                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating login date and points", e));
                        }
                    } else {
                        Log.e("Firestore", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user document", e));
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
}