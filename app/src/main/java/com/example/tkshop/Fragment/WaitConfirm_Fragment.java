package com.example.tkshop.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tkshop.Activity.OrderInformationActivity;
import com.example.tkshop.Common;
import com.example.tkshop.Model.Request;
import com.example.tkshop.R;
import com.example.tkshop.ViewHolder.ViewHolder_Order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class WaitConfirm_Fragment extends Fragment {

    RecyclerView recyclerView_wait_confirm;
    Query requestReference;
    DatabaseReference reference;
    FirebaseRecyclerOptions<Request> options;
    FirebaseRecyclerAdapter<Request, ViewHolder_Order> adapter;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_wait_confirm_, container, false);
        reference = FirebaseDatabase.getInstance().getReference("Request");
        requestReference = FirebaseDatabase.getInstance().getReference("Request").orderByChild("status").equalTo(0);
        recyclerView_wait_confirm = root.findViewById(R.id.recyclerView_wait_confirm);
        recyclerView_wait_confirm.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        manager.setStackFromEnd(true);
        recyclerView_wait_confirm.setLayoutManager(manager);

        options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(requestReference,Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, ViewHolder_Order>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Order holder, int position, @NonNull Request model) {
                holder.name_order.setText(adapter.getRef(position).getKey());
                holder.status_order.setText(Common.convertStatus(model.getStatus()));
                holder.size_product.setText("Size: " + model.getList().get(0).getSize());
                holder.color_product.setText("Color: " + model.getList().get(0).getColor());
                holder.name_product_in_order.setText(model.getList().get(0).getProductName());
                holder.price_product_order.setText(decimalFormat.format(model.getList().get(0).getPrice()) + "đ");
                Picasso.get().load(model.getList().get(0).getProductImage()).into(holder.image_product);
                if (model.getList().size() == 1) {
                    holder.quantity_order.setText("x" + model.getList().get(0).getQuantity());
                } else if (model.getList().size() > 1) {
                    int quantity_order = 0;
                    for (int i = 1; i < model.getList().size(); i++) {
                        quantity_order += model.getList().get(i).getQuantity();
                    }
                    holder.quantity_order.setText("và " + quantity_order + " sản phẩm khác");
                }

                holder.setItemClickListener((view, position1, isLongClick) -> {
                    Intent i = new Intent(getContext(), OrderInformationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("request_detail",adapter.getItem(holder.getAbsoluteAdapterPosition()));
                    bundle.putString("name_order",adapter.getRef(position1).getKey());
                    i.putExtra("bundle",bundle);
                    startActivity(i);
                });
            }

            @NonNull
            @Override
            public ViewHolder_Order onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order,parent,false);
                return new ViewHolder_Order(v);
            }
        };
        adapter.startListening();
        recyclerView_wait_confirm.setAdapter(adapter);
        return root;
    }

}