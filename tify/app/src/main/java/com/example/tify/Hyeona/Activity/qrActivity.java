package com.example.tify.Hyeona.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Minwoo.Activity.OrderListActivity;
import com.example.tify.R;
//해당 페이지는 준비중입니다 띄울 예정
public class qrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_activity_qr);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //액션바 삭제
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(qrActivity.this, JiseokMainActivity.class);
        startActivity(intent);
    }
}