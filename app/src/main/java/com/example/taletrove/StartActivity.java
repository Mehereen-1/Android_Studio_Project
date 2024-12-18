package com.example.taletrove;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        //startActivity(intent);

        if (isUserLoggedIn()) {
            // Redirect to HomeActivity (or your main screen)
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            // Redirect to LoginActivity
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        // Finish StartActivity so it doesn't stay in the back stack
        //finish();
    }
    private boolean isUserLoggedIn() {
        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
            return sharedPreferences.getBoolean("isLoggedIn", false);  // Default is false
        } catch (Exception e) {
            Toast.makeText(this, "Error checking login status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;  // Assume not logged in if any error occurs
        }
    }

}