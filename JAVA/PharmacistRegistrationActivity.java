package com.example.medi;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.GeoPoint;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PharmacistRegistrationActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText customerName, customerEmail, customerLocation, pharmacyLicence, customerMobile, customerPassword, customerConfirmPassword;
    private TextView errorTextViewEmail, errorTextViewLicence, errorTextViewMobile, errorTextViewPassword, errorTextViewConfirmPassword;
    private CheckBox checkBoxTermsConditions;
    private Button registerButton, buttonSelectLocation;
    private ProgressBar progressBar;
    String userID;

    private FusedLocationProviderClient fusedLocationClient;

    private FirebaseAuth myAuth;
    private FirebaseFirestore myStore;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int GPS_ENABLE_REQUEST_CODE = 2;
    private GeoPoint GeoPoint;

    private boolean isEmailValid = false;
    private boolean isPasswordValid = false;
    private boolean isConfirmPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacist_registration);

        FirebaseApp.initializeApp(this);

        myAuth = FirebaseAuth.getInstance();
        myStore = FirebaseFirestore.getInstance();

//        if (myAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), Sample.class));
//            finish();
//        }

        customerName = findViewById(R.id.customerName);
        customerEmail = findViewById(R.id.customerEmail);
        customerLocation = findViewById(R.id.customerlocation);  // Location field
        pharmacyLicence = findViewById(R.id.pharmacy_licence);
        customerMobile = findViewById(R.id.cuctomer_register_mobile);
        customerPassword = findViewById(R.id.customer_register_password);
        customerConfirmPassword = findViewById(R.id.customer_register_confirm_password);

        errorTextViewEmail = findViewById(R.id.errorTextView_email);
        errorTextViewLicence = findViewById(R.id.errorTextView_licence);
        errorTextViewMobile = findViewById(R.id.errorTextView_mobile);
        errorTextViewPassword = findViewById(R.id.errorTextView_paasowrd);
        errorTextViewConfirmPassword = findViewById(R.id.errorTextView_Con_paasowrd);

        checkBoxTermsConditions = findViewById(R.id.checkBox_terms_conditions);
        registerButton = findViewById(R.id.button_register);
        buttonSelectLocation = findViewById(R.id.button_Select_Location);
        progressBar = findViewById(R.id.progressBar);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        registerButton.setEnabled(false);

        // Add listeners for validation and button state
        customerEmail.addTextChangedListener(new EmailWatcher());
        customerMobile.addTextChangedListener(new MobileWatcher());
        customerPassword.addTextChangedListener(new PasswordWatcher());
        customerConfirmPassword.addTextChangedListener(new ConfirmPasswordWatcher());
        checkBoxTermsConditions.setOnCheckedChangeListener((buttonView, isChecked) -> updateRegisterButtonState());
        registerButton.setOnClickListener(v -> {
            if (isFormValid()) {
                progressBar.setVisibility(View.VISIBLE);
                registerPharmacist();
            }
        });

        buttonSelectLocation.setOnClickListener(v -> {
            if (!isGPSEnabled()) {
                showEnableGPSDialog();
            } else {
                getLocation();
            }
        });
    }



    private void updateRegisterButtonState() {
        registerButton.setEnabled(isFormValid());
    }

    private boolean isFormValid() {
        return !customerName.getText().toString().isEmpty() &&
                !customerEmail.getText().toString().isEmpty() &&
                !customerLocation.getText().toString().isEmpty() &&
                !pharmacyLicence.getText().toString().isEmpty() &&
                !customerMobile.getText().toString().isEmpty() &&
                !customerPassword.getText().toString().isEmpty() &&
                !customerConfirmPassword.getText().toString().isEmpty() &&
                isPasswordValid && isConfirmPasswordValid &&
                checkBoxTermsConditions.isChecked();
    }


    private void registerPharmacist() {
        String email = customerEmail.getText().toString().trim();
        String password = customerPassword.getText().toString().trim();
        String fullName = customerName.getText().toString();
        String phone = customerMobile.getText().toString();
        String licence = pharmacyLicence.getText().toString();

        if (TextUtils.isEmpty(email)) {
            customerEmail.setError("Email is Required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            customerPassword.setError("Password is Required.");
            return;
        }

        if (password.length() < 6) {
            customerPassword.setError("Password Must be >= 6 Characters");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Register the user in Firebase
        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PharmacistRegistrationActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    userID = myAuth.getCurrentUser().getUid();

                    DocumentReference documentReference = myStore.collection("Pharmacies").document(userID);

                    Map<String, Object> user = new HashMap<>();
                    user.put("id", userID);
                    user.put("name", fullName);
                    user.put("email", email);
                    user.put("phone", phone);
                    user.put("license", licence);
                    user.put("password", password);
                    user.put("location",GeoPoint);

                    // Save the data to Firestore
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), PmainActivity.class));
                } else {
                    Toast.makeText(PharmacistRegistrationActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private class EmailWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateEmail(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {}

        private void validateEmail(String email) {
            new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... params) {
                    try {
                        String apiUrl = "https://api.hunter.io/v2/email-verifier?email=" + params[0] + "&api_key=your_api_key";
                        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
                        connection.setRequestMethod("GET");
                        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        bufferedReader.close();

                        // Now parse the JSON response
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        String status = jsonResponse.getJSONObject("data").getString("status");

                        return status.equals("valid");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    isEmailValid = result;
                    SpannableString emailText = new SpannableString(result ? "Valid Email" : "Invalid Email");
                    emailText.setSpan(new ForegroundColorSpan(result ? Color.GREEN : Color.RED), 0, emailText.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    errorTextViewEmail.setText(emailText);
                    errorTextViewEmail.setVisibility(View.VISIBLE);
                    updateRegisterButtonState();
                }
            }.execute(email);
        }
    }

    private class MobileWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateMobile(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {}

        private void validateMobile(String mobile) {
            new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... params) {
                    try {
                        String apiUrl = "https://apilayer.net/api/validate?access_key=your_api_key&number=" + params[0];
                        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
                        connection.setRequestMethod("GET");
                        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        bufferedReader.close();

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        String status = jsonResponse.getString("valid");
                        return status.equals("true");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    SpannableString mobileText = new SpannableString(result ? "Valid Mobile" : "Invalid Mobile");
                    mobileText.setSpan(new ForegroundColorSpan(result ? Color.GREEN : Color.RED), 0, mobileText.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    errorTextViewMobile.setText(mobileText);
                    errorTextViewMobile.setVisibility(View.VISIBLE);
                    updateRegisterButtonState();
                }
            }.execute(mobile);
        }
    }

    private class PasswordWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validatePassword(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {}

        private void validatePassword(String password) {
            String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
            Matcher matcher = Pattern.compile(passwordPattern).matcher(password);
            isPasswordValid = matcher.matches();

            SpannableString passwordText = new SpannableString(isPasswordValid ? "Valid Password" : "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one digit, and one special character.");
            passwordText.setSpan(new ForegroundColorSpan(isPasswordValid ? Color.GREEN : Color.RED), 0, passwordText.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            errorTextViewPassword.setText(passwordText);
            errorTextViewPassword.setVisibility(View.VISIBLE);
            updateRegisterButtonState();
        }
    }

    private class ConfirmPasswordWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateConfirmPassword(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {}

        private void validateConfirmPassword(String confirmPassword) {
            isConfirmPasswordValid = confirmPassword.equals(customerPassword.getText().toString());
            SpannableString confirmPasswordText = new SpannableString(isConfirmPasswordValid ? "Passwords match" : "Passwords don't match");
            confirmPasswordText.setSpan(new ForegroundColorSpan(isConfirmPasswordValid ? Color.GREEN : Color.RED), 0, confirmPasswordText.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            errorTextViewConfirmPassword.setText(confirmPasswordText);
            errorTextViewConfirmPassword.setVisibility(View.VISIBLE);
            updateRegisterButtonState();
        }
    }

    private void getLocation() {
        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some cases this can be null.
                            if (location != null) {
                                // Use the location object to get the latitude and longitude
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                GeoPoint = new GeoPoint(latitude, longitude);


                                // You can update the location on your UI here
                                customerLocation.setText("Lat: " + latitude + ", Long: " + longitude);
                            } else {
                                // Handle location being null (e.g., prompt user to enable GPS)
                                Toast.makeText(PharmacistRegistrationActivity.this,
                                        "Unable to get location. Please try again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Permission not granted, request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showEnableGPSDialog() {
        new AlertDialog.Builder(this)
                .setMessage("GPS is not enabled. Do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_ENABLE_REQUEST_CODE);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the location
                getLocation();
            } else {
                // Permission denied, notify the user
                Toast.makeText(this, "Permission Denied. Cannot access location.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
