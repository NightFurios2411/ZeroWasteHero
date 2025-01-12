package com.example.zerowastehero.Main.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.zerowastehero.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePageFragment extends Fragment {

    ImageButton IBChallengesView, IBLoyaltyView, IBRedeemedView, IBMyStatsView, IBLeaderboardView;

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

        IBChallengesView.setOnClickListener(v -> viewNavigation(v, R.id.DestChallenges));
        IBLoyaltyView.setOnClickListener(v -> viewNavigation(v, R.id.DestLoyalty));
        IBRedeemedView.setOnClickListener(v -> viewNavigation(v, R.id.DestRedeemed));
        IBMyStatsView.setOnClickListener(v -> viewNavigation(v, R.id.DestMyStats));
        IBLeaderboardView.setOnClickListener(v -> viewNavigation(v, R.id.DestLeaderboard));
        return view;
    }

    private void viewNavigation(View view, int destID) {
        Navigation.findNavController(view).navigate(destID);
    }
}