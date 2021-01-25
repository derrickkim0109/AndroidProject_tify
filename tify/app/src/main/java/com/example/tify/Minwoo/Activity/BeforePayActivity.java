package com.example.tify.Minwoo.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tify.Hyeona.Activity.qrActivity;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Minwoo.Bean.Order;
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


import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BeforePayActivity extends AppCompatActivity {

    // 메뉴 고르고 바로결제를 클릭했을 경우

    String TAG = "BeforePayActivity";

    private ArrayList<Order> list;

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

    int getPoint;
    int discountedPrice;
    String tempText;

    BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_before_pay);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //하단 네비게이션 선언
        menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) { //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.//
                    case R.id.action1: {
                        Intent intent = new Intent(BeforePayActivity.this, JiseokMainActivity.class);
                        startActivity(intent);
                        //홈버튼
                        return true;
                    }
                    case R.id.action2: {
                        //주문내역
                        Intent intent = new Intent(BeforePayActivity.this, OrderListActivity.class);
                        intent.putExtra("from", "JiseokMainActivity"); // value에 어디서 보내는지를 적어주세요
                        startActivity(intent);
                        finish();

                        return true;
                    }
                    case R.id.action3: {
                        Intent intent = new Intent(BeforePayActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸 여기는 즐겨찾기 부분
                        return true;
                    }

                    case R.id.action4: {
                        Intent intent = new Intent(BeforePayActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸

                        return true;
                    }
                    case R.id.action5: {
                        //마이페이지
                        Intent intent = new Intent(BeforePayActivity.this, MypageActivity.class);
                        startActivity(intent);

                        return true;
                    }
                    default:
                        return false;
                }
            }
        });

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

        connectPoint(); // 유저 포인트 불러오기

        // 세자리 콤마
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

        if(point == 0){ // 포인트가 없을 경우 버튼 비활성화
            btn_point.setEnabled(false);
        }
        et_point.setHint(strPoint);
        btn_point.setBackgroundColor(Color.parseColor("#FF999999"));

        // et_point 값 제한

        et_point.addTextChangedListener(new TextWatcher() { // 포인트 값 입력 변화에 따라 제한
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

                        if (point < temp) { // 사용하려는 포인트가 보유 포인트보다 클 때 (적어놓은 포인트 초기화)
                            et_point.setText("");
                            Toast.makeText(BeforePayActivity.this, "보유하신 포인트는 " + strPoint + "p 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (totalPrice < temp) { // 사용하려는 포인트가 총금액을 초과했을 때! (버튼 비활성화)
                                btn_point.setEnabled(false);
                                btn_point.setBackgroundColor(Color.parseColor("#28979595"));
                                Toast.makeText(BeforePayActivity.this, "최대 " + strTotal + "p 까지 사용가능합니다.", Toast.LENGTH_SHORT).show();
                            } else if (totalPrice >= temp){ // 총금액이 사용하려는 포인트 보다 많거나 같을 때 (정상 => 버튼 활성화)
                                btn_point.setEnabled(true);
                                btn_point.setBackgroundColor(Color.parseColor("#0084ff"));
                            }else if(point < temp){ // 총금액이 사용하려는 포인트보다 적을 때 (적어놓은 포인트 초기화)
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
        btn_point.setOnClickListener(mClickListener);
        cardBtn.setOnClickListener(mClickListener);

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
                case R.id.activity_Before_Btn_Point: // 포인트 사용을 위해 적용버튼을 눌렀을 경우

                    if(btn_point.getText().toString().equals("적용")){ // 포인트를 정상 사용할 수 있는 경우
                        getPoint = Integer.parseInt(et_point.getText().toString());
                        discountedPrice = totalPrice - getPoint;
                        remainPoint = point - getPoint;

                        strTotal = moneyFormat.format(discountedPrice);
                        strDiscountPoint = moneyFormat.format(getPoint);
                        strRemainPoint = moneyFormat.format(remainPoint);

                        et_point.setEnabled(false);
                        et_point.setText("");
                        et_point.setHint(strRemainPoint);
                        tv_totalDiscount.setText(strDiscountPoint + "원");
                        tv_totalPayPrice.setText(strTotal + "원");
                        btn_point.setEnabled(true);
                        btn_point.setText("초기화");
                        btn_point.setBackgroundColor(Color.parseColor("#0084ff"));
                    }else{ // 버튼 Text가 '초기화' 상태로 있는 경우

                        strTotal = moneyFormat.format(totalPrice);

                        Log.v(TAG, "totalPrice : " + totalPrice);

                        et_point.setEnabled(true);
                        et_point.setText("");
                        et_point.setHint(strPoint);
                        tv_totalDiscount.setText("0원");
                        tv_totalPayPrice.setText(strTotal + "원");
                        btn_point.setText("적용");
                        btn_point.setEnabled(false);
                    }
                    break;
                case R.id.beforePay_Btn_Card: // 카드 결제
                    if(cardCountselect()==0){
                        new AlertDialog.Builder(BeforePayActivity.this)
                                .setTitle("등록하신 카드가 없습니다. \n카드등록페이지로 이동합니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent1 = new Intent(BeforePayActivity.this, Mypage_CardDetailActivity.class);
                                        intent1.putExtra("uNo",user_uSeqNo);
                                        startActivity(intent1);
                                    }
                                })
                                .show();
                        break;
                    }

                    where = "select";
                    urlAddr = "http://" + macIP + ":8080/tify/lmw_orderoNo_select.jsp?user_uNo=" + user_uSeqNo;

                    intent = new Intent(BeforePayActivity.this, OrderPage_PaymentActivity.class);
                    intent.putExtra("olPrice", olPrice);
                    intent.putExtra("menu_mName", menu_mName);
                    intent.putExtra("olSizeUp", olSizeUp);
                    intent.putExtra("olAddShot", olAddShot);
                    intent.putExtra("olRequest", olRequest);
                    intent.putExtra("olQuantity", olQuantity);
                    intent.putExtra("sName", sName);
                    intent.putExtra("from", "BeforePayActivity");
                    intent.putExtra("store_sSeqNo", store_sSeqNo);

                    if(discountedPrice == 0){ // 할인된 금액이 없는 경우 총금액 보내기
                        intent.putExtra("total", totalPrice);
                    }else{ // 할인된 금액이 있는 경우 할인된 금액 보내기
                        intent.putExtra("total", discountedPrice);
                    }
                    Log.v(TAG, "total : " + discountedPrice);
                    Log.v(TAG, "total : " + totalPrice);

                    if(getPoint == 0){ // 포인트 사용 여부에 따라 다른 값 보내기
                        intent.putExtra("point", 0);
                    }else{
                        intent.putExtra("point", getPoint);
                    }
                    startActivity(intent);

                    break;
            }

        }
    };

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

    private int cardCountselect () {

        int cardcount = 0;
        try {

            String urlAddr = "http://" + macIP + ":8080/tify/mypage_cardcountselect.jsp?user_uNo=" + user_uSeqNo;
            Log.v("dddd",urlAddr);

            NetworkTask_TaeHyun countnetworkTask = new NetworkTask_TaeHyun(BeforePayActivity.this, urlAddr, "count");
            Object obj = countnetworkTask.execute().get();

            cardcount = (int) obj;

        } catch (Exception e) {

        }
        return cardcount;
    }
}