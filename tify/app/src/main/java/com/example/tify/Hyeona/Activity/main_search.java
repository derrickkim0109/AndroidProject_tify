package com.example.tify.Hyeona.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tify.Hyeona.Adapter.pointHistory_adapter;
import com.example.tify.Hyeona.Bean.Bean_point_history;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_reward;
import com.example.tify.R;
import com.example.tify.Taehyun.Activity.Mypage_CardDetailActivity;

import java.util.ArrayList;


public class main_search extends AppCompatActivity {
    private ImageView main_img_SearchBtn;
    private EditText main_SearchText;
    //private macIP = ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_main_search);

        main_SearchText = findViewById(R.id.main_SearchText);
        main_img_SearchBtn = findViewById(R.id.main_img_SearchBtn);

        main_img_SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(main_SearchText.getText().length()==0){
                    new AlertDialog.Builder(main_search.this)
                            .setTitle("검색어를 입력하세요")
                            .setPositiveButton("확인",null)
                            .show();
                    main_SearchText.requestFocus();
                }else{
                    //connectGetData(String s);
                }

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
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
        title.setText("");
        ImageButton cart = findViewById(R.id.cart);
        cart.setVisibility(View.GONE);
//         장바구니 없애려면 위에거 살리면 됨
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

//    private void connectGetData(String s){
//        try {
//            String urlAddr = "http://" + macIP + ":8080/tify/pointHistory_select.jsp?";
//            //여기 변경 포인트
//            String urlAddress = urlAddr + "uNo=" + s;
//            CUDNetworkTask_reward CUDNetworkTask_reward = new CUDNetworkTask_reward(urlAddress,"pointHistory_select");
//            Object obj = CUDNetworkTask_reward.execute().get();
//            point_history = (ArrayList<Bean_point_history>) obj;
//
//
//            recyclerView.setHasFixedSize(true);
//            //레이아웃 매니저 만들기
//            layoutManager = new LinearLayoutManager(this);
//            recyclerView.setLayoutManager(layoutManager);
//            adapter = new pointHistory_adapter(PointActivity.this, R.layout.cha_point_history,user_uNo,point_history);
//            //어댑터에게 보내기
//            recyclerView.setAdapter(adapter);
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
