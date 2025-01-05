    package com.example.zerowastehero.Main.Community;

    import static android.content.ContentValues.TAG;

    import android.app.AlertDialog;
    import android.os.Bundle;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.core.view.MenuProvider;
    import androidx.fragment.app.Fragment;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.navigation.Navigation;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.util.Log;
    import android.view.ContextMenu;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;
    import android.widget.Toast;


    //import com.example.zerowastehero.Main.Community.Adapter.PostAdapter;
    import com.example.zerowastehero.DataBinding.Model.PostModel;
    import com.example.zerowastehero.DataBinding.ViewModel.SharedPostModel;
    import com.example.zerowastehero.Main.Community.Adapter.PostAdapter;
    import com.example.zerowastehero.Main.Community.Interface.PostInterface;
    import com.example.zerowastehero.R;
    import com.google.android.material.bottomnavigation.BottomNavigationView;
    import com.google.android.material.floatingactionbutton.FloatingActionButton;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.FirebaseFirestoreException;
    import com.google.firebase.firestore.ListenerRegistration;
    import com.google.firebase.firestore.Query;
    import com.google.firebase.firestore.QuerySnapshot;

    import java.util.ArrayList;
    import java.util.List;

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link CommunityFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class CommunityFragment extends Fragment implements PostInterface {

        private ArrayList<PostModel> postModels = new ArrayList<>();
        private SharedPostModel sharedPostModel;
        private PostAdapter adapter;
        private FirebaseFirestore db;
        private ListenerRegistration firestoreListener;
        private BottomNavigationView bottomNavigationView;

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
            db = FirebaseFirestore.getInstance();
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
            bottomNavigationView = requireActivity().findViewById(R.id.BottomNavView);

            bottomNavigationView.setVisibility(View.VISIBLE);

            // Initialize Firestore
            db = FirebaseFirestore.getInstance();

            // Fetch posts
            postModels = new ArrayList<>();
            loadPosts();

            // Set up RecyclerView for post
            RecyclerView RVPost = view.findViewById(R.id.RVPost);
            RVPost.setLayoutManager(new LinearLayoutManager(getContext()));

            adapter = new PostAdapter(getContext(), postModels, this);
            RVPost.setAdapter(adapter);

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

        private void loadPosts() {
            db.collection("posts")
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        postModels.clear();
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            PostModel post = doc.toObject(PostModel.class);
                            postModels.add(post);
                        }
                        adapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
            Log.d("Community Fragment", "Position: " + position);
            String postID = postModels.get(position).getPostID();
            Log.d("Community Fragment", "Post ID: " + postID);
            if (postID.isEmpty() || postID == null) {
                Log.e("Community Fragment", "Post ID is empty or null");
                return;
            }
            bundle.putString("postID", postID);
            Log.d("Community Fragment", "Passed postID to Post View: " + bundle.getString("postID"));
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