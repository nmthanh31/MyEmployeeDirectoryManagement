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
import com.example.myemployeedirectorymanagement.models.Department;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentHolder> {

    private List<Department> departments;

    private Context cont;

    private OnItemDepartmentActionListener onItemDepartmentActionListener;

    public DepartmentAdapter(List<Department> departments, Context cont, OnItemDepartmentActionListener onItemDepartmentActionListener) {
        this.departments = departments;
        this.cont = cont;
        this.onItemDepartmentActionListener = onItemDepartmentActionListener;
    }

    @NonNull
    @Override
    public DepartmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv, parent, false);
        return new DepartmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentHolder holder, int position) {
        Department department = departments.get(position);

        holder.tvDisplayName.setText(department.getNameDepartment());
        if (department.getLogo()!=null ){
            Glide.with(holder.itemView.getContext())
                    .load(department.getLogo())
                    .into(holder.imvLogo);
        }

        holder.bind(department, onItemDepartmentActionListener);
    }

    @Override
    public int getItemCount() {
        return departments!=null ? departments.size() : 0;
    }

    public void filterList(List<Department> filteredList) {
        departments = filteredList;
        notifyDataSetChanged();
    }
    public static class DepartmentHolder extends RecyclerView.ViewHolder{

        private TextView tvDisplayName;

        private ImageView imvLogo;

        public DepartmentHolder(@NonNull View itemView) {
            super(itemView);

            tvDisplayName = itemView.findViewById(R.id.tvDisplayName);

            imvLogo = itemView.findViewById(R.id.imvLogo);
        }

        public void bind(final Department department, final OnItemDepartmentActionListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemDepartmentClickListener(department);
                }
            });
        }
    }

}
