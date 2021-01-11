package com.example.tify.Hyeona.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Jiseok.Activity.JoinActivity;
import com.example.tify.R;

public class LoginActivity extends AppCompatActivity {
    String userEmail;
    String userSeq;
    TextView emailJoin;
    Button btnEmailLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_activity_login);

        //자동로그인
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        userEmail = auto.getString("userEmail",null);
        userSeq = auto.getString("userSeq",null);
        if(userEmail !=null && userSeq!=null){
            startActivity(new Intent(LoginActivity.this, JiseokMainActivity.class));
        }
        //자동로그인 끝

        emailJoin = findViewById(R.id.login_tv_emailJoin);
        btnEmailLogin = findViewById(R.id.login_btn_emailLogin);
        emailJoin.setOnClickListener(onClickListener);
        btnEmailLogin.setOnClickListener(onClickListener);




    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_tv_emailJoin:
                    Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                    startActivity(intent);
                    break;
                case R.id.login_btn_emailLogin:

                    break;
            }
        }
    };
}