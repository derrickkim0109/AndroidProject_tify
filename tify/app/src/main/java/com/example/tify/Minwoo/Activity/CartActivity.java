package com.example.tify.Minwoo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Minwoo.Adapter.CartAdapter;
import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    String TAG = "CartActivity";

    private ArrayList<Cart> data = null;
    private CartAdapter adapter = null;
    private ListView listView = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;

    Button payBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_cart);
        Log.v(TAG, "CartActivity onCreate");

        data = new ArrayList<Cart>();

        data.add(new Cart("아메리카노(COLD)","+사이즈업",null, "얼음 조금만 넣어주세요", 5,4000));
        data.add(new Cart("아메리카노(HOT)","+사이즈업","+샷추가", "미지근하게 해주세요", 1,5000));
        data.add(new Cart("라떼(COLD)","+사이즈업",null, null, 3,4000));
        data.add(new Cart("라떼(HOT)","+사이즈업","+샷추가", null, 1,4000));
        data.add(new Cart("치즈케이크","+사이즈업",null, "포크 2개 넣어주세요", 1,5500));
        data.add(new Cart("밀크티(COLD)","+사이즈업",null, null, 1,5000));

        //리사이클러뷰에 있는 아이디를 찾기
        recyclerView = findViewById(R.id.cart_recycler_view);

        //리사이클러뷰 규격 만들기
        recyclerView.setHasFixedSize(true);

        //레이아웃 매니저 만들기
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //리사이클러뷰 어댑터를 넣기
        adapter = new CartAdapter(CartActivity.this, R.layout.lmw_activity_cart_recycler_item, data);

        //어댑터에게 보내기
        recyclerView.setAdapter(adapter);

        // 기본 구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.cart_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        payBtn = findViewById(R.id.cart_Btn_Pay);
        payBtn.setOnClickListener(mClickListener);
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
            Intent intent = new Intent(CartActivity.this, OrderListActivity.class);
            startActivity(intent);
        }
    };
}