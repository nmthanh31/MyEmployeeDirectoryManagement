package com.example.myemployeedirectorymanagement.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myemployeedirectorymanagement.R;
import com.example.myemployeedirectorymanagement.activity.AddDepartmentActivity;
import com.example.myemployeedirectorymanagement.activity.DetailDepartmentActivity;
import com.example.myemployeedirectorymanagement.adapter.DepartmentAdapter;
import com.example.myemployeedirectorymanagement.interfaces.OnItemDepartmentActionListener;
import com.example.myemployeedirectorymanagement.models.Department;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DepartmentFragment extends Fragment {

    private AppCompatEditText edtSearch;

    private RecyclerView rcvListDepartment;

    private ImageButton btnAdd;

    private List<Department> departmentList;

    private DepartmentAdapter departmentAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DepartmentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department, container, false);
        edtSearch = view.findViewById(R.id.edtSearch);
        rcvListDepartment = view.findViewById(R.id.rcvListDepartment);

        rcvListDepartment.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        btnAdd = view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddDepartmentActivity.class);
                startActivity(intent);
            }
        });

        departmentList = new ArrayList<Department>();


        departmentAdapter = new DepartmentAdapter(departmentList, getContext(), new OnItemDepartmentActionListener() {
            @Override
            public void onItemDepartmentClickListener(Department department) {
                Intent intent = new Intent(getActivity(), DetailDepartmentActivity.class);
                intent.putExtra("id", department.getIdDepartment());
                intent.putExtra("name", department.getNameDepartment());
                intent.putExtra("email", department.getEmail());
                intent.putExtra("website", department.getWebsite());
                intent.putExtra("address", department.getAddress());
                intent.putExtra("phone", department.getPhoneNumber());
                intent.putExtra("logo", String.valueOf(department.getLogo()));
                startActivity(intent);
            }
        });
        rcvListDepartment.setAdapter(departmentAdapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Không cần thiết phải làm gì trước khi thay đổi văn bản
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterDepartments(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần thiết phải làm gì sau khi thay đổi văn bản
            }
        });


        loadDepartments();


        return view;
    }

    private void loadDepartments() {
        db.collection("departments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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

                                Department department = new Department(id, name, email, website, logoUrl,address, phoneNumber);
                                departmentList.add(department);
                            }
                            Collections.sort(departmentList, new Comparator<Department>() {
                                @Override
                                public int compare(Department d1, Department d2) {
                                    return d1.getNameDepartment().compareToIgnoreCase(d2.getNameDepartment());
                                }
                            });

                            departmentAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("Loi", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void filterDepartments(String text) {
        List<Department> filteredList = new ArrayList<>();
        for (Department department : departmentList) {
            if (department.getNameDepartment().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(department);
            }
        }

        departmentAdapter.filterList(filteredList);
    }

}
