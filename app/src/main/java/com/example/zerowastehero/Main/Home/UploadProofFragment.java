package com.example.zerowastehero.Main.Home;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.zerowastehero.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadProofFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadProofFragment extends Fragment {

    private static final int PICK_FILE_REQUEST = 1;

    private Button BtnUploadFile, BtnUploadProof;
    private ImageView IVImageSelected;
    private ActivityResultLauncher<PickVisualMediaRequest> mediaPickerLauncher;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final ActivityResultContracts.GetContent contentPicker = new ActivityResultContracts.GetContent();

    public UploadProofFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadProofFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadProofFragment newInstance(String param1, String param2) {
        UploadProofFragment fragment = new UploadProofFragment();
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

        // Initialize the media picker launcher in onCreate
        mediaPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        // Handle the selected media URI
                        Log.d("MediaPicker", "Selected URI: " + uri);
                        // Example: Display the selected image in an ImageView
                        IVImageSelected.setVisibility(View.VISIBLE);
                        BtnUploadProof.setVisibility(View.VISIBLE);
                        IVImageSelected.setImageURI(uri);
                        IVImageSelected.setTag(uri);
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
        View view = inflater.inflate(R.layout.fragment_upload_proof, container, false);

        BtnUploadFile = view.findViewById(R.id.BtnUploadFile);
        BtnUploadProof = view.findViewById(R.id.BtnUploadProof);
        IVImageSelected = view.findViewById(R.id.IVImageSelected);

        BtnUploadFile.setOnClickListener(v -> pickImage());
        BtnUploadProof.setOnClickListener(v -> uploadProof());

        return view;
    }

    private void pickImage() {
        // Create a PickVisualMediaRequest to specify the type of media
        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build();

        // Launch the media picker
        IVImageSelected.setTag("");
        mediaPickerLauncher.launch(request);
    }

    private void uploadProof() {

    }
}