package com.example.tify.Taehyun.Adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.R;
import com.example.tify.Taehyun.Activity.Mypage_CardRegistrationActivity;
import com.example.tify.Taehyun.Bean.Bean_Mypage_CardInfo;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;

import java.util.ArrayList;


public class Mypage_CardInfoAdapter extends RecyclerView.Adapter<Mypage_CardInfoAdapter.MyViewHolder>  {

        ///field
        private ArrayList<Bean_Mypage_CardInfo> cardInfo ;
        private Context mContext = null;
        //IP
        /*ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();*/
    String MacIP = "";
    int uNo = 0;

        public Mypage_CardInfoAdapter (Context mContext, int layout, ArrayList<Bean_Mypage_CardInfo> cardInfo, String MacIP, int uNo) {
            this.cardInfo = cardInfo;
            this.uNo = uNo;
            this.MacIP = MacIP;
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
            Log.v("TT","ddddddddddddd");
            //connectGetData(user_uNo);
            String card_Image = cardInfo.get(position).getcImage();
            String card_cNo = cardInfo.get(position).getcCardNo();

            String cInfo = cardInfo.get(position).getcInfo();

            int cNo = cardInfo.get(position).getcNo();


            holder.card_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("카드를 삭제하시겠습니까? ");
                    builder.setNegativeButton("아니오", null);
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() { // 예를 눌렀을 경우 로그인 창으로 이동
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            connectDelete(cNo);
                            Intent intent = new Intent(mContext, Mypage_CardRegistrationActivity.class)
                                    .putExtra("MacIP",MacIP)
                                    .putExtra("uNo",uNo);

                            mContext.startActivity(intent);

                        }
                    });
                    builder.create().show();
                }
            });



            if (card_Image.equals("MASTERCARD")) {
                holder.mypage_card_icon.setImageResource(R.drawable.mastercard);
            }else if (card_Image.equals("VISA")) {
                holder.mypage_card_icon.setImageResource(R.drawable.visa);
            }else if (card_Image.equals("AMEX")) {
                holder.mypage_card_icon.setImageResource(R.drawable.amex);

            }else if (card_Image.equals("DINERS_CLUB And CARTE_BLANCHE")) {
                holder.mypage_card_icon.setImageResource(R.drawable.dinersclub);

            }else if (card_Image.equals("DISCOVER")) {
                holder.mypage_card_icon.setImageResource(R.drawable.discover);

            }else if (card_Image.equals("JCB")) {
                holder.mypage_card_icon.setImageResource(R.drawable.jcb);
            }

            holder.card_text_view.setText(card_cNo);
            holder.card_cInfo.setText(cInfo);
            holder.card_delete.setImageResource(R.drawable.ic_action_x);

        }


        @Override
        public int getItemCount() {
            return cardInfo.size();
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


        public void connectDelete(int cNo){

            String urlAddr = "http://" + MacIP + ":8080/tify/mypage_card_info_delete.jsp?";
            String urlAddress = urlAddr + "cNo=" + cNo;

            try {

                NetworkTask_TaeHyun networkTask_taeHyun = new NetworkTask_TaeHyun(mContext,urlAddress,"delete");
                Object obj = networkTask_taeHyun.execute().get();

            }catch (Exception e){
                e.printStackTrace();

            }

        }


    }///END

