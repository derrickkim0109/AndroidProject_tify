package com.example.tify.Taehyun.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tify.R;
import com.example.tify.Taehyun.Bean.Bean_MypageList;

import java.util.ArrayList;

public class MypageListAdapter extends BaseAdapter {

    //field  Date.2021.01.06 - 태현
    private Context mContext = null;
    private int layout = 0;

    //Bean   Date.2021.01.06 - 태현
    private ArrayList<Bean_MypageList> data = null;
    private LayoutInflater inflater = null;


    //-----------------------------------------------
    //Date.2021.01.06 - 태현
    //constructor
    //LayoutInflater
    //-----------------------------------------------

    public MypageListAdapter(Context mContext, int layout, ArrayList<Bean_MypageList> data) {
        this.mContext = mContext;
        this.layout = layout;
        this.data = data;

        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }





    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getTerms();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){ //null -> 보여줄게 없다.
            convertView = inflater.inflate(this.layout,parent, false);
        }
        TextView textview_terms = convertView.findViewById(R.id.mypage_text_view);
        ImageView imageview_icon = convertView.findViewById(R.id.mypage_gointo_icon);

//        java Bean에서 가져오는 것.
        textview_terms.setText(data.get(position).getTerms());
        imageview_icon.setImageResource(data.get(position).getInto());



        return convertView;    }
}
