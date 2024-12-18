package com.example.taletrove.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taletrove.PotentialPartnersActivity;
import com.example.taletrove.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class ExchangeFragment extends Fragment {


    private RecyclerView recyclerView;
    private DatabaseReference exchangeRef;
    private FirebaseAuth firebaseAuth;

    public ExchangeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);

        ImageView btnSeePotentialPartners = view.findViewById(R.id.btnSeePotentialPartners);

        btnSeePotentialPartners.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PotentialPartnersActivity.class);
            startActivity(intent);
        });

        ImageView btnMail = view.findViewById(R.id.btnmail);

        /*btnMail.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LookupActivity.class);
            startActivity(intent);
        });*/

        return view;
    }

}