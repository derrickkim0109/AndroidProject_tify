package com.android.tify_store.Minwoo.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tify_store.Minwoo.Adapter.OrderRequestAdapter;
import com.android.tify_store.Minwoo.Adapter.ProgressingAdapter;
import com.android.tify_store.Minwoo.Bean.OrderRequest;
import com.android.tify_store.Minwoo.Bean.Progressing;
import com.android.tify_store.R;

import java.util.ArrayList;

public class ProgressingFragment extends Fragment {

    private ArrayList<OrderRequest> progressings = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressingAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.lmw_fragment_progressing, container, false);

        //recyclerview
        recyclerView = v.findViewById(R.id.progressing_recycler_view);
        recyclerView.setHasFixedSize(true);
        mAdapter = new ProgressingAdapter(progressings, ProgressingFragment.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData(); // 데이터 넣어주자
    }

    private void prepareData() { // 받은 데이터가 null일 경우 접수된 주문이 없다고 멘트 띄워주기
        progressings.add(new OrderRequest(1,"2021-01-05 13:11","테스트","아메리카노(COLD)",2,null,null,null,4000));
        progressings.add(new OrderRequest(2,"2021-01-05 14:42","테스트","아메리카노(COLD)",1,null,null,"얼음 조금만",4000));
        progressings.add(new OrderRequest(3,"2021-01-05 15:34","테스트","아메리카노(COLD)",1,"+사이즈업","샷추가","얼음 많이",4000));

    }
}