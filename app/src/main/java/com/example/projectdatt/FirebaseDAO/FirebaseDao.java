package com.example.projectdatt.FirebaseDAO;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.projectdatt.Adapter.Cart.CartAdapter;
import com.example.projectdatt.Adapter.Profile.History.CancelSucessfulFragment;
import com.example.projectdatt.Adapter.Profile.History.HistoryAdapter;
import com.example.projectdatt.Fragment.Cart.CartFragment;
import com.example.projectdatt.Fragment.Profile.History.HistoryFragment;
import com.example.projectdatt.Fragment.Profile.ProfileFragment;
import com.example.projectdatt.Interface.StatusGetHistoryBill;
import com.example.projectdatt.Interface.StatusGetProducts;
import com.example.projectdatt.Interface.StatusGetProductsAddCart;
import com.example.projectdatt.Interface.StatusGetUsers;
import com.example.projectdatt.LoginActivity;
import com.example.projectdatt.MainActivity;
import com.example.projectdatt.Model.Bill;
import com.example.projectdatt.Model.Products;
import com.example.projectdatt.Model.ProductsAddCart;
import com.example.projectdatt.Model.Users;
import com.example.projectdatt.R;
import com.example.projectdatt.SharedPreferences.SaveUserLogin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FirebaseDao {
    public static FirebaseDatabase db = FirebaseDatabase.getInstance();
    public static List<Users> myListUsers;
    public static List<Products> myListProducts;
    public static List<ProductsAddCart> addCartList;
    public static List<Bill> billList;

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

    public static void UpdateProfileUser(String id, String name, String phone, Context context) {
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

    public static void UpdatePasswordUser(String id, String password, Context context) {
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

    public static void AddNewProduct(String product_name, String image, String type_product, int quantity, int price, String description, Context context) {
        Products product = new Products(product_name, image, type_product, quantity, price, description);
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

    public static void UpdateInfoProduct(String id, String product_name, String image, String type_product, int quantity, int price, String description, Context context) {
        Products product = new Products(product_name, image, type_product, quantity, price, description);
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

    public static void ReadProductsAddCart(final StatusGetProductsAddCart callback, Context context) {
        DatabaseReference myRef = db.getReference().child("ProductsAddCart");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ProductsAddCart> addCartList = new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    ProductsAddCart pro = dt.getValue(ProductsAddCart.class);
                    if (pro == null) {
                        continue;
                    }
                    pro.setId(dt.getKey());
                    if (pro.getId_user().equals(SaveUserLogin.getAccount(context).getId())) {
                        addCartList.add(pro);
                    }
                }
                callback.onSuccess(addCartList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase ListProductsAddCart", "DatabaseError: " + error.toString());
                callback.onError(error);
            }
        });
    }

    public static void UpdateListProductsAddCart(Context context) {
        ReadProductsAddCart(new StatusGetProductsAddCart() {
            @Override
            public void onSuccess(List<ProductsAddCart> addCartList) {
                FirebaseDao.addCartList = addCartList;

                Log.d("LENGTH", "onSuccess: " + FirebaseDao.addCartList.size());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.d("ERROR", "onError: ");
            }
        }, context);
    }//to update myListProductsAddCart

    public static void AddProductToCart(String id_user, String id_product, String name_product, String image_product, int num_product, int pricetotal_product, Context context) {
        ProductsAddCart product = new ProductsAddCart(id_user, id_product, name_product, image_product, num_product, pricetotal_product);
        DatabaseReference productsRef = db.getReference().child("ProductsAddCart");
        productsRef.push().setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void DeleteProductsFromCart(String id, Context context) {
        DatabaseReference myRef = db.getReference().child("ProductsAddCart");
        myRef.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                UpdateListProductsAddCart(context);
            }
        });
    }

    public static void Pay(String id_user, String username, String phone, String location, String paymethod, List<ProductsAddCart> cartList, int total, Context context) {
        Bill bill = new Bill(id_user, username, phone, location, paymethod, "Chờ xác nhận", cartList, total);
        DatabaseReference billRef = db.getReference().child("Bill");
        billRef.push().setValue(bill).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                ClearAddCartThenPay(context);
                Toast.makeText(context, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void ClearAddCartThenPay(Context context) {
        DatabaseReference myRef = db.getReference().child("ProductsAddCart");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    ProductsAddCart pro = dt.getValue(ProductsAddCart.class);
                    if (pro == null) {
                        continue;
                    }
                    pro.setId(dt.getKey());
                    if (pro.getId_user().equals(SaveUserLogin.getAccount(context).getId())) {
                        myRef.child(pro.getId()).removeValue();
                    }
                    Log.d("ITEMM", "onDataChange: " + pro.getId() + " va " + pro.getName_product());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase ListProductsAddCart", "DatabaseError: " + error.toString());
            }
        });
    }

    public static void ReadHistory(final StatusGetHistoryBill callback, Context context) {
        DatabaseReference myRef = db.getReference().child("Bill");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Bill> billList = new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Bill b = dt.getValue(Bill.class);
                    if (b == null) {
                        continue;
                    }
                    b.setId(dt.getKey());
                    if (b.getId_user().equals(SaveUserLogin.getAccount(context).getId())) {
                        billList.add(b);
                    }
                }
                Collections.reverse(billList);
                callback.onSuccess(billList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase ListProductsAddCart", "DatabaseError: " + error.toString());
                callback.onError(error);
            }
        });
    }

    public static void UpdateListBill(Context context) {
        ReadHistory(new StatusGetHistoryBill() {
            @Override
            public void onSuccess(List<Bill> billList) {
                FirebaseDao.billList = billList;
                Log.d("BILL", "onSuccess: " + FirebaseDao.billList.size());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.d("ERROR", "onError: ");
            }
        }, context);
    }//to update myListBill

    public static void SetBillCancel(String id, String status, Context context) {
        HashMap hashMap = new HashMap();
        hashMap.put("order_status", status);
        DatabaseReference productRef = db.getReference().child("Bill");
        productRef.child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Hủy thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
