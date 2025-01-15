package com.example.zerowastehero.Main.Map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.zerowastehero.Main.Map.Adapter.SearchMapAdapter;
import com.example.zerowastehero.Main.Map.Interface.RecycleLocationInterface;
import com.example.zerowastehero.R;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchMapFragment extends Fragment implements RecycleLocationInterface {

    private SearchView SVSearchRecycleLocation;
    private RecyclerView RVLocationSearch;
    private SearchMapAdapter searchMapAdapter;
    private FirebaseFirestore db;
    private LinearLayout LLSearchMapRecentQueries;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchMapFragment newInstance(String param1, String param2) {
        SearchMapFragment fragment = new SearchMapFragment();
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
        View view = inflater.inflate(R.layout.fragment_search_map, container, false);

        // Initialize view
        SVSearchRecycleLocation = view.findViewById(R.id.SVSearchRecycleLocation);
        RVLocationSearch = view.findViewById(R.id.RVLocationSearch);
        LLSearchMapRecentQueries = view.findViewById(R.id.LLSearchMapRecentQueries);

        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        RVLocationSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        searchMapAdapter = new SearchMapAdapter(getContext(), this);
        RVLocationSearch.setAdapter(searchMapAdapter);

        // Fetch locations from Firestore


        // Set up SearchView
        SVSearchRecycleLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LLSearchMapRecentQueries.setVisibility(View.GONE);
                filterLocations(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    LLSearchMapRecentQueries.setVisibility(View.VISIBLE);
                    RVLocationSearch.setVisibility(View.GONE);
                    filterLocations(newText);
                    return true;
                } else {
                    LLSearchMapRecentQueries.setVisibility(View.GONE);
                    RVLocationSearch.setVisibility(View.VISIBLE);
                    filterLocations(newText);
                    return true;
                }
            }
        });

        return view;
    }

    private void filterLocations(String query) {
        searchMapAdapter.updateList();
    }

    @Override
    public void onRecycleLocationClick(int position) {
        Navigation.findNavController(requireView()).navigate(R.id.DestRecycleCenter);
    }
}