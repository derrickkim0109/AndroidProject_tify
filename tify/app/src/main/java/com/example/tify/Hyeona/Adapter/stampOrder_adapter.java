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

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Bean.Bean_review_review;
import com.example.tify.Hyeona.Bean.Bean_review_userinfo;
import com.example.tify.Hyeona.Bean.Bean_stamp_orderlist;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_stamp;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_userinfo;
import com.example.tify.R;

import java.util.ArrayList;

    public class stampOrder_adapter extends RecyclerView.Adapter<stampOrder_adapter.MyViewHolder> {
        private Context mContext = null;
        private int layout = 0;
        private ArrayList<Bean_stamp_orderlist> orderlists = new ArrayList<Bean_stamp_orderlist>();
        private LayoutInflater inflater = null;
        int user_uNo ;
        Bean_stamp_orderlist bean_stamp_orderlist =new Bean_stamp_orderlist();
        private String uImage;
        private String uNickName;
        String macIP = "192.168.0.55";
        //private CircularImageView review_profileIv;
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cha_stamp_orderlist, parent, false);
            //     반복할 xml 파일
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Log.v("TT","ddddddddddddd");

            connectGetData(user_uNo);

//            user_uNo = reviews.get(position).getUser_uNo();
//            connectGetData(reviews.get(position).getUser_uNo());
//            holder.review_name.setText(uNickName);
//            Log.v("TT","ddddddddddddd"+user_uNo);
//            holder.review_day.setText(reviews.get(position).getrInsertDate());
//            holder.review_text.setText(reviews.get(position).getrContent());
//            Log.v("TT","ddddddddddddd");
        }

        @Override
        public int getItemCount() {
            return orderlists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            final static String TAG = "MemberAdapter";
            private CircularImageView stamp_orderlist;
            private TextView count_number;
            private TextView store_name;
            private TextView days;

            MyViewHolder(View v) {
                super(v);
                stamp_orderlist = v.findViewById(R.id.stamp_orderlist);
                count_number = v.findViewById(R.id.count_number);
                store_name = v.findViewById(R.id.store_name);
                days = v.findViewById(R.id.days);
            }
        }

        public stampOrder_adapter(Context mContext, int layout, ArrayList<Bean_stamp_orderlist> orderlists,int user_uNo) {
            this.mContext = mContext;
            this.layout = layout;
            this.orderlists = orderlists;
            this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.user_uNo=user_uNo;
        }

        //여기에서 해당 고객이 어디에서 몇개를 주문했는지 띄워야함
        private void connectGetData(int s){
            try {
                String urlAddr = "http://" + macIP + ":8080/tify/stamp_orderlists.jsp?";
                //여기 변경 포인트
                String urlAddress = urlAddr + "uNo=" + s;
                CUDNetworkTask_stamp CUDNetworkTask_stamp = new CUDNetworkTask_stamp(stampOrder_adapter.this, urlAddress,"select");
                Object obj = CUDNetworkTask_stamp.execute().get();
                bean_stamp_orderlist = (Bean_stamp_orderlist) obj;

//                uImage = bean_review_userinfo.getuImage();
//                uNickName = bean_review_userinfo.getuNickName();
                Log.v("tttt","dd"+uNickName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }



    }


