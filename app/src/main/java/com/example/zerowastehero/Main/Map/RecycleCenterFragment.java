package com.example.zerowastehero.Main.Map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zerowastehero.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecycleCenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecycleCenterFragment extends Fragment {

    private Button BtnReport, BtnMapView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecycleCenterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DonationCentreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecycleCenterFragment newInstance(String param1, String param2) {
        RecycleCenterFragment fragment = new RecycleCenterFragment();
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
        View view = inflater.inflate(R.layout.fragment_recycle_center, container, false);

        BtnReport = view.findViewById(R.id.BtnRecycleCenterReport);
        BtnMapView = view.findViewById(R.id.BtnMapView);

        BtnReport.setOnClickListener(v -> { Navigation.findNavController(v).navigate(R.id.DestRepostList); });
        BtnMapView.setOnClickListener(v -> { Navigation.findNavController(v).navigate(R.id.DestGoogleMapRecycleCenter); });

        return view;
    }
}