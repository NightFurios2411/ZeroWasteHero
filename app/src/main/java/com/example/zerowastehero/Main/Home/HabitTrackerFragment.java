package com.example.zerowastehero.Main.Home;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitTrackerFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Context context;

    private Button addHabitProgress, addHabit2Progress, addHabit3Progress;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_tracker, container, false);

        addHabitProgress = view.findViewById(R.id.add_habit_progress);
        addHabit2Progress = view.findViewById(R.id.add_habit2_progress);
        addHabit3Progress = view.findViewById(R.id.add_habit3_progress);

        addHabitProgress.setOnClickListener(v -> uploadProofNavgiation(v));
        addHabit2Progress.setOnClickListener(v -> uploadProofNavgiation(v));
        addHabit3Progress.setOnClickListener(v -> uploadProofNavgiation(v));

        return view;
    }

    private void uploadProofNavgiation(View view) {
        Navigation.findNavController(view).navigate(R.id.DestUploadProof);
    }
}