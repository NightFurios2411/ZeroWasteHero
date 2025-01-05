package com.example.zerowastehero.Main.Profile;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zerowastehero.R;

public class ProfileFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Claim Points Button
        Button claimPointsButton = findViewById(R.id.claim_points_button);
        claimPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileFragment.this, "Points Claimed!", Toast.LENGTH_SHORT).show();
            }
        });

        // Reward Buttons
        Button reward3000Button = findViewById(R.id.reward_3000);
        reward3000Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileFragment.this, "Redeemed 3000 Points!", Toast.LENGTH_SHORT).show();
            }
        });

        Button reward5000Button = findViewById(R.id.reward_5000);
        reward5000Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileFragment.this, "Redeemed 5000 Points!", Toast.LENGTH_SHORT).show();
            }
        });

        Button reward10000Button = findViewById(R.id.reward_10000);
        reward10000Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileFragment.this, "Redeemed 10000 Points!", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation Buttons
        Button navHomeButton = findViewById(R.id.nav_home);
        navHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileFragment.this, "Navigating to Home!", Toast.LENGTH_SHORT).show();
            }
        });

        Button navChallengesButton = findViewById(R.id.nav_challenges);
        navChallengesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileFragment.this, "Navigating to Challenges!", Toast.LENGTH_SHORT).show();
            }
        });

        Button navSeeRewardsButton = findViewById(R.id.nav_seeRewards);
        navSeeRewardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileFragment.this, "Navigating to Rewards!", Toast.LENGTH_SHORT).show();
            }
        });

        // Partner Loyalty Program Button
        Button partnerLoyaltyProgramButton = findViewById(R.id.nav_partner_loyalty);
        partnerLoyaltyProgramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileFragment.this, "Opening Partner Loyalty Program!", Toast.LENGTH_SHORT).show();
                // Add intent to navigate to PartnerLoyaltyProgramActivity
                // Intent intent = new Intent(MainActivity.this, PartnerLoyaltyProgramActivity.class);
                // startActivity(intent);
            }
        });

        // My Stats Button
        Button myStatsButton = findViewById(R.id.nav_my_stats);
        myStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileFragment.this, "Opening My Stats!", Toast.LENGTH_SHORT).show();
                // Add intent to navigate to MyStatsActivity
                // Intent intent = new Intent(MainActivity.this, MyStatsActivity.class);
                // startActivity(intent);
            }
        });
    }
}