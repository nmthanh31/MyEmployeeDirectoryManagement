package com.example.myemployeedirectorymanagement.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.myemployeedirectorymanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditDepartmentActivity extends AppCompatActivity {

    private ImageButton btnBack;

    private Button btnSave, btnAddLogo;

    private ImageView imvLogo;

    private AppCompatEditText edtName,edtEmail, edtWebsite, edtAddress, edtPhone;

    private static final int PICK_IMAGE = 1;

    private Uri selectedImage;

    private StorageReference storageReference;
    private FirebaseFirestore firestore;

    private String departmentId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            imvLogo = findViewById(R.id.imvLogo);
            imvLogo.setImageURI(selectedImage);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_department);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore = FirebaseFirestore.getInstance();




        btnBack = findViewById(R.id.btnBack);

        btnAddLogo = findViewById(R.id.btnAddLogo);

        btnSave = findViewById(R.id.btnSave);

        edtName = findViewById(R.id.edtName);

        edtEmail = findViewById(R.id.edtEmail);

        edtWebsite = findViewById(R.id.edtWebsite);

        edtAddress = findViewById(R.id.edtAddress);

        edtPhone = findViewById(R.id.edtPhone);

        imvLogo = findViewById(R.id.imvLogo);

        Intent intent = getIntent();
        String departmentId = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String website = intent.getStringExtra("website");
        String address = intent.getStringExtra("address");
        String phone = intent.getStringExtra("phone");
        selectedImage = Uri.parse(intent.getStringExtra("logo"));
        Uri logo = Uri.parse(intent.getStringExtra("logo"));

        edtName.setText(name);
        edtEmail.setText(email);
        edtWebsite.setText(website);
        edtAddress.setText(address);
        edtPhone.setText(phone);
        Glide.with(this).load(selectedImage).into(imvLogo);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImage != null) {
                    if (logo.equals(selectedImage)){
                        updateDataInFirestore(String.valueOf(logo), departmentId);
                    }else{
                        uploadImageAndSaveData();
                    }

                } else {
                    updateDataInFirestore(null, departmentId);
                }
            }
        });
    }
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }
    private void uploadImageAndSaveData() {
        final StorageReference fileReference = storageReference.child("department_logos/" + System.currentTimeMillis() + ".jpg");
        fileReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                // Nếu tập tin đã tồn tại, không tải lên lại
                updateDataInFirestore(fileReference.getPath(), departmentId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Nếu tập tin không tồn tại hoặc có lỗi, tải lên mới
                fileReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadUrl = uri.toString();
                                        updateDataInFirestore(downloadUrl, departmentId);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditDepartmentActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void updateDataInFirestore(@Nullable String logoUrl, String departmentId) {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String website = edtWebsite.getText().toString();
        String address = edtAddress.getText().toString();
        String phone = edtPhone.getText().toString();

        Map<String, Object> department = new HashMap<>();
        department.put("name", name);
        department.put("email", email);
        department.put("website", website);
        department.put("address", address);
        department.put("phone", phone);
        if (logoUrl != null) {
            department.put("logoUrl", logoUrl);
        }

        firestore.collection("departments")
                .document(departmentId) // departmentId là id của phòng ban cần cập nhật
                .set(department) // Sử dụng set() thay vì add()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditDepartmentActivity.this, "Department updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditDepartmentActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditDepartmentActivity.this, "Error updating department: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}