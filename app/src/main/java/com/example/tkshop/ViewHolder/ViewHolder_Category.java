package com.example.tkshop.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tkshop.R;


public class ViewHolder_Category extends RecyclerView.ViewHolder{
    public ConstraintLayout layout_item_category;
    public ConstraintLayout layout_delete, layout_update;
    public ConstraintLayout foreground;
    public ImageView image_Category;
    public TextView name_Category;
    public ViewHolder_Category(@NonNull View itemView) {
        super(itemView);
        layout_item_category = itemView.findViewById(R.id.layout_item_category);
        image_Category = itemView.findViewById(R.id.categoryImage);
        name_Category = itemView.findViewById(R.id.categoryName);
        layout_update = itemView.findViewById(R.id.update_category);
        layout_delete = itemView.findViewById(R.id.delete_category);
        foreground = itemView.findViewById(R.id.foregroound_layout);
    }

}
