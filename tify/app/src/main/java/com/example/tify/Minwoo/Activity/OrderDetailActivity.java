package com.example.tify.Minwoo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Minwoo.Adapter.OrderDetailAdapter;
import com.example.tify.Minwoo.Adapter.OrderListAdapter;
import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderNetworkTask;
import com.example.tify.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

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

    // layout
    TextView tv_UserName;
    TextView tv_TotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_order_detail);
        Log.v(TAG, "OrderDetailActivity onCreate");

        // 받은 값 저장
        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        user_uSeqNo = intent.getIntExtra("user_uSeqNo", 0);
        store_sSeqNo = intent.getIntExtra("store_sSeqNo", 0);
        order_oNo = intent.getIntExtra("order_oNo", 0);
        order_oInsertDate = intent.getStringExtra("order_oInsertDate");

        // layout
        tv_UserName = findViewById(R.id.activity_OrderDetail_TV_cName);
        tv_TotalPrice = findViewById(R.id.orderDetail_TV_TotalPrice);

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

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.orderdetil_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
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
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_OrderListNetworkTask networkTask = new LMW_OrderListNetworkTask(OrderDetailActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

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
}
