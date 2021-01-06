
package com.example.tify.Minwoo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Minwoo.Adapter.OrderListAdapter;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.R;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {

    String TAG = "OrderListActivity";

    private ArrayList<OrderList> data = null;
    private OrderListAdapter adapter = null;
    private ListView listView = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_order_list);
        Log.v(TAG, "OrderListActivity onCreate");

        data = new ArrayList<OrderList>();

        data.add(new OrderList(1,"2021-01-12",1, "제조완료"));
        data.add(new OrderList(2,"2021-01-12",1, "주문접수"));
        data.add(new OrderList(3,"2021-01-12",2, "주문접수"));
        data.add(new OrderList(4,"2021-01-12",1, "제조완료"));
        data.add(new OrderList(5,"2021-01-12",2, "주문요청"));
        data.add(new OrderList(6,"2021-01-12",3, "제조완료"));

        //리사이클러뷰에 있는 아이디를 찾기
        recyclerView = findViewById(R.id.orderList_recycler_view);

        //리사이클러뷰 규격 만들기
        recyclerView.setHasFixedSize(true);

        //레이아웃 매니저 만들기
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //리사이클러뷰 어댑터를 넣기
        adapter = new OrderListAdapter(OrderListActivity.this, R.layout.lmw_activity_orderlist_recycler_item, data);

        //어댑터에게 보내기
        recyclerView.setAdapter(adapter);

        // 기본 구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

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
            startActivity(intent);
        }
    };
}