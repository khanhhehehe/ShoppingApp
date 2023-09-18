package com.example.projectdatt.Adapter.Profile.History;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdatt.Adapter.Cart.CartAdapter;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.LoginActivity;
import com.example.projectdatt.Model.Bill;
import com.example.projectdatt.Model.ProductsAddCart;
import com.example.projectdatt.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context context;
    private List<Bill> billList;

    public HistoryAdapter(Context context) {
        this.context = context;
    }

    public void setDataProductsCart(List<Bill> billList) {
        this.billList = billList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Bill bill = billList.get(position);
        if (bill == null) {
            return;
        }
        String detail = "";
        for (ProductsAddCart item : bill.getCartList()) {
            detail += item.getName_product() + ": " + item.getNum_product() + "\n";
        }
        holder.tv_detail.setText(detail);
        holder.tv_location.setText("Địa chỉ: " + bill.getLocation());
        holder.tv_phone.setText("Điện thoại: " + bill.getPhone());
        holder.tv_method.setText(bill.getPaymethod());
        holder.tv_totalprice.setText(bill.getTotalprice() + "");
        holder.tv_order_status.setText(bill.getOrder_status());
        if (bill.getOrder_status().equals("Đã hủy")) {
            holder.btn_cancel.setEnabled(false);
        } else {
            holder.btn_cancel.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Hủy đơn hàng");
                builder.setMessage("Bạn có chắc muốn hủy đơn hàng không?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDao.SetBillCancel(bill.getId(), "Đã hủy", context);
                        FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.layout_content, CancelSucessfulFragment.newInstance()).addToBackStack(null).commit();
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
    }

    @Override
    public int getItemCount() {
        return billList == null ? 0 : billList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_detail, tv_location, tv_phone, tv_method, tv_totalprice, tv_order_status;
        Button btn_cancel;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_method = itemView.findViewById(R.id.tv_method);
            tv_totalprice = itemView.findViewById(R.id.tv_totalprice);
            tv_order_status = itemView.findViewById(R.id.tv_order_status);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
        }
    }
}
