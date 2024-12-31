package com.example.zerowastehero.Main.Community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.DataBinding.Model.ReplyModel;
import com.example.zerowastehero.DataBinding.ViewModel.SharedPostModel;
import com.example.zerowastehero.DataBinding.ViewModel.SharedReplyModel;
import com.example.zerowastehero.Main.Community.Adapter.ReplyAdapter;
import com.example.zerowastehero.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostViewFragment extends Fragment {

    private ArrayList<PostModel> postModels = new ArrayList<>();
    private ArrayList<ReplyModel> replyModels = new ArrayList<>();
    private SharedReplyModel sharedReplyModel;
    private SharedPostModel sharedPostModel;
    private ReplyAdapter adapter;

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
        sharedReplyModel = new ViewModelProvider(requireActivity()).get(SharedReplyModel.class);
        sharedPostModel = new ViewModelProvider(requireActivity()).get(SharedPostModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_view, container, false);

        Bundle bundle = this.getArguments();
        String postID = null;
        if (bundle != null) {
            postID = bundle.getString("postID");
            Log.d("PostViewFragment", "postID from Bundle: " + postID);
        }

        // Observe the LiveData from SharedPostModel
        String finalPostID = postID; // Make postID effectively final for lambda use
        sharedPostModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            if (posts != null) {
                postModels.clear(); // Clear old data
                postModels.addAll(posts); // Add new posts
                Log.d("PostViewFragment", "Posts updated: " + posts.size());

                // Find the matching post after the list is updated
                if (finalPostID != null) {
                    for (PostModel post : postModels) {
                        Log.d("PostViewFragment", "Comparing postID: " + finalPostID + " with " + post.getPostID());
                        if (post.getPostID().equals(finalPostID)) {
                            Log.d("PostViewFragment", "Post found with ID: " + finalPostID);

                            // Set up RecyclerView for replies
                            RecyclerView recyclerView = view.findViewById(R.id.RVReply);
                            if (adapter == null) {
                                adapter = new ReplyAdapter(getContext(), replyModels, post);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                }
            }
        });

        // Observe the LiveData from SharedReplyModel
        sharedReplyModel.getReplies().observe(getViewLifecycleOwner(), replies -> {
            if (replies != null) {
                replyModels.clear(); // Clear old data
                replyModels.addAll(replies); // Add new replies
                Log.d("PostViewFragment", "Replies updated: " + replies.size());
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}