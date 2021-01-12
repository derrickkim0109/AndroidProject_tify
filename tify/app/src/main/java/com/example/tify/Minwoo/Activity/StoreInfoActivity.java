package com.example.tify.Minwoo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tify.Minwoo.Fragment.InfoFragment;
import com.example.tify.Minwoo.Fragment.MenuFragment;
import com.example.tify.Minwoo.Fragment.ReviewFragment;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.google.android.material.tabs.TabLayout;

public class StoreInfoActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    Fragment menuFragment, infoFragment, reviewFragment;

    ImageView iv_sPhoto;
    TextView tv_sName;
    TextView tv_sAddress;

    String macIP;
    String urlAddr = null;
    String where = null;
    int user_uSeqNo = 0;
    String userNickName = null;

    int store_sSeqNo = 0;
    String sName;
    String userEmail;
    String sAddress;
    String sImage;
    String sTime = null;
    String sTelNo;
    int sPackaging;
    String sComment;
    int skSeqNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_storeinfo);

        // 기본 설정 값 (지석씨에게 받는다)
//        Intent intent = getIntent();
//        macIP = intent.getStringExtra("macIP");
//        userName = intent.getIntExtra("userName", 0);
//        store_sSeqNo = intent.getIntExtra("store_sSeqNo", 0);
//        user_uSeqNo = intent.getIntExtra("user_uSeqNo", 0);
//        sAddress = intent.getStringExtra("sAddress");
//        sName = intent.getStringExtra("sName");


        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        ShareVar shareVar = new ShareVar();
        macIP = shareVar.getMacIP();

//        user_uSeqNo = auto.getString("userSeq",null); // 지석씨랑 화면 연결되면 쓰기
//        userNickName = auto.getString("userNickName",null); // 지석씨랑 화면 연결되면 쓰기

        user_uSeqNo = 2;
        userNickName = "이민우";
        store_sSeqNo = 1;
        sName = "스타벅스 강남점";
        sAddress = "서울 강남";

        // 가게사진, 가게이름, 가게주소
        iv_sPhoto = findViewById(R.id.activity_StoreInfo_IV_sPhoto);
        tv_sName = findViewById(R.id.activity_StoreInfo_TV_sName);
        tv_sAddress = findViewById(R.id.activity_StoreInfo_TV_sAddress);

        //지석씨에게 인텐트로 받는다.
//        iv_sPhoto.setImageResource();
//        tv_sName.setText();
//        tv_sAddress.setText();

        //-----------------------------

        TabLayout tabs = findViewById(R.id.tabs);

        menuFragment = new MenuFragment();
        infoFragment = new InfoFragment();
        reviewFragment = new ReviewFragment();

        // MenuFragment로 넘긴다. (지석씨에게 값 받으면 다시 설정해주자)
        Bundle bundle = new Bundle();
        bundle.putString("macIP", macIP);
        bundle.putString("sName", sName);
        bundle.putInt("user_uSeqNo", user_uSeqNo);
        bundle.putInt("store_sSeqNo", store_sSeqNo);

        menuFragment.setArguments(bundle);
        infoFragment.setArguments(bundle);
        reviewFragment.setArguments(bundle);

        //-----------------------------

        getSupportFragmentManager().beginTransaction().add(R.id.frame, menuFragment).commit(); // 초기 시작 프래그먼트

        tabs.addTab(tabs.newTab().setText("메뉴"));
        tabs.addTab(tabs.newTab().setText("정보"));
        tabs.addTab(tabs.newTab().setText("리뷰"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if(position == 0){
                    selected = menuFragment;
                }else if (position == 1){
                    selected = infoFragment;
                }else if (position == 2) {
                    selected = reviewFragment;
                }
                Log.v(TAG, "구간6---");
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.storeinfo_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()){
                    case R.id.toolbar_cart: // 이동할 때 해당 아이디 seqno 넘기기?
                        intent = new Intent(StoreInfoActivity.this, CartActivity.class);
                        intent.putExtra("sName", sName); // sName 넣어주기
                        intent.putExtra("macIP", macIP); // sName 넣어주기
                        intent.putExtra("user_uSeqNo", user_uSeqNo); // sName 넣어주기
                        intent.putExtra("store_sSeqNo", store_sSeqNo); // sName 넣어주기
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 툴바 장바구니

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Log.v(TAG, "test");
//                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}