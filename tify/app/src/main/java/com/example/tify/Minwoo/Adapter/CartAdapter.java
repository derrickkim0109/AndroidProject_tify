package com.example.tify.Minwoo.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Minwoo.Activity.CartActivity;
import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    String TAG = "CartAdapter 확인용";

    private ArrayList<Cart> mDataset;
    private OnItemClickListener mListener = null;
    String strAddOrder1;
    String strAddOrder2;
    String strRequest;

    //인터페이스 선언
    public interface OnItemClickListener{
        void onItemClick(View v, int position);

    }

    //메인에서 사용할 클릭메서드 선언
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
        Log.v(TAG, "setOnItemClickListener");
    }

    // 메인 액티비티에서 받은 myDataset을 가져오
    public CartAdapter(CartActivity cartActivity, int layout, ArrayList<Cart> myDataset) {
        mDataset = myDataset;
        Log.v(TAG, "CartAdapter Constructor");

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mName;
        public TextView addOrder1;
        public TextView addOrder2;
        public TextView request;
        public TextView cLQuantity;
        public TextView cLPrice;
        public CircleImageView cLPhoto;

        MyViewHolder(View v) {
            super(v);
            Log.v(TAG, "MyViewHolder");
            cLPhoto = v.findViewById(R.id.activity_Cart_CV_mPhoto);
            mName = v.findViewById(R.id.activity_Cart_CV_mName);
            addOrder1 = v.findViewById(R.id.activity_Cart_CV_AddOrder1);
            addOrder2 = v.findViewById(R.id.activity_Cart_CV_AddOrder2);
            request = v.findViewById(R.id.activity_Cart_CV_Request);
            cLQuantity = v.findViewById(R.id.activity_Cart_CV_Quantity);
            cLPrice = v.findViewById(R.id.activity_Cart_CV_SubTotalPrice);

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
    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(TAG, "onCreateViewHolder");
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lmw_activity_cart_recycler_item, parent, false);
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
        holder.cLPhoto.setImageResource(R.drawable.ic_launcher_foreground);
        holder.mName.setText(mDataset.get(position).getmName());

        if(mDataset.get(position).getAddOrder1() == null){
            holder.addOrder1.setText("");
        }else{
            holder.addOrder1.setText(mDataset.get(position).getAddOrder1());
        }
        if(mDataset.get(position).getAddOrder2() == null){
            holder.addOrder2.setText("");
        }else{
            holder.addOrder2.setVisibility(View.VISIBLE);
            holder.addOrder2.setText(mDataset.get(position).getAddOrder2());
        }
        if(mDataset.get(position).getRequest() == null){
            holder.request.setText("");
        }else{
            holder.request.setText(mDataset.get(position).getRequest());
        }

        holder.cLQuantity.setText(Integer.toString(mDataset.get(position).getcLQuantity()));
        holder.cLPrice.setText(mDataset.get(position).getcLPrice() + "원");


    }

    @Override
    public int getItemCount() {
        Log.v(TAG, "getItemCount");
        return mDataset.size();
    }
}
