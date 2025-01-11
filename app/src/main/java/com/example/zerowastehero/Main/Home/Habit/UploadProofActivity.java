package com.example.zerowastehero.Main.Home.Habit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.zerowastehero.R;

public class UploadProofActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1; // Request code for file picker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_proof); // Your layout for the upload proof screen

        // Find the "Upload File" button
        Button uploadFileButton = findViewById(R.id.upload_file_button);

        // Set onClickListener for the upload file button to open the gallery picker
        uploadFileButton.setOnClickListener(v -> openGallery());
    }

    // Method to open the gallery picker for images and videos
    private void openGallery() {
        // Open the gallery (image or video selection)
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/* video/*"); // Allow both images and videos
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    // Handle the file selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedMediaUri = data.getData(); // Get the selected file URI

                // You can now use the URI to display the media or upload it
                // Example: uploadFile(selectedMediaUri);
            }
        }
    }
}
