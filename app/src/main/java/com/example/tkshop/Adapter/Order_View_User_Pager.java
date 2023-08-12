package com.example.tkshop.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tkshop.Fragment.All_Order_User_Fragment;
import com.example.tkshop.Fragment.Canceled_Oder_User_Fragment;
import com.example.tkshop.Fragment.Confirm_Oder_User_Fragment;
import com.example.tkshop.Fragment.Delivery_Order_User_Fragment;
import com.example.tkshop.Fragment.Success_Order_User_Fragment;
import com.example.tkshop.Fragment.WaitConfirmOrderUser_Fragment;


public class Order_View_User_Pager extends FragmentStateAdapter {


    public Order_View_User_Pager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new WaitConfirmOrderUser_Fragment();
            case 2:
                return new Confirm_Oder_User_Fragment();
            case 3:
                return new Canceled_Oder_User_Fragment();
            case 4:
                return new Delivery_Order_User_Fragment();
            case 5:
                return new Success_Order_User_Fragment();
            default:
                return new All_Order_User_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
