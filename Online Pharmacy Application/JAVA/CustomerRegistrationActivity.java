package com.example.medi;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerRegistrationActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText CUName, CUEmail, CUDOB, CUMobile, CUPassword, CUConfirmPassword;
    private TextView errorCUEmail, errorCUMobile, errorCUPassword, errorCUConfirmPassword;
    private CheckBox CUCheckBoxTerms;
    private Button CURegisterButton;
    private ProgressBar CUProgressBar;
    String userID;

    RadioGroup genderGroup;
    RadioButton radioFemale, radioMale;

    private FirebaseAuth myAuth;
    private FirebaseFirestore myStore;

    private boolean isEmailValid = false;
    private boolean isPasswordValid = false;
    private boolean isConfirmPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_registration);

        FirebaseApp.initializeApp(this);

        myAuth = FirebaseAuth.getInstance();
        myStore = FirebaseFirestore.getInstance();

//        if (myAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), Sample.class));
//            finish();
//        }


        // Initialize views
        CUName = findViewById(R.id.CUName);
        CUEmail = findViewById(R.id.CUEmail);
        CUDOB = findViewById(R.id.CUDOB);
        CUMobile = findViewById(R.id.cuctomer_CU_mobile);
        CUPassword = findViewById(R.id.customer_CU_password);
        CUConfirmPassword = findViewById(R.id.customer_CU_confirm_password);
        errorCUEmail = findViewById(R.id.errorTextView_CUemail);
        errorCUMobile = findViewById(R.id.errorTextView_CUmobile);
        errorCUPassword = findViewById(R.id.errorTextView_cupaasowrd);
        errorCUConfirmPassword = findViewById(R.id.errorTextView_cu_con_paasowrd);
        CUCheckBoxTerms = findViewById(R.id.CUcheckBox_terms_conditions);
        CURegisterButton = findViewById(R.id.CUbutton_register);
        CUProgressBar = findViewById(R.id.CUprogressBar);

        genderGroup = findViewById(R.id.CU_gender_group);
        radioFemale = findViewById(R.id.radio_CUfemale);
        radioMale = findViewById(R.id.radio_CUmale);

        CURegisterButton.setEnabled(false);

        // Add listeners for validation and button state
        CUEmail.addTextChangedListener(new EmailWatcher());
        CUMobile.addTextChangedListener(new MobileWatcher());
        CUPassword.addTextChangedListener(new PasswordWatcher());
        CUConfirmPassword.addTextChangedListener(new ConfirmPasswordWatcher());
        CUCheckBoxTerms.setOnCheckedChangeListener((buttonView, isChecked) -> updateRegisterButtonState());
        CURegisterButton.setOnClickListener(v -> {
            if (isFormValid()) {
                CUProgressBar.setVisibility(View.VISIBLE);
                registerCustomer();
            }
        });
    }

    private void updateRegisterButtonState() {
        CURegisterButton.setEnabled(isFormValid());
    }

    private boolean isFormValid() {
        return !CUName.getText().toString().isEmpty() &&
                !CUEmail.getText().toString().isEmpty() &&
                !CUDOB.getText().toString().isEmpty() &&
                !CUMobile.getText().toString().isEmpty() &&
                !CUPassword.getText().toString().isEmpty() &&
                !CUConfirmPassword.getText().toString().isEmpty() && isPasswordValid && isConfirmPasswordValid &&
                CUCheckBoxTerms.isChecked();
    }

    private void registerCustomer() {
        CURegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedGenderId = genderGroup.getCheckedRadioButtonId();
                RadioButton selectedGenderButton = findViewById(selectedGenderId);

                final String gender = selectedGenderButton.getText().toString().trim();
                final String email = CUEmail.getText().toString().trim();
                String password = CUPassword.getText().toString().trim();
                final String fullName = CUName.getText().toString();
                final String phone    = CUMobile.getText().toString();
                final String dob    = CUDOB.getText().toString();

                if(TextUtils.isEmpty(email)){
                    CUEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    CUPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    CUPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                CUProgressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                myAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(CustomerRegistrationActivity.this,"Account Created", Toast.LENGTH_SHORT).show();
                            userID = myAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = myStore.collection("Customers").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("id", userID);
                            user.put("name",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("gender", gender);
                            user.put("dob", dob);
                            user.put("password", password);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), CmainActivity.class));
                        }else {
                            Toast.makeText(CustomerRegistrationActivity.this,"Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            CUProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


    }


    private class EmailWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateEmail(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        private void validateEmail(String email) {
            new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... params) {
                    try {
                        String apiUrl = "https://api.hunter.io/v2/email-verifier?email=" + params[0] + "&api_key=e8bad4ac3a54effeef5853fd7897b5cf812b0342";
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

                        // Debug: Log the response to check what is returned
                        Log.d("EmailValidationResponse", response.toString());

                        // Now parse the JSON response
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        String status = jsonResponse.getJSONObject("data").getString("status");

                        // Log status to debug
                        Log.d("EmailValidationStatus", status);

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
                    errorCUEmail.setText(emailText);
                    errorCUEmail.setVisibility(View.VISIBLE);
                    updateRegisterButtonState();
                }
            }.execute(email);
        }


    }

    private class MobileWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateMobile(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        private void validateMobile(String mobile) {
            new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... params) {
                    try {
                        String apiUrl = "https://apilayer.net/api/validate?access_key=a4d32ea265ca5d572a28c8d3c3dde6c8&number=" + params[0];
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
                        return jsonResponse.getBoolean("valid");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    SpannableString mobileText = new SpannableString(result ? "Valid Mobile" : "Invalid Mobile");
                    mobileText.setSpan(new ForegroundColorSpan(result ? Color.GREEN : Color.RED), 0, mobileText.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    errorCUMobile.setText(mobileText);
                    errorCUMobile.setVisibility(View.VISIBLE);
                    updateRegisterButtonState();
                }
            }.execute(mobile);
        }
    }

    private class PasswordWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validatePassword(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        private void validatePassword(String password) {
            // Advanced password validation pattern
            String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
            Matcher matcher = Pattern.compile(passwordPattern).matcher(password);
            isPasswordValid = matcher.matches();

            SpannableString passwordText = new SpannableString(isPasswordValid ? "Valid Password" : "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one digit, and one special character.");
            passwordText.setSpan(new ForegroundColorSpan(isPasswordValid ? Color.GREEN : Color.RED), 0, passwordText.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            errorCUPassword.setText(passwordText);
            errorCUPassword.setVisibility(View.VISIBLE);
            updateRegisterButtonState();
        }
    }

    private class ConfirmPasswordWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateConfirmPassword(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        private void validateConfirmPassword(String confirmPassword) {
            isConfirmPasswordValid = confirmPassword.equals(CUPassword.getText().toString());
            SpannableString confirmPasswordText = new SpannableString(isConfirmPasswordValid ? "Passwords match" : "Passwords don't match");
            confirmPasswordText.setSpan(new ForegroundColorSpan(isConfirmPasswordValid ? Color.GREEN : Color.RED), 0, confirmPasswordText.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            errorCUConfirmPassword.setText(confirmPasswordText);
            errorCUConfirmPassword.setVisibility(View.VISIBLE);
            updateRegisterButtonState();
        }
    }
}
