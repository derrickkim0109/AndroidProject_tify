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
import com.bumptech.glide.Glide;
import com.example.tify.R;
import com.example.tify.Taehyun.Adapter.MypageListAdapter;
import com.example.tify.Taehyun.Bean.Bean_MypageList;
import com.example.tify.Taehyun.Bean.Bean_Mypage_userinfo;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;

import java.util.ArrayList;

public class Mypage extends AppCompatActivity {

    //2021.01.07 - 태현
    //field
    final static String TAG = "DetailViewActivity";
    private ArrayList<Bean_MypageList> data = null;
    private MypageListAdapter adapter = null;
    private ListView listView = null;

    //the values for return
    TextView nickName = null, email = null;
    CircularImageView profileIv = null;

    //DB
    int uTelNo = 0, uPayPassword, uNo = 0;
    String uEmail, uNickName, uImage = null;
    //url
    //jsp적을 주소
    String urlAddr = null;
    //
    String urlAddress = null;
    String macIP = "192.168.200.129";
    //Bean
    Bean_Mypage_userinfo userinfo = null;

    Intent intent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_mypage);

        //url
        urlAddr = "http://" + macIP + ":8080/tify/mypage.jsp?";
        //listview
        data = new ArrayList<Bean_MypageList>();

        data.add(new Bean_MypageList("프로필 변경", R.drawable.ic_action_go));
        data.add(new Bean_MypageList("카드등록   ", R.drawable.ic_action_go));
        data.add(new Bean_MypageList("로그아웃   ", R.drawable.ic_action_go));
        adapter = new MypageListAdapter(Mypage.this, R.layout.kth_activity_mypage_list,data);
        listView = findViewById(R.id.mypage_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mItemClickListener);


        intent = getIntent();
        uNo = intent.getIntExtra("uNo", 0);

        ///////

    }


    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){

                //프로필 변경
                case 1:
                    intent = new Intent(Mypage.this,ProfileChage.class)
                            .putExtra("uNo", uNo)
                            .putExtra("uEmail",uEmail)
                            .putExtra("uNickName",uNickName)
                            .putExtra("uTelNo",uTelNo)
                            .putExtra("uImage",uImage)
                            .putExtra("uPayPassword",uPayPassword);
                    startActivity(intent);
                    break;

                    //카드등록
                case 2:
                    intent = new Intent(Mypage.this,CardRegistration.class)
                            .putExtra("uNo", uNo);
                    startActivity(intent);

                    break;

                    //로그아웃 
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

    @Override
    protected void onResume() {
        super.onResume();
        connectGetData();
    }

    private void connectGetData(){
        try {
            //임시값
            uNo = 1;

            urlAddress = urlAddr + "uNo=" + uNo;
            NetworkTask_TaeHyun myPageNetworkTask = new NetworkTask_TaeHyun(Mypage.this, urlAddress,"select");
            Object obj = myPageNetworkTask.execute().get();
            userinfo = (Bean_Mypage_userinfo) obj;

            nickName.setText(uNickName);
            email.setText(uEmail);
            //DB
            uTelNo = userinfo.getuTelNo();
            uPayPassword = userinfo.getuPayPassword();
            uEmail = userinfo.getuEmail();
            uNickName = userinfo.getuNickName();
            uImage = userinfo.getuImage();

            ////////////////////////////////////////////////////////////
            //                                                        //
            //                                                        //
            //                    /이미지 불러오기 //   2021.01.07 -태현     //
            ////////////////////////////////////////////////////////////


            ///////
            if(uImage.equals("null")){
                profileIv.setImageResource(R.drawable.ic_person);
            }else {
                sendImageRequest(uImage);
            }
            ///////

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //           프로필 이미지 불러오기
    //           Date : 2021.01.07 - 태현
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void sendImageRequest(String s) {

        String url = "http://" + macIP + ":8080/tify/" + s ;
        Glide.with(this).load(url).into(profileIv);

    }
}