package com.example.zerowastehero.Main.Community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.example.zerowastehero.Main.Community.Adapter.PostAdapter;
import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {

//    private RecyclerView recyclerView;
//    private PostAdapter postAdapter;
//    private List<PostModel> postList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
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
        View view = inflater.inflate(R.layout.fragment_community, container, false);

//        recyclerView = view.findViewById(R.id.RVPost);
//
//        // Initialize RecyclerView and Adapter
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        postAdapter = new PostAdapter(postList);
//        recyclerView.setAdapter(postAdapter);
//
//        // Add simple data (you can replace this with your actual data)
//        loadPosts();

        return view;
    }

//    private void loadPosts() {
//        postList.add(new PostModel("Title 1", "Description 1"));
//        postList.add(new PostModel("Title 2", "Description 2"));
//        postList.add(new PostModel("Title 3", "Description 3"));
//        postAdapter.notifyDataSetChanged();
//    }
}