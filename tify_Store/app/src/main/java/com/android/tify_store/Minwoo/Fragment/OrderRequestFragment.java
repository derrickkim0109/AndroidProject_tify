package com.android.tify_store.Minwoo.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.tify_store.Minwoo.Adapter.OrderRequestAdapter;
import com.android.tify_store.Minwoo.Bean.OrderRequest;
import com.android.tify_store.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.android.tify_store.Minwoo.NetworkTask.LMW_OrderNetworkTask;
import com.android.tify_store.R;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrderRequestFragment extends Fragment {

    String TAG = "OrderRequestFragment";

    private ArrayList<OrderRequest> orderRequests = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderRequestAdapter mAdapter;

    private ArrayList<OrderRequest> list;

    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.lmw_fragment_order_request, container, false);

        // StoreInfoActivity로 부터 값을 받는다.
        Bundle bundle = getArguments();
        macIP = bundle.getString("macIP");
        skSeqNo = bundle.getInt("skSeqNo");

        list = new ArrayList<OrderRequest>();
        list = connectGetData(); // db를 통해 받은 데이터를 담는다.

        if(list.size() == 0){
            Toast.makeText(getActivity(), "아직 요청받은 주문이 없습니다.", Toast.LENGTH_SHORT).show();
        }

        //recyclerview
        recyclerView = v.findViewById(R.id.orderRequest_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new OrderRequestAdapter(OrderRequestFragment.this, R.layout.lmw_fragment_order_request, orderRequests, macIP, skSeqNo);
        recyclerView.setAdapter(mAdapter);

        // 기본 구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter.setOnItemClickListener(new OrderRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private ArrayList<OrderRequest> connectGetData(){
        ArrayList<OrderRequest> beanList = new ArrayList<OrderRequest>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_orderliset_for_store.jsp?skSeqNo=" + skSeqNo;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_OrderListNetworkTask networkTask = new LMW_OrderListNetworkTask(getActivity(), urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            orderRequests = (ArrayList<OrderRequest>) obj;
            Log.v(TAG, "orderRequests.size() : " + orderRequests.size());

            beanList = orderRequests;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }
}