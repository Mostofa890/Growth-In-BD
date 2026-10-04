package com.pure.cash.mk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryActivity extends Activity {

    private RecyclerView recyclerView;
    private TextView emptyText;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            finish();
            return;
        }

        uid = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.historyRecyclerView);
        emptyText = findViewById(R.id.historyEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadHistory();
    }

    private void loadHistory() {
        List<RequestModel> list = new ArrayList<>();

        // Deposit requests
        dbRef.child("depositRequests").orderByChild("userId").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.removeIf(r -> "DEPOSIT".equals(r.getType()));
                        for (DataSnapshot child : snapshot.getChildren()) {
                            RequestModel req = child.getValue(RequestModel.class);
                            if (req != null) {
                                req.setType("DEPOSIT");
                                list.add(req);
                            }
                        }
                        updateUI(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("History", "Error: " + error.getMessage());
                    }
                });

        // Withdraw requests
        dbRef.child("withdrawRequests").orderByChild("userId").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.removeIf(r -> "WITHDRAW".equals(r.getType()));
                        for (DataSnapshot child : snapshot.getChildren()) {
                            RequestModel req = child.getValue(RequestModel.class);
                            if (req != null) {
                                req.setType("WITHDRAW");
                                list.add(req);
                            }
                        }
                        updateUI(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("History", "Error: " + error.getMessage());
                    }
                });
    }

    private void updateUI(List<RequestModel> list) {
        if (list.isEmpty()) {
            emptyText.setText("এখনো কোনো লেনদেন নেই");
            emptyText.setVisibility(android.view.View.VISIBLE);
            recyclerView.setVisibility(android.view.View.GONE);
            return;
        }

        Collections.sort(list, new Comparator<RequestModel>() {
            @Override
            public int compare(RequestModel a, RequestModel b) {
                return Long.compare(b.getTime(), a.getTime());
            }
        });

        emptyText.setVisibility(android.view.View.GONE);
        recyclerView.setVisibility(android.view.View.VISIBLE);

        HistoryAdapter adapter = new HistoryAdapter(HistoryActivity.this, list);
        recyclerView.setAdapter(adapter);
    }
}
