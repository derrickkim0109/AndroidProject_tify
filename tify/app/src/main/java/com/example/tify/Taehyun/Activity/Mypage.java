package com.example.tify.Taehyun.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.tify.R;
import com.example.tify.Taehyun.Adapter.MypageListAdapter;
import com.example.tify.Taehyun.Bean.MypageList;

import java.util.ArrayList;

public class Mypage extends AppCompatActivity {

    //2021.01.07 - 태현
    //field
    final static String TAG = "DetailViewActivity";
    private ArrayList<MypageList> data = null;
    private MypageListAdapter adapter = null;
    private ListView listView = null;

    //the values for return
    TextView nickName = null, email = null;
    CircularImageView profileIv = null;

    Intent intent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_mypage);

        //listview
        data = new ArrayList<MypageList>();

        data.add(new MypageList("프로필 변경", R.drawable.ic_action_go));
        data.add(new MypageList("카드등록   ", R.drawable.ic_action_go));
        data.add(new MypageList("로그아웃   ", R.drawable.ic_action_go));
        adapter = new MypageListAdapter(Mypage.this, R.layout.kth_activity_mypage_list,data);
        listView = findViewById(R.id.mypage_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mItemClickListener);


        intent = getIntent();
        String imageLoad = intent.getStringExtra("fImage");

        ///////
//        if(imageLoad.equals("null")){
//            profileIv.setImageResource(R.drawable.ic_person);
//        }else {
//            sendImageRequest(imageLoad);
//        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //           프로필 이미지 불러오기
    //           Date : 2021.01.07 - 태현
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void sendImageRequest(String s) {

//        String url = "http://" + macIP + ":8080/mypeople/" + s ;
//        Glide.with(this).load(url).into(profileIv);

    }
    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 1:
                    intent = new Intent(Mypage.this,ProfileChage.class);
                    startActivity(intent);
                    break;
                case 2:

                    break;
                case 3:
                    AlertDialog.Builder builder = new AlertDialog.Builder(Mypage.this);
                    builder.setTitle("로그아웃");
                    builder.setMessage("로그아웃 하시겠습니까?");
                    builder.setNegativeButton("아니오", null);
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() { // 예를 눌렀을 경우 로그인 창으로 이동
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            intent = new Intent(Mypage.this, LoginActivity.class);
//                            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
//                            SharedPreferences.Editor autoLogin = auto.edit();
//
//                            autoLogin.clear();
//                            autoLogin.commit();
//                            startActivity(intent);
                            // 아이디값 넘기기?
                        }
                    });
                    builder.create().show();

                    break;
            }
        }
    };
}