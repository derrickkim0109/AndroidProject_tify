package com.example.tify.Jiseok.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Activity.LoginActivity;
import com.example.tify.Hyeona.Activity.PointActivity;
import com.example.tify.Hyeona.Activity.StampActivity;
import com.example.tify.Jiseok.NetworkTask.CJS_NetworkTask;
import com.example.tify.Minwoo.Activity.OrderListActivity;
import com.example.tify.R;
import com.example.tify.Taehyun.Activity.MypageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class JiseokMainActivity extends AppCompatActivity {
    String MacIP = "192.168.219.100";
    LinearLayout ll_hide;
    InputMethodManager inputMethodManager;

    BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    Menu menu;
    ImageView gps_setting, imgStamp, imgPoint, imgRecommend, imgSearch;
    AutoCompleteTextView etSearch;


    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;
    String userEmail;
    String userSeq;
    String userNickName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjs_activity_jiseok_main);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        userEmail = auto.getString("userEmail", null);
        userSeq = auto.getString("userSeq", null);
        userNickName = auto.getString("userNickName", null);

        imgPoint = findViewById(R.id.main_img_point);
        imgStamp = findViewById(R.id.main_img_stamp);
        imgRecommend = findViewById(R.id.main_img_recommend);
        imgSearch = findViewById(R.id.main_img_SearchBtn);
        etSearch = findViewById(R.id.main_Edit_SearchText);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // 상단 액션바 삭제
        gps_setting = findViewById(R.id.main_img_gps);
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
                        Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.clear();
                                autoLogin.commit();
                                Intent intent = new Intent(JiseokMainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });

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
                inputMethodManager.hideSoftInputFromWindow(ll_hide.getWindowToken(), 0);
            }
        });


        imgStamp.setOnClickListener(imgClickListener);
        imgPoint.setOnClickListener(imgClickListener);
        imgRecommend.setOnClickListener(imgClickListener);

        gps_setting.setOnClickListener(mapListener);
        imgSearch.setOnClickListener(mapListener);


    }

    View.OnClickListener imgClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //스탬프버튼
                case R.id.main_img_stamp:
                    startActivity(new Intent(JiseokMainActivity.this, StampActivity.class));
                    break;
                //포인트버튼
                case R.id.main_img_point:
                    startActivity(new Intent(JiseokMainActivity.this, PointActivity.class));
                    break;
                case R.id.main_img_recommend:
                    break;
            }
        }
    };

    View.OnClickListener mapListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_img_gps:
                    break;

                case R.id.main_img_SearchBtn:
                    break;
            }
        }
    };




        private int selectUserSeq() {
            int utc = 0;
            try {
                String urlAddr = "http://" + MacIP + ":8080/tify/userSeqSelect.jsp?uEmail=" + userEmail;
                CJS_NetworkTask cjs_networkTask = new CJS_NetworkTask(JiseokMainActivity.this, urlAddr, "uNoSelect");
                Object obj = cjs_networkTask.execute().get();

                utc = (int) obj;
            } catch (Exception e) {

            }
            return utc;
        }



}






