package com.example.projectdatt.Adapter.ManageUserAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.projectdatt.Fragment.ManageUser.Adminragment;
import com.example.projectdatt.Fragment.ManageUser.UsersFragment;

public class TabLayoutManageUsersAdapter extends FragmentStateAdapter {
    public TabLayoutManageUsersAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new UsersFragment();
            case 1:
                return new Adminragment();
            default:
                return new UsersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
