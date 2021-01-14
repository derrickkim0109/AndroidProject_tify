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


    String TAG = "BeforePayActivity";

    // 통신
    private Handler mHandler;
    InetAddress serverAddr;
    Socket socket;
    PrintWriter sendWriter;
    private String ip = "211.195.53.163";
    private int port = 8888;
    String strStatus = null;

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

    // 통신 ------------- 소켓 닫는 거
//    @Override
//    protected void onStop() {
//        super.onStop();
//        try {
////            sendWriter.close();
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    // -----------------------------

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


        // 통신 ------------------------------------------------
        ///////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////
//        mHandler = new Handler();

//        new Thread() {
//            public void run() { // 받는 스레드
//                try {
//                    Log.v("통신 순서", "순서 1 - showOrder");
//                    InetAddress serverAddr = InetAddress.getByName(ip);
//                    socket = new Socket(serverAddr, port);
//                    sendWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"euc-kr")),true);
//                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(),"euc-kr"));
//                    while(true){
//                        strStatus = input.readLine();
//                        Log.v(TAG, "Customer 받은 값 : " + strStatus);
//
//                        if(strStatus!=null){ // 점주가 변화를 줄 때 반응하는 부분 (여길 바꿔보자)
//                            mHandler.post(new msgUpdate(strStatus));
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } }}.start();

//        if(strStatus != null){ // 점주가 요청에 반응했을 때
//            Toast.makeText(BeforePayActivity.this, "주문이 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show();
//        }
        ///////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////

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
        et_point.setHint(strPoint);
        btn_point.setBackgroundColor(Color.parseColor("#FF999999"));

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
                        et_point.setHint(strRemainPoint);
                        tv_totalDiscount.setText(strDiscountPoint + "원");
                        tv_totalPayPrice.setText(strTotal + "원");
                        btn_point.setEnabled(true);
                        btn_point.setText("초기화");
                        btn_point.setBackgroundColor(Color.parseColor("#0084ff"));
                    }else{

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
                case R.id.beforePay_Btn_Card:
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
                    // 통신 -------------------------- 점주에게 접수 요청
//                    strStatus = "주문이 들어왔습니다!! \n주문내역을 확인해주세요.";
//                    Log.v(TAG, "Customer 주는 값 : " + strStatus);
//                    new Thread() { // 주는 스레드
//                        @Override
//                        public void run() {
//                            super.run();
//                            try {
//                                sendWriter.println(strStatus);
//                                sendWriter.flush();
////                            message.setText("");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
                    // ------------------------------


                    // 결제 부분 완료되면 결제 부분에서 JSP 실행하기 (카드번호랑 카드이름 필요)
                    // 1. 결제 완료되면 OrderListActivity로!
                    // 1-1. order 테이블 Insert
                    // 1-2. order oNo Select => oNo가 있어야 orderlist에 Insert 가능..
                    // 1-3. orderlist 테이블 Insert
                    // 2. 결제 실패하면 StoreInfoActivity로!

                    // 테스트 ---------
                    // order 테이블 Insert
//                    where = "insert";
//                    int cardNum = 13153123;
//                    String cardName = "신한은행";
//                    urlAddr = "http://" + macIP + ":8080/tify/lmw_order_insert.jsp?user_uNo=" + user_uSeqNo + "&store_sSeqNo=" + store_sSeqNo + "&store_sName=" + sName + "&oSum=" + totalPrice + "&oCardName=" + cardName + "&oCardNo=" + cardNum + "&oReview=" + 0 + "&oStatus=" + 0;
//
//                    connectInsertData(); // order Insert
//
//                    where = "oNo";
//                    urlAddr = "http://" + macIP + ":8080/tify/lmw_orderoNo_select.jsp?user_uNo=" + user_uSeqNo;
//                    list = connectGetData(); // order Select onCreate할 때 미리 마지막 번호 찾아와서 +1하기
//                    if (list.size() == 0){
//                        oNo = 1;
//                    }else{
//                        oNo = list.get(0).getMax();
//                    }
//                    Log.v(TAG, "마지막 oNo : " + oNo);

//                    // oNo Select
                        where = "select";
                        urlAddr = "http://" + macIP + ":8080/tify/lmw_orderoNo_select.jsp?user_uNo=" + user_uSeqNo;

//                    list = connectGetData(); // order Select onCreate할 때 미리 마지막 번호 찾아와서 +1하기
//                    int oNo = list.get(0).getoNo();
//                    Log.v(TAG, "받은 oNo : " + oNo);

                    // orderlist 테이블 Insert
//                    where = "insert";
//                    urlAddr = "http://" + macIP + ":8080/tify/lmw_orderlist_insert.jsp?user_uNo=" + user_uSeqNo + "&order_oNo=" + oNo + "&store_sSeqNo=" + store_sSeqNo + "&store_sName=" + sName + "&menu_mName=" + menu_mName + "&olSizeUp=" + olSizeUp + "&olAddShot=" + olAddShot + "&olRequest=" + olRequest + "&olPrice=" + olPrice + "&olQuantity=" + olQuantity;
//
//                    connectInsertData(); // orderlist Insert
//
//                    // 테스트용
//                    intent = new Intent(BeforePayActivity.this, OrderListActivity.class);
//                    intent.putExtra("macIP", macIP);
//                    intent.putExtra("user_uSeqNo", user_uSeqNo);
//                    intent.putExtra("store_sSeqNo", store_sSeqNo);
//                    intent.putExtra("totalPrice", totalPrice);
//                    intent.putExtra("user_uSeqNo", user_uSeqNo);
//                    intent.putExtra("from", "BeforePayActivity");
//                    startActivity(intent);

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

                    if(discountedPrice == 0){
                        intent.putExtra("total", totalPrice);
                    }else{
                        intent.putExtra("total", discountedPrice);
                    }
                    Log.v(TAG, "total : " + discountedPrice);
                    Log.v(TAG, "total : " + totalPrice);

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

    // 통신 ------------------------------------------
//    class msgUpdate implements Runnable{ // 받아서 작동하는 메소드
//        private String msg;
//        public msgUpdate(String str) {this.msg=str;}
//
//        @Override
//        public void run() {
//            Log.v("통신 순서", "순서 2 - msgUpdate");
//            Log.v(TAG, "msgUpdate msg : " + msg);
////            status.setText(status.getText().toString()+msg+"\n");
//
////            AlertDialog.Builder builder = new AlertDialog.Builder(BeforePayActivity.this);
////            builder.setTitle("주문 결과");
////            builder.setMessage(msg + " \n test"); // 문장이 길 때는 String에 넣어서 사용하면 된다.
////            builder.setIcon(R.mipmap.ic_launcher); // 아이콘은 mipmap에 넣고 사용한다.
////            builder.show();
////
////            Toast.makeText(BeforePayActivity.this, "주문이 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show();
//
//        }
//    }
    // -----------------------------------------------
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