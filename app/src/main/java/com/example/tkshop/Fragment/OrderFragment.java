package com.example.tkshop.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tkshop.Adapter.Order_View_Pager;
import com.example.tkshop.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class OrderFragment extends Fragment {

    ViewPager2 view_pager_order_admin;
    TabLayout tabLayout;
    Order_View_Pager orderViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order, container, false);
        tabLayout = root.findViewById(R.id.tab_order);
        view_pager_order_admin = root.findViewById(R.id.view_pager_order_admin);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        orderViewPager = new Order_View_Pager(requireActivity());
        view_pager_order_admin.setAdapter(orderViewPager);

        new TabLayoutMediator(tabLayout, view_pager_order_admin, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("TẤT CẢ");
                    break;
                case 1:
                    tab.setText("CHỜ XÁC NHẬN");
                    break;
                case 2:
                    tab.setText("XÁC NHẬN");
                    break;
                case 3:
                    tab.setText("ĐÃ HỦY");
                    break;
                case 4:
                    tab.setText("ĐANG GIAO");
                    break;
                case 5:
                    tab.setText("THÀNH CÔNG");
                    break;
            }
        }).attach();

        view_pager_order_admin.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager_order_admin.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }

}