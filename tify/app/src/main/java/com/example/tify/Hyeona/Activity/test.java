package com.example.tify.Hyeona.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tify.R;

public class test extends AppCompatActivity {
    Button review_white;
    // 가게 번호와 유저 넘버를 받아와야함
    int sSeqNo = 1;
    int uNo = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_test_review);

        review_white = findViewById(R.id.review_white);
        review_white.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            review_white_dialoge review_white_dialoge = new review_white_dialoge(test.this,sSeqNo,uNo);
            review_white_dialoge.callDialog();
            }
        };


    };
