package com.eating.restaurant.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eating.restaurant.Fragment.CompleteOrder_Fragment;
import com.eating.restaurant.Fragment.OpenOrder_Fragment;


public class OrderPagerAdapter extends FragmentStateAdapter {
    public OrderPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new OpenOrder_Fragment();
            case 1:
                return new CompleteOrder_Fragment();
            default:
                return new OpenOrder_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
