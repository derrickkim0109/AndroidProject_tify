package com.android.tify_store.Minwoo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.tify_store.R;

public class LoginActivity extends AppCompatActivity {

    String TAG = "LoginActivity";
    EditText ip;
    EditText pw;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_login);

        ip = findViewById(R.id.ip);
        pw = findViewById(R.id.pw);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 서버 접속 (가게 ip는 아이디, pw는 직접 부여한다.) => DB로 관리
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}