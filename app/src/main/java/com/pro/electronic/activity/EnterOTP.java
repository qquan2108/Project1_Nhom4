package com.pro.electronic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pro.electronic.R;

import java.util.concurrent.TimeUnit;

public class EnterOTP extends AppCompatActivity {

    public static final String TAG = EnterOTP.class.getName();

    private TextView tvResendOTP;
    private EditText etOTP;
    private Button btnVerify;

    private FirebaseAuth mAuth;
    private String mPhoneNumber;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enter_otp);

        getDataIntent();
        setTitleToolbar();
        initUi();

        mAuth = FirebaseAuth.getInstance();

        btnVerify.setOnClickListener(v -> {
            String otpCode = etOTP.getText().toString().trim();
            onClickVerify(otpCode);
        });

        tvResendOTP.setOnClickListener(v -> onClickResendOTP());
    }

    private void getDataIntent() {
        mPhoneNumber = getIntent().getStringExtra("phone_Number");
        mVerificationId = getIntent().getStringExtra("verificationId");
    }

    private void setTitleToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Enter OTP");
        }
    }

    private void initUi() {
        etOTP = findViewById(R.id.etOTP);
        btnVerify = findViewById(R.id.btnVerify);
        tvResendOTP = findViewById(R.id.tvResendOTP);
    }

    private void onClickVerify(String otpCode) {
        if (otpCode.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void onClickResendOTP() {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(mPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setForceResendingToken(mResendToken)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(EnterOTP.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Verification failed: " + e.getMessage());
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);
                        mVerificationId = verificationId;
                        mResendToken = token;
                        Toast.makeText(EnterOTP.this, "Mã OTP đã được gửi lại", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            gotoMainActivity(user.getPhoneNumber());
                        }
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "Mã OTP không chính xác", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void gotoMainActivity(String phoneNumber) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
        finish();
    }
}
