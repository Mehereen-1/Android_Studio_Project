package com.example.taletrove;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taletrove.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

        binding.signupButton.setOnClickListener(v -> {
            String email = binding.sinupEmail.getText().toString().trim();
            String password = binding.sinupPassword.getText().toString().trim();
            String confirmPassword = binding.sinupConfirm.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                if (password.equals(confirmPassword)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SignupActivity.this, SetProfileActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignupActivity.this, "something is wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignupActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        binding.loginRedirectText.setOnClickListener(v -> {
            Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });

        /*
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_singup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
}