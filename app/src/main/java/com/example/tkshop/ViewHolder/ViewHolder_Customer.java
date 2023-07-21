package com.example.tkshop.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tkshop.R;


public class ViewHolder_Customer extends RecyclerView.ViewHolder {
    public TextView name_customer,address_customer,phone_customer;
    public ImageView image_customer,edit_customer,delete_customer;
    public ViewHolder_Customer(@NonNull View itemView) {
        super(itemView);
        name_customer = itemView.findViewById(R.id.name_customer);
        address_customer = itemView.findViewById(R.id.address_Customer);
        phone_customer = itemView.findViewById(R.id.phone_customer);
        image_customer = itemView.findViewById(R.id.image_customer);
        edit_customer = itemView.findViewById(R.id.edit_customer);
        delete_customer = itemView.findViewById(R.id.delete_customer);
    }
}
