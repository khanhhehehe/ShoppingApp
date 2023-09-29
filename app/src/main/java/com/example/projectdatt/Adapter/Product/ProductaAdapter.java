package com.example.projectdatt.Adapter.Product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdatt.Adapter.Home.HomeAdapter;
import com.example.projectdatt.DetailProduct;
import com.example.projectdatt.Model.Products;
import com.example.projectdatt.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductaAdapter extends RecyclerView.Adapter<ProductaAdapter.ProductViewHolder> {
    private Context context;
    private List<Products> productsList;
    int numSelect;

    public ProductaAdapter(Context context) {
        this.context = context;
    }

    public void setDataProducts(List<Products> productsList) {
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_ngang, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products products = productsList.get(position);
        if (products == null) {
            return;
        }
        Picasso.get().load(products.getImage()).placeholder(R.drawable.shoppingbag).error(R.drawable.shoppingbag).into(holder.image_imgproduct);
        holder.tv_productname.setText(products.getProduct_name()+"vô danh ");
        holder.item_product.setOnClickListener(view -> {
            // ShowDialogAddToCart(products);
            // Tạo Intent để chuyển sang ProductDetailActivity
            Intent intent = new Intent(context, DetailProduct.class);
            intent.putExtra("productId", products.getId());
            intent.putExtra("productName", products.getProduct_name());
            intent.putExtra("productImage", products.getImage());
            intent.putExtra("productQuantity", products.getQuantity());
            intent.putExtra("productDescription", products.getDescription());
            intent.putExtra("productPrice", products.getPrice());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productsList == null ? 0 : productsList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image_imgproduct;
        TextView tv_productname;
        LinearLayout item_product;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image_imgproduct = itemView.findViewById(R.id.image_product);
            tv_productname = itemView.findViewById(R.id.text_product_name);
            item_product = itemView.findViewById(R.id.item_product_ngang);
        }
    }
}
