package com.example.tkshop.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tkshop.Adapter.InfoProductOrderAdapter;
import com.example.tkshop.Common;
import com.example.tkshop.Model.Order;
import com.example.tkshop.Model.Request;
import com.example.tkshop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView name_user_order, phone_user_order, address_user_order, total_item, delivery_fee, total, quantity_checkout, total_checkout, changeinfo;
    RecyclerView recycler_product_order;
    Button btnOrder;
    EditText edt_message;
    int delivery = 30000;
    int sum = 0;
    Request newRequest;
    List<Order> listOrder;
    InfoProductOrderAdapter adapter;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    DatabaseReference reference, cart_user,products;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        reference = FirebaseDatabase.getInstance().getReference("Request");
        cart_user = FirebaseDatabase.getInstance().getReference("Cart");
//        products=FirebaseDatabase.getInstance().getReference("Product");
        toolbar = findViewById(R.id.toolbar_checkout);
        edt_message = findViewById(R.id.edt_message);
        name_user_order = findViewById(R.id.name_user_order);
        phone_user_order = findViewById(R.id.phone_user_order);
        address_user_order = findViewById(R.id.address_user_order);
        quantity_checkout = findViewById(R.id.quantity_checkout);
        total_checkout = findViewById(R.id.total_checkout);
        changeinfo = findViewById(R.id.changeinfo);
        btnOrder = findViewById(R.id.btnOrder);
        name_user_order.setText(Common.currentUser.getName());
        phone_user_order.setText(Common.currentUser.getPhone());
        address_user_order.setText(Common.currentUser.getAddress());

        total_item = findViewById(R.id.total_item);
        delivery_fee = findViewById(R.id.delivery_fee);
        total = findViewById(R.id.total);
        Intent i = getIntent();
        Bundle bundle_buy_again = i.getBundleExtra("buy_request_again");
        if (bundle_buy_again == null) {
            sum = CartActivity.sum;
            listOrder = CartActivity.userOrders;
            quantity_checkout.setText("(" + CartActivity.count + " sản phẩm )");
            total_item.setText(decimalFormat.format(sum) + " đ");
            sum += delivery;

        } else {
            newRequest = (Request) bundle_buy_again.getSerializable("request_again");
            listOrder = newRequest.getList();
            sum = newRequest.getTotal();
            total_item.setText(decimalFormat.format(sum - delivery) + " đ");
            quantity_checkout.setText("(" + bundle_buy_again.getInt("quantity_again") + " sản phẩm");
        }
        total.setText(decimalFormat.format(sum) + " đ");
        total_checkout.setText(decimalFormat.format(sum) + " đ");

        adapter = new InfoProductOrderAdapter(this, listOrder);
        recycler_product_order = findViewById(R.id.recycler_product_order);
        recycler_product_order.setHasFixedSize(true);
        recycler_product_order.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recycler_product_order.setAdapter(adapter);
        delivery_fee.setText(decimalFormat.format(delivery) + " đ");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Pay");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        changeinfo.setOnClickListener(v -> {
            Dialog dialog = new Dialog(CheckOutActivity.this);
            dialog.setContentView(R.layout.dialog_info);
            dialog.show();
            EditText address = dialog.findViewById(R.id.address);
            Button cancel = dialog.findViewById(R.id.btnCancel);
            Button change = dialog.findViewById(R.id.btnChange);

            cancel.setOnClickListener(v1 -> dialog.dismiss());
            change.setOnClickListener(v12 -> {
                address_user_order.setText(address.getText().toString());
                dialog.dismiss();
            });
        });

        btnOrder.setOnClickListener(v -> {
            if (address_user_order.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập địa chỉ người nhận", Toast.LENGTH_SHORT).show();
            } else {
                Calendar calendar = Calendar.getInstance();
                if (bundle_buy_again == null) {
                    Request request = new Request(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getName(),
                            address_user_order.getText().toString(),
                            sum,
                            simpleDateFormat.format(calendar.getTime()),
                            listOrder,
                            edt_message.getText().toString());
                    reference.child("FS" + Common.currentUser.getPhone() + System.currentTimeMillis()).setValue(request)
                            .addOnCompleteListener(task -> {
                                Toast.makeText(CheckOutActivity.this, "Bạn đã đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                MainActivity_User.userOrders.clear();
                                cart_user.child(Common.currentUser.getPhone()).removeValue((error, ref) -> {
                                    startActivity(new Intent(CheckOutActivity.this, OrderActivity.class));
                                    finish();
                                });
                            });
                } else {
                    Request request = new Request(
                            newRequest.getPhone(),
                            newRequest.getName(),
                            address_user_order.getText().toString(),
                            newRequest.getTotal(),
                            simpleDateFormat.format(calendar.getTime()),
                            listOrder,
                            edt_message.getText().toString());
                    reference.child("FS" + newRequest.getPhone() + System.currentTimeMillis()).setValue(request)
                            .addOnCompleteListener(task -> {

                                Toast.makeText(CheckOutActivity.this, "Bạn đã đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CheckOutActivity.this, OrderActivity.class));
                                finish();

                            });
                }
                // Delete cart
                if (bundle_buy_again == null) {
                    MainActivity_User.userOrders.clear();
                    cart_user.child(Common.currentUser.getPhone()).removeValue((error, ref) -> {
                        startActivity(new Intent(CheckOutActivity.this, OrderActivity.class));
                        finish();
                    });
                }

            }
        });
    }
//    public void truDL(){
//        products.child("inventory");
//        products.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


}