package com.example.tify.Taehyun.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tify.Hyeona.Activity.LoginActivity;
import com.example.tify.R;

public class Mypage_UserDeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_mypage__user_delete);

        Button userDelete = findViewById(R.id.userDelete);

        userDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mypage_UserDeleteActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

}