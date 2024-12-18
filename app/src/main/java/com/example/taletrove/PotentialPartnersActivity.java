package com.example.taletrove;

import android.os.Bundle;
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

public class PotentialPartnersActivity extends AppCompatActivity {

    private RecyclerView rvPotentialPartners;
    private PotentialPartnersAdapter adapter;
    private List<PotentialPartner> potentialPartners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential_partners);

        rvPotentialPartners = findViewById(R.id.rvPotentialPartners);
        rvPotentialPartners.setLayoutManager(new LinearLayoutManager(this));

        potentialPartners = new ArrayList<>();
        adapter = new PotentialPartnersAdapter(potentialPartners, this);
        rvPotentialPartners.setAdapter(adapter);

        fetchPotentialPartners();
    }

    private void fetchPotentialPartners() {
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books");
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        booksRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();

                List<Book> currentUserFinishedBooks = new ArrayList<>();
                List<Book> currentUserWantToReadBooks = new ArrayList<>();
                List<PotentialPartner> potentialPartners = new ArrayList<>();

                // amar boi
                if (snapshot.hasChild(currentUserId)) {
                    DataSnapshot currentUserBooks = snapshot.child(currentUserId);
                    for (DataSnapshot bookSnapshot : currentUserBooks.getChildren()) {
                        String title = bookSnapshot.child("title").getValue(String.class);
                        String author = bookSnapshot.child("author").getValue(String.class);
                        String status = bookSnapshot.child("status").getValue(String.class);

                        if ("finished".equalsIgnoreCase(status)) {
                            Book tempBook = new Book();
                            tempBook.setAuthor(author);
                            tempBook.setTitle(title);
                            currentUserFinishedBooks.add(tempBook);
                        } else if ("want to read".equalsIgnoreCase(status)) {
                            Book tempBook = new Book();
                            tempBook.setAuthor(author);
                            tempBook.setTitle(title);
                            currentUserWantToReadBooks.add(tempBook);
                        }
                    }
                } else {
                    Toast.makeText(this, "No books found for the current user", Toast.LENGTH_SHORT).show();
                    return;
                }

                // matching boi
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    if (userId.equals(currentUserId)) continue;

                    List<Book> theirFinishedBooks = new ArrayList<>();
                    List<Book> theirWantToReadBooks = new ArrayList<>();

                    for (DataSnapshot bookSnapshot : userSnapshot.getChildren()) {
                        String title = bookSnapshot.child("title").getValue(String.class);
                        String author = bookSnapshot.child("author").getValue(String.class);
                        String status = bookSnapshot.child("status").getValue(String.class);

                        if ("finished".equalsIgnoreCase(status)) {
                            Book tempBook = new Book();
                            tempBook.setAuthor(author);
                            tempBook.setTitle(title);
                            theirFinishedBooks.add(tempBook);
                        } else if ("want to read".equalsIgnoreCase(status)) {
                            Book tempBook = new Book();
                            tempBook.setAuthor(author);
                            tempBook.setTitle(title);
                            theirWantToReadBooks.add(tempBook);
                        }
                    }

                    // exchange
                    List<Book> booksToGive = new ArrayList<>();
                    List<Book> booksToReceive = new ArrayList<>();

                    // deyar boi
                    for (Book finishedBook : currentUserFinishedBooks) {
                        for (Book theirWantToReadBook : theirWantToReadBooks) {
                            if (finishedBook.equals(theirWantToReadBook)) {
                                booksToGive.add(finishedBook);
                                //break;
                            }
                        }
                    }

                    // neyar boi
                    for (Book theirFinishedBook : theirFinishedBooks) {
                        for (Book currentUserWantBook : currentUserWantToReadBooks) {
                            if (theirFinishedBook.equals(currentUserWantBook)) {
                                booksToReceive.add(theirFinishedBook);
                                 // break;
                            }
                        }
                    }


                    // adding
                    if (!booksToGive.isEmpty() && !booksToReceive.isEmpty()) {
                        potentialPartners.add(new PotentialPartner(userId, booksToGive, booksToReceive));
                    }
                }

                // Update RecyclerView
                if (potentialPartners.isEmpty()) {
                    Toast.makeText(this, "No potential partners found", Toast.LENGTH_SHORT).show();
                } else {
                    PotentialPartnersAdapter adapter = new PotentialPartnersAdapter(potentialPartners, this);
                    rvPotentialPartners.setAdapter(adapter);
                }
            } else {
                Toast.makeText(this, "Failed to fetch potential partners", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
