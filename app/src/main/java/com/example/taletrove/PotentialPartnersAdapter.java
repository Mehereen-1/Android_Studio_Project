package com.example.taletrove;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PotentialPartnersAdapter extends RecyclerView.Adapter<PotentialPartnersAdapter.ViewHolder> {

    private final List<PotentialPartner> potentialPartners;
    private final Context context;
    DatabaseReference databaseReference;
    String otherMail;

    public PotentialPartnersAdapter(List<PotentialPartner> potentialPartners, Context context) {
        this.potentialPartners = potentialPartners;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_potential_partner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PotentialPartner partner = potentialPartners.get(position);
        String uid = partner.getUserId();
        /*DatabaseReference otherMailref = FirebaseDatabase.getInstance().getReference("users").child(uid).child("email");

        otherMailref.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                otherMail = task.getResult().getValue(String.class);
            }else{
            }
        });*/

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("userName");
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uname = task.getResult().getValue(String.class);
                holder.partnerId.setText(String.format("You can share with: %s", uname));
                if (uname != null) {
                    Log.d("Username", "User's name is: " + uname);
                } else {
                    Log.d("Username", "Username not found.");
                }
            } else {
                Log.d("Error", "Failed to fetch username.");
            }
        });

        // deyar boi
        StringBuilder booksToGiveBuilder = new StringBuilder("Books to Give:\n");
        for (Book book : partner.getBooksToGive()) {
            booksToGiveBuilder.append("- ").append(book.getTitle()).append(" by ").append(book.getAuthor()).append("\n");
        }
        holder.booksToGive.setText(booksToGiveBuilder.toString().trim());

        //neyar boi
        StringBuilder booksToReceiveBuilder = new StringBuilder("Books to Receive:\n");
        for (Book book : partner.getBooksToReceive()) {
            booksToReceiveBuilder.append("- ").append(book.getTitle()).append(" by ").append(book.getAuthor()).append("\n");
        }
        holder.booksToReceive.setText(booksToReceiveBuilder.toString().trim());

        holder.reqbtn.setOnClickListener(v -> {
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String otherUserId = uid;
            DatabaseReference otherMailref = FirebaseDatabase.getInstance().getReference("users").child(otherUserId).child("email");

            otherMailref.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    otherMail = task.getResult().getValue(String.class);
                }else{
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Contact Via")
                    .setMessage(otherMail)
                    .setPositiveButton("OK", (dialog, which) -> {
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

            // path for database
            /*String requestPath = "book_requests/" + currentUserId + "_" + otherUserId;

            // Reference
            DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference(requestPath);

            // pending
            requestRef.setValue(new BookRequestStatus("pending", currentUserId, otherUserId))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            holder.reqbtn.setEnabled(false);
                            //Toast.makeText(context, "Request sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to send request", Toast.LENGTH_SHORT).show();
                        }
                    });*/

        });


    }

    @Override
    public int getItemCount() {
        return potentialPartners.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView partnerId, booksToGive, booksToReceive;
        Button reqbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            partnerId = itemView.findViewById(R.id.partnerId);
            booksToGive = itemView.findViewById(R.id.booksToGive);
            booksToReceive = itemView.findViewById(R.id.booksToReceive);
            reqbtn = itemView.findViewById(R.id.reqbtn);
        }
    }

    public static class BookRequestStatus {
        String requestStatus;
        String fromUserId;
        String toUserId;

        public BookRequestStatus(String requestStatus, String fromUserId, String toUserId) {
            this.requestStatus = requestStatus;
            this.fromUserId = fromUserId;
            this.toUserId = toUserId;
        }

        public String getRequestStatus() {
            return requestStatus;
        }

        public String getFromUserId() {
            return fromUserId;
        }

        public String getToUserId() {
            return toUserId;
        }
    }

}
