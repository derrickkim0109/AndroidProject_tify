package com.example.tify.Hyeona.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tify.R;

public class SecondFragment extends Fragment {

    private String title;
    private int page;

    public static SecondFragment newInstance(int page, String title) {
        SecondFragment fragment = new SecondFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page",page);
        bundle.putString("title",title);
        fragment.setArguments(bundle);


        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        page =getArguments().getInt("page");

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = null;
        view = inflater.inflate(R.layout.cha_fragment_second,container,false);
        TextView tv02 = view.findViewById(R.id.tv_02);
        tv02.setText("\n사진등록으로\n빠른 친구구분");

        TextView tv02_01 = view.findViewById(R.id.tv_02_01);
        tv02_01.setText("주소록 등록시 사진 업로드로 리스트에서\n친구를 사진으로 구별 할 수 있습니다.");

        ImageView iv_02 = view.findViewById(R.id.iv_02);
       //iv_02.setImageResource(R.drawable.intro_02);

        return view;
    }

}

