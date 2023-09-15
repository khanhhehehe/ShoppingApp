package com.example.projectdatt.FirebaseDAO;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projectdatt.Interface.StatusGetProducts;
import com.example.projectdatt.Interface.StatusGetUsers;
import com.example.projectdatt.LoginActivity;
import com.example.projectdatt.Model.Products;
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
import java.util.HashMap;
import java.util.List;

public class FirebaseDao {
    public static FirebaseDatabase db = FirebaseDatabase.getInstance();
    public static List<Users> myListUsers;
    public static List<Products> myListProducts;

    //-----------------About Users
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

    public static void UpdateListUsers() {
        ReadUsers(new StatusGetUsers() {
            @Override
            public void onSuccess(List<Users> listUsers) {
                FirebaseDao.myListUsers = listUsers;
            }

            @Override
            public void onError(DatabaseError error) {
                Log.d("ERROR", "onError: ");
            }
        });
    }//to update myListUsers

    public static void UpdateProfileUser(String id, String name, String phone,Context context) {
        HashMap hashMap = new HashMap();
        hashMap.put("name", name);
        hashMap.put("phone", phone);
        DatabaseReference userRef = db.getReference().child("Users");
        userRef.child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                UpdateListUsers();
                Toast.makeText(context, "Sửa thông tin thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Sửa thông tin thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void UpdatePasswordUser(String id, String password,Context context) {
        HashMap hashMap = new HashMap();
        hashMap.put("pass", password);
        DatabaseReference userRef = db.getReference().child("Users");
        userRef.child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                UpdateListUsers();
                Toast.makeText(context, "Sửa mật khẩu thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Sửa mật khẩu thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //-----------------About Products
    public static void ReadProducts(final StatusGetProducts callback) {
        DatabaseReference myRef = db.getReference().child("Products");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Products> listProducts = new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Products p = dt.getValue(Products.class);
                    if (p == null) {
                        continue;
                    }
                    p.setId(dt.getKey());
                    listProducts.add(p);
                }
                callback.onSuccess(listProducts);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase ListProducts", "DatabaseError: " + error.toString());
                callback.onError(error);
            }
        });
    }

    public static void UpdateListProducts() {
        ReadProducts(new StatusGetProducts() {
            @Override
            public void onSuccess(List<Products> listProducts) {
                FirebaseDao.myListProducts = listProducts;
            }

            @Override
            public void onError(DatabaseError error) {
                Log.d("ERROR", "onError: ");
            }
        });
    }//to update myListProducts

    public static void AddNewProduct(String product_name, int price, String description, Context context) {
        Products product = new Products(product_name, price, description);
        DatabaseReference productsRef = db.getReference().child("Products");
        productsRef.push().setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                UpdateListProducts();
                Toast.makeText(context, "Thêm thành sản phẩm công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void UpdateInfoProduct(String id, String product_name, int price, String description,Context context) {
        Products product = new Products(product_name,price,description);
        DatabaseReference productRef = db.getReference().child("Products");
        productRef.child(id).setValue(product).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                UpdateListProducts();
                Toast.makeText(context, "Sửa thông tin thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Sửa thông tin thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
