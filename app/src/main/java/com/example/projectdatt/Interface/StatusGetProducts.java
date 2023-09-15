package com.example.projectdatt.Interface;

import com.example.projectdatt.Model.Products;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface StatusGetProducts {
    void onSuccess(List<Products> listProducts);
    void onError(DatabaseError error);
}
