package com.example.googlemaptest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    //우편검색
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    //현재위치
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private GpsTracker gpsTracker;

    private GoogleMap mMap;
    LatLng location;
    LatLng location2;
    double latitude;
    double longitude;
    double latitude2;
    double longitude2;
    String address = "서울 서초구 서초대로78길 48 (서초동)";

    List<Marker> previous_marker = null; // 검색정보 리스트  마커

    TextView tv;
    Button btn;
    Button myLocationBtn;
    LinearLayout ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("여기", "onCreate");
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv_address);
        btn = findViewById(R.id.btn_search);
        ly = findViewById(R.id.ly_map);
        myLocationBtn = findViewById(R.id.btn_mylocation);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        previous_marker = new ArrayList<Marker>();

        tv.setOnClickListener(addressClick);
        btn.setOnClickListener(searchClick);
        myLocationBtn.setOnClickListener(myLocationClick);

    }

    // 우편번호검색 API 로 이동
    View.OnClickListener addressClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, AddressSearchActivity.class);
            startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
        }
    };

    // 검색버튼
    View.OnClickListener searchClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMap.clear();
            address = tv.getText().toString();
            latitude = findGeoPoint(MainActivity.this, " 서울 서초구 서초대로78길 48 (서초동)").getLatitude();//위도
            longitude = findGeoPoint(MainActivity.this, " 서울 서초구 서초대로78길 48 (서초동)").getLongitude();//경도
            latitude2 = findGeoPoint(MainActivity.this, "서울 서초구 강남대로 349").getLatitude();//위도
            longitude2 = findGeoPoint(MainActivity.this, "서울 서초구 강남대로 349").getLongitude();//경도

            Log.v("여기", "address : " + address);

            location = new LatLng(latitude, longitude);
            location2 = new LatLng(latitude2, longitude2);

            double distance=getDistance(location,location2);
            Log.v("여기", "latitude : " + latitude);
            Log.v("여기", "longitude : " + longitude);
            Log.v("여기", "latitude2 : " + latitude2);
            Log.v("여기", "longitude2 : " + longitude2);


            Log.v("여기","거리 : "+distance);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title("서울");
            markerOptions.snippet("한국의 수도");
            mMap.addMarker(markerOptions);

            // 기존에 사용하던 다음 2줄은 문제가 있습니다.

            // CameraUpdateFactory.zoomTo가 오동작하네요.
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));//카메라위치

            ly.setVisibility(View.VISIBLE);
        }
    };


    //내위치 버튼
    View.OnClickListener myLocationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v("여기", "myLocationClick");
            //myLocation();
            gpsTracker = new GpsTracker(MainActivity.this);
            Log.v("여기","현재위치 : ("+gpsTracker.getLatitude()+","+gpsTracker.getLongitude()+")");
            latitude=gpsTracker.getLatitude();
            longitude=gpsTracker.getLongitude();
            mMap.clear();

            //Log.v("여기","address : "+address);

            Log.v("여기", "latitude : " + latitude);
            Log.v("여기", "longitude : " + longitude);
            location = new LatLng(latitude, longitude);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title("서울");
            markerOptions.snippet("한국의 수도");
            mMap.addMarker(markerOptions);

            // 기존에 사용하던 다음 2줄은 문제가 있습니다.

            // CameraUpdateFactory.zoomTo가 오동작하네요.
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));//카메라위치
            tv.setText(getCurrentAddress(location));
            ly.setVisibility(View.VISIBLE);
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
                        tv.setText(data);
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

        latitude = findGeoPoint(MainActivity.this, address).getLatitude();//위도
        longitude = findGeoPoint(MainActivity.this, address).getLongitude();//경도

        location = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);

        // 기존에 사용하던 다음 2줄은 문제가 있습니다.

        // CameraUpdateFactory.zoomTo가 오동작하네요.
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));//카메라위치
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


    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
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