package com.example.taletrove.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.taletrove.EditProfile;
import com.example.taletrove.LoginActivity;
import com.example.taletrove.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private FirebaseAuth fAuth;
    private DatabaseReference databaseReference;
    private TextView tvUsername, tvEmail, tvAge, tvGender, tvAddress;
    private Button logoutBtn, editBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialization
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAge = view.findViewById(R.id.tvAge);
        tvGender = view.findViewById(R.id.tvGender);
        tvAddress = view.findViewById(R.id.tvAddress);
        logoutBtn = view.findViewById(R.id.logoutId);
        editBtn = view.findViewById(R.id.editId);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(requireActivity(), "No user is logged in.", Toast.LENGTH_SHORT).show();
        }


        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            //logOut();
        });

        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfile.class);
            startActivity(intent);
        });

        // Initialize Firebase
        fAuth = FirebaseAuth.getInstance();
        String userID = fAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID);

        // Fetch data from Firebase
        fetchUserProfile();

        return view;
    }

    private void fetchUserProfile() {
        databaseReference.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("userName").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String age = snapshot.child("age").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);

                    // Update the TextViews with data
                    tvUsername.setText("Username: " + username);
                    tvEmail.setText("Email: " + email);
                    tvAge.setText("Age: " + age);
                    tvGender.setText("Gender: " + gender);
                    tvAddress.setText("Address: " + address);
                } else {
                    Toast.makeText(getContext(), "No profile data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileFragment", "Error fetching data", error.toException());
                Toast.makeText(getContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void logOut() {
        if (getActivity() != null) {
            try {
                // Access SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);  // Set false after logout
                editor.apply();
                //editor.clear();
                //editor.apply();

                // Log out from FirebaseAuth
                FirebaseAuth.getInstance().signOut();

                // Redirect to LoginActivity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

                //successful logout
                Toast.makeText(getActivity(), "Logged out successfully.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //exception
                Toast.makeText(getActivity(), "Logout failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // fragment is not attached to an activity
            Toast.makeText(getActivity(), "Error: Unable to log out. Please try later.", Toast.LENGTH_SHORT).show();
        }
    }



}
