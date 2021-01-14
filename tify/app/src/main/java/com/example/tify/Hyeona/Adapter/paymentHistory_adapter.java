package com.example.tify.Hyeona.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Hyeona.Bean.Bean_payment_paylist;
import com.example.tify.Hyeona.Bean.Bean_point_history;
import com.example.tify.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class paymentHistory_adapter extends RecyclerView.Adapter<paymentHistory_adapter.MyViewHolder>  {
        private ArrayList<Bean_payment_paylist> payment_paylists ;

        public paymentHistory_adapter(Context mContext, int layout,ArrayList<Bean_payment_paylist> payment_paylists) {
            this.payment_paylists = payment_paylists;
        }

        //private CircularImageView review_profileIv;
        @NonNull
        @Override
        public paymentHistory_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cha_mypage_paymentcontent, parent, false);
            //     반복할 xml 파일
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Log.v("TT","ddddddddddddd");

            String store_sName = payment_paylists.get(position).getStore_sName();
            String oInsertDate = payment_paylists.get(position).getoInsertDate();
            int oSum = payment_paylists.get(position).getoSum();

            NumberFormat numf = NumberFormat.getInstance(Locale.getDefault());
            String oSum1 = numf.format(oSum);

            Log.v("TT","ddddddddddddd"+store_sName);
            holder.mp_day.setText(oInsertDate);
            holder.mp_storename.setText(store_sName);
            holder.mp_pay.setText(oSum1);



        }

        @Override
        public int getItemCount() {
            return payment_paylists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            final static String TAG = "MemberAdapter";
            // private CircularImageView point_history1;

            private TextView mp_day;
            private TextView mp_storename;
            private TextView mp_pay;

            MyViewHolder(View v) {
                super(v);
                //  point_history1 = v.findViewById(R.id.point_history);

                mp_day = v.findViewById(R.id.mp_day);
                mp_storename = v.findViewById(R.id.mp_storename);
                mp_pay = v.findViewById(R.id.mp_pay);
            }
        }




    }


