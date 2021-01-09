package com.example.tify.Hyeona.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Bean.Bean_review_store;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_review;
import com.example.tify.R;

public class review_white extends AppCompatActivity {
    LinearLayout ll_hide;
    InputMethodManager inputMethodManager ;
    private Context context;
    private int sSeqNo;
    //데이터상 storekeeper_skSeqNo 임 확인필수
    private int uNo;
    private String macIP = "192.168.0.55";
    private String urlAddr = "http://" + macIP + ":8080/tify/review_white_storeinfo.jsp?";
    private Bean_review_store bean_review_store = new Bean_review_store();
    private String sName;
    private String sImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_activity_review_white);
        Intent intent = getIntent();
        uNo = intent.getIntExtra("uNo",0);
        sSeqNo = intent.getIntExtra("sSeqNo",0);
        Log.v("다이얼로그", "ㅇ" + uNo);
        Log.v("다이얼로그", "ㅇ" + sSeqNo);


        final Button review_white_complete = findViewById(R.id.review_white_complete);
        final ImageView review_add_image = findViewById(R.id.review_add_image);
        final ImageView review_cancel = findViewById(R.id.review_cancel);
        final EditText review_content = findViewById(R.id.review_content);

        review_white_complete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //키보드 화면 터치시 숨기기위해 선언.
        ll_hide = findViewById(R.id.detail_ll_hide);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  //OS에서 지원해주는 메소드이다.

        //키보드 화면 터치시 숨김.
        ll_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(ll_hide.getWindowToken(),0);
            }
        });

    }
        @Override
        protected void onResume() {
            super.onResume();
            connectGetData();
        }
        private void connectGetData(){
            try {
                urlAddr = urlAddr+"storekeeper_skSeqNo="+sSeqNo;
                Log.v("이거다",urlAddr);
                CUDNetworkTask_review mCUDNetworkTask_review = new CUDNetworkTask_review(review_white.this, urlAddr,"select_review_storeinfo");
                Object obj = mCUDNetworkTask_review.execute().get();
                bean_review_store = (Bean_review_store) obj;

                //받아온 정보

                final ImageView review_store_image = findViewById(R.id.review_store_image);
                sImage = bean_review_store.getsImage();
                Glide.with(this).load("http://" + macIP + ":8080/tify/"+ sImage).into(review_store_image);

                final TextView review_store_name = findViewById(R.id.review_store_name);
                sName = bean_review_store.getsName();
                review_store_name.setText(sName);

            }catch (Exception e){
                e.printStackTrace();
            }
        };

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
        title.setText("리뷰작성");
        ImageButton cart = findViewById(R.id.cart);
        cart.setVisibility(View.GONE);
//         장바구니 없애려면 위에거 살리면 됨
        //액션바 양쪽 공백 없애기
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        return true;
    }

}