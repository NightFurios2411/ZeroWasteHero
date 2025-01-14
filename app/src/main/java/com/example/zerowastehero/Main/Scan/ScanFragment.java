package com.example.zerowastehero.Main.Scan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zerowastehero.DataBinding.Model.ProductResponse;
import com.example.zerowastehero.Main.Scan.Interface.BarcodeLookupAPI;
import com.example.zerowastehero.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private Retrofit retrofit;
    private BarcodeLookupAPI api;

    private ImageView IVScan, IVScanImageSelected;
    private ActivityResultLauncher<PickVisualMediaRequest> mediaPickerLauncher;
    private ConstraintLayout CLScanProductInformation;
    private TextView TVScanProductName, TVScanProductDescription, TVScanEcoFriendlyInfo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
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
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        user = mAuth.getCurrentUser();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.barcodelookup.com/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(BarcodeLookupAPI.class);

        // Handle selected image URI
        mediaPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        // Convert the URI to a Bitmap (e.g., using BitmapFactory or any other method)
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                            // Create a BitmapDrawable
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

                            // Set the BitmapDrawable to the ImageView
                            IVScanImageSelected.setImageDrawable(bitmapDrawable);
                            IVScanImageSelected.setVisibility(View.VISIBLE);
                            IVScanImageSelected.setTag(uri); // Optionally set the URI as a tag
                            CLScanProductInformation.setVisibility(View.VISIBLE);
                            TVScanProductName.setText("N/A");
                            TVScanProductDescription.setText("No Product Description Found");
                            TVScanEcoFriendlyInfo.setText("No Eco-Friendly Info Found");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("MediaPicker", "Error loading image: " + e.getMessage());
                        }
                    } else {
                        Log.d("MediaPicker", "No media selected");
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        // Initialize View
        IVScan = view.findViewById(R.id.IVScan);
        IVScanImageSelected = view.findViewById(R.id.IVScanImageSelected);
        CLScanProductInformation = view.findViewById(R.id.CLScanProductInformation);
        TVScanProductName = view.findViewById(R.id.TVScanProductName);
        TVScanProductDescription = view.findViewById(R.id.TVScanProductDescription);
        TVScanEcoFriendlyInfo = view.findViewById(R.id.TVScanEcoFriendlyInfo);

        IVScan.setOnClickListener(v -> showScanOptions());

        return view;
    }

    private void showScanOptions() {
        // Create a dialog to choose between uploading an image or scanning a barcode
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose an Option")
                .setItems(new String[]{"Upload Image", "Scan Barcode"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Upload Image option
                            openImagePicker();
                        } else if (which == 1) {
                            // Scan Barcode option
                            scanBarcode();
                        }
                    }
                })
                .show();
    }

    private void openImagePicker() {
        // Create a PickVisualMediaRequest to specify the type of media
        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build();

        // Launch the media picker
        IVScanImageSelected.setTag("");
        mediaPickerLauncher.launch(request);
    }

    private void scanBarcode() {
        // Get the drawable from the ImageView
        Drawable drawable = IVScanImageSelected.getDrawable();

        // Check if the drawable is a BitmapDrawable
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            // Convert Bitmap to InputImage for ML Kit
            InputImage image = InputImage.fromBitmap(bitmap, 0);

            // Initialize the BarcodeScanner
            BarcodeScanner barcodeScanner = BarcodeScanning.getClient();

            barcodeScanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        // Process each detected barcode
                        for (Barcode barcode : barcodes) {
                            String barcodeValue = barcode.getDisplayValue();
                            Log.d("Scan", "Detected Barcode: " + barcodeValue);

                            // Fetch product details using Barcode Lookup API
                            fetchProductDetails(barcodeValue);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Scan", "Barcode scanning failed: " + e.getMessage());
                    });
        } else {
            Log.e("Scan", "Drawable is not a BitmapDrawable, unable to scan barcode.");
        }
    }


    private void fetchProductDetails(String barcode) {
        api.getProductDetails(barcode, "8y3s4lh63iq5xqgbdvw0tzf2zxbanz").enqueue(new retrofit2.Callback<ProductResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse product = response.body();
                    String productName = product.getProductName();
                    String description = product.getProductDescription();
                    String ecoFriendly = product.getEcoFriendly();  // Check if eco-friendly info is available

                    // Display product info in the UI (you can add this to your UI)
                    CLScanProductInformation.setVisibility(View.VISIBLE);
                    TVScanProductName.setText(productName);
                    TVScanProductDescription.setText(description);
                    TVScanEcoFriendlyInfo.setText(ecoFriendly);
                } else {
                    Log.d("API Error", "No product found or error fetching data.");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ProductResponse> call, Throwable t) {
                Log.e("API Error", "Error: " + t.getMessage());
            }
        });
    }
}