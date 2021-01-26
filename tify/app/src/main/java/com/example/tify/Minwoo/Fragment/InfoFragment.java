package com.example.tify.Minwoo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.tify.ShareVar;

import java.util.ArrayList;

public class InfoFragment extends Fragment {

    // 매장정보를 보여주는 화면

    // layout
    TextView tv_sTime;
    TextView tv_sAddress;
    TextView tv_sComment;
    TextView tv_sTel;

    // bundle
    String sTime;
    String sAddress;
    String sComment;
    String sTel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.lmw_fragment_info, container, false);

        tv_sTime = v.findViewById(R.id.fragment_Info_TV_sTime);
        tv_sTel = v.findViewById(R.id.fragment_Info_TV_sTel);
        tv_sAddress = v.findViewById(R.id.fragment_Info_TV_sAddress);
        tv_sComment = v.findViewById(R.id.fragment_Info_TV_sComment);

        // StoreInfoActivity로 부터 값을 받는다.
        Bundle bundle = getArguments();
        sTime = bundle.getString("sTime");
        sAddress = bundle.getString("sAddress");
        sTel = bundle.getString("sTelNo");
        sComment = bundle.getString("sComment");

        // layout setting
        tv_sTel.setText(sTel);
        tv_sTime.setText(sTime);
        tv_sAddress.setText(sAddress);
        tv_sComment.setText(sComment);

        return v;
    }
}