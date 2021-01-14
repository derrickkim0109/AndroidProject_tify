package com.example.tify.Taehyun.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Hyeona.Activity.Payment_resultActivity;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Adapter.Mypage_CardListAdapter;
import com.example.tify.Taehyun.Bean.Bean_Mypage_cardlist;
import com.example.tify.Taehyun.Bean.Bean_Mypage_userinfo;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_RecycleView_CardView;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;

import java.util.ArrayList;

public class OrderPage_PaymentActivity extends AppCompatActivity {

    //field
    final static String TAG = "OrderPage_PaymentActivity";

    //카드 리스트
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;
    private Mypage_CardListAdapter cardListAdapter;
    private ArrayList<Bean_Mypage_cardlist> bean_mypage_cardlists = null;

    //카드 정보 불러오기 select
    Bean_Mypage_cardlist cardlist ;

    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

//    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
//    int user_uNo = auto.getInt("userSeq",0);
    //임시 유저 값.
    int user_uNo = 1;


    //Intent
    Intent intent ;
    int oNo = 0;
    int point = 0;
    //DB
    String order_cComapany, order_cCardNO;
    int cNo = 0;
    //XML
    ImageView orderPay_cCompanyIV;
    TextView orderPay_cNumberTV,oderPay_oPriceTV;
    CheckBox orderPay_CB;
    Button orderPay_insertbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_payment);
        init();

        intent = getIntent();
        inheritance();

    }

    private void inheritance() {
        oNo = intent.getIntExtra("oNo",0);
        point = intent.getIntExtra("point",0);
    }

    private void init() {
        recyclerView = findViewById(R.id.orderPay_cardListView);
        orderPay_cCompanyIV = findViewById(R.id.orderPay_cCompany);
        orderPay_cNumberTV = findViewById(R.id.orderPay_cNumber);
        oderPay_oPriceTV = findViewById(R.id.oderPay_oPrice);
        orderPay_CB = findViewById(R.id.orderPay_CB);
        orderPay_insertbtn = findViewById(R.id.orderPay_insertbtn);

        orderPay_CB.setOnClickListener(mClickListener);
        orderPay_insertbtn.setOnClickListener(mClickListener);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                //동의 체크 박스
                case R.id.orderPay_CB:

                    break;
                    // 결제하기 버튼
                case R.id.orderPay_insertbtn:
                    intent = new Intent(OrderPage_PaymentActivity.this, Payment_resultActivity.class)
                            .putExtra("oNo",oNo)
                            .putExtra("point",point);
                    startActivity(intent);
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        //카드 이미지 리스트 띄우기
        connectCardView(user_uNo);
        //카드 정보띄우기
        connectGetData(user_uNo);
    }

    private void connectGetData(int s) {
        try {
            //임시값


            String urlAddr = "http://" + MacIP + ":8080/tify/mypage_card_list_select.jsp?";
            String urlAddress = urlAddr + "user_uNo=" + s;


            NetworkTask_RecycleView_CardView networkTask_taeHyun = new NetworkTask_RecycleView_CardView(urlAddress,"CardList_Bean");
            Object obj = networkTask_taeHyun.execute().get();
            cardlist = (Bean_Mypage_cardlist) obj;

            //DB
            order_cComapany = cardlist.getcCardCompany();
            order_cCardNO = cardlist.getcCardNo();
            cNo = cardlist.getcNo();

            if (order_cComapany.equals("MASTERCARD")){
                orderPay_cCompanyIV.setImageResource(R.drawable.mastercard);
            }else if (order_cComapany.equals("VISA")){
                orderPay_cCompanyIV.setImageResource(R.drawable.visa);

            }else if (order_cComapany.equals("AMEX")){
                orderPay_cCompanyIV.setImageResource(R.drawable.amex);

            }else if (order_cComapany.equals("DINERS_CLUB And CARTE_BLANCHE")){
                orderPay_cCompanyIV.setImageResource(R.drawable.dinersclub);

            }else if (order_cComapany.equals("DISCOVER")){
                orderPay_cCompanyIV.setImageResource(R.drawable.discover);

            }else if (order_cComapany.equals("JCB")){
                orderPay_cCompanyIV.setImageResource(R.drawable.jcb);

            }

            String str= "";
            for (int i = 0; i < order_cCardNO.length()-4; i++) {
                if (i%4 == 0 ){
                    str += " ";
                }
                str +="*";
            }
            str +=" ";
            orderPay_cNumberTV.setText(str+order_cCardNO.substring((order_cCardNO.length()-4),order_cCardNO.length()));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("LongLogTag")
    private void connectCardView(int s){

        try {

            String urlAddr = "http://" + MacIP + ":8080/tify/mypage_card_list_select.jsp?";
            String urlAddress = urlAddr + "user_uNo=" + s;
            Log.v(TAG,urlAddress);


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
