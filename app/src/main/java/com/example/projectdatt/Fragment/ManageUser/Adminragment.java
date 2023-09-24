package com.example.projectdatt.Fragment.ManageUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectdatt.Adapter.ManageUserAdapter.ManageUsersAdapter;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.Model.Users;
import com.example.projectdatt.R;
import com.example.projectdatt.SharedPreferences.SaveUserLogin;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Adminragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Adminragment extends Fragment {
    RecyclerView recycler_listusers;
    ManageUsersAdapter manageUsersAdapter;
    Users user;
    public Adminragment() {
        // Required empty public constructor
    }

    public static Adminragment newInstance() {
        Adminragment fragment = new Adminragment();
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
        return inflater.inflate(R.layout.fragment_adminragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetView(view);
        user = SaveUserLogin.getAccount(getContext());
        manageUsersAdapter = new ManageUsersAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_listusers.setLayoutManager(layoutManager);
        recycler_listusers.setAdapter(manageUsersAdapter);
        manageUsersAdapter.setDataUsers(GetDataAdmins());
    }
    private void GetView(View view) {
        recycler_listusers = view.findViewById(R.id.recycler_admins);
    }
    public List<Users> GetDataAdmins(){
        List<Users> myListUsers = new ArrayList<>();
        for (Users u : FirebaseDao.myListUsers) {
            if(u.isRole() && !u.getName().equals("admin") && !u.getName().equals(user.getName())){
                myListUsers.add(u);
            }
        }
        return myListUsers;
    }
}