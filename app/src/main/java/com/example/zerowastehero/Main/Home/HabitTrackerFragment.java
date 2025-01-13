package com.example.zerowastehero.Main.Home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private FirebaseUser user;
    private DatabaseReference userDatabase;

    private Button addHabitProgress, addHabit2Progress, addHabit3Progress;
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
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference("users").child(userID).child("loginDates");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_tracker, container, false);

        addHabitProgress = view.findViewById(R.id.add_habit_progress);
        addHabit2Progress = view.findViewById(R.id.add_habit2_progress);
        addHabit3Progress = view.findViewById(R.id.add_habit3_progress);
        calendarView = view.findViewById(R.id.calendarView);

        addHabitProgress.setOnClickListener(this::uploadProofNavgiation);
        addHabit2Progress.setOnClickListener(this::uploadProofNavgiation);
        addHabit3Progress.setOnClickListener(this::uploadProofNavgiation);

        String today = dateFormat.format(new Date());
        updateLoginDate(today);
        highlightConsecutiveDays();

        return view;
    }

    private void updateLoginDate(String date) {
        // Update login date in Firebase
        Map<String, Object> dateMap = new HashMap<>();
        dateMap.put(date, true);
        userDatabase.updateChildren(dateMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Login date updated!", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void uploadProofNavgiation(View view) {
        Navigation.findNavController(view).navigate(R.id.DestUploadProof);
    }
}