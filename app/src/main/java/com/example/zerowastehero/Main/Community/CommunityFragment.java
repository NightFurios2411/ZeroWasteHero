package com.example.zerowastehero.Main.Community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


//import com.example.zerowastehero.Main.Community.Adapter.PostAdapter;
import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.Main.Community.Adapter.PostAdapter;
import com.example.zerowastehero.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {

    private ArrayList<PostModel> postModels = new ArrayList<>();
    private FloatingActionButton FABCommunity, FABCreatePost, FABCreateProof;
    private TextView TVCreatePost, TVCreateProof;
    private View darkOverlayCommunity, darkOverlay;
    private boolean isFABOpen = false;

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

        FABCommunity = view.findViewById(R.id.FABCommunity);
        FABCreatePost = view.findViewById(R.id.FABCreatePost);
        FABCreateProof = view.findViewById(R.id.FABCreateProof);
        darkOverlayCommunity = view.findViewById(R.id.DarkOverlayCommunity);
        darkOverlay = requireActivity().findViewById(R.id.darkOverlay);
        TVCreatePost = view.findViewById(R.id.TVCreatePost);
        TVCreateProof = view.findViewById(R.id.TVCreateProof);

        FABCommunity.setOnClickListener(v -> toggleFABMenu());
        darkOverlayCommunity.setOnClickListener(v -> closeFABMenu());
//        darkOverlay.setOnClickListener(v -> closeFABMenu());
        FABCreatePost.setOnClickListener(v -> {
            closeFABMenu();
            Navigation.findNavController(view).navigate(R.id.DestCreatePost);
        });
        FABCreateProof.setOnClickListener(v -> {
            closeFABMenu();
            Navigation.findNavController(view).navigate(R.id.DestCreateProof);
        });

        setupPostModels();

        RecyclerView recyclerView = view.findViewById(R.id.RVPost);
        PostAdapter adapter = new PostAdapter(getContext(), postModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private void toggleFABMenu() {
        if(isFABOpen) {
            closeFABMenu();
        } else {
            openFABMenu();
        }
    }

    private void openFABMenu() {
        FABCommunity.setVisibility(View.GONE);
        FABCreatePost.setVisibility(View.VISIBLE);
        TVCreatePost.setVisibility(View.VISIBLE);
        FABCreateProof.setVisibility(View.VISIBLE);
        TVCreateProof.setVisibility(View.VISIBLE);
        darkOverlayCommunity.setVisibility(View.VISIBLE);
//        darkOverlay.setVisibility(View.VISIBLE);
        isFABOpen = true;
    }

    private void closeFABMenu() {
        FABCommunity.setVisibility(View.VISIBLE);
        FABCreatePost.setVisibility(View.GONE);
        TVCreatePost.setVisibility(View.GONE);
        FABCreateProof.setVisibility(View.GONE);
        TVCreateProof.setVisibility(View.GONE);
        darkOverlayCommunity.setVisibility(View.GONE);
//        darkOverlay.setVisibility(View.GONE);
        isFABOpen = false;
    }

    private void setupPostModels() {
        postModels.add(new PostModel("Today’s Achievement!", "Cleaned up the park near my neighborhood! It was so satisfying to see it spotless again. Let’s keep our environment clean!"));
        postModels.add(new PostModel("DIY Tips for Plastic Bottles!","Turn your old soda bottles into hanging garden pots! Here’s a quick guide: Cut, paint, and hang. Let’s upcycle instead of waste."));
        postModels.add(new PostModel("Rubbish Spotted in Town", "Spotted litter near the riverbank. Let’s team up this weekend to clean it up! Anyone nearby, join me?"));
        postModels.add(new PostModel("Rubbish Spotted in Town", "Spotted litter near the riverbank. Let’s team up this weekend to clean it up! Anyone nearby, join me?"));
        postModels.add(new PostModel("Rubbish Spotted in Town", "Spotted litter near the riverbank. Let’s team up this weekend to clean it up! Anyone nearby, join me?"));
        postModels.add(new PostModel("Rubbish Spotted in Town", "Spotted litter near the riverbank. Let’s team up this weekend to clean it up! Anyone nearby, join me?"));
        postModels.add(new PostModel("Rubbish Spotted in Town", "Spotted litter near the riverbank. Let’s team up this weekend to clean it up! Anyone nearby, join me?"));
        postModels.add(new PostModel("Rubbish Spotted in Town", "Spotted litter near the riverbank. Let’s team up this weekend to clean it up! Anyone nearby, join me?"));
    }
}