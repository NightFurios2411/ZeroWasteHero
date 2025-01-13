package com.example.zerowastehero.Main.Community;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Cache.CurrentUser;
import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.DataBinding.ViewModel.SharedPostModel;
import com.example.zerowastehero.Main.Community.Adapter.PostAdapter;
import com.example.zerowastehero.R;
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
 * Use the {@link CreatePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePostFragment extends Fragment {

    ArrayList<PostModel> postModels = new ArrayList<PostModel>();
    SharedPostModel sharedPostModel;
    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private FirebaseStorage storage;

    private ProgressBar PBCreatePost;
    ImageView IVSearchImage, IVImageSelected;
    Button BtnSubmitPost;
    EditText ETPostDescriptionText;
    ActivityResultLauncher<PickVisualMediaRequest> mediaPickerLauncher;

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePostFragment.
     */
    // Rename and change types and number of parameters
    public static CreatePostFragment newInstance(String param1, String param2) {
        CreatePostFragment fragment = new CreatePostFragment();
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
        sharedPostModel = new ViewModelProvider(requireActivity()).get(SharedPostModel.class);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();

        if (sharedPostModel.getPosts().getValue() != null) {
            postModels = new ArrayList<>(sharedPostModel.getPosts().getValue());
        } else {
            postModels = new ArrayList<>(); // Ensure it's never null
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
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        // Initialize views
        IVSearchImage = view.findViewById(R.id.IVImageSearch);
        IVImageSelected = view.findViewById(R.id.IVImageSelected);
        BtnSubmitPost = view.findViewById(R.id.BtnSubmitPost);
        ETPostDescriptionText = view.findViewById(R.id.ETPostDescriptionText);
        PBCreatePost = view.findViewById(R.id.PBCreatePost);

        IVSearchImage.setOnClickListener(v -> pickImage());

        BtnSubmitPost.setEnabled(false);
        ETPostDescriptionText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                BtnSubmitPost.setEnabled(!charSequence.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        BtnSubmitPost.setOnClickListener(v -> submitPost());

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

    private void submitPost() {
        // Disable button to avoid multiple uploads
        BtnSubmitPost.setEnabled(false);
        PBCreatePost.setVisibility(View.VISIBLE);

        String description = ETPostDescriptionText.getText().toString().trim();
        String postImageURL;
        if (IVImageSelected.getTag() != null)
            postImageURL = IVImageSelected.getTag().toString().trim();
        else {
            postImageURL = "";
        }

        String userID = user.getUid();
        String email = user.getEmail();
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        String username = user.getUsername();
                        if (postImageURL.isEmpty() || postImageURL == null) {
                            createPostWithoutImage(username, userID, description);
                        } else {
                            createPostWithImage(username, userID, description, Uri.parse(postImageURL));
                        }
                        // Use these details in your activity
                        Log.d("Firestore", "User Name: " + username + ", Email: " + email);
                    } else {
                        Log.e("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e))
                .addOnCompleteListener(task -> BtnSubmitPost.setEnabled(true));
    }

    private void createPostWithImage(String userName, String userID, String description, Uri imageUri) {
        if (user == null) {
            Toast.makeText(getContext(), "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e("CreatePostFragment", "FirebaseUser is null. Can't create a post.");
            return;
        }

        // Generate a unique filename for the image
        String fileName = "images/posts/" + userID + "/" + System.currentTimeMillis() + ".jpg";

        // Reference to Firebase Storage
        StorageReference storageRef = storage.getReference().child(fileName);

        // Upload the image
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the URL of the uploaded image
                    storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        String imageURL = downloadUri.toString();

                        // After the image is uploaded, create the post
                        DocumentReference newPostRef = db.collection("posts").document();
                        String postID = newPostRef.getId();

                        PostModel newPost = new PostModel(description, userID, userName, imageURL, "", "", "post", new Timestamp(new Date()));
                        newPost.setPostID(postID);

                        // Save the post details in Firestore
                        newPostRef.set(newPost)
                                .addOnSuccessListener(aVoid -> {
                                    // Add the new post to the sharedPostModel
                                    postModels.add(newPost);
                                    sharedPostModel.setPosts(postModels);

                                    Log.d("CreatePostFragment", "Post with image successfully added to Firestore with ID: " + postID);

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

    private void createPostWithoutImage(String userName,String userID, String description) {
        if (user == null) {
            Toast.makeText(getContext(), "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e("CreatePostFragment", "FirebaseUser is null. Can't create a post.");
            return;
        }

        DocumentReference newPostRef = db.collection("posts").document();
        String postID = newPostRef.getId();

        PostModel newPost = new PostModel(description, userID, userName, "", "", "", "post", new Timestamp(new Date()));
        newPost.setPostID(postID);

        // Add the post to Firestore
        newPostRef.set(newPost)
                .addOnSuccessListener(aVoid -> {
                    // Add the new post to the sharedPostModel
                    postModels.add(newPost);
                    sharedPostModel.setPosts(postModels);

                    Log.d("CreatePostFragment", "Post successfully added to Firestore with ID: " + postID);

                    // Navigate back or give feedback to the user
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Log.e("CreatePostFragment", "Error adding post to Firestore: ", e);

                    // Handle the error (e.g., show a message to the user)
                });
    }
}