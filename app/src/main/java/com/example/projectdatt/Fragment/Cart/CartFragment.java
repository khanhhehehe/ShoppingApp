package com.example.projectdatt.Fragment.Cart;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectdatt.Adapter.Cart.CartAdapter;
import com.example.projectdatt.Adapter.Home.HomeAdapter;
import com.example.projectdatt.ChooseDiscountActivity;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.LoginActivity;
import com.example.projectdatt.Model.Bill;
import com.example.projectdatt.Model.ProductsAddCart;
import com.example.projectdatt.Model.Users;
import com.example.projectdatt.R;
import com.example.projectdatt.SharedPreferences.SaveUserLogin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    Button btn_pay;
    TextView tv_total;
    RecyclerView recycler_listproductsadd;
    CartAdapter cartAdapter;
    Users user;
    String paymentmethod;
    public static int total = 0;
    Bill bill = new Bill();
    boolean isDiscountUsed = bill.isDiscountUsed();
    int discountAmount = 0;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetView(view);
        user = SaveUserLogin.getAccount(getContext());
        tv_total.setText("Tổng số tiền: " + TotalBill() + " VND");
        cartAdapter = new CartAdapter(getActivity(), tv_total);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_listproductsadd.setLayoutManager(layoutManager);
        recycler_listproductsadd.setAdapter(cartAdapter);
        cartAdapter.setDataProductsCart(FirebaseDao.addCartList);
        btn_pay.setOnClickListener(view1 -> {
            if (FirebaseDao.addCartList.size() == 0) {
                Toast.makeText(getContext(), "Không có sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            ShowDialogPayment();

        });

    }

    private void GetView(View view) {
        btn_pay = view.findViewById(R.id.btn_pay);
        tv_total = view.findViewById(R.id.tv_total);
        recycler_listproductsadd = view.findViewById(R.id.recycler_listproductsadd);
    }

    public int TotalBill() {
        total = 0;
        for (int i = 0; i < FirebaseDao.addCartList.size(); i++) {
            total += FirebaseDao.addCartList.get(i).getPricetotal_product();
        }
        return total;
    }

    private void ShowDialogPayment() {
        paymentmethod = "Thanh toán khi nhận hàng";
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_payment);
        TextView txtma = dialog.findViewById(R.id.txtmagg);
        TextView txttongtien = dialog.findViewById(R.id.txt_tongtien);
        ImageView img_dismiss = dialog.findViewById(R.id.img_dismiss);
        EditText edt_username = dialog.findViewById(R.id.edt_username);
        EditText edt_phone = dialog.findViewById(R.id.edt_phone);
        EditText edt_location = dialog.findViewById(R.id.edt_location);
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);


        edt_username.setText(user.getName());
        edt_phone.setText(user.getPhone());
        img_dismiss.setOnClickListener(view -> {
            dialog.dismiss();
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        paymentmethod = "Thanh toán khi nhận hàng";
                        break;
                    case R.id.radioButton2:
                        paymentmethod = "Thanh toán ngân hàng";
                        break;
                    default:
                        break;
                }
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy ngày tháng hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0 nên cộng thêm 1
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                String currentDate = day + "-" + month + "-" + year;
                String username = edt_username.getText().toString();
                String phone = edt_phone.getText().toString();
                String location = edt_location.getText().toString();
                if (username.isEmpty() || phone.isEmpty() || location.isEmpty()) {
                    Toast.makeText(getContext(), "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    tv_total.setText("Tổng số tiền: 0 VND");
                    List<ProductsAddCart> addCartList = new ArrayList<>();
                    cartAdapter.setDataProductsCart(addCartList);
                    FirebaseDao.Pay(user.getId(), username, phone, location, paymentmethod, FirebaseDao.addCartList, total,currentDate, getContext());
                    dialog.dismiss();
                }
            }
        });
        int newTotal = total - discountAmount; // Áp dụng mã giảm giá cho số tiền tổng
        txtma.setText("Mã giảm: " + discountAmount + " VNĐ");
        txtma.setTextColor(Color.BLUE);
        txttongtien.setText("Tổng tiền: " + newTotal + " VND");
        txttongtien.setTag(newTotal); // Lưu giá trị mới của tổng tiền vào tag của TextView
        txtma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDiscountUsed) {
                    // Nếu mã giảm giá chưa được sử dụng, tính toán giá trị mã giảm giá ngẫu nhiên và lưu lại
                    discountAmount = (int) (Math.random() * 40001) + 10000;
                    int newTotal = total - discountAmount; // Áp dụng mã giảm giá cho số tiền tổng
                    txtma.setText("Mã giảm: " + discountAmount + " VNĐ");
                    txtma.setTextColor(Color.BLUE);
                    txttongtien.setText("Tổng tiền: " + newTotal + " VND");
                    txttongtien.setTag(newTotal); // Lưu giá trị mới của tổng tiền vào tag của TextView
                    isDiscountUsed = true;
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }




}