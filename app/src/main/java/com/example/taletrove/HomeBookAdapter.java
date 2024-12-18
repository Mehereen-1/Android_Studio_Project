package com.example.taletrove;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HomeBookAdapter extends RecyclerView.Adapter<HomeBookAdapter.ViewHolder> {

    private List<HomeBook> bookProgressList;
    private final Context context;
    private DatabaseReference usersRef;

    public HomeBookAdapter(List<HomeBook> bookProgressList, Context context) {
        this.bookProgressList = bookProgressList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeBook homeBook = bookProgressList.get(position);
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        String temp = homeBook.getUserName();
        holder.userNameTextView.setText(homeBook.getUserName() + " is currently reading");
        holder.titleTextView.setText(homeBook.getTitle());
        holder.authorTextView.setText("by " + homeBook.getAuthor());
        //String currAge, currId, currAdd, currgen;

        Glide.with(context)
                .load(homeBook.getImgUrl())
                .into(holder.imgCover);


        holder.userNameTextView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            LayoutInflater inflater = LayoutInflater.from(v.getContext());
            View dialogView = inflater.inflate(R.layout.user_details_dialog, null);

            TextView age = dialogView.findViewById(R.id.userAge);
            TextView gender = dialogView.findViewById(R.id.userGender);
            TextView address = dialogView.findViewById(R.id.userAddress);
            //TextView bookcount = dialogView.findViewById(R.id.bookCount);

            usersRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    boolean userFound = false;

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userName = userSnapshot.child("userName").getValue(String.class);

                        if (userName != null && userName.equals(temp)) {
                            userFound = true;

                            String currId = userSnapshot.getKey();
                            String currAge = userSnapshot.child("age").getValue(String.class).toString();
                            String currAdd = userSnapshot.child("address").getValue(String.class).toString();
                            String currgen = userSnapshot.child("gender").getValue(String.class).toString();

                            age.setText("Age: " + currAge);
                            gender.setText("Gender: " + currgen);
                            address.setText("Address: " + currAdd);
                            //bookcount.setText("Total Finished Books  = 0");

                            break;
                        }
                    }

                    if (!userFound) {
                    }
                } else {
                }
            });

            builder.setView(dialogView);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return bookProgressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, titleTextView, authorTextView;
        ImageView imgCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            imgCover = itemView.findViewById(R.id.imgCover);
        }
    }
}
