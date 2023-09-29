package com.example.projectdatt.Adapter.Home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdatt.DetailProduct;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.LoginActivity;
import com.example.projectdatt.Model.Products;
import com.example.projectdatt.R;
import com.example.projectdatt.SharedPreferences.SaveUserLogin;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ProductVerticalViewHolder> {
    private Context context;
    private List<Products> productsList;
    int numSelect;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    public void setDataProducts(List<Products> productsList) {
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductVerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_vertical, parent, false);
        return new ProductVerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVerticalViewHolder holder, int position) {
        Products products = productsList.get(position);
        if (products == null) {
            return;
        }
        Picasso.get().load(products.getImage()).placeholder(R.drawable.shoppingbag).error(R.drawable.shoppingbag).into(holder.image_imgproduct);
        holder.tv_productname.setText(products.getProduct_name());
        holder.item_product.setOnClickListener(view -> {
            // ShowDialogAddToCart(products);
// Tạo Intent để chuyển sang ProductDetailActivity
            Intent intent = new Intent(context, DetailProduct.class);
            // Truyền dữ liệu sản phẩm qua Intent

            intent.putExtra("productId",products.getId());
            intent.putExtra("productName", products.getProduct_name());
            intent.putExtra("productImage", products.getImage());
            intent.putExtra("productQuantity", products.getQuantity());
            intent.putExtra("productDescription", products.getDescription());
            intent.putExtra("productPrice", products.getPrice());

            // Chuyển sang ProductDetailActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productsList == null ? 0 : productsList.size();
    }

    public class ProductVerticalViewHolder extends RecyclerView.ViewHolder {
        ImageView image_imgproduct;
        TextView tv_productname;
        LinearLayout item_product;

        public ProductVerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            image_imgproduct = itemView.findViewById(R.id.image_imgproduct);
            tv_productname = itemView.findViewById(R.id.tv_productname);
            item_product = itemView.findViewById(R.id.item_product);
        }
    }

    private void ShowDialogAddToCart(Products products) {
        numSelect = 1;
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_order);
        TextView tv_nameproduct = dialog.findViewById(R.id.tv_nameproduct);
        TextView tv_quantity = dialog.findViewById(R.id.tv_quantity);
        TextView tv_num = dialog.findViewById(R.id.tv_num);
        ImageView img_product = dialog.findViewById(R.id.img_product);
        ImageButton img_btn_minus = dialog.findViewById(R.id.img_btn_minus);
        ImageButton img_btn_add = dialog.findViewById(R.id.img_btn_add);
        TextView tv_description = dialog.findViewById(R.id.tv_description);
        TextView tv_price = dialog.findViewById(R.id.tv_price);
        Button btn_addcart = dialog.findViewById(R.id.btn_addcart);

        Picasso.get().load(products.getImage()).placeholder(R.drawable.shoppingbag).error(R.drawable.shoppingbag).into(img_product);
        tv_nameproduct.setText(products.getProduct_name());
        tv_quantity.setText("Kho: " + products.getQuantity());
        tv_num.setText(numSelect + "");
        tv_description.setText(products.getDescription());
        tv_price.setText(numSelect * products.getPrice() + " VND");
        img_btn_minus.setOnClickListener(view -> {
            if (numSelect > 0) {
                numSelect--;
                tv_num.setText(numSelect + "");
                tv_price.setText(numSelect * products.getPrice() + " VND");
            }
        });
        img_btn_add.setOnClickListener(view -> {
            if (numSelect < products.getQuantity()) {
                numSelect++;
                tv_num.setText(numSelect + "");
                tv_price.setText(numSelect * products.getPrice() + " VND");
            }
        });
        btn_addcart.setOnClickListener(view -> {
            dialog.dismiss();
            FirebaseDao.AddProductToCart(SaveUserLogin.getAccount(context).getId()
                    , products.getId(), products.getProduct_name(), products.getImage()
                    , numSelect, numSelect * products.getPrice(), context);
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


}
