package com.example.myemployeedirectorymanagement.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.myemployeedirectorymanagement.R;
import com.example.myemployeedirectorymanagement.models.Department;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditEmployeeActivity extends AppCompatActivity {

    private ImageButton btnBack;

    private Button btnSave, btnAddLogo;

    private ImageView imvLogo;

    private AppCompatEditText edtName,edtEmail, edtPosition, edtPhone;

    private AppCompatSpinner spnDepartment;

    private List<Department> departmentList;

    private static final int PICK_IMAGE = 1;

    private Uri selectedImage;

    private StorageReference storageReference;
    private FirebaseFirestore firestore;

    private String idEmployee;

    private String idDepartment;

    private ArrayAdapter<Department> adapter;

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
        setContentView(R.layout.activity_edit_employee);
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

        edtPosition = findViewById(R.id.edtPosition);

        edtPhone = findViewById(R.id.edtPhone);

        spnDepartment = findViewById(R.id.spnDepartment);

        Intent intent = getIntent();
        idEmployee = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String position = intent.getStringExtra("position");
        idDepartment = intent.getStringExtra("idDepartment");
        String phone = intent.getStringExtra("phone");
        selectedImage = Uri.parse(intent.getStringExtra("avatar"));
        Uri avatar = Uri.parse(intent.getStringExtra("avatar"));

        edtName.setText(name);
        edtEmail.setText(email);
        edtPosition.setText(position);
        edtPhone.setText(phone);
//        Glide.with(this).load(selectedImage).into(imvLogo);

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
        departmentList = new ArrayList<Department>();

        adapter = new ArrayAdapter<Department>(this, R.layout.spinner_item, departmentList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.spinner_item, parent, false);
                }
                TextView textView = convertView.findViewById(R.id.textView);
                textView.setText(departmentList.get(position).getNameDepartment());
                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.spinner_item, parent, false);
                }
                TextView textView = convertView.findViewById(R.id.textView);
                textView.setText(departmentList.get(position).getNameDepartment());
                return convertView;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDepartment.setAdapter(adapter);

        spnDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Department selectedDepartment = (Department) adapterView.getItemAtPosition(i);
                idDepartment = selectedDepartment.getIdDepartment();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        btnSave.setOnClickListener(view -> {
            if (selectedImage != null) {
                if (avatar.equals(selectedImage)){
                    updateDataInFirestore(String.valueOf(avatar), idEmployee);
                }else{
                    uploadImageAndSaveData();
                }
            } else {
                updateDataInFirestore(null, idEmployee);
            }
        });

        loadDepartments();
    }
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void uploadImageAndSaveData() {
        final StorageReference fileReference = storageReference.child("employee_avatars/" + System.currentTimeMillis() + ".jpg");

        fileReference.putFile(selectedImage)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    updateDataInFirestore(downloadUrl, idEmployee);
                }))
                .addOnFailureListener(e -> Toast.makeText(EditEmployeeActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
//
    private void updateDataInFirestore(@Nullable String avatarUrl, String employeeId) {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String position = edtPosition.getText().toString().trim();
        String phone = edtPhone.getText().toString();

        Map<String, Object> employee = new HashMap<>();
        employee.put("name", name);
        employee.put("email", email);
        employee.put("position", position);
        employee.put("idDepartment", idDepartment);
        employee.put("phone", phone);
        if (avatarUrl != null) {
            employee.put("avatarURL", avatarUrl);
        }

        firestore.collection("employees")
                .document(employeeId)
                .set(employee)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(EditEmployeeActivity.this, "Employee added successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditEmployeeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditEmployeeActivity.this, "Error adding employee: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadDepartments() {
        firestore.collection("departments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        departmentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String email = document.getString("email");
                            String website = document.getString("website");
                            Uri logoUrl = Uri.parse(document.getString("logoUrl"));
                            String address = document.getString("address");
                            String phoneNumber = document.getString("phone");

                            Department department = new Department(id, name, email, website, logoUrl, address, phoneNumber);
                            departmentList.add(department);
//                            Toast.makeText(this, department.getIdDepartment(), Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("LoadDepartments", "Error getting documents.", task.getException());
                    }
                });
    }
}