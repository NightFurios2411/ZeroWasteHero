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
    UserModel currentUser;
    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    ImageView IVSearchImage, IVImageSelected;
    Button BtnSubmitPost;
    EditText ETPostDescriptionText, ETPostTitleText;
    ActivityResultLauncher<PickVisualMediaRequest> mediaPickerLauncher;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
    // TODO: Rename and change types and number of parameters
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

        if (sharedPostModel.getPosts().getValue() != null) {
            postModels = new ArrayList<>(sharedPostModel.getPosts().getValue());
        } else {
            postModels = new ArrayList<>(); // Ensure it's never null
        }
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
        ETPostTitleText = view.findViewById(R.id.ETPostTitleText);

        IVSearchImage.setOnClickListener(v -> {
            // Create a PickVisualMediaRequest to specify the type of media
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build();

            // Launch the media picker
            mediaPickerLauncher.launch(request);
        });

        IVImageSelected.setTag("");
        mediaPickerLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
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
        });

        ETPostTitleText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean titleFilled = !ETPostTitleText.getText().toString().trim().isEmpty();
                boolean descriptionFilled = !ETPostDescriptionText.getText().toString().trim().isEmpty();
                bothEditTextFilled(titleFilled, descriptionFilled);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        ETPostDescriptionText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean titleFilled = !ETPostTitleText.getText().toString().trim().isEmpty();
                boolean descriptionFilled = !ETPostDescriptionText.getText().toString().trim().isEmpty();
                bothEditTextFilled(titleFilled, descriptionFilled);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        BtnSubmitPost.setOnClickListener(v -> submitPost());

        return view;
    }

    private void submitPost() {
        String title = ETPostTitleText.getText().toString().trim();
        String description = ETPostDescriptionText.getText().toString().trim();
        String postImageURL = IVImageSelected.getTag().toString().trim();

        String userID = user.getUid();
        String email = user.getEmail();
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        String username = user.getUsername();
                        if (postImageURL.isEmpty() || postImageURL == null) {
                            createPostWithoutImage(username, userID, title, description);
                        } else {
                            createPostWithImage(username, userID, title, description, Uri.parse(postImageURL));
                        }
                        // Use these details in your activity
                        Log.d("Firestore", "User Name: " + username + ", Email: " + email);
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }

    private void bothEditTextFilled(boolean text1, boolean text2) {
        BtnSubmitPost.setEnabled(text1 && text2);
    }

    private void createPostWithImage(String userName, String userID, String title,String description, Uri imageUri) {

    }

    private void createPostWithoutImage(String userName,String userID, String title, String description) {
        if (user == null) {
            Toast.makeText(getContext(), "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e("CreatePostFragment", "FirebaseUser is null. Cannot create a post.");
            return;
        }

        DocumentReference newPostRef = db.collection("posts").document();
        String postID = newPostRef.getId();

        PostModel newPost = new PostModel(title, description, userID, userName, "", new Timestamp(new Date()));
        newPost.setPostID(postID);
        newPost.setPostType("post");

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