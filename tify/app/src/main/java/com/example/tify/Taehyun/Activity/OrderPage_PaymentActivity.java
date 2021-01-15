package com.example.tify.Taehyun.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Hyeona.Activity.Payment_resultActivity;
import com.example.tify.Jiseok.Activity.PaymentPayPasswordActivity;
import com.example.tify.Minwoo.Adapter.CartAdapter;
import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Adapter.Mypage_CardListAdapter;
import com.example.tify.Taehyun.Bean.Bean_Mypage_cardlist;
import com.example.tify.Taehyun.Bean.Bean_Mypage_userinfo;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_RecycleView_CardView;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

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



    //임시 유저 값.
     int user_uNo = 0;


    //Intent
    Intent intent ;
    String card_cNo;
    int point = 0;
    int oNo = 0;

    //DB
    String cCardCompany;

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


    //jsp적을 주소
    String urlAddr = null;
    //
    String urlAddress = null;


    //order === BEAN
    Order order = null;
    int dataSize = 0;
    ArrayList<String> urls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_payment);


        intent = getIntent();
        inheritance();
        init();
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        user_uNo = auto.getInt("userSeq",0);

        urlAddr =  "http://" + MacIP + ":8080/tify/orderPay_insert.jsp?";



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

        // 결제 가격
    }

    @SuppressLint("LongLogTag")
    private void inheritance() {
        point = intent.getIntExtra("point", 0);
        from = intent.getStringExtra("from");
        totalPrice = intent.getIntExtra("total", 0);
        store_SeqNo = intent.getIntExtra("store_sSeqNo", 0);
        sName = intent.getStringExtra("sName");



            Log.v(TAG, "from : " + from);
            Log.v(TAG, "totalPrice : " + totalPrice);
            Log.v(TAG, "point : " + point);
            Log.v(TAG, "store_SeqNo : " + store_SeqNo); // 옮겨 넣기
            if (from.equals("BeforePayActivity2")) { // 장바구니에서 왔음
                carts = (ArrayList<Cart>) getIntent().getSerializableExtra("Carts");
                dataSize = carts.size();

                for (int i = 0; i < carts.size(); i++) {
                    Log.v(TAG, "carts(" + i + "): " + carts.get(i).getMenu_mName());
                }

            } else { // 바로결제에서 왔음

                menu_mName = intent.getStringExtra("menu_mName");
                olSizeUp = intent.getIntExtra("olSizeUp", 0);
                olAddShot = intent.getIntExtra("olAddShot", 0);
                olRequest = intent.getStringExtra("olRequest");
                olPrice = intent.getIntExtra("olPrice", 0);
                olQuantity = intent.getIntExtra("olQuantity", 0);
                sName = intent.getStringExtra("sName");



        }
    }
        View.OnClickListener mClickListener = new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    // 결제하기 버튼
                    case R.id.orderPay_insertbtn:
                        if (orderPay_CB.isChecked() == false) {
                            new AlertDialog.Builder(OrderPage_PaymentActivity.this)
                                    .setTitle("동의를 체크해주세요.")
                                    .setPositiveButton("확인", null)
                                    .show();
                            break;
                        } else {

                            //String result = connectOrderInsert();
//                            Log.v(TAG,"result = " + result);
//                            if (result.equals("1")) {

                                //oNo 다시 받아오기

                                ////////
                                Log.v(TAG,"oNo"+oNo);
                                Log.v(TAG,"oNo"+sName);
                                Log.v(TAG,"oNo"+store_SeqNo);
                                //orderlist에 들어갈 값들
                            Log.v("ddd",from);
                                if(from.equals("BeforePayActivity2")) {
                                    for (int i = 0; i < carts.size(); i++) { // 클래스 만들어서 메소드 이용하면 더 빠를 수도?oNo
                                        urlAddress = "http://" + MacIP + ":8080/tify/lmw_orderlist_insert.jsp?order_oNo=" + oNo + "&store_sSeqNo=" + store_SeqNo + "&user_uNo=" + user_uNo +
                                                "&store_sName=" + sName + "&menu_mName=" + carts.get(i).getMenu_mName() + "&olSizeUp=" + carts.get(i).getcLSizeUp()
                                                + "&olAddShot=" + carts.get(i).getcLAddShot() + "&olRequest=" + carts.get(i).getcLRequest() + "&olPrice=" + carts.get(i).getcLPrice()
                                                + "&olQuantity=" + carts.get(i).getcLQuantity();

                                        urls.add(urlAddress);


                                    }
                                    for(int i = 0; i < urls.size(); i++){
                                        Log.v(TAG, "urls : " + urls.get(i));
                                    }

                                }else{
                                    urlAddress = "http://" + MacIP + ":8080/tify/lmw_orderlist_insert.jsp?order_oNo=" + oNo + "&store_sSeqNo=" + store_SeqNo + "&user_uNo=" + user_uNo +
                                            "&store_sName=" + sName + "&menu_mName=" + menu_mName + "&olSizeUp=" + olSizeUp
                                            + "&olAddShot=" + olAddShot + "&olRequest=" + olRequest + "&olPrice=" + olPrice
                                            + "&olQuantity=" + olQuantity;
                                }

                                Log.v("qqq",""+oNo);

                                intent = new Intent(OrderPage_PaymentActivity.this, PaymentPayPasswordActivity.class)
                                        .putExtra("point", point)
                                        .putExtra("cNo", cNo)
                                        .putExtra("store_SeqNo", store_SeqNo)
                                        .putExtra("sName", sName)
                                        .putExtra("totalPrice", totalPrice)
                                        .putExtra("cCardCompany", cCardCompany)
                                        .putExtra("card_cNo", card_cNo)
                                        .putExtra("urlAddress", urlAddress);

                                if(from.equals("BeforePayActivity2")){
                                    intent.putExtra("size", dataSize);
                                    intent.putExtra("urls", urls);
                                    Log.v(TAG, "size : " + dataSize);
                                }

                                startActivity(intent);
//                            } else {
//                                Toast.makeText(OrderPage_PaymentActivity.this, "실패", Toast.LENGTH_SHORT).show();
//                            }

                        }
                        break;
                }
            }
        };


    @Override
        protected void onResume () {
            super.onResume();
            //카드 이미지 리스트 띄우기
            connectCardView(user_uNo);
            Log.v("dkdkdk",""+bean_mypage_cardlists.size());



            // 카드 선택 안 했을 경우!
        cCardCompany = bean_mypage_cardlists.get(0).getcCardCompany();
        card_cNo = bean_mypage_cardlists.get(0).getcCardNo();


        Log.v("qqqq",cCardCompany);
        if (cCardCompany.equals("MASTERCARD")) {
            orderPay_cCompanyIV.setImageResource(R.drawable.mastercard);
        } else if (cCardCompany.equals("VISA")) {
            orderPay_cCompanyIV.setImageResource(R.drawable.visa);

        } else if (cCardCompany.equals("AMEX")) {
            orderPay_cCompanyIV.setImageResource(R.drawable.amex);

        } else if (cCardCompany.equals("DINERS_CLUB And CARTE_BLANCHE")) {
            orderPay_cCompanyIV.setImageResource(R.drawable.dinersclub);

        } else if (cCardCompany.equals("DISCOVER")) {
            orderPay_cCompanyIV.setImageResource(R.drawable.discover);

        } else if (cCardCompany.equals("JCB")) {
            orderPay_cCompanyIV.setImageResource(R.drawable.jcb);

        }

        Log.v("cardNo",card_cNo);
        String str = "";
        for (int j = 0; j < card_cNo.length() - 4; j++) {
            if (j % 4 == 0) {
                str += " ";
            }
            str += "*";
        }
        str += " ";
        orderPay_cNumberTV.setText(str + card_cNo.substring((card_cNo.length() - 4), card_cNo.length()));


        }


        ///카드 이미지보이기 // 카드 정보 띄우기
        @SuppressLint("LongLogTag")
        private void connectCardView ( int s){

            try {

                String urlAddr = "http://" + MacIP + ":8080/tify/mypage_card_list_select.jsp?";
                String urlAddress = urlAddr + "user_uNo=" + s;
                Log.v(TAG, urlAddress);


                NetworkTask_RecycleView_CardView networkTask_taeHyun = new NetworkTask_RecycleView_CardView(urlAddress, "select_cardList");
                Object obj = networkTask_taeHyun.execute().get();

                bean_mypage_cardlists = (ArrayList<Bean_Mypage_cardlist>) obj;
                recyclerView.setHasFixedSize(true);

                //레이아웃 매니저 만들기            //가로 레이아웃  - 카드 그림
                layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                cardListAdapter = new Mypage_CardListAdapter(OrderPage_PaymentActivity.this, R.layout.kth_activity_mypage_cardimagelist, bean_mypage_cardlists, MacIP, user_uNo);


                //어댑터에게 보내기
                recyclerView.setAdapter(cardListAdapter);

                int card_quantities = cardCountselect();
                Log.v(TAG,"card_quantities :" + card_quantities);

//                if (card_quantities == 0) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderPage_PaymentActivity.this);
//                    builder.setTitle("카드등록");
//                    builder.setMessage("카드등록 하시겠습니까?");
//                    builder.setNegativeButton("아니오", null);
//                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() { // 예를 눌렀을 경우 로그인 창으로 이동
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            intent = new Intent(OrderPage_PaymentActivity.this, Mypage_CardDetailActivity.class)
//                                    .putExtra("uNo", user_uNo);
//                            startActivity(intent);
//                        }
//                    });
//                    builder.create().show();
//                }


                cardListAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                                    Toast.makeText(OrderPage_PaymentActivity.this,""+position,Toast.LENGTH_SHORT).show();
                                    cNo = bean_mypage_cardlists.get(position).getcNo();
                                    cCardCompany = bean_mypage_cardlists.get(position).getcCardCompany();
                                    card_cNo = bean_mypage_cardlists.get(position).getcCardNo();

                                    Log.v("카드", "카드seqNo : " + cNo);
                                    Log.v("카드", "카드회사명 : " + cCardCompany);
                                    Log.v("카드", "카드번호 : " + card_cNo);

                                    if (cCardCompany.equals("MASTERCARD")) {
                                        orderPay_cCompanyIV.setImageResource(R.drawable.mastercard);
                                    } else if (cCardCompany.equals("VISA")) {
                                        orderPay_cCompanyIV.setImageResource(R.drawable.visa);

                                    } else if (cCardCompany.equals("AMEX")) {
                                        orderPay_cCompanyIV.setImageResource(R.drawable.amex);

                                    } else if (cCardCompany.equals("DINERS_CLUB And CARTE_BLANCHE")) {
                                        orderPay_cCompanyIV.setImageResource(R.drawable.dinersclub);

                                    } else if (cCardCompany.equals("DISCOVER")) {
                                        orderPay_cCompanyIV.setImageResource(R.drawable.discover);

                                    } else if (cCardCompany.equals("JCB")) {
                                        orderPay_cCompanyIV.setImageResource(R.drawable.jcb);

                                    }

                                    String str = "";
                                    for (int j = 0; j < card_cNo.length() - 4; j++) {
                                        if (j % 4 == 0) {
                                            str += " ";
                                        }
                                        str += "*";
                                    }
                                    str += " ";
                                    orderPay_cNumberTV.setText(str + card_cNo.substring((card_cNo.length() - 4), card_cNo.length()));
                                }

                });

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        //결제 네트워크 타스크  -> order  순서 1
        @SuppressLint("LongLogTag")
        private String connectOrderInsert () {

            urlAddress = urlAddr + "&user_uNo=" + user_uNo + "&storekeeper_skSeqNo=" + store_SeqNo + "&store_sName=" + sName
                    + "&oSum=" + totalPrice + "&oCardName=" + cCardCompany + "&oCardNo=" + card_cNo;

            String result = null;
            try {
                NetworkTask_TaeHyun insertNetworkTask = new NetworkTask_TaeHyun(OrderPage_PaymentActivity.this, urlAddress, "insert");
                Object obj = insertNetworkTask.execute().get();
                result = (String) obj;

            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }


        // 전화번호 중복체크
        private int cardCountselect () {

            int cardcount = 0;
            try {

                String urlAddr = "http://" + MacIP + ":8080/tify/mypage_cardcountselect.jsp?user_uNo=" + user_uNo;

                NetworkTask_TaeHyun countnetworkTask = new NetworkTask_TaeHyun(OrderPage_PaymentActivity.this, urlAddr, "count");
                Object obj = countnetworkTask.execute().get();

                cardcount = (int) obj;

            } catch (Exception e) {

            }
            return cardcount;
        }

    // orderlist 테이블 Insert
    //결제 네트워크 타스크  --> order -> orderlist 순서 2

    private String connectOrderListInsert(String orderlistUrl) {
        String result = null;
        try {
            NetworkTask_TaeHyun insertNetworkTask = new NetworkTask_TaeHyun(OrderPage_PaymentActivity.this, orderlistUrl, "insert");
            Object obj = insertNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }




}/////---END