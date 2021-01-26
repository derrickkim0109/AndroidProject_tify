package com.example.tify.Hyeona.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tify.Hyeona.Adapter.review_adapter;
import com.example.tify.Hyeona.Bean.Bean_review_review;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_review;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.util.ArrayList;

public class reviewActivity extends Fragment {

    // private ListView listView;
    private Button button;
    private TextView textView;
    private ArrayList<Bean_review_review> reviews = null;
    private review_adapter adapter;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;
    int storekeeper_skSeqNo = 1;
    // 가게 번호 넘겨야댐
    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

    String urlAddr = "http://" + MacIP + ":8080/tify/review.jsp?";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.cha_reviewlist, container, false);

        recyclerView = v.findViewById(R.id.reviewList);
        //recyclerView.setHasFixedSize(true);
        //레이아웃 매니저 만들기
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //reviews = new ArrayList<Bean_review>();

        Bundle bundle = getArguments();
        storekeeper_skSeqNo = bundle.getInt("store_sSeqNo",0);
        Log.v("가게번호!!", "store_sSeqNo : " + storekeeper_skSeqNo);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        connectGetData();
    }

    private void connectGetData(){
        try {
            String urlAddress = urlAddr + "storekeeper_skSeqNo=" + storekeeper_skSeqNo;

            CUDNetworkTask_review mCUDNetworkTask_review = new CUDNetworkTask_review(getContext(), urlAddress,"select");
            Object obj = mCUDNetworkTask_review.execute().get();
            reviews = (ArrayList<Bean_review_review>) obj;
            Log.v("dddd","dd"+reviews);
            //리사이클러뷰 어댑터를 넣기
            adapter = new review_adapter(getContext(), R.layout.cha_reviewcontent, reviews);
            //어댑터에게 보내기
            recyclerView.setAdapter(adapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}