package com.example.tkshop;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1234
        CheckBranch checkBranch = new CheckBranch(1, 2);
        Log.d(TAG, "onCreate: ket qua la: " + checkBranch);
    }
}