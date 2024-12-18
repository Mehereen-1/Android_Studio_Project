package com.example.taletrove;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    private final List<PotentialPartnersAdapter.BookRequestStatus> requestList;
    private final Context context;
    private final DatabaseReference requestsRef;

    public RequestsAdapter(List<PotentialPartnersAdapter.BookRequestStatus> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
        this.requestsRef = FirebaseDatabase.getInstance().getReference("book_requests");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PotentialPartnersAdapter.BookRequestStatus request = requestList.get(position);

        holder.fromUser.setText("From: " + request.getFromUserId());

        holder.acceptButton.setOnClickListener(v -> {
            // Accept the request
            String requestKey = request.getFromUserId() + "_" + request.getToUserId();
            requestsRef.child(requestKey).child("requestStatus").setValue("accepted")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Request accepted", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        holder.deleteButton.setOnClickListener(v -> {
            // Delete the request
            String requestKey = request.getFromUserId() + "_" + request.getToUserId();
            requestsRef.child(requestKey).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Request deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fromUser;
        Button acceptButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fromUser = itemView.findViewById(R.id.fromUser);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}

