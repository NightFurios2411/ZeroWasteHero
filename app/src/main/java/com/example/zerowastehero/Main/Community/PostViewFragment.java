package com.example.zerowastehero.Main.Community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.DataBinding.Model.ReplyModel;
import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.DataBinding.ViewModel.SharedPostModel;
import com.example.zerowastehero.DataBinding.ViewModel.SharedReplyModel;
import com.example.zerowastehero.Main.Community.Adapter.ReplyAdapter;
import com.example.zerowastehero.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostViewFragment extends Fragment{

    private ImageView createReplyUserProfile;
    private EditText ETReplyText;
    private Button BtnSubmitReply;
    private BottomNavigationView BottomNavView;

    private ArrayList<ReplyModel> replyModels = new ArrayList<>();
    private ReplyAdapter adapter;
    private PostModel post;
    private RecyclerView RVReply;
    private ProgressBar PBPostView;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostViewFragment newInstance(String param1, String param2) {
        PostViewFragment fragment = new PostViewFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_view, container, false);

        // Initialize view
        createReplyUserProfile = view.findViewById(R.id.createReplyUserProfile);
        ETReplyText = view.findViewById(R.id.ETReplyText);
        BtnSubmitReply = view.findViewById(R.id.BtnSubmitReply);
        BottomNavView = getActivity().findViewById(R.id.BottomNavView);
        PBPostView = view.findViewById(R.id.PBPostView);

        BottomNavView.setVisibility(View.GONE);

        Bundle bundle = this.getArguments();
        String postID = null;
        if (bundle != null) {
            postID = bundle.getString("postID");
            Log.d("PostViewFragment", "postID from Bundle: " + postID);
        }

        // Initialize RecyclerView
        RVReply = view.findViewById(R.id.RVReply);
        RVReply.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter
        adapter = new ReplyAdapter(getContext(), replyModels, null); // Post is null initially
        RVReply.setAdapter(adapter);

        // Fetch the post and replies
        String finalPostID = postID;
        fetchPost(finalPostID);
//        fetchReplies(finalPostID);

        ETReplyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                BtnSubmitReply.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        BtnSubmitReply.setOnClickListener(v -> submitReply(finalPostID));

        return view;
    }

    private void submitReply(String postID) {
        String replyText = ETReplyText.getText().toString().trim();

        String userID = user.getUid();
        String email = user.getEmail();
        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        String username = user.getUsername();
                        createReply(username, postID, userID, replyText);
                        Log.d("Firestore", "User Name: " + username + ", Email: " + email);
                    } else {
                        Log.d("Firestore", "No such user found!");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));

    }

    private void createReply(String userName, String postID, String userID, String replyText) {
        if (user == null) {
            Toast.makeText(getContext(), "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e("CreatePostFragment", "FirebaseUser is null. Cannot create a post.");
            return;
        }

        DocumentReference newPostRef = db.collection("replies").document();
        String replyID = newPostRef.getId();

        ReplyModel newReply = new ReplyModel(replyText, postID, userID, userName, new Timestamp(new Date()));
        newReply.setReplyID(replyID);

        // Add the reply to Firestore
        newPostRef.set(newReply)
                .addOnSuccessListener(aVoid -> {
                    // Clear the EditText and log success
                    ETReplyText.setText("");
                    Log.d("CreateReplyFragment", "Reply successfully added to Firestore with ID: " + replyID);

                    // Update the reply count in the post
                    updateReplyCount(postID); // Call the method to update the reply count
                })
                .addOnFailureListener(e -> {
                    Log.e("CreateReplyFragment", "Error adding reply to Firestore: ", e);
                });
    }

    private void updateReplyCount(String postID) {
        // Get the current post and its reply count
        db.collection("posts").document(postID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        PostModel post = documentSnapshot.toObject(PostModel.class);

                        if (post != null) {
                            // Increment the reply count
                            int currentReplyCount = post.getReplyCount(); // Assuming you have a getter for replyCount
                            int newReplyCount = currentReplyCount + 1;

                            // Update the reply count in the post document
                            documentSnapshot.getReference()
                                    .update("replyCount", newReplyCount)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("PostViewFragment", "Reply count updated to: " + newReplyCount);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("PostViewFragment", "Error updating reply count: ", e);
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PostViewFragment", "Error fetching post to update reply count: ", e);
                });
    }

    private void fetchPost(String finalPostID) {
        if (finalPostID == null || finalPostID.isEmpty()) {
            Log.e("PostViewFragment", "Invalid PostID: " + finalPostID);
            return;
        }

        db.collection("posts")
                .document(finalPostID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        post = documentSnapshot.toObject(PostModel.class);

                        if (post != null) {
                            Log.d("FetchPost", "Post fetched successfully: " + post.getPostID());
                            adapter.setPost(post); // Update the adapter with the fetched post
                            adapter.notifyDataSetChanged();
                            fetchReplies(finalPostID);
                        } else {
                            Log.e("FetchPost", "Post object is null");
                        }
                    } else {
                        Log.e("FetchPost", "No such document exists");
                    }
                })
                .addOnFailureListener(e -> Log.e("FetchPost", "Error fetching post", e));
    }

    private void fetchReplies(String finalPostID) {
        if (finalPostID == null || finalPostID.isEmpty()) {
            Log.e("PostViewFragment", "Invalid PostID: " + finalPostID);
            return;
        }

        db.collection("replies")
                .whereEqualTo("postID", finalPostID)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e("PostViewFragment", "Error fetching replies", e);
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        replyModels.clear(); // Clear only if new data exists
                        for (var doc : queryDocumentSnapshots) {
                            ReplyModel reply = doc.toObject(ReplyModel.class);
                            replyModels.add(reply);
                        }
                        Log.d("PostViewFragment", "Replies fetched: " + replyModels.size());
                        adapter.setReplyModels(replyModels);
                        adapter.notifyDataSetChanged();
                        PBPostView.setVisibility(View.GONE);
                    } else {
                        Log.d("PostViewFragment", "No replies found for PostID: " + finalPostID);
                    }
                });
    }
}