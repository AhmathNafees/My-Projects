package com.example.medi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlaceOrderActivity extends AppCompatActivity {

    private TextView pharmacyNameText, pharmacyPhoneText;
    private EditText orderMessage;
    private ImageView uploadedImage;
    private Button selectImageButton, placeOrderButton;

    private Uri selectedImageUri;
    private FirebaseFirestore myStore;
    private String pharmacyUid;
    private Cloudinary cloudinary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placing);

        pharmacyNameText = findViewById(R.id.pharmacyNameText);
        pharmacyPhoneText = findViewById(R.id.pharmacyPhoneText);
        orderMessage = findViewById(R.id.orderMessage);
        uploadedImage = findViewById(R.id.uploadedImage);
        selectImageButton = findViewById(R.id.selectImageButton);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        myStore = FirebaseFirestore.getInstance();

        // Initialize Cloudinary configuration
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dtx3yz5fb", // Replace with your actual Cloudinary cloud name
                "api_key", "319448824861979", // Replace with your actual API key
                "api_secret", "zpm0GLUX3068t57-bnRMvOz36YQ" // Replace with your actual API secret
        ));

        // Get the pharmacy details from the intent
        Intent intent = getIntent();
        String pharmacyName = intent.getStringExtra("pharmacyName");
        String pharmacyPhone = intent.getStringExtra("pharmacyPhone");
        pharmacyUid = intent.getStringExtra("pharmacyUid");

        Log.d("PlaceOrderActivity", "Received Pharmacy UID: " + pharmacyUid);

        pharmacyNameText.setText(pharmacyName);
        pharmacyPhoneText.setText(pharmacyPhone);

        // Image selection logic
        selectImageButton.setOnClickListener(v -> {
            Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(imageIntent, 1);
        });

        // Place Order button
        placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    // Handle the image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            selectedImageUri = data.getData();
            uploadedImage.setImageURI(selectedImageUri);
        }
    }

    // Place the order in Firestore
    private void placeOrder() {
        String message = orderMessage.getText().toString();
        if (selectedImageUri != null && !message.isEmpty()) {
            try {
                // Convert the selected image URI to a temporary file
                File tempFile = createTempFileFromUri(selectedImageUri);

                // Upload the image to Cloudinary
                new Thread(() -> {
                    try {
                        Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
                        String imageUrl = (String) uploadResult.get("secure_url");

                        // Save the order details to Firestore
                        saveOrderToFirestore(imageUrl, message);

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(PlaceOrderActivity.this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                }).start();

            } catch (Exception e) {
                Toast.makeText(this, "Error handling image file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a message and select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveOrderToFirestore(String imageUrl, String message) {

        Log.d("PlaceOrderActivity", "Saving Order with Pharmacy UID: " + pharmacyUid);

        DocumentReference pharmacyRef = myStore.collection("Pharmacies").document(pharmacyUid);

        String customerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> order = new HashMap<>();
        order.put("customerUid", customerUid); // Sender UID
        order.put("pharmacyUid", pharmacyUid); // Receiver UID
        order.put("pharmacy", pharmacyRef);
        order.put("imageUrl", imageUrl);
        order.put("message", message);
        order.put("status", "Pending");
        order.put("date", new Timestamp(new Date()));

        // Add the order to Firestore
        myStore.collection("Orders").add(order)
                .addOnSuccessListener(documentReference -> {
                    // Retrieve the generated document ID
                    String orderId = documentReference.getId();

                    // Update the document to include the order ID
                    documentReference.update("orderUid", orderId)
                            .addOnSuccessListener(aVoid -> {
                                sendEmailNotification(customerUid, pharmacyUid, "New Order Placed", "A new order has been placed. Please check your dashboard.");
                                runOnUiThread(() -> {
                                    Toast.makeText(PlaceOrderActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                            })
                            .addOnFailureListener(e -> runOnUiThread(() -> {
                                e.printStackTrace();
                                Toast.makeText(PlaceOrderActivity.this, "Error updating order ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }));
                })
                .addOnFailureListener(e -> runOnUiThread(() -> {
                    e.printStackTrace();
                    Toast.makeText(PlaceOrderActivity.this, "Error placing order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }


    private void sendEmailNotification(String senderUid, String receiverUid, String subject, String body) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch recipient's email based on receiverUid
        db.collection("Pharmacies").document(receiverUid).get() // Modify based on recipient type (Pharmacy or Customer)
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String recipientEmail = documentSnapshot.getString("email");
                        if (recipientEmail != null) {
                            EmailNotification emailNotification = new EmailNotification();
                            emailNotification.sendEmailWithSenderReceiver(senderUid, receiverUid, recipientEmail, subject, body);
                        } else {
                            Log.e("PlaceOrderActivity", "Recipient email not found.");
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("PlaceOrderActivity", "Error fetching recipient details: " + e.getMessage()));
    }


    private File createTempFileFromUri(Uri uri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile("tempImage", ".jpg", getCacheDir());

        if (inputStream != null) {
            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } finally {
                inputStream.close();
            }
        }
        return tempFile;
    }
}
