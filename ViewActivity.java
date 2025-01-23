package com.example.medi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private ArrayList<Medicine> medicineList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineList = new ArrayList<>();
        adapter = new MedicineAdapter(medicineList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadMedicines();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(ViewActivity.this, AddMedicineActivity.class)));
    }

    private void loadMedicines() {
        String currentPharmacyUid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current pharmacy UID

        db.collection("medicines")
                .whereEqualTo("pharmacyUid", currentPharmacyUid) // Query medicines for the current pharmacy UID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        medicineList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Medicine medicine = document.toObject(Medicine.class);
                            medicine.setId(document.getId());
                            medicineList.add(medicine);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("loadMedicines", "Error fetching medicines: ", task.getException());
                    }
                });
    }

}
