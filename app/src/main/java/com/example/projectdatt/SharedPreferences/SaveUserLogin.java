package com.example.projectdatt.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.projectdatt.Model.Users;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;

public class SaveUserLogin {
    private static final String MY_SHARED_PREFS = "MySharedPrefs";
    private static final String MY_ACCOUNT = "myAccount";
    public static void saveAccount(Context context, Users user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String dataUser = gson.toJson(user);
        editor.putString(MY_ACCOUNT, dataUser);
        editor.apply();
    }
    public static Users getAccount(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE);
        String my_account = sharedPreferences.getString(MY_ACCOUNT, "");
        if (my_account.isEmpty()){
            return null;
        }
        Gson gson = new Gson();
        Users user = gson.fromJson(my_account, Users.class);
        return user;
    }
}
