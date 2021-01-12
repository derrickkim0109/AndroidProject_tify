package com.example.tify.Minwoo.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_PointNetworkTask;
import com.example.tify.R;
import com.example.tify.ShareVar;

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
    TextView tv_totalDiscount;
    EditText et_point;
    Button cardBtn;
    Button btn_point;

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
    String strPoint;
    String strDiscountPoint;
    String strRemainPoint;
    int number;
    int oNo;
    int point;

    String tempText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_before_pay);

        // OrderSummaryActivity로 부터 값을 받는다.
        Intent intent = getIntent();

        ShareVar shareVar = new ShareVar();
        macIP = shareVar.getMacIP();
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

        connectPoint();

        // 콤마 찍어서 원화로 바꿔줌!
        NumberFormat moneyFormat = NumberFormat.getInstance(Locale.KOREA);
        strTotal = moneyFormat.format(totalPrice);
        strPoint = moneyFormat.format(point);

        // layout 설정
        cardBtn = findViewById(R.id.beforePay_Btn_Card);
        tv_totalOrderPrice = findViewById(R.id.beforePay_TV_totalOrderPrice);
        tv_totalPayPrice = findViewById(R.id.beforePay_TV_totalPayPrice);
        tv_totalDiscount = findViewById(R.id.beforePay_TV_Discount);
        et_point = findViewById(R.id.beforePay_TV_Point);
        btn_point = findViewById(R.id.activity_Before_Btn_Point);

        // 값 대입
        tv_totalOrderPrice.setText(strTotal + "원");
        tv_totalPayPrice.setText(strTotal + "원");

        if(point == 0){
            btn_point.setEnabled(false);
        }
        et_point.setHint("보유 포인트 : " + strPoint + "p");

        // et_point 값 제한

        et_point.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.v(TAG, " 입력값 : " + et_point.getText().toString());
                if(et_point.getText().length() == 0){ // 아무것도 입력되지 않았을 때 입력처리
                    btn_point.setEnabled(false);
                }else { // 입력했을 때
                    Log.v(TAG, " 값!! : " + Integer.parseInt(et_point.getText().toString()));
                    if (Integer.parseInt(et_point.getText().toString()) < 1) { // 1보다 작게 입력했을 때 예외처리

                    } else {
                        int temp = Integer.parseInt(s.toString());

                        if (point < temp) { // 사용하려는 포인트가 보유 포인트보다 클 때
                            et_point.setText("");
                            Toast.makeText(BeforePayActivity.this, "보유하신 포인트는 " + strPoint + "p 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (totalPrice < temp) { // 사용하려는 포인트가 totalPrice를 초과했을 때!
                                btn_point.setEnabled(false);
                                btn_point.setBackgroundColor(Color.parseColor("#28979595"));
                                Toast.makeText(BeforePayActivity.this, "최대 " + strTotal + "p 까지 사용가능합니다.", Toast.LENGTH_SHORT).show();
                            } else if (totalPrice >= temp){
                                btn_point.setEnabled(true);
                                btn_point.setBackgroundColor(Color.parseColor("#0084ff"));
                            }else if(point < temp){
                                et_point.setText("");
                                Toast.makeText(BeforePayActivity.this, "보유하신 포인트는 " + strPoint + "p 입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 클릭 리스너
        cardBtn.setOnClickListener(mClickListener);
        btn_point.setOnClickListener(mClickListener);

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

            int getPoint;
            int discountedPrice;
            int remainPoint;
            NumberFormat moneyFormat = NumberFormat.getInstance(Locale.KOREA);

            switch (v.getId()){
                case R.id.activity_Before_Btn_Point:

                    if(btn_point.getText().toString().equals("적용")){
                        getPoint = Integer.parseInt(et_point.getText().toString());
                        discountedPrice = totalPrice - getPoint;
                        remainPoint = point - getPoint;

                        strTotal = moneyFormat.format(discountedPrice);
                        strDiscountPoint = moneyFormat.format(getPoint);
                        strRemainPoint = moneyFormat.format(remainPoint);

                        et_point.setEnabled(false);
                        et_point.setText("");
                        et_point.setHint("남은 포인트 : " + strRemainPoint + "p");
                        tv_totalDiscount.setText(strDiscountPoint + "p");
                        tv_totalPayPrice.setText(strTotal + "원");
                        btn_point.setEnabled(true);
                        btn_point.setText("초기화");
                    }else{

                        strTotal = moneyFormat.format(totalPrice);

                        Log.v(TAG, "totalPrice : " + totalPrice);

                        et_point.setEnabled(true);
                        et_point.setText("");
                        et_point.setHint("보유 포인트 : " + strPoint + "p");
                        tv_totalDiscount.setText("0p");
                        tv_totalPayPrice.setText(strTotal + "원");
                        btn_point.setText("적용");
                        btn_point.setEnabled(false);
                    }
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



                    where = "oNo";
                    urlAddr = "http://" + macIP + ":8080/tify/lmw_orderoNo_select.jsp?user_uNo=" + user_uSeqNo;
                    list = connectGetData(); // order Select onCreate할 때 미리 마지막 번호 찾아와서 +1하기
                    if (list.size() == 0){
                        oNo = 1;
                    }else{
                        oNo = list.get(0).getMax();
                    }
                    Log.v(TAG, "마지막 oNo : " + oNo);


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

    private void connectPoint(){
        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_point_select.jsp?user_uSeqNo=" + user_uSeqNo;
        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_PointNetworkTask networkTask = new LMW_PointNetworkTask(BeforePayActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            point = (Integer) obj;
            Log.v(TAG, "point) : " + point);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
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
        title.setText("주문하기");

        ImageButton cart = findViewById(R.id.cart);
        cart.setVisibility(View.INVISIBLE);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
}