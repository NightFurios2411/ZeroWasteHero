package com.example.zerowastehero.Main.Home;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.DataBinding.Model.ProofModel;
import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadProofFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadProofFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private UserModel user;
    private String userID, userName;
    private Uri imageUri;

    private String habitType;
    private ProgressBar PBUploadProof;
    private Button BtnUploadFile, BtnUploadProof;
    private ImageView IVImageSelected;
    private ActivityResultLauncher<PickVisualMediaRequest> mediaPickerLauncher;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final ActivityResultContracts.GetContent contentPicker = new ActivityResultContracts.GetContent();

    public UploadProofFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadProofFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadProofFragment newInstance(String param1, String param2) {
        UploadProofFragment fragment = new UploadProofFragment();
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
        storage = FirebaseStorage.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        Bundle arguments = getArguments();
        if (arguments != null) {
            habitType = arguments.getString("habitType");
        }

        // Initialize the media picker launcher in onCreate
        mediaPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        // Handle the selected media URI
                        Log.d("MediaPicker", "Selected URI: " + uri);
                        // Example: Display the selected image in an ImageView
                        IVImageSelected.setVisibility(View.VISIBLE);
                        BtnUploadProof.setVisibility(View.VISIBLE);
                        IVImageSelected.setImageURI(uri);
                        IVImageSelected.setTag(uri);
                    } else {
                        Log.d("MediaPicker", "No media selected");
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_proof, container, false);

        BtnUploadFile = view.findViewById(R.id.BtnUploadFile);
        BtnUploadProof = view.findViewById(R.id.BtnUploadProof);
        IVImageSelected = view.findViewById(R.id.IVImageSelected);
        PBUploadProof = view.findViewById(R.id.PBUploadProof);

        fetchUser();

        BtnUploadFile.setOnClickListener(v -> pickImage());
        BtnUploadProof.setOnClickListener(v -> uploadProof(habitType));

        return view;
    }

    private void pickImage() {
        // Create a PickVisualMediaRequest to specify the type of media
        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build();

        // Launch the media picker
        IVImageSelected.setTag("");
        mediaPickerLauncher.launch(request);
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
                        user = documentSnapshot.toObject(UserModel.class);
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }

    private void uploadProof(String habitType) {
        if (user == null) {
            Toast.makeText(getContext(), "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e("Upload Proof Fragment", "FirebaseUser is null. Can't create a proof.");
            return;
        }
        BtnUploadProof.setVisibility(View.GONE);
        PBUploadProof.setVisibility(View.VISIBLE);

        // Get the current date to track the reset
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Reference to the user's document in Firestore
        DocumentReference userRef = db.collection("users").document(userID);

        // Check if the proof upload tracker is reset today
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String lastResetDate = documentSnapshot.getString("lastResetDate");
                long recycleProofCount = documentSnapshot.getLong("recycleProofCount") != null ? documentSnapshot.getLong("recycleProofCount") : 0;
                long totalTrash = documentSnapshot.getLong("totalTrash") != null ? documentSnapshot.getLong("totalTrash") : 0;
                long trashCollectProofCount = documentSnapshot.getLong("trashCollectProofCount") != null ? documentSnapshot.getLong("trashCollectProofCount") : 0;
                long totalRecycle = documentSnapshot.getLong("totalRecycle") != null ? documentSnapshot.getLong("totalRecycle") : 0;
                long totalPoints = documentSnapshot.getLong("totalPoints") != null ? documentSnapshot.getLong("totalPoints") : 0;

                // If the tracker was not reset today, reset the counts
                if (lastResetDate == null || !lastResetDate.equals(currentDate)) {
                    // Reset proof counts and update lastResetDate
                    userRef.update("recycleProofCount", 0, "trashCollectProofCount", 0, "lastResetDate", currentDate)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Upload Proof Fragment", "Tracker reset successfully.");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Upload Proof Fragment", "Error resetting tracker: ", e);
                            });
                }

                // Check if the user has already uploaded 5 proofs for the current habit type
                if (habitType.equals("Recycle") && recycleProofCount >= 5) {
                    Toast.makeText(getContext(), "You can only upload 5 proofs for recycling items per day.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (habitType.equals("TrashCollect") && trashCollectProofCount >= 5) {
                    Toast.makeText(getContext(), "You can only upload 5 proofs for trash collection per day.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Generate a unique filename for the image
                String fileName = "images/posts/" + userID + "/proof/" + System.currentTimeMillis() + ".jpg";

                // Reference to Firebase Storage
                StorageReference storageRef = storage.getReference().child(fileName);

                // Upload the image
                storageRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Get the URL of the uploaded image
                            storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                String imageURL = downloadUri.toString();

                                // After the image is uploaded, create the proof post
                                DocumentReference newProofRef = db.collection("proofs").document();
                                String proofID = newProofRef.getId();

                                ProofModel newProof = new ProofModel(imageURL, userID, userName, new Timestamp(new Date()), habitType);
                                newProof.setProofID(proofID);

                                // Save the post details in Firestore
                                newProofRef.set(newProof)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Upload Proof Fragment", "Proof with image successfully added to Firestore with ID: " + proofID);

                                            // Increment the proof count for the respective habit type
                                            if (habitType.equals("Recycle")) {
                                                userRef.update("recycleProofCount", recycleProofCount + 1);
                                                userRef.update("totalRecycle", totalRecycle + 1);
                                                // Add 2 points for recycling proof
                                                userRef.update("recyclePoints", documentSnapshot.getLong("recyclePoints") + 2);
                                            } else if (habitType.equals("TrashCollect")) {
                                                userRef.update("trashCollectProofCount", trashCollectProofCount + 1);
                                                userRef.update("totalTrash", totalTrash + 1);
                                                // Add 2 points for trash collection proof
                                                userRef.update("trashCollectPoints", documentSnapshot.getLong("trashCollectPoints") + 2);
                                            }

                                            // Increment total points by 2 for any proof upload
                                            userRef.update("totalPoints", totalPoints + 2)
                                                    .addOnSuccessListener(aVoid2 -> {
                                                        Log.d("Upload Proof Fragment", "Points updated successfully.");
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("Upload Proof Fragment", "Error updating points: ", e);
                                                    });

                                            // Navigate back or give feedback to the user
                                            requireActivity().getSupportFragmentManager().popBackStack();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("CreatePostFragment", "Error adding post to Firestore: ", e);
                                            Toast.makeText(getContext(), "Failed to save post in Firestore.", Toast.LENGTH_SHORT).show();
                                        });
                            }).addOnFailureListener(e -> {
                                Log.e("CreatePostFragment", "Error getting download URL: ", e);
                                Toast.makeText(getContext(), "Failed to retrieve image URL.", Toast.LENGTH_SHORT).show();
                            });
                        })
                        .addOnFailureListener(e -> {
                            Log.e("CreatePostFragment", "Error uploading image: ", e);
                            Toast.makeText(getContext(), "Failed to upload image to Firebase.", Toast.LENGTH_SHORT).show();
                        });
            }
        }).addOnFailureListener(e -> {
            Log.e("Upload Proof Fragment", "Error fetching user data: ", e);
            Toast.makeText(getContext(), "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
        });
    }


}