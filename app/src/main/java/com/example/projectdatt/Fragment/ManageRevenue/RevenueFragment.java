package com.example.projectdatt.Fragment.ManageRevenue;


import static com.example.projectdatt.FirebaseDAO.FirebaseDao.billList;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projectdatt.Adapter.Profile.History.HistoryAdapter;
import com.example.projectdatt.Adapter.revenue.RevenueAdapter;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.Fragment.Profile.History.HistoryFragment;
import com.example.projectdatt.Interface.StatusGetHistoryBill;
import com.example.projectdatt.Model.Bill;
import com.example.projectdatt.Model.ProductSummary;
import com.example.projectdatt.Model.ProductsAddCart;
import com.example.projectdatt.R;
import com.google.firebase.database.DatabaseError;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class RevenueFragment extends Fragment {

    private EditText startDateEditText;
    private EditText endDateEditText;
    private Calendar startCalendar;
    private Calendar endCalendar;
    private TextView txtdoanhthu,txtdoanhso,showfullds;
    private Button btntinh;
    public RevenueFragment() {
    }


    public static RevenueFragment newInstance() {
        RevenueFragment fragment = new RevenueFragment();
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
        return inflater.inflate(R.layout.fragment_revenue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetView(view);

        // Read and update the list of bills
        FirebaseDao.ReadHistory(new StatusGetHistoryBill() {
            @Override
            public void onSuccess(List<Bill> billList) {
                // Update the list in FirebaseDao
                FirebaseDao.billList = billList;

            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", "Failed to get bills: " + error.getMessage());
            }
        }, getContext());

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        setupDatePicker(startDateEditText, startCalendar);
        setupDatePicker(endDateEditText, endCalendar);


        btntinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = startDateEditText.getText().toString();
                String endDate = endDateEditText.getText().toString();

                int totalDoanhThu = 0;
                int totalDoanhSo = 0;
                String detail = "";
                // Khai báo Map để theo dõi sản phẩm và tổng số lượng
                List<ProductSummary> aggregatedProductList = new ArrayList<>();

                for (Bill bill : billList) {
                    if (bill.getOrder_status().equals("Đã thanh toán")) {
                        String billDate = bill.getDate(); // Sử dụng trường ngày của hóa đơn
                        if (isWithinDateRange(billDate, startDate, endDate)) {
                            totalDoanhThu += bill.getTotalprice();
                            for (ProductsAddCart item : bill.getCartList()) {
                                totalDoanhSo +=  item.getNum_product();
                                String productName = item.getName_product();
                                int totalQuantity = item.getNum_product() ;
                                int totalAmount = item.getPricetotal_product();
                                String imageURL = item.getImage_product();


                                // Kiểm tra xem sản phẩm đã tồn tại trong danh sách tổng hợp chưa
                                boolean isProductExist = false;
                                for (ProductSummary productInfo : aggregatedProductList) {
                                    if (productInfo.getProductName().equals(productName)) {
                                        // Sản phẩm đã tồn tại, cộng tổng số lượng và tổng tiền
                                        productInfo.setTotalQuantity(productInfo.getTotalQuantity() + totalQuantity);
                                        productInfo.setTotalAmount(  productInfo.getTotalAmount() + totalAmount);

                                        isProductExist = true;
                                        break;
                                    }
                                }

                                // Nếu sản phẩm chưa tồn tại, thêm mới vào danh sách tổng hợp
                                if (!isProductExist) {
                                    aggregatedProductList.add(new ProductSummary(productName, totalQuantity, imageURL, totalAmount));
                                }
                            }
                        }
                    }
                }

                // Gán danh sách đã tổng hợp vào adapter và hiển thị trong ListView
                RevenueAdapter adapter = new RevenueAdapter(getContext(), aggregatedProductList);
                ListView listView = view.findViewById(R.id.listview);
                listView.setAdapter(adapter);

                showfullds.setText(detail);
                txtdoanhthu.setText("Tổng doanh thu: " + totalDoanhThu + " VNĐ");
                txtdoanhso.setText("Tổng doanh số: " + totalDoanhSo);
            }

        });
    }
    private void GetView(View view){
        showfullds = view.findViewById(R.id.showfullds);
        startDateEditText = view.findViewById(R.id.startDateEditText);
        endDateEditText = view.findViewById(R.id.endDateEditText);
        txtdoanhso = view.findViewById(R.id.showds);
        txtdoanhthu = view.findViewById(R.id.showdt);
        btntinh = view.findViewById(R.id.btntinh);
        ListView listView = view.findViewById(R.id.listview);
    }
    private boolean isWithinDateRange(String date, String startDate, String endDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            Date billDate = dateFormat.parse(date);
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            return billDate.compareTo(start) >= 0 && billDate.compareTo(end) <= 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra trong việc xử lý ngày tháng
        }
    }



    private void setupDatePicker(EditText editText, Calendar calendar) {
        editText.setFocusable(false);
        editText.setClickable(true);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editText, calendar);
            }
        });
    }

    private void showDatePickerDialog(EditText editText, Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                calendar.set(Calendar.YEAR, selectedYear);
                calendar.set(Calendar.MONTH, selectedMonth);
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                editText.setText(dateFormat.format(calendar.getTime()));
            }
        }, year, month, day);

        datePickerDialog.show();
    }

}