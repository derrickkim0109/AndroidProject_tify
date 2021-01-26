package com.example.tify.Hyeona.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.tify.Hyeona.Adapter.paymentHistory_adapter;
import com.example.tify.Hyeona.Adapter.pointHistory_adapter;
import com.example.tify.Hyeona.Bean.Bean_payment_paylist;
import com.example.tify.Hyeona.Bean.Bean_point_history;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_payment;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_reward;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

//마이페이지에서 결제내역을 클릭할 경우 나오는 페이지
// 결제 히스토리를 받아 출력한다
public class MypageActivity_paymentlist extends AppCompatActivity {

    private paymentHistory_adapter adapter;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;
    private ArrayList<Bean_payment_paylist> payment_paylists = null;


    private int user_uNo;
    private String user_uName;

    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_activity_mypage_paymentlist);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //액션바 삭제
        recyclerView = findViewById(R.id.payment_history);
       //리사이클러뷰 찾기

        // 앞서 저장된 유저 정보를 받아와서 결제정보를 출력하는데 사용
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        user_uNo = auto.getInt("userSeq",0);
        user_uName= auto.getString("userNickName",null);

        userName = findViewById(R.id.user_name);
        userName.setText(user_uName + "님의");

    }

    @Override
    protected void onResume() {
        super.onResume();
        connectGetData(user_uNo);


    }

    private void connectGetData(int s){
        try {
            //여기서 결제 내역을 받아옴
            String urlAddr = "http://" + MacIP + ":8080/tify/cha_payment_list.jsp?";
            String urlAddress = urlAddr + "uNo=" + s;
            CUDNetworkTask_payment CUDNetworkTask_payment = new CUDNetworkTask_payment(urlAddress,"paylist");
            Object obj = CUDNetworkTask_payment.execute().get();
            payment_paylists = (ArrayList<Bean_payment_paylist>) obj;

            recyclerView.setHasFixedSize(true);
            //레이아웃 매니저 만들기
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new paymentHistory_adapter(MypageActivity_paymentlist.this, R.layout.cha_activity_mypage_paymentlist,payment_paylists);
            //어댑터에게 보내기
            recyclerView.setAdapter(adapter);


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}