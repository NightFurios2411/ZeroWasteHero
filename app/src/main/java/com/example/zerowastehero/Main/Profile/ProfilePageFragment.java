package com.example.zerowastehero.Main.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePageFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_CAMERA_REQUEST = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 3;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private UserModel user;
    private FirebaseFirestore db;
    private ActivityResultLauncher<PickVisualMediaRequest> mediaPickerLauncher;

    private ImageView IVProfilePic;
    private TextView TVProfileUserName, TVProfileUserBio, TVProfileUserPoint;
    private ImageButton IBChallengesView, IBLoyaltyView, IBRedeemedView, IBMyStatsView, IBLeaderboardView;
    private ImageButton IBPointClaim1, IBPointClaim2, IBPointClaim3, IBPointClaimUser;
    private LinearLayout LLPointClaim1, LLPointClaim2, LLPointClaim3, LLProfilePageClaimReward;
    private int selectedPoints = 0;
    private boolean isPointClaim1Selected = false;
    private boolean isPointClaim2Selected = false;
    private boolean isPointClaim3Selected = false;

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    IVProfilePic.setImageURI(selectedImageUri); // Set the selected image
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                    IVProfilePic.setImageBitmap(photo); // Set the captured image
                }
            });

    private void openGallery() {
        // Create a PickVisualMediaRequest to specify the type of media
        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build();

        // Launch the media picker
        IVProfilePic.setTag("");
        mediaPickerLauncher.launch(request);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }


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
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        // Initialize the media picker launcher in onCreate
        mediaPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        // Handle the selected media URI
                        Log.d("MediaPicker", "Selected URI: " + uri);
                        // Example: Display the selected image in an ImageView
                        IVProfilePic.setImageURI(uri);
                        IVProfilePic.setTag(uri);
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
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        // Initialize view
        IBChallengesView = view.findViewById(R.id.IBChallengesView);
        IBLoyaltyView = view.findViewById(R.id.IBLoyaltyView);
        IBRedeemedView = view.findViewById(R.id.IBRedeemedView);
        IBMyStatsView = view.findViewById(R.id.IBMyStatsView);
        IBLeaderboardView = view.findViewById(R.id.IBLeaderboardView);
        TVProfileUserName = view.findViewById(R.id.TVProfileUserName);
        TVProfileUserBio = view.findViewById(R.id.TVProfileUserBio);
        TVProfileUserPoint = view.findViewById(R.id.TVProfileUserPoint);
        IBPointClaim1 = view.findViewById(R.id.IBPointClaim1);
        IBPointClaim2 = view.findViewById(R.id.IBPointClaim2);
        IBPointClaim3 = view.findViewById(R.id.IBPointClaim3);
        IBPointClaimUser = view.findViewById(R.id.IBPointClaimUser);
        LLPointClaim1 = view.findViewById(R.id.LLPointClaim1);
        LLPointClaim2 = view.findViewById(R.id.LLPointClaim2);
        LLPointClaim3 = view.findViewById(R.id.LLPointClaim3);
        IVProfilePic = view.findViewById(R.id.IVProfilePic);
        LLProfilePageClaimReward = view.findViewById(R.id.LLProfilePageClaimReward);

        fetchUser();

        // Set Profile Pic
        IVProfilePic.setOnClickListener(v -> showImagePickerOptions());

        // Navigation
        IBChallengesView.setOnClickListener(v -> viewNavigation(v, R.id.DestChallenges));
        IBLoyaltyView.setOnClickListener(v -> viewNavigation(v, R.id.DestLoyalty));
        IBRedeemedView.setOnClickListener(v -> viewNavigation(v, R.id.DestRedeemed));
        IBMyStatsView.setOnClickListener(v -> viewNavigation(v, R.id.DestMyStats));
        IBLeaderboardView.setOnClickListener(v -> viewNavigation(v, R.id.DestLeaderboard));

        // Point claim button click listeners
        IBPointClaim1.setOnClickListener(v -> toggleRewardClaim(500, IBPointClaim1, 1));  // Select 500 points
        IBPointClaim2.setOnClickListener(v -> toggleRewardClaim(1000, IBPointClaim2, 2));  // Select 1000 points
        IBPointClaim3.setOnClickListener(v -> toggleRewardClaim(2000, IBPointClaim3, 3));  // Select 2000 points

        // Claim points button click listener
        IBPointClaimUser.setEnabled(false);
        IBPointClaimUser.setOnClickListener(v -> {
            if (selectedPoints > 0) {
                rewardClaim(selectedPoints);  // Claim the selected points
                resetPointClaims();
            } else {
                Toast.makeText(getContext(), "Please select a point option first!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void showImagePickerOptions() {
        // Show options for Gallery or Camera
        String[] options = {"Choose from Gallery", "Take a Photo"};
        new AlertDialog.Builder(getContext())
                .setTitle("Set Profile Picture")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Choose from Gallery
                        openGallery();
                    } else if (which == 1) {
                        // Take a Photo
                        openCamera();
                    }
                })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri selectedImageUri = data.getData();
                IVProfilePic.setImageURI(selectedImageUri); // Set the selected image
                // Optionally upload the image to Firebase storage
                uploadProfilePicture(selectedImageUri);
            } else if (requestCode == PICK_CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                IVProfilePic.setImageBitmap(photo); // Set the captured image
                // Optionally upload the image to Firebase storage
                Uri imageUri = getImageUri(getContext(), photo);
                uploadProfilePicture(imageUri);
            }
        }
    }

    // You can use this utility method to convert Bitmap to Uri:
    private Uri getImageUri(Context context, Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera(); // Permission granted, proceed to open camera
            } else {
                Toast.makeText(getContext(), "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toggleRewardClaim(int point, ImageButton imageButton, int claimNumber) {
        if (claimNumber == 1 && isPointClaim1Selected) {
            // Untick: Remove points and reset button background
            selectedPoints -= point;
            isPointClaim1Selected = false;
            imageButton.setBackgroundResource(R.drawable.image_unticked);  // Reset to default background
        } else if (claimNumber == 1) {
            // Tick: Add points and change button background
            selectedPoints += point;
            isPointClaim1Selected = true;
            imageButton.setBackgroundResource(R.drawable.image_ticked);  // Show ticked background
        } else if (claimNumber == 2 && isPointClaim2Selected) {
            // Untick: Remove points and reset button background
            selectedPoints -= point;
            isPointClaim2Selected = false;
            imageButton.setBackgroundResource(R.drawable.image_unticked);  // Reset to default background
        } else if (claimNumber == 2) {
            // Tick: Add points and change button background
            selectedPoints += point;
            isPointClaim2Selected = true;
            imageButton.setBackgroundResource(R.drawable.image_ticked);  // Show ticked background
        } else if (claimNumber == 3 && isPointClaim3Selected) {
            // Untick: Remove points and reset button background
            selectedPoints -= point;
            isPointClaim3Selected = false;
            imageButton.setBackgroundResource(R.drawable.image_unticked);  // Reset to default background
        } else if (claimNumber == 3) {
            // Tick: Add points and change button background
            selectedPoints += point;
            isPointClaim3Selected = true;
            imageButton.setBackgroundResource(R.drawable.image_ticked);  // Show ticked background
        }

        Log.d("PointClaim", "Selected Points: " + selectedPoints);
        // Enable the claim points button if points are selected
        IBPointClaimUser.setEnabled(selectedPoints > 0);
    }

    private void rewardClaim(int point) {
        String userID = firebaseUser.getUid();

        // Get the user's document reference
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the current points
                        user = documentSnapshot.toObject(UserModel.class);
                        int currentPoints = user.getPoint();

                        // Minus the claimed reward to the current points
                        int updatedPoints = currentPoints - point;

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("point", updatedPoints);
                        if (isPointClaim1Selected) {
                            user.setPointClaimed1(true);
                            updates.put("pointClaimed1", true);
                        }
                        if (isPointClaim2Selected) {
                            user.setPointClaimed2(true);
                            updates.put("pointClaimed2", true);
                        }
                        if (isPointClaim3Selected) {
                            user.setPointClaimed3(true);
                            updates.put("pointClaimed3", true);
                        }

                        // Update the points in Firestore
                        db.collection("users").document(userID)
                                .update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    // Update the UI to show the new points
                                    TVProfileUserPoint.setText(String.valueOf(updatedPoints));
                                    Log.d("Firestore", "Points and claim updated successfully");
                                })
                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating points", e));
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }

    private void uploadProfilePicture(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("profile_pictures").child(firebaseUser.getUid() + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL and save it in Firestore
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String profilePicUrl = uri.toString();
                        db.collection("users").document(firebaseUser.getUid())
                                .update("profilePicUrl", profilePicUrl)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firestore", "Profile picture URL saved successfully");
                                })
                                .addOnFailureListener(e -> Log.e("Firestore", "Error saving profile picture URL", e));
                    });
                })
                .addOnFailureListener(e -> Log.e("Storage", "Error uploading image", e));
    }

    private void resetPointClaims() {
        // Reset the selected points and untick all point claim buttons
        selectedPoints = 0;
        if (isPointClaim1Selected) {
            isPointClaim1Selected = false;
            LLPointClaim1.setVisibility(View.GONE);
        }
        if (isPointClaim2Selected) {
            isPointClaim2Selected = false;
            LLPointClaim2.setVisibility(View.GONE);
        }
        if (isPointClaim3Selected) {
            isPointClaim3Selected = false;
            LLPointClaim3.setVisibility(View.GONE);
        }

        // Reset all buttons to their default state
        IBPointClaim1.setBackgroundResource(R.drawable.image_unticked);
        IBPointClaim2.setBackgroundResource(R.drawable.image_unticked);
        IBPointClaim3.setBackgroundResource(R.drawable.image_unticked);

        // Disable the claim points button since no points are selected
        IBPointClaimUser.setEnabled(false);
    }

    private void fetchUser() {
        String userID = firebaseUser.getUid();
        String email = firebaseUser.getEmail();
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        user = documentSnapshot.toObject(UserModel.class);
                        String username = user.getUsername();
                        String bio = user.getBio();
                        String point = String.valueOf(user.getPoint());
                        TVProfileUserName.setText(username);
                        TVProfileUserBio.setText(bio);
                        TVProfileUserPoint.setText(point);
                        if (!user.isPointClaimed1() || !user.isPointClaimed2() || !user.isPointClaimed3()) {
                            LLProfilePageClaimReward.setVisibility(View.VISIBLE);
                            if (!user.isPointClaimed1()) LLPointClaim1.setVisibility(View.VISIBLE);
                            if (!user.isPointClaimed2()) LLPointClaim2.setVisibility(View.VISIBLE);
                            if (!user.isPointClaimed3()) LLPointClaim3.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }

    private void viewNavigation(View view, int destID) {
        Navigation.findNavController(view).navigate(destID);
    }
}