package com.example.tify.Taehyun.Adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tify.R;
import com.example.tify.Taehyun.Bean.Bean_Mypage_CardInfo;
import com.example.tify.Taehyun.Bean.Bean_Mypage_cardlist;

import java.util.ArrayList;

public class Mypage_CardListAdapter extends RecyclerView.Adapter<Mypage_CardListAdapter.MyViewHolder>{

    ///field
    private ArrayList<Bean_Mypage_cardlist> cardlists ;
    private Context mContext = null;
    private String macIP = null;


    public Mypage_CardListAdapter (Context mContext, int layout, ArrayList<Bean_Mypage_cardlist> cardlists, String macIP) {
        this.cardlists = cardlists;
        this.macIP = macIP;
        this.mContext = mContext;
    }


    //
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kth_activity_cardinfo_list, parent, false);

        //     반복할 xml 파일
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //connectGetData(user_uNo);
        String getcCardCompany = cardlists.get(position).getcCardCompany();
        String card_cMM = cardlists.get(position).getcMM();
        String card_cYear = cardlists.get(position).getcYear();
        String card_uName = cardlists.get(position).getuName();
        String card_cNo = cardlists.get(position).getcCardNo();
        int cNo = cardlists.get(position).getcNo();

        
    }





    @Override
    public int getItemCount() {
        return cardlists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final static String TAG = "MemberAdapter";

        private ImageButton mypage_card_icon;
        private TextView card_text_view;
        private ImageView card_delete;
        private TextView card_cInfo;

        MyViewHolder(View v) {

            super(v);

            mypage_card_icon = v.findViewById(R.id.mypage_card_icon);
            card_cInfo = v.findViewById(R.id.card_cInfo);
            card_text_view = v.findViewById(R.id.card_text_view);
            card_delete = v.findViewById(R.id.card_delete);

        }
    }
}
