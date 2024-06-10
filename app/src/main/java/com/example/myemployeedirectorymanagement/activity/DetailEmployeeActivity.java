package com.example.myemployeedirectorymanagement.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.myemployeedirectorymanagement.R;
import com.example.myemployeedirectorymanagement.models.Department;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailEmployeeActivity extends AppCompatActivity {

    private ImageButton btnBack,btnEdit, btnDelete;

    private ShapeableImageView imgLogo;

    private TextView tvName,tvPosition, tvEmail, tvPhone, tvDepartment;

    private Department selectedDepartment;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_employee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        imgLogo = findViewById(R.id.imgLogo);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPosition = findViewById(R.id.tvPosition);
        tvDepartment = findViewById(R.id.tvDepartment);
        tvPhone = findViewById(R.id.tvPhone);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String position = intent.getStringExtra("position");
        String idDepartment = intent.getStringExtra("idDepartment");
        String phone = intent.getStringExtra("phone");
        Uri avatar = Uri.parse(intent.getStringExtra("avatar"));


        loadDepartment(idDepartment);



        tvName.setText(name);
        tvEmail.setText("Email: "+email);
        tvPhone.setText("Số điện thoại: "+phone);
        tvPosition.setText("Chức vụ: "+position);
//        tvDepartment.setText("Đơn vị: "+selectedDepartment.getNameDepartment());
        Glide.with(this).load(avatar).into(imgLogo);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("employees").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DetailEmployeeActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailEmployeeActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailEmployeeActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(DetailEmployeeActivity.this, EditEmployeeActivity.class);
                editIntent.putExtra("id", id);
                editIntent.putExtra("name", name);
                editIntent.putExtra("email", email);
                editIntent.putExtra("position", position);
                editIntent.putExtra("idDepartment", idDepartment);
                editIntent.putExtra("phone", phone);
                editIntent.putExtra("avatar", avatar.toString());
                startActivity(editIntent);
            }
        });
    }
    private void loadDepartment(String idDepartment) {
        db.collection("departments").document(idDepartment).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String departmentName = document.getString("name");
                                tvDepartment.setText("Đơn vị: " + departmentName);
                            } else {
                                Log.w("DetailEmployeeActivity", "No such document");
                            }
                        } else {
                            Log.w("DetailEmployeeActivity", "get failed with ", task.getException());
                        }
                    }
                });
    }
}