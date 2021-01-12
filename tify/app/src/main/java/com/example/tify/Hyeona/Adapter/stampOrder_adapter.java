package com.example.tify.Hyeona.Adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tify.Hyeona.Bean.Bean_reward_stamphistory;
import com.example.tify.R;
import java.util.ArrayList;

    public class stampOrder_adapter extends RecyclerView.Adapter<stampOrder_adapter.MyViewHolder> {

        private ArrayList<Bean_reward_stamphistory> stamphistory;


        public stampOrder_adapter(Context mContext, int layout, ArrayList<Bean_reward_stamphistory> stamphistory) {
            this.stamphistory = stamphistory;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.cha_stamp_orderlist, parent, false);
            //반복할 xml 파일
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Log.v("TT","ddddddddddddd");
            Log.v("TT","stamphistory"+stamphistory.get(position).getTotal());
            Log.v("TT","stamphistory"+stamphistory.get(position).getStore_sName());
            Log.v("TT","stamphistory"+stamphistory.get(position).getoInsertDate());


            String num1 = String.valueOf(stamphistory.get(position).getTotal());
            String num2 = stamphistory.get(position).getStore_sName();
            String num3 = stamphistory.get(position).getoInsertDate();


            holder.count_number.setText("+ "+num1);
            holder.store_name.setText(num2);
            holder.days.setText(num3);

        }

        @Override
        public int getItemCount() {
            return stamphistory.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            final static String TAG = "MemberAdapter";

            private TextView count_number;
            private TextView store_name;
            private TextView days;

            MyViewHolder(View v) {
                super(v);

                count_number = v.findViewById(R.id.count_number);
                store_name = v.findViewById(R.id.store_name);
                days = v.findViewById(R.id.days);
            }
        }



    }


