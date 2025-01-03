package com.example.zerowastehero.Main.Community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.Main.Community.Adapter.SearchUserAdapter;
import com.example.zerowastehero.Main.Community.Interface.SearchUserInterface;
import com.example.zerowastehero.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchUserFragment extends Fragment implements SearchUserInterface {

    private SearchView SVSearchUser;
    private RecyclerView RVSearchUser;
    private SearchUserAdapter searchUserAdapter;
    private List<UserModel> userModels = new ArrayList<>();
    private FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchUserFragment newInstance(String param1, String param2) {
        SearchUserFragment fragment = new SearchUserFragment();
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
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);

        // Initialize view
        RVSearchUser = view.findViewById(R.id.RVSearchUser);
        SVSearchUser = view.findViewById(R.id.SVSearchUser);

        db = FirebaseFirestore.getInstance();

        RVSearchUser.setLayoutManager(new LinearLayoutManager(getContext()));
        searchUserAdapter = new SearchUserAdapter(userModels, this);
        RVSearchUser.setAdapter(searchUserAdapter);

        // Fetch users from Firestore
        fetchUsers();

        // Set up SearchView
        SVSearchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });

        return view;
    }

    private void fetchUsers() {
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userModels.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        UserModel user = document.toObject(UserModel.class);
                        userModels.add(user);
                    }
                    searchUserAdapter.updateList(userModels);
                })
                .addOnFailureListener(e -> Log.e("SearchUserFragment", "Error fetching users", e));
    }

    private void filterUsers(String query) {
        List<UserModel> filteredUsers = new ArrayList<>();
        for (UserModel user : userModels) {
            if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        searchUserAdapter.updateList(filteredUsers);
    }

    @Override
    public void onUserClick(int position) {

    }
}