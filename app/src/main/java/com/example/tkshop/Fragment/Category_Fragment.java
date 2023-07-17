package com.example.tkshop.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tkshop.Model.Categories;
import com.example.tkshop.R;
import com.example.tkshop.ViewHolder.ViewHolder_Category_admin;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Category_Fragment extends Fragment {
    private TextInputEditText edt_name_category_admin;
    private ConstraintLayout container_add_category;
    private TextView tv_title_product_admin;
    ImageView image_category_choose, img_product_add_avatar, img_close_add_update_category;
    RecyclerView recyclerView_category;
    FloatingActionButton fab;
    Uri selectedImageUri = null;
    DatabaseReference categoryReference;
    StorageReference storageReference;
    Button btnAddCategory;
    Categories categories;
    String key_category="";
    FirebaseRecyclerOptions<Categories> optionsCategory;
    FirebaseRecyclerAdapter<Categories, ViewHolder_Category_admin> categoriesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category_, container, false);
        categoryReference = FirebaseDatabase.getInstance().getReference("Category");
        edt_name_category_admin = root.findViewById(R.id.edt_name_customer);
        container_add_category = root.findViewById(R.id.container_add_category);
        image_category_choose = root.findViewById(R.id.image_category_choose);
        img_product_add_avatar = root.findViewById(R.id.img_product_add_avatar);
        img_close_add_update_category = root.findViewById(R.id.img_close_add_update_category);
        recyclerView_category = root.findViewById(R.id.recyclerView_category);
        tv_title_product_admin = root.findViewById(R.id.tv_title_product_admin);
        recyclerView_category.setHasFixedSize(true);
        recyclerView_category.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        fab = root.findViewById(R.id.fab);
        btnAddCategory = root.findViewById(R.id.btnAddCategory);
        fab.setOnClickListener(v -> showContainerAdd(0));
        img_close_add_update_category.setOnClickListener(v -> hideContainerAdd());
        img_product_add_avatar.setOnClickListener(v -> imageChooser());

        optionsCategory = new FirebaseRecyclerOptions.Builder<Categories>().setQuery(categoryReference, Categories.class).build();
        categoriesAdapter = new FirebaseRecyclerAdapter<Categories, ViewHolder_Category_admin>(optionsCategory) {

            @NonNull
            @Override
            public ViewHolder_Category_admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category_admin, parent, false);
                return new ViewHolder_Category_admin(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Category_admin holder, int position, @NonNull Categories model) {
                holder.name_Category.setText(model.getName());
                Picasso.get().load(Uri.parse(model.getImage())).into(holder.image_Category);
                holder.edit.setOnClickListener(v -> {
                    categories = categoriesAdapter.getItem(position);
                    key_category = categoriesAdapter.getRef(position).getKey();
                    selectedImageUri = Uri.parse(categories.getImage());
                    showContainerAdd(1);
                });

                holder.delete.setOnClickListener(v -> {
                    String key_remove = categoriesAdapter.getRef(position).getKey();
                    Log.e("a", "onBindViewHolder: " + key_remove);
                    AlertDialog.Builder diBuilder = new AlertDialog.Builder(getActivity());
                    diBuilder.setMessage("Xóa danh mục này sẽ xóa sản phẩm thuộc danh mục bạn có chắc chắn xóa không ?")
                            .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                            .setPositiveButton("Có", (dialog, which) -> categoryReference.child(key_remove).removeValue((error, ref) -> {
                                Toast.makeText(getActivity(), "Xóa danh mục thành công", Toast.LENGTH_SHORT).show();
                                Query productReference = FirebaseDatabase.getInstance().getReference("Product").orderByChild("id_Category").equalTo(key_remove);
                                productReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            dataSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                dialog.dismiss();
                            })).create();
                    diBuilder.show();
                });
            }
        };
        categoriesAdapter.startListening();
        recyclerView_category.setAdapter(categoriesAdapter);
        return root;
    }

    public void showContainerAdd(int type) {
        container_add_category.setVisibility(View.VISIBLE);
        if (type == 0) {
            tv_title_product_admin.setText("THÊM DANH MỤC");
            btnAddCategory.setText("THÊM DANH MỤC");
        } else {
            tv_title_product_admin.setText("SỬA DANH MỤC");
            btnAddCategory.setText("SỬA DANH MỤC");
            edt_name_category_admin.setText(categories.getName());
            Picasso.get().load(categories.getImage()).into(image_category_choose);
        }

        btnAddCategory.setOnClickListener(v -> {
            if (validate() > 0) {
                String name = edt_name_category_admin.getText().toString();
                if (type == 0) {
                    if (!(name.isEmpty() && selectedImageUri != null)) {
                        storageReference = FirebaseStorage.getInstance().getReference().child("image_category").child(selectedImageUri.getLastPathSegment());
                        storageReference.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                            categories = new Categories(name, task.getResult().toString());
                            String key = categoryReference.push().getKey();
                            categoryReference.child(key).setValue(categories)
                                    .addOnCompleteListener(task1 -> Toast.makeText(getActivity(), "Thêm danh mục thành công", Toast.LENGTH_SHORT).show())
                                    .addOnCanceledListener(() -> Toast.makeText(getActivity(), "Thêm danh mục thất bại", Toast.LENGTH_SHORT).show());
                        }));
                        clearImageAndEditText();
                        hideContainerAdd();
                    }
                } else {
                    if (!(name.isEmpty() && selectedImageUri != null)) {
                        if (!selectedImageUri.toString().contains("https")) {
                            storageReference = FirebaseStorage.getInstance().getReference().child("image_category").child(selectedImageUri.getLastPathSegment());
                            storageReference.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                                categories.setName(name);
                                categories.setImage(task.getResult().toString());
                                categoryReference.child(key_category).setValue(categories)
                                        .addOnCompleteListener(task1 -> Toast.makeText(getActivity(), "Sửa danh mục thành công", Toast.LENGTH_SHORT).show())
                                        .addOnCanceledListener(() -> Toast.makeText(getActivity(), "Sửa danh mục thất bại", Toast.LENGTH_SHORT).show());
                            }));
                        } else {
                            categories.setName(name);
                            categories.setImage(selectedImageUri.toString());
                            categoryReference.child(key_category).setValue(categories)
                                    .addOnCompleteListener(task1 -> Toast.makeText(getActivity(), "Sửa danh mục thành công", Toast.LENGTH_SHORT).show())
                                    .addOnCanceledListener(() -> Toast.makeText(getActivity(), "Sửa danh mục thất bại", Toast.LENGTH_SHORT).show());
                        }

                        clearImageAndEditText();
                        hideContainerAdd();
                    }
                }
            }
        });
    }

    public void hideContainerAdd() {
        container_add_category.setVisibility(View.GONE);
    }

    public void clearImageAndEditText() {
        edt_name_category_admin.setText("");
        selectedImageUri = null;
        image_category_choose.setImageBitmap(null);
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
                Picasso.get().load(selectedImageUri).into(image_category_choose);
            }
        }
    });

    public int validate() {
        if (selectedImageUri == null) {
            Toast.makeText(getActivity(), "Vui lòng chọn ảnh danh mục", Toast.LENGTH_SHORT).show();
            return -1;
        }
        if (edt_name_category_admin.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            return -1;
        }

        return 1;
    }

}
