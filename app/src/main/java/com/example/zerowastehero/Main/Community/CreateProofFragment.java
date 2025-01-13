package com.example.zerowastehero.Main.Community;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.DataBinding.ViewModel.SharedPostModel;
import com.example.zerowastehero.Main.Community.Interface.UploadCallbackInterface;
import com.example.zerowastehero.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateProofFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateProofFragment extends Fragment {

    private ArrayList<PostModel> postModels = new ArrayList<>();
    private SharedPostModel sharedPostModel;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Button BtnSubmitProof;
    private EditText ETProofDescriptionText;
    private ShapeableImageView IVCreateProofBeforeImage, IVCreateProofAfterImage;
    private ActivityResultLauncher<PickVisualMediaRequest> mediaPickerLauncher;
    private Uri beforeImageUri, afterImageUri;
    private boolean isBeforeImageSelected = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateProofFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateProofFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateProofFragment newInstance(String param1, String param2) {
        CreateProofFragment fragment = new CreateProofFragment();
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
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mediaPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        if (isBeforeImageSelected) {
                            beforeImageUri = uri;
                            IVCreateProofBeforeImage.setImageURI(uri);
                            IVCreateProofBeforeImage.setTag(uri.toString());
                        } else {
                            afterImageUri = uri;
                            IVCreateProofAfterImage.setImageURI(uri);
                            IVCreateProofAfterImage.setTag(uri.toString());
                        }
                    } else {
                        Log.d("MediaPicker", "No media selected");
                        Toast.makeText(requireContext(), "No media selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_proof, container, false);

        // Initialize view
        BtnSubmitProof = view.findViewById(R.id.BtnSubmitProof);
        ETProofDescriptionText = view.findViewById(R.id.ETProofDescriptionText);
        IVCreateProofBeforeImage = view.findViewById(R.id.IVCreateProofBeforeImage);
        IVCreateProofAfterImage = view.findViewById(R.id.IVCreateProofAfterImage);

        IVCreateProofBeforeImage.setTag("");
        IVCreateProofAfterImage.setTag("");
        IVCreateProofBeforeImage.setOnClickListener(v -> pickImage(true));
        IVCreateProofAfterImage.setOnClickListener(v -> pickImage(false));

        BtnSubmitProof.setEnabled(false);
        ETProofDescriptionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Check if text is not empty and both images are selected
                BtnSubmitProof.setEnabled(!charSequence.toString().trim().isEmpty() &&
                        !IVCreateProofBeforeImage.getTag().toString().isEmpty() &&
                        !IVCreateProofAfterImage.getTag().toString().isEmpty()); // Enable button
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        BtnSubmitProof.setOnClickListener(v -> submitProof());

        return view;
    }

    private void pickImage(boolean isBeforeImage) {
        isBeforeImageSelected = isBeforeImage;

        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build();

        mediaPickerLauncher.launch(request);
    }

    private void submitProof() {
        // Disable button to avoid multiple uploads
        BtnSubmitProof.setEnabled(false);

        String description = ETProofDescriptionText.getText().toString().trim();
        String beforeImageURL = IVCreateProofBeforeImage.getTag().toString().trim();
        String afterImageURL = IVCreateProofAfterImage.getTag().toString().trim();

        String userID = user.getUid();
        String email = user.getEmail();
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        String username = user.getUsername();

                        uploadImageToStorage(beforeImageURL, "before_image", (beforeImageStorageURL) -> {
                            uploadImageToStorage(afterImageURL, "after_image", (afterImageStorageURL) -> {
                                // Once both images are uploaded, create the proof in Firestore
                                createProof(username, userID, description, Uri.parse(beforeImageStorageURL), Uri.parse(afterImageStorageURL));
                            });
                        });Log.d("Firestore", "User Name: " + username + ", Email: " + email);
                    } else {
                        Log.e("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e))
                .addOnCompleteListener(task -> BtnSubmitProof.setEnabled(true));
    }

    private void uploadImageToStorage(String imageURL, String imageName, UploadCallbackInterface callback) {
        if (imageURL.isEmpty()) {
            callback.onUploadComplete(""); // If no image selected, return empty URL
            return;
        }

        Uri imageUri = Uri.parse(imageURL); // Convert the string URL to Uri
        StorageReference imageRef = storageReference.child("images/" + imageName + "/" + System.currentTimeMillis() + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadURL = uri.toString();
                        Log.d("FirebaseStorage", "Image uploaded successfully: " + downloadURL);
                        callback.onUploadComplete(downloadURL); // Callback with the image download URL
                    }).addOnFailureListener(e -> {
                        Log.e("FirebaseStorage", "Error getting download URL: ", e);
                        callback.onUploadComplete(""); // In case of failure, return empty URL
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseStorage", "Error uploading image: ", e);
                    callback.onUploadComplete(""); // In case of failure, return empty URL
                });
    }

    private void createProof(String username, String userID, String description, Uri beforeImageUri, Uri afterImageUri) {
        if (user == null) {
            Toast.makeText(getContext(), "User authentication failed. Please log in", Toast.LENGTH_SHORT).show();
            Log.e("CreateProofFragment", "FirebaseUser is null. Can't create a proof");
            return;
        }

        DocumentReference newPostRef = db.collection("posts").document();
        String postID = newPostRef.getId();

        PostModel newPost = new PostModel(description, userID, username, "", beforeImageUri.toString(), afterImageUri.toString(), "proof", new Timestamp(new Date()));
        newPost.setPostID(postID);

        // Add the post to Firestore
        newPostRef.set(newPost)
                .addOnSuccessListener(aVoid -> {
                    // Add the new post to the sharedPostModel
                    postModels.add(newPost);
                    sharedPostModel.setPosts(postModels);

                    Log.d("CreateProofFragment", "Proof successfully added to Firestore with ID: " + postID);

                    // Navigate back or give feedback to the user
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Log.e("CreateProofFragment", "Error adding proof to Firestore: ", e);

                    // Handle the error (e.g., show a message to the user)
                });
    }
}