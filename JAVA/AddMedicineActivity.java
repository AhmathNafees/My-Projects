package com.example.medi;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddMedicineActivity extends AppCompatActivity {

    private EditText nameEditText, brandEditText, weightEditText;
    private FirebaseFirestore db;
    private String medicineId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        nameEditText = findViewById(R.id.medicine_name);
        brandEditText = findViewById(R.id.medicine_brand);
        weightEditText = findViewById(R.id.medicine_weight);
        db = FirebaseFirestore.getInstance();

        if (getIntent().hasExtra("medicineId")) {
            medicineId = getIntent().getStringExtra("medicineId");
            nameEditText.setText(getIntent().getStringExtra("name"));
            brandEditText.setText(getIntent().getStringExtra("brand"));
            weightEditText.setText(getIntent().getStringExtra("weight"));
        }

        findViewById(R.id.submit_button).setOnClickListener(v -> saveMedicine());
    }

    private void saveMedicine() {
        String name = nameEditText.getText().toString();
        String brand = brandEditText.getText().toString();
        String weight = weightEditText.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(brand) || TextUtils.isEmpty(weight)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> medicine = new HashMap<>();
        medicine.put("name", name);
        medicine.put("brand", brand);
        medicine.put("weight", weight);

// Add pharmacy UID (assuming you have the UID)
        String pharmacyUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        medicine.put("pharmacyUid", pharmacyUid);

// Add pharmacy document reference (assuming you have the Firestore instance)
        DocumentReference pharmacyRef = FirebaseFirestore.getInstance().collection("Pharmacies").document(pharmacyUid);
        medicine.put("pharmacyRef", pharmacyRef);

// Save to Firestore
        FirebaseFirestore.getInstance().collection("medicines")
                .add(medicine)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Medicine added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error adding medicine", e);
                });


        if (medicineId != null) {
            db.collection("medicines").document(medicineId)
                    .set(medicine)
                    .addOnSuccessListener(aVoid -> finish());
        } else {
            db.collection("medicines").add(medicine)
                    .addOnSuccessListener(documentReference -> finish());
        }
    }
}
