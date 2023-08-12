package com.example.tkshop.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.tkshop.Adapter.Order_View_User_Pager;
import com.example.tkshop.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager2 view_pager_order_user;
    TabLayout tabLayout;
    Order_View_User_Pager order_view_user_pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        toolbar = findViewById(R.id.toolbar_order_user);
        tabLayout = findViewById(R.id.tab_order_user);
        view_pager_order_user = findViewById(R.id.view_pager_order_user);
        order_view_user_pager = new Order_View_User_Pager(this);
        view_pager_order_user.setAdapter(order_view_user_pager);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Order");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());


        new TabLayoutMediator(tabLayout, view_pager_order_user, (tab, position) -> {
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

        view_pager_order_user.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager_order_user.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}