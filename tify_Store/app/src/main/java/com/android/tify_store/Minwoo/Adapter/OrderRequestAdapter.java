package com.android.tify_store.Minwoo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tify_store.Minwoo.Activity.StoreInfoActivity;
import com.android.tify_store.Minwoo.Bean.OrderRequest;
import com.android.tify_store.Minwoo.Bean.Store;
import com.android.tify_store.Minwoo.Fragment.DialogFragment_OrderRequest_Cancel;
import com.android.tify_store.Minwoo.Fragment.DialogFragment_OrderRequest_Ok;
import com.android.tify_store.Minwoo.Fragment.OrderRequestFragment;
import com.android.tify_store.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.android.tify_store.Minwoo.NetworkTask.LMW_OrderNetworkTask;
import com.android.tify_store.Minwoo.NetworkTask.LMW_StoreNetworkTask;
import com.android.tify_store.R;
import com.bumptech.glide.RequestManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderRequestAdapter extends RecyclerView.Adapter<OrderRequestAdapter.MyViewHolder> {

    String TAG = "OrderRequestAdapter";
    Context context;
    Fragment fragment;

    String macIP;
    int skSeqNo;
    int oNo;
    String where;
    String urlAddr;
    int positionNum;

    private ArrayList<OrderRequest> orderRequests = new ArrayList<>();
    private ArrayList<Integer> list = new ArrayList<Integer>();

    //인터페이스 선언
    public interface  OnItemClickListener{
        void onItemClick(View v, int position);

    }
    private OnItemClickListener mListener = null;

    //메인에서 사용할 클릭메서드 선언
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;

        Log.v(TAG, "setOnItemClickListener");
    }

    private ArrayList<OrderRequest> mDataset;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView mName;
        public TextView addOrder1;
        public TextView addOrder2;
        public TextView request;
        public TextView sName;
        public TextView oDate;
        public TextView oSeqno;
        public TextView subTotalPrice;
        public Button reject;
        public Button accept;

        MyViewHolder(View v) {
            super(v);
            Log.v(TAG, "MyViewHolder");
            oSeqno = v.findViewById(R.id.fragment_OrderRequest_CV_Seqno);
            oDate = v.findViewById(R.id.fragment_OrderRequest_CV_Date);
            sName = v.findViewById(R.id.fragment_OrderRequest_CV_StoreName);
            mName = v.findViewById(R.id.fragment_OrderRequest_CV_MenuName);
            addOrder1 = v.findViewById(R.id.fragment_OrderRequest_CV_AddOrder1);
            addOrder2 = v.findViewById(R.id.fragment_OrderRequest_CV_AddOrder2);
            request = v.findViewById(R.id.fragment_OrderRequest_CV_Request);
            subTotalPrice = v.findViewById(R.id.fragment_OrderRequest_CV_SubTotalPrice);
            reject = v.findViewById(R.id.orderRequest_CV_Btn_Cancel);
            accept = v.findViewById(R.id.orderRequest_CV_Btn_Accept);

            // 뷰홀더에서만 리스트 포지션값을 불러올 수 있음.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v(TAG, "MyViewHolder onClick");

                    int position=getAdapterPosition();//어뎁터 포지션값
                    Log.v(TAG, "position : " + position);
                    // 뷰홀더에서 사라지면 NO_POSITION 을 리턴
                    if(position!=RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(view,position);
                        }
                    }
                }
            });

        }
    }


    public OrderRequestAdapter() {
    }

    // 메인 액티비티에서 받은 myDataset을 가져오
    public OrderRequestAdapter(OrderRequestFragment MenuFragment, int member, ArrayList<OrderRequest> myDataset, String macIP, int skSeqNo) {
        this.mDataset = myDataset;
        this.fragment = MenuFragment;
        this.macIP = macIP;
        this.skSeqNo = skSeqNo;

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_order_select_ostatus.jsp?skSeqNo=" + skSeqNo;
        orderRequests = connectOrderGetData();

        for(int i = 0; i < orderRequests.size(); i++){ // 선언하면 바로 oStatus가 0인 데이터 뽑아낸다.
            list.add(orderRequests.get(i).getOrder_oNo()); // oStatus == 0 (주문요청상태)인 데이터들을 저장
        }
        Log.v(TAG, "list : " + list);

        ArrayList<Integer> integers = new ArrayList<Integer>();

        for(int i = 0; i < mDataset.size(); i++){
            if(!list.contains(mDataset.get(i).getOrder_oNo())){ // list(주문요청상태)인 상태가 아니라면 뷰홀더를 꾸리는 mDataset의 해당 데이터를 삭제
                mDataset.remove(i);
            }else{
                integers.add(mDataset.get(i).getOrder_oNo()); // 아니라면 integers라는 ArrayList에 추가
            }
        }

        Log.v(TAG, "integers : " + integers);
        Log.v(TAG, "mDataset size : " + mDataset.size());
        Log.v(TAG, "MenuAdapter Constructor");

    }

    @NonNull
    @Override
    public OrderRequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(TAG, "OrderRequestAdapter");
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lmw_orderrequest_recycler_item, parent, false);
        //     반복할 xml 파일
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRequestAdapter.MyViewHolder holder, int position) {
        Log.v(TAG, "onBindViewHolder");

        holder.setIsRecyclable(false);

        if(list.contains(mDataset.get(position).getOrder_oNo())){ // oStatus가 0인 oNo가 담겨있는 list라는 ArrayList의 oNo와 같다면 화면에 띄운다.
            holder.oSeqno.setText("주문번호 : " + mDataset.get(position).getOrder_oNo());
            holder.oDate.setText(mDataset.get(position).getoInsertDate());
            holder.sName.setText(mDataset.get(position).getStore_sName());
            holder.mName.setText(mDataset.get(position).getMenu_mName() + " X " + mDataset.get(position).getOlQuantity());

            if (mDataset.get(position).getOlAddShot() != 0) {
                holder.addOrder1.setVisibility(View.VISIBLE);
                holder.addOrder1.setText("+사이즈업 X " + mDataset.get(position).getOlAddShot());
            }
            if (mDataset.get(position).getOlSizeUp() != 0) {
                holder.addOrder2.setVisibility(View.VISIBLE);
                holder.addOrder2.setText("+샷추가 X " + mDataset.get(position).getOlSizeUp());
            }
            if (mDataset.get(position).getOlRequest() != null) {
                holder.request.setVisibility(View.VISIBLE);
                holder.request.setText(mDataset.get(position).getOlRequest());
            }

            // 세자리 콤마
            NumberFormat moneyFormat = null;
            moneyFormat = NumberFormat.getInstance(Locale.KOREA);
            String strTotal = moneyFormat.format(mDataset.get(position).getOlPrice());

            holder.subTotalPrice.setText(strTotal + "원");

            holder.reject.setOnClickListener(new View.OnClickListener() { // 거절사유 작성 폼
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "클릭값 : " + mDataset.get(position).getOrder_oNo());

                    Bundle bundle = new Bundle();
                    Log.v(TAG, "거절");

                    DialogFragment_OrderRequest_Cancel dialogFragment_orderRequest_cancel = new DialogFragment_OrderRequest_Cancel(); // 거절사유를 작성하는 프래그먼트를 띄움
                    bundle.putString("macIP", macIP);
                    bundle.putInt("skSeqNo", skSeqNo);
                    bundle.putInt("oNo", mDataset.get(position).getOrder_oNo());

                    Log.v(TAG, "oNo1 : " + mDataset.get(position).getOrder_oNo());

                    dialogFragment_orderRequest_cancel.setArguments(bundle);
                    dialogFragment_orderRequest_cancel.show(fragment.getActivity().getSupportFragmentManager(),"tag");
                }
            });
            holder.accept.setOnClickListener(new View.OnClickListener() { // 예상소요시간 작성 폼
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Log.v(TAG, "접수");

                    DialogFragment_OrderRequest_Ok dialogFragment_orderRequest_ok = new DialogFragment_OrderRequest_Ok(); // 예상소요시간을 작성하는 프래그먼트를 띄움
                    bundle.putString("macIP", macIP);
                    bundle.putInt("skSeqNo", skSeqNo);
                    bundle.putInt("oNo", mDataset.get(position).getOrder_oNo());

                    Log.v(TAG, "oNo : " + mDataset.get(position).getOrder_oNo());

                    dialogFragment_orderRequest_ok.setArguments(bundle);
                    dialogFragment_orderRequest_ok.show(fragment.getActivity().getSupportFragmentManager(),"tag");
                }
            });

        }
    }
    
    @Override
    public int getItemCount() {
        Log.v(TAG, "getItemCount");
        return mDataset.size();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) { // 데이터 꼬임 방지
        super.onViewRecycled(holder);

        holder.oSeqno.clearComposingText();
        holder.oDate.clearComposingText();
        holder.sName.clearComposingText();
        holder.mName.clearComposingText();
        holder.addOrder1.clearComposingText();
        holder.addOrder2.clearComposingText();
        holder.request.clearComposingText();
        holder.subTotalPrice.clearComposingText();
        holder.reject.clearComposingText();
        holder.accept.clearComposingText();

    }

    private ArrayList<OrderRequest> connectOrderGetData(){
        ArrayList<OrderRequest> beanList = new ArrayList<OrderRequest>();

        try {
            LMW_OrderNetworkTask networkTask = new LMW_OrderNetworkTask(fragment.getActivity(), urlAddr, where);

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
