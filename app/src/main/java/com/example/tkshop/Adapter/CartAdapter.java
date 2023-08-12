package com.example.tkshop.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tkshop.Activity.MainActivity_User;
import com.example.tkshop.Common;
import com.example.tkshop.Model.Order;
import com.example.tkshop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<Order> listCart;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    public CartAdapter(Context context, List<Order> listCart) {
        this.context = context;
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(listCart.get(position).getProductImage()).into(holder.image_product);
        holder.name_product.setText(listCart.get(position).getProductName());
        holder.price_product.setText(decimalFormat.format(listCart.get(position).getPrice()) + "đ");
        holder.color_product.setText("Color: " + listCart.get(position).getColor());
        holder.size_product.setText("Size:" + listCart.get(position).getSize());
        holder.quantity_item_cart.setText(listCart.get(position).getQuantity() + "");

        holder.btnminus_item_cart.setOnClickListener(v -> minusCartItem(holder, listCart.get(position), holder.getAbsoluteAdapterPosition()));

        holder.btnplus_item_cart.setOnClickListener(v -> plusCartItem(holder, listCart.get(position), holder.getAbsoluteAdapterPosition()));

        holder.delete_item_cart.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn chắc chắn muốn xóa " + listCart.get(position).getProductName() + " khỏi giỏ hàng?")
                    .setNegativeButton("Không", (dialog12, which) -> dialog12.dismiss())
                    .setPositiveButton("Có", (dialog1, which) -> {
                        deleteFromFireBase(listCart.get(position));
                        dialog1.dismiss();
                    }).create();
            dialog.show();
        });
    }

    private void deleteFromFireBase(Order order) {
        DatabaseReference listOrderCart = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
        listCart.remove(order);
        listOrderCart.setValue(listCart);
        MainActivity_User.userOrders = listCart;
    }

    private void plusCartItem(ViewHolder holder, Order order, int position) {
        order.setQuantity(order.getQuantity() + 1);
        holder.quantity_item_cart.setText(order.getQuantity() + "");
        notifyDataSetChanged();
        updateFirebase(position, order);
    }

    private void minusCartItem(ViewHolder holder, Order order, int position) {
        if (order.getQuantity() > 1) {
            order.setQuantity(order.getQuantity() - 1);
            holder.quantity_item_cart.setText(order.getQuantity() + "");
            updateFirebase(position, order);
        }
    }

    public void updateFirebase(int position, Order order) {
        DatabaseReference listOrderCart = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone() + "/" + position);
        listOrderCart.setValue(order);
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_product, btnplus_item_cart, btnminus_item_cart, delete_item_cart;
        TextView name_product, size_product, color_product, price_product, quantity_item_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            btnplus_item_cart = itemView.findViewById(R.id.btnplus_item_cart);
            btnminus_item_cart = itemView.findViewById(R.id.btnminus_item_cart);
            name_product = itemView.findViewById(R.id.name_product);
            size_product = itemView.findViewById(R.id.size_product);
            color_product = itemView.findViewById(R.id.color_product);
            price_product = itemView.findViewById(R.id.price_product);
            quantity_item_cart = itemView.findViewById(R.id.quantity_item_cart);
            delete_item_cart = itemView.findViewById(R.id.delete_item_cart);
        }
    }
}
