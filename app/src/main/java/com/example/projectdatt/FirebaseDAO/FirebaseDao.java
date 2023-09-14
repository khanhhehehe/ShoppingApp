package com.example.projectdatt.FirebaseDAO;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projectdatt.Interface.StatusGetUsers;
import com.example.projectdatt.LoginActivity;
import com.example.projectdatt.Model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDao {
    public static FirebaseDatabase db = FirebaseDatabase.getInstance();
    public static List<Users> myListUsers;
//    public void LoginUser(String st){
//        db.collection("Users").document().set(st).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Log.d("GGGGGGG", "onSuccess: THem thanh cong");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("GGGGGGG", "onSuccess: THem that bai");
//
//            }
//        });
//    }
public static void ReadUsers(final StatusGetUsers callback) {
    DatabaseReference myRef = db.getReference().child("Users");
    myRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<Users> listUsers = new ArrayList<>();
            for (DataSnapshot dt : dataSnapshot.getChildren()) {
                Users u = dt.getValue(Users.class);
                if (u == null) {
                    continue;
                }
                u.setId(dt.getKey());
                listUsers.add(u);
            }
            callback.onSuccess(listUsers);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            Log.e("Firebase ListUsers", "DatabaseError: " + error.toString());
            callback.onError(error);
        }
    });
}
}
