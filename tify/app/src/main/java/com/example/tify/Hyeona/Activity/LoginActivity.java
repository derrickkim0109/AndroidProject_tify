package com.example.tify.Hyeona.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tify.Jiseok.Activity.JoinActivity;
import com.example.tify.R;

public class LoginActivity extends AppCompatActivity {
    TextView emailJoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_activity_login);

        emailJoin = findViewById(R.id.login_tv_emailJoin);
        emailJoin.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_tv_emailJoin:
                    Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}