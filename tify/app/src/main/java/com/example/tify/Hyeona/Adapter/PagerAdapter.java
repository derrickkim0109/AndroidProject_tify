package com.example.tify.Hyeona.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tify.Hyeona.Activity.FirstFragment;
import com.example.tify.Hyeona.Activity.SecondFragment;
import com.example.tify.Hyeona.Activity.ThirdFragment;


public class PagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    public PagerAdapter(FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0 :
                return FirstFragment.newInstance(position,"Page # 1");
            case 1 :
                return SecondFragment.newInstance(position,"Page # 2");
            case 2 :
                return ThirdFragment.newInstance(position,"Page # 3");
        }
        return null;
    }

    @Override
    public int getCount(){return NUM_ITEMS;}


    //인디케이터에 페이지 타이틀 리턴
    @Nullable
    @Override

    public CharSequence getPageTitle(int position){
        return "page"+position;
    }
}
