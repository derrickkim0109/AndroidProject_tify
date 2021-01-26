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
        tv02.setText("\n주변에 있는\n개인카페와 함께");

        TextView tv02_01 = view.findViewById(R.id.tv_02_01);
        tv02_01.setText("티피에서는 당신의 주변에 있는\n이웃 개인카페만 만나보실 수 있습니다.\n*프렌차이즈는 입점할 수 없습니다.");


        return view;
    }

}

