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

//인트로 스플래시 끝나고 진행되는 슬라이드 첫번째 프레그먼트
// 해당 프레그먼트의 뷰페이저는 ViewPagerActivity
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

        //레이아웃에 텍스트 세팅
        View view = null;
        view = inflater.inflate(R.layout.cha_fragment_first,container,false);
        TextView tv01 = view.findViewById(R.id.tv_01);
        tv01.setText("\n비대면시대\n편리한 스마트오더");

        TextView tv01_01 = view.findViewById(R.id.tv_01_01);
        tv01_01.setText("직접적인 신체적 접촉을 피하고\n생활에 필수적인 커피는\n스마트오더로 바로픽업!");

        //ImageView iv_01 = view.findViewById(R.id.iv_01);
        //iv_01.setImageResource(R.drawable.intro_01);
     return view;
    }

    }

