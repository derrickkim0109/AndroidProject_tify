package com.example.tify.Taehyun.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tify.R;

public class Mypage_CardRegistrationActivity extends AppCompatActivity {

    //처음 결제카드 등록 구간.
    FrameLayout card_enroll_FL;
    //카드 등록 누르는곳.
    LinearLayout card_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_card_registration);
        init();

    }

    private void init() {
        card_enroll_FL = findViewById(R.id.card_enroll_FL);
        card_btn = findViewById(R.id.card_btn);

        card_btn.setOnClickListener(mClickListener);

    }
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.card_btn:
                    card_enroll_FL.setVisibility(View.INVISIBLE);

                    break;

            }
        }
    };

}//END