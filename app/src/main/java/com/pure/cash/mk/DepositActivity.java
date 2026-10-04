package com.pure.cash.mk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DepositActivity extends Activity {

    private EditText amountInput, methodInput, txnInput;
    private Button submitBtn, backBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        amountInput = findViewById(R.id.depAmountInput);
        methodInput = findViewById(R.id.depMethodInput);
        txnInput = findViewById(R.id.depTxnInput);
        submitBtn = findViewById(R.id.depSubmitBtn);
        backBtn = findViewById(R.id.depBackBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountStr = amountInput.getText().toString().trim();
                String method = methodInput.getText().toString().trim();
                String txnId = txnInput.getText().toString().trim();

                if (TextUtils.isEmpty(amountStr)) {
                    amountInput.setError("Amount required");
                    return;
                }
                if (TextUtils.isEmpty(method)) {
                    methodInput.setError("Method required (bKash/Nagad)");
                    return;
                }
                if (TextUtils.isEmpty(txnId)) {
                    txnInput.setError("Transaction ID required");
                    return;
                }

                long amount;
                try {
                    amount = Long.parseLong(amountStr);
                } catch (Exception e) {
                    amountInput.setError("Invalid amount");
                    return;
                }

                if (amount < 100) {
                    amountInput.setError("Minimum ৳100");
                    return;
                }

                if (mAuth.getCurrentUser() == null) {
                    Toast.makeText(DepositActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
                    return;
                }

                String uid = mAuth.getCurrentUser().getUid();
                String requestId = dbRef.child("depositRequests").push().getKey();

                if (requestId == null) return;

                Map<String, Object> req = new HashMap<>();
                req.put("requestId", requestId);
                req.put("userId", uid);
                req.put("amount", amount);
                req.put("method", method);
                req.put("txnId", txnId);
                req.put("status", "PENDING");
                req.put("time", System.currentTimeMillis());

                submitBtn.setEnabled(false);
                submitBtn.setText("Submitting...");

                dbRef.child("depositRequests").child(requestId).setValue(req)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(DepositActivity.this,
                                    "Deposit request sent! Admin will review.",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            submitBtn.setEnabled(true);
                            submitBtn.setText("Submit Request");
                            Toast.makeText(DepositActivity.this,
                                    "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
