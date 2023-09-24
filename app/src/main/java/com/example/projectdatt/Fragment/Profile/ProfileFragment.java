package com.example.projectdatt.Fragment.Profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.Fragment.Profile.History.HistoryFragment;
import com.example.projectdatt.LoginActivity;
import com.example.projectdatt.MainActivity;
import com.example.projectdatt.Model.Users;
import com.example.projectdatt.R;
import com.example.projectdatt.SharedPreferences.SaveUserLogin;
import com.google.android.gms.common.api.Response;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    TextView tv_update, tv_name_profile, tv_phone, tv_username;
    LinearLayout linear_logout, linear_changepass,linear_history;
    ImageView img_user;
    Users user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = SaveUserLogin.getAccount(getContext());
        GetView(view);
        Picasso.get().load(user.getImage()).placeholder(R.drawable.shoppingbag).error(R.drawable.shoppingbag).into(img_user);
        tv_name_profile.setText(user.getName());
        tv_username.setText(user.getName());
        tv_phone.setText(user.getPhone());

        tv_update.setOnClickListener(view1 -> {
            ShowDialogUpdateProfile();
        });
        linear_changepass.setOnClickListener(view1 -> {
            ShowDialogUpdatePassword();
        });
        if (user.isRole()){
            linear_history.setVisibility(View.GONE);
        }
        linear_history.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.layout_content, HistoryFragment.newInstance()).addToBackStack(null).commit();
        });
        linear_logout.setOnClickListener(view1 -> {
            ShowDialogLogout();
        });
    }

    private void GetView(View view) {
        tv_update = view.findViewById(R.id.tv_update);
        tv_name_profile = view.findViewById(R.id.tv_name_profile);
        tv_username = view.findViewById(R.id.tv_username);
        tv_phone = view.findViewById(R.id.tv_phone);
        img_user = view.findViewById(R.id.img_user);
        linear_logout = view.findViewById(R.id.linear_logout);
        linear_changepass = view.findViewById(R.id.linear_changepass);
        linear_history = view.findViewById(R.id.linear_history);
    }

    private void ShowDialogUpdateProfile() {
        Dialog dialog1 = new Dialog(getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.layout_update_profile);
        EditText edt_username = dialog1.findViewById(R.id.edt_username);
        EditText edt_phone = dialog1.findViewById(R.id.edt_phone);
        Button btn_update = dialog1.findViewById(R.id.btn_update);
        ImageView img_cancel = dialog1.findViewById(R.id.img_cancel);
        edt_username.setText(user.getName());
        edt_phone.setText(user.getPhone());
        img_cancel.setOnClickListener(view -> {
            dialog1.dismiss();
        });
        btn_update.setOnClickListener(view -> {
            if (edt_username.getText().toString().isEmpty() || edt_phone.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                return;
            } else if (edt_username.getText().toString().equals(user.getName()) && edt_phone.getText().toString().equals(user.getPhone())) {
                Toast.makeText(getContext(), "Thông tin không thay đổi", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseDao.UpdateProfileUser(user.getId(), edt_username.getText().toString(), edt_phone.getText().toString(), getContext());
            UpdateView(edt_username.getText().toString(), edt_phone.getText().toString());
            dialog1.dismiss();
        });
        dialog1.show();
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void ShowDialogUpdatePassword() {
        Dialog dialog1 = new Dialog(getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.layout_update_password);
        EditText edt_oldpass = dialog1.findViewById(R.id.edt_oldpass);
        EditText edt_pass = dialog1.findViewById(R.id.edt_pass);
        EditText edt_newpass = dialog1.findViewById(R.id.edt_newpass);
        Button btn_update = dialog1.findViewById(R.id.btn_update);
        ImageView img_cancel = dialog1.findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(view -> {
            dialog1.dismiss();
        });
        btn_update.setOnClickListener(view -> {
            if (edt_oldpass.getText().toString().isEmpty() || edt_pass.getText().toString().isEmpty() || edt_newpass.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                return;
            } else if (!edt_oldpass.getText().toString().equals(user.getPass())) {
                Toast.makeText(getContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                return;
            } else if (!edt_pass.getText().toString().equals(edt_newpass.getText().toString())) {
                Toast.makeText(getContext(), "Mật khẩu mới không trùng khớp", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseDao.UpdatePasswordUser(user.getId(), edt_pass.getText().toString(), getContext());
            dialog1.dismiss();
        });
        dialog1.show();
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void ShowDialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc muốn đăng xuất không?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveUserLogin.saveAccount(getContext(), new Users());
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void UpdateView(String name, String phone) {
        user.setName(name);
        user.setPhone(phone);
        SaveUserLogin.saveAccount(getContext(), user);
        tv_name_profile.setText(user.getName());
        tv_username.setText(user.getName());
        tv_phone.setText(user.getPhone());
    }
}