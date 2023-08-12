package com.example.tkshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tkshop.Adapter.CartAdapter;
import com.example.tkshop.Common;
import com.example.tkshop.Model.Order;
import com.example.tkshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView emptyTxt, quantity_checkout, total_checkout;
    RecyclerView recycler_cart;
    Button btnOrder;
    LinearLayout linearLayout;
    public static List<Order> userOrders;
    CartAdapter cartAdapter;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    public static int sum;
    public static int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        linearLayout = findViewById(R.id.linearLayout);
        toolbar = findViewById(R.id.toolbar_cart);
        emptyTxt = findViewById(R.id.emptyTxt);
        quantity_checkout = findViewById(R.id.quantity_checkout);
        total_checkout = findViewById(R.id.total_checkout);
        btnOrder = findViewById(R.id.btnOrder);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Cart");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        userOrders = new ArrayList<>();
        recycler_cart = findViewById(R.id.recycler_cart);
        recycler_cart.setHasFixedSize(true);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getListCart();

        btnOrder.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, CheckOutActivity.class)));
    }

    public void getListCart() {
        DatabaseReference listOrderCart = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
        listOrderCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userOrders.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    userOrders.add(order);
                }
                cartAdapter = new CartAdapter(CartActivity.this, userOrders);
                recycler_cart.setAdapter(cartAdapter);
                if (userOrders.size() == 0) {
                    emptyTxt.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                } else {
                    emptyTxt.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    getTotalCart();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getTotalCart(){
        sum = 0;
        count = 0;
        for (Order order:userOrders){
            sum += order.getQuantity() * order.getPrice();
            count+=order.getQuantity();
        }
        quantity_checkout.setText("( "+count+" sản phẩm)");
        total_checkout.setText(decimalFormat.format(sum)+"đ");
    }


}