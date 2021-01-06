package com.example.tify.Minwoo.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Minwoo.Adapter.OrderDetailAdapter;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.R;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {

    String TAG = "OrderDetailActivity";

    private ArrayList<OrderList> data = null;
    private OrderDetailAdapter adapter = null;
    private ListView listView = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_order_detail);
        Log.v(TAG, "OrderDetailActivity onCreate");

        data = new ArrayList<OrderList>();

        data.add(new OrderList(1,"2021-01-12","아메리카노(COLD)",null,null,"얼음 조금만", "스타벅스", 4000));
        data.add(new OrderList(1,"2021-01-12","아메리카노(COLD)","+사이즈업",null,null, "스타벅스", 4000));
        data.add(new OrderList(1,"2021-01-12","아메리카노(COLD)",null,"+샷추가",null, "스타벅스", 4000));
        data.add(new OrderList(1,"2021-01-12","아메리카노(COLD)","+사이즈업","+샷추가","얼음 조금만", "스타벅스", 4000));

        //리사이클러뷰에 있는 아이디를 찾기
        recyclerView = findViewById(R.id.orderDetail_recycler_view);

        //리사이클러뷰 규격 만들기
        recyclerView.setHasFixedSize(true);

        //레이아웃 매니저 만들기
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //리사이클러뷰 어댑터를 넣기
        adapter = new OrderDetailAdapter(OrderDetailActivity.this, R.layout.lmw_activity_orderdetail_recycler_item, data);

        //어댑터에게 보내기
        recyclerView.setAdapter(adapter);

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
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
