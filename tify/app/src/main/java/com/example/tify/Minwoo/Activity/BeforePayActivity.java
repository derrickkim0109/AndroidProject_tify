package com.example.tify.Minwoo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderNetworkTask;
import com.example.tify.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BeforePayActivity extends AppCompatActivity {


    String TAG = "BeforePayActivity";

    private ArrayList<Order> list;
    private ArrayList<OrderList> orderLists;

    // layout
    TextView tv_totalOrderPrice;
    TextView tv_totalPayPrice;
    Button cardBtn;
    Button kakaoPay;

    // order Insert (카드이름이랑 카드번호 받기)
    String macIP;
    String urlAddr = null;
    String where = null;
    int user_uSeqNo = 0;
    int store_sSeqNo = 0;
    int totalPrice = 0;
    String oCardName;
    int oCardNo;
    String sName;

    // orderlist Insert
    String menu_mName;
    int olSizeUp;
    int olAddShot;
    String olRequest;
    int olPrice;
    int olQuantity;

    String from;
    String strTotal;
    int number;
    int oNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_before_pay);

        // OrderSummaryActivity로 부터 값을 받는다.
        Intent intent = getIntent();

        macIP = intent.getStringExtra("macIP");
        user_uSeqNo = intent.getIntExtra("user_uSeqNo", 0);
        store_sSeqNo = intent.getIntExtra("store_sSeqNo", 0);
        totalPrice = intent.getIntExtra("totalPrice", 0);

        menu_mName = intent.getStringExtra("menu_mName");
        olSizeUp = intent.getIntExtra("olSizeUp", 0);
        olAddShot = intent.getIntExtra("olAddShot", 0);
        olRequest = intent.getStringExtra("olRequest");
        olPrice = intent.getIntExtra("olPrice", 0);
        olQuantity = intent.getIntExtra("olQuantity", 0);
        sName = intent.getStringExtra("sName");

        from = intent.getStringExtra("from"); // CartActivity에서 오는지 OrderSummaryActivity에서 오는지 확인
        Log.v(TAG, from);

        switch (from){
            case "OrderSummaryActivity":
                break;
            case "CartActivity":
                break;
            default:
                Intent intent1 = new Intent(BeforePayActivity.this, StoreInfoActivity.class);
                startActivity(intent1);
                break;

        }

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_orderoNo_select.jsp?user_uNo=" + user_uSeqNo;
        list = connectGetData(); // order Select onCreate할 때 미리 마지막 번호 찾아와서 +1하기
        if (list.size() == 0){
            oNo = 1;
        }else{
            oNo = list.get(0).getoNo() + 1;
        }

        Log.v(TAG, "마지막 oNo : " + oNo);

        // 콤마 찍어서 원화로 바꿔줌!
        NumberFormat moneyFormat = NumberFormat.getInstance(Locale.KOREA);
        strTotal = moneyFormat.format(totalPrice);

        // layout 설정
        cardBtn = findViewById(R.id.beforePay_Btn_Card);
        tv_totalOrderPrice = findViewById(R.id.beforePay_TV_totalOrderPrice);
        tv_totalPayPrice = findViewById(R.id.beforePay_TV_totalPayPrice);
        kakaoPay = findViewById(R.id.beforePay_Btn_Kakaopay);

        // 클릭 리스너
        cardBtn.setOnClickListener(mClickListener);
        kakaoPay.setOnClickListener(mClickListener);

        // 값 대입
        tv_totalOrderPrice.setText(strTotal + "원");
        tv_totalPayPrice.setText(strTotal + "원");

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.order_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;

            switch (v.getId()){
                case R.id.beforePay_Btn_Kakaopay:


                    break;
                case R.id.beforePay_Btn_Card:


                    // 결제 부분 완료되면 결제 부분에서 JSP 실행하기 (카드번호랑 카드이름 필요)
                    // 1. 결제 완료되면 OrderListActivity로!
                    // 1-1. order 테이블 Insert
                    // 1-2. order oNo Select => oNo가 있어야 orderlist에 Insert 가능..
                    // 1-3. orderlist 테이블 Insert
                    // 2. 결제 실패하면 StoreInfoActivity로!

                    // 테스트 ---------
                    // order 테이블 Insert
                    where = "insert";
                    int cardNum = 13153123;
                    String cardName = "신한은행";
                    urlAddr = "http://" + macIP + ":8080/tify/lmw_order_insert.jsp?user_uNo=" + user_uSeqNo + "&store_sSeqNo=" + store_sSeqNo + "&store_sName=" + sName + "&oSum=" + totalPrice + "&oCardName=" + cardName + "&oCardNo=" + cardNum + "&oReview=" + 0 + "&oStatus=" + 0;

                    connectInsertData(); // order Insert

//                    // oNo Select
//                    where = "select";
//                    urlAddr = "http://" + macIP + ":8080/tify/lmw_orderoNo_select.jsp?user_uNo=" + user_uSeqNo;

//                    list = connectGetData(); // order Select onCreate할 때 미리 마지막 번호 찾아와서 +1하기
//                    int oNo = list.get(0).getoNo();
//                    Log.v(TAG, "받은 oNo : " + oNo);

                    // orderlist 테이블 Insert
                    where = "insert";
                    urlAddr = "http://" + macIP + ":8080/tify/lmw_orderlist_insert.jsp?user_uNo=" + user_uSeqNo + "&order_oNo=" + oNo + "&store_sSeqNo=" + store_sSeqNo + "&store_sName=" + sName + "&menu_mName=" + menu_mName + "&olSizeUp=" + olSizeUp + "&olAddShot=" + olAddShot + "&olRequest=" + olRequest + "&olPrice=" + olPrice + "&olQuantity=" + olQuantity;

                    connectInsertData(); // orderlist Insert

                    // 테스트용
                    intent = new Intent(BeforePayActivity.this, OrderListActivity.class);
                    intent.putExtra("macIP", macIP);
                    intent.putExtra("user_uSeqNo", user_uSeqNo);
                    intent.putExtra("store_sSeqNo", store_sSeqNo);
                    intent.putExtra("totalPrice", totalPrice);
                    intent.putExtra("user_uSeqNo", user_uSeqNo);
                    startActivity(intent);

                    break;
            }

        }
    };
    private String connectInsertData(){ // 장바구니에 넣기
        String result = null;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where <- "insert"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            if(where.equals("insert")){
                LMW_OrderNetworkTask networkTask = new LMW_OrderNetworkTask(BeforePayActivity.this, urlAddr, where);
                Object obj = networkTask.execute().get();
                result = (String)obj;
            }else{
                LMW_OrderListNetworkTask networkTask = new LMW_OrderListNetworkTask(BeforePayActivity.this, urlAddr, where);
                Object obj = networkTask.execute().get();
                result = (String)obj;
            }

            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<Order> connectGetData(){
        ArrayList<Order> beanList = new ArrayList<Order>();

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_OrderNetworkTask networkTask = new LMW_OrderNetworkTask(BeforePayActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            list = (ArrayList<Order>) obj;
            Log.v(TAG, "data.size() : " + list.size());

            beanList = list;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }
}