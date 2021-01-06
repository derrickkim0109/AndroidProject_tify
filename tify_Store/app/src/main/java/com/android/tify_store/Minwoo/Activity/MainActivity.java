package com.android.tify_store.Minwoo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.android.tify_store.Minwoo.Fragment.CompleteFragment;
import com.android.tify_store.Minwoo.Fragment.OrderRequestFragment;
import com.android.tify_store.Minwoo.Fragment.ProgressingFragment;
import com.android.tify_store.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    Fragment CompleteFragment, OrderRequestFragment, ProgressingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_main);

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.lmw_main_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        TabLayout tabs = findViewById(R.id.tabs);

        OrderRequestFragment = new OrderRequestFragment();
        ProgressingFragment = new ProgressingFragment();
        CompleteFragment = new CompleteFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.frame, OrderRequestFragment).commit(); // 초기 시작 프래그먼트

        tabs.addTab(tabs.newTab().setText("접수대기"));
        tabs.addTab(tabs.newTab().setText("처리중"));
        tabs.addTab(tabs.newTab().setText("완료"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
}