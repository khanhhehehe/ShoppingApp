package com.example.projectdatt;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.SharedPreferences.SaveUserLogin;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProductActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private CircleImageView imgAnh ;
    private ImageView btnAlbum ;
    private Button btn_addProduct;
    private EditText ed_Nameproduct,ed_Price,ed_typeProduct,ed_quantity,ed_descProduct ;
    //store

    private Uri img_uri;
    private StorageTask uploadtalk;
    private String muri;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        btn_addProduct=findViewById(R.id.btn_addProduct);
        ed_Nameproduct=findViewById(R.id.ed_nameProduct);
        ed_Price=findViewById(R.id.ed_priceProduct);
        ed_typeProduct=findViewById(R.id.ed_typeProduct);
        ed_quantity=findViewById(R.id.ed_quantityProduct);
        ed_descProduct=findViewById(R.id.ed_desProduct);

        btnAlbum = findViewById(R.id.btn_album);
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
        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namePr = ed_Nameproduct.getText().toString().trim();
                String priceStr = ed_Price.getText().toString().trim();
                String quantityStr = ed_quantity.getText().toString().trim();
                String typePr = ed_typeProduct.getText().toString().trim();
                String descPr = ed_descProduct.getText().toString().trim();

                // Check if any of the fields are empty
                if (namePr.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty() || typePr.isEmpty() || descPr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // Parse the price and quantity as integers
                        int price = Integer.parseInt(priceStr);
                        int quantity = Integer.parseInt(quantityStr);

                        // Check if price and quantity are greater than zero
                        if (price <= 0 || quantity <= 0) {
                            Toast.makeText(getApplicationContext(), "Price and Quantity must be greater than zero", Toast.LENGTH_SHORT).show();
                        } else {
                            // All input is valid, proceed to upload
                            uploadImageToFirebase(namePr, typePr, quantity, price, descPr);
                        }
                    } catch (NumberFormatException e) {
                        // Handle the case where price or quantity cannot be parsed as integers
                        Toast.makeText(getApplicationContext(), "Price and Quantity must be valid numbers", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    private void uploadImageToFirebase(final String namePr, final String typePr, final int quantity, final int price, final String descPr) {
        if (img_uri != null) {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage("Uploading...");
//            progressDialog.show();

            // Generate a unique file name for the image using UUID
            String fileName = UUID.randomUUID().toString();

            // Create a reference to the Firebase Storage path
            StorageReference fileReference = storageRef.child("products/" + fileName);

            // Upload the image
            uploadtalk = fileReference.putFile(img_uri);

            // Continue with the upload task
            uploadtalk.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
//                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        muri = downloadUri.toString();

                        // Create a new Product object
                        FirebaseDao.AddNewProduct(namePr, muri, typePr, quantity, price, descPr, AddProductActivity.this);

                        // Finish the activity
                        finish();
                    } else {
                        Toast.makeText(AddProductActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    progressDialog.dismiss();
                    Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // ... (other methods)

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            img_uri = data.getData();
            Glide.with(this).load(img_uri).into(imgAnh);
        }
    }



}