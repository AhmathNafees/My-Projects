package com.example.medi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GetStartedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.get_started);

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

                                    startActivity(new Intent(getApplicationContext(), CmainActivity.class));
                                    finish();
                                } else {

                                    startActivity(new Intent(getApplicationContext(), PmainActivity.class));
                                    finish();
                                }
                            } else {
                                Toast.makeText(GetStartedActivity.this, "Error: Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.get_started), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.getStartedbutton).setOnClickListener(v -> {
            // Move to Role Selection Screen
            Intent intent = new Intent(GetStartedActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
