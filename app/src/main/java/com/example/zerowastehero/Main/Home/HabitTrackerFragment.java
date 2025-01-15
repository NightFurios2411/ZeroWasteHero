package com.example.zerowastehero.Main.Home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitTrackerFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Context context;
    private FirebaseUser firebaseUser;
    private DatabaseReference userDatabase;
    private UserModel user;

    private TextView TVProofStorage, TVHabitTrackerDailyProgress, TVHabitTrackerProgressHabit1, TVHabitTrackerProgressHabit2;
    private Button addHabitProgress, addHabit2Progress;
    private CalendarView calendarView;
    private String userID;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HabitTrackerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HabitTrackerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HabitTrackerFragment newInstance(String param1, String param2) {
        HabitTrackerFragment fragment = new HabitTrackerFragment();
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
        userID = firebaseUser.getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference("users").child(userID).child("loginDates");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_tracker, container, false);

        addHabitProgress = view.findViewById(R.id.add_habit_progress);
        addHabit2Progress = view.findViewById(R.id.add_habit2_progress);
        calendarView = view.findViewById(R.id.calendarView);
        TVProofStorage = view.findViewById(R.id.TVProofStorage);
        TVHabitTrackerDailyProgress = view.findViewById(R.id.TVHabitTrackerDailyProgress);
        TVHabitTrackerProgressHabit1 = view.findViewById(R.id.TVHabitTrackerProgressHabit1);
        TVHabitTrackerProgressHabit2 = view.findViewById(R.id.TVHabitTrackerProgressHabit2);

        fetchUser();
        resetTracker();

        addHabitProgress.setOnClickListener(v -> uploadProofNavigation(view, "collectTrash"));
        addHabit2Progress.setOnClickListener(v -> uploadProofNavigation(view, "recycleItem"));
        TVProofStorage.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.DestProofStorage));

        highlightConsecutiveDays();

        return view;
    }

    private void resetTracker() {
        // Get the current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Reference to the user's document in Firestore
        DocumentReference userRef = db.collection("users").document(userID);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Fetch current user data
                String lastResetDate = documentSnapshot.getString("lastResetDate");
                long recycleProofCount = documentSnapshot.getLong("recycleProofCount") != null ? documentSnapshot.getLong("recycleProofCount") : 0;
                long trashCollectProofCount = documentSnapshot.getLong("trashCollectProofCount") != null ? documentSnapshot.getLong("trashCollectProofCount") : 0;

                // Log for debugging
                Log.d("Upload Proof Fragment", "Recycle Proof Count: " + recycleProofCount);
                Log.d("Upload Proof Fragment", "Trash Collect Proof Count: " + trashCollectProofCount);

                // Check if the tracker needs resetting
                if (lastResetDate == null || !lastResetDate.equals(currentDate)) {
                    userRef.update("recycleProofCount", 0, "trashCollectProofCount", 0, "lastResetDate", currentDate)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Upload Proof Fragment", "Tracker reset successfully.");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Upload Proof Fragment", "Error resetting tracker: ", e);
                            });
                }

                // Update visibility of UI components based on proof limits
                if (recycleProofCount >= 5) {
                    addHabitProgress.setVisibility(View.GONE);
                }
                if (trashCollectProofCount >= 5) {
                    addHabit2Progress.setVisibility(View.GONE);
                }
            } else {
                Log.e("Upload Proof Fragment", "Document does not exist for user.");
            }
        }).addOnFailureListener(e -> {
            Log.e("Upload Proof Fragment", "Error fetching user data: ", e);
            Toast.makeText(getContext(), "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
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
                        int habitTracker = 0;
                        user = documentSnapshot.toObject(UserModel.class);
                        int trashCollectCount = user.getTrashCollectProofCount() != null ? user.getTrashCollectProofCount() : 0;
                        int recycleCount = user.getRecycleProofCount() != null ? user.getRecycleProofCount() : 0;

                        TVHabitTrackerProgressHabit1.setText(String.valueOf(trashCollectCount + " / 5 Trash"));
                        TVHabitTrackerProgressHabit2.setText(String.valueOf(recycleCount + " / 5 Items"));
                        if (trashCollectCount >= 5) habitTracker += 1;
                        if (recycleCount >= 5) habitTracker += 1;
                        TVHabitTrackerDailyProgress.setText(String.valueOf(habitTracker + " of 2 completed"));
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }

    private void highlightConsecutiveDays() {
        userDatabase.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                Map<String, Boolean> loginDates = new HashMap<>();
                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    loginDates.put(dateSnapshot.getKey(), true);
                }
                highlightDatesOnCalendar(loginDates);
            }
        });
    }

    private void highlightDatesOnCalendar(@NonNull Map<String, Boolean> loginDates) {
        calendarView.setDateTextAppearance(R.style.CalendarDateNormal);
        for (String date : loginDates.keySet()) {
            calendarView.setDateTextAppearance(date.equals(dateFormat.format(new Date()))
                    ? R.style.CalendarDateHighlighted : R.style.CalendarDateNormal);
        }
    }

    private void uploadProofNavigation(View view, String habitType) {
        // Create a Bundle to pass the habitType
        Bundle bundle = new Bundle();
        bundle.putString("habitType", habitType); // Add the habitType to the Bundle

        // Navigate to DestUploadProof with the Bundle
        Navigation.findNavController(view).navigate(R.id.DestUploadProof, bundle);
    }
}