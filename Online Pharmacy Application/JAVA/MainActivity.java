package com.example.medi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                                Toast.makeText(MainActivity.this, "Error: Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        //login Activity
        Button login = findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //open register activity
        Button register = findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RoleSelectionActivity.class);
                startActivity(intent);
            }
        });



    }
}