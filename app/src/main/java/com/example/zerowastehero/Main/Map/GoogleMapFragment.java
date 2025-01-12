package com.example.zerowastehero.Main.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zerowastehero.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapFragment extends Fragment {

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;

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

            // fetch current location
//            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        // Get device's current location
//                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                        // Add a marker for the current location
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
//                    } else {
//                        Toast.makeText(requireContext(), "Unable to fetch current location", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });

            // List of locations to display on the map
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
        return inflater.inflate(R.layout.fragment_google_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}