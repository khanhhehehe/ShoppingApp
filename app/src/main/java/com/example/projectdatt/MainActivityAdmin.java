package com.example.projectdatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.projectdatt.Fragment.Cart.CartFragment;
import com.example.projectdatt.Fragment.Home.HomeFragment;
import com.example.projectdatt.Fragment.ManageOrder.OrderFragment;
import com.example.projectdatt.Fragment.ManageProduct.ProductFragment;
import com.example.projectdatt.Fragment.ManageRevenue.RevenueFragment;
import com.example.projectdatt.Fragment.ManageUser.ManageUserFragment;
import com.example.projectdatt.Fragment.Profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivityAdmin extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        bottomNavigationView = findViewById(R.id.bottomNavigation_admin);
        replaceFragment(RevenueFragment.newInstance());
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_doanhthu:
                        replaceFragment(RevenueFragment.newInstance());
                        return true;
                    case R.id.bottom_sanpham:
                        replaceFragment(ProductFragment.newInstance());
                        return true;
                    case R.id.bottom_donhang:
                        replaceFragment(OrderFragment.newInstance());
                        return true;
                    case R.id.bottom_taikhoan:
                        replaceFragment(ManageUserFragment.newInstance());
                        return true;
                }
                return false;
            }
        });
    }
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_content_admin, fragment);
        transaction.commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }
}