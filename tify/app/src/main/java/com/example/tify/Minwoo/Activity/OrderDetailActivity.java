package com.example.tify.Minwoo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Hyeona.Activity.qrActivity;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Minwoo.Adapter.OrderDetailAdapter;
import com.example.tify.Minwoo.Adapter.OrderListAdapter;
import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderNetworkTask;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Activity.MypageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    // 주문 상세 내역 화면

    String TAG = "OrderDetailActivity";

    private ArrayList<OrderList> data = null;
    private OrderDetailAdapter adapter = null;
    private ListView listView = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;

    private ArrayList<OrderList> list;

    String macIP;
    String urlAddr = null;
    String where = null;
    int user_uSeqNo = 0;
    int store_sSeqNo = 0;
    int order_oNo = 0;
    String order_oInsertDate = null;
    String userName;

    // layout
    TextView tv_UserName;
    TextView tv_TotalPrice;

    BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_order_detail);
        Log.v(TAG, "OrderDetailActivity onCreate");
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //하단 네비게이션 선언
        menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) { //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.//
                    case R.id.action1: {
                        Intent intent = new Intent(OrderDetailActivity.this, JiseokMainActivity.class);
                        startActivity(intent);
                        //홈버튼
                        return true;
                    }
                    case R.id.action2: {
                        //주문내역
                        Intent intent = new Intent(OrderDetailActivity.this, OrderListActivity.class);
                        intent.putExtra("from", "JiseokMainActivity"); // value에 어디서 보내는지를 적어주세요
                        startActivity(intent);
                        finish();

                        return true;
                    }
                    case R.id.action3: {
                        Intent intent = new Intent(OrderDetailActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸 여기는 즐겨찾기 부분
                        return true;
                    }

                    case R.id.action4: {
                        Intent intent = new Intent(OrderDetailActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸

                        return true;
                    }
                    case R.id.action5: {
                        //마이페이지
                        Intent intent = new Intent(OrderDetailActivity.this, MypageActivity.class);
                        startActivity(intent);

                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
        // 받은 값 저장
        Intent intent = getIntent();
        ShareVar shareVar = new ShareVar();
        macIP = shareVar.getMacIP();
        user_uSeqNo = intent.getIntExtra("user_uSeqNo", 0);
        store_sSeqNo = intent.getIntExtra("store_sSeqNo", 0);
        order_oNo = intent.getIntExtra("order_oNo", 0);
        order_oInsertDate = intent.getStringExtra("order_oInsertDate");

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        userName = auto.getString("userNickName",null); // 지석씨랑 화면 연결되면 쓰기

        // layout
        tv_UserName = findViewById(R.id.activity_OrderDetail_TV_cName);
        tv_TotalPrice = findViewById(R.id.orderDetail_TV_TotalPrice);

        tv_UserName.setText(userName + "님의 주문내역");

        //리사이클러뷰에 있는 아이디를 찾기
        recyclerView = findViewById(R.id.orderDetail_recycler_view);

        //리사이클러뷰 규격 만들기
        recyclerView.setHasFixedSize(true);

        //레이아웃 매니저 만들기
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<OrderList>();
        list = connectGetData(); // db를 통해 받은 데이터를 담는다.

        // 기본 구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작

                Intent intent = new Intent(OrderDetailActivity.this, OrderListActivity.class);

                intent.putExtra("macIP", macIP);
                intent.putExtra("user_uSeqNo", user_uSeqNo);
                intent.putExtra("store_sSeqNo", store_sSeqNo);

                startActivity(intent);

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<OrderList> connectGetData(){
        ArrayList<OrderList> beanList = new ArrayList<OrderList>();

        int total = 0;

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_orderlist_select.jsp?user_uNo=" + user_uSeqNo + "&order_oNo=" + order_oNo;

        try {

            LMW_OrderListNetworkTask networkTask = new LMW_OrderListNetworkTask(OrderDetailActivity.this, urlAddr, where);

            Object obj = networkTask.execute().get();
            data = (ArrayList<OrderList>) obj;
            Log.v(TAG, "data.size() : " + data.size());

            adapter = new OrderDetailAdapter(OrderDetailActivity.this, R.layout.lmw_activity_orderdetail_recycler_item, data, order_oNo, order_oInsertDate);
            recyclerView.setAdapter(adapter);

            for(int i = 0; i < data.size(); i++){
                total = total + data.get(i).getOlPrice();
            }

            NumberFormat moneyFormat = null;
            moneyFormat = NumberFormat.getInstance(Locale.KOREA);
            String strTotal = moneyFormat.format(total);

            tv_TotalPrice.setText(strTotal + "원");

            beanList = data;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
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
        title.setText("주문내역");

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
