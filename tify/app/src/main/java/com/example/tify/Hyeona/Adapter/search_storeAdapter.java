package com.example.tify.Hyeona.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Activity.main_search;
import com.example.tify.Hyeona.Bean.Bean_main_search;
import com.example.tify.Hyeona.Bean.Bean_point_history;
import com.example.tify.Minwoo.Activity.StoreInfoActivity;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.util.ArrayList;


    public class search_storeAdapter extends RecyclerView.Adapter<search_storeAdapter.MyViewHolder>  {
        private ArrayList<Bean_main_search> searchs;
        ShareVar shareVar =new ShareVar();
        String MacIP = shareVar.getMacIP();
        private Context mContext = null;

        public search_storeAdapter(Context mContext, int layout, ArrayList<Bean_main_search> searchs) {
            this.searchs = searchs;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cha_maincafe_content, parent, false);
            //     반복할 xml 파일
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Log.v("TT","ddddddddddddd");
            //connectGetData(user_uNo);

            if (searchs.size()==0){
                new AlertDialog.Builder(mContext)
                        .setTitle("검색결과가 없습니다.")
                        .setPositiveButton("확인",null)
                        .show();

            }
            String sName = searchs.get(position).getsName();
            String sTelNo = searchs.get(position).getsTelNo();
            String sRunningTime = searchs.get(position).getsRunningTime();
            String sAddress = searchs.get(position).getsAddress();
            String sImage = searchs.get(position).getsImage();
            String sPackaging = searchs.get(position).getsPackaging();
            String sComment = searchs.get(position).getsComment();
            String storekeeper_skSeqNo = searchs.get(position).getStorekeeper_skSeqNo();
            String likeCount = searchs.get(position).getLikeCount();
            String reviewCount = searchs.get(position).getReviewCount();
            String skStatus = searchs.get(position).getSkStatus();



            holder.main_cafeList_title.setText(sName);
            ////////////////////////////////////////////////////////////////////////////////////////////////////

            //현재 테스트용으로 이미지가 없으므로, 주석처리 해놓음 !
            // 입력된 상점 이미지가 있을 경우 꼭 살려야함
            //Glide.with(mContext).load("http://" + MacIP + ":8080/tify/"+sImage).into(holder.main_cafeList_img);

            ////////////////////////////////////////////////////////////////////////////////////////////////////

            holder.main_cafeList_like.setText(likeCount);
            holder.main_cafeList_review.setText(reviewCount);
            //여기에 거리측정 넣어야댐
            //holder.main_cafeList_distance.setText(reviewCount);




            holder.main_cafeList_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, StoreInfoActivity.class);
                    intent.putExtra("sName",sName);
                    intent.putExtra("sAddress", sAddress);
                    intent.putExtra("sImage",sImage);
                    intent.putExtra("sTime",sRunningTime);
                    intent.putExtra("sTelNo",sTelNo);
                    intent.putExtra("sPackaging",sPackaging);
                    intent.putExtra("sComment",sComment);
                    intent.putExtra("skSeqNo",storekeeper_skSeqNo);
                    intent.putExtra("skStatus", skStatus);

                    mContext.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return searchs.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            final static String TAG = "MemberAdapter";

            private TextView main_cafeList_title;
            private ImageView main_cafeList_img;
            private TextView main_cafeList_like;
            private TextView main_cafeList_review;
            private TextView main_cafeList_distance;


            MyViewHolder(View v) {
                super(v);
                main_cafeList_title = v.findViewById(R.id.main_cafeList_title);
                main_cafeList_img = v.findViewById(R.id.main_cafeList_img);
                main_cafeList_like = v.findViewById(R.id.main_cafeList_like);
                main_cafeList_review = v.findViewById(R.id.main_cafeList_review);
                main_cafeList_distance = v.findViewById(R.id.main_cafeList_distance);
            }


        }
    }

