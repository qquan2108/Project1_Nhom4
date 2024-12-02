package com.pro.electronic.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pro.electronic.R;
import com.pro.electronic.activity.admin.AdminMainActivity;
import com.pro.electronic.model.User;
import com.pro.electronic.prefs.DataStoreManager;
import com.pro.electronic.utils.Constant;
import com.pro.electronic.utils.GlobalFunction;
import com.pro.electronic.utils.StringUtil;

public class MailLoginActivity extends BaseActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private LinearLayout layoutRegister;
    private TextView tvForgotPassword, tvBackToLogin;
    private boolean isEnableButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initUi();
        initListener();
    }

    private void initUi() {
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        layoutRegister = findViewById(R.id.layout_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvBackToLogin = findViewById(R.id.backToLoginText);
    }

    private void initListener() {
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString())) {
                    edtEmail.setBackgroundResource(R.drawable.bg_white_corner_16_border_main);
                } else {
                    edtEmail.setBackgroundResource(R.drawable.bg_white_corner_16_border_gray);
                }

                String strPassword = edtPassword.getText().toString().trim();
                if (!StringUtil.isEmpty(s.toString()) && !StringUtil.isEmpty(strPassword)) {
                    isEnableButtonLogin = true;
                    btnLogin.setBackgroundResource(R.drawable.bg_button_enable_corner_16);
                } else {
                    isEnableButtonLogin = false;
                    btnLogin.setBackgroundResource(R.drawable.bg_button_disable_corner_16);
                }
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString())) {
                    edtPassword.setBackgroundResource(R.drawable.bg_white_corner_16_border_main);
                } else {
                    edtPassword.setBackgroundResource(R.drawable.bg_white_corner_16_border_gray);
                }

                String strEmail = edtEmail.getText().toString().trim();
                if (!StringUtil.isEmpty(s.toString()) && !StringUtil.isEmpty(strEmail)) {
                    isEnableButtonLogin = true;
                    btnLogin.setBackgroundResource(R.drawable.bg_button_enable_corner_16);
                } else {
                    isEnableButtonLogin = false;
                    btnLogin.setBackgroundResource(R.drawable.bg_button_disable_corner_16);
                }
            }
        });

        layoutRegister.setOnClickListener(
                v -> GlobalFunction.startActivity(this, RegisterActivity.class));

        btnLogin.setOnClickListener(v -> onClickValidateLogin());

        tvForgotPassword.setOnClickListener(
                v -> GlobalFunction.startActivity(this, ForgotPasswordActivity.class));

        tvBackToLogin.setOnClickListener(
                v -> GlobalFunction.startActivity(this, ChooseLoginActivity.class));
    }

    private void onClickValidateLogin() {
        if (!isEnableButtonLogin) return;

        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            showToastMessage(getString(R.string.msg_email_require));
        } else if (StringUtil.isEmpty(strPassword)) {
            showToastMessage(getString(R.string.msg_password_require));
        } else if (!StringUtil.isValidEmail(strEmail)) {
            showToastMessage(getString(R.string.msg_email_invalid));
        } else {
            loginUserFirebase(strEmail, strPassword);
        }
    }

    private void loginUserFirebase(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            if (user.getEmail() != null && user.getEmail().contains(Constant.ADMIN_EMAIL_FORMAT)) {
                                userObject.setAdmin(true);
                            }
                            DataStoreManager.setUser(userObject);
                            goToMainActivity();
                        }
                    } else {
                        showToastMessage(getString(R.string.msg_login_error) + task.getException());
                        Log.e("TAG", "loginUserFirebase: " + task.getException());
                    }
                });
    }

    private void goToMainActivity() {
        if (DataStoreManager.getUser().isAdmin()) {
            GlobalFunction.startActivity(this, AdminMainActivity.class);
        } else {
            GlobalFunction.startActivity(this, MainActivity.class);
        }
        finishAffinity();
    }

}