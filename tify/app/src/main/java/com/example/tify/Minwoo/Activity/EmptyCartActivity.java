package com.example.tify.Minwoo.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tify.R;

public class EmptyCartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_empty_cart);

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.cart_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }
}