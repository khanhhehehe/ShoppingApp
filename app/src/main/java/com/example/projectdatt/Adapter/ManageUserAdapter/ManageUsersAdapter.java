package com.example.projectdatt.Adapter.ManageUserAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdatt.Adapter.Profile.History.CancelSucessfulFragment;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.Fragment.ManageUser.SuccessfulFragment;
import com.example.projectdatt.Model.Users;
import com.example.projectdatt.R;
import com.example.projectdatt.SharedPreferences.SaveUserLogin;

import java.util.List;

public class ManageUsersAdapter extends RecyclerView.Adapter<ManageUsersAdapter.ManageUsersViewHolder>{
    private Context context;
    private List<Users> usersList;

    public ManageUsersAdapter(Context context) {
        this.context = context;
    }

    public void setDataUsers(List<Users> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ManageUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ManageUsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageUsersViewHolder holder, int position) {
        Users users = usersList.get(position);
        if (users == null) {
            return;
        }
        holder.name_user.setText(users.getName());
        holder.phone_user.setText(users.getPhone());
        holder.switchButton.setChecked(users.isRole());
        if (users.isRole()){
            holder.switchButton.setText("Admin");
        }else{
            holder.switchButton.setText("User");
        }
        holder.switchButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Thay đổi vai trò");
            builder.setMessage("Bạn có chắc muốn thay đổi quyền người dùng?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDao.UpdateRoleUser(users.getId(), !users.isRole(), context);
                    FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.layout_content_admin, SuccessfulFragment.newInstance()).addToBackStack(null).commit();
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    holder.switchButton.setChecked(users.isRole());
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        if (!SaveUserLogin.getAccount(context).getName().equals("admin")){
            if (users.isRole()){
                holder.img_ban.setVisibility(View.GONE);
            }
        }
        if (!users.isBan()){
            holder.img_ban.setBackgroundColor(Color.parseColor("#FF5722"));
            holder.img_ban.setImageResource(R.drawable.baseline_do_not_disturb_alt_24);
        }else{
            holder.img_ban.setBackgroundColor(Color.parseColor("#00C853"));
            holder.img_ban.setImageResource(R.drawable.baseline_loop_24);
        }
        holder.img_ban.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Trạng thái tài khoản");
            builder.setMessage("Bạn có chắc muốn thay đổi trạng thái tài khoản này?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDao.BanUser(users.getId(), !users.isBan(), context);
                    FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.layout_content_admin, SuccessfulFragment.newInstance()).addToBackStack(null).commit();
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
        });
    }

    @Override
    public int getItemCount() {
        return usersList == null ? 0 : usersList.size();
    }

    public class ManageUsersViewHolder extends RecyclerView.ViewHolder {
        TextView name_user, phone_user;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switchButton;
        ImageView img_user, img_ban;

        public ManageUsersViewHolder(@NonNull View itemView) {
            super(itemView);
            name_user = itemView.findViewById(R.id.name_user);
            phone_user = itemView.findViewById(R.id.phone_user);
            switchButton = itemView.findViewById(R.id.switchButton);
            img_user = itemView.findViewById(R.id.img_user);
            img_ban = itemView.findViewById(R.id.img_ban);
        }
    }
}
