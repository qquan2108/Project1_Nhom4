package com.pro.electronic.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pro.electronic.R;

public class EnterOTP extends AppCompatActivity {

    private EditText otpEditText;
    private Button verifyButton;
    private TextView errorMessage;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String phoneNumber;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        mAuth = FirebaseAuth.getInstance();

        verificationId = getIntent().getStringExtra("verificationId");
        phoneNumber = getIntent().getStringExtra("phone");

        otpEditText = findViewById(R.id.otpEditText);
        verifyButton = findViewById(R.id.verifyButton);
        errorMessage = findViewById(R.id.errorMessage);

        verifyButton.setOnClickListener(v -> {
            String otp = otpEditText.getText().toString().trim();

            if (otp.isEmpty()) {
                Toast.makeText(EnterOTP.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            } else {
                verifyOTP(otp);
            }
        });
    }

    private void verifyOTP(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EnterOTP.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().getCurrentUser();
                        gotoMainActivity();
                    } else {
                        errorMessage.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(EnterOTP.this, MainActivity.class);
        intent.putExtra("phone_Number", phoneNumber);
        startActivity(intent);
        finish();
    }


}
