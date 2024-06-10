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
import com.example.myemployeedirectorymanagement.activity.AddEmployeeActivity;
import com.example.myemployeedirectorymanagement.activity.DetailDepartmentActivity;
import com.example.myemployeedirectorymanagement.activity.DetailEmployeeActivity;
import com.example.myemployeedirectorymanagement.adapter.DepartmentAdapter;
import com.example.myemployeedirectorymanagement.adapter.EmployeeAdapter;
import com.example.myemployeedirectorymanagement.interfaces.OnItemEmployeeActionListener;
import com.example.myemployeedirectorymanagement.models.Department;
import com.example.myemployeedirectorymanagement.models.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EmployeeFragment extends Fragment {

    private AppCompatEditText edtSearch;

    private RecyclerView rcvListEmployee;

    private ImageButton btnAdd;

    private List<Employee> employeeList;

    private EmployeeAdapter employeeAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public EmployeeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_employee,container, false);

        edtSearch = view.findViewById(R.id.edtSearch);
        rcvListEmployee = view.findViewById(R.id.rcvListEmployee);

        rcvListEmployee.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        btnAdd = view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEmployeeActivity.class);
                startActivity(intent);
            }
        });

        employeeList = new ArrayList<Employee>();

        employeeAdapter = new EmployeeAdapter(employeeList, getContext(), new OnItemEmployeeActionListener() {
            @Override
            public void onItemEmployeeClickListener(Employee employee) {
                Intent intent = new Intent(getActivity(), DetailEmployeeActivity.class);
                intent.putExtra("id", employee.getIdEmployee());
                intent.putExtra("name", employee.getNameEmployee());
                intent.putExtra("email", employee.getEmail());
                intent.putExtra("position", employee.getPosition());
                intent.putExtra("phone", employee.getPhoneNumber());
                intent.putExtra("avatar", String.valueOf(employee.getAvatar()));
                intent.putExtra("idDepartment", employee.getIdDepartment());
                startActivity(intent);
            }
        });
        rcvListEmployee.setAdapter(employeeAdapter);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterDepartments(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        loadEmployees();

        return view;
    }

    private void loadEmployees() {
        db.collection("employees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            employeeList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String name = document.getString("name");
                                String email = document.getString("email");
                                String position = document.getString("position");
                                String phone = document.getString("phone");
                                Uri avatar = Uri.parse(document.getString("avatarURL"));
                                String idDepartment = document.getString("idDepartment");

                                Employee employee = new Employee(id, name, position, email, phone, avatar, idDepartment);
                                employeeList.add(employee);
                            }
                            Collections.sort(employeeList, new Comparator<Employee>() {
                                @Override
                                public int compare(Employee d1, Employee d2) {
                                    return d1.getNameEmployee().compareToIgnoreCase(d2.getNameEmployee());
                                }
                            });

                            employeeAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("Loi", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void filterDepartments(String text) {
        List<Employee> filteredList = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (employee.getNameEmployee().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(employee);
            }
        }

        employeeAdapter.filterList(filteredList);
    }
}
