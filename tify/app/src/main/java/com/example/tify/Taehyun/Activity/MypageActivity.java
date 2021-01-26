package com.example.tify.Taehyun.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Activity.LoginActivity;
import com.example.tify.Hyeona.Activity.MypageActivity_paymentlist;
import com.example.tify.Hyeona.Activity.qrActivity;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Minwoo.Activity.BeforePayActivity;
import com.example.tify.Minwoo.Activity.OrderListActivity;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Adapter.MypageListAdapter;
import com.example.tify.Taehyun.Bean.Bean_MypageList;
import com.example.tify.Taehyun.Bean.Bean_Mypage_userinfo;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

public class MypageActivity extends AppCompatActivity {

    //2021.01.07 - 태현
    //field
    final static String TAG = "MypageActivity";
    private ArrayList<Bean_MypageList> data = null;
    private MypageListAdapter adapter = null;
    private ListView listView = null;
    final static int RValue = 0;

    //the values for return
    TextView nickName = null, email = null;
    CircularImageView profileIv = null;

    //DB
    String uTelNo = null;
    String uPayPassword = null;
    int uNo = 0;
    String uEmail, uNickName, uImage = null;
    //url
    //jsp적을 주소
    String urlAddr = null;
    //
    String urlAddress = null;
    //IP
    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();
    //Bean
    Bean_Mypage_userinfo userinfo = null;

    //변수 값 보내기 위해
    Intent intent = null;
    String mypage_uImage = null;

    BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_mypage);
        nickName = findViewById(R.id.mypage_nickName);
        email = findViewById(R.id.mypage_email);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //하단 네비게이션 선언
        menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) { //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.//
                    case R.id.action1: {
                        Intent intent = new Intent(MypageActivity.this, JiseokMainActivity.class);
                        startActivity(intent);
                        //홈버튼
                        return true;
                    }
                    case R.id.action2: {
                        //주문내역
                        Intent intent = new Intent(MypageActivity.this, OrderListActivity.class);
                        intent.putExtra("from", "JiseokMainActivity"); // value에 어디서 보내는지를 적어주세요
                        startActivity(intent);
                        finish();

                        return true;
                    }
                    case R.id.action3: {
                        Intent intent = new Intent(MypageActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸 여기는 즐겨찾기 부분
                        return true;
                    }

                    case R.id.action4: {
                        Intent intent = new Intent(MypageActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸

                        return true;
                    }
                    case R.id.action5: {
                        //마이페이지
                        Intent intent = new Intent(MypageActivity.this, MypageActivity.class);
                        startActivity(intent);

                        return true;
                    }
                    default:
                        return false;
                }
            }
        });

        //url
        urlAddr = "http://" + MacIP + ":8080/tify/mypage.jsp?";
        //listview
        data = new ArrayList<Bean_MypageList>();
        userinfo = new Bean_Mypage_userinfo();


        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();

        uNo = auto.getInt("userSeq", 0);


        data.add(new Bean_MypageList("프로필 변경", R.drawable.ic_action_go));
        data.add(new Bean_MypageList("카드등록   ", R.drawable.ic_action_go));
        data.add(new Bean_MypageList("내 결제 내역", R.drawable.ic_action_go));
        data.add(new Bean_MypageList("로그아웃   ", R.drawable.ic_action_go));

        adapter = new MypageListAdapter(MypageActivity.this, R.layout.kth_activity_mypage_list, data);
        listView = findViewById(R.id.mypage_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mItemClickListener);
        profileIv = findViewById(R.id.mypage_profileIv);

        intent = getIntent();
        mypage_uImage = intent.getStringExtra("uImage");

    }



    ////////////////////////////////////////////////////////////
    //                                                        //
    //      프로필 변경, 카드등록, 내 주문내역, 로그아웃.
    //          //   2021.01.07 -태현     //
    ////////////////////////////////////////////////////////////
    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {

                //프로필 변경
                case 0:
                    intent = new Intent(MypageActivity.this, Mypage_ProfileChageActivity.class)
                            .putExtra("uNo", uNo)
                            .putExtra("uNickName", uNickName)
                            .putExtra("uTelNo", uTelNo)
                            .putExtra("uImage", uImage)
                            .putExtra("uPayPassword", uPayPassword)
                            .putExtra("MacIP", MacIP);

                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                    break;

                //카드등록
                case 1:
                    //카드 등록햇나 유무 파악
                    int cardcount = cardCountselect();
                    Log.v(TAG,"cardcount : " + cardcount);
                    //카드 있을떄.
                    if (cardcount > 0){
                        Log.v("ㄲㄲㄲ여기기기기", ":"+cardcount);
                        intent = new Intent(MypageActivity.this, Mypage_CardRegistrationActivity.class)
                                .putExtra("uNo", uNo)
                                .putExtra("MacIP", MacIP);

                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                    //카드 없을때
                    if (cardcount == 0 ){
                        intent = new Intent(MypageActivity.this, Mypage_NoCardActivity.class)
                                .putExtra("uNo", uNo)
                                .putExtra("MacIP", MacIP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }

                    break;

                //내 주문 내역
                case 2:
                    intent = new Intent(MypageActivity.this, MypageActivity_paymentlist.class);
                    startActivity(intent);

                    //로그아웃
                case 3:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MypageActivity.this);
                    builder.setTitle("로그아웃");
                    builder.setMessage("로그아웃 하시겠습니까?");
                    builder.setNegativeButton("아니오", null);
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() { // 예를 눌렀을 경우 로그인 창으로 이동
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor autoLogin = auto.edit();
                                    autoLogin.clear();
                                    autoLogin.commit();
                                    Intent intent = new Intent(MypageActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });

                        }
                    });
                    builder.create().show();

                    break;
            }
        }
    };
    ////////////////////////////////////////////////////////////
    //                                                        //
    //                                                        //
    //        켰을때 뜰 데이터 값  //   2021.01.07 -태현     //
    ////////////////////////////////////////////////////////////
    @Override
    protected void onResume() {
        super.onResume();
        connectGetData();

    }
    ////////////////////////////////////////////////////////////
    //                                                        //
    //                                                        //
    //              Mypage_데이터 불러오기 //   2021.01.07 -태현     //
    ////////////////////////////////////////////////////////////
    private void connectGetData() {
        try {
            //임시값


            urlAddress = urlAddr + "uNo=" + uNo;
            Log.v("dddd","dd"+urlAddress);
            NetworkTask_TaeHyun myPageNetworkTask = new NetworkTask_TaeHyun(MypageActivity.this, urlAddress, "select");
            Object obj = myPageNetworkTask.execute().get();
            userinfo = (Bean_Mypage_userinfo) obj;

            //DB
            //전화번호
            uTelNo = userinfo.getuTelNo();
            //결제 비번
            uPayPassword = userinfo.getuPayPassword();
            //이메일
            uEmail = userinfo.getuEmail();
            //닉네임
            uNickName = userinfo.getuNickName();
            //이미지 이름
            uImage = userinfo.getuImage();

            //닉네임,이메일 셋팅
            nickName.setText(uNickName);
            email.setText(uEmail);

            ////////////////////////////////////////////////////////////
            //                                                        //
            //                                                        //
            //                    /이미지 불러오기 //   2021.01.07 -태현     //
            ////////////////////////////////////////////////////////////


          //프로필 사진 셋팅
            sendImageRequest(uImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //           프로필 이미지 불러오기
    //           Date : 2021.01.07 - 태현
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void sendImageRequest(String s) {

        String url = "http://" + MacIP + ":8080/tify/" + s;

        Glide.with(this).load(url).into(profileIv);

    }

    // 전화번호 중복체크
    private int cardCountselect(){
        int cardcount = 0;
        try {

            String urlAddr = "http://" + MacIP + ":8080/tify/mypage_cardcountselect.jsp?user_uNo="+ uNo;

            NetworkTask_TaeHyun countnetworkTask = new NetworkTask_TaeHyun(MypageActivity.this, urlAddr, "count");
            Object obj = countnetworkTask.execute().get();

            cardcount= (int) obj;
        }catch (Exception e){

        }
        return cardcount;
    }

    //Back 버튼 누르면 메인 화면으로 -->
    @Override
    public void onBackPressed() {

        startActivity(new Intent(MypageActivity.this, JiseokMainActivity.class));

    }
    // 액션바----------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayHomeAsUpEnabled(false);
        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowTitleEnabled(false);
        //홈 아이콘을 숨김처리합니다.
        actionBar.setDisplayShowHomeEnabled(false);

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.cha_custom_actionbar, null);

        actionBar.setCustomView(actionbar);
        TextView title = findViewById(R.id.title);
        title.setText("마이페이지");

        ImageButton cart = findViewById(R.id.cart);
        cart.setVisibility(View.VISIBLE);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


//         장바구니 없애려면 위에거 살리면 됨
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        return true;
    }


    //---------------------------------
}////---END