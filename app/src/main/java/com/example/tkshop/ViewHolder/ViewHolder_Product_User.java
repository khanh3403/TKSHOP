package com.example.tkshop.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tkshop.R;


public class ViewHolder_Product_User extends RecyclerView.ViewHolder {

    public ImageView imageProduct;
    public TextView nameProduct, priceProduct;
    public ConstraintLayout layout_item_product;
    public RatingBar rateProduct;
    public ViewHolder_Product_User(@NonNull View itemView) {
        super(itemView);
        imageProduct = itemView.findViewById(R.id.image_customer);
        nameProduct = itemView.findViewById(R.id.name_customer);
        priceProduct = itemView.findViewById(R.id.address_Customer);
        rateProduct = itemView.findViewById(R.id.rateProduct);
        layout_item_product = itemView.findViewById(R.id.layout_item_product);
    }
}
