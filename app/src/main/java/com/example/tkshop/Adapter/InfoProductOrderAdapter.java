package com.example.tkshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tkshop.Model.Order;
import com.example.tkshop.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class InfoProductOrderAdapter extends RecyclerView.Adapter<InfoProductOrderAdapter.ViewHolder> {
    Context context;
    List<Order> list;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");


    public InfoProductOrderAdapter(Context context, List<Order> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_product_info_order,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getProductImage()).into(holder.image_product);
        holder.name_product.setText(list.get(position).getProductName());
        holder.size_product.setText("Size: "+list.get(position).getSize());
        holder.color_product.setText("Color: "+list.get(position).getColor());
        holder.price_product.setText(decimalFormat.format(list.get(position).getPrice())+"đ");
        holder.quantity_product.setText("x"+list.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_product;
        TextView name_product, size_product, color_product, price_product, quantity_product;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            size_product = itemView.findViewById(R.id.size_product);
            color_product = itemView.findViewById(R.id.color_product);
            price_product = itemView.findViewById(R.id.price_product);
            quantity_product = itemView.findViewById(R.id.quantity_product);
        }
    }
}
