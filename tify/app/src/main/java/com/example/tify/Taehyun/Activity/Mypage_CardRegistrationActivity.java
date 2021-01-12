package com.example.tify.Taehyun.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Hyeona.Activity.PointActivity;
import com.example.tify.Hyeona.Adapter.pointHistory_adapter;
import com.example.tify.Hyeona.Bean.Bean_point_history;
import com.example.tify.R;
import com.example.tify.Taehyun.Adapter.Mypage_CardInfoAdapter;
import com.example.tify.Taehyun.Adapter.Mypage_CardListAdapter;
import com.example.tify.Taehyun.Bean.Bean_Mypage_CardInfo;
import com.example.tify.Taehyun.Bean.Bean_Mypage_cardlist;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_CardRecycleView_Taehyun;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_RecycleView_Taehyun;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;

import java.util.ArrayList;

public class Mypage_CardRegistrationActivity extends AppCompatActivity {

    //field
    final static private String TAG = "Mypage_CardRegistrationActivity";
    private Mypage_CardInfoAdapter cardInfoAdapter;
    private Mypage_CardListAdapter cardListAdapter;
    private RecyclerView recyclerView = null;

    private RecyclerView.LayoutManager layoutManager = null;
    private ArrayList<Bean_Mypage_CardInfo> bean_mypage_cardInfos = null;

    private RecyclerView recyclerView_card_image = null;
    private ArrayList<Bean_Mypage_cardlist> bean_mypage_cardlists = null;


    //결제 등록 버튼  - 처음에 뜨는 장면
    ImageView card_firstbtn, cardadd_btn;
    Intent intent;
    LinearLayout card_firstll,cardRG_ll_list;
    //화면 전환을 위한 변수


    String macIP = null;
    int uNo = 0;
    ActionBar actionBar = null;
    final static int RValue = 0;
    //카드 유무 파악
    int cardcount = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_card_registration);
        init();
        //상속
        intent = getIntent();
        inheritance();

        //카드등록 구간 없애기 위해
        cardview_enroll();
    }

    private void cardview_enroll() {
        card_firstll = findViewById(R.id.card_firstll);
        cardRG_ll_list = findViewById(R.id.cardRG_ll_list);

//        if (cardcount == 0){
//            card_firstll.setVisibility(View.VISIBLE);
//            cardRG_ll_list.setVisibility(View.INVISIBLE);
//        }else {

            card_firstll.setVisibility(View.INVISIBLE);
            cardRG_ll_list.setVisibility(View.VISIBLE);
//        }
    }

    //XML아이디 선언. Button Listener 선언  - 태현 2020.01.10
    private void init() {
        //결제 수단 등록
        card_firstbtn = findViewById(R.id.card_firstbtn);
        cardadd_btn = findViewById(R.id.cardadd_btn);

        card_firstbtn.setOnClickListener(mClickListener);
        cardadd_btn.setOnClickListener(mClickListener);

        //back버튼
        actionBar = getSupportActionBar();
        recyclerView = findViewById(R.id.card_name_delete);
        recyclerView_card_image = findViewById(R.id.card_cardListView);

    }

    private void inheritance() {

        //IP
        macIP = intent.getStringExtra("macIP");
        cardcount = intent.getIntExtra("cardcount",0);
        //보낼 data
        uNo = intent.getIntExtra("uNo",0);
    }


    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.cardadd_btn:
                    intent = new Intent(Mypage_CardRegistrationActivity.this,Mypage_CardDetailActivity.class)
                            .putExtra("uNo",uNo)
                            .putExtra("macIP",macIP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                    break;

                case R.id.card_firstbtn:

                    intent = new Intent(Mypage_CardRegistrationActivity.this,Mypage_CardDetailActivity.class)
                            .putExtra("uNo",uNo)
                            .putExtra("macIP",macIP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                    break;

            }
        }
    };

    //액션바에 Back 버튼 만들기
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back by clicking back button of actionbar
        return super.onSupportNavigateUp();
    }

    //액션바에 등록하기 버튼 만들기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mypage_card_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_card_enroll:
                intent = new Intent(Mypage_CardRegistrationActivity.this,Mypage_CardDetailActivity.class)
                        .putExtra("uNo",uNo)
                        .putExtra("macIP",macIP);;
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //카드 이미지
        connectCardView(uNo);
        //카드 리스트
        connectData(uNo);
    }



    private void connectCardView(int s){

        try {

            String urlAddr = "http://" + macIP + ":8080/tify/mypage_card_view_select.jsp?";
            String urlAddress = urlAddr + "user_uNo=" + s;
            Log.v("dddd",urlAddress);

            NetworkTask_CardRecycleView_Taehyun networkTask_taeHyun = new NetworkTask_CardRecycleView_Taehyun(urlAddress,"select_cardList");
            Object obj = networkTask_taeHyun.execute().get();

            bean_mypage_cardlists = (ArrayList<Bean_Mypage_cardlist>) obj;

            recyclerView_card_image.setHasFixedSize(true);

            //레이아웃 매니저 만들기            //가로 레이아웃  - 카드 그림
            layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            recyclerView_card_image.setLayoutManager(layoutManager);
            cardListAdapter = new Mypage_CardListAdapter(Mypage_CardRegistrationActivity.this, R.layout.kth_activity_mypage_cardimagelist,bean_mypage_cardlists,macIP);

            //어댑터에게 보내기
            recyclerView_card_image.setAdapter(cardListAdapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void connectData(int s){

        try {

            String urlAddr = "http://" + macIP + ":8080/tify/mypage_card_info_select.jsp?";
            String urlAddress = urlAddr + "user_uNo=" + s ;

            NetworkTask_RecycleView_Taehyun networkTask_taeHyun = new NetworkTask_RecycleView_Taehyun(urlAddress,"select_cardInfo");
            Object obj = networkTask_taeHyun.execute().get();
            bean_mypage_cardInfos = (ArrayList<Bean_Mypage_CardInfo>) obj;

            recyclerView.setHasFixedSize(true);
            //레이아웃 매니저 만들기
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            cardInfoAdapter = new Mypage_CardInfoAdapter(Mypage_CardRegistrationActivity.this, R.layout.kth_activity_cardinfo_list,bean_mypage_cardInfos,macIP);
            //어댑터에게 보내기
            recyclerView.setAdapter(cardInfoAdapter);

        }catch (Exception e){

            e.printStackTrace();

        }

    }
}//END