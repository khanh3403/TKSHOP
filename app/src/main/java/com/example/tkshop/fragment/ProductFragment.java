package com.example.tkshop.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tkshop.Model.Categories;
import com.example.tkshop.Model.Products;
import com.example.tkshop.R;
import com.example.tkshop.ViewHolder.ViewHolder_Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {
    Categories categories;
    StorageReference storageReference;
    Uri selectedImageUri = null;
    ImageView image_choose_product2;
    RecyclerView recyclerView_product;
    FirebaseRecyclerOptions<Products> optionsProduct;
    FirebaseRecyclerAdapter<Products, ViewHolder_Product> productAdapter;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    DatabaseReference productReference, categoryReference;
    FloatingActionButton fab_add_product;
    TextInputEditText edt_nameProduct, edt_inventoryProduct, edt_priceProduct;
    Products products;
    List<Categories> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_product, container, false);
        productReference = FirebaseDatabase.getInstance().getReference("Product");
        categoryReference = FirebaseDatabase.getInstance().getReference("Category");
        recyclerView_product = root.findViewById(R.id.recyclerView_product);
        recyclerView_product.setHasFixedSize(true);
        recyclerView_product.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        fab_add_product = root.findViewById(R.id.fab_add_product);

        fab_add_product.setOnClickListener(v -> showDialog(0));

        optionsProduct = new FirebaseRecyclerOptions.Builder<Products>().setQuery(productReference, Products.class).build();
        productAdapter = new FirebaseRecyclerAdapter<Products, ViewHolder_Product>(optionsProduct) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Product holder, int position, @NonNull Products model) {
                holder.nameProduct.setText(model.getName());
                holder.priceProduct.setText("Giá: " + decimalFormat.format(model.getPrice()) + "đ");
                Picasso.get().load(model.getImage()).into(holder.imageProduct);
                holder.inventoryProduct.setText("Số lượng: "+model.getInventory());
                holder.edit_product.setOnClickListener(v -> {
                    products = productAdapter.getItem(position);
                    products.setId(productAdapter.getRef(position).getKey());
                    selectedImageUri = Uri.parse(products.getImage());
                    showDialog(1);
                });

                holder.delete_product.setOnClickListener(v -> {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setMessage("Bạn có chắc chắn xóa sản phẩm này không?")
                            .setNegativeButton("Không", (dialog12, which) -> dialog12.dismiss())
                            .setPositiveButton("Có", (dialog1, which) ->
                                    productReference.child(productAdapter.getRef(holder.getAbsoluteAdapterPosition()).getKey())
                                            .removeValue((error, ref) -> {
                                                Toast.makeText(getActivity(), "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                                dialog1.dismiss();
                                            })).create();
                    dialog.show();
                });
            }

            @NonNull
            @Override
            public ViewHolder_Product onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_product, parent, false);
                return new ViewHolder_Product(v);
            }
        };
        productAdapter.startListening();
        recyclerView_product.setAdapter(productAdapter);
        getCategoriesList();
        return root;
    }

    private void showDialog(int type) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.activity_add_product,null);
        dialog.setView(view);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        ImageView image_choose_product = view.findViewById(R.id.image_choose_product);
        image_choose_product2 = view.findViewById(R.id.image_choose_product2);
        Spinner spinner_category = view.findViewById(R.id.spinner_category);
        edt_nameProduct = view.findViewById(R.id.edt_nameProduct);
        edt_inventoryProduct = view.findViewById(R.id.edt_inventoryProduct);
        edt_priceProduct = view.findViewById(R.id.edt_priceProduct);
        Button btnAddProduct = view.findViewById(R.id.btnAddProduct);
        ArrayAdapter<Categories> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter1);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categories = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        image_choose_product.setOnClickListener(v1 -> imageChooser());

        if (type == 0) {
            btnAddProduct.setText("THÊM SẢN PHẨM");
        } else {
            Picasso.get().load(products.getImage()).into(image_choose_product2);
            edt_nameProduct.setText(products.getName());
            edt_inventoryProduct.setText(products.getInventory() + "");
            edt_priceProduct.setText(products.getPrice() + "");
            int index = -1;
            for (Categories categories : list) {
                if (categories.getId().equals(products.getId_Category())) {
                    index = list.indexOf(categories);
                    break;
                }
            }
            spinner_category.setSelection(index);
            btnAddProduct.setText("SỬA SẢN PHẨM");
        }

        btnAddProduct.setOnClickListener(v -> {
            if (validate() > 0) {
                String name = edt_nameProduct.getText().toString();
                int price = Integer.parseInt(edt_priceProduct.getText().toString());
                int inventory = Integer.parseInt(edt_inventoryProduct.getText().toString());

                if (type == 0) {
                    if (!(name.isEmpty() && selectedImageUri != null)) {
                        storageReference = FirebaseStorage.getInstance().getReference().child("image_product").child(System.currentTimeMillis() + selectedImageUri.getLastPathSegment());
                        storageReference.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {

                            products = new Products(name, inventory, price, task.getResult().toString(), categories.getId());
                            String key = productReference.push().getKey();
                            productReference.child(key).setValue(products)
                                    .addOnCompleteListener(task1 -> Toast.makeText(getActivity(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show())
                                    .addOnCanceledListener(() -> Toast.makeText(getActivity(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show());
                        }));
                        alertDialog.dismiss();
                    }
                } else {
                    if (!selectedImageUri.toString().contains("https")) {
                        storageReference = FirebaseStorage.getInstance().getReference().child("image_product").child(selectedImageUri.getLastPathSegment());
                        storageReference.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                            products.setName(name);
                            products.setPrice(price);
                            products.setInventory(inventory);
                            products.setImage(task.getResult().toString());
                            products.setId_Category(list.get(spinner_category.getSelectedItemPosition()).getId());
                            productReference.child(products.getId()).setValue(products)
                                    .addOnCompleteListener(task1 -> Toast.makeText(getActivity(), "Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show())
                                    .addOnCanceledListener(() -> Toast.makeText(getActivity(), "Sửa sản phẩm thất bại", Toast.LENGTH_SHORT).show());
                            alertDialog.dismiss();
                        }));
                    } else {
                        products.setName(name);
                        products.setPrice(price);
                        products.setInventory(inventory);
                        products.setImage(selectedImageUri.toString());
                        products.setId_Category(list.get(spinner_category.getSelectedItemPosition()).getId());
                        productReference.child(products.getId()).setValue(products)
                                .addOnCompleteListener(task1 -> Toast.makeText(getActivity(), "Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show())
                                .addOnCanceledListener(() -> Toast.makeText(getActivity(), "Sửa sản phẩm thất bại", Toast.LENGTH_SHORT).show());
                        alertDialog.dismiss();
                    }
                }
            }
        });

    }

    public void getCategoriesList() {
        categoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Categories categories = dataSnapshot.getValue(Categories.class);
                    categories.setId(dataSnapshot.getKey());
                    list.add(categories);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int validate() {
        if (edt_nameProduct.getText().toString().trim().isEmpty() ||
                edt_priceProduct.getText().toString().trim().isEmpty() ||
                edt_inventoryProduct.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đủ trường dữ liệu", Toast.LENGTH_SHORT).show();
            return -1;
        }

        try {
            Integer.parseInt(edt_priceProduct.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Giá phải nhập số", Toast.LENGTH_SHORT).show();
            return -1;
        }

        try {
            Integer.parseInt(edt_inventoryProduct.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Số lượng phải là số nguyên", Toast.LENGTH_SHORT).show();
            return -1;
        }

        if (selectedImageUri == null){
            Toast.makeText(getActivity(), "Vui lòng chọn ảnh sản phẩm", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return 1;
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode()
                == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                Picasso.get().load(selectedImageUri).into(image_choose_product2);
            }
        }
    });
}