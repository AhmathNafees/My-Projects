package com.example.medi;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
//    private EditText editName, editEmail, editPhone, editLicense;
//    private FirebaseAuth mAuth;
//    private DatabaseReference databaseReference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.edit_profile);
//
//        mAuth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Pharmacists");
//
//        editName = findViewById(R.id.editName);
//        editEmail = findViewById(R.id.editEmail);
//        editPhone = findViewById(R.id.editPhone);
//        editLicense = findViewById(R.id.editLicense);
//
//        findViewById(R.id.saveChangesButton).setOnClickListener(v -> saveChanges());
//    }
//
//    private void saveChanges() {
//        String name = editName.getText().toString().trim();
//        String email = editEmail.getText().toString().trim();
//        String phone = editPhone.getText().toString().trim();
//        String license = editLicense.getText().toString().trim();
//
//        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(license)) {
//            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String userId = mAuth.getCurrentUser().getUid();
//
//        // Re-authenticate for email and password changes if needed
//        // Then update the profile data in the database
//
//        Pharmacist updatedPharmacist = new Pharmacist(userId, name, email, phone, license);
//        databaseReference.child(userId).setValue(updatedPharmacist)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
