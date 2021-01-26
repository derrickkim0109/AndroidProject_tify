package com.example.tify.Minwoo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
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

import com.example.tify.Hyeona.Activity.qrActivity;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Jiseok.Activity.PaymentPayPasswordActivity;
import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.Minwoo.NetworkTask.LMW_CartNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_PointNetworkTask;
import com.example.tify.R;
import com.example.tify.ShareVar;

import com.example.tify.Taehyun.Activity.MypageActivity;
import com.example.tify.Taehyun.Activity.Mypage_CardDetailActivity;
import com.example.tify.Taehyun.Activity.OrderPage_PaymentActivity;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BeforePayActivity2 extends AppCompatActivity {

// 장바구니 결제할 때 사용되는 페이지(리스트를 모두 Insert해야하므로 과부하를 줄이기 위해 BeforeActivity와 구분했음.

    String TAG = "BeforePayActivity2";

    private ArrayList<Order> list;
    private ArrayList<OrderList> orderLists;
    private ArrayList<Cart> carts;

    // layout
    TextView tv_totalOrderPrice2;
    TextView tv_totalPayPrice2;
    TextView tv_totalDiscount2;
    Button cardBtn2;
    Button btn_point;
    EditText et_point;

    // order Insert
    String macIP;
    String urlAddr = null;
    String where = null;
    int user_uSeqNo = 0;
    int store_sSeqNo = 0;
    String sName;

    String from;
    int total;
    int point;
    int getPoint;

    String strPoint;
    String strDiscountPoint;
    String strRemainPoint;
    String strTotal;
    int discountedPrice;

    BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_before_pay2);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //하단 네비게이션 선언
        menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) { //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.//
                    case R.id.action1: {
                        Intent intent = new Intent(BeforePayActivity2.this, JiseokMainActivity.class);
                        startActivity(intent);
                        //홈버튼
                        return true;
                    }
                    case R.id.action2: {
                        //주문내역
                        Intent intent = new Intent(BeforePayActivity2.this, OrderListActivity.class);
                        intent.putExtra("from", "JiseokMainActivity"); // value에 어디서 보내는지를 적어주세요
                        startActivity(intent);
                        finish();

                        return true;
                    }
                    case R.id.action3: {
                        Intent intent = new Intent(BeforePayActivity2.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸 여기는 즐겨찾기 부분
                        return true;
                    }

                    case R.id.action4: {
                        Intent intent = new Intent(BeforePayActivity2.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸

                        return true;
                    }
                    case R.id.action5: {
                        //마이페이지
                        Intent intent = new Intent(BeforePayActivity2.this, MypageActivity.class);
                        startActivity(intent);

                        return true;
                    }
                    default:
                        return false;
                }
            }
        });

        // CartActivity로 부터 값을 받는다.
        Intent intent = getIntent();

        ShareVar shareVar = new ShareVar();
        macIP = shareVar.getMacIP();
        user_uSeqNo = intent.getIntExtra("user_uSeqNo", 0);
        store_sSeqNo = intent.getIntExtra("store_sSeqNo", 0);
        total = intent.getIntExtra("total", 0);
        sName = intent.getStringExtra("sName");
        from = intent.getStringExtra("from");

        Log.v(TAG, "유저번호 : " + user_uSeqNo);

        where = "oNo";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_orderoNo_select.jsp?user_uNo=" + user_uSeqNo;
        list = connectGetData(); // 장바구니 리스트 불러오기

        connectPoint(); // 포인트 불러오기

        // layout 설정
        cardBtn2 = findViewById(R.id.beforePay_Btn_Card2);
        tv_totalOrderPrice2 = findViewById(R.id.beforePay_TV_totalOrderPrice2);
        tv_totalPayPrice2 = findViewById(R.id.beforePay_TV_totalPayPrice2);
        tv_totalDiscount2 = findViewById(R.id.beforePay_TV_Discount2);
        btn_point = findViewById(R.id.activity_Before2_Btn_Point);
        et_point = findViewById(R.id.beforePay_TV_Point2);

        // 값 대입
        NumberFormat moneyFormat = null;
        moneyFormat = NumberFormat.getInstance(Locale.KOREA);
        strTotal = moneyFormat.format(total);
        strPoint = moneyFormat.format(point);

        tv_totalOrderPrice2.setText(strTotal + "원");
        tv_totalPayPrice2.setText(strTotal + "원");

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
                    btn_point.setBackgroundColor(Color.parseColor("#FF999999"));
                }else { // 입력했을 때
                    Log.v(TAG, " 값!! : " + Integer.parseInt(et_point.getText().toString()));
                    if (Integer.parseInt(et_point.getText().toString()) < 1) { // 1보다 작게 입력했을 때 예외처리

                    } else {
                        int temp = Integer.parseInt(s.toString());

                        if (point < temp) { // 사용하려는 포인트가 보유 포인트보다 클 때
                            et_point.setText("");
                            Toast.makeText(BeforePayActivity2.this, "보유하신 포인트는 " + strPoint + "p 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (total < temp) { // 사용하려는 포인트가 totalPrice를 초과했을 때!
                                btn_point.setEnabled(false);
                                btn_point.setBackgroundColor(Color.parseColor("#28979595"));
                                Toast.makeText(BeforePayActivity2.this, "최대 " + strTotal + "p 까지 사용가능합니다.", Toast.LENGTH_SHORT).show();
                            } else if (total >= temp){
                                btn_point.setEnabled(true);
                                btn_point.setBackgroundColor(Color.parseColor("#0084ff"));
                            }else if(point < temp){
                                et_point.setText("");
                                Toast.makeText(BeforePayActivity2.this, "보유하신 포인트는 " + strPoint + "p 입니다.", Toast.LENGTH_SHORT).show();
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
        cardBtn2.setOnClickListener(mClickListener);
        btn_point.setOnClickListener(mClickListener);

        if(point == 0){
            btn_point.setEnabled(false);
        }
        et_point.setHint(strPoint);

        carts = new ArrayList<Cart>();
        carts = CartConnectGetData(); // db를 통해 받은 데이터를 담는다.
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


            int remainPoint;
            NumberFormat moneyFormat = NumberFormat.getInstance(Locale.KOREA);

            switch (v.getId()){
                case R.id.activity_Before2_Btn_Point:

                    if(btn_point.getText().toString().equals("적용")){
                        getPoint = Integer.parseInt(et_point.getText().toString());
                        discountedPrice = total - getPoint;
                        remainPoint = point - getPoint;

                        strTotal = moneyFormat.format(discountedPrice);
                        strDiscountPoint = moneyFormat.format(getPoint);
                        strRemainPoint = moneyFormat.format(remainPoint);

                        Log.v(TAG, "값 1 : " + strTotal);
                        Log.v(TAG, "값 2 : " + strDiscountPoint);
                        Log.v(TAG, "값 3 : " + strRemainPoint);

                        et_point.setEnabled(false);
                        et_point.setText("");
                        et_point.setHint(strRemainPoint);
                        tv_totalDiscount2.setText(strDiscountPoint + "원");
                        tv_totalPayPrice2.setText(strTotal + "원");
                        btn_point.setEnabled(true);
                        btn_point.setText("초기화");
                        btn_point.setBackgroundColor(Color.parseColor("#0084ff"));
                    }else{

                        strTotal = moneyFormat.format(total);

                        Log.v(TAG, "totalPrice : " + total);

                        et_point.setEnabled(true);
                        et_point.setText("");
                        et_point.setHint(strPoint);
                        tv_totalDiscount2.setText("0원");
                        tv_totalPayPrice2.setText(strTotal + "원");
                        btn_point.setText("적용");
                        btn_point.setEnabled(false);
                    }
                    break;

                case R.id.beforePay_Btn_Card2:
                    if(cardCountselect()==0){
                        new AlertDialog.Builder(BeforePayActivity2.this)
                                .setTitle("카드없어.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent1 = new Intent(BeforePayActivity2.this, Mypage_CardDetailActivity.class);
                                        intent1.putExtra("uNo",user_uSeqNo);
                                        startActivity(intent1);
                                    }
                                })
                                .show();
                        break;
                    }

                    intent = new Intent(BeforePayActivity2.this, OrderPage_PaymentActivity.class);
                    intent.putExtra("Carts", carts);
                    intent.putExtra("from", "BeforePayActivity2");
                    intent.putExtra("store_sSeqNo", store_sSeqNo);
                    intent.putExtra("sName", sName);

                    if(discountedPrice == 0){
                        intent.putExtra("total", total);
                    }else{
                        intent.putExtra("total", discountedPrice);
                    }
                    Log.v(TAG, "total : " + discountedPrice);
                    Log.v(TAG, "total : " + total);

                    if(getPoint == 0){
                        intent.putExtra("point", 0);
                    }else{
                        intent.putExtra("point", getPoint);
                    }
                    startActivity(intent);
                    break;
            }

        }
    };

    private ArrayList<Order> connectGetData(){
        ArrayList<Order> beanList = new ArrayList<Order>();

        try {
            LMW_OrderNetworkTask networkTask = new LMW_OrderNetworkTask(BeforePayActivity2.this, urlAddr, where);


            Object obj = networkTask.execute().get();
            list = (ArrayList<Order>) obj;
            Log.v(TAG, "data.size() : " + list.size());

            beanList = list;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    private ArrayList<Cart> CartConnectGetData(){
        ArrayList<Cart> beanList = new ArrayList<Cart>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_cartlist_select.jsp?user_uSeqNo=" + user_uSeqNo;

        try {
            LMW_CartNetworkTask networkTask = new LMW_CartNetworkTask(BeforePayActivity2.this, urlAddr, where);

            Object obj = networkTask.execute().get();
            carts = (ArrayList<Cart>) obj;

            Cart cart = new Cart(carts);
            Log.v(TAG, "data.size() : " + carts.size());


            beanList = carts;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    private void connectPoint(){
        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_point_select.jsp?user_uSeqNo=" + user_uSeqNo;
        try {
            LMW_PointNetworkTask networkTask = new LMW_PointNetworkTask(BeforePayActivity2.this, urlAddr, where);

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

    private int cardCountselect () {

        int cardcount = 0;
        try {

            String urlAddr = "http://" + macIP + ":8080/tify/mypage_cardcountselect.jsp?user_uNo=" + user_uSeqNo;
            Log.v("dddd",urlAddr);

            NetworkTask_TaeHyun countnetworkTask = new NetworkTask_TaeHyun(BeforePayActivity2.this, urlAddr, "count");
            Object obj = countnetworkTask.execute().get();

            cardcount = (int) obj;

        } catch (Exception e) {

        }
        return cardcount;
    }

}