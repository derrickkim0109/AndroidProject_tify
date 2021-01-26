package com.android.tify_store.Minwoo.Adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.android.tify_store.Minwoo.Fragment.ProgressingFragment;
import com.android.tify_store.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.MyViewHolder> {

    String TAG = "CompleteAdapter";
    Context context;
    Fragment fragment;

    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo = 0;

    //인터페이스 선언
    public interface OnItemClickListener{
        void onItemClick(View v, int position);

    }
    private CompleteAdapter.OnItemClickListener mListener = null;

    //메인에서 사용할 클릭메서드 선언
    public void setOnItemClickListener(CompleteAdapter.OnItemClickListener listener){
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
        public TextView oStatus;

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
            oStatus = v.findViewById(R.id.fragment_Complete_TV_oStatus);
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


    // 메인 액티비티에서 받은 myDataset을 가져오
    public CompleteAdapter(CompleteFragment MenuFragment, int member, ArrayList<OrderRequest> myDataset, String macIP, int skSeqNo) {
        this.mDataset = myDataset;
        this.fragment = MenuFragment;
        this.macIP = macIP;
        this.skSeqNo = skSeqNo;
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

        holder.oSeqno.setText("주문번호 : " + mDataset.get(position).getOrder_oNo());
        holder.oDate.setText(mDataset.get(position).getoInsertDate());
        holder.sName.setText(mDataset.get(position).getStore_sName());
        holder.mName.setText(mDataset.get(position).getMenu_mName());

        if(mDataset.get(position).getOlAddShot() != 0){ // 샷추가 여부
            holder.addOrder1.setVisibility(View.VISIBLE);
            holder.addOrder1.setText("+샷추가 X " + mDataset.get(position).getOlAddShot());
        }
        if(mDataset.get(position).getOlSizeUp() != 0){ // 사이즈업 여부
            holder.addOrder2.setVisibility(View.VISIBLE);
            holder.addOrder2.setText("+사이즈업 X " + mDataset.get(position).getOlSizeUp());
        }
        if(mDataset.get(position).getOlRequest() != null){ // 요청사항 여부
            holder.request.setVisibility(View.VISIBLE);
            holder.request.setText(mDataset.get(position).getOlRequest());
        }

        if(mDataset.get(position).getoStatus() == 3){ // 픽업 완료된 주문
            holder.oStatus.setVisibility(View.VISIBLE);
            holder.oStatus.setText("픽업완료된 주문");
            holder.oStatus.setTextColor(Color.parseColor("#009688"));
        }

        if(mDataset.get(position).getoStatus() == 4 || (mDataset.get(position).getoStatus() == 5)){ // 주문 취소된 경우 (매장 요청 or 고객 요청)
            holder.oStatus.setVisibility(View.VISIBLE);
            holder.oStatus.setTextColor(Color.parseColor("#870146"));
            if(mDataset.get(position).getoStatus() == 4){
                holder.oStatus.setText("주문 취소 : 고객 요청");
            }
            if(mDataset.get(position).getoStatus() == 5){
                holder.oStatus.setText("주문 취소 : 매장 요청");
            }
        }

        // 세자리 콤마
        NumberFormat moneyFormat = null;
        moneyFormat = NumberFormat.getInstance(Locale.KOREA);
        String strTotal = moneyFormat.format(mDataset.get(position).getOlPrice());

        holder.subTotalPrice.setText(strTotal + "원");
    }

    @Override
    public int getItemCount() {
        Log.v(TAG, "getItemCount");
        return mDataset.size();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) { // 데이터 꼬임 방지
        super.onViewRecycled(holder);

            holder.oSeqno.setText("");
            holder.oDate.setText("");
            holder.sName.setText("");
            holder.mName.setText("");
            holder.addOrder1.setText("");
            holder.addOrder2.setText("");
            holder.request.setText("");
            holder.subTotalPrice.setText("");
            holder.oStatus.setText("");
        }

}
