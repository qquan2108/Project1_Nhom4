package com.pro.electronic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pro.electronic.R;

public class ChooseLoginActivity extends AppCompatActivity {

    private Button googleLoginButton, otpLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);

        googleLoginButton = findViewById(R.id.googleLoginButton);
        otpLoginButton = findViewById(R.id.otpLoginButton);

        googleLoginButton.setOnClickListener(view -> {
            Intent intent = new Intent(ChooseLoginActivity.this, MailLoginActivity.class);
            startActivity(intent);
        });

        otpLoginButton.setOnClickListener(view -> {
            Intent intent = new Intent(ChooseLoginActivity.this, OTPLoginActivity.class);
            startActivity(intent);
        });
    }
}
