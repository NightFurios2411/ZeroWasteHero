package com.example.zerowastehero.Main.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zerowastehero.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapRecycleCenterFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // Get device's current location
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Request permission if not granted
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                // Enable location tracking
                googleMap.setMyLocationEnabled(true);
            }

            // List of locations to display on the map
            List<LatLng> locations = new ArrayList<>();
            List<String> names = new ArrayList<>();

            // Add Universiti Malaya's location (default)
            LatLng universitiMalaya = new LatLng(3.1147, 101.6749); // Coordinates for Universiti Malaya, Kuala Lumpur
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(universitiMalaya, 12)); // Move camera to Universiti Malaya

            locations.add(new LatLng(3.133863991441962, 101.68684548502978)); // Location 1
            names.add("Klean @ KL Sentral"); // Name for Location 1

            // Add markers for all locations with names
            for (int i = 0; i < locations.size(); i++) {
                googleMap.addMarker(new MarkerOptions()
                        .position(locations.get(i))
                        .title(names.get(i))); // Assign name to the marker
            }

            // Move camera to the first location
            if (!locations.isEmpty()) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(0), 12));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_google_map_recycle_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}