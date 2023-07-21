package com.example.tkshop.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tkshop.R;


public class ViewHolder_Product extends RecyclerView.ViewHolder {

    public ImageView imageProduct,delete_product,edit_product;
    public TextView nameProduct, priceProduct, inventoryProduct;
    public ConstraintLayout foreground;
    public ViewHolder_Product(@NonNull View itemView) {
        super(itemView);
        imageProduct = itemView.findViewById(R.id.image_customer);
        nameProduct = itemView.findViewById(R.id.name_customer);
        priceProduct = itemView.findViewById(R.id.address_Customer);
        inventoryProduct = itemView.findViewById(R.id.phone_customer);
        foreground = itemView.findViewById(R.id.foregroound_layout);
        delete_product = itemView.findViewById(R.id.delete_customer);
        edit_product = itemView.findViewById(R.id.edit_customer);
    }
}
