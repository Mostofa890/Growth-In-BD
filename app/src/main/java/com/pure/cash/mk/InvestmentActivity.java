package com.pure.cash.mk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvestmentActivity extends Activity {

    private RecyclerView recyclerView;
    private TextView emptyText;
    private FirebaseAuth mAuth;
    private DatabaseReference investmentsRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            finish();
            return;
        }

        uid = mAuth.getCurrentUser().getUid();
        investmentsRef = FirebaseDatabase.getInstance().getReference()
                .child("investments");

        recyclerView = findViewById(R.id.investmentsRecyclerView);
        emptyText = findViewById(R.id.investmentsEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        investmentsRef.orderByChild("userId").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<InvestmentModel> list = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            InvestmentModel inv = child.getValue(InvestmentModel.class);
                            if (inv != null) {
                                inv.setId(child.getKey());
                                list.add(inv);
                            }
                        }

                        if (list.isEmpty()) {
                            emptyText.setText("এখনো কোনো বিনিয়োগ নেই\n\nডিপোজিট করুন এবং Admin অ্যাপ্রুভ করলে এখানে দেখাবে।");
                            emptyText.setVisibility(android.view.View.VISIBLE);
                            recyclerView.setVisibility(android.view.View.GONE);
                        } else {
                            emptyText.setVisibility(android.view.View.GONE);
                            recyclerView.setVisibility(android.view.View.VISIBLE);
                            InvestmentAdapter adapter = new InvestmentAdapter(InvestmentActivity.this, list);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Investment", "Error: " + error.getMessage());
                    }
                });
    }
}
