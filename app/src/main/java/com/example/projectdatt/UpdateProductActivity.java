package com.example.projectdatt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    EditText edtNameProduct ;
    EditText edtPriceProduct;
    EditText edtTypeProduct ;
    EditText edtQuantityProduct ;
    EditText edtDesProduct ;
    private CircleImageView imgAnh;
    ImageView btnAlbum;
    private Uri imageUri; // Declare imageUri as a class variable
    private String productId;
    String productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        edtNameProduct = findViewById(R.id.ed_nameProduct);
        edtPriceProduct = findViewById(R.id.ed_priceProduct);
        edtTypeProduct = findViewById(R.id.ed_typeProduct);
        edtQuantityProduct = findViewById(R.id.ed_quantityProduct);
        edtDesProduct = findViewById(R.id.ed_desProduct);
        imgAnh = (CircleImageView)findViewById(R.id.img_anh);
        btnAlbum = findViewById(R.id.btn_album);
        Button btnUpdate=findViewById(R.id.btn_UpProduct);
        Button btnCancel=findViewById(R.id.btn_Cancel);

        //
        imgAnh = (CircleImageView) findViewById(R.id.img_anh);
        btnAlbum.setOnClickListener(v -> {
            // Open the gallery to select an image
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });
        imgAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        // Get the data passed from the previous activity
        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("productId");
            String productName = intent.getStringExtra("productName");
            String productPrice = intent.getStringExtra("productPrice");
            String productType = intent.getStringExtra("productType");
            String productQuantity = intent.getStringExtra("productQuantity");
            String productDescription = intent.getStringExtra("productDescription");
            productImage = intent.getStringExtra("productImage");

            // Populate the UI elements with the retrieved data
            edtNameProduct.setText(productName);
            edtPriceProduct.setText(productPrice);
            edtTypeProduct.setText(productType);
            edtQuantityProduct.setText(productQuantity);
            edtDesProduct.setText(productDescription);

            // Load the image using Picasso or another image-loading library
            Picasso.get().load(productImage).into(imgAnh);
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgAnh.setImageURI(imageUri);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgAnh.setImageBitmap(imageBitmap);
        }
    }

    private void updateProduct() {
        String productName = edtNameProduct.getText().toString().trim();
        String priceStr = edtPriceProduct.getText().toString().trim();
        String quantityStr = edtQuantityProduct.getText().toString().trim();
        String productType = edtTypeProduct.getText().toString().trim();
        String productDescription = edtDesProduct.getText().toString().trim();

        // Check if any of the fields are empty
        if (TextUtils.isEmpty(productName)) {
            edtNameProduct.setError("Please enter a name");
            edtNameProduct.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(priceStr)) {
            edtPriceProduct.setError("Please enter a price");
            edtPriceProduct.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(quantityStr)) {
            edtQuantityProduct.setError("Please enter a quantity");
            edtQuantityProduct.requestFocus();
            return;
        }

        try {
            // Parse the price and quantity as integers
            int productPrice = Integer.parseInt(priceStr);
            int productQuantity = Integer.parseInt(quantityStr);

            if (productPrice <= 0) {
                edtPriceProduct.setError("Price must be greater than zero");
                edtPriceProduct.requestFocus();
                return;
            }

            if (productQuantity <= 0) {
                edtQuantityProduct.setError("Quantity must be greater than zero");
                edtQuantityProduct.requestFocus();
                return;
            }

            // Upload the new image to Firebase Storage if an image was selected
            if (imageUri != null) {
                // Upload the image to Firebase Storage
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("product_images");
                final StorageReference imageRef = storageRef.child(productId + ".jpg");

                imageRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Get the updated image URL
                            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String updatedImageURL = uri.toString();

                                // Update the product information in Firebase Realtime Database
                                FirebaseDao.UpdateInfoProduct(productId, productName, updatedImageURL, productType, productQuantity, productPrice, productDescription, this);
                            });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                        });
            } else {
                // No new image was selected, so update product information only
                FirebaseDao.UpdateInfoProduct(productId, productName, productImage, productType, productQuantity, productPrice, productDescription, this);
                finish();
            }
        } catch (NumberFormatException e) {
            // Handle the case where price or quantity cannot be parsed as integers
            Toast.makeText(this, "Price and Quantity must be valid numbers", Toast.LENGTH_SHORT).show();
        }
    }


}