package com.example.tify.Hyeona.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Hyeona.Bean.Bean_review;
import com.example.tify.Hyeona.Bean.Bean_userinfo;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_review;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_userinfo;
import com.example.tify.R;

import java.util.ArrayList;

public class review_adapter extends RecyclerView.Adapter<review_adapter.MyViewHolder> {
    private Context mContext = null;
    private int layout = 0;
    private ArrayList<Bean_review> reviews = null;
    private LayoutInflater inflater = null;
    int user_uNo ;
    Bean_userinfo bean_userinfo = null;
    private String uImage;
    private String uNickName;

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
        //holder.review_profileIv.setImageResource(reviews.get(position).getrImage());
        //이미지 가져오는건 다시 해야함
        user_uNo = reviews.get(position).getUser_uNo();
        connectGetData();
        holder.review_name.setText(uNickName);
        holder.review_day.setText(reviews.get(position).getrInsertDate());
        holder.review_text.setText(reviews.get(position).getrContent());
        holder.review_storesay.setText(reviews.get(position).getrOwnerComment());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final static String TAG = "MemberAdapter";
        private ImageView review_profileIv;
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

    public review_adapter(Context mContext, int layout, ArrayList<Bean_review> reviews) {
        this.mContext = mContext;
        this.layout = layout;
        this.reviews = reviews;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void connectGetData(){
        try {
            String macIP = "192.168.0.55";
            String urlAddr = "http://" + macIP + ":8080/tify/user_info.jsp?";
            //여기 변경 포인트
            String urlAddress = urlAddr + "uNo=" + user_uNo;
            CUDNetworkTask_userinfo mCUDNetworkTask_userinfo = new CUDNetworkTask_userinfo(review_adapter.this, urlAddress,"select");
            Object obj = mCUDNetworkTask_userinfo.execute().get();
            bean_userinfo = (Bean_userinfo) obj;

            uImage = bean_userinfo.getuImage();
            uNickName = bean_userinfo.getuNickName();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

    // Create new views (invoked by the layout manager)


