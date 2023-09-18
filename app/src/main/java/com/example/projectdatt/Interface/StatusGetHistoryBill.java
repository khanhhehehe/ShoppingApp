package com.example.projectdatt.Interface;

import com.example.projectdatt.Model.Bill;
import com.example.projectdatt.Model.Products;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface StatusGetHistoryBill {
    void onSuccess(List<Bill> listBills);
    void onError(DatabaseError error);
}
