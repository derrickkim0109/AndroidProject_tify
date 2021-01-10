package com.example.tify.Taehyun.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.tify.R;

public class CardRG2Activity extends AppCompatActivity {

    //field

    //카드 리스트뜰 레이아웃
    LinearLayout card_viewll;

    //카드 모양
    CardView cardView;
    //listview
    ListView card_cardListView;
    //카드 이름들 / 삭제 버튼 리스트뷰
    ListView card_name_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_card_r_g2);
        init();
    }

    private void init() {
        card_viewll = findViewById(R.id.card_viewll);

        cardView = findViewById(R.id.cardView);
        card_cardListView = findViewById(R.id.card_cardListView);
        card_name_delete = findViewById(R.id.card_name_delete);

        card_viewll.setOnClickListener(mClickListener);
        cardView.setOnClickListener(mClickListener);
        card_cardListView.setOnClickListener(mClickListener);
        card_name_delete.setOnClickListener(mClickListener);
    }
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}