package com.example.tify.Taehyun.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Adapter.Mypage_CardInfoAdapter;
import com.example.tify.Taehyun.Adapter.Mypage_CardListAdapter;
import com.example.tify.Taehyun.Bean.Bean_Mypage_CardInfo;
import com.example.tify.Taehyun.Bean.Bean_Mypage_cardlist;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_RecycleView_CardInfo;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_RecycleView_CardView;

import java.util.ArrayList;

public class Mypage_CardRegistrationActivity extends AppCompatActivity {

    //field
    final static private String TAG = "Mypage_CardRegistrationActivity";
    //카드 정보 Adapter
    private Mypage_CardInfoAdapter cardInfoAdapter;
    //카드 리스트 Adapter--> 내가 등록한 카드들
    private Mypage_CardListAdapter cardListAdapter;
    //카드 정보를 위해
    private RecyclerView recyclerView = null;
    //LayoutManager
    private RecyclerView.LayoutManager layoutManager = null;
    //카드정보 ArrayList
    private ArrayList<Bean_Mypage_CardInfo> bean_mypage_cardInfos = null;
    //카드 이미지 불러오기 위해
    private RecyclerView recyclerView_card_image = null;
    //카드 이미지 ArrayList
    private ArrayList<Bean_Mypage_cardlist> bean_mypage_cardlists = null;


    //결제 등록 버튼  - 처음에 뜨는 장면
    Intent intent;
    //화면 전환을 위한 변수


    //IP
    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();
    Button card_RG_updatebtn;

    int uNo = 0;
    ActionBar actionBar = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_card_registration);
        init();
        //상속
        intent = getIntent();
        inheritance();


    }


    //XML아이디 선언. Button Listener 선언  - 태현 2020.01.10
    private void init() {
        //결제 수단 등록
        card_RG_updatebtn = findViewById(R.id.card_RG_updatebtn);
        card_RG_updatebtn.setOnClickListener(mClickListener);
        //back버튼
        actionBar = getSupportActionBar();
        recyclerView = findViewById(R.id.card_name_delete);
        recyclerView_card_image = findViewById(R.id.card_cardListView);

    }
    //결제 카드 등록하러 가기
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(Mypage_CardRegistrationActivity.this,Mypage_CardDetailActivity.class)
                    .putExtra("MacIP",MacIP)
                    .putExtra("uNo",uNo);
            startActivity(intent);
        }
    };

    private void inheritance() {
        uNo = intent.getIntExtra("uNo",0);
    }


    //액션바에 Back 버튼 만들기
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back by clicking back button of actionbar
        return super.onSupportNavigateUp();
    }

    //액션바에 등록하기 버튼 만들기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayHomeAsUpEnabled(false);
        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowTitleEnabled(false);
        //홈 아이콘을 숨김처리합니다.
        actionBar.setDisplayShowHomeEnabled(false);

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.cha_custom_actionbar, null);

        actionBar.setCustomView(actionbar);
        TextView title = findViewById(R.id.title);
        title.setText("내 카드보기");

        ImageButton cart = findViewById(R.id.cart);
        cart.setVisibility(View.VISIBLE);
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
                Intent intent4 = new Intent(Mypage_CardRegistrationActivity.this,MypageActivity.class);
                intent.putExtra("uNo",uNo);
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        //카드 이미지
        connectCardView(uNo);
        //카드 리스트
        connectData(uNo);
    }
    ////////////////////////////////////////////////////////////
    //                                                        //
    //              //   2021.01.07 -태현
    //               카드 이미지 RecyclerView 위한 Connector
    ////////////////////////////////////////////////////////////


    @SuppressLint("LongLogTag")
    private void connectCardView(int s){

        try {

            String urlAddr = "http://" + MacIP + ":8080/tify/mypage_card_list_select.jsp?";
            String urlAddress = urlAddr + "user_uNo=" + s;

            // 카드 정보를 불러오기 위한 NetworkTask
            NetworkTask_RecycleView_CardView networkTask_taeHyun = new NetworkTask_RecycleView_CardView(urlAddress,"select_cardList");
            Object obj = networkTask_taeHyun.execute().get();

            // bean_cardlist 연결
            bean_mypage_cardlists = (ArrayList<Bean_Mypage_cardlist>) obj;
            Log.v(TAG,"빈 : "+bean_mypage_cardlists);
            recyclerView_card_image.setHasFixedSize(true);

            //레이아웃 매니저 만들기            //가로 레이아웃  - 카드 그림
            layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            recyclerView_card_image.setLayoutManager(layoutManager);
            cardListAdapter = new Mypage_CardListAdapter(Mypage_CardRegistrationActivity.this, R.layout.kth_activity_mypage_cardimagelist,bean_mypage_cardlists,MacIP,uNo);

            //어댑터에게 보내기
            recyclerView_card_image.setAdapter(cardListAdapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    ////////////////////////////////////////////////////////////
    //                                                        //
    //              //   2021.01.07 -태현
    //               카드 이미지 RecyclerView 위한 Connector
    ////////////////////////////////////////////////////////////

    @SuppressLint("LongLogTag")
    private void connectData(int s){

        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/mypage_card_info_select.jsp?";
            String urlAddress = urlAddr + "user_uNo=" + s ;

            NetworkTask_RecycleView_CardInfo networkTask_taeHyun = new NetworkTask_RecycleView_CardInfo(urlAddress,"select_cardInfo");
            Object obj = networkTask_taeHyun.execute().get();
            bean_mypage_cardInfos = (ArrayList<Bean_Mypage_CardInfo>) obj;

            recyclerView.setHasFixedSize(true);
            //레이아웃 매니저 만들기
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            cardInfoAdapter = new Mypage_CardInfoAdapter(Mypage_CardRegistrationActivity.this, R.layout.kth_activity_cardinfo_list,bean_mypage_cardInfos,MacIP,uNo);
            //어댑터에게 보내기
            recyclerView.setAdapter(cardInfoAdapter);

        }catch (Exception e){

            e.printStackTrace();

        }

    }

}//END