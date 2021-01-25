package com.example.tify.Minwoo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tify.Minwoo.Activity.CartActivity;
import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.Minwoo.Bean.Menu;
import com.example.tify.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    String TAG = "CartAdapter 확인용";
    CardView cardView;
    private int positionCheck;
    private boolean isStartViewCheck = true;


    private ArrayList<Cart> mDataset;
    private ArrayList<Menu> menuNames;
    private OnItemClickListener mListener = null;

    String mName;
    int sizeUpCount = 0;
    int shotCount = 0;

    Context context = null;
    String macIP;

    //인터페이스 선언
    public interface OnItemClickListener{
        void onItemClick(View v, int position);

    }

    //메인에서 사용할 클릭메서드 선언 (삭제)
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
        Log.v(TAG, "setOnItemClickListener");
    }


    // 메인 액티비티에서 받은 myDataset을 가져오
    public CartAdapter(CartActivity cartActivity, int layout, ArrayList<Cart> myDataset, String macIP) {
        mDataset = myDataset;
        this.context = cartActivity;
        this.macIP = macIP;

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
        public CardView cardView;

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
            cardView = v.findViewById(R.id.activity_Cart_CardView);

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

        if (isStartViewCheck) { // 카드뷰 애니메이션
            if (position > 6) isStartViewCheck = false;
        } else {
            if (position > positionCheck) { holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.down));
            } else {
                holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.up)); }
        }
        // 어뎁터 데이터 화면 표시 구간 holder.textview.setText("데이터 표시"); // 현재 위치값을 positionCheck에 저장 - onBindViewHolder 마지막 부분에 구현 positionCheck = position;

        // - get element from your dataset at this position
        // - replace the contents of the view with that element

//        ArrayList<Integer> numbers = new ArrayList<Integer>();
//        ArrayList<Integer> mNumbers = new ArrayList<Integer>();
//
//        for(int i = 0; i < menuNames.size(); i++){
//            numbers.add(menuNames.get(i).getmNo());
//        }
//        Log.v(TAG, String.valueOf(numbers));
//
//        for(int i = 0; i < mDataset.size(); i++){
//            mNumbers.add(mDataset.get(i).getMenu_mSeqNo());
//        }
//
//        TreeSet<Integer> tree = new TreeSet<Integer>(mNumbers); // 중복제거
//        ArrayList<Integer> mNumbers2 = new ArrayList<Integer>(tree); // 다시 반환
//        Log.v(TAG, String.valueOf(mNumbers2));
//
//        for(int i = 0; i < menuNames.size(); i++){ // 사이즈 맞추기
//            if(mNumbers2.size() != menuNames.size()){
//                mNumbers2.add(0);
//            }
//        }
//        Log.v(TAG, String.valueOf(mNumbers2));
//
//        for(int i = 0; i < menuNames.size(); i++){

//            for(int j = 0; j < mNumbers2.size(); j++){
//                if(mNumbers2.get(j) == menuNames.get(i).getmNo()){
//                    holder.mName.setText(menuNames.get(i).getmName());
//                }
//            }
//        }


//        // cartlist로 Insert하기!
//        if(sizeUpCount == 1 && shotCount == 1){ // 둘다 추가
//            addOrderTotal = 3;
//        }else if(sizeUpCount == 1 && shotCount == 0){ // 사이즈업만
//            addOrderTotal = 2;
//        }else if(shotCount == 1 && sizeUpCount == 0){ // 샷추가만
//            addOrderTotal = 1;
//        }else{ // 아무것도 안함
//            addOrderTotal = 0;
//        }

        Log.v(TAG, "AddShot : " + mDataset.get(position).getcLAddShot());

        // 콤마 찍어서 원화로 바꿔줌!
        NumberFormat moneyFormat = NumberFormat.getInstance(Locale.KOREA);
        String price = moneyFormat.format(mDataset.get(position).getcLPrice());

        Log.v(TAG, "mDataset.get(position).getcLImage() : " + mDataset.get(position).getcLImage());
        Glide.with(context).load("http://" + macIP + ":8080/tify/"+ mDataset.get(position).getcLImage()).into(holder.cLPhoto);

        //데이터를 받은걸 올리기
        if(mDataset.get(position).getcLSizeUp() != 0){
            holder.addOrder1.setText("+사이즈업 X " + mDataset.get(position).getcLSizeUp());
        }else{
            holder.addOrder1.setText("");
        }

        if(mDataset.get(position).getcLAddShot() != 0){
            holder.addOrder2.setText("+샷추가 X " + mDataset.get(position).getcLAddShot());
            holder.addOrder2.setVisibility(View.VISIBLE);
        }else{
            holder.addOrder2.setText("");
        }

        holder.mName.setText(mDataset.get(position).getMenu_mName());
        holder.cLQuantity.setText(mDataset.get(position).getcLQuantity() + "");
        holder.cLPrice.setText(price+ "원");
        holder.request.setText(mDataset.get(position).getcLRequest());

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
        holder.addOrder1.clearComposingText();
        holder.addOrder2.clearComposingText();
        holder.request.clearComposingText();
        holder.cLQuantity.clearComposingText();
        holder.cLPrice.clearComposingText();
        holder.cLPhoto.clearColorFilter();

    }
}
