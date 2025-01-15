package com.example.zerowastehero.Main.Profile;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RedeemedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RedeemedFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private UserModel user;

    private ConstraintLayout CLRedeemedReward1, CLRedeemedReward2, CLRedeemedReward3;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RedeemedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Redeemed.
     */
    // TODO: Rename and change types and number of parameters
    public static RedeemedFragment newInstance(String param1, String param2) {
        RedeemedFragment fragment = new RedeemedFragment();
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
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_redeemed, container, false);

        // Initialize view
        CLRedeemedReward1 = view.findViewById(R.id.CLRedeemedReward1);
        CLRedeemedReward2 = view.findViewById(R.id.CLRedeemedReward2);
        CLRedeemedReward3 = view.findViewById(R.id.CLRedeemedReward3);

        fetchUser();

        return view;
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
                            if (user.isPointClaimed1()) CLRedeemedReward1.setVisibility(View.VISIBLE);
                            if (user.isPointClaimed2()) CLRedeemedReward2.setVisibility(View.VISIBLE);
                            if (user.isPointClaimed3()) CLRedeemedReward3.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }
}