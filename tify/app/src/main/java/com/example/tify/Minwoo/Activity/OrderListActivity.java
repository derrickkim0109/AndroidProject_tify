
package com.example.tify.Minwoo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Minwoo.Adapter.CartAdapter;
import com.example.tify.Minwoo.Adapter.OrderListAdapter;
import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.Minwoo.NetworkTask.LMW_CartNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderNetworkTask;
import com.example.tify.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderListActivity extends AppCompatActivity {

    String TAG = "OrderListActivity";

    private ArrayList<Order> data = null;
    private OrderListAdapter adapter = null;
    private ListView listView = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;

    private ArrayList<Order> list;

    String macIP;
    String urlAddr = null;
    String where = null;
    int user_uSeqNo = 0;
    int store_sSeqNo = 0;
    String from;

    TextView tv_UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_order_list);
        Log.v(TAG, "OrderListActivity onCreate");

        // 고객 이름
//        tv_UserName = findViewById(R.id.activity_OrderList_TV_cName);
//        tv_UserName.setText(); // 값 받아서 넣기

        // 받은 값 저장
        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        user_uSeqNo = intent.getIntExtra("user_uSeqNo", 0);
        store_sSeqNo = intent.getIntExtra("store_sSeqNo", 0);

        if (intent.getStringExtra("from") == null){

        }else{
            from = intent.getStringExtra("from");
            connectDeleteData(); // 카트 비우기
        }

        //리사이클러뷰에 있는 아이디를 찾기
        recyclerView = findViewById(R.id.orderList_recycler_view);

        //리사이클러뷰 규격 만들기
        recyclerView.setHasFixedSize(true);

        //레이아웃 매니저 만들기
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 기본 구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        list = new ArrayList<Order>();
        list = connectGetData(); // db를 통해 받은 데이터를 담는다.

        Log.v(TAG, "list size : " + list.size());

        if(list.size() == 0){
            finish();

            Intent intent1 = new Intent(OrderListActivity.this, EmptyOrderListActivity.class);
            startActivity(intent1);
        }

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.orderlist_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        adapter.setOnItemClickListener(mClickListener);
    }

    OrderListAdapter.OnItemClickListener mClickListener = new OrderListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);

            int order_oNo = list.get(position).getoNo();
            String order_oInsertDate = list.get(position).getoInsertDate();

            intent.putExtra("macIP", macIP);
            intent.putExtra("user_uSeqNo", user_uSeqNo);
            intent.putExtra("store_sSeqNo", store_sSeqNo);
            intent.putExtra("order_oNo", order_oNo);
            intent.putExtra("order_oInsertDate", order_oInsertDate);



            startActivity(intent);
        }
    };

    private ArrayList<Order> connectGetData(){
        ArrayList<Order> beanList = new ArrayList<Order>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_order_select.jsp?user_uNo=" + user_uSeqNo;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_OrderNetworkTask networkTask = new LMW_OrderNetworkTask(OrderListActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            data = (ArrayList<Order>) obj;
            Log.v(TAG, "data.size() : " + data.size());

            adapter = new OrderListAdapter(OrderListActivity.this, R.layout.lmw_activity_orderlist_recycler_item, data);
            recyclerView.setAdapter(adapter);

            beanList = data;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    private String connectDeleteData(){ // 선택한 메뉴 삭제 or 전체 삭제
        String result = null;

        urlAddr = "http://" + macIP + ":8080/tify/lmw_cartlist_delete_all.jsp?user_uSeqNo=" + user_uSeqNo + "&store_sSeqNo=" + store_sSeqNo;
        where = "delete";

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where <- "insert"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_CartNetworkTask networkTask = new LMW_CartNetworkTask(OrderListActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            Object obj = networkTask.execute().get();
            result = (String) obj;
            ///////////////////////////////////////////////////////////////////////////////////////

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}