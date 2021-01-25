package com.example.tify.Hyeona.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tify.R;

public class ThirdFragment extends Fragment {

    private String title;
    private int page;
    Button button;

    public static ThirdFragment newInstance(int page, String title) {
        ThirdFragment fragment = new ThirdFragment();
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
        view = inflater.inflate(R.layout.cha_fragment_third,container,false);

        TextView tv03 = view.findViewById(R.id.tv_03);
        tv03.setText("\n모두다 어려운시기\n주변과 함께 나눠요");

        TextView tv03_01 = view.findViewById(R.id.tv_03_01);
        tv03_01.setText("주변의 이웃과 함께 이겨낼 수 있도록\n 티피를 시작해볼까요?");


        return view;
    }

}
