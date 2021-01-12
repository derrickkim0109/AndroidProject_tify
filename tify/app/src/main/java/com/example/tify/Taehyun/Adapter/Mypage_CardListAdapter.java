//package com.example.tify.Taehyun.Adapter;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.recyclerview.widget.RecyclerView;
//
//
//import com.example.tify.R;
//import com.example.tify.Taehyun.Activity.Mypage_CardRegistrationActivity;
//import com.example.tify.Taehyun.Bean.Bean_Mypage_CardInfo;
//import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;
//
//import java.util.ArrayList;
//
//public class Mypage_CardListAdapter extends RecyclerView.Adapter<Mypage_CardInfoAdapter.MyViewHolder>{
//    ///field
//    private ArrayList<Bean_Mypage_CardInfo> cardInfo ;
//    private Context mContext = null;
//    private String macIP = null;
//
//
//    public Mypage_CardListAdapter (Context mContext, int layout, ArrayList<Bean_Mypage_CardInfo> cardInfo, String macIP) {
//        this.cardInfo = cardInfo;
//        this.macIP = macIP;
//        this.mContext = mContext;
//    }
//
//
//    //
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.kth_activity_cardinfo_list, parent, false);
//        //     반복할 xml 파일
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Mypage_CardInfoAdapter.MyViewHolder holder, int position) {
//        Log.v("TT","ddddddddddddd");
//        //connectGetData(user_uNo);
//        String card_Image = cardInfo.get(position).getcImage();
//        String card_cNo = cardInfo.get(position).getcCardNo();
//        int cNo = cardInfo.get(position).getcNo();
//
//
//
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return cardInfo.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        final static String TAG = "MemberAdapter";
//
//        private ImageButton mypage_card_icon;
//        private TextView card_text_view;
//        private ImageView card_delete;
//        private TextView card_cInfo;
//
//        MyViewHolder(View v) {
//
//            super(v);
//
//            mypage_card_icon = v.findViewById(R.id.mypage_card_icon);
//            card_cInfo = v.findViewById(R.id.card_cInfo);
//            card_text_view = v.findViewById(R.id.card_text_view);
//            card_delete = v.findViewById(R.id.card_delete);
//
//        }
//    }
//}
