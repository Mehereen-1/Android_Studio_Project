package com.example.taletrove;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taletrove.fragments.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    String userID;
    String uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        EditText etUsername = findViewById(R.id.etName);
        EditText etAge = findViewById(R.id.etAge);
        RadioGroup rgGender = findViewById(R.id.etGender);
        EditText etAddress = findViewById(R.id.etAddress);
        Button btnSave = findViewById(R.id.btnSave);
        fAuth =  FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String email = fAuth.getCurrentUser().getEmail().toString();
        TextView emailView = findViewById(R.id.etEmail);
        emailView.setText(email);

        btnSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            int selectedGenderId = rgGender.getCheckedRadioButtonId();


            if (username.isEmpty() || age.isEmpty() || address.isEmpty() || selectedGenderId == -1) {
                Toast.makeText(EditProfile.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedGender = findViewById(selectedGenderId);
            String gender = selectedGender.getText().toString();

            // Save the data
            saveProfile(username, age, gender, address);
            Intent intent = new Intent(EditProfile.this, ProfileFragment.class);
            startActivity(intent);
        });
    }

    private void saveProfile(String username, String age, String gender, String address) {
        userID = fAuth.getCurrentUser().getUid();
        uemail = fAuth.getCurrentUser().getEmail().toString().trim();

        Map<String, Object> user = new HashMap<>();
        user.put("userName", username);
        user.put("email", uemail);
        user.put("age", age);
        user.put("gender", gender);
        user.put("address", address);

        // Store data under the user's unique ID
        databaseReference.child(userID).setValue(user)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(EditProfile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(EditProfile.this, "Failed to Update Profile: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}