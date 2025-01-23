package com.example.medi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PharmacyProfileActivity extends AppCompatActivity {
    private TextView profileName, profileEmail, profilePhone, profileLicense, profileLocation;
//    private Button logout;
    private FirebaseAuth myAuth;
    private FirebaseFirestore myStore;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_profile);

        profilePhone = findViewById(R.id.textView_show_pmobile);
        profileName = findViewById(R.id.textView_show_pfull_name);
        profileEmail    = findViewById(R.id.textView_show_pemail);
        profileLicense    = findViewById(R.id.textView_show_plicense);

//        logout =findViewById(R.id.btn_plogout);


        myAuth = FirebaseAuth.getInstance();
        myStore = FirebaseFirestore.getInstance();
        userId = myAuth.getCurrentUser().getUid();

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(PharmacyProfileActivity.this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//            }
//        });


        DocumentReference documentReference = myStore.collection("Pharmacies").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                profileName.setText(documentSnapshot.getString("name"));
                profileEmail.setText(documentSnapshot.getString("email"));
                profileLicense.setText(documentSnapshot.getString("license"));
                profilePhone.setText(documentSnapshot.getString("phone"));

            }
        });


    };

}
