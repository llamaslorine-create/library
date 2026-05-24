package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView tvUserInfo = findViewById(R.id.tv_user_info);
        LinearLayout llAdminPanel = findViewById(R.id.ll_admin_panel);
        Button btnLogout = findViewById(R.id.btn_logout);

        User user = UserData.getCurrentUser();
        if (user != null) {
            tvUserInfo.setText("当前登录: " + user.getName() + " (" + 
                    (user.isAdmin() ? "管理员" : "普通用户") + ")");
            
            if (user.isAdmin()) {
                llAdminPanel.setVisibility(View.VISIBLE);
            }
        }

        btnLogout.setOnClickListener(v -> {
            UserData.setCurrentUser(null);
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void goToSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void goToBookList(View view) {
        Intent intent = new Intent(this, BookListActivity.class);
        startActivity(intent);
    }

    public void goToBorrow(View view) {
        Intent intent = new Intent(this, BorrowActivity.class);
        startActivity(intent);
    }

    public void goToAdmin(View view) {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}