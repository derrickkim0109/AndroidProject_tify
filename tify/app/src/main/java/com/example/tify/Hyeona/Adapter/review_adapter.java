package com.example.tify.Hyeona.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_userinfo;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.util.ArrayList;

public class review_adapter extends RecyclerView.Adapter<review_adapter.MyViewHolder> {
    private Context mContext = null;
    private int layout = 0;
    private ArrayList<Bean_review_review> reviews = new ArrayList<Bean_review_review>();
    private LayoutInflater inflater = null;
    int user_uNo ;
    Bean_review_userinfo bean_review_userinfo =new Bean_review_userinfo();
    private String uImage;
    private String uNickName;
    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();
    Intent intent = null;

    //

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cha_reviewcontent, parent, false);
        //     반복할 xml 파일
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.v("TT","ddddddddddddd");


        user_uNo = reviews.get(position).getUser_uNo();
        connectGetData(reviews.get(position).getUser_uNo());

        holder.review_name.setText(uNickName);

       // if(uImage.equals("null")){
       //     holder.review_profileIv.setImageResource(R.drawable.ic_person);
       // }else {
            Glide.with(mContext).load("http://" + MacIP + ":8080/tify/"+ uImage).into(holder.review_profileIv);
        //}
        Glide.with(mContext).load("http://" + MacIP + ":8080/tify/"+ reviews.get(position).getrImage()).into(holder.review_image);
        Log.v("TT","ddddddddddddd"+user_uNo);
        holder.review_day.setText(reviews.get(position).getrInsertDate());

        if (reviews.get(position).getrOwnerComment().equals("null")){
            holder.review_storesay.setVisibility(View.GONE);
            holder.review_storename.setVisibility(View.GONE);
            // 사장님 댓글이 없는 경우 댓글 기본 세팅값도 보이지 않음
        }else{
            holder.review_storesay.setText(reviews.get(position).getrOwnerComment());
            //댓글이 있을 경우에만 나타남
        }

        holder.review_text.setText(reviews.get(position).getrContent());
        Log.v("TT","ddddddddddddd");


    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final static String TAG = "MemberAdapter";
        private CircularImageView review_profileIv;
        private TextView review_name;
        private TextView review_day;
        private TextView review_text;
        private TextView review_storename;
        private TextView review_storesay;
        private ImageView review_image;

        MyViewHolder(View v) {
            super(v);
            review_profileIv = v.findViewById(R.id.review_profileIv);
            review_name = v.findViewById(R.id.review_name);
            review_day = v.findViewById(R.id.review_day);
            review_text = v.findViewById(R.id.review_text);
            review_storename = v.findViewById(R.id.review_storename);
            review_storesay = v.findViewById(R.id.review_storesay);
            review_image = v.findViewById(R.id.review_image);

        }
    }

    public review_adapter(Context mContext, int layout, ArrayList<Bean_review_review> reviews) {
        this.mContext = mContext;
        this.layout = layout;
        this.reviews = reviews;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void connectGetData(int s){
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/user_info.jsp?";
            //여기 변경 포인트
            String urlAddress = urlAddr + "uNo=" + s;
            CUDNetworkTask_userinfo mCUDNetworkTask_userinfo = new CUDNetworkTask_userinfo(urlAddress,"select");
            Object obj = mCUDNetworkTask_userinfo.execute().get();
            bean_review_userinfo = (Bean_review_userinfo) obj;

            uImage = bean_review_userinfo.getuImage();
            uNickName = bean_review_userinfo.getuNickName();
            Log.v("tttt","dd"+uNickName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}

// Create new views (invoked by the layout manager)