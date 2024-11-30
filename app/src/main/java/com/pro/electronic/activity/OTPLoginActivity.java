package com.pro.electronic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pro.electronic.R;

import java.util.concurrent.TimeUnit;

public class OTPLoginActivity extends AppCompatActivity {

    private static final String TAG = "OTPLoginActivity";

    private EditText phoneNumberEditText;
    private TextView backToLoginText;
    private Button sendOTPButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otplogin);

        mAuth = FirebaseAuth.getInstance();

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        sendOTPButton = findViewById(R.id.sendOTPButton);
        backToLoginText = findViewById(R.id.backToLoginText);

        backToLoginText.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChooseLoginActivity.class);
            startActivity(intent);
            finish();
        });

        sendOTPButton.setOnClickListener(view -> {
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                Log.d(TAG, "Sending OTP to: " + phoneNumber);
                sendOTP(phoneNumber);
            } else {
                Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOTP(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        Log.d(TAG, "Verification completed automatically");
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.e(TAG, "Verification failed", e);
                        Toast.makeText(OTPLoginActivity.this, "Xác minh thất bại", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        Log.d(TAG, "Code sent: " + verificationId);
                        goToEnterOTP(phoneNumber, verificationId);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "OTP sign-in successful");
                Intent intent = new Intent(OTPLoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.e(TAG, "OTP sign-in failed");
                Toast.makeText(this, "OTP không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToEnterOTP(String phone, String verificationId) {
        Intent intent = new Intent(this, EnterOTP.class);
        intent.putExtra("phone", phone);
        intent.putExtra("verificationId", verificationId);
        startActivity(intent);
    }
}