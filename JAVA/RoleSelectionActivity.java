package com.example.medi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RoleSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_selection);

        FirebaseApp.initializeApp(this);

        FirebaseAuth myAuth = FirebaseAuth.getInstance();

        if (myAuth.getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Check if the user exists in the Customers collection
            db.collection("Customers").document(userId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    // User is a Customer
                                    startActivity(new Intent(getApplicationContext(), CmainActivity.class));
                                    finish();
                                } else {
                                    // User is not a Customer, so must be a Pharmacy
                                    startActivity(new Intent(getApplicationContext(), PmainActivity.class));
                                    finish();
                                }
                            } else {
                                Toast.makeText(RoleSelectionActivity.this, "Error: Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        findViewById(R.id.customerButton).setOnClickListener(v -> {
            // Navigate to Customer Registration
            Intent intent = new Intent(RoleSelectionActivity.this, CustomerRegistrationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.pharmacyButton).setOnClickListener(v -> {
            // Navigate to Pharmacist Registration
            Intent intent = new Intent(RoleSelectionActivity.this, PharmacistRegistrationActivity.class);
            startActivity(intent);
        });
    }
}
