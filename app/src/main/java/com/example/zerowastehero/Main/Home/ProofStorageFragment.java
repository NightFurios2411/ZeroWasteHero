package com.example.zerowastehero.Main.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Model.ProofModel;
import com.example.zerowastehero.DataBinding.Model.ReportModel;
import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.Main.Home.Adapter.ProofStorageAdapter;
import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProofStorageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProofStorageFragment extends Fragment {

    private GridView GVProofStorage;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private List<ProofModel> proofModels;
    private ProofStorageAdapter adapter;
    private ProgressBar PBProofStorage;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProofStorageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProofStorageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProofStorageFragment newInstance(String param1, String param2) {
        ProofStorageFragment fragment = new ProofStorageFragment();
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
        proofModels = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_proof_storage, container, false);

        // Initialize view
        GVProofStorage = view.findViewById(R.id.GVProofStorage);
        PBProofStorage = view.findViewById(R.id.PBProofStorage);

        fetchProofs();

        adapter = new ProofStorageAdapter(getContext(), proofModels);
        GVProofStorage.setAdapter(adapter);

        return view;
    }

    private void fetchProofs() {
        db.collection("proofs")
                .whereEqualTo("userID", firebaseUser.getUid())
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Clear existing data to avoid duplicates if method is called again
                        proofModels.clear();

                        // Iterate through snapshots and add to proofModels
                        for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                            ProofModel proof = queryDocumentSnapshots.getDocuments().get(i).toObject(ProofModel.class);
                            if (proof != null) {
                                proofModels.add(proof);
                            }
                        }

                        // Notify adapter of data change
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("fetchProofs", "No proofs found for this user.");
                        Toast.makeText(getContext(), "No proofs found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("fetchProofs", "Error fetching proofs: ", e);
                    Toast.makeText(getContext(), "Failed to fetch proofs.", Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(task -> {
                    // Hide progress bar regardless of success or failure
                    PBProofStorage.setVisibility(View.GONE);
                });
    }

}