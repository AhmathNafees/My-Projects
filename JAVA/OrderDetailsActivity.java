package com.example.medi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {

    private TextView customerName, orderDate;
    private ImageView uploadedImage;
    private EditText etCustomerMessage;
    private Button btnConfirmOrder, btnCancelOrder;

    private FirebaseFirestore db;
    private String orderId;
    private String pharmacyUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Get current pharmacy UID
        pharmacyUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize Views
        customerName = findViewById(R.id.CustomerName);
        orderDate = findViewById(R.id.OrderDate);
        uploadedImage = findViewById(R.id.uploadedImage);
        etCustomerMessage = findViewById(R.id.etCustomerMessage);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);

        // Get the order ID from intent
        orderId = getIntent().getStringExtra("orderId");

        if (orderId != null) {
            fetchOrderDetails(orderId);
        }

        // Set up button click listeners
        btnConfirmOrder.setOnClickListener(v -> updateOrderStatus("Confirmed"));
        btnCancelOrder.setOnClickListener(v -> updateOrderStatus("Cancelled"));
    }

    private void fetchOrderDetails(String orderId) {
        // Fetch the order details from Firestore
        DocumentReference orderRef = db.collection("Orders").document(orderId);
        orderRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Get the customerUid from the Orders document
                String customerUid = documentSnapshot.getString("customerUid");
                String orderDateText = documentSnapshot.getTimestamp("date").toDate().toString();
                String imageUrl = documentSnapshot.getString("imageUrl");
                String customerMessage = documentSnapshot.getString("message");

                // Fetch the customer's name from the Customers collection
                if (customerUid != null) {
                    db.collection("Customers").document(customerUid).get()
                            .addOnSuccessListener(customerSnapshot -> {
                                if (customerSnapshot.exists()) {
                                    String customerNameText = customerSnapshot.getString("name");
                                    customerName.setText(customerNameText != null ? customerNameText : "N/A");
                                } else {
                                    customerName.setText("Customer not found");
                                }
                            })
                            .addOnFailureListener(e -> {
                                customerName.setText("Error fetching customer name");
                                Toast.makeText(this, "Error fetching customer details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    customerName.setText("Customer UID not found");
                }

                // Set other order details
                orderDate.setText(orderDateText != null ? orderDateText : "N/A");
                etCustomerMessage.setText(customerMessage != null ? customerMessage : "No Message");

                // Load the image using Glide
                if (imageUrl != null) {
                    Glide.with(this)
                            .load(imageUrl)
                            .placeholder(R.drawable.no_profile_pic)
                            .into(uploadedImage);
                }
            } else {
                Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error fetching order details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateOrderStatus(String status) {
        // Update the order status in Firestore
        DocumentReference orderRef = db.collection("Orders").document(orderId);
        orderRef.update("status", status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order status updated to " + status, Toast.LENGTH_SHORT).show();

                    // Fetch customer details and send email
                    orderRef.get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String customerUid = documentSnapshot.getString("customerUid");

                            // Fetch customer email and send notification
                            if (customerUid != null) {
                                db.collection("Customers").document(customerUid).get()
                                        .addOnSuccessListener(customerSnapshot -> {
                                            if (customerSnapshot.exists()) {
                                                String customerEmail = customerSnapshot.getString("email");
                                                if (customerEmail != null) {
                                                    sendEmailNotification(pharmacyUid, customerUid, customerEmail, status);
                                                } else {
                                                    Log.e("OrderDetailsActivity", "Customer email not found");
                                                }
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("OrderDetailsActivity", "Error fetching customer email: " + e.getMessage());
                                        });
                            }
                        }
                    });

                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update order status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void sendEmailNotification(String senderUid, String receiverUid, String customerEmail, String status) {
        // Create subject and message based on the status
        String subject = "Order Status Updated";
        String body = "Your order has been " + status + ".\nThank you for using our service.";

        // Send email with sender and receiver UID
        EmailNotification emailNotification = new EmailNotification();
        emailNotification.sendEmailWithSenderReceiver(senderUid, receiverUid, customerEmail, subject, body);
    }
}
