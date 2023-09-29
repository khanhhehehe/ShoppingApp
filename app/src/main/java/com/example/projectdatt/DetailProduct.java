package com.example.projectdatt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectdatt.Adapter.Home.HomeAdapter;
import com.example.projectdatt.Adapter.Product.ProductaAdapter;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.Model.Products;
import com.example.projectdatt.SharedPreferences.SaveUserLogin;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailProduct extends AppCompatActivity {
    Products products;
    Context context;
    int numSelect;
    double discountAmount = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        products = new Products();
        numSelect = 1;

        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        String productName = intent.getStringExtra("productName");
        String productImage = intent.getStringExtra("productImage");
        int productQuantity = intent.getIntExtra("productQuantity", 0);
        String productDescription = intent.getStringExtra("productDescription");
        int productPrice = intent.getIntExtra("productPrice", 0);


        TextView tv_nameproduct = findViewById(R.id.tv_nameproduct);
        TextView tv_quantity = findViewById(R.id.tv_quantity);
        TextView tv_num = findViewById(R.id.tv_num);
        ImageView img_product = findViewById(R.id.img_product);
        ImageButton img_btn_minus = findViewById(R.id.img_btn_minus);
        ImageButton img_btn_add = findViewById(R.id.img_btn_add);
        TextView tv_description = findViewById(R.id.tv_description);
        TextView tv_price = findViewById(R.id.tv_price);
        Button btn_addcart = findViewById(R.id.btn_addcart);

        Picasso.get().load(productImage).placeholder(R.drawable.shoppingbag).error(R.drawable.shoppingbag).into(img_product);
        tv_nameproduct.setText(productName);
        tv_quantity.setText("Kho: " + productQuantity);
        tv_description.setText(productDescription);
        tv_price.setText(productPrice + " VND");
        tv_num.setText(numSelect + "");

        img_btn_minus.setOnClickListener(view -> {
            if (numSelect > 0) {
                numSelect--;
                tv_num.setText(numSelect + "");
                tv_price.setText(numSelect * productPrice + " VND");
            }
        });
        img_btn_add.setOnClickListener(view -> {
            if (numSelect < productQuantity) {
                numSelect++;
                tv_num.setText(numSelect + "");
                tv_price.setText(numSelect * productPrice + " VND");
            }
        });
        btn_addcart.setOnClickListener(view -> {
            FirebaseDao.AddProductToCart(SaveUserLogin.getAccount(this).getId(),
                    productId, productName, productImage,
                    numSelect, numSelect * productPrice, this);
        });
        List<Products> horizontalProducts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Products product = new Products();
            horizontalProducts.add(product);
        }


        RecyclerView horizontalRecyclerView = findViewById(R.id.recyclerViewOtherProducts);
        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ProductaAdapter horizontalAdapter = new ProductaAdapter(this);
        horizontalAdapter.setDataProducts(horizontalProducts);
        horizontalRecyclerView.setAdapter(horizontalAdapter);
        horizontalAdapter.setDataProducts(FirebaseDao.myListProducts);
    }

}