    package com.example.zerowastehero.Main.Community;

    import android.app.AlertDialog;
    import android.os.Bundle;

    import androidx.fragment.app.Fragment;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.navigation.Navigation;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;


    //import com.example.zerowastehero.Main.Community.Adapter.PostAdapter;
    import com.example.zerowastehero.DataBinding.Model.PostModel;
    import com.example.zerowastehero.DataBinding.ViewModel.SharedPostModel;
    import com.example.zerowastehero.Main.Community.Adapter.CommunityAdapter;
    import com.example.zerowastehero.Main.Community.Interface.CommunityInterface;
    import com.example.zerowastehero.R;
    import com.google.android.material.floatingactionbutton.FloatingActionButton;

    import java.util.ArrayList;

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link CommunityFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class CommunityFragment extends Fragment implements CommunityInterface {

        private ArrayList<PostModel> postModels = new ArrayList<>();
        private SharedPostModel sharedPostModel;
        private CommunityAdapter adapter;

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
            sharedPostModel = new ViewModelProvider(requireActivity()).get(SharedPostModel.class);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_community, container, false);

            // Initialize views
            FABCommunity = view.findViewById(R.id.FABCommunity);
            FABCreatePost = view.findViewById(R.id.FABCreatePost);
            FABCreateProof = view.findViewById(R.id.FABCreateProof);
            darkOverlayCommunity = view.findViewById(R.id.DarkOverlayCommunity);
            darkOverlay = requireActivity().findViewById(R.id.darkOverlay);
            TVCreatePost = view.findViewById(R.id.TVCreatePost);
            TVCreateProof = view.findViewById(R.id.TVCreateProof);

            // Set up RecyclerView for post
            RecyclerView recyclerView = view.findViewById(R.id.RVPost);
            adapter = new CommunityAdapter(getContext(), postModels, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // Observe the LiveData from SharedPostModel
            sharedPostModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
                // This block will be called whenever the posts data changes
                if (posts != null) {
                    postModels.clear(); // Clear old data if necessary
                    postModels.addAll(posts); // Add new posts to the list
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });

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

        @Override
        public void onPostClick(int position) {
            Bundle bundle = new Bundle();
            String postID = postModels.get(position).getPostID();
            bundle.putString("postID", postID);
            Navigation.findNavController(requireView()).navigate(R.id.DestPostView, bundle);
        }

        @Override
        public void onChallengeClick() {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("5-Day Plastic Free Challenge");
            builder.setMessage("Avoid single use plastic for 5 days.");

            builder.setPositiveButton("OK", (dialog, which) -> {
                // Handle positive button click
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Handle negative button click
                dialog.dismiss();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }