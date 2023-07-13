package com.example.tkshop.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tkshop.Model.Users;
import com.example.tkshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpTabFragment extends Fragment {

    DatabaseReference table_user = FirebaseDatabase.getInstance().getReference("User");
    private EditText edt_phone, edt_pass, edt_name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_up_tab, container, false);
        edt_phone = root.findViewById(R.id.edt_phone_customer);
        edt_pass = root.findViewById(R.id.edt_Pass_customer);
        edt_name = root.findViewById(R.id.edt_name_customer);
        Button btn_signup = root.findViewById(R.id.btn_add_customer);
        btn_signup.setOnClickListener(v -> {
            if (validate() > 0){
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Vui lòng chờ...");
                progressDialog.show();
                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Đã tồn tại tài khoản
                        if (snapshot.hasChild(edt_phone.getText().toString())){
                            Toast.makeText(getActivity(), "Số điện thoại này đã được đăng ký", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else { // Chưa tồn tại tài khoản
                            Users user = new Users(edt_name.getText().toString(),edt_pass.getText().toString(),"",1,"");
                            table_user.child(edt_phone.getText().toString()).setValue(user);
                            Toast.makeText(getActivity(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return root;
    }

    private int validate() {
        String regex = "0[0-9]{9}";
        String phone = edt_phone.getText().toString();
        String pass = edt_pass.getText().toString();
        String name = edt_name.getText().toString();

        if (phone.trim().isEmpty()) {
            edt_phone.setError("Vui lòng nhập số điện thoại");
            return -1;
        }

        if (!phone.matches(regex)){
            edt_phone.setError("Số điện thoại của bạn không hợp lệ");
            return -1;
        }

        if (pass.trim().isEmpty()) {
            edt_pass.setError("Vui lòng nhập mật khẩu");
            return -1;
        }

        if (name.trim().isEmpty()) {
            edt_name.setError("Vui lòng nhập tên");
            return -1;
        }

        return 1;
    }

}
