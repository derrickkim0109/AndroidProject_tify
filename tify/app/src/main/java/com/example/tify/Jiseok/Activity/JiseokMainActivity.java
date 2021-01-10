package com.example.tify.Jiseok.Activity;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
=======
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
>>>>>>> 8b41972bfdec32b53d8743a9c08520eb943c912a
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Adapter.review_adapter;
import com.example.tify.Hyeona.Bean.Bean_review_review;
import com.example.tify.Minwoo.Activity.OrderListActivity;
import com.example.tify.R;
import com.example.tify.Taehyun.Activity.MypageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class JiseokMainActivity extends AppCompatActivity {
    LinearLayout ll_hide;
    InputMethodManager inputMethodManager ;

    BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    Menu menu;

    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjs_activity_jiseok_main);

<<<<<<< HEAD
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // 상단 액션바 삭제
        ImageView gps_setting = findViewById(R.id.gps_setting);
        Glide.with(this).load(R.drawable.gps_setting).into(gps_setting);

        //////////////////////////////////////////////////////////////////////////////////

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //하단 네비게이션 선언
        menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) { //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.//
                    case R.id.action1: {
                        Intent intent = new Intent(JiseokMainActivity.this, JiseokMainActivity.class);
                        startActivity(intent);
                        //홈버튼
                        return true;
                    }
                    case R.id.action2: {
                    //주문내역
                        Intent intent = new Intent(JiseokMainActivity.this, OrderListActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                    case R.id.action3: {
                    //즐겨찾기
                        return true;
                    }
                    case R.id.action4: {
                     //큐알결제
                        return true;
                    }
                    case R.id.action5: {
                    //마이페이지
                        Intent intent = new Intent(JiseokMainActivity.this, MypageActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                    default:
                        return false;
                }
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
}
=======

    }


    //뒤로가기버튼
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
>>>>>>> 8b41972bfdec32b53d8743a9c08520eb943c912a
