package com.example.tkshop.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tkshop.Activity.MainActivity_User;
import com.example.tkshop.Activity.NavigationDrawer;
import com.example.tkshop.Common;
import com.example.tkshop.Model.Users;
import com.example.tkshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginTabFragment extends Fragment {

    private EditText edt_phone, edt_pass;
    private CheckBox chk_remember;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login_tab, container, false);
        edt_phone = root.findViewById(R.id.edt_phone);
        edt_pass = root.findViewById(R.id.edt_Pass);
        Button btn_login = root.findViewById(R.id.btn_login);
        chk_remember = root.findViewById(R.id.chk_remember);
        SharedPreferences spf = getActivity().getSharedPreferences("USERS_FILE", Context.MODE_PRIVATE);
        String phone = spf.getString("PHONE", "");
        String pass = spf.getString("PASSWORD", "");
        boolean remember = spf.getBoolean("REMEMBER", false);
        edt_phone.setText(phone);
        edt_pass.setText(pass);
        chk_remember.setChecked(remember);

        btn_login.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Vui lòng chờ....");
            progressDialog.show();
            if (validate() > 0) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(edt_phone.getText().toString())){
                            Users user =  snapshot.child(edt_phone.getText().toString()).getValue(Users.class);
                            user.setPhone(edt_phone.getText().toString());
                            if (user.getPass().equals(edt_pass.getText().toString())){
                                if (user.getRole() == 0) {
                                    Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    startActivity(new Intent(getActivity(), NavigationDrawer.class));

                                }else {
                                    Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    startActivity(new Intent(getActivity(), MainActivity_User.class));
                                }
                                Common.currentUser = user;
                                rememberUser(edt_phone.getText().toString(),edt_pass.getText().toString(),chk_remember.isChecked());
                                getActivity().finish();
                                databaseReference.removeEventListener(this);

                            }else {
                                Toast.makeText(getActivity(), "Mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Số điện thoại chưa được đăng ký", Toast.LENGTH_SHORT).show();
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

    private void rememberUser(String username, String pass, boolean status) {
        SharedPreferences spf = getActivity().getSharedPreferences("USERS_FILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        if (!status) {
            edit.clear();
            edit.putString("PHONE", username);
        } else {
            edit.putString("PHONE", username);
            edit.putString("PASSWORD", pass);
        }
        edit.putBoolean("REMEMBER", status);
        edit.apply();
    }

    private int validate() {
        String regex = "0[0-9]{9}";
        String phone = edt_phone.getText().toString();
        String pass = edt_pass.getText().toString();
        if (phone.trim().isEmpty()) {
            edt_phone.setError("Vui lòng nhập số điện thoại");
            return -1;
        }

        if (!phone.matches(regex)){
            edt_phone.setError("Số điện thoại của bạn không hợp lệ");
        }

        if (pass.trim().isEmpty()) {
            edt_pass.setError("Vui lòng nhập mật khẩu");
            return -1;
        }

        return 1;
    }
}
