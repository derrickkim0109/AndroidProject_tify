package com.example.tify.Jiseok.Adapta;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.tify.Jiseok.Bean.Bean_MainCafeList_cjs;
import com.example.tify.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainCafeListAdapter_Top10 extends RecyclerView.Adapter<MainCafeListAdapter_Top10.MyViewHolder>{

    ArrayList<Bean_MainCafeList_cjs> arrayList=null;
    String myLocation ="강남";
    Context context = null;
    String strDistance;
    int count = 0;
    LatLng Location1,Location2;
    double latitude;
    double longitude;
    double latitude2;
    double longitude2;




    //-----------------Click Event---------------------
    //-----------------Click Event---------------------
    //인터페이스 선언
    public interface OnItemClickListener2{
        void onItemClick2(View v, int position);
    }
    private OnItemClickListener2 mListener2 = null;

    //메인에서 사용할 클릭메서드 선언
    public void setOnItemClickListener2(OnItemClickListener2 listener2){
        this.mListener2 = listener2;
    }
    //-----------------Click Event---------------------
    //-----------------Click Event---------------------


    public MainCafeListAdapter_Top10() {
    }

    public MainCafeListAdapter_Top10(Context mContext, int layout, ArrayList<Bean_MainCafeList_cjs> bean_mainCafeList_cjs, String myLocation){
        this.context = mContext;
        this.arrayList=bean_mainCafeList_cjs;
        this.myLocation=myLocation;
        this.latitude=findGeoPoint(context,myLocation).getLatitude();
        this.longitude=findGeoPoint(context,myLocation).getLongitude();
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.cha_maincafe_content, parent, false);
        //반복할 xml 파일
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    public void clearRequestManager(RequestManager requestManager){
        //requestManager.clear();
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


            latitude2=findGeoPoint(context,arrayList.get(position).getsAddress()).getLatitude();
            longitude2=findGeoPoint(context,arrayList.get(position).getsAddress()).getLongitude();
            Log.v("내위치@@",myLocation);
            Log.v("내위치","latitude : "+latitude+", longtiude : "+longitude);
            Log.v("위치","latitude : "+latitude2+", longtiude : "+longitude2);

            Location1 = new LatLng(latitude, longitude);
            Location2 = new LatLng(latitude2, longitude2);
            Log.v("내위치 :",""+getCurrentAddress(Location1));
            Log.v("위치 :",""+getCurrentAddress(Location2));
            Log.v("사이즈 :",""+ arrayList.size());


            double distance1 = getDistance(Location1,Location2);
            String strdistance = Double.toString(distance1);

            Log.v("위치","거리 :" +distance1);



                    holder.cafeTitle.setText(arrayList.get(position).getsName());
                    holder.cafeLike.setText(arrayList.get(position).getLikeCount());
                    holder.cafeReviewCount.setText(arrayList.get(position).getReviewCount());
                    holder.distance.setText(strdistance.substring(0, strdistance.indexOf(".")) + " m");
                    Log.v("위치", "" + arrayList.get(position).getsAddress());





    }

    @Override
    public int getItemCount() {
        Log.v("countcount","123");

        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView cafeTitle;
        public TextView cafeLike;
        public TextView cafeReviewCount;
        public TextView distance;
        public ImageView img;



        MyViewHolder(View v) {
            super(v);
            cafeTitle = v.findViewById(R.id.main_cafeList_title);
            cafeLike = v.findViewById(R.id.main_cafeList_like);
            cafeReviewCount = v.findViewById(R.id.main_cafeList_review);
            distance = v.findViewById(R.id.main_cafeList_distance);
            img = v.findViewById(R.id.main_cafeList_img);

            // 뷰홀더에서만 리스트 포지션값을 불러올 수 있음.


            //-----------------Click Event---------------------
            //-----------------Click Event---------------------
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position=getAdapterPosition();//어뎁터 포지션값
                    // 뷰홀더에서 사라지면 NO_POSITION 을 리턴
                    if(position!=RecyclerView.NO_POSITION){
                        if(mListener2 !=null){
                            mListener2.onItemClick2(view,position);
                        }
                    }
                }
            });
            //-----------------Click Event---------------------
            //-----------------Click Event---------------------

        }
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
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제

            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {

            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            return "더 자세한 주소를 입력해주세요";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }

}
