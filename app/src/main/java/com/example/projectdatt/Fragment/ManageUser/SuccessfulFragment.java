package com.example.projectdatt.Fragment.ManageUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectdatt.Fragment.Profile.History.HistoryFragment;
import com.example.projectdatt.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuccessfulFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuccessfulFragment extends Fragment {
    public SuccessfulFragment() {
        // Required empty public constructor
    }

    public static SuccessfulFragment newInstance() {
        SuccessfulFragment fragment = new SuccessfulFragment();
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
        return inflater.inflate(R.layout.fragment_successful, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.layout_content_admin, ManageUserFragment.newInstance()).addToBackStack(null).commit();
            }
        }, 2000);
    }
}