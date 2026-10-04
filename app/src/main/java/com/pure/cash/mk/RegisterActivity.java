package com.pure.cash.mk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {

    private EditText nameInput, emailInput, phoneInput, passwordInput;
    private Button registerBtn;
    private TextView loginLink;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        nameInput = findViewById(R.id.regNameInput);
        emailInput = findViewById(R.id.regEmailInput);
        phoneInput = findViewById(R.id.regPhoneInput);
        passwordInput = findViewById(R.id.regPasswordInput);
        registerBtn = findViewById(R.id.registerBtn);
        loginLink = findViewById(R.id.loginLink);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    nameInput.setError("Name required");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    emailInput.setError("Email required");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    phoneInput.setError("Phone required");
                    return;
                }
                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    passwordInput.setError("Password min 6 characters");
                    return;
                }

                registerBtn.setEnabled(false);
                registerBtn.setText("Creating account...");

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    saveUserToDatabase(user.getUid(), name, email, phone);
                                }
                            } else {
                                registerBtn.setEnabled(true);
                                registerBtn.setText("Register");
                                Toast.makeText(RegisterActivity.this,
                                        "Registration failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void saveUserToDatabase(String uid, String name, String email, String phone) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", uid);
        userData.put("name", name);
        userData.put("email", email);
        userData.put("phone", phone);
        userData.put("balance", 0);
        userData.put("totalInvested", 0);
        userData.put("totalEarned", 0);
        userData.put("createdAt", System.currentTimeMillis());

        dbRef.child("users").child(uid).setValue(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this,
                            "Account created successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this,
                            "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    registerBtn.setEnabled(true);
                    registerBtn.setText("Register");
                });
    }
}
