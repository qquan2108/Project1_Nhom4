package com.pro.electronic.activity.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pro.electronic.MyApplication;
import com.pro.electronic.R;
import com.pro.electronic.activity.BaseActivity;
import com.pro.electronic.adapter.admin.AdminRoleAdapter;
import com.pro.electronic.model.Admin;
import com.pro.electronic.utils.GlobalFunction;

import java.util.ArrayList;
import java.util.List;

public class AdminRoleActivity extends BaseActivity {

    private FloatingActionButton btnAdd;
    private List<Admin> mListAdmin;
    private AdminRoleAdapter mAdminRoleAdapter;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_role);

        initToolbar();
        initUi();
        initView();
        loadListAdmin();
    }

    private void initToolbar() {
        ImageView imgToolbarBack = findViewById(R.id.img_toolbar_back);
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        imgToolbarBack.setOnClickListener(view -> onBackPressed());
        tvToolbarTitle.setText(getString(R.string.label_role_admin));
    }

    private void initUi() {
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(v -> onClickAddAdmin());
    }

    private void initView() {
        RecyclerView rcvData = findViewById(R.id.rcv_data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvData.setLayoutManager(linearLayoutManager);
        mListAdmin = new ArrayList<>();
        mAdminRoleAdapter = new AdminRoleAdapter(mListAdmin);
        rcvData.setAdapter(mAdminRoleAdapter);
        rcvData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    btnAdd.hide();
                } else {
                    btnAdd.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void onClickAddAdmin() {
        GlobalFunction.startActivity(this, AdminAddRoleActivity.class);
    }

    public void loadListAdmin() {
        mChildEventListener = new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                if (admin == null || mListAdmin == null) return;
                mListAdmin.add(0, admin);
                if (mAdminRoleAdapter != null) mAdminRoleAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                if (admin == null || mListAdmin == null || mListAdmin.isEmpty()) return;
                for (int i = 0; i < mListAdmin.size(); i++) {
                    if (admin.getId() == mListAdmin.get(i).getId()) {
                        mListAdmin.set(i, admin);
                        break;
                    }
                }
                if (mAdminRoleAdapter != null) mAdminRoleAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                if (admin == null || mListAdmin == null || mListAdmin.isEmpty()) return;
                for (Admin adminObject : mListAdmin) {
                    if (admin.getId() == adminObject.getId()) {
                        mListAdmin.remove(adminObject);
                        break;
                    }
                }
                if (mAdminRoleAdapter != null) mAdminRoleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        MyApplication.get(this).getAdminDatabaseReference().addChildEventListener(mChildEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChildEventListener != null) {
            MyApplication.get(this).getAdminDatabaseReference().addChildEventListener(mChildEventListener);
        }
    }
}
