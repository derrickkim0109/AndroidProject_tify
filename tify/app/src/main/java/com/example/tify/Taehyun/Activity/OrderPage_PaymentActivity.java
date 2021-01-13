package com.example.tify.Taehyun.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Adapter.Mypage_CardListAdapter;
import com.example.tify.Taehyun.Bean.Bean_Mypage_cardlist;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_RecycleView_CardView;

import java.util.ArrayList;

public class OrderPage_PaymentActivity extends AppCompatActivity {

    //field
    //카드 리스트
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;
    private Mypage_CardListAdapter cardListAdapter;
    private ArrayList<Bean_Mypage_cardlist> bean_mypage_cardlists = null;


    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
    int user_uNo = auto.getInt("userSeq",0);
    String user_uName= auto.getString("userNickName",null);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.cha_payment);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //카드 리스트 띄우기
        connect_Order_CardView(user_uNo);

    }

    private void connect_Order_CardView(int user_uNo) {
        try {

            String urlAddr = "http://" + MacIP + ":8080/tify/mypage_card_list_select.jsp?";
            String urlAddress = urlAddr + "user_uNo=" + user_uNo;
            Log.v("dddd",urlAddress);

            NetworkTask_RecycleView_CardView networkTask_taeHyun = new NetworkTask_RecycleView_CardView(urlAddress,"select_cardList");
            Object obj = networkTask_taeHyun.execute().get();

            bean_mypage_cardlists = (ArrayList<Bean_Mypage_cardlist>) obj;
            recyclerView.setHasFixedSize(true);

            //레이아웃 매니저 만들기            //가로 레이아웃  - 카드 그림
            layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            cardListAdapter = new Mypage_CardListAdapter(OrderPage_PaymentActivity.this, R.layout.kth_activity_mypage_cardimagelist,bean_mypage_cardlists,MacIP,user_uNo);

            //어댑터에게 보내기
            recyclerView.setAdapter(cardListAdapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
