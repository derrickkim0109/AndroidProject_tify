package com.example.tify.Jiseok.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Activity.PointActivity;
import com.example.tify.Hyeona.Activity.StampActivity;
import com.example.tify.Hyeona.Activity.main_search;
import com.example.tify.Hyeona.Activity.qrActivity;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_userinfo;
import com.example.tify.Jiseok.Adapta.MainCafeListAdapter;
import com.example.tify.Jiseok.Bean.Bean_MainCafeList_cjs;
import com.example.tify.Jiseok.NetworkTask.CJS_NetworkTask;
import com.example.tify.Jiseok.NetworkTask.CJS_NetworkTask_CafeList;
import com.example.tify.Minwoo.Activity.OrderListActivity;
import com.example.tify.Minwoo.Activity.StoreInfoActivity;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Activity.MypageActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JiseokMainActivity extends AppCompatActivity {
    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();
    LinearLayout ll_hide;
    InputMethodManager inputMethodManager;

    BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    Menu menu;
    ImageView gps_setting, imgStamp, imgPoint, imgRecommend, imgSearch;
    AutoCompleteTextView etSearch;

    LinearLayout real_click;
    TextView main_Edit_SearchText;
    TextView user_coffee_count;
    TextView nickname_main;


    private RecyclerView recyclerView = null;
    private RecyclerView recyclerView2 =null;
    private RecyclerView.LayoutManager layoutManager = null;
    MainCafeListAdapter mainCafeListAdapter =null;
    Bean_MainCafeList_cjs bean_mainCafeList_cjs = new Bean_MainCafeList_cjs();
    ArrayList<Bean_MainCafeList_cjs> arrayList = null;
    String userEmail;
    int userSeq;
    String userNickName;
    String myLocation="noLocation";
    int coffeeCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjs_activity_jiseok_main);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        userEmail = auto.getString("userEmail", null);
        userSeq = auto.getInt("userSeq", 0);
        userNickName = auto.getString("userNickName", null);
        myLocation = auto.getString("myLocation", "noLocation");



        Log.v("내위치",""+myLocation);
        Log.v("내위치",""+userEmail);
        Log.v("내위치",""+userSeq);
        Log.v("내위치",""+userNickName);

        imgPoint = findViewById(R.id.main_img_point);
        imgStamp = findViewById(R.id.main_img_stamp);
        imgRecommend = findViewById(R.id.main_img_recommend);
        imgSearch = findViewById(R.id.main_img_SearchBtn);
        //etSearch = findViewById(R.id.main_Edit_SearchText);
        recyclerView = findViewById(R.id.main_rcv_cafeList);
        real_click = findViewById(R.id.real_click);
        main_Edit_SearchText = findViewById(R.id.main_Edit_SearchText);
        user_coffee_count = findViewById(R.id.user_coffee_count);
        real_click = findViewById(R.id.real_click);

        nickname_main = findViewById(R.id.nickname_main);



        // 만약 주소값이 없으면 이부분 변경 해야 한다.


        Log.v("내위치","구간 1-----");



        // 리스트 띄우기
        if(myLocation == "noLocation"){
            selectCafeList();
            main_Edit_SearchText.setText("위치를 설정해주세요.");
            nickname_main.setText("Top 10 카페리스트");

        }else{
            selectCafeList();
            Log.v("내위치","구간 2-----");
            main_Edit_SearchText.setText(myLocation.substring(0,myLocation.indexOf(" ",3))+"에 계신가요?");
            nickname_main.setText(userNickName+"님 근처에 있는 카페");
            mainCafeListAdapter.setOnItemClickListener(rcvClick);
        }
        Log.v("내위치","구간 3-----");

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // 상단 액션바 삭제
        gps_setting = findViewById(R.id.main_img_gps);
        Glide.with(this).load(R.drawable.gps_setting).into(gps_setting);

        Log.v("내위치","구간 4-----");

        //////////////////////////////////////////////////////////////////////////////////

        real_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JiseokMainActivity.this, main_search.class);
                startActivity(intent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //하단 네비게이션 선언
        menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) { //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.//
                    case R.id.action1: {
                        Intent intent = new Intent(JiseokMainActivity.this, JiseokMainActivity.class);
                        startActivity(intent);
                        //홈버튼
                        return true;
                    }
                    case R.id.action2: {
                        //주문내역
                        Intent intent = new Intent(JiseokMainActivity.this, OrderListActivity.class);
                        intent.putExtra("from", "JiseokMainActivity"); // value에 어디서 보내는지를 적어주세요
                        startActivity(intent);
                        finish();

                        return true;
                    }
                    case R.id.action3: {
                        Intent intent = new Intent(JiseokMainActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸 여기는 즐겨찾기 부분
                        return true;
                    }

                    case R.id.action4: {
                        Intent intent = new Intent(JiseokMainActivity.this, qrActivity.class);
                        startActivity(intent);
                        finish();
                        //준비중 페이지 뜸

                        return true;
                    }
                    case R.id.action5: {
                        //마이페이지
                        Intent intent = new Intent(JiseokMainActivity.this, MypageActivity.class);
                        startActivity(intent);

                        return true;
                    }
                    default:
                        return false;
                }
            }
        });

        //키보드 화면 터치시 숨기기위해 선언.
        ll_hide = findViewById(R.id.detail_ll_hide);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  //OS에서 지원해주는 메소드이다.

        //키보드 화면 터치시 숨김.
        ll_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(ll_hide.getWindowToken(), 0);
            }
        });


        imgStamp.setOnClickListener(imgClickListener);
        imgPoint.setOnClickListener(imgClickListener);
        imgRecommend.setOnClickListener(imgClickListener);

        gps_setting.setOnClickListener(mapListener);
        imgSearch.setOnClickListener(mapListener);

            mainCafeListAdapter.setOnItemClickListener(rcvClick);

    }

    MainCafeListAdapter.OnItemClickListener rcvClick = new MainCafeListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {

            Intent intent = new Intent(JiseokMainActivity.this, StoreInfoActivity.class);
            intent.putExtra("sName",arrayList.get(position).getsName());
            intent.putExtra("sAddress",arrayList.get(position).getsAddress());
            intent.putExtra("sImage",arrayList.get(position).getsImage());
            intent.putExtra("sTime",arrayList.get(position).getsRunningTime());
            intent.putExtra("sTelNo",arrayList.get(position).getsTelNo());
            intent.putExtra("sPackaging",arrayList.get(position).getsPackaging());
            intent.putExtra("sComment",arrayList.get(position).getsComment());
            intent.putExtra("skSeqNo",arrayList.get(position).getStorekeeper_skSeqNo());
            intent.putExtra("skStatus",arrayList.get(position).getSkStatus());
            startActivity(intent);

        }
    };

    MainCafeListAdapter.OnItemClickListener rcvClick2 = new MainCafeListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {

            Intent intent = new Intent(JiseokMainActivity.this, StoreInfoActivity.class);
            intent.putExtra("sName",arrayList.get(position).getsName());
            intent.putExtra("sAddress",arrayList.get(position).getsAddress());
            intent.putExtra("sImage",arrayList.get(position).getsImage());
            intent.putExtra("sTime",arrayList.get(position).getsRunningTime());
            intent.putExtra("sTelNo",arrayList.get(position).getsTelNo());
            intent.putExtra("sPackaging",arrayList.get(position).getsPackaging());
            intent.putExtra("sComment",arrayList.get(position).getsComment());
            intent.putExtra("skSeqNo",arrayList.get(position).getStorekeeper_skSeqNo());
            intent.putExtra("skStatus",arrayList.get(position).getSkStatus());
            startActivity(intent);

        }
    };


    View.OnClickListener imgClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //스탬프버튼
                case R.id.main_img_stamp:
                    startActivity(new Intent(JiseokMainActivity.this, StampActivity.class));
                    break;
                //포인트버튼
                case R.id.main_img_point:
                    startActivity(new Intent(JiseokMainActivity.this, PointActivity.class));
                    break;
                    //친구추천 버튼
                case R.id.main_img_recommend:
                    break;
            }
        }
    };



    View.OnClickListener mapListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_img_gps:
                    Intent intent = new Intent(JiseokMainActivity.this,MyLocationActivity.class);
                    startActivityForResult(intent,9999);
                    finish();
                    break;

                case R.id.main_img_SearchBtn:
                    break;
            }
        }
    };




        private int selectUserSeq() {
            int utc = 0;
            try {
                String urlAddr = "http://" + MacIP + ":8080/tify/userSeqSelect.jsp?uEmail=" + userEmail;
                CJS_NetworkTask cjs_networkTask = new CJS_NetworkTask(JiseokMainActivity.this, urlAddr, "uNoSelect");
                Object obj = cjs_networkTask.execute().get();

                utc = (int) obj;
            } catch (Exception e) {

            }
            return utc;
        }




        private void selectCafeList(){
            String urlAddr="";

            try {
                if(myLocation.equals("noLocation")){
                    urlAddr = "http://" + MacIP + ":8080/tify/cjs_mainCafeListTOP10.jsp";
                }else {
                    urlAddr = "http://" + MacIP + ":8080/tify/mainCafeLocationList.jsp";
                }

                CJS_NetworkTask_CafeList cjs_networkTask_cafeList = new CJS_NetworkTask_CafeList(JiseokMainActivity.this,urlAddr,"selectCafeList");
                Object obj = cjs_networkTask_cafeList.execute().get();

                arrayList = (ArrayList<Bean_MainCafeList_cjs>) obj;


                //리사이클러뷰 규격 만들기
                recyclerView.setHasFixedSize(true);


                //레이아웃 매니저 만들기
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);

                LatLng Location1,Location2;
                double latitude;
                double longitude;
                double latitude2;
                double longitude2;

                latitude=findGeoPoint(JiseokMainActivity.this,myLocation).getLatitude();
                longitude=findGeoPoint(JiseokMainActivity.this,myLocation).getLongitude();
                Location1= new LatLng(latitude,longitude);


                // 1000미터 안에있는 매장의 수 구해서 Adapter 에 값 전
                int arrayCount = 0;
                if(myLocation.equals("noLocation")) {
                    arrayCount=arrayList.size();
                }else{

                    for (int i = 0; i < arrayList.size(); i++) {
                        latitude2 = findGeoPoint(JiseokMainActivity.this, arrayList.get(i).getsAddress()).getLatitude();
                        longitude2 = findGeoPoint(JiseokMainActivity.this, arrayList.get(i).getsAddress()).getLongitude();
                        Log.v("여기여기여기", arrayList.get(i).getsAddress());

                        Location2 = new LatLng(latitude2, longitude2);
                        Log.v("여기여기여기", "" + getDistance(Location1, Location2));
                        if (getDistance(Location1, Location2) < 1000.0) arrayCount++;
                    }
                }

                String cc = "ok";


                mainCafeListAdapter = new MainCafeListAdapter(JiseokMainActivity.this,R.layout.cha_maincafe_content,arrayList,myLocation,arrayCount);
                recyclerView.setAdapter(mainCafeListAdapter);


            } catch (Exception e) {

            }
        }

        

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectCafeList();

    }


    // 주소 -> 위도,경도
    public static Location findGeoPoint(Context mcontext, String address) {
        Location loc = new Location("");
        Geocoder coder = new Geocoder(mcontext);
        List<Address> addr = null;// 한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 설정
        try {
            addr = coder.getFromLocationName(address, 5);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 몇개 까지의 주소를 원하는지 지정 1~5개 정도가 적당
        if (addr != null) {
            for (int i = 0; i < addr.size(); i++) {
                Address lating = addr.get(i);
                double lat = lating.getLatitude(); // 위도가져오기
                double lon = lating.getLongitude(); // 경도가져오기
                loc.setLatitude(lat);
                loc.setLongitude(lon);
            }
        }
        return loc;
    }

    // 거리계산
    public double getDistance(LatLng LatLng1, LatLng LatLng2) {
        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(LatLng1.latitude);
        locationA.setLongitude(LatLng1.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(LatLng2.latitude);
        locationB.setLongitude(LatLng2.longitude);
        distance = locationA.distanceTo(locationB);

        return Math.round(distance);

    }

    @Override
    protected void onResume() {
        super.onResume();
        coffeeCount = connectGetData();
        user_coffee_count.setText("지금까지 주문한 커피는 "+coffeeCount+"잔 입니다.");
    }

    private int connectGetData(){
        int result =0;
        try {
            //  얼마나 커피 주문했는지 받아오기
            String urlAddr = "http://" + MacIP + ":8080/tify/cha_main_coffee_count.jsp?user_uNo="+userSeq;
            CUDNetworkTask_userinfo CUDNetworkTask_userinfo = new CUDNetworkTask_userinfo(urlAddr,"cha_main_coffee_count");
            Object obj = CUDNetworkTask_userinfo.execute().get();
            result = (int)obj;

            Log.v("테스트","ㅇㅇ"+result);

        }catch (Exception e){
            e.printStackTrace();
        }return result;
    }
}






