package com.example.tify.Minwoo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Activity.reviewActivity;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Minwoo.Fragment.InfoFragment;
import com.example.tify.Minwoo.Fragment.MenuFragment;
import com.example.tify.Minwoo.Fragment.ReviewFragment;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.google.android.material.tabs.TabLayout;

public class StoreInfoActivity extends AppCompatActivity {

    // 매장 정보, 메뉴, 리뷰 화면

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
    int skStatus;
    String userName;
    String sTel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_storeinfo);

        // 기본 설정 값
        Intent intent = getIntent();
        ShareVar shareVar = new ShareVar();
        macIP = shareVar.getMacIP();

        if(intent.getStringExtra("from") == null){ // 매장 검색화면에서 온 경우
            store_sSeqNo = Integer.parseInt(intent.getStringExtra("skSeqNo"));
            sImage = intent.getStringExtra("sImage");
            sAddress = intent.getStringExtra("sAddress");
            sName = intent.getStringExtra("sName");
            sTime = intent.getStringExtra("sTime");
            sTelNo = intent.getStringExtra("sTelNo");
            sPackaging = Integer.parseInt(intent.getStringExtra("sPackaging"));
            sComment = intent.getStringExtra("sComment");
            skStatus = Integer.parseInt(intent.getStringExtra("skStatus"));
        }else{ // 그 외의 경우
            SharedPreferences sharedPreferences = getSharedPreferences("storeInfo", MODE_PRIVATE);
            store_sSeqNo = sharedPreferences.getInt("skSeqNo", 0);
            sImage = sharedPreferences.getString("sImage", null);
            sAddress = sharedPreferences.getString("sAddress", null);
            sName = sharedPreferences.getString("sName", null);
            sTime = sharedPreferences.getString("sTime", null);
            sTelNo = sharedPreferences.getString("sTelNo", null);
            sPackaging = sharedPreferences.getInt("sPackaging", 0);
            sComment = sharedPreferences.getString("sComment", null);
            skStatus = sharedPreferences.getInt("skStatus", 0);
        }

        Log.v(TAG, "sPackaging : " + sPackaging);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        user_uSeqNo = auto.getInt("userSeq",0);
        userNickName = auto.getString("userNickName",null);

        SharedPreferences sharedPreferences = getSharedPreferences("storeInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("skSeqNo", store_sSeqNo);
        editor.putString("sImage", sImage);
        editor.putString("sAddress", sAddress);
        editor.putString("sName", sName);
        editor.putString("sTime", sTime);
        editor.putString("sTelNo", sTelNo);
        editor.putString("sComment", sComment);
        editor.putInt("sPackaging", sPackaging);
        editor.putInt("skStatus", skStatus);
        editor.commit();

        // 가게사진, 가게이름, 가게주소
        iv_sPhoto = findViewById(R.id.activity_StoreInfo_IV_sPhoto);
        tv_sName = findViewById(R.id.activity_StoreInfo_TV_sName);
        tv_sAddress = findViewById(R.id.activity_StoreInfo_TV_sAddress);

        tv_sAddress.setText(sAddress);
        tv_sName.setText(sName);
        sendImageRequest(sImage);
        Log.v(TAG, "sImage : " + sImage);

        TabLayout tabs = findViewById(R.id.tabs);

        menuFragment = new MenuFragment();
        infoFragment = new InfoFragment();
        reviewFragment = new ReviewFragment();

        // MenuFragment로 넘긴다.
        Bundle bundle = new Bundle();
        bundle.putString("macIP", macIP);
        bundle.putString("sName", sName);
        bundle.putInt("user_uSeqNo", user_uSeqNo);
        bundle.putInt("store_sSeqNo", store_sSeqNo);
        bundle.putInt("skStatus", skStatus);

        bundle.putString("sAddress", sAddress);
        bundle.putString("sName", sName);
        bundle.putString("sTime", sTime);
        bundle.putString("sTelNo", sTelNo);
        bundle.putString("sComment", sComment);
        bundle.putString("sAddress", sAddress);
        bundle.putInt("sPackaging", sPackaging);

        menuFragment.setArguments(bundle);
        infoFragment.setArguments(bundle);
        reviewFragment.setArguments(bundle);
        com.example.tify.Hyeona.Activity.reviewActivity reviewActivity = new reviewActivity();
        reviewActivity.setArguments(bundle);

        //-----------------------------

        getSupportFragmentManager().beginTransaction().add(R.id.frame, menuFragment).commit(); // 초기 시작 프래그먼트

        tabs.addTab(tabs.newTab().setText("메뉴"));
        tabs.addTab(tabs.newTab().setText("정보"));
        tabs.addTab(tabs.newTab().setText("리뷰"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { // 탭 레이아웃 설정
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {
                    selected = menuFragment;
                } else if (position == 1) {
                    selected = infoFragment;
                } else if (position == 2) {
                    selected = reviewActivity;
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

    }

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
        title.setText("매장정보");

        ImageButton cart = findViewById(R.id.cart);
        cart.setVisibility(View.VISIBLE);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StoreInfoActivity.this, CartActivity.class);
                intent.putExtra("sName", sName); // sName 넣어주기
                intent.putExtra("macIP", macIP); // sName 넣어주기
                intent.putExtra("user_uSeqNo", user_uSeqNo); // sName 넣어주기
                intent.putExtra("store_sSeqNo", store_sSeqNo); // sName 넣어주기
                startActivity(intent);
            }
        });


//         장바구니 없애려면 위에거 살리면 됨
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(StoreInfoActivity.this, JiseokMainActivity.class);
              startActivity(intent);

            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        return true;
    }

    // 이미지
    private void sendImageRequest(String s) {

        String url = "http://" + macIP + ":8080/tify/" + s;
        Log.v(TAG, "ImageUrl : " + url);
        Glide.with(this).load(url).into(iv_sPhoto);
    }
}