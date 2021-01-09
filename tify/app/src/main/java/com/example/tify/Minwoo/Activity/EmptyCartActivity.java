package com.example.tify.Minwoo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tify.R;

public class EmptyCartActivity extends AppCompatActivity {

    Button orderNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_empty_cart);

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.cart_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        orderNow = findViewById(R.id.activity_EmptyCart_Btn_OrderNow);
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmptyCartActivity.this, StoreInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}