package com.example.tify.Taehyun.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tify.R;
import com.example.tify.ShareVar;

public class Mypage_NoCardActivity extends AppCompatActivity {
    ////////////////////////////////////////////////////////////
    //
    //              //   2021.01.07 -태현
    //               카드 등록 안되있을 경우 뜰 Activity
    ////////////////////////////////////////////////////////////

    //fields
    Intent intent;
    int uNo;
    //IP
    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

    ImageView nocard_firstbtn;
    Button nocard_cardadd_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_mypage__no_card);
        init();
        intent = getIntent();
        inheritance();

    }

    private void inheritance() {
        uNo = intent.getIntExtra("uNo",0);
    }

    private void init() {
        //
        nocard_firstbtn = findViewById(R.id.nocard_firstbtn);
        nocard_cardadd_btn = findViewById(R.id.nocard_cardadd_btn);

        nocard_firstbtn.setOnClickListener(mClickListener);
        nocard_cardadd_btn.setOnClickListener(mClickListener);
    }
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.nocard_firstbtn:
                    intent = new Intent(Mypage_NoCardActivity.this,Mypage_CardDetailActivity.class)
                            .putExtra("uNo",uNo)
                            .putExtra("MacIP",MacIP);
                    startActivity(intent);
                    break;
                case R.id.nocard_cardadd_btn:
                    intent = new Intent(Mypage_NoCardActivity.this,Mypage_CardDetailActivity.class)
                            .putExtra("uNo",uNo)
                            .putExtra("MacIP",MacIP);
                    startActivity(intent);
                    break;
            }

        }
    };


    //액션바에 등록하기 버튼 만들기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.cha_custom_actionbar, null);

        actionBar.setCustomView(actionbar);
        TextView title = findViewById(R.id.title);
        title.setText("카드등록");

        ImageButton cart = findViewById(R.id.cart);
        cart.setVisibility(View.INVISIBLE);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


//         장바구니 없애려면 위에거 살리면 됨
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

        }
        return super.onOptionsItemSelected(item);
    }

}