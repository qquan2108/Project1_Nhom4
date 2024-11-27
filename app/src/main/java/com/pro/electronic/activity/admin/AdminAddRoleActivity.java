package com.pro.electronic.activity.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pro.electronic.MyApplication;
import com.pro.electronic.R;
import com.pro.electronic.activity.BaseActivity;
import com.pro.electronic.model.Admin;
import com.pro.electronic.utils.Constant;
import com.pro.electronic.utils.GlobalFunction;
import com.pro.electronic.utils.StringUtil;

public class AdminAddRoleActivity extends BaseActivity {

    private EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_role);

        initUi();
    }

    private void initUi() {
        ImageView imgToolbarBack = findViewById(R.id.img_toolbar_back);
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(getString(R.string.label_add_role));
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        Button btnAdd = findViewById(R.id.btn_add);

        imgToolbarBack.setOnClickListener(view -> onBackPressed());
        btnAdd.setOnClickListener(v -> addRole());
    }

    private void addRole() {
        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            showToastMessage(getString(R.string.msg_email_admin_empty));
            return;
        }

        if (StringUtil.isEmpty(strPassword)) {
            showToastMessage(getString(R.string.msg_password_admin_empty));
            return;
        }

        if (!StringUtil.isValidEmail(strEmail)) {
            showToastMessage(getString(R.string.msg_email_invalid));
            return;
        }

        if (!strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
            showToastMessage(getString(R.string.msg_email_invalid_admin));
            return;
        }

        // Add admin
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            long adminId = System.currentTimeMillis();
                            Admin admin = new Admin(adminId, user.getEmail());
                            MyApplication.get(this).getAdminDatabaseReference()
                                    .child(String.valueOf(adminId)).setValue(admin, (error, ref) -> {
                                        edtEmail.setText("");
                                        edtPassword.setText("");
                                        GlobalFunction.hideSoftKeyboard(this);
                                        showToastMessage(getString(R.string.msg_add_admin_success));
                                    });
                        }
                    } else {
                        showToastMessage(getString(R.string.msg_register_error));
                    }
                });
    }
}