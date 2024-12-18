package com.example.taletrove;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {
    private List<Book> bookList;
    private final Context context;

    private DatabaseReference databaseReference;

    public BooksAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        com.example.taletrove.Book currentBook = bookList.get(position);
        Book book = bookList.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText("Author: " + book.getAuthor());
        holder.tvRating.setText("View Details");

        holder.tvRating.setOnClickListener(v -> {
            //dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            LayoutInflater inflater = LayoutInflater.from(v.getContext());
            View dialogView = inflater.inflate(R.layout.book_details_dialog, null);

            //dialog TextViews
            TextView tvDialogRating = dialogView.findViewById(R.id.tvDialogRating);
            TextView tvDialogTotalRatings = dialogView.findViewById(R.id.tvDialogTotalRatings);
            TextView tvDialogPublicationYear = dialogView.findViewById(R.id.tvDialogPublicationYear);
            TextView tvDialogAuthor = dialogView.findViewById(R.id.tvDialogAuthor);

            //dialog data
            tvDialogRating.setText("Rating: " + book.getRating());
            tvDialogTotalRatings.setText("Total Ratings: " + book.getRatings());
            tvDialogPublicationYear.setText("Publication Year: " + book.getPublicationYear());
            tvDialogAuthor.setText("Author: " + book.getAuthor());

            //show the dialog
            builder.setView(dialogView);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        // Load the image
        Glide.with(context)
                .load(book.getSmallImageURL())
                .into(holder.imgCover);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Read button action
        holder.readButton.setOnClickListener(v -> {
            v.setPressed(true);
            v.postDelayed(() -> v.setPressed(false), 100);
            updateBookStatus(userId, book, "finished");
        });

        // Want to read button action
        holder.wantToReadButton.setOnClickListener(v -> {
            v.setPressed(true);
            v.postDelayed(() -> v.setPressed(false), 100);
            updateBookStatus(userId, book, "want to read");
        });

        // Currently reading button action
        holder.currentlyReadingButton.setOnClickListener(v -> {
            v.setPressed(true);
            v.postDelayed(() -> v.setPressed(false), 100);
            updateBookStatus(userId, book, "currently reading");
        });
    }

    public void updateBookStatus(String userUID, Book book, String newStatus) {
        String bookID = book.getId();
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books").child(userUID).child(bookID);
        book.setStatus(newStatus);

        booksRef.setValue(book)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Book status updated successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Failed to update book status", e);
                });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void updateBooks(List<Book> newBooks) {
        this.bookList = newBooks;
        notifyDataSetChanged();
    }

    // ViewHolder class to hold the references to the views
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvRating;
        ImageView imgCover;
        RadioButton readButton, wantToReadButton, currentlyReadingButton;

        public BookViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvRating = itemView.findViewById(R.id.tvRating);
            imgCover = itemView.findViewById(R.id.imgCover);
            readButton = itemView.findViewById(R.id.completeButton);
            wantToReadButton = itemView.findViewById(R.id.wantToReadButton);
            currentlyReadingButton = itemView.findViewById(R.id.currentlyReadingButton);
        }
    }
}

