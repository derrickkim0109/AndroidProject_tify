package com.example.tify.Minwoo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tify.Minwoo.Activity.OrderSummaryActivity;
import com.example.tify.Minwoo.Adapter.MenuAdapter;
import com.example.tify.Minwoo.Bean.Menu;
import com.example.tify.R;

import java.util.ArrayList;


public class MenuFragment extends Fragment {

    private ArrayList<Menu> menuList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MenuAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            // Inflate the layout for this fragment
            View v =  inflater.inflate(R.layout.lmw_fragment_menu, container, false);

            //recyclerview
            recyclerView = v.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            mAdapter = new MenuAdapter(menuList);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = new Intent(getActivity(), OrderSummaryActivity.class);
                    startActivity(intent);
                }
            });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
    }

    private void prepareData() {
        menuList.add(new Menu("아메리카노(HOT)",3500));
        menuList.add(new Menu("아메리카노(COLD)",4000));
        menuList.add(new Menu("라떼(HOT)",4000));
        menuList.add(new Menu("라떼(COLD)",4500));
        menuList.add(new Menu("치즈케이크",5000));
    }
}