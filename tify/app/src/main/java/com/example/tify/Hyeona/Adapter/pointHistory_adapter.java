package com.example.tify.Hyeona.Adapter;

import android.content.Context;
import android.media.Image;
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
import com.example.tify.Hyeona.Activity.PointActivity;
import com.example.tify.Hyeona.Bean.Bean_point_history;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_reward;
import com.example.tify.R;

import java.util.ArrayList;

    public class pointHistory_adapter extends RecyclerView.Adapter<pointHistory_adapter.MyViewHolder>  {
        private ArrayList<Bean_point_history> point_history ;
        int user_uNo ;


        public pointHistory_adapter(Context mContext, int layout,int user_uNo,ArrayList<Bean_point_history> point_history) {
            this.point_history = point_history;
            this.user_uNo=user_uNo;
        }


        //private CircularImageView review_profileIv;
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cha_point_history, parent, false);
            //     반복할 xml 파일
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Log.v("TT","ddddddddddddd");
            //connectGetData(user_uNo);
            int rhChoice = point_history.get(position).getRhChoice();

            if(rhChoice == 1){
                holder.rhChoice.setImageResource(R.drawable.ic_action_agree1_h_add);
            }else{
                holder.rhChoice.setImageResource(R.drawable.ic_action_agree1_h_minus);
            };
            String rhDay = point_history.get(position).getRhDay();
            String rhContent = point_history.get(position).getRhContent();
            String rhPointHow = point_history.get(position).getRhPointHow();


            holder.rhDay.setText(rhDay);
            holder.rhContent.setText(rhContent);
            holder.rhPointHow.setText(rhPointHow);


        }

        @Override
        public int getItemCount() {
            return point_history.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            final static String TAG = "MemberAdapter";
           // private CircularImageView point_history1;

            private TextView rhDay;
            private TextView rhContent;
            private TextView rhPointHow;
            private ImageView rhChoice;

            MyViewHolder(View v) {
                super(v);
              //  point_history1 = v.findViewById(R.id.point_history);

                rhDay = v.findViewById(R.id.rhDay);
                rhContent = v.findViewById(R.id.rhContent);
                rhPointHow = v.findViewById(R.id.rhPointHow);
                rhChoice = v.findViewById(R.id.rhChoice);
            }
        }



//        //여기에서 해당 고객의 리워드 히스토리를 띄워야함
//        private void connectGetData(int s){
//            try {
//                String urlAddr = "http://" + macIP + ":8080/tify/pointHistory_select.jsp?";
//                //여기 변경 포인트
//                String urlAddress = urlAddr + "uNo=" + s;
//                CUDNetworkTask_reward CUDNetworkTask_reward = new CUDNetworkTask_reward(pointHistory_adapter.this,urlAddress,"pointHistory_select");
//                Object obj = CUDNetworkTask_reward.execute().get();
//
//                bean_point_history = (Bean_point_history) obj;
//                Log.v("빈값확인","ㅇ"+bean_point_history.getRhContent());
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }



    }


