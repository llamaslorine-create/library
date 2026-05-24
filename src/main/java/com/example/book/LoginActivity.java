package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BookData.initialize(this);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        tvError = findViewById(R.id.tv_error);

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                User user = UserData.login(username, password);
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    tvError.setText("账号或密码错误");
                }
            }
        });
    }
}