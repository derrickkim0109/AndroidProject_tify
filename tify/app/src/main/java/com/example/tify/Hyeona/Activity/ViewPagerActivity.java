package com.example.tify.Hyeona.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tify.Hyeona.Adapter.PagerAdapter;
import com.example.tify.R;

import me.relex.circleindicator.CircleIndicator;


public class ViewPagerActivity extends AppCompatActivity {

    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager vp;
   // private CircleIndicator indicator;
   private CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_activity_view_pager);
        initView();
        Button start_btn = findViewById(R.id.start_btn);

        // 여기에 다시는 보지않게 할 자리
//        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor autoLogin = auto.edit();
//        autoLogin.putString("inputId", et_id.getText().toString());
//        autoLogin.putString("inputPw", et_pw.getText().toString());
//        autoLogin.commit();


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //액션바 삭제

        SharedPreferences introskip = getSharedPreferences("introskip", Activity.MODE_PRIVATE);

        String skip2 = introskip.getString("skip",null);

        if(skip2 !=null) {
            Intent intent = new Intent(ViewPagerActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPagerActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView(){
        vp = findViewById(R.id.vp);
        indicator = findViewById(R.id.indicator);
        fragmentPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        vp.setAdapter(fragmentPagerAdapter);
        indicator.setViewPager(vp);
    }
}