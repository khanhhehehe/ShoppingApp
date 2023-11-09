package com.example.projectdatt.Adapter.revenue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdatt.Adapter.Profile.History.CancelSucessfulFragment;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.Model.Bill;
import com.example.projectdatt.Model.ProductSummary;
import com.example.projectdatt.Model.ProductsAddCart;
import com.example.projectdatt.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RevenueAdapter extends ArrayAdapter<ProductSummary> {
    public RevenueAdapter(Context context, List<ProductSummary> aggregatedProductList) {
        super(context, 0, aggregatedProductList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_revenue, parent, false);
        }

        ProductSummary aggregatedProductInfo = getItem(position);

        ImageView productImageView = listItemView.findViewById(R.id.reavt);
        TextView productNameTextView = listItemView.findViewById(R.id.rename);
        TextView totalQuantityTextView = listItemView.findViewById(R.id.renum);
        TextView totalAmountTextView = listItemView.findViewById(R.id.reprice);

        if (aggregatedProductInfo != null) {
            // Đặt hình ảnh sản phẩm (cần sử dụng thư viện hình ảnh, ví dụ: Picasso hoặc Glide)
             Picasso.get().load(aggregatedProductInfo.getImageURL()).into(productImageView);

            productNameTextView.setText("Tên sp: " + aggregatedProductInfo.getProductName());
            totalQuantityTextView.setText( "Số lượng đã bán: "+ String.valueOf(aggregatedProductInfo.getTotalQuantity()));
            totalAmountTextView.setText(String.valueOf("Doanh thu sp: "+ aggregatedProductInfo.getTotalAmount() + " VNĐ"));
        }

        return listItemView;
    }
}
