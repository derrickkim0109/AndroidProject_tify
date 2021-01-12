package com.android.tify_store.Minwoo.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.tify_store.Minwoo.Activity.MainActivity;
import com.android.tify_store.Minwoo.Adapter.OrderRequestAdapter;
import com.android.tify_store.Minwoo.Adapter.ProgressingAdapter;
import com.android.tify_store.Minwoo.Bean.OrderRequest;
import com.android.tify_store.Minwoo.Bean.Progressing;
import com.android.tify_store.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.android.tify_store.R;

import java.util.ArrayList;

public class ProgressingFragment extends Fragment{

    String TAG = "ProgressingFragment";

    SwipeRefreshLayout mSwipeRefreshLayout = null;

    private ArrayList<OrderRequest> orderRequests = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressingAdapter mAdapter;

    private ArrayList<OrderRequest> list;

    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo = 0;
    String makeDone = null;
    String pickUpDone = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.v(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.lmw_fragment_progressing, container, false);

        // StoreInfoActivity로 부터 값을 받는다.
        Bundle bundle = getArguments();
        macIP = bundle.getString("macIP");
        skSeqNo = bundle.getInt("skSeqNo");
        makeDone = bundle.getString("makeDone");
        pickUpDone = bundle.getString("pickUpDone");

        list = new ArrayList<OrderRequest>();
        list = connectGetData(); // db를 통해 받은 데이터를 담는다.

        Log.v(TAG, "makeDone : " + makeDone);
        Log.v(TAG, "pickUpDone : " + pickUpDone);

        if(list.size() == 0){
            Toast.makeText(getActivity(), "아직 처리중인 주문이 없습니다.", Toast.LENGTH_SHORT).show();
        }

        //recyclerview
        recyclerView = v.findViewById(R.id.progressing_recycler_view);
        recyclerView.setHasFixedSize(true);
        mAdapter = new ProgressingAdapter(ProgressingFragment.this, R.layout.lmw_fragment_progressing, orderRequests, macIP, skSeqNo);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new SimpleItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                return true;
            }

            @Override
            public boolean animateAdd(RecyclerView.ViewHolder holder) {
                return false;
            }

            @Override
            public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
                return false;
            }

            @Override
            public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
                return false;
            }

            @Override
            public void runPendingAnimations() {

            }

            @Override
            public void endAnimation(@NonNull RecyclerView.ViewHolder item) {

            }

            @Override
            public void endAnimations() {

            }

            @Override
            public boolean isRunning() {
                return false;
            }
        });
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mAdapter.setOnItemClickListener(new ProgressingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

        Log.v(TAG, "adapter position : " + mAdapter.returnNum());

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
    }



    private ArrayList<OrderRequest> connectGetData(){
        ArrayList<OrderRequest> beanList = new ArrayList<OrderRequest>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_orderlist_select_progressing.jsp?skSeqNo=" + skSeqNo;

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

    private void notifyDataSetChanged(){

        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }


}
