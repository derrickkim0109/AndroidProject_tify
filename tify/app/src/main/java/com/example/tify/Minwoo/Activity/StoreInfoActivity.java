package com.example.tify.Minwoo.Activity;

import android.content.Intent;
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

import com.example.tify.Minwoo.Fragment.InfoFragment;
import com.example.tify.Minwoo.Fragment.MenuFragment;
import com.example.tify.Minwoo.Fragment.ReviewFragment;
import com.example.tify.R;
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
    int store_sSeqNo = 0;

    String sName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_storeinfo);




        // 기본 설정 값 (지석씨에게 받는다)
//        Intent intent = getIntent();
//        macIP = intent.getStringExtra("macIP");
//        userSeqno = intent.getIntExtra("userSeqno", 0);
//        store_sSeqNo = intent.getIntExtra("store_sSeqNo", 0);
//        sName = intent.getStringExtra("sName");

        // 가게사진, 가게이름, 가게주소
        iv_sPhoto = findViewById(R.id.activity_StoreInfo_IV_sPhoto);
        tv_sName = findViewById(R.id.activity_StoreInfo_TV_sName);
        tv_sAddress = findViewById(R.id.activity_StoreInfo_TV_sAddress);

        //지석씨에게 인텐트로 받는다.
//        sPhoto.setImageResource();
//        sName.setText();
//        sAddress.setText();
        //-----------------------------

        TabLayout tabs = findViewById(R.id.tabs);

        menuFragment = new MenuFragment();
        infoFragment = new InfoFragment();
        reviewFragment = new ReviewFragment();

        // MenuFragment로 넘긴다. (지석씨에게 값 받으면 다시 설정해주자)
        Bundle bundle = new Bundle();
        bundle.putString("macIP", "172.30.1.27");
        bundle.putString("sName", "스타벅스 강남점");
        bundle.putInt("userSeqno", 2);
        bundle.putInt("store_sSeqNo", 1);

        menuFragment.setArguments(bundle);

        // 테스트 값
        macIP = "172.30.1.27";
        sName = "스타벅스 강남점";
        user_uSeqNo = 2;
        store_sSeqNo = 1;
        //-----------------------------

        getSupportFragmentManager().beginTransaction().add(R.id.frame, menuFragment).commit(); // 초기 시작 프래그먼트

        tabs.addTab(tabs.newTab().setText("메뉴"));
        tabs.addTab(tabs.newTab().setText("정보"));
        tabs.addTab(tabs.newTab().setText("리뷰"));
        Log.v(TAG, "구간1---");

        Log.v(TAG, "구간2---");

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if(position == 0){
                    Log.v(TAG, "구간3---");
                    selected = menuFragment;
                }else if (position == 1){
                    Log.v(TAG, "구간4---");
                    selected = infoFragment;
                }else if (position == 2) {
                    Log.v(TAG, "구간5---");
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