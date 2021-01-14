package com.example.tify.Taehyun.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Minwoo.Adapter.CartAdapter;
import com.example.tify.R;
import com.example.tify.Taehyun.Bean.Bean_Mypage_cardlist;

import java.util.ArrayList;

public class Mypage_CardListAdapter extends RecyclerView.Adapter<Mypage_CardListAdapter.MyViewHolder>{

    ///field
    private ArrayList<Bean_Mypage_cardlist> cardlists ;
    private Context mContext = null;
    String MacIP = "";
    int uNo = 0;
    private CartAdapter.OnItemClickListener mListener = null;


    public Mypage_CardListAdapter (Context mContext, int layout, ArrayList<Bean_Mypage_cardlist> cardlists, String MacIP, int uNo) {
        this.cardlists = cardlists;
        this.MacIP = MacIP;
        this.uNo = uNo;
        this.mContext = mContext;
    }
    //인터페이스 선언
    public interface OnItemClickListener{
        void onItemClick(View v, int position);

    }

    //메인에서 사용할 클릭메서드 선언 (삭제)
    public void setOnItemClickListener(CartAdapter.OnItemClickListener listener){
        this.mListener = listener;

    }


    //
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kth_activity_mypage_cardimagelist, parent, false);

        //     반복할 xml 파일
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //connectGetData(user_uNo);
        String cCardCompany = cardlists.get(position).getcCardCompany();
        String card_cMM = cardlists.get(position).getcMM();
        String card_cYear = cardlists.get(position).getcYear();
        String card_cNo = cardlists.get(position).getcCardNo();

        int cNo = cardlists.get(position).getcNo();



        if (cCardCompany.equals("MASTERCARD")) {
            String white = "#FAFAFA";
            String black = "#FF000000";

            holder.cardBox_Company.setImageResource(R.drawable.mastercard);

            holder.cardBox_color.setBackgroundColor(Color.parseColor(white));
            holder.cardBox_Vaild.setTextColor(Color.parseColor(black));
            holder.cardBox_cMM.setTextColor(Color.parseColor(black));
            holder.cardBox_slash.setTextColor(Color.parseColor(black));
            holder.cardBox_cYear.setTextColor(Color.parseColor(black));
            holder.cardBox_number.setTextColor(Color.parseColor(black));

        }else if (cCardCompany.equals("VISA")) {
            String blue = "#282C43";
            String white = "#FAFAFA";


            holder.cardBox_color.setBackgroundColor(Color.parseColor(blue));
            holder.cardBox_Vaild.setTextColor(Color.parseColor(white));
            holder.cardBox_cMM.setTextColor(Color.parseColor(white));
            holder.cardBox_slash.setTextColor(Color.parseColor(white));
            holder.cardBox_cYear.setTextColor(Color.parseColor(white));
            holder.cardBox_number.setTextColor(Color.parseColor(white));
            holder.cardBox_Company.setImageResource(R.drawable.visa);

        }else if (cCardCompany.equals("AMEX")) {

            String green = "0x2098FB98";
            String black = "0x20000000";
            String white = "#FAFAFA";

            holder.cardBox_Company.setImageResource(R.drawable.amex);

            holder.cardBox_AMEX.setVisibility(View.VISIBLE);

            holder.cardBox_AMEX.setTextColor(0x50808080);
            holder.cardBox_color.setBackgroundColor(0x50808080);
            holder.cardBox_Vaild.setTextColor(0x50808080);
            holder.cardBox_cMM.setTextColor(0x50808080);
            holder.cardBox_slash.setTextColor(0x50808080);
            holder.cardBox_cYear.setTextColor(0x50808080);
            holder.cardBox_number.setTextColor(0x50808080);

        }else if (cCardCompany.equals("DINERS_CLUB And CARTE_BLANCHE")) {

            String gold = "FFB5A416";
            String black = "#FF000000";

            holder.cardBox_Company.setImageResource(R.drawable.dinersclub);

            holder.cardBox_color.setBackgroundColor(Color.parseColor(gold));
            holder.cardBox_Vaild.setTextColor(Color.parseColor(black));
            holder.cardBox_cMM.setTextColor(Color.parseColor(gold));
            holder.cardBox_slash.setTextColor(Color.parseColor(gold));
            holder.cardBox_cYear.setTextColor(Color.parseColor(gold));
            holder.cardBox_number.setTextColor(Color.parseColor(gold));

        }else if (cCardCompany.equals("DISCOVER")) {

            String gold = "FFB5A416";
            String black = "#FF000000";
            holder.cardBox_Company.setImageResource(R.drawable.discover);

            holder.cardBox_color.setBackgroundColor(Color.parseColor(gold));
            holder.cardBox_Vaild.setTextColor(Color.parseColor(black));
            holder.cardBox_cMM.setTextColor(Color.parseColor(gold));
            holder.cardBox_slash.setTextColor(Color.parseColor(gold));
            holder.cardBox_cYear.setTextColor(Color.parseColor(gold));
            holder.cardBox_number.setTextColor(Color.parseColor(gold));

        }else if (cCardCompany.equals("JCB")) {
            String gold = "FFB5A416";
            String black = "#FF000000";
            holder.cardBox_Company.setImageResource(R.drawable.jcb);

            holder.cardBox_color.setBackgroundColor(Color.parseColor(gold));
            holder.cardBox_Vaild.setTextColor(Color.parseColor(black));
            holder.cardBox_cMM.setTextColor(Color.parseColor(gold));
            holder.cardBox_slash.setTextColor(Color.parseColor(gold));
            holder.cardBox_cYear.setTextColor(Color.parseColor(gold));
            holder.cardBox_number.setTextColor(Color.parseColor(gold));

        }

        ///   월/년   , 카드 숫자 , 마그네틱 이미지, 슬래시,  유효기간 (글자)
        holder.cardBox_cardMagnetic.setImageResource(R.drawable.magnetic);
        holder.cardBox_Vaild.setText("Valid Thru");
        holder.cardBox_cMM.setText(card_cMM);
        holder.cardBox_slash.setText("/");
        holder.cardBox_cYear.setText(card_cYear);
        String str= "";
        for (int i = 0; i < card_cNo.length()-4; i++) {
            if (i%4 == 0 ){
                str += " ";
            }
            str +="*";
        }
        str +=" ";

            holder.cardBox_number.setText(str+card_cNo.substring((card_cNo.length()-4),card_cNo.length()));


    }





    @Override
    public int getItemCount() {
        return cardlists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final static String TAG = "CardListAdapter";

        private CardView cardBox_color;
        private ImageView cardBox_cardMagnetic;
        private ImageView cardBox_Company;
        private TextView cardBox_number;
        private TextView cardBox_AMEX;
        private TextView cardBox_Vaild;
        private TextView cardBox_cMM;
        private TextView cardBox_slash;
        private TextView cardBox_cYear;


        MyViewHolder(View v) {

            super(v);

            cardBox_color = v.findViewById(R.id.cardBox_color);
            cardBox_cardMagnetic = v.findViewById(R.id.cardBox_cardMagnetic);

            cardBox_number = v.findViewById(R.id.cardBox_number);
            cardBox_Company = v.findViewById(R.id.cardBox_Company);
            cardBox_AMEX = v.findViewById(R.id.cardBox_AMEX);
            cardBox_Vaild = v.findViewById(R.id.cardBox_Vaild);
            cardBox_cMM = v.findViewById(R.id.cardBox_cMM);
            cardBox_slash = v.findViewById(R.id.cardBox_slash);
            cardBox_cYear = v.findViewById(R.id.cardBox_cYear);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position);
                        }
                    }
                }
            });

        }

    }
}
