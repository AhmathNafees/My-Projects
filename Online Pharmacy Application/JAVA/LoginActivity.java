package com.example.medi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail, loginPassword;
    private Button loginBtn;
    private TextView register, forgotTextLink;
    private ProgressBar progressBar;

    private FirebaseAuth myAuth;
    private FirebaseFirestore myStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        loginEmail = findViewById(R.id.editText_login_email);
        loginPassword = findViewById(R.id.editText_login_pwd);
        loginBtn = findViewById(R.id.button_login);
        register = findViewById(R.id.textView_register_link);
        forgotTextLink = findViewById(R.id.textView_forgot_password_link);
        progressBar = findViewById(R.id.progressBar);

        // Initialize Firebase instances
        myAuth = FirebaseAuth.getInstance();
        myStore = FirebaseFirestore.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                // Validate inputs
                if (TextUtils.isEmpty(email)) {
                    loginEmail.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    loginPassword.setError("Password is required.");
                    return;
                }
                if (password.length() < 6) {
                    loginPassword.setError("Password must be >= 6 characters.");
                    return;
                }

                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Authenticate user
                myAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    String userId = myAuth.getCurrentUser().getUid();
                                    // Check if user is in "Customers" collection
                                    myStore.collection("Customers").document(userId).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful() && task.getResult().exists()) {
                                                        // Navigate to Customer Main Activity
                                                        startActivity(new Intent(getApplicationContext(), CmainActivity.class));
                                                        finish();
                                                    } else {
                                                        // Check if user is in "Pharmacies" collection
                                                        String userId = myAuth.getCurrentUser().getUid();
                                                        myStore.collection("Pharmacies").document(userId).get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful() && task.getResult().exists()) {
                                                                            // Navigate to Pharmacy Main Activity
                                                                            startActivity(new Intent(getApplicationContext(), PmainActivity.class));
                                                                            finish();
                                                                        } else {
                                                                            Toast.makeText(LoginActivity.this, "Error: User not found in any collection.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Navigate to Registration Activity
        register.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RoleSelectionActivity.class)));

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        myAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });


    }
}
