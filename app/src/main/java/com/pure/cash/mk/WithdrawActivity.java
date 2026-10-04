package com.pure.cash.mk;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class WithdrawActivity extends Activity {

    private TextView balanceText;
    private EditText amountInput, methodInput, numberInput;
    private Button submitBtn, backBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private String uid;
    private long currentBalance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            finish();
            return;
        }

        uid = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference();

        balanceText = findViewById(R.id.wdBalanceText);
        amountInput = findViewById(R.id.wdAmountInput);
        methodInput = findViewById(R.id.wdMethodInput);
        numberInput = findViewById(R.id.wdNumberInput);
        submitBtn = findViewById(R.id.wdSubmitBtn);
        backBtn = findViewById(R.id.wdBackBtn);

        // ব্যালেন্স লোড
        dbRef.child("users").child(uid).child("balance")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Long b = snapshot.getValue(Long.class);
                        currentBalance = b != null ? b : 0;
                        balanceText.setText("উপলব্ধ ব্যালেন্স: ৳ " + currentBalance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountStr = amountInput.getText().toString().trim();
                String method = methodInput.getText().toString().trim();
                String number = numberInput.getText().toString().trim();

                if (TextUtils.isEmpty(amountStr)) {
                    amountInput.setError("Amount required");
                    return;
                }
                if (TextUtils.isEmpty(method)) {
                    methodInput.setError("Method required");
                    return;
                }
                if (TextUtils.isEmpty(number)) {
                    numberInput.setError("Number required");
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
                if (amount > currentBalance) {
                    amountInput.setError("Insufficient balance");
                    return;
                }

                String requestId = dbRef.child("withdrawRequests").push().getKey();
                if (requestId == null) return;

                Map<String, Object> req = new HashMap<>();
                req.put("requestId", requestId);
                req.put("userId", uid);
                req.put("amount", amount);
                req.put("method", method);
                req.put("number", number);
                req.put("status", "PENDING");
                req.put("time", System.currentTimeMillis());

                submitBtn.setEnabled(false);
                submitBtn.setText("Submitting...");

                dbRef.child("withdrawRequests").child(requestId).setValue(req)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(WithdrawActivity.this,
                                    "Withdraw request sent! Admin will review.",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            submitBtn.setEnabled(true);
                            submitBtn.setText("Submit Request");
                            Toast.makeText(WithdrawActivity.this,
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
