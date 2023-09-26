package com.example.projectdatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.example.projectdatt.Adapter.Cart.CartAdapter;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.Fragment.Cart.CartFragment;
import com.example.projectdatt.Fragment.Home.HomeFragment;
import com.example.projectdatt.Fragment.Profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigation);



        replaceFragment(HomeFragment.newInstance());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_home:
                        replaceFragment(HomeFragment.newInstance());
                        return true;
                    case R.id.bottom_cart:
                        replaceFragment(CartFragment.newInstance());
                        return true;
                    case R.id.bottom_chat:
                        Intent chat_Activity= new Intent(getBaseContext(), ChatActivity.class);
                        startActivity(chat_Activity);
                        return true;
                    case R.id.bottom_user:
                        replaceFragment(ProfileFragment.newInstance());
                        return true;
                }
                return false;
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_content, fragment);
        transaction.commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }
}