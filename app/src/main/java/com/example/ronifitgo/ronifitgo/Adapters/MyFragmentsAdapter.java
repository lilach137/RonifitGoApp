package com.example.ronifitgo.ronifitgo.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ronifitgo.ronifitgo.Fragments.fragment_homePage;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_measures;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_weights;

public class MyFragmentsAdapter extends FragmentStateAdapter {



    public MyFragmentsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch(position) {
            case 0:
                fragment = new fragment_homePage();
                break;
            case 1:
                fragment =new fragment_weights();
                break;
            case 2:
                fragment =new fragment_measures();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}