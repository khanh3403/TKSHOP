package com.example.tkshop.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tkshop.Interface.ItemClickListener;
import com.example.tkshop.R;


public class ViewHolder_Order_User extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView status_order,size_product,color_product,price_product_order,quantity_order,name_product_in_order,total_order,total_product_order_user;
    public Button btn_Buy_Again;
    public ImageView image_product;
    public Button btnCancel_Order;
    ItemClickListener itemClickListener;

    public ViewHolder_Order_User(@NonNull View itemView) {
        super(itemView);

        status_order = itemView.findViewById(R.id.status_order);
        size_product = itemView.findViewById(R.id.size_product);
        color_product = itemView.findViewById(R.id.color_product);
        price_product_order = itemView.findViewById(R.id.price_product_order);
        quantity_order = itemView.findViewById(R.id.quantity_order);
        name_product_in_order = itemView.findViewById(R.id.name_product_in_order);
        image_product = itemView.findViewById(R.id.image_product);
        btn_Buy_Again = itemView.findViewById(R.id.btnBuyAgain);
        total_order = itemView.findViewById(R.id.total_order);
        total_product_order_user = itemView.findViewById(R.id.total_product_order_user);
        btnCancel_Order = itemView.findViewById(R.id.btnCancel_Order);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAbsoluteAdapterPosition(),false);
    }
}
