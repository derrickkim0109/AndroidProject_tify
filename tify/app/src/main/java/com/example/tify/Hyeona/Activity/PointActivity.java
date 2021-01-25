package com.example.tify.Hyeona.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tify.Hyeona.Adapter.pointHistory_adapter;
import com.example.tify.Hyeona.Adapter.review_adapter;
import com.example.tify.Hyeona.Bean.Bean_point_history;
import com.example.tify.Hyeona.Bean.Bean_review_review;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_reward;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_stampCount;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Minwoo.Activity.OrderListActivity;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Activity.MypageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PointActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    Menu menu;


    private pointHistory_adapter adapter;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;
    private ArrayList<Bean_point_history> point_history = null;


    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

    TextView userName;


    private int user_uNo;
    private String user_uName;
    // 유저 넘버 받아와야함!!
    private int pointCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_activity_point);
        recyclerView = findViewById(R.id.point_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //액션바 삭제

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        user_uNo = auto.getInt("userSeq",0);
        user_uName= auto.getString("userNickName",null);

        userName = findViewById(R.id.user_name);
        userName.setText(user_uName + "님의");




        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //하단 네비게이션 선언
        menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) { //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.//
                    case R.id.action1: {
                        Intent intent = new Intent(PointActivity.this, JiseokMainActivity.class);
                        startActivity(intent);
                        //홈버튼
                        return true;
                    }
                    case R.id.action2: {
                        //주문내역
                        Intent intent = new Intent(PointActivity.this, OrderListActivity.class);
                        intent.putExtra("from", "JiseokMainActivity"); // value에 어디서 보내는지를 적어주세요
                        startActivity(intent);
                        finish();

                        return true;
                    }
                    case R.id.action3: {
                        Intent intent = new Intent(PointActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸 여기는 즐겨찾기 부분
                        return true;
                    }

                    case R.id.action4: {
                        Intent intent = new Intent(PointActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸

                        return true;
                    }
                    case R.id.action5: {
                        //마이페이지
                        Intent intent = new Intent(PointActivity.this, MypageActivity.class);
                        startActivity(intent);

                        return true;
                    }
                    default:
                        return false;
                }
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        connectGetData(user_uNo);
        pointCount = connectGetData2(user_uNo);

        //숫자에다가 점찍어서 출력
        NumberFormat numf = NumberFormat.getInstance(Locale.getDefault());
        String total = numf.format(pointCount);

        TextView how_point = findViewById(R.id.how_point);
        how_point.setText(total+"P");
    }


    //여기에서 해당 고객의 리워드 히스토리를 띄워야함
    private void connectGetData(int s){
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/pointHistory_select.jsp?";
            //여기 변경 포인트
            String urlAddress = urlAddr + "uNo=" + s;
            CUDNetworkTask_reward CUDNetworkTask_reward = new CUDNetworkTask_reward(urlAddress,"pointHistory_select");
            Object obj = CUDNetworkTask_reward.execute().get();
            point_history = (ArrayList<Bean_point_history>) obj;


            recyclerView.setHasFixedSize(true);
            //레이아웃 매니저 만들기
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new pointHistory_adapter(PointActivity.this, R.layout.cha_point_history,user_uNo,point_history);
            //어댑터에게 보내기
            recyclerView.setAdapter(adapter);


        }catch (Exception e){
            e.printStackTrace();
        }
    }





    //여기에서 포인트 얼마나 있는지 겟해온다 result 형태로 받아옴 어차피 값 하나...
    private int connectGetData2(int s){
        int result =0;
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/point_select.jsp?";
            String urlAddress = urlAddr + "uNo=" + s;
            CUDNetworkTask_reward CUDNetworkTask_reward = new CUDNetworkTask_reward(urlAddress,"select_point");
            Object obj = CUDNetworkTask_reward.execute().get();
            result = (int)obj;


        }catch (Exception e){
            e.printStackTrace();
        }return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}