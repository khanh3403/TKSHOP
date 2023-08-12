package com.example.tkshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.tkshop.Common;
import com.example.tkshop.Model.Categories;
import com.example.tkshop.Model.Order;
import com.example.tkshop.Model.Products;
import com.example.tkshop.R;
import com.example.tkshop.ViewHolder.ViewHolder_Category;
import com.example.tkshop.ViewHolder.ViewHolder_Product_User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity_User extends AppCompatActivity {

    TextView cart_count,name;
    public static List<Order> userOrders;
    boolean check = true;
    ImageView profile_icon, icon_cart, icon_order;
    ViewFlipper viewFlipper;
    int row_index = -1;
    int old_index = -2;
    RecyclerView recyclerview_category_user, recyclerview_product_user;
    FirebaseRecyclerOptions<Categories> options;
    DatabaseReference databaseReference;

    Query productReference;
    FirebaseRecyclerOptions<Products> optionsProduct;
    FirebaseRecyclerAdapter<Products, ViewHolder_Product_User> productAdapter;

    FirebaseRecyclerAdapter<Categories, ViewHolder_Category> categoriesAdapter;
    List<ConstraintLayout> constraintLayoutList = new ArrayList<>();
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        databaseReference = FirebaseDatabase.getInstance().getReference("Category");
        productReference = FirebaseDatabase.getInstance().getReference("Product");
        profile_icon = findViewById(R.id.profile_icon);
        icon_cart = findViewById(R.id.icon_cart);
        icon_order = findViewById(R.id.icon_order);
        cart_count = findViewById(R.id.cartCount);
        viewFlipper=findViewById(R.id.idViewlipper);
        name = findViewById(R.id.name);
        name.setText(Common.currentUser.getName());
        recyclerview_category_user = findViewById(R.id.recyclerview_category_user);
        recyclerview_product_user = findViewById(R.id.recyclerview_product_user);
        recyclerview_category_user.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerview_product_user.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview_product_user.setLayoutManager(new GridLayoutManager(this,2));

        loadData();
        userOrders = new ArrayList<>();
        icon_cart.setOnClickListener(v -> startActivity(new Intent(MainActivity_User.this, CartActivity.class)));
        icon_order.setOnClickListener(v -> startActivity(new Intent(MainActivity_User.this,OrderActivity.class)));
        profile_icon.setOnClickListener(v -> startActivity(new Intent(MainActivity_User.this,UserActivity.class)));
        getListUserOrder();
        actionViewFlipper();
    }

    private void actionViewFlipper() {
        List<String> quangcao=new ArrayList<>();
        quangcao.add("https://dojeannam.com/wp-content/uploads/2018/02/BANNER-SALE-OFF.jpg");
        quangcao.add("https://intphcm.com/data/upload/banner-thoi-trang-nam.jpg");
        quangcao.add("https://dojeannam.com/wp-content/uploads/2017/09/BANNER-KHAI-TRUONG-DOJEANNAM.jpg");
        quangcao.add("https://tmluxury.vn/wp-content/uploads/banner-do-shop-thoi-trang-nam-tphcm-tmluxury.jpg");
        quangcao.add("https://salt.tikicdn.com/ts/brickv2og/00/50/1b/3cde53d8619069b98772edffe8c5dad1.jpg");
        for(int i=0;i<quangcao.size();i++){
            ImageView imageView=new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(quangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }

    public void updateRecyclerViewProduct(Query reference) {
        optionsProduct = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference, Products.class).build();
        productAdapter = new FirebaseRecyclerAdapter<Products, ViewHolder_Product_User>(optionsProduct) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Product_User holder, int position, @NonNull Products model) {
                holder.nameProduct.setText(model.getName());
                holder.priceProduct.setText(decimalFormat.format(model.getPrice()) + "đ");
                Picasso.get().load(model.getImage()).into(holder.imageProduct);

                holder.layout_item_product.setOnClickListener(v -> {
                    Intent i = new Intent(MainActivity_User.this, ProductDetailActivityUser.class);
                    model.setId(productAdapter.getRef(position).getKey());
                    i.putExtra("object", model);
                    startActivity(i);
                });
            }

            @NonNull
            @Override
            public ViewHolder_Product_User onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_product_user, parent, false);
                return new ViewHolder_Product_User(v);
            }
        };
        productAdapter.startListening();
        recyclerview_product_user.setAdapter(productAdapter);
    }


    public void loadData() {
        options = new FirebaseRecyclerOptions.Builder<Categories>().setQuery(databaseReference, Categories.class).build();
        categoriesAdapter = new FirebaseRecyclerAdapter<Categories, ViewHolder_Category>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Category holder, int position, @NonNull Categories model) {
                if (!constraintLayoutList.contains(holder.foreground)) {
                    constraintLayoutList.add(holder.foreground);
                }
                holder.name_Category.setText(model.getName());
                Picasso.get().load(Uri.parse(model.getImage())).into(holder.image_Category);
                if (check) {
                    updateRecyclerViewProduct(productReference);
                    check = false;
                }

                holder.layout_item_category.setOnClickListener(v -> {
                    old_index = row_index;
                    row_index = holder.getLayoutPosition();
                    for (ConstraintLayout c : constraintLayoutList) {
                        c.setBackgroundResource(R.drawable.category_background);
                    }
                    if (old_index == row_index) {
                        holder.foreground.setBackgroundResource(R.drawable.category_background);
                        old_index = -2;
                        row_index = -1;
                        productReference = FirebaseDatabase.getInstance().getReference("Product");
                    } else {
                        holder.foreground.setBackgroundResource(R.drawable.category_selected_bg);
                        productReference = FirebaseDatabase.getInstance().getReference("Product").orderByChild("id_Category").equalTo(categoriesAdapter.getRef(position).getKey());
                    }
                    updateRecyclerViewProduct(productReference);
                });
            }

            @NonNull
            @Override
            public ViewHolder_Category onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
                return new ViewHolder_Category(v);
            }
        };
        categoriesAdapter.startListening();
        recyclerview_category_user.setAdapter(categoriesAdapter);
    }


    public void getListUserOrder() {
        DatabaseReference listOrderCart = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
        listOrderCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userOrders.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Order order = dataSnapshot.getValue(Order.class);
                        userOrders.add(order);
                    }
                    cart_count.setVisibility(View.VISIBLE);
                    cart_count.setText(String.valueOf(userOrders.size()));
                }else {
                    cart_count.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}