package com.example.medi;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {

    private Uri imageUri = null;
    private static final String TAG = "MAIN_TAG";
    private Button button2;
    Button orderBtn;
    EditText editText;
    private ShapeableImageView imageIv;
    private EditText recognizedTextET;
    private ImageView imageView11;
    private Button upload;
    private static final int CAMERA_REQUEST_CODE = 100;

    // Arrays of permission required for camera
    private String[] cameraPermission;
    private TextRecognizer textRecognizer;

    // Firestore instance
    private FirebaseFirestore db;

    // ActivityResultLauncher for image picking
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        View imageView3 = findViewById(R.id.imageView3);
        Button orderBtn_v = findViewById(R.id.orderBtn);

        // Handle click to navigate to the next activity
        imageView3.setOnClickListener(view -> {
            // Create an Intent to start NextActivity
            Intent intent = new Intent(Home.this, Instruction2.class);
            startActivity(intent);
        });
        orderBtn = findViewById(R.id.orderBtn);
        editText = findViewById(R.id.recognizedTextET);
        //For button visibility
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()){
                    orderBtn.setVisibility(View.GONE);
                }else{
                    orderBtn.setVisibility(View.VISIBLE);
                    orderBtn.setOnClickListener(v -> {
                        String extractedText = recognizedTextET.getText().toString().trim(); // Extract text from EditText
                        if (extractedText.isEmpty()) {
                            Toast.makeText(Home.this, "No text to process", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<String> medicineNames = extractMedicineNames(extractedText); // Process the extracted text
                        if (medicineNames.isEmpty()) {
                            Toast.makeText(Home.this, "No valid medicines found in the text", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        findPharmaciesWithMedicines(medicineNames);
                        Intent intent = new Intent(Home.this, CustomerHome.class);
                        startActivity(intent);
                    });

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Init UI views
        imageView11 = findViewById(R.id.imageView8);
        button2 = findViewById(R.id.button2);
        imageIv = findViewById(R.id.imageIv);
        recognizedTextET = findViewById(R.id.recognizedTextET);

        // Init permission arrays for camera
        cameraPermission = new String[]{Manifest.permission.CAMERA};

        // Init TextRecognizer
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // Handle click to show input dialog (camera/gallery)
        imageView11.setOnClickListener(view -> showInputImageDialog());

        // Handle click to start recognizing text
        button2.setOnClickListener(view -> {
            if (imageUri == null) {
                Toast.makeText(Home.this, "Please select an image", Toast.LENGTH_SHORT).show();
            } else {
                recognizedTextFromImage();
            }
        });

//        // Init Firebase
//        FirebaseApp.initializeApp(this);
//        db = FirebaseFirestore.getInstance();
//
//        // Initialize image picker launcher
//        imagePickerLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        imageUri = result.getData().getData();
//                        imageIv.setImageURI(imageUri);
//                    }
//                });
//
//        upload = findViewById(R.id.button);
//        upload.setOnClickListener(v -> uploadImage());
    }

    // Show options to select Camera or Gallery
    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(Home.this, imageView11);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Camera");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Gallery");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == 1) {
                if (checkCameraPermission()) {
                    pickImageCamera();
                } else {
                    requestCameraPermission();
                }
            } else if (id == 2) {
                pickImageGallery();
            }
            return true;
        });
    }

    private void recognizedTextFromImage() {
        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);
            textRecognizer.process(inputImage)
                    .addOnSuccessListener(text -> recognizedTextET.setText(text.getText()))
                    .addOnFailureListener(e -> Toast.makeText(Home.this, "Failed to recognize text: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            Toast.makeText(Home.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    imageUri = data.getData();
                    imageIv.setImageURI(imageUri);
                } else {
                    Toast.makeText(Home.this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void pickImageCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    imageIv.setImageURI(imageUri);
                } else {
                    Toast.makeText(Home.this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Upload the image to Firebase Storage
    private void uploadImage() {
        if (imageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + System.currentTimeMillis() + ".jpg");
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        saveToFirestore(downloadUrl);
                    }))
                    .addOnFailureListener(e -> Toast.makeText(Home.this, "Failed to upload: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(Home.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Save image URL and recognized text to Firestore
    private void saveToFirestore(String imageUrl) {
        String recognizedText = recognizedTextET.getText().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("imageUrl", imageUrl);
        data.put("recognizedText", recognizedText);

        db.collection("RecognizedTexts").add(data)
                .addOnSuccessListener(documentReference -> Toast.makeText(Home.this, "Data saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(Home.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    private List<String> extractMedicineNames(String text) {
        // Split the text into words and clean them
        String[] words = text.split("\\s+");
        List<String> medicineNames = new ArrayList<>();

        for (String word : words) {
            if (!word.isEmpty()) {
                medicineNames.add(word.toLowerCase()); // Store as lowercase for consistency
            }
        }

        return medicineNames;
    }

    private void findPharmaciesWithMedicines(List<String> medicineNames) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Integer> pharmacyMatchCount = new HashMap<>();

        for (String medicineName : medicineNames) {
            db.collection("medicines")
                    .whereEqualTo("name", medicineName) // Query by medicine name
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String pharmacyUid = doc.getString("pharmacyUid"); // Get the pharmacy UID

                                // Increment match count for the pharmacy
                                pharmacyMatchCount.put(pharmacyUid, pharmacyMatchCount.getOrDefault(pharmacyUid, 0) + 1);
                            }

                            // After processing all medicine names, display the results
                            if (medicineName.equals(medicineNames.get(medicineNames.size() - 1))) {
                                displayPharmacyList(pharmacyMatchCount);
                            }
                        } else {
                            Log.e("Firestore", "Error fetching medicines: ", task.getException());
                        }
                    });
        }
    }

    private void displayPharmacyList(Map<String, Integer> pharmacyMatchCount) {

        if (pharmacyMatchCount.isEmpty()) {
            Toast.makeText(this, "No pharmacies found with the given medicines", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> sortedPharmacies = new ArrayList<>(pharmacyMatchCount.keySet());
        Collections.sort(sortedPharmacies, (p1, p2) -> pharmacyMatchCount.get(p2) - pharmacyMatchCount.get(p1)); // Sort by match count

        StringBuilder result = new StringBuilder("Pharmacies with the most matches:\n");
        for (String pharmacyUid : sortedPharmacies) {
            int count = pharmacyMatchCount.get(pharmacyUid);
            result.append("Pharmacy: ").append(pharmacyUid).append(" - Matches: ").append(count).append("\n");
        }

        // Display the pharmacy list (you can replace this with a RecyclerView or another UI element)
        Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
    }

}
