package com.example.taletrove.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.taletrove.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {

    FirebaseAuth fAuth;
    ArrayList<String> finishedBooks, currReadingBooks, wantToReadbooks;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("notiFragment", "onCreateView Called");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        ImageView imgFinishedBooks = view.findViewById(R.id.imgFinishedBooks);
        ImageView imgCurrentlyReading = view.findViewById(R.id.imgCurrentlyReading);
        ImageView imgWantToRead = view.findViewById(R.id.imgWantToRead);

        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        finishedBooks = new ArrayList<>();
        currReadingBooks = new ArrayList<>();
        wantToReadbooks = new ArrayList<>();
        getFinishedBooks(userId);

        imgFinishedBooks.setOnClickListener(v -> {
            showBookListDialog("Finished Books", finishedBooks.toArray(new String[0]));
        });
        imgCurrentlyReading.setOnClickListener(v -> {
            showBookListDialog("Books you are currently reading", currReadingBooks.toArray(new String[0]));
        });
        imgWantToRead.setOnClickListener(v -> {
            showBookListDialog("Books you want to read", wantToReadbooks.toArray(new String[0]));
        });

        return view;
    }

    private void showBookListDialog(String title, String[] books) {
        // Create and display a dialog with a scrollable list of books
        StringBuilder bookList = new StringBuilder();
        for (String book : books) {
            bookList.append("• ").append(book).append("\n");
        }

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(bookList.toString().isEmpty() ? "No books to display." : bookList.toString())
                .setPositiveButton("Close", null)
                .show();
    }

    private void getFinishedBooks(String userId) {
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books").child(userId);
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot bookSnapshot: snapshot.getChildren()){
                        String bookId = bookSnapshot.getKey();
                        String status = bookSnapshot.child("status").getValue(String.class);
                        String title = bookSnapshot.child("title").getValue(String.class);

                        if("finished".equalsIgnoreCase(status)){
                            finishedBooks.add(title);
                        }
                        else if("currently reading".equalsIgnoreCase(status)){
                            currReadingBooks.add(title);
                        }
                        else if("want to read".equalsIgnoreCase(status)){
                            wantToReadbooks.add(title);
                        }
                    }
                }else{
                    Toast.makeText(getActivity(), "No books for you", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Firebase Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}