package com.example.taletrove.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taletrove.Book;
import com.example.taletrove.HomeBook;
import com.example.taletrove.HomeBookAdapter;
import com.example.taletrove.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeBookAdapter adapter;
    private List<HomeBook> bookProgressList = new ArrayList<HomeBook>();
    private HashMap<String, String> userNameCache = new HashMap<>();
    public FirebaseAuth fAuth;

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fAuth = FirebaseAuth.getInstance();
        String main_userID = fAuth.getCurrentUser().getUid();

        adapter = new HomeBookAdapter(bookProgressList, getActivity());
        recyclerView.setAdapter(adapter);
        prefetchUserNames();
        fetchReadingProgress(main_userID);

        return view;
    }

    private void fetchReadingProgress(String main_userID) {
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books");

        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookProgressList.clear();
                for (DataSnapshot userBooksSnapshot : snapshot.getChildren()) {
                    String userId = userBooksSnapshot.getKey();
                    for (DataSnapshot bookSnapshot : userBooksSnapshot.getChildren()) {
                        String status = bookSnapshot.child("status").getValue(String.class);
                        Book book = bookSnapshot.getValue(Book.class);

                        if ("currently reading".equalsIgnoreCase(status)) {
                            String title = bookSnapshot.child("title").getValue(String.class);
                            String author = bookSnapshot.child("author").getValue(String.class);
                            String image = bookSnapshot.child("smallImageURL").getValue(String.class);
                            /*String userName = userNameCache.getOrDefault(userId, "Unknown User");

                            if(userId.equalsIgnoreCase(main_userID)) continue;
                            bookProgressList.add(new HomeBook(userName, title, author, image));
                            adapter.notifyDataSetChanged();*/
                            getUserNameById(userId, userName -> {
                                if(!userId.equalsIgnoreCase(main_userID)) bookProgressList.add(new HomeBook(userName, title, author, image));
                                adapter.notifyDataSetChanged(); // Ensure the update happens after data is added
                            });
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface UserNameCallback {
        void onUserNameRetrieved(String userName);
    }


    private void getUserNameById(String userId, UserNameCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.getValue(String.class);
                    callback.onUserNameRetrieved(userName);
                } else {
                    callback.onUserNameRetrieved("Unknown User");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onUserNameRetrieved("Unknown User");
            }
        });
    }


    private void prefetchUserNames() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    String userName = userSnapshot.child("userName").getValue(String.class);
                    userNameCache.put(userId, userName != null ? userName : "Unknown User");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to load usernames", error.toException());
            }
        });
    }


}