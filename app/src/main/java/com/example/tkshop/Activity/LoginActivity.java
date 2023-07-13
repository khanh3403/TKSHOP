package com.example.tkshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Toast;

import com.example.tkshop.Adapter.LoginAdapter;
import com.example.tkshop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton fab_facebook, fab_google, fab_twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        fab_facebook = findViewById(R.id.fab_facebook);
        fab_google = findViewById(R.id.fab_google);
        fab_twitter = findViewById(R.id.fab_twitter);
        LoginAdapter adapter = new LoginAdapter(this);
        viewPager.setAdapter(adapter);
        animate();
        // giao dien fragment login,sign up
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("LOGIN");
                    break;
                case 1:
                    tab.setText("SIGNUP");
                    break;
            }
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab_google.setOnClickListener(v -> Toast.makeText(LoginActivity.this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show());

        fab_facebook.setOnClickListener(v -> Toast.makeText(LoginActivity.this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show());

        fab_twitter.setOnClickListener(v -> Toast.makeText(LoginActivity.this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show());

    }

    private void animate() {
        fab_facebook.setTranslationY(500);
        fab_twitter.setTranslationY(500);
        fab_google.setTranslationY(500);
        tabLayout.setTranslationY(500);

        float alpha = 0;
        fab_facebook.setAlpha(alpha);
        fab_twitter.setAlpha(alpha);
        fab_google.setAlpha(alpha);
        tabLayout.setAlpha(alpha);


        fab_facebook.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(1500).start();
        fab_twitter.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(2500).start();
        fab_google.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(2000).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(1000).start();
    }
}
