package com.example.tify.Minwoo.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_storeinfo);

        TabLayout tabs = findViewById(R.id.tabs);

        menuFragment = new MenuFragment();
        infoFragment = new InfoFragment();
        reviewFragment = new ReviewFragment();

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