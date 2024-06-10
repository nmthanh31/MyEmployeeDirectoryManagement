package com.example.myemployeedirectorymanagement.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailDepartmentActivity extends AppCompatActivity {

    private ImageButton btnBack, btnEdit, btnDelete;

    private ShapeableImageView imgLogo;

    private TextView tvName, tvEmail, tvWebsite, tvAddress, tvPhone;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_department);
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
        tvWebsite = findViewById(R.id.tvWebsite);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String website = intent.getStringExtra("website");
        String address = intent.getStringExtra("address");
        String phone = intent.getStringExtra("phone");
        Uri logo = Uri.parse(intent.getStringExtra("logo"));

        tvName.setText(name);
        tvEmail.setText("Email: "+email);
        tvPhone.setText("Số điện thoại: "+phone);
        tvWebsite.setText("Website: "+website);
        tvAddress.setText("Địa chỉ: "+address);
        Glide.with(this).load(logo).into(imgLogo);



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("departments").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DetailDepartmentActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailDepartmentActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailDepartmentActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(DetailDepartmentActivity.this, EditDepartmentActivity.class);
                editIntent.putExtra("id", id);
                editIntent.putExtra("name", name);
                editIntent.putExtra("email", email);
                editIntent.putExtra("website", website);
                editIntent.putExtra("address", address);
                editIntent.putExtra("phone", phone);
                editIntent.putExtra("logo", logo.toString());
                startActivity(editIntent);
            }
        });
    }
}