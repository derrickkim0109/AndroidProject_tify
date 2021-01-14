package com.example.tify.Taehyun.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Hyeona.Activity.Payment_resultActivity;
import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Adapter.Mypage_CardListAdapter;
import com.example.tify.Taehyun.Bean.Bean_Mypage_cardlist;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_RecycleView_CardView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderPage_PaymentActivity11111 extends AppCompatActivity {

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

    int user_uNo = 0;

    //임시 유저 값.
//    int user_uNo = 1;


    //Intent
    Intent intent ;
    int oNo = 1;
    int point = 0;
    //DB
    String order_cComapany, order_cCardNO;
    int cNo = 0;
    //XML
    ImageView orderPay_cCompanyIV;
    TextView orderPay_cNumberTV,oderPay_oPriceTV;
    CheckBox orderPay_CB;
    Button orderPay_insertbtn;

    // BeforePayActivity2 (Cart)
    ArrayList<Cart> carts = null;
    String from = null;
    int totalPrice = 0;

    // BeforePayActivity
    String menu_mName;
    int olSizeUp;
    int olAddShot;
    String olRequest;
    int olPrice;
    int olQuantity;
    int store_SeqNo; // 옮겨 넣기
    String sName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_payment);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        user_uNo = auto.getInt("userSeq",0);

        intent = getIntent();
        inheritance();
        init();

    }

    @SuppressLint("LongLogTag")
    private void inheritance() {
//        oNo = intent.getIntExtra("oNo",0);
        point = intent.getIntExtra("point",0);
        from = intent.getStringExtra("from");
        totalPrice = intent.getIntExtra("total", 0);
        store_SeqNo = intent.getIntExtra("store_sSeqNo", 0);

        Log.v(TAG, "from : " + from);
        Log.v(TAG, "totalPrice : " + totalPrice);
        Log.v(TAG, "point : " + point);
        Log.v(TAG, "store_SeqNo : " + store_SeqNo); // 옮겨 넣기
        if(from.equals("BeforePayActivity2")){ // 장바구니에서 왔음
            carts = (ArrayList<Cart>) getIntent().getSerializableExtra("Carts");

            for(int i = 0; i < carts.size(); i++){
                Log.v(TAG, "carts : " + carts.get(i).getMenu_mName());
            }

        }else { // 바로결제에서 왔음
            menu_mName = intent.getStringExtra("menu_mName");
            olSizeUp = intent.getIntExtra("olSizeUp", 0);
            olAddShot = intent.getIntExtra("olAddShot", 0);
            olRequest = intent.getStringExtra("olRequest");
            olPrice = intent.getIntExtra("olPrice", 0);
            olQuantity = intent.getIntExtra("olQuantity", 0);
            sName = intent.getStringExtra("sName");

            Log.v(TAG, "menu_mName : " + menu_mName);
            Log.v(TAG, "olSizeUp : " + olSizeUp);
            Log.v(TAG, "olAddShot : " + olAddShot);
            Log.v(TAG, "olRequest : " + olRequest);
            Log.v(TAG, "olPrice : " + olPrice);
            Log.v(TAG, "olQuantity : " + olQuantity);
            Log.v(TAG, "sName : " + sName);
        }
    }

    private void init() {
        recyclerView = findViewById(R.id.orderPay_cardListView);
        orderPay_cCompanyIV = findViewById(R.id.orderPay_cCompany);
        orderPay_cNumberTV = findViewById(R.id.orderPay_cNumber);
        oderPay_oPriceTV = findViewById(R.id.oderPay_oPrice);
        orderPay_CB = findViewById(R.id.orderPay_CB);
        orderPay_insertbtn = findViewById(R.id.orderPay_insertbtn);

        // 결제 가격
        Log.v("TAG", " 최종 totalPrice : " + totalPrice);
        NumberFormat moneyFormat = NumberFormat.getInstance(Locale.KOREA);
        String strTotal = moneyFormat.format(totalPrice);
        oderPay_oPriceTV.setText(strTotal + "원");
        // --------------------

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
                    intent = new Intent(OrderPage_PaymentActivity11111.this, Payment_resultActivity.class)
                            .putExtra("oNo",oNo)
                            .putExtra("point",point)
                            .putExtra("from", from) // 옮겨 넣기
                            .putExtra("store_sSeqNo", store_SeqNo); // 옮겨 넣기
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
            cardListAdapter = new Mypage_CardListAdapter(OrderPage_PaymentActivity11111.this, R.layout.kth_activity_mypage_cardimagelist,bean_mypage_cardlists,MacIP,user_uNo);



            //어댑터에게 보내기
            recyclerView.setAdapter(cardListAdapter);

        }catch (Exception e){
            e.printStackTrace();

        }
    }
}
