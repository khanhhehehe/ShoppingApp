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

import com.example.projectdatt.Adapter.Cart.CartAdapter;
import com.example.projectdatt.Adapter.ManageUserAdapter.ManageUsersAdapter;
import com.example.projectdatt.FirebaseDAO.FirebaseDao;
import com.example.projectdatt.Model.Users;
import com.example.projectdatt.R;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {
    RecyclerView recycler_listusers;
    ManageUsersAdapter manageUsersAdapter;

    public UsersFragment() {
        // Required empty public constructor
    }

    public static UsersFragment newInstance() {
        UsersFragment fragment = new UsersFragment();
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
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetView(view);
        manageUsersAdapter = new ManageUsersAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_listusers.setLayoutManager(layoutManager);
        recycler_listusers.setAdapter(manageUsersAdapter);
        manageUsersAdapter.setDataUsers(GetDataUsers());
    }
    private void GetView(View view) {
        recycler_listusers = view.findViewById(R.id.recycler_users);
    }
    public List<Users> GetDataUsers(){
        List<Users> myListUsers = new ArrayList<>();
        for (Users u : FirebaseDao.myListUsers) {
            if(!u.isRole()){
                myListUsers.add(u);
            }
        }
        return myListUsers;
    }
}