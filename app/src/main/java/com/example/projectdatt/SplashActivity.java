package com.example.projectdatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.Interface.StatusGetUsers;
import com.example.projectdatt.Model.Users;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseDao.ReadUsers(new StatusGetUsers() {
            @Override
            public void onSuccess(List<Users> listUsers) {
                FirebaseDao.myListUsers = listUsers;
            }

            @Override
            public void onError(DatabaseError error) {
                Log.d("ERROR", "onError: ");
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}