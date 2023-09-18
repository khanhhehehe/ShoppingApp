package com.example.projectdatt.Adapter.Cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdatt.Adapter.Home.HomeAdapter;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.Fragment.Cart.CartFragment;
import com.example.projectdatt.LoginActivity;
import com.example.projectdatt.Model.Products;
import com.example.projectdatt.Model.ProductsAddCart;
import com.example.projectdatt.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<ProductsAddCart> productsList;
    TextView tv_totalbill;

    public CartAdapter(Context context,TextView tv_totalbill) {
        this.context = context;
        this.tv_totalbill = tv_totalbill;
    }

    public void setDataProductsCart(List<ProductsAddCart> productsList) {
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addcart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductsAddCart products = productsList.get(position);
        if (products == null) {
            return;
        }
        Picasso.get().load(products.getImage_product()).placeholder(R.drawable.shoppingbag).error(R.drawable.shoppingbag).into(holder.img_pro);
        holder.name_pro.setText(products.getName_product());
        holder.num_pro.setText("Số lượng: " + products.getNum_product());
        holder.img_delete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa sản phẩm");
            builder.setMessage("Bạn có chắc muốn xóa sản phẩm ra khỏi giỏ hàng?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<ProductsAddCart> newList = FirebaseDao.addCartList;
                    for (ProductsAddCart p : newList) {
                        if (p.getId().equals(products.getId())) {
                            newList.remove(p);
                            break;
                        }
                    }
                    setDataProductsCart(newList);

                    // set tv_total_price_again
                    CartFragment.total = 0;
                    for (int i=0;i<newList.size();i++){
                        CartFragment.total+=newList.get(i).getPricetotal_product();
                    }
                    tv_totalbill.setText("Tổng số tiền: "+CartFragment.total+" VND");

                    FirebaseDao.DeleteProductsFromCart(products.getId(),context);
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
        return productsList == null ? 0 : productsList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView img_pro;
        TextView name_pro, num_pro;
        ImageView img_delete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img_pro = itemView.findViewById(R.id.img_pro);
            name_pro = itemView.findViewById(R.id.name_pro);
            num_pro = itemView.findViewById(R.id.num_pro);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }
}
