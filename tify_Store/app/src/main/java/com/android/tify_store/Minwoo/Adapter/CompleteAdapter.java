package com.android.tify_store.Minwoo.Adapter;

import android.content.Context;
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

import com.android.tify_store.Minwoo.Bean.Complete;
import com.android.tify_store.Minwoo.Bean.OrderRequest;
import com.android.tify_store.Minwoo.Fragment.CompleteFragment;
import com.android.tify_store.Minwoo.Fragment.OrderRequestFragment;
import com.android.tify_store.R;

import java.util.ArrayList;

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.MyViewHolder> {

    String TAG = "CompleteAdapter";
    Context context;
    Fragment fragment;

    //인터페이스 선언
    public interface OnItemClickListener{
        void onItemClick(View v, int position);

    }
    private OrderRequestAdapter.OnItemClickListener mListener = null;

    //메인에서 사용할 클릭메서드 선언
    public void setOnItemClickListener(OrderRequestAdapter.OnItemClickListener listener){
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

        MyViewHolder(View v) {
            super(v);
            Log.v(TAG, "MyViewHolder");
            oSeqno = v.findViewById(R.id.fragment_Complete_CV_Seqno);
            oDate = v.findViewById(R.id.fragment_Complete_CV_Date);
            sName = v.findViewById(R.id.fragment_Complete_CV_StoreName);
            mName = v.findViewById(R.id.fragment_Complete_CV_MenuName);
            addOrder1 = v.findViewById(R.id.fragment_Complete_CV_AddOrder1);
            addOrder2 = v.findViewById(R.id.fragment_Complete_CV_AddOrder2);
            request = v.findViewById(R.id.fragment_Complete_CV_Request);
            subTotalPrice = v.findViewById(R.id.fragment_Complete_CV_SubTotalPrice);

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


    public CompleteAdapter(ArrayList<OrderRequest> mDataset, CompleteFragment fragment) {
        this.mDataset = mDataset;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public CompleteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(TAG, "OrderRequestAdapter");
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lmw_complete_recycler_item, parent, false);
        //     반복할 xml 파일
        CompleteAdapter.MyViewHolder vh = new CompleteAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CompleteAdapter.MyViewHolder holder, int position) {
        Log.v(TAG, "onBindViewHolder");

        holder.oSeqno.setText("주문번호 : " + mDataset.get(position).getSeqno());
        holder.oDate.setText(mDataset.get(position).getDate());
//       holder.sName.setText("가게이름");
        holder.mName.setText(mDataset.get(position).getMenuName());

        if(mDataset.get(position).getAddOrder1() != null){
            holder.addOrder1.setVisibility(View.VISIBLE);
            holder.addOrder1.setText(mDataset.get(position).getAddOrder1());
        }
        if(mDataset.get(position).getAddOrder2() != null){
            holder.addOrder2.setVisibility(View.VISIBLE);
            holder.addOrder2.setText(mDataset.get(position).getAddOrder2());
        }
        if(mDataset.get(position).getOrderRequest() != null){
            holder.request.setVisibility(View.VISIBLE);
            holder.request.setText(mDataset.get(position).getOrderRequest());
        }

        holder.subTotalPrice.setText(mDataset.get(position).getPrice() + "원");
    }

    @Override
    public int getItemCount() {
        Log.v(TAG, "getItemCount");
        return mDataset.size();
    }
}
