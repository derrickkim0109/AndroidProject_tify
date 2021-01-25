package com.example.tify.Minwoo.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Hyeona.Activity.qrActivity;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Minwoo.Adapter.CartAdapter;
import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Minwoo.NetworkTask.LMW_CartNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderNetworkTask;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Activity.MypageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    // 장바구니 화면

    String TAG = "CartActivity";

    private ArrayList<Cart> data = null;
    private CartAdapter adapter = null;
    private ListView listView = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;

    private ArrayList<Cart> list;
    private ArrayList<Order> orders;

    // layout
    Button payBtn;
    TextView tv_sName;
    ImageView iv_Init;
    TextView tv_totalPrice;

    // OrderSummaryActivity로 부터 받을 값
    String sName;

    // cartlist RUD
    String macIP;
    String urlAddr = null;
    String where = null;
    int user_uSeqNo = 0;
    int store_sSeqNo = 0;

    String strTotal;
    int oNo;
    int total;

    BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_cart);
        Log.v(TAG, "CartActivity onCreate");


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //하단 네비게이션 선언
        menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) { //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.//
                    case R.id.action1: {
                        Intent intent = new Intent(CartActivity.this, JiseokMainActivity.class);
                        startActivity(intent);
                        //홈버튼
                        return true;
                    }
                    case R.id.action2: {
                        //주문내역
                        Intent intent = new Intent(CartActivity.this, OrderListActivity.class);
                        intent.putExtra("from", "JiseokMainActivity"); // value에 어디서 보내는지를 적어주세요
                        startActivity(intent);
                        finish();

                        return true;
                    }
                    case R.id.action3: {
                        Intent intent = new Intent(CartActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸 여기는 즐겨찾기 부분
                        return true;
                    }

                    case R.id.action4: {
                        Intent intent = new Intent(CartActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸

                        return true;
                    }
                    case R.id.action5: {
                        //마이페이지
                        Intent intent = new Intent(CartActivity.this, MypageActivity.class);
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
        sName = intent.getStringExtra("sName");
        ShareVar shareVar = new ShareVar();
        macIP = shareVar.getMacIP();
        user_uSeqNo = intent.getIntExtra("user_uSeqNo", 0);
        store_sSeqNo = intent.getIntExtra("store_sSeqNo", 0);

        // layout 세팅
        payBtn = findViewById(R.id.cart_Btn_Pay);
        tv_sName = findViewById(R.id.cart_TV_sName);
        tv_totalPrice = findViewById(R.id.cart_TV_TotalPrice);
        iv_Init = findViewById(R.id.cart_IV_Cancel);

        tv_sName.setText(sName);
        tv_totalPrice.setText(strTotal + "원");

        //리사이클러뷰에 있는 아이디를 찾기
        recyclerView = findViewById(R.id.cart_recycler_view);

        //리사이클러뷰 규격 만들기
        recyclerView.setHasFixedSize(true);

        //레이아웃 매니저 만들기
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 기본 구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        list = new ArrayList<Cart>();
        list = connectGetData(); // db를 통해 받은 데이터를 담는다.

        Log.v(TAG, "list size : " + list.size());

        if(list.size() == 0){
            finish();

            Intent intent1 = new Intent(CartActivity.this, EmptyCartActivity.class);
            intent.putExtra("macIP", macIP);
            intent.putExtra("user_uSeqNo", user_uSeqNo);
            intent.putExtra("store_sSeqNo", store_sSeqNo);
            startActivity(intent1);
        }

        // 클릭 리스너
        payBtn.setOnClickListener(mClickListener);
        iv_Init.setOnClickListener(mClickListener);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(CartActivity.this, StoreInfoActivity.class);
                startActivity(intent);
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
                case R.id.cart_Btn_Pay: // 결제 버튼을 눌렀을 경우
                    intent = new Intent(CartActivity.this, BeforePayActivity2.class);

                    intent.putExtra("sName", sName);
                    intent.putExtra("macIP", macIP);
                    intent.putExtra("user_uSeqNo", user_uSeqNo);
                    intent.putExtra("store_sSeqNo", store_sSeqNo);
                    intent.putExtra("total", total);
                    intent.putExtra("from", "CartActivity");

                    startActivity(intent);
                    break;
                case R.id.cart_IV_Cancel: // 장바구니 전체 삭제
                    Log.v(TAG, "클릭 확인");

                    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    builder.setTitle("<장바구니 전체삭제>");
                    builder.setMessage("장바구니에 담긴 모든 메뉴를 삭제하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            urlAddr = "http://" + macIP + ":8080/tify/lmw_cartlist_delete_all.jsp?user_uSeqNo=" + user_uSeqNo + "&store_sSeqNo=" + store_sSeqNo;
                            where = "delete";

                            connectDeleteData();
                            Intent intent = new Intent(CartActivity.this, CartActivity.class);
                            intent.putExtra("sName", sName);
                            intent.putExtra("macIP", macIP);
                            intent.putExtra("user_uSeqNo", user_uSeqNo);
                            intent.putExtra("store_sSeqNo", store_sSeqNo);
                            startActivity(intent);
                        }
                    });
                   builder.setNegativeButton("취소", null);
                   builder.create().show();
                   break;
            }

        }
    };

    private ArrayList<Cart> connectGetData(){
        ArrayList<Cart> beanList = new ArrayList<Cart>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_cartlist_select.jsp?user_uSeqNo=" + user_uSeqNo;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_CartNetworkTask networkTask = new LMW_CartNetworkTask(CartActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            data = (ArrayList<Cart>) obj;
            Log.v(TAG, "data.size() : " + data.size());

            adapter = new CartAdapter(CartActivity.this, R.layout.lmw_activity_cart_recycler_item, data, macIP);
            recyclerView.setAdapter(adapter);

            total = 0;

            for(int i = 0; i < data.size(); i++){
                total = total + data.get(i).getcLPrice();
            }

            NumberFormat moneyFormat = null;
            moneyFormat = NumberFormat.getInstance(Locale.KOREA);
            strTotal = moneyFormat.format(total);

            tv_totalPrice.setText(strTotal + "원");

            beanList = data;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    private ArrayList<Order> OrderConnectGetData(){
        ArrayList<Order> beanList = new ArrayList<Order>();

        where = "oNo";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_orderoNo_select.jsp?user_uNo=" + user_uSeqNo;

        try {
            LMW_OrderNetworkTask networkTask = new LMW_OrderNetworkTask(CartActivity.this, urlAddr, "oNo");

            Object obj = networkTask.execute().get();
            orders = (ArrayList<Order>) obj;
            Log.v(TAG, "data.size() : " + orders.size());

            beanList = orders;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    private String connectDeleteData(){ // 선택한 메뉴 삭제 or 전체 삭제
        String result = null;

        try {

            LMW_CartNetworkTask networkTask = new LMW_CartNetworkTask(CartActivity.this, urlAddr, where);

            Object obj = networkTask.execute().get();
            result = (String) obj;

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected void onResume() {
        super.onResume();

        adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) { // 장바구니 선택 삭제

                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("<장바구니 삭제>");
                builder.setMessage("이 메뉴를 장바구니에서 삭제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() { // 해당 메뉴 장바구니에서 삭제
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        urlAddr = "http://" + macIP + ":8080/tify/lmw_cartlist_delete_choose.jsp?user_uSeqNo=" + user_uSeqNo + "&cLNo=" + list.get(position).getcLNo();
                        where = "CartActivity";

                        connectDeleteData(); // 해당 메뉴 삭제

                        Intent intent = new Intent(CartActivity.this, CartActivity.class);

                        intent.putExtra("sName", sName);
                        intent.putExtra("macIP", macIP);
                        intent.putExtra("user_uSeqNo", user_uSeqNo);
                        intent.putExtra("store_sSeqNo", store_sSeqNo);

                        startActivity(intent);

                        Toast.makeText(CartActivity.this, "선택하신 메뉴가 장바구니에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.create().show();
            }
        });
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
        title.setText("장바구니");

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
//                Intent intent = new Intent(CartActivity.this, StoreInfoActivity.class);
//                intent.putExtra("macIP", macIP);
//                intent.putExtra("user_uSeqNo", user_uSeqNo);
//                intent.putExtra("store_sSeqNo", store_sSeqNo);
//                startActivity(intent);
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        return true;
    }
}