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


public class FirstFragment extends Fragment {

    private String title;
    private int page;

    public static FirstFragment newInstance(int page, String title) {
        FirstFragment fragment = new FirstFragment();
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
        view = inflater.inflate(R.layout.cha_fragment_first,container,false);
        TextView tv01 = view.findViewById(R.id.tv_01);
        tv01.setText("\n태그설정으로\n편리한 그룹관리");

        TextView tv01_01 = view.findViewById(R.id.tv_01_01);
        tv01_01.setText("중복 설정 가능한 태그로\n검색을 빠르게 할 수 있습니다.");

        ImageView iv_01 = view.findViewById(R.id.iv_01);
        //iv_01.setImageResource(R.drawable.intro_01);
     return view;
    }

    }

