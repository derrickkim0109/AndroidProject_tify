package com.example.tify.Hyeona.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Activity.main_search;
import com.example.tify.Hyeona.Bean.Bean_main_search;
import com.example.tify.Hyeona.Bean.Bean_point_history;
import com.example.tify.Jiseok.Adapta.MainCafeListAdapter;
import com.example.tify.Minwoo.Activity.StoreInfoActivity;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class search_storeAdapter extends RecyclerView.Adapter<search_storeAdapter.MyViewHolder>  {
        private ArrayList<Bean_main_search> searchs;
        ShareVar shareVar =new ShareVar();
        String MacIP = shareVar.getMacIP();
        private Context mContext = null;
        LatLng Location1,Location2;
        double latitude;
        double longitude;
        double latitude2;
        double longitude2;
        String myLocation;

        public search_storeAdapter(Context mContext, int layout, ArrayList<Bean_main_search> searchs,String myLocation) {
            this.searchs = searchs;
            this.mContext = mContext;
            this.myLocation=myLocation;
            this.latitude=findGeoPoint(mContext,myLocation).getLatitude();
            this.longitude=findGeoPoint(mContext,myLocation).getLongitude();
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cha_searchcafe_content, parent, false);
            //     반복할 xml 파일
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Log.v("TT","ddddddddddddd");
            //connectGetData(user_uNo);

            if (searchs.size()==0){
                new AlertDialog.Builder(mContext)
                        .setTitle("검색결과가 없습니다.")
                        .setPositiveButton("확인",null)
                        .show();

            }
            String sName = searchs.get(position).getsName();
            String sTelNo = searchs.get(position).getsTelNo();
            String sRunningTime = searchs.get(position).getsRunningTime();
            String sAddress = searchs.get(position).getsAddress();
            String sImage = searchs.get(position).getsImage();
            String sPackaging = searchs.get(position).getsPackaging();
            String sComment = searchs.get(position).getsComment();
            String storekeeper_skSeqNo = searchs.get(position).getStorekeeper_skSeqNo();
            String likeCount = searchs.get(position).getLikeCount();
            String reviewCount = searchs.get(position).getReviewCount();
            String skStatus = searchs.get(position).getSkStatus();



            holder.main_cafeList_title.setText(sName);
            ////////////////////////////////////////////////////////////////////////////////////////////////////

            //현재 테스트용으로 이미지가 없으므로, 주석처리 해놓음 !
            // 입력된 상점 이미지가 있을 경우 꼭 살려야함
            //Glide.with(mContext).load("http://" + MacIP + ":8080/tify/"+sImage).into(holder.main_cafeList_img);

            ////////////////////////////////////////////////////////////////////////////////////////////////////
            Glide.with(mContext).load("http://" + MacIP + ":8080/tify/"+ sImage).into(holder.main_cafeList_img);
            holder.main_cafeList_like.setText(likeCount);
            holder.main_cafeList_review.setText(reviewCount);
            //여기에 거리측정 넣어야댐
            latitude2=findGeoPoint(mContext,sAddress).getLatitude();
            longitude2=findGeoPoint(mContext,sAddress).getLongitude();
            Log.v("내위치@@",myLocation);
            Log.v("내위치","latitude : "+latitude+", longtiude : "+longitude);
            Log.v("위치","latitude : "+latitude2+", longtiude : "+longitude2);

            Location1 = new LatLng(latitude, longitude);
            Location2 = new LatLng(latitude2, longitude2);




            if(!myLocation.equals("noLocation")) {
                double distance1 = getDistance(Location1,Location2);
                String strdistance = Double.toString(distance1);
                holder.main_cafeList_distance.setText(strdistance.substring(0, strdistance.indexOf(".")) + " m");
            }else{
                holder.main_cafeList_distance.setText("");
            }

            holder.main_cafeList_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, StoreInfoActivity.class);
                    intent.putExtra("sName",searchs.get(position).getsName());
                    intent.putExtra("sAddress",searchs.get(position).getsAddress());
                    intent.putExtra("sImage",searchs.get(position).getsImage());
                    intent.putExtra("sTime",searchs.get(position).getsRunningTime());
                    intent.putExtra("sTelNo",searchs.get(position).getsTelNo());
                    intent.putExtra("sPackaging",searchs.get(position).getsPackaging());
                    intent.putExtra("sComment",searchs.get(position).getsComment());
                    intent.putExtra("skSeqNo",searchs.get(position).getStorekeeper_skSeqNo());
                    intent.putExtra("skStatus",searchs.get(position).getSkStatus());

                    mContext.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return searchs.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            final static String TAG = "MemberAdapter";

            private TextView main_cafeList_title;
            private ImageView main_cafeList_img;
            private TextView main_cafeList_like;
            private TextView main_cafeList_review;
            private TextView main_cafeList_distance;


            MyViewHolder(View v) {
                super(v);
                main_cafeList_title = v.findViewById(R.id.main_cafeList_title);
                main_cafeList_img = v.findViewById(R.id.main_cafeList_img);
                main_cafeList_like = v.findViewById(R.id.main_cafeList_like);
                main_cafeList_review = v.findViewById(R.id.main_cafeList_review);
                main_cafeList_distance = v.findViewById(R.id.main_cafeList_distance);
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
    }

