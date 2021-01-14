package com.example.tify.Jiseok.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.example.tify.R;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    ImageView imgBackBtn,imgSearch;
    TextView etSearch;
    LinearLayout lyMap;
    Button btnMyLocation;
    //우편검색
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    //현재위치
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private GpsTracker gpsTracker;

    private GoogleMap mMap;
    String myAddress="서울";
    LatLng myLocation;
    double latitude;
    double longitude;

    List<Marker> previous_marker = null; // 검색정보 리스트  마커

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjs_activity_my_location);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //액션바 삭제

       // imgBackBtn = findViewById(R.id.myLocation_img_backBtn);
        imgSearch = findViewById(R.id.myLocation_img_search);
        etSearch = findViewById(R.id.myLocation_et_search);
        lyMap = findViewById(R.id.myLocation_ly_map);
        btnMyLocation = findViewById(R.id.myLocation_btn_myLocationSetting);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myLocation_fragment_map);
        mapFragment.getMapAsync(this);



        //imgBackBtn.setOnClickListener(onClickListener);
        imgSearch.setOnClickListener(onClickListener);
        etSearch.setOnClickListener(onClickListener);
        btnMyLocation.setOnClickListener(onClickListener);



    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 위치검색 주소 API 로 이동
                case R.id.myLocation_et_search:
                    Intent i = new Intent(MyLocationActivity.this, AddressSearchActivity.class);
                    startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                    break;
                    // 검색어에 잇는 주소를 지도에 띄움
                case R.id.myLocation_img_search:
                    mMap.clear();
                    myAddress=etSearch.getText().toString();
                    latitude = findGeoPoint(MyLocationActivity.this, myAddress).getLatitude();//위도
                    longitude = findGeoPoint(MyLocationActivity.this, myAddress).getLongitude();//경도

                    myLocation = new LatLng(latitude,longitude);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(myLocation);
                    markerOptions.title("서울");
                    markerOptions.snippet("한국의 수도");
                    mMap.addMarker(markerOptions);

                    // 기존에 사용하던 다음 2줄은 문제가 있습니다.

                    // CameraUpdateFactory.zoomTo가 오동작하네요.
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));//카메라위치

                    lyMap.setVisibility(View.VISIBLE);
                    btnMyLocation.setVisibility(View.VISIBLE);

                    break;
                case R.id.myLocation_btn_myLocationSetting:
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("myLocation", myAddress);
                    autoLogin.commit();
                    Log.v("내위치",""+myAddress);
                    new AlertDialog.Builder(MyLocationActivity.this)
                            .setTitle("위치가 설정되었습니다.")
                            .setPositiveButton("확인", null)
                            .show();
                    break;
//                case R.id.myLocation_img_backBtn:
//                    startActivity(new Intent(MyLocationActivity.this,JiseokMainActivity.class));
//                    break;
            }


        }
    };
    // 우편번호검색에서 돌아왔을때 실행되는 곳
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.v("여기", "onActivityResult");

        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        etSearch.setText(data);
                    }
                }
                break;
            case GPS_ENABLE_REQUEST_CODE: //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;

        }
    }


    // 맵읽어오는 매서드 실행되자마자 켜짐.
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.v("여기", "onMapReady");
        mMap = googleMap;

        latitude = findGeoPoint(MyLocationActivity.this, myAddress).getLatitude();//위도
        longitude = findGeoPoint(MyLocationActivity.this, myAddress).getLongitude();//경도

        myLocation = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(myLocation);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);

        // 기존에 사용하던 다음 2줄은 문제가 있습니다.

        // CameraUpdateFactory.zoomTo가 오동작하네요.
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));//카메라위치
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


    // 위도,경도 -> 주소
    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            return "더 자세한 주소를 입력해주세요";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.cha_custom_actionbar, null);

        actionBar.setCustomView(actionbar);
        TextView title = findViewById(R.id.title);
        title.setText("");

        ImageButton cart = findViewById(R.id.cart);
        cart.setVisibility(View.INVISIBLE);
//         장바구니 없애려면 위에거 살리면 됨
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyLocationActivity.this, JiseokMainActivity.class);
                startActivity(intent);
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        return true;
    }

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MyLocationActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    void checkRunTimePermission(){
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MyLocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MyLocationActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MyLocationActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MyLocationActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MyLocationActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MyLocationActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}