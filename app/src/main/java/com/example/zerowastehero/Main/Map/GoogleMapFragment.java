

package com.example.zerowastehero.Main.Map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zerowastehero.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMap;

    public GoogleMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull android.view.LayoutInflater inflater,
                                          @Nullable android.view.ViewGroup container,
                                          @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_google_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.id_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            throw new RuntimeException("Error: Map fragment not found!");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        // List of locations with names
        List<LatLng> locations = new ArrayList<>();
        List<String> names = new ArrayList<>();

        locations.add(new LatLng(3.133863991441962, 101.68684548502978)); // Location 1
        names.add("Klean @ KL Sentral"); // Name for Location 1

        locations.add(new LatLng(3.155914535569095, 101.61070137360095)); // Location 2
        names.add("IPC Recycling & Buy-Back Centre"); // Name for Location 2

        locations.add(new LatLng(3.1933941391698424, 101.65954824313722)); // Location 3
        names.add("Alam Flora"); // Name for Location 3

        // Add markers for all locations with names
        for (int i = 0; i < locations.size(); i++) {
            gMap.addMarker(new MarkerOptions()
                    .position(locations.get(i))
                    .title(names.get(i))); // Assign name to the marker
        }

        // Move camera to the first location
        if (!locations.isEmpty()) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(0), 12));
        }
    }
}
