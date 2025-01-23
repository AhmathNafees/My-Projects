package com.example.medi;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Sample extends AppCompatActivity {

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

//        getSupportActionBar().setTitle("MediScan");

        //logout Activity
        logout = findViewById(R.id.button_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();

            }
        });



    }
}
