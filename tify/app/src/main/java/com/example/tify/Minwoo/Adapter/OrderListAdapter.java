package com.example.tify.Minwoo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tify.Minwoo.Activity.OrderListActivity;
import com.example.tify.Minwoo.Activity.StoreInfoActivity;
import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.R;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {

    String TAG = "OrderListAdapter 확인용";
    Context context = null;

    private ArrayList<Order> mDataset;
    private OrderListAdapter.OnItemClickListener mListener = null;

    int uNo = 0;
    int skSeqNo = 0;

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
    public OrderListAdapter(OrderListActivity orderListActivity, int layout, ArrayList<Order> myDataset, int uNo, int skSeqNo) {
        mDataset = myDataset;
        this.context = orderListActivity;
        this.uNo = uNo;
        this.skSeqNo = skSeqNo;
        Log.v(TAG, "OrderListAdapter Constructor");

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        // layout
        LinearLayout linearLayout;
        TextView tv_orderNo;
        TextView tv_orderDate;
        ImageView tv_right1;
        ImageView tv_right2;
        ImageView tv_right3;
        TextView tv_orderRequest;
        TextView tv_orderGet;
        TextView tv_complete;
        TextView tv_sName;
        Button btn_Review;

        MyViewHolder(View v) {
            super(v);
            Log.v(TAG, "MyViewHolder");
            linearLayout = v.findViewById(R.id.orderList_RV_LL);
            tv_orderNo = v.findViewById(R.id.activity_OrderList_CV_OSeqno);
            tv_orderDate = v.findViewById(R.id.activity_OrderList_CV_ODate);
            tv_sName = v.findViewById(R.id.activity_OrderList_CV_sName);
            tv_right1 = v.findViewById(R.id.activity_OrderList_CV_right1);
            tv_right2 = v.findViewById(R.id.activity_OrderList_CV_right2);
            tv_right3 = v.findViewById(R.id.activity_OrderList_CV_right3);
            tv_orderRequest = v.findViewById(R.id.activity_OrderList_CV_TV_OrderRequest);
            tv_orderGet = v.findViewById(R.id.activity_OrderList_CV_TV_OrderConfirm);
            tv_complete = v.findViewById(R.id.activity_OrderList_CV_TV_OrderComplete);
            btn_Review = v.findViewById(R.id.activity_OrderList_CV_Btn_InsertReview);

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

        holder.tv_orderNo.setText("주문번호 : " + mDataset.get(position).getoNo());
        holder.tv_orderDate.setText(mDataset.get(position).getoInsertDate());
        holder.tv_sName.setText(mDataset.get(position).getStore_sName());

        switch (mDataset.get(position).getoStatus()){ // 0 주문요청 1 주문접수 2 제조완료 3 픽업완료 4 고객이 취소 5 매장이 취소
            case 0: // 주문요청이 들어간 경우
                Glide.with(holder.tv_right1).load(R.drawable.dotdot).into(holder.tv_right1);
                holder.tv_orderRequest.setTextColor(Color.parseColor("#0084ff"));
                holder.btn_Review.setVisibility(View.INVISIBLE);
                break;
            case 1: // 주문접수가 완료된 경우
                Glide.with(holder.tv_right2).load(R.drawable.dotdot).into(holder.tv_right2);
                holder.tv_orderGet.setTextColor(Color.parseColor("#0084ff"));
                holder.btn_Review.setVisibility(View.INVISIBLE);
                break;
            case 2: // 제조완료된 경우
                Glide.with(holder.tv_right3).load(R.drawable.dotdot).into(holder.tv_right3);
                holder.tv_complete.setTextColor(Color.parseColor("#0084ff"));
                holder.btn_Review.setVisibility(View.INVISIBLE);
                break;
            case 3: // 픽업완료한 경우 (리뷰쓰기 버튼 활성화)
                holder.tv_right1.setImageResource(R.drawable.ic_action_progress_checked);
                holder.tv_right2.setImageResource(R.drawable.ic_action_progress_checked);
                holder.tv_right3.setImageResource(R.drawable.ic_action_progress_checked);
                holder.tv_orderRequest.setTextColor(Color.parseColor("#0084ff"));
                holder.tv_orderGet.setTextColor(Color.parseColor("#0084ff"));
                holder.tv_complete.setTextColor(Color.parseColor("#0084ff"));
                holder.btn_Review.setVisibility(View.VISIBLE);
                break;
        }

        if(mDataset.get(position).getoReview() == 1 && mDataset.get(position).getoStatus() == 3){ // 리뷰를 작성했고 픽업도 완료한 주문의 경우
            holder.linearLayout.setBackgroundColor(Color.WHITE);
            holder.btn_Review.setVisibility(View.GONE);
            holder.tv_right1.setVisibility(View.GONE);
            holder.tv_right2.setVisibility(View.GONE);
            holder.tv_right3.setVisibility(View.GONE);
            holder.tv_orderRequest.setVisibility(View.GONE);
            holder.tv_complete.setVisibility(View.GONE);
            holder.tv_orderGet.setText("픽업 완료된 주문입니다.");
        }

        if(mDataset.get(position).getoStatus() == 4 || mDataset.get(position).getoStatus() == 5){ // 주문이 취소된 경우
            holder.linearLayout.setBackgroundColor(Color.parseColor("#16000000"));
            holder.btn_Review.setVisibility(View.GONE);
            holder.tv_right1.setVisibility(View.GONE);
            holder.tv_right2.setVisibility(View.GONE);
            holder.tv_right3.setVisibility(View.GONE);
            holder.tv_orderRequest.setVisibility(View.GONE);
            holder.tv_complete.setVisibility(View.GONE);
            if(mDataset.get(position).getoStatus() == 4){
                holder.tv_orderGet.setText("취소 사유 : 고객 요청 \n취소 날짜 : " + mDataset.get(position).getoDeleteDate());
            }
            if(mDataset.get(position).getoStatus() == 5){
                holder.tv_orderGet.setText("취소 사유 : 매장 요청\n취소 날짜 : " + mDataset.get(position).getoDeleteDate());
            }
        }

        holder.btn_Review.setOnClickListener(new View.OnClickListener() { // 리뷰쓰기 버튼 클릭
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), com.example.tify.Hyeona.Activity.review_white.class);
                intent.putExtra("uNo", uNo);
                intent.putExtra("sSeqNo", skSeqNo);
                intent.putExtra("oNo", mDataset.get(position).getoNo());
                Log.v(TAG, "skSeqNo : " + skSeqNo);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(TAG, "getItemCount");
        return mDataset.size();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);

        holder.tv_orderNo.clearComposingText();
        holder.tv_orderDate.clearComposingText();
        holder.tv_orderRequest.clearComposingText();
        holder.tv_orderGet.clearComposingText();
        holder.tv_complete.clearComposingText();
        holder.tv_sName.clearComposingText();
        holder.tv_right1.clearColorFilter();
        holder.tv_right2.clearColorFilter();
        holder.tv_right3.clearColorFilter();

   }
}
