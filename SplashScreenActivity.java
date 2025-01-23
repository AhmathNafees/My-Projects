package com.example.medi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseAuth myAuth = FirebaseAuth.getInstance();

        // Check if the user is logged in
        if (myAuth.getCurrentUser() != null) {
            String userId = myAuth.getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Check if the user exists in the Customers collection
            db.collection("Customers").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                // User is a Customer
                                startActivity(new Intent(SplashScreenActivity.this, CmainActivity.class));
                                finish();
                            } else {
                                // User is not a Customer, so must be a Pharmacy
                                startActivity(new Intent(SplashScreenActivity.this, PmainActivity.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(SplashScreenActivity.this, "Error: Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // User is not logged in, redirect to login activity
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScreenActivity.this, GetStartedActivity.class);
                startActivity(intent);
                finish();
            }, 2000);
        }
    }
}
