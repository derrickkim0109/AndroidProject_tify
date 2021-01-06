package com.example.tify.Minwoo.Adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Minwoo.Activity.OrderListActivity;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.R;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {

    String TAG = "OrderListAdapter 확인용";

    private ArrayList<OrderList> mDataset;
    private OrderListAdapter.OnItemClickListener mListener = null;

    //인터페이스 선언
    public interface OnItemClickListener{
        void onItemClick(View v, int position);

    }

    //메인에서 사용할 클릭메서드 선언
    public void setOnItemClickListener(OrderListAdapter.OnItemClickListener listener){
        this.mListener = listener;
        Log.v(TAG, "setOnItemClickListener");
    }

    // 메인 액티비티에서 받은 myDataset을 가져오
    public OrderListAdapter(OrderListActivity orderListActivity, int layout, ArrayList<OrderList> myDataset) {
        mDataset = myDataset;
        Log.v(TAG, "OrderListAdapter Constructor");

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView OSeqno;
        public TextView ODate;
        public TextView sName;
        public Button reviewBtn;
        public ImageButton orderRequestBtn;
        public ImageButton orderConfirmBtn;
        public ImageButton orderCompleteBtn;

        MyViewHolder(View v) {
            super(v);
            Log.v(TAG, "MyViewHolder");
            OSeqno = v.findViewById(R.id.activity_OrderList_CV_OSeqno);
            ODate = v.findViewById(R.id.activity_OrderList_CV_ODate);
            sName = v.findViewById(R.id.activity_OrderList_CV_sName);
            reviewBtn = v.findViewById(R.id.activity_OrderList_CV_Btn_InsertReview);
            orderRequestBtn = v.findViewById(R.id.activity_OrderList_CV_OrderRequest);
            orderConfirmBtn = v.findViewById(R.id.activity_OrderList_CV_OrderConfirm);
            orderCompleteBtn = v.findViewById(R.id.activity_OrderList_CV_OrderComplete);

            // 뷰홀더에서만 리스트 포지션값을 불러올 수 있음.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v(TAG, "MyViewHolder onClick");

                    int position=getAdapterPosition();//어뎁터 포지션값
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

    @NonNull
    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(TAG, "onCreateViewHolder");
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lmw_activity_orderlist_recycler_item, parent, false);
        //     반복할 xml 파일
        OrderListAdapter.MyViewHolder vh = new OrderListAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.MyViewHolder holder, int position) {
        Log.v(TAG, "onBindViewHolder");

        holder.OSeqno.setText("주문번호 : " + mDataset.get(position).getoSeqno());
        holder.ODate.setText(mDataset.get(position).getoDate());
//        holder.sName.setText("가게이름");

        switch (mDataset.get(position).getoStatus()){
            case "주문요청":
                holder.orderRequestBtn.setBackgroundColor(Color.BLUE);
                break;
            case "주문접수":
                holder.orderConfirmBtn.setBackgroundColor(Color.BLUE);
                break;
            case "제조완료":
                holder.orderCompleteBtn.setBackgroundColor(Color.BLUE);
                holder.reviewBtn.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        Log.v(TAG, "getItemCount");
        return mDataset.size();
    }
}
