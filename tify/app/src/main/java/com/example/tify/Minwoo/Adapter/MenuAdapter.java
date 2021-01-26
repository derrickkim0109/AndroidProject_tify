package com.example.tify.Minwoo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tify.Minwoo.Bean.Menu;
import com.example.tify.Minwoo.Fragment.MenuFragment;
import com.example.tify.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    String TAG = "MenuAdapter";
    Fragment fragment;

    private ArrayList<Menu> mDataset;

    String macIP;

    //인터페이스 선언
    public interface OnItemClickListener{
        void onItemClick(View v, int position);

    }
    private OnItemClickListener mListener = null;

    //메인에서 사용할 클릭메서드 선언
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
        Log.v(TAG, "setOnItemClickListener");
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mName;
        public TextView mPrice;
        public CircleImageView mPhoto;

        MyViewHolder(View v) {
            super(v);
            Log.v(TAG, "MyViewHolder");
            mName = v.findViewById(R.id.fragment_Menu_TV_mName);
            mPrice = v.findViewById(R.id.fragment_Menu_TV_mPrice);
            mPhoto = v.findViewById(R.id.fragment_Menu_CV_Image);

            // 뷰홀더에서만 리스트 포지션값을 불러올 수 있음.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v(TAG, "MyViewHolder onClick");

                    int position=getAdapterPosition();//어뎁터 포지션값
                    // 뷰홀더에서 사라지면 NO_POSITION 을 리턴
                    if(position!=RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(view,position);
                        }
                    }
                }
            });

        }
    }

    // 메인 액티비티에서 받은 myDataset을 가져오
    public MenuAdapter(MenuFragment MenuFragment, int member, ArrayList<Menu> myDataset, String macIP) {
        this.mDataset = myDataset;
        this.fragment = MenuFragment;
        this.macIP = macIP;
        Log.v(TAG, "mDataset size : " + mDataset.size());
        Log.v(TAG, "MenuAdapter Constructor");

    }

    public MenuAdapter(ArrayList<Menu> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public MenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(TAG, "MonCreateViewHolder");
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lmw_fragment_menu_recycler_item, parent, false);
        //     반복할 xml 파일
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.v(TAG, "onBindViewHolder");
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //데이터를 받은걸 올리기
        holder.mName.setText(mDataset.get(position).getmName());

        // 콤마 찍어서 원화로 바꿔줌!
        NumberFormat moneyFormat = NumberFormat.getInstance(Locale.KOREA);
        String price = moneyFormat.format(mDataset.get(position).getmPrice());

        holder.mPrice.setText(price + "원");
        Glide.with(fragment).load("http://" + macIP + ":8080/tify/"+ mDataset.get(position).getmImage()).into(holder.mPhoto);

    }


    @Override
    public int getItemCount() {
        Log.v(TAG, "getItemCount");
        return mDataset.size();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) { // 데이터 꼬임 방지
        super.onViewRecycled(holder);
        holder.mName.clearComposingText();
        holder.mPrice.clearComposingText();
        holder.mPhoto.clearColorFilter();
    }
}
