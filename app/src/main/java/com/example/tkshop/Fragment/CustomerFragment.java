package com.example.tkshop.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tkshop.Model.Users;
import com.example.tkshop.R;
import com.example.tkshop.ViewHolder.ViewHolder_Customer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class CustomerFragment extends Fragment {

    RecyclerView recyclerView_Customer;
    FirebaseRecyclerOptions<Users> options;
    FirebaseRecyclerAdapter<Users, ViewHolder_Customer> adapter;
    Query userReference;
    DatabaseReference userDatabase;
    FloatingActionButton fab_addCustomer;
    EditText edt_phone_customer, edt_Pass_customer, edt_name_customer, edt_address_customer;
    Button btn_add_customer;
    Users user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_customer, container, false);
        recyclerView_Customer = root.findViewById(R.id.recyclerView_Customer);
        fab_addCustomer = root.findViewById(R.id.fab_addCustomer);
        userDatabase = FirebaseDatabase.getInstance().getReference("User");
        fab_addCustomer.setOnClickListener(v -> showDialog(0));
        recyclerView_Customer.setHasFixedSize(true);
        recyclerView_Customer.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        userReference = FirebaseDatabase.getInstance().getReference("User").orderByChild("role").equalTo(1);

        options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(userReference, Users.class).build();
        adapter = new FirebaseRecyclerAdapter<Users, ViewHolder_Customer>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Customer holder, int position, @NonNull Users model) {
                holder.name_customer.setText(model.getName());
                holder.phone_customer.setText(adapter.getRef(position).getKey());
                if (model.getAvatar().equals("")) {
                    holder.image_customer.setImageResource(R.drawable.avatar);
                } else {
                    Picasso.get().load(model.getAvatar()).into(holder.image_customer);
                }
                holder.address_customer.setText(model.getAddress());

                holder.edit_customer.setOnClickListener(v -> {
                    user = adapter.getItem(position);
                    user.setPhone(adapter.getRef(position).getKey());
                    showDialog(1);
                });

                holder.delete_customer.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Bạn có chắc chắn xóa khách hàng này không?")
                            .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                            .setPositiveButton("Có", (dialog, which) -> {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
                                reference.child(adapter.getRef(holder.getAbsoluteAdapterPosition()).getKey())
                                        .removeValue((error, ref) -> Toast.makeText(getActivity(), "Xóa khách hàng thành công", Toast.LENGTH_SHORT).show());
                            }).create();
                    builder.show();
                });
            }

            @NonNull
            @Override
            public ViewHolder_Customer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_customer, parent, false);
                return new ViewHolder_Customer(v);
            }
        };
        adapter.startListening();
        recyclerView_Customer.setAdapter(adapter);
        return root;
    }

    private void showDialog(int type) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.add_customer_dialog, null);
        dialog.setView(view);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        edt_name_customer = view.findViewById(R.id.edt_name_customer);
        edt_phone_customer = view.findViewById(R.id.edt_phone_customer);
        edt_address_customer = view.findViewById(R.id.edt_address_customer);
        edt_Pass_customer = view.findViewById(R.id.edt_Pass_customer);
        btn_add_customer = view.findViewById(R.id.btn_add_customer);
        if (type == 0) {
            btn_add_customer.setText("Thêm");
        } else {
            edt_name_customer.setText(user.getName());
            edt_address_customer.setText(user.getAddress());
            edt_Pass_customer.setText(user.getPass());
            edt_Pass_customer.setVisibility(View.GONE);
            edt_phone_customer.setText(user.getPhone());
            edt_phone_customer.setVisibility(View.GONE);
            btn_add_customer.setText("Sửa");
        }

        btn_add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate() > 0) {
                    if (type == 0) {
                        String name = edt_name_customer.getText().toString();
                        String phone = edt_phone_customer.getText().toString();
                        String address = edt_address_customer.getText().toString();
                        String pass = edt_address_customer.getText().toString();

                        userDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(phone)) {
                                    Toast.makeText(getActivity(), "Số điện thoại này đã được đăng ký", Toast.LENGTH_SHORT).show();
                                } else {
                                    user = new Users(name, pass, address, 1, "");
                                    userDatabase.child(phone).setValue(user, (error, ref) -> Toast.makeText(getActivity(), "Thêm thông tin khách hàng thành công", Toast.LENGTH_SHORT).show());
                                    userDatabase.removeEventListener(this);
                                    alertDialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("address", edt_address_customer.getText().toString());
                        map.put("name", edt_name_customer.getText().toString());
                        userDatabase.child(user.getPhone()).updateChildren(map, (error, ref) -> {Toast.makeText(getActivity(), "Cập nhập thông tin khách thành công", Toast.LENGTH_SHORT).show();alertDialog.dismiss();});
                    }
                }
            }
        });
    }

    private int validate() {
        String regex = "0[0-9]{9}";
        String phone = edt_phone_customer.getText().toString();
        String pass = edt_Pass_customer.getText().toString();
        String name = edt_name_customer.getText().toString();
        String address = edt_address_customer.getText().toString();
        if (phone.trim().isEmpty()) {
            edt_phone_customer.setError("Vui lòng nhập số điện thoại");
            return -1;
        }

        if (!phone.matches(regex)) {
            edt_phone_customer.setError("Số điện thoại của bạn không hợp lệ");
        }

        if (pass.trim().isEmpty()) {
            edt_Pass_customer.setError("Vui lòng nhập mật khẩu");
            return -1;
        }

        if (name.trim().isEmpty()) {
            edt_name_customer.setError("Vui lòng nhập tên");
            return -1;
        }

        if (address.trim().isEmpty()) {
            edt_address_customer.setError("Vui lòng nhập địa chỉ");
            return -1;
        }

        return 1;
    }
}