package com.example.myemployeedirectorymanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myemployeedirectorymanagement.R;
import com.example.myemployeedirectorymanagement.interfaces.OnItemDepartmentActionListener;
import com.example.myemployeedirectorymanagement.interfaces.OnItemEmployeeActionListener;
import com.example.myemployeedirectorymanagement.models.Department;
import com.example.myemployeedirectorymanagement.models.Employee;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeHolder> {

    private List<Employee> employees;

    private Context cont;

    private OnItemEmployeeActionListener onItemEmployeeActionListener;

    public EmployeeAdapter(List<Employee> employees, Context cont, OnItemEmployeeActionListener onItemEmployeeActionListener) {
        this.employees = employees;
        this.cont = cont;
        this.onItemEmployeeActionListener = onItemEmployeeActionListener;
    }

    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployeeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeHolder holder, int position) {
        Employee employee = employees.get(position);

        holder.tvDisplayName.setText(employee.getNameEmployee());

        if (employee.getAvatar()!=null ){
            Glide.with(holder.itemView.getContext())
                    .load(employee.getAvatar())
                    .into(holder.imvLogo);
        }

        holder.bind(employee, onItemEmployeeActionListener);
    }

    @Override
    public int getItemCount() {
        return employees!=null ? employees.size() : 0;
    }

    public void filterList(List<Employee> filteredList) {
        employees = filteredList;
        notifyDataSetChanged();
    }

    public static class EmployeeHolder extends RecyclerView.ViewHolder{

        private TextView tvDisplayName;

        private ImageView imvLogo;

        public EmployeeHolder(@NonNull View itemView) {
            super(itemView);

            tvDisplayName = itemView.findViewById(R.id.tvDisplayName);

            imvLogo = itemView.findViewById(R.id.imvLogo);
        }

        public void bind(final Employee employee, final OnItemEmployeeActionListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemEmployeeClickListener(employee);
                }
            });
        }
    }
}
