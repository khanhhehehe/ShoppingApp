package com.example.projectdatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.Interface.StatusGetUsers;
import com.example.projectdatt.Model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private CheckBox myCheckbox;
    private EditText edt_username, edt_passwd;
    private Button btn_login;
    private TextView tv_register;
    private Dialog dialog1, dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ShowBottomDialogLogin();

    }

    private void GetDataUsersThenLogin(String username, String password) {
        for (Users u : FirebaseDao.myListUsers) {
            if (u.getName().equals(username)) {
                if (u.getPass().equals(password)) {
                    dialog1.dismiss();
                    dialog2.dismiss();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    return;
                }
            }
        }
        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
    }

    private void ShowBottomDialogLogin() {
        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.bottom_login);
        edt_username = dialog1.findViewById(R.id.edt_username);
        edt_passwd = dialog1.findViewById(R.id.edt_passwd);
        btn_login = dialog1.findViewById(R.id.btn_login);
        tv_register = dialog1.findViewById(R.id.tv_register);
        myCheckbox = dialog1.findViewById(R.id.myCheckbox);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_username.getText().toString().isEmpty() || edt_passwd.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    GetDataUsersThenLogin(edt_username.getText().toString(), edt_passwd.getText().toString());
                }
            }
        });
        tv_register.setOnClickListener(view -> {
            dialog1.dismiss();
            ShowBottomDialogRegister();
        });
        dialog1.show();
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void GetDataUsersThenRegister(String username, String phone, String password) {
        for (Users u : FirebaseDao.myListUsers) {
            if (u.getName().equals(username)) {
                Toast.makeText(LoginActivity.this, "Tên người dùng đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Users user = new Users(username, phone, password, false);
        DatabaseReference usersRef = FirebaseDao.db.getReference().child("Users");
        usersRef.push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(LoginActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                ShowBottomDialogLogin();
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowBottomDialogRegister() {
        dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.bottom_dialog_register);
        EditText edt_username_res = dialog2.findViewById(R.id.edt_username_res);
        EditText edt_passwd_res = dialog2.findViewById(R.id.edt_passwd_res);
        EditText edt_passwd_res_again = dialog2.findViewById(R.id.edt_passwd_res_again);
        EditText edt_phone_res = dialog2.findViewById(R.id.edt_email_res);
        Button btn_res = dialog2.findViewById(R.id.btn_res);
        TextView tv_login = dialog2.findViewById(R.id.tv_login);
        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edt_username_res.getText().toString();
                String passwd = edt_passwd_res.getText().toString();
                String pass_again = edt_passwd_res_again.getText().toString();
                String phone = edt_phone_res.getText().toString();
                if (username.isEmpty() || passwd.isEmpty() || pass_again.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                } else if (!passwd.equals(pass_again)) {
                    Toast.makeText(LoginActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                } else {
                    GetDataUsersThenRegister(username, phone, passwd);
                }
            }
        });
        tv_login.setOnClickListener(view -> {
            dialog2.dismiss();
            ShowBottomDialogLogin();
        });
        dialog2.show();
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.getWindow().setGravity(Gravity.BOTTOM);
    }
}