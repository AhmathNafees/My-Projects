package com.example.medi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PmainActivity extends AppCompatActivity {

    private TextView name,verifyMsg;
    private Button profile,resendCode;
    FirebaseUser fuser;

    private String userId;
    private FirebaseAuth myAuth;
    private FirebaseFirestore myStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_main);

        name = findViewById(R.id.textView_show_pfull_name);
        profile = findViewById(R.id.button_pprofile);

        myAuth = FirebaseAuth.getInstance();
        myStore = FirebaseFirestore.getInstance();
        userId = myAuth.getCurrentUser().getUid();

        DocumentReference documentReference = myStore.collection("Pharmacies").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("name"));
            }
        });

        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);

        userId = myAuth.getCurrentUser().getUid();
        fuser = myAuth.getCurrentUser();

// Check if the user is not verified
        if (!fuser.isEmailVerified()) {
            verifyMsg.setVisibility(View.GONE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent " + e.getMessage());
                        }
                    });
                }
            });

            // Start periodic check for email verification
            verifyEmailPeriodically();
        } else {
            // If already verified, redirect immediately
            redirectToCustomerHome();
        }


        // Navigate to Pharmacist Registration
        profile.setOnClickListener(v -> {
            // Navigate to Pharmacist Registration
            Intent intent = new Intent(PmainActivity.this, PharmacyProfileActivity.class);
            startActivity(intent);
        });

    }
    private void verifyEmailPeriodically() {
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fuser.reload().addOnCompleteListener(task -> {
                    if (fuser.isEmailVerified()) {
                        Toast.makeText(PmainActivity.this, "Email Verified! Redirecting...", Toast.LENGTH_SHORT).show();
                        redirectToCustomerHome();
                        handler.removeCallbacksAndMessages(null); // Stop checking once verified
                    } else {
                        handler.postDelayed(this, 3000); // Check again after 3 seconds
                    }
                });
            }
        }, 3000); // Initial delay of 3 seconds
    }
    private void redirectToCustomerHome() {
        Intent intent = new Intent(PmainActivity.this, PharmacyHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
