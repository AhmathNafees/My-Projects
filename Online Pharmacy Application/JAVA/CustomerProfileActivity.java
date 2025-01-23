package com.example.medi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CustomerProfileActivity extends AppCompatActivity {
    private TextView profileName, profileEmail, profilePhone, profileDOB, profileGender;
//    private Button  logout;
    private String userId;
    private FirebaseAuth myAuth;
    private FirebaseFirestore myStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        profilePhone = findViewById(R.id.textView_show_mobile);
        profileName = findViewById(R.id.textView_show_full_name);
        profileEmail    = findViewById(R.id.textView_show_email);
        profileDOB    = findViewById(R.id.textView_show_dob);
        profileGender    = findViewById(R.id.textView_show_gender);

//        logout =findViewById(R.id.btn_logout);


        myAuth = FirebaseAuth.getInstance();
        myStore = FirebaseFirestore.getInstance();
        userId = myAuth.getCurrentUser().getUid();

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(CustomerProfileActivity.this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//            }
//        });

        DocumentReference documentReference = myStore.collection("Customers").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                profileName.setText(documentSnapshot.getString("name"));
                profileEmail.setText(documentSnapshot.getString("email"));
                profileDOB.setText(documentSnapshot.getString("dob"));
                profileGender.setText(documentSnapshot.getString("gender"));
                profilePhone.setText(documentSnapshot.getString("phone"));
            }
        });

    };

}
