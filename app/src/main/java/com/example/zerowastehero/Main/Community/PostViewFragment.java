package com.example.zerowastehero.Main.Community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zerowastehero.DataBinding.Model.ReplyModel;
import com.example.zerowastehero.Main.Community.Adapter.ReplyAdapter;
import com.example.zerowastehero.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostViewFragment extends Fragment {

    private ArrayList<ReplyModel> replyModels = new ArrayList<>();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_view, container, false);

        setupReplyModels();

        // Set up RecyclerView for reply
        RecyclerView recyclerView = view.findViewById(R.id.RVReply);
        ReplyAdapter adapter = new ReplyAdapter(getContext(), replyModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private void setupReplyModels() {
        replyModels.add(new ReplyModel("Great job! Keep it up."));
        replyModels.add(new ReplyModel("Amazing effort! You’re an inspiration."));
        replyModels.add(new ReplyModel("Let’s join hands to clean up together!"));
        replyModels.add(new ReplyModel("Wow, that DIY tip is super creative. Thanks!"));
        replyModels.add(new ReplyModel("I’ll bring my friends for the clean-up this weekend."));
        replyModels.add(new ReplyModel("Such a thoughtful initiative. Kudos to you!"));
        replyModels.add(new ReplyModel("Turtles are lucky to have you around!"));
        replyModels.add(new ReplyModel("Eco-friendly goals! Love the tote bag idea."));
        replyModels.add(new ReplyModel("Count me in for the playground clean-up."));
        replyModels.add(new ReplyModel("Amazing teamwork! Keep the environment clean."));
    }
}