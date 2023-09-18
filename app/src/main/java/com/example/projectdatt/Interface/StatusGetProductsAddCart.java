package com.example.projectdatt.Interface;

import com.example.projectdatt.Model.Products;
import com.example.projectdatt.Model.ProductsAddCart;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface StatusGetProductsAddCart {
    void onSuccess(List<ProductsAddCart> listProductsAddCart);
    void onError(DatabaseError error);
}
