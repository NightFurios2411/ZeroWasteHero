package com.example.zerowastehero.Main.Community;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.DataBinding.ViewModel.SharedPostModel;
import com.example.zerowastehero.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePostFragment extends Fragment {

    ArrayList<PostModel> postModels = new ArrayList<PostModel>();
    SharedPostModel sharedPostModel;

    ImageView IVSearchImage, IVImageSelected;
    Button BtnSubmitPost;
    EditText ETPostDescriptionText, ETPostTitleText;
    ActivityResultLauncher<PickVisualMediaRequest> mediaPickerLauncher;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePostFragment newInstance(String param1, String param2) {
        CreatePostFragment fragment = new CreatePostFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        // Initialize views
        IVSearchImage = view.findViewById(R.id.IVImageSearch);
        IVImageSelected = view.findViewById(R.id.IVImageSelected);
        BtnSubmitPost = view.findViewById(R.id.BtnSubmitPost);
        ETPostDescriptionText = view.findViewById(R.id.ETPostDescriptionText);
        ETPostTitleText = view.findViewById(R.id.ETPostTitleText);

        IVSearchImage.setOnClickListener(v -> {
            // Create a PickVisualMediaRequest to specify the type of media
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build();

            // Launch the media picker
            mediaPickerLauncher.launch(request);
        });

        mediaPickerLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                // Handle the selected media URI
                Log.d("MediaPicker", "Selected URI: " + uri);
                // Example: Display the selected image in an ImageView
                IVImageSelected.setVisibility(View.VISIBLE);
                IVImageSelected.setImageURI(uri);
            } else {
                Log.d("MediaPicker", "No media selected");
            }
        });

        ETPostTitleText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean titleFilled = !ETPostTitleText.getText().toString().trim().isEmpty();
                boolean descriptionFilled = !ETPostDescriptionText.getText().toString().trim().isEmpty();
                bothEditTextFilled(titleFilled, descriptionFilled);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        ETPostDescriptionText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean titleFilled = !ETPostTitleText.getText().toString().trim().isEmpty();
                boolean descriptionFilled = !ETPostDescriptionText.getText().toString().trim().isEmpty();
                bothEditTextFilled(titleFilled, descriptionFilled);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        BtnSubmitPost.setOnClickListener(v -> {
            String titleText = ETPostTitleText.getText().toString().trim();
            String descriptionText = ETPostDescriptionText.getText().toString().trim();

            if (IVImageSelected.getVisibility() == View.VISIBLE) {
                // Post with image
                Uri imageUri = IVImageSelected.getTag() != null ? (Uri) IVImageSelected.getTag() : null;

                if (imageUri != null) {
                    createPostWithImage(titleText, descriptionText, imageUri);
                } else {
                    Toast.makeText(getContext(), "Something went wrong with the selected image.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Post without image
                createPostWithoutImage(titleText, descriptionText);
            }

            // Observe the LiveData from SharedPostModel
            sharedPostModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
                // This block will be called whenever the posts data changes
                if (posts != null) {
                    postModels.clear(); // Clear old data if necessary
                    postModels.addAll(posts); // Add new posts to the list
                }
            });
        });

        return view;
    }

    private void bothEditTextFilled(boolean text1, boolean text2) {
        BtnSubmitPost.setEnabled(text1 && text2);
    }

    private void createPostWithImage(String title,String description, Uri imageUri) {

    }

    private void createPostWithoutImage(String title, String description) {
//        postmodels.add(new Post());
    }
}