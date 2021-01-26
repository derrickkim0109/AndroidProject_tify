package com.example.tify.Hyeona.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tify.Hyeona.Adapter.review_adapter;
import com.example.tify.Hyeona.Bean.Bean_payment_select;
import com.example.tify.Hyeona.Bean.Bean_review_review;
import com.example.tify.Hyeona.Bean.Bean_reward_stamphistory;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_payment;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_review;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_stampCount;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Minwoo.Activity.OrderListActivity;
import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.util.ArrayList;



// 결제완료 후 뜨는 창
public class Payment_resultActivity extends AppCompatActivity {

    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

    int userSeq;
    String userNickName;
    String myLocation;
    Button payment_result_btn;
    int oNo;
    int user_uNo;

    int coffee_count;

    TextView payment_money, payment_day, payment_card,payment_card_number;
    Bean_payment_select bean_payment_select;

    int point = 0;
    int totalPrice = 0;
    int store_SeqNo = 0;

    String from = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_payment_result);

        // 앞서 저장된 유저정보 받아옴
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        user_uNo = auto.getInt("userSeq",0);
        // 앞에서 결제정보 관련 정보를 받아옴
        Intent intent = getIntent();
        oNo = intent.getIntExtra("oNo",0);
        point = intent.getIntExtra("point", 0);
        totalPrice = intent.getIntExtra("total", 0);
        from = intent.getStringExtra("from");
        store_SeqNo = intent.getIntExtra("store_sSeqNo", 0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //액션바 삭제

        payment_money = findViewById(R.id.payment_money);
        payment_day = findViewById(R.id.payment_day);
        payment_card =findViewById(R.id.payment_card);
        payment_card_number = findViewById(R.id.payment_card_number);

        payment_result_btn = findViewById(R.id.payment_result_btn);
        payment_result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 디비 서버에 저장시키기 위한 정보들. 버튼을 누르면
                Intent intent = new Intent(Payment_resultActivity.this, OrderListActivity.class)
                        .putExtra("oNo",oNo)
                        .putExtra("from", from)
                        .putExtra("store_sSeqNo", store_SeqNo);

                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        connectGetData();
        connectInsertData(point);
        coffee_count = connectStampData(oNo);
        Log.v("커피","테스트"+coffee_count);
        //위에서 받은 커피 주문수를 밑의 스탬프 추가하는 곳에 넣는다
        connectStampAdd(coffee_count);
        if (point>0){
            connectPointhistory();
        }

    }

    private void connectGetData(){
        // 시간 등 결제정보를 받아오는 곳
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/cha_payment_select.jsp?";
            String urlAddress = urlAddr + "oNo=" + oNo;
            CUDNetworkTask_payment mCUDNetworkTask_payment = new CUDNetworkTask_payment(urlAddress,"select");
            Object obj = mCUDNetworkTask_payment.execute().get();
            bean_payment_select = (Bean_payment_select) obj;

            payment_money.setText(bean_payment_select.getoSum());
            payment_day.setText(bean_payment_select.getoInsertDate());
            payment_card.setText(bean_payment_select.getoCardName());
            payment_card_number.setText(bean_payment_select.getoCardNo());

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private int connectStampData(int oNo) {
        // 여기에서 몇잔 주문했는지 받아올 것임 받은 숫자는 다시 스탬프에 넣어야한다
        int result = 0;
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/cha_stamp_update1.jsp?oNo="+oNo;
            CUDNetworkTask_payment mCUDNetworkTask_payment = new CUDNetworkTask_payment(urlAddr,"stamp_count");
            Object obj = mCUDNetworkTask_payment.execute().get();
            result = (int) obj;
        }catch (Exception e){
            e.printStackTrace();
        }return result;
    }

    private int connectStampAdd(int coffee_count) {
        // 스탬프를 입력해주는 곳
        int result = 0;
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/cha_stamp_update2.jsp?user_uNo="+user_uNo+"&coffee_count="+coffee_count;
            CUDNetworkTask_payment mCUDNetworkTask_payment = new CUDNetworkTask_payment(urlAddr,"stamp_update");
            Object obj = mCUDNetworkTask_payment.execute().get();
            result = (int) obj;
        }catch (Exception e){
            e.printStackTrace();
        }return result;
    }

    private int connectPointhistory() {

        // 포인트 사용시 히스토리 입력
        int result = 0;
        try {
            String rhContent = "포인트사용";
            int rhChoice = 0; // 이 경우는 사용
            String rhPointHow = point+"p";

            // 스탬프로 인한 적립 히스토리 저장
            String urlAddress3 = "http://" + MacIP + ":8080/tify/cha_rewardhistory_insert2.jsp?user_uNo="+user_uNo +"&rhContent="+rhContent +"&rhChoice="+rhChoice+"&rhPointHow="+rhPointHow;
            CUDNetworkTask_stampCount CUDNetworkTask_stampCount =  new CUDNetworkTask_stampCount(urlAddress3,"stamp_update");
            // 귀찮으니까 걍 업데이트랑 같은거 사용
            Object obj = CUDNetworkTask_stampCount.execute().get();
            result = (int) obj;

        }catch (Exception e){
            e.printStackTrace();
        }return result;
    }



    private int connectInsertData(int point) {
        //여기 리절트는 성공 실패만 확인
        int result = 0;
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/cha_payment_update.jsp?point="+point+"&user_uNo="+user_uNo;
            CUDNetworkTask_payment mCUDNetworkTask_payment = new CUDNetworkTask_payment(urlAddr,"update");
            Object obj = mCUDNetworkTask_payment.execute().get();
            result = (int) obj;

        }catch (Exception e){
            e.printStackTrace();
        }return result;
    }


}
