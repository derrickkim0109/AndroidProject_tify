package com.android.tify_store.Minwoo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tify_store.Minwoo.Activity.MainActivity;
import com.android.tify_store.Minwoo.Bean.OrderRequest;
import com.android.tify_store.Minwoo.Fragment.DialogFragment_OrderRequest_Cancel;
import com.android.tify_store.Minwoo.Fragment.DialogFragment_Progressing_Accept;
import com.android.tify_store.Minwoo.Fragment.OrderRequestFragment;
import com.android.tify_store.Minwoo.Fragment.ProgressingFragment;
import com.android.tify_store.Minwoo.NetworkTask.LMW_LoginNetworkTask;
import com.android.tify_store.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.android.tify_store.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProgressingAdapter extends RecyclerView.Adapter<ProgressingAdapter.MyViewHolder> {

    String TAG = "ProgressingAdapter";
    Context context;
    Fragment fragment;

    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo = 0;

    int positionNum = -1;

    int switchNum = -1;

    OnItemClickListener onItemClickListener = null;

    //인터페이스 선언
    public interface OnItemClickListener{
        void onItemClick(View v, int position);

    }
    private ProgressingAdapter.OnItemClickListener mListener = null;

    //메인에서 사용할 클릭메서드 선언
    public void setOnItemClickListener(ProgressingAdapter.OnItemClickListener listener){
        this.mListener = listener;
        Log.v(TAG, "setOnItemClickListener");

    }

    public int returnNum(){

        return positionNum;
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
        public Button makeDone;
        public Button pickUpDone;

        MyViewHolder(View v) {
            super(v);
            Log.v(TAG, "MyViewHolder");
            oSeqno = v.findViewById(R.id.fragment_Progressing_CV_Seqno);
            oDate = v.findViewById(R.id.fragment_Progressing_CV_Date);
            sName = v.findViewById(R.id.fragment_Progressing_CV_StoreName);
            mName = v.findViewById(R.id.fragment_Progressing_CV_MenuName);
            addOrder1 = v.findViewById(R.id.fragment_Progressing_CV_AddOrder1);
            addOrder2 = v.findViewById(R.id.fragment_Progressing_CV_AddOrder2);
            request = v.findViewById(R.id.fragment_Progressing_CV_Request);
            subTotalPrice = v.findViewById(R.id.fragment_Progressing_CV_SubTotalPrice);
            makeDone = v.findViewById(R.id.Progressing_CV_Btn_MakeDone);
            pickUpDone = v.findViewById(R.id.Progressing_CV_Btn_PickUpDone);

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
    public ProgressingAdapter(ProgressingFragment MenuFragment, int member, ArrayList<OrderRequest> myDataset, String macIP, int skSeqNo) {
        this.mDataset = myDataset;
        this.fragment = MenuFragment;
        this.macIP = macIP;
        this.skSeqNo = skSeqNo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(TAG, "OrderRequestAdapter");
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lmw_progressing_recycler_item, parent, false);
        //     반복할 xml 파일
        ProgressingAdapter.MyViewHolder vh = new ProgressingAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.v(TAG, "onBindViewHolder");
        Log.v(TAG, "switchNum : " + switchNum);

        if(switchNum == 1){ // 이미 픽업이 완료된 상태의 주문이라면 해당 버튼들 사용 불가
            holder.makeDone.setEnabled(false);
            holder.pickUpDone.setEnabled(false);
        }

        holder.oSeqno.setText("주문번호 : " + mDataset.get(position).getOrder_oNo());
        holder.oDate.setText(mDataset.get(position).getoInsertDate());
        holder.sName.setText(mDataset.get(position).getStore_sName());
        holder.mName.setText(mDataset.get(position).getMenu_mName());

        if(mDataset.get(position).getOlSizeUp() != 0){ // 사이즈업 여부
            holder.addOrder1.setVisibility(View.VISIBLE);
            holder.addOrder1.setText("+사이즈업 X " + mDataset.get(position).getOlSizeUp());
        }
        if(mDataset.get(position).getOlAddShot() != 0){ // 샷추가 여부
            holder.addOrder2.setVisibility(View.VISIBLE);
            holder.addOrder2.setText("+샷추가 X " + mDataset.get(position).getOlAddShot());
        }
        if(mDataset.get(position).getOlRequest() != null){ // 요청사항 여부
            holder.request.setVisibility(View.VISIBLE);
            holder.request.setText(mDataset.get(position).getOlRequest());
        }

        // 세자리 콤마
        NumberFormat moneyFormat = null;
        moneyFormat = NumberFormat.getInstance(Locale.KOREA);
        String strTotal = moneyFormat.format(mDataset.get(position).getOlPrice());

        holder.subTotalPrice.setText(strTotal + "원");
        holder.oSeqno.setTag(position);

        Log.v(TAG, "mDataset.get(position).getoStatus() : " + mDataset.get(position).getoStatus());

        if(mDataset.get(position).getoStatus() == 1){ // oStatus == 1 (요청 접수)인 경우
            holder.makeDone.setEnabled(true);
            holder.makeDone.setBackgroundColor(Color.WHITE);
            holder.pickUpDone.setEnabled(false);
            holder.pickUpDone.setBackgroundColor(Color.parseColor("#1D000000"));
        }else{ // oStatus == 2 (제조완료)인 경우
            holder.makeDone.setEnabled(false);
            holder.makeDone.setBackgroundColor(Color.parseColor("#1D000000"));
            holder.pickUpDone.setBackgroundColor(Color.WHITE);
            holder.pickUpDone.setEnabled(true);
            holder.pickUpDone.setTextColor(Color.BLACK);
        }

        holder.makeDone.setOnClickListener(new View.OnClickListener() { // 제조완료 버튼을 눌렀을 경우 (픽업완료 버튼 활성화)
            @Override
            public void onClick(View v) {

                Log.v(TAG, "mDataset.get(position).getOrder_oNo() : " + mDataset.get(position).getOrder_oNo());

                holder.makeDone.setEnabled(false);
                holder.makeDone.setBackgroundColor(Color.parseColor("#1D000000"));
                holder.pickUpDone.setBackgroundColor(Color.WHITE);
                holder.pickUpDone.setEnabled(true);

                urlAddr = "http://" + macIP + ":8080/tify/lmw_order_update_ostatus1.jsp?oNo=" + mDataset.get(position).getOrder_oNo() + "&oStatus=" + 2; // oStatus 2로 Update
                where = "update";

                String result = connectUpdateData();

                if(result.equals("1")){

                }else { // DB Action 실패 시 예외처리
                    Toast.makeText(fragment.getActivity(), "오류 발생! \n관리자에게 연락바랍니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.pickUpDone.setOnClickListener(new View.OnClickListener() { // 픽업완료 버튼 눌렀을 경우
            @Override
            public void onClick(View v) {
                holder.pickUpDone.setEnabled(false);
                holder.pickUpDone.setBackgroundColor(Color.parseColor("#1D000000"));
                holder.makeDone.setVisibility(View.GONE);
                holder.pickUpDone.setText("완료탭에서 확인해주세요");

                urlAddr = "http://" + macIP + ":8080/tify/lmw_order_update_ostatus1.jsp?oNo=" + mDataset.get(position).getOrder_oNo() + "&oStatus=" + 3; // oStatus 3으로 Update
                where = "update";

                String result = connectUpdateData();

                if(result.equals("1")){
                    Toast.makeText(fragment.getActivity(), "고객에게 알림이 전송되었습니다.", Toast.LENGTH_SHORT).show();
                    switchNum = 1;
                }else { // DB Action 실패 시 예외처리
                    Toast.makeText(fragment.getActivity(), "오류 발생! \n관리자에게 연락바랍니다.", Toast.LENGTH_SHORT).show();
                }
            }

        });

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

    }

    private String connectUpdateData(){ // oStatus 2,3으로 바꾸기
        String result = null;

        try {
            LMW_OrderListNetworkTask networkTask = new LMW_OrderListNetworkTask(fragment.getActivity(), urlAddr, where);

            Object obj = networkTask.execute().get();
            result = (String) obj;

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


}
