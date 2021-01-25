package com.android.tify_store.Minwoo.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

import com.android.tify_store.Minwoo.Fragment.CompleteFragment;
import com.android.tify_store.Minwoo.Fragment.OrderRequestFragment;
import com.android.tify_store.Minwoo.Fragment.ProgressingFragment;
import com.android.tify_store.Minwoo.NetworkTask.LMW_LoginNetworkTask;
import com.android.tify_store.R;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    // 로그인 후 보여지는 탭 레이아웃 화면 (주문 요청 및 완료 사항 확인 가능)

    String TAG = "MainActivity";

    // DB Connect
    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo;
    int skStatus = 0;
    String strResult = null;

    Fragment CompleteFragment, OrderRequestFragment, ProgressingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("openStatus", MODE_PRIVATE);
        strResult = sharedPreferences.getString("openResult", "null");
        Log.v(TAG, "오픈 상태 : " + strResult);

        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        skSeqNo = intent.getIntExtra("skSeqNo", 0);
        skStatus = intent.getIntExtra("skStatus", 0);

        Log.v(TAG, "skStatus : " + skStatus);


        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.lmw_main_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() { // 로그아웃 버튼 리스너
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()){
                    case R.id.toolbar_logout: // 매장 마감 & 로그아웃
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("<로그아웃>");
                        builder.setMessage("로그아웃하시면 매장 영업이 종료됩니다. \n종료하시겠습니까?");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                connectCloseStore(); // 로그아웃 시 매장 마감

                                SharedPreferences sharedPreferences = getSharedPreferences("openStatus",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("openResult", "null");
                                editor.commit();

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("취소", null);
                        builder.create().show();
                        break;
                    case R.id.toolbar_Menu: // 메뉴 정보
                        intent = new Intent(MainActivity.this, MenuListActivity.class);

                        intent.putExtra("macIP", macIP);
                        intent.putExtra("skSeqNo", skSeqNo);

                        startActivity(intent);
                        break;
                    case R.id.toolbar_store: // 매장 정보
                        intent = new Intent(MainActivity.this, StoreInfoActivity.class);

                        intent.putExtra("macIP", macIP);
                        intent.putExtra("skSeqNo", skSeqNo);

                        startActivity(intent);
                        break;
                }
                return true;
            }
        });


        TabLayout tabs = findViewById(R.id.tabs);

        OrderRequestFragment = new OrderRequestFragment();
        ProgressingFragment = new ProgressingFragment();
        CompleteFragment = new CompleteFragment();

        // OrderRequestFragment로 넘긴다.
        Bundle bundle = new Bundle();
        bundle.putString("macIP", macIP);
        bundle.putInt("skSeqNo", skSeqNo);
        OrderRequestFragment.setArguments(bundle);
        ProgressingFragment.setArguments(bundle);
        CompleteFragment.setArguments(bundle);

        tabs.addTab(tabs.newTab().setText("접수대기"));
        tabs.addTab(tabs.newTab().setText("처리중"));
        tabs.addTab(tabs.newTab().setText("완료"));

        getSupportFragmentManager().beginTransaction().add(R.id.frame, OrderRequestFragment).commit(); // 탭 레이아웃 시작 화면

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { // 탭 레이아웃
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if(position == 0){
                    selected = OrderRequestFragment;
                }else if (position == 1){
                    selected = ProgressingFragment;
                }else if (position == 2) {
                    selected = CompleteFragment;
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
    public boolean onCreateOptionsMenu(Menu menu) { // 툴바 장바구니

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_open_toolbar, menu);
        menuInflater.inflate(R.menu.menu_menulist_toolbar, menu);
        menuInflater.inflate(R.menu.menu_toolbar, menu);

        return true;
    }

    private String connectCloseStore(){ // 로그아웃하면 skStatus 0로 바꾸기 => 매장 마감
        String result = null;
        skStatus = 0;

        urlAddr = "http://" + macIP + ":8080/tify/lmw_storekeeper_update.jsp?skSeqNo=" + skSeqNo + "&skStatus=" + skStatus;
        where = "update";

        try {
            LMW_LoginNetworkTask networkTask = new LMW_LoginNetworkTask(MainActivity.this, urlAddr, where);

            Object obj = networkTask.execute().get();
            result = (String) obj;

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("test", "onPrepareOptionsMenu - 옵션메뉴가 " +
                "화면에 보여질때 마다 호출됨");

        if(strResult.equals("1") || skStatus == 1){
            Log.v(TAG, "오픈 상태 : 오픈");
            menu.getItem(0).setIcon(R.drawable.ic_action_storeopen_open);
        }
        return super.onPrepareOptionsMenu(menu);
    }


}