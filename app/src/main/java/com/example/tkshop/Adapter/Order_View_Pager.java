package com.example.tkshop.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tkshop.fragment.AllOrder_Fragment;
import com.example.tkshop.fragment.Canceled_Order_Fragment;
import com.example.tkshop.fragment.Confirm_Fragment;
import com.example.tkshop.fragment.Delivery_Order_Fragment;
import com.example.tkshop.fragment.Success_Order_Fragment;
import com.example.tkshop.fragment.WaitConfirm_Fragment;


public class Order_View_Pager extends FragmentStateAdapter {


    public Order_View_Pager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new WaitConfirm_Fragment();
            case 2:
                return new Confirm_Fragment();
            case 3:
                return new Canceled_Order_Fragment();
            case 4:
                return new Delivery_Order_Fragment();
            case 5:
                return new Success_Order_Fragment();
            default:
                return new AllOrder_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
