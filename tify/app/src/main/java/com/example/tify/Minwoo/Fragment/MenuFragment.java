package com.example.tify.Minwoo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.example.tify.Minwoo.NetworkTask.LMW_MenuNetworkTask;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.util.ArrayList;


public class MenuFragment extends Fragment {

    // 메뉴 정보 보여주는 화면

    String TAG = "MenuFragment";

    private ArrayList<Menu> menuList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MenuAdapter mAdapter;

    private ArrayList<Menu> list;

    String macIP;
    String urlAddr = null;
    String where = null;
    String sName = null;
    int user_uSeqNo = 0;
    int store_sSeqNo = 0;
    int skStatus = 0;
    String sTel;

    RecyclerView RV = null;
    LinearLayout LL = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            // Inflate the layout for this fragment
            View v =  inflater.inflate(R.layout.lmw_fragment_menu, container, false);

            // StoreInfoActivity로 부터 값을 받는다.
            Bundle bundle = getArguments();
            ShareVar shareVar = new ShareVar();
            macIP = shareVar.getMacIP();
            user_uSeqNo = bundle.getInt("user_uSeqNo");
            store_sSeqNo = bundle.getInt("store_sSeqNo");
            sName = bundle.getString("sName");
            skStatus = bundle.getInt("skStatus");
            sTel = bundle.getString("sTelNo");

            // 초기 설정 값
            where = "select";
            urlAddr = "http://" + macIP + ":8080/tify/lmw_menulist.jsp?store_sSeqNo=" + store_sSeqNo;
            Log.v(TAG, "urlAddr : " + urlAddr);
            Log.v(TAG, "macIP : " + macIP);
            Log.v(TAG, "user_uSeqNo : " + user_uSeqNo);
            Log.v(TAG, "store_sSeqNo : " + store_sSeqNo);
            Log.v(TAG, "sName : " + sName);
            //----------------------

            RV = v.findViewById(R.id.fragment_recycler_view);
            LL = v.findViewById(R.id.fragment_Menu_LL);

            //recyclerview
            recyclerView = v.findViewById(R.id.fragment_recycler_view);
            recyclerView.setHasFixedSize(true);
            mAdapter = new MenuAdapter(MenuFragment.this, R.layout.lmw_fragment_menu , menuList, macIP);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            list = new ArrayList<Menu>();
            list = connectGetData(); // db를 통해 받은 데이터를 담는다.

            if(list.size() == 0){
                RV.setVisibility(View.GONE);
                LL.setVisibility(View.VISIBLE);
            }
                mAdapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        if(skStatus == 0){ // 점주가 매장을 오픈하지 않았을 경우
                            Toast.makeText(getContext(), "영업 준비중입니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(getActivity(), OrderSummaryActivity.class);

                            intent.putExtra("mNo", menuList.get(position).getmNo());
                            intent.putExtra("mName", menuList.get(position).getmName());
                            intent.putExtra("mPrice", menuList.get(position).getmPrice());
                            intent.putExtra("mSizeUp", menuList.get(position).getmSizeUp());
                            intent.putExtra("mShot", menuList.get(position).getmShot());
                            intent.putExtra("mImage", menuList.get(position).getmImage());
                            intent.putExtra("mType", menuList.get(position).getmType());
                            intent.putExtra("mComment", menuList.get(position).getmComment());

                            intent.putExtra("macIP", macIP);
                            intent.putExtra("user_uSeqNo", user_uSeqNo);
                            intent.putExtra("store_sSeqNo", store_sSeqNo);
                            intent.putExtra("sName", sName);
                            intent.putExtra("sTel", sTel);

                            startActivity(intent);
                        }
                    }
                });
        return v;
    }

    private ArrayList<Menu> connectGetData(){
        ArrayList<Menu> beanList = new ArrayList<Menu>();
        try {

            LMW_MenuNetworkTask networkTask = new LMW_MenuNetworkTask(getActivity(), urlAddr, where);

            Object obj = networkTask.execute().get();
            menuList = (ArrayList<Menu>) obj;
            Log.v(TAG, "menuList.size() : " + menuList.size());

            mAdapter = new MenuAdapter(MenuFragment.this, R.layout.lmw_fragment_menu_recycler_item, menuList,macIP);
            recyclerView.setAdapter(mAdapter);

            beanList = menuList;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }
}