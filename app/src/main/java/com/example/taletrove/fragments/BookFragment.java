package com.example.taletrove.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.taletrove.Book;
import com.example.taletrove.BooksAdapter;
import com.example.taletrove.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BookFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<Book> books = new ArrayList<>();
    private List<Book> filteredBooks = new ArrayList<>();
    private BooksAdapter adapter;

    public BookFragment() {
        //
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("BookFragment", "onCreateView Called");
        // Inflate
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchView = view.findViewById(R.id.searchView);
        adapter = new BooksAdapter(books, getActivity());

        fetchBooks();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return true;
            }
        });


        return view;
    }

    private void fetchBooks() {
        String url = "https://api.myjson.online/v1/records/05b40740-e08a-43a5-9ee8-8397ff140143";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        // exist or not
                        if (response.optJSONArray("data") != null) {
                            JSONArray booksArray = response.getJSONArray("data");

                            books.clear();
                            for (int i = 0; i < booksArray.length(); i++) {
                                JSONObject bookObject = booksArray.getJSONObject(i);

                                String id = bookObject.getString("id");
                                String bookUrl = bookObject.getString("url");
                                String title = bookObject.getString("title");
                                String author = bookObject.getString("author");
                                double rating = bookObject.getDouble("rating");
                                int ratings = bookObject.getInt("ratings");
                                String imageUrl = bookObject.getString("smallImageURL");
                                int publicationYear = bookObject.getInt("publicationYear");

                                books.add(new Book(id, bookUrl, title, author, rating, ratings, imageUrl, publicationYear));
                            }

                            filteredBooks.addAll(books);
                            adapter.updateBooks(filteredBooks);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity(), "No 'data' found in response", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("API Error", error.toString());
                    Toast.makeText(getActivity(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the Volley queue
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
    }

    private void filterBooks(String query) {
        filteredBooks.clear();
        if (TextUtils.isEmpty(query)) {
            filteredBooks.addAll(books); // all books when query is empty
        } else {
            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                    filteredBooks.add(book);
                }
            }
        }
        adapter.updateBooks(filteredBooks);
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Update the adapter
        }
    }
}