package com.example.tify.Jiseok.Adapta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Hyeona.Adapter.stampOrder_adapter;
import com.example.tify.Hyeona.Bean.Bean_reward_stamphistory;
import com.example.tify.Jiseok.Bean.Bean_MainCafeList_cjs;
import com.example.tify.R;

import java.util.ArrayList;

public class MainCafeListAdapter extends RecyclerView.Adapter<MainCafeListAdapter.MyViewHolder>{

    ArrayList<Bean_MainCafeList_cjs> arrayList=null;
    String myLocation ="강남";

    //-----------------Click Event---------------------
    //-----------------Click Event---------------------
    //인터페이스 선언
    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    private OnItemClickListener mListener = null;

    //메인에서 사용할 클릭메서드 선언
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    //-----------------Click Event---------------------
    //-----------------Click Event---------------------


    public MainCafeListAdapter(Context mContext, int layout, ArrayList<Bean_MainCafeList_cjs> bean_mainCafeList_cjs,String myLocation){
        this.arrayList=bean_mainCafeList_cjs;
        this.myLocation=myLocation;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.cha_maincafe_content, parent, false);
        //반복할 xml 파일
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.cafeTitle.setText(arrayList.get(position).getsName());
        holder.cafeLike.setText(arrayList.get(position).getLikeCount());
        holder.cafeReviewCount.setText(arrayList.get(position).getReviewCount());
        holder.distance.setText("1");



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView cafeTitle;
        public TextView cafeLike;
        public TextView cafeReviewCount;
        public TextView distance;
        public ImageView img;



        MyViewHolder(View v) {
            super(v);
            cafeTitle = v.findViewById(R.id.main_cafeList_title);
            cafeLike = v.findViewById(R.id.main_cafeList_like);
            cafeReviewCount = v.findViewById(R.id.main_cafeList_review);
            distance = v.findViewById(R.id.main_cafeList_distance);
            img = v.findViewById(R.id.main_cafeList_img);

            // 뷰홀더에서만 리스트 포지션값을 불러올 수 있음.


            //-----------------Click Event---------------------
            //-----------------Click Event---------------------
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position=getAdapterPosition();//어뎁터 포지션값
                    // 뷰홀더에서 사라지면 NO_POSITION 을 리턴
                    if(position!=RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(view,position);
                        }
                    }
                }
            });
            //-----------------Click Event---------------------
            //-----------------Click Event---------------------

        }
    }

}
