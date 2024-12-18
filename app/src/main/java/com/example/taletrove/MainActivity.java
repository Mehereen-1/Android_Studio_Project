package com.example.taletrove;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taletrove.databinding.ActivityMainBinding;
import com.example.taletrove.fragments.BookFragment;
import com.example.taletrove.fragments.ExchangeFragment;
import com.example.taletrove.fragments.HomeFragment;
import com.example.taletrove.fragments.NotificationFragment;
import com.example.taletrove.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        replaceFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(R.id.bottom_home == item.getItemId()){
                Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                replaceFragment(new HomeFragment());
            }
            else if(R.id.bottom_books == item.getItemId()){
                Toast.makeText(MainActivity.this, "books", Toast.LENGTH_SHORT).show();
                replaceFragment(new BookFragment());
            }
            else if(R.id.bottom_exchange == item.getItemId()){
                Toast.makeText(MainActivity.this, "Exchange Books", Toast.LENGTH_SHORT).show();
                replaceFragment(new ExchangeFragment());
            }
            else if(R.id.bottom_notification == item.getItemId()){
                Toast.makeText(MainActivity.this, "Shelf", Toast.LENGTH_SHORT).show();
                replaceFragment(new NotificationFragment());
            }
            else if(R.id.bottom_profile == item.getItemId()){
                Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                replaceFragment(new ProfileFragment());
            }
            else{
                Toast.makeText(MainActivity.this, "something is wrong", Toast.LENGTH_SHORT).show();
            }
            return  true;
        });

    }

    private void replaceFragment(Fragment fragment){
        Log.d("FragmentTransaction", "Replacing with: " + fragment.getClass().getSimpleName());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}