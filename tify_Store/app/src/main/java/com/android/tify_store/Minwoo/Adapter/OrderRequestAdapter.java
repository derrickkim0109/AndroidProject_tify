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

import com.android.tify_store.Minwoo.Bean.OrderRequest;
import com.android.tify_store.Minwoo.Fragment.DialogFragment_OrderRequest_Cancel;
import com.android.tify_store.Minwoo.Fragment.DialogFragment_OrderRequest_Ok;
import com.android.tify_store.Minwoo.Fragment.OrderRequestFragment;
import com.android.tify_store.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderRequestAdapter extends RecyclerView.Adapter<OrderRequestAdapter.MyViewHolder> {

    String TAG = "OrderRequestAdapter";
    Context context;
    Fragment fragment;

    //인터페이스 선언
    public interface OnItemClickListener{
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
    public OrderRequestAdapter(OrderRequestFragment MenuFragment, int member, ArrayList<OrderRequest> myDataset) {
        this.mDataset = myDataset;
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

        holder.oSeqno.setText("주문번호 : " + mDataset.get(position).getOrder_oNo());
        holder.oDate.setText("2021-01-10 13:13"); // 고객이 요청할 때 보내주는 입력 시간으로 받기
        holder.sName.setText(mDataset.get(position).getStore_sName());
        holder.mName.setText(mDataset.get(position).getMenu_mName());

        if(mDataset.get(position).getOlAddShot() != 0){
            holder.addOrder1.setVisibility(View.VISIBLE);
            holder.addOrder1.setText("+사이즈업 X " + mDataset.get(position).getOlSizeUp());
        }
        if(mDataset.get(position).getOlSizeUp() != 0){
            holder.addOrder2.setVisibility(View.VISIBLE);
            holder.addOrder2.setText("+샷추가 X " + mDataset.get(position).getOlAddShot());
        }
        if(mDataset.get(position).getOlRequest() != null){
            holder.request.setVisibility(View.VISIBLE);
            holder.request.setText(mDataset.get(position).getOlRequest());
        }

        NumberFormat moneyFormat = null;
        moneyFormat = NumberFormat.getInstance(Locale.KOREA);
        String strTotal = moneyFormat.format(mDataset.get(position).getOlPrice());

        holder.subTotalPrice.setText(strTotal + "원");

        holder.reject.setOnClickListener(btnClickListener);
        holder.accept.setOnClickListener(btnClickListener);

    }

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.orderRequest_CV_Btn_Cancel:
                    Log.v(TAG, "거절");

                    DialogFragment_OrderRequest_Cancel dialogFragment_orderRequestCancel = new DialogFragment_OrderRequest_Cancel();
                    dialogFragment_orderRequestCancel.show(fragment.getFragmentManager(),"tag");
                    break;
                case R.id.orderRequest_CV_Btn_Accept:
                    Log.v(TAG, "접수");

                    DialogFragment_OrderRequest_Ok dialogFragment_orderRequest_ok = new DialogFragment_OrderRequest_Ok();
                    dialogFragment_orderRequest_ok.show(fragment.getFragmentManager(), "tag");
                    break;
            }

        }
    };

    @Override
    public int getItemCount() {
        Log.v(TAG, "getItemCount");
        return mDataset.size();
    }



}
