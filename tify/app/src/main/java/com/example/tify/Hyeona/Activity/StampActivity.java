package com.example.tify.Hyeona.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Hyeona.Adapter.review_adapter;
import com.example.tify.Hyeona.Adapter.stampOrder_adapter;
import com.example.tify.Hyeona.Bean.Bean_reward_stamphistory;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_stampCount;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Minwoo.Activity.OrderListActivity;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Activity.MypageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StampActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    Menu menu;

    int user_uNo = 1;
    String user_uName = "지돌지돌";
    // 인텐트로 받아와야함

    TextView user_name;
    private ArrayList<Bean_reward_stamphistory> stamphistory = null;
    private stampOrder_adapter adapter;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;
    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

    String urlAddr = "http://" + MacIP + ":8080/tify/stamp_count.jsp?";
    String urlAddr2 = "http://" + MacIP + ":8080/tify/stamp_update.jsp?";
    String urlAddr3 = "http://" + MacIP + ":8080/tify/rewardhistory_insert.jsp?";
    String urlAddr4 = "http://" + MacIP + ":8080/tify/stamphistory.jsp?";
    int stamp_count;

    //총 10개의 스탬프를 배열로 넣을 예정
    ImageView[] img = new ImageView[10];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_activity_stamp);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        user_uNo = auto.getInt("userSeq",0);
        user_uName= auto.getString("userNickName",null);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //액션바 삭제

        recyclerView = findViewById(R.id.stamp_orderlist);
        recyclerView.setHasFixedSize(true);
        //레이아웃 매니저 만들기
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        user_name = findViewById(R.id.user_name);
        user_name.setText(user_uName+"님의");

        // 스탬프 각각을 배열에 넣음
        img[0]=findViewById(R.id.stamp01);
        img[1]=findViewById(R.id.stamp02);
        img[2]=findViewById(R.id.stamp03);
        img[3]=findViewById(R.id.stamp04);
        img[4]=findViewById(R.id.stamp05);
        img[5]=findViewById(R.id.stamp06);
        img[6]=findViewById(R.id.stamp07);
        img[7]=findViewById(R.id.stamp08);
        img[8]=findViewById(R.id.stamp09);
        img[9]=findViewById(R.id.stamp10);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //하단 네비게이션 선언
        menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) { //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.//
                    case R.id.action1: {
                        Intent intent = new Intent(StampActivity.this, JiseokMainActivity.class);
                        startActivity(intent);
                        //홈버튼
                        return true;
                    }
                    case R.id.action2: {
                        //주문내역
                        Intent intent = new Intent(StampActivity.this, OrderListActivity.class);
                        intent.putExtra("from", "JiseokMainActivity"); // value에 어디서 보내는지를 적어주세요
                        startActivity(intent);
                        finish();

                        return true;
                    }
                    case R.id.action3: {
                        Intent intent = new Intent(StampActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸 여기는 즐겨찾기 부분
                        return true;
                    }

                    case R.id.action4: {
                        Intent intent = new Intent(StampActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸

                        return true;
                    }
                    case R.id.action5: {
                        //마이페이지
                        Intent intent = new Intent(StampActivity.this, MypageActivity.class);
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
        connectGetData2();

        stamp_count = connectGetData();
        Log.v("테스트","ㅇㅇ"+stamp_count);
        //스탬프 관련 작업은 여기서!!
        //stamp_count = 11;
        // 스탬프 수 만큼 색칠
        TextView total_stamp = findViewById(R.id.total_stamp);
        total_stamp.setText("Total : " + stamp_count);
        if(stamp_count<10) {
            for (int i = 0; i < stamp_count; i++) {
                img[i].setImageResource(R.drawable.stamp00);
            }

        }else if(stamp_count>=10){
            for (int i = 0; i < 10; i++) {
                img[i].setImageResource(R.drawable.stamp00);
                if(i == 9){
                    img[9].setImageResource(R.drawable.stamp03);
                }
            }
            //10개 이상 모았을 때 클릭 이벤트 발생
            // 여기서 포인트는 +1000 시키고, 스탬프는 -10 시킨다.
            img[9].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(StampActivity.this)
                            .setTitle("스탬프를 포인트로 교체하겠습니까?")
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(StampActivity.this, "스탬프는 포인트로 교환해야만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   int point_result = connectUpdateData();
                                   int point_result2 = connectInsertData();
                                   Log.v("적립 내역 확인","ㅇㅇ"+point_result2);
                                   if (point_result ==1){
                                       Toast.makeText(StampActivity.this, "포인트가 적립되었습니다.", Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(StampActivity.this, PointActivity.class);
                                       intent.putExtra("user_uNo",user_uNo);
                                       //  포인트 창에 값 전달
                                       startActivity(intent);
                                       //finish();
                                   }else{
                                       Toast.makeText(StampActivity.this, "에러발생", Toast.LENGTH_SHORT).show();
                                   }
                                }
                            })
                            .show();
                }
            });

        }
    }

    private int connectUpdateData() {
       int result = 0;
        try {
            // 스탬프 갯수 채운사람한테 포인트로 업데이트 해줌
            String urlAddress2 = urlAddr2 + "user_uNo=" + user_uNo;
            CUDNetworkTask_stampCount CUDNetworkTask_stampCount =  new CUDNetworkTask_stampCount(urlAddress2,"update_stamp");
            Object obj = CUDNetworkTask_stampCount.execute().get();
            result = (int) obj;

        }catch (Exception e){
            e.printStackTrace();
        }return result;
    }

    private int connectInsertData() {
        int result = 0;
        try {
            String rhContent = "스탬프적립";
            int rhChoice = 1; // 이 경우는 적립이니까
            String rhPointHow = "1,000p";

            // 스탬프로 인한 적립 히스토리 저장
            String urlAddress3 = urlAddr3 + "user_uNo=" + user_uNo +"&rhContent="+rhContent +"&rhChoice="+rhChoice+"&rhPointHow="+rhPointHow;
            CUDNetworkTask_stampCount CUDNetworkTask_stampCount =  new CUDNetworkTask_stampCount(urlAddress3,"insert_stamp");
            Object obj = CUDNetworkTask_stampCount.execute().get();
            result = (int) obj;

        }catch (Exception e){
            e.printStackTrace();
        }return result;
    }

    private int connectGetData(){
        int result =0;
        try {
            //  여기서 기본적으로 스탬프 갯수 및 유저 정보를 받아온다

            String urlAddress = urlAddr + "user_uNo=" + user_uNo;
            CUDNetworkTask_stampCount CUDNetworkTask_stampCount = new CUDNetworkTask_stampCount(urlAddress,"select_stamp");
            Object obj = CUDNetworkTask_stampCount.execute().get();
            result = (int)obj;

            Log.v("테스트","ㅇㅇ"+result);

        }catch (Exception e){
            e.printStackTrace();
        }return result;
    }
    private void connectGetData2(){
        try {
            String urlAddress = urlAddr4 + "user_uNo=" + user_uNo;
            CUDNetworkTask_stampCount CUDNetworkTask_stampCount = new CUDNetworkTask_stampCount(urlAddress,"select_orderhistory");
            Object obj = CUDNetworkTask_stampCount.execute().get();

            stamphistory = (ArrayList<Bean_reward_stamphistory>) obj;
            Log.v("dddd","dd"+stamphistory);

            //리사이클러뷰 어댑터를 넣기
            adapter = new stampOrder_adapter(StampActivity.this, R.layout.cha_reviewcontent, stamphistory);
            //어댑터에게 보내기
            recyclerView.setAdapter(adapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}