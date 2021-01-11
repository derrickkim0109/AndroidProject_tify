package com.example.tify.Jiseok.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tify.R;

public class EmailLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjs_activity_email_login);
        ImageView giflogin = findViewById(R.id.giflogin);
        Glide.with(this).load(R.drawable.login_image04).into(giflogin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //액션바 삭제

    }
}