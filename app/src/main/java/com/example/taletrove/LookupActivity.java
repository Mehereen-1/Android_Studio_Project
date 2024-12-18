package com.example.taletrove;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LookupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("book_requests");

        requestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<PotentialPartnersAdapter.BookRequestStatus> incomingRequests = new ArrayList<>();
                DataSnapshot snapshot = task.getResult();

                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    String toUserId = requestSnapshot.child("toUserId").getValue(String.class);

                    if (toUserId != null && toUserId.equals(currentUserId)) {
                        Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
                        String fromUserId = requestSnapshot.child("fromUserId").getValue(String.class);
                        String requestStatus = requestSnapshot.child("requestStatus").getValue(String.class);

                        incomingRequests.add(new PotentialPartnersAdapter.BookRequestStatus(fromUserId, toUserId, requestStatus));
                    }
                }

                displayRequests(incomingRequests);
            } else {
                Toast.makeText(this, "ki hocche", Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Failed to retrieve requests");
            }
        });

    }
    private void displayRequests(List<PotentialPartnersAdapter.BookRequestStatus> incomingRequests) {
        RecyclerView recyclerView = findViewById(R.id.requestsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RequestsAdapter adapter = new RequestsAdapter(incomingRequests, this);
        recyclerView.setAdapter(adapter);
    }

}