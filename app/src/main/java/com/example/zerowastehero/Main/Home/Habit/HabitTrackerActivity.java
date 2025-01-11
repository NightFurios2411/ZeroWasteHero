package com.example.zerowastehero.Main.Home.Habit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.zerowastehero.R;

public class HabitTrackerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_tracker); // Your habit tracker page layout

        // Find the "+" buttons for each habit
        Button addHabitProgressButton = findViewById(R.id.add_habit_progress);
        Button addHabit2ProgressButton = findViewById(R.id.add_habit2_progress);
        Button addHabit3ProgressButton = findViewById(R.id.add_habit3_progress);

        // Set onClickListener for "+" buttons to open UploadProofActivity
        addHabitProgressButton.setOnClickListener(v -> openUploadProofActivity());
        addHabit2ProgressButton.setOnClickListener(v -> openUploadProofActivity());
        addHabit3ProgressButton.setOnClickListener(v -> openUploadProofActivity());
    }

    // Method to navigate to UploadProofActivity
    private void openUploadProofActivity() {
        Intent intent = new Intent(HabitTrackerActivity.this, UploadProofActivity.class);
        startActivity(intent);
    }
}
