package com.pro.electronic.adapter.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.electronic.R;
import com.pro.electronic.model.Admin;

import java.util.List;

public class AdminRoleAdapter extends RecyclerView.Adapter<AdminRoleAdapter.AdminRoleViewHolder> {

    private final List<Admin> mListAdmin;

    public AdminRoleAdapter(List<Admin> mListAdmin) {
        this.mListAdmin = mListAdmin;
    }

    @NonNull
    @Override
    public AdminRoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_role, parent, false);
        return new AdminRoleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRoleViewHolder holder, int position) {
        Admin admin = mListAdmin.get(position);
        if (admin == null) return;
        holder.tvEmail.setText(admin.getEmail());
    }

    @Override
    public int getItemCount() {
        if (mListAdmin != null) {
            return mListAdmin.size();
        }
        return 0;
    }

    public static class AdminRoleViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvEmail;

        public AdminRoleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.tv_email);
        }
    }
}
