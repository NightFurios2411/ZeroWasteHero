package com.example.zerowastehero.Main.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.zerowastehero.Main.Home.Habit.HabitTrackerActivity;
import com.example.zerowastehero.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the "View All" button
        Button viewAllButton = view.findViewById(R.id.viewAllButton);

        // Set click listener for the button
        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start HabitTrackerActivity
                Intent intent = new Intent(getActivity(), HabitTrackerActivity.class);
                startActivity(intent); // This will bring you to HabitTrackerActivity
            }
        });

        return view;
    }
}
