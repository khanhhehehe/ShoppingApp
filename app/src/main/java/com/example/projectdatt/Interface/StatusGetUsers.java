package com.example.projectdatt.Interface;

import com.example.projectdatt.Model.Users;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface StatusGetUsers {
    void onSuccess(List<Users> listUsers);
    void onError(DatabaseError error);
}
