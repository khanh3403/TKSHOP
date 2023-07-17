package com.example.tkshop.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tkshop.Common;
import com.example.tkshop.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class InfomationFragment extends Fragment {
    TextView total_product, waitOrder;
    TextInputEditText edt_passnew;
    Button changePassword;
    DatabaseReference userReference,productReference,requestReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_infomation, container, false);
        userReference = FirebaseDatabase.getInstance().getReference("User").child(Common.currentUser.getPhone());
        productReference = FirebaseDatabase.getInstance().getReference("Product");
        requestReference = FirebaseDatabase.getInstance().getReference("Request");
        total_product = root.findViewById(R.id.total_product);
        waitOrder = root.findViewById(R.id.waitOrder);
        edt_passnew = root.findViewById(R.id.edt_passnew);
        changePassword = root.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(v -> {
            if (edt_passnew.getText().toString().trim().equals("")) {
                edt_passnew.setError("Vui lòng nhập mật khẩu mới");
            } else {
                Common.currentUser.setPass(edt_passnew.getText().toString());
                Map<String, Object> map = new HashMap<>();
                map.put("pass", edt_passnew.getText().toString());
                userReference.updateChildren(map, (error, ref) -> Toast.makeText(getActivity(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show());
            }
        });

        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total_product.setText("Hiện có " + snapshot.getChildrenCount() + " sản phẩm");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        requestReference.orderByChild("status").equalTo(0).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                waitOrder.setText(snapshot.getChildrenCount()+ "đơn hàng chờ xử lý");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return root;
    }
}