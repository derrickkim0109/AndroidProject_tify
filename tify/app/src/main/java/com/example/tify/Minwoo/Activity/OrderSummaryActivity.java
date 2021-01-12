package com.example.tify.Minwoo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.tify.Minwoo.NetworkTask.LMW_CartNetworkTask;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.text.NumberFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderSummaryActivity extends AppCompatActivity {

    String TAG = "OrderSummaryActivity";

    // MenuFragment로 부터 받을 값 설정
    int mNo;
    String mName;
    int mPrice;
    int mSizeUp;
    int mShot;
    String mImage;
    int mType;
    String mComment;

    String sName;
    String sTel;
    int count = 1;
    int sizeUpCount = 0;
    int shotCount = 0;
    int plusTotal = 0;
    int sizeUpSwitchNum = 0;
    int shotSwitchNum = 0;
    int addOrderTotal = 0;

    // layout
    CircleImageView tv_mPhoto;
    TextView tv_mName;
    TextView tv_mPrice;
    Button tv_mSizeUp;
    Button tv_mShot;
    TextView tv_SizeupTitle;
    TextView tv_ShotTitle;
    Button btn_Plus;
    Button btn_Minus;
    TextView tv_Quantity;
    EditText et_Request;
    TextView tv_TotalPrice;
    Button direct;
    Button cart;
    TextView tv_Content;

    // cartlist Insert
    String macIP;
    String urlAddr = null;
    String where = null;
    int user_uSeqNo = 0;
    int store_sSeqNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_order_summary);


        // MenuFragment로 부터 값을 받는다.
        Intent intent = getIntent();

        mNo = intent.getIntExtra("mNo", 0);
        mName = intent.getStringExtra("mName");
        mPrice = intent.getIntExtra("mPrice", 0);
        mSizeUp = intent.getIntExtra("mSizeUp", 0);
        mShot = intent.getIntExtra("mShot", 0);
        mImage = intent.getStringExtra("mImage");
        mType = intent.getIntExtra("mType", 0);
        mComment = intent.getStringExtra("mComment");
        sName = intent.getStringExtra("sName");

        ShareVar shareVar = new ShareVar();
        macIP = shareVar.getMacIP();
        user_uSeqNo = intent.getIntExtra("user_uSeqNo", 0);
        store_sSeqNo = intent.getIntExtra("store_sSeqNo", 0);



//        sName = intent.getStringExtra("sName");
//        sTel = intent.getStringExtra("sTel");

        Log.v(TAG, "mSizeUp : " + mSizeUp);
        Log.v(TAG, "mShot : " + mShot);
        Log.v(TAG, "mType : " + mType);


        // 기본 세팅------------------------
        tv_mPhoto = findViewById(R.id.orderSummary_CIV_mPhoto);
        tv_mName = findViewById(R.id.orderSummary_TV_mName);
        tv_mPrice = findViewById(R.id.orderSummary_TV_mPrice);
        tv_mSizeUp = findViewById(R.id.orderSummary_Btn_SizeUp);
        tv_mShot = findViewById(R.id.orderSummary_Btn_Shot);
        tv_SizeupTitle = findViewById(R.id.orderSummary_TV_SizeUpTitle);
        tv_ShotTitle = findViewById(R.id.orderSummary_TV_ShotTitle);
        btn_Plus = findViewById(R.id.orderSummary_Btn_Plus);
        btn_Minus = findViewById(R.id.orderSummary_Btn_Minus);
        tv_Quantity = findViewById(R.id.orderSummary_TV_Quantity);
        et_Request = findViewById(R.id.orderSummary_ET_Request);
        direct = findViewById(R.id.orderSummary_Btn_Direct);
        tv_TotalPrice = findViewById(R.id.orderSummary_TV_TotalPrice);
        cart = findViewById(R.id.orderSummary_Btn_Cart);
        tv_Content = findViewById(R.id.orderSummary_TV_Content);

//        tv_mPhoto.setImageResource(); // 태현형한테 사진 받기
        tv_mName.setText(mName);
        tv_Content.setText(mComment);
        sendImageRequest(mImage);

        // 콤마 찍어서 원화로 바꿔줌!
        NumberFormat moneyFormat = NumberFormat.getInstance(Locale.KOREA);
        String price = moneyFormat.format(mPrice);
        tv_mPrice.setText(price + "원");
        tv_TotalPrice.setText(price + "원");
        plusTotal = mPrice;

        if(mType != 0){ // 메뉴가 음료가 아니라면 사이즈업과 샷추가를 숨긴다.
            tv_mSizeUp.setVisibility(View.GONE);
            tv_mShot.setVisibility(View.GONE);
            tv_SizeupTitle.setVisibility(View.GONE);
            tv_ShotTitle.setVisibility(View.GONE);
        }
        if(mSizeUp != 1){ // 0 == 사이즈업 불가능 / 1 == 가능
            tv_mSizeUp.setVisibility(View.GONE);
            tv_SizeupTitle.setVisibility(View.GONE);
        }
        if(mShot != 1){ // 0 == 샷추가 불가능 / 1 == 가능
            tv_mShot.setVisibility(View.GONE);
            tv_ShotTitle.setVisibility(View.GONE);
        }
        //---------------------------------------------

        // 버튼 클릭
        btn_Plus.setOnClickListener(mClickListener);
        btn_Minus.setOnClickListener(mClickListener);
        direct.setOnClickListener(mClickListener);
        cart.setOnClickListener(mClickListener);
        tv_mSizeUp.setOnClickListener(mClickListener);
        tv_mShot.setOnClickListener(mClickListener);

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.order_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }

    View.OnClickListener mClickListener = new View.OnClickListener() { // 버튼들 액션!!!!
        @Override
        public void onClick(View v) {
            Intent intent = null;
            int total = 0;
            NumberFormat moneyFormat = null;
            String strTotal = null;

            switch (v.getId()){
                case R.id.orderSummary_Btn_Direct:


                     intent = new Intent(OrderSummaryActivity.this, BeforePayActivity.class);

                    intent.putExtra("macIP", macIP);
                    intent.putExtra("user_uSeqNo", user_uSeqNo);
                    intent.putExtra("store_sSeqNo", store_sSeqNo);
                    intent.putExtra("totalPrice", plusTotal);
                    intent.putExtra("sName", sName);
                    intent.putExtra("menu_mName", mName);
                    intent.putExtra("olSizeUp", sizeUpCount);
                    intent.putExtra("olAddShot", shotCount);
                    intent.putExtra("olRequest", et_Request.getText().toString());
                    intent.putExtra("olPrice", plusTotal);
                    intent.putExtra("olQuantity", Integer.parseInt(tv_Quantity.getText().toString()));
                    intent.putExtra("from", "OrderSummaryActivity");

                     startActivity(intent);
                     break;
                case R.id.orderSummary_Btn_Plus:
                     count++;

                    if(count > 10){

                        count = 10;
                        tv_Quantity.setText("10");

                        moneyFormat = NumberFormat.getInstance(Locale.KOREA);
                        strTotal = moneyFormat.format(plusTotal);
                        tv_TotalPrice.setText(strTotal + "원");

                        // sTel, sName 넣기
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderSummaryActivity.this);
                        builder.setTitle("<주문 제한>");
                        builder.setMessage("최대 10개까지 주문가능합니다. \n더 많은 주문을 원하시면 매장으로 연락주시길 바랍니다. \n\n매장명 : 스타벅스 강남점  \n매장 연락처 : 031-666-7777");
                        builder.setPositiveButton("확인", null);
                        builder.create().show();
                    }else{
                        tv_Quantity.setText("" + count);
                        plusTotal = plusTotal + mPrice;
                        moneyFormat = NumberFormat.getInstance(Locale.KOREA);
                        strTotal = moneyFormat.format(plusTotal);
                        tv_TotalPrice.setText(strTotal + "원");
                    }

                     break;
                case R.id.orderSummary_Btn_Minus:
                     count--;

                    if (count < 1) {
                        count = 1;
                        plusTotal = mPrice * count;
                        tv_Quantity.setText("1");
                        moneyFormat = NumberFormat.getInstance(Locale.KOREA);
                        strTotal = moneyFormat.format(plusTotal);
                        tv_TotalPrice.setText(strTotal + "원");
                        sizeUpCount = 0;
                        shotCount = 0;
                        tv_mShot.setText("+샷추가");
                        tv_mSizeUp.setText("+사이즈업");

                    }else{
                        tv_Quantity.setText("" + count);
                        plusTotal = mPrice * count;

                        moneyFormat = NumberFormat.getInstance(Locale.KOREA);
                        strTotal = moneyFormat.format(plusTotal);
                        tv_TotalPrice.setText(strTotal + "원");
                        sizeUpCount = 0;
                        shotCount = 0;
                        tv_mShot.setText("+샷추가");
                        tv_mSizeUp.setText("+사이즈업");
                    }
                    break;
                case R.id.orderSummary_Btn_SizeUp:
                    sizeUpCount++;
                    Log.v(TAG, "sizeUpCount : " + sizeUpCount);

                    if(count >= sizeUpCount){

                        if(sizeUpCount <= 10){
                            Log.v(TAG, "sizeUpSwitchNum : " + sizeUpSwitchNum);
                            sizeUpSwitchNum = 1;
                            plusTotal = plusTotal + 1000;
                            tv_mSizeUp.setText("+사이즈업 X " + sizeUpCount);


                            moneyFormat = NumberFormat.getInstance(Locale.KOREA);
                            strTotal = moneyFormat.format(plusTotal);
                            tv_TotalPrice.setText(strTotal + "원");
                        }else{
                            sizeUpCount = 10;
                            tv_mSizeUp.setText("+사이즈업 X " + sizeUpCount);
                        }
                    }else{
                        sizeUpCount = count;
                    }
//                    else{
//                        Log.v(TAG, "sizeUpSwitchNum : " + sizeUpSwitchNum);
//                        sizeUpSwitchNum = 0;
//                        plusTotal = plusTotal - 1000;
//                        tv_mSizeUp.setBackgroundColor(Color.WHITE);
//
//                        moneyFormat = NumberFormat.getInstance(Locale.KOREA);
//                        strTotal = moneyFormat.format(plusTotal);
//                        tv_TotalPrice.setText(strTotal + "원");
//                    }
                    break;
                case R.id.orderSummary_Btn_Shot:
                    shotCount++;
                    Log.v(TAG, "shotCount : " + shotCount);

                    if(count >= shotCount){

                        if(shotCount <= 10){
                            Log.v(TAG, "shotSwitchNum : " + shotSwitchNum);
                            shotSwitchNum = 1;
                            plusTotal = plusTotal + 500;
                            tv_mShot.setText("+샷추가 X " + shotCount);

                            moneyFormat = NumberFormat.getInstance(Locale.KOREA);
                            strTotal = moneyFormat.format(plusTotal);
                            tv_TotalPrice.setText(strTotal + "원");
                        }else{
                            shotCount = 10;
                            tv_mShot.setText("+샷추가 X " + shotCount);
                        }
                    }else{
                        shotCount = count;
                    }
//                    else{
//                        Log.v(TAG, "shotSwitchNum : " + shotSwitchNum);
//                        shotSwitchNum = 0;
//                        plusTotal = plusTotal - 500;
//                        tv_mShot.setBackgroundColor(Color.WHITE);
//
//                        moneyFormat = NumberFormat.getInstance(Locale.KOREA);
//                        strTotal = moneyFormat.format(plusTotal);
//                        tv_TotalPrice.setText(strTotal + "원");
//                    }
                    break;


                case R.id.orderSummary_Btn_Cart:

                    String request = et_Request.getText().toString();
                    int quantity = Integer.parseInt(tv_Quantity.getText().toString());

                    Log.v(TAG, "plusTotal : " + plusTotal);
                    if(request.length() == 0){
                        request = "요청없음";
                    }

                    // NetworkTask 연결
                    urlAddr = "http://" + macIP + ":8080/tify/lmw_cartlist_insert.jsp?user_uSeqNo=" + user_uSeqNo + "&store_sSeqNo=" + store_sSeqNo + "&store_sName=" + sName + "&menu_mName=" + mName + "&cLPrice=" + plusTotal + "&cLQuantity=" + quantity + "&cLImage=" + mImage + "&cLSizeUp=" + sizeUpCount + "&cLAddShot=" + shotCount + "&cLRequest=" + request;
                    where = "insert";
                    String result = connectInsertData();
                    Log.v(TAG, "장바구니 넣기 : " + result);

                    intent = new Intent(OrderSummaryActivity.this, CartActivity.class);
                    intent.putExtra("mPrice", mPrice);
                    intent.putExtra("macIP", macIP);
                    intent.putExtra("user_uSeqNo", user_uSeqNo);
                    intent.putExtra("store_sSeqNo", store_sSeqNo);
                    intent.putExtra("sName", sName);
                    intent.putExtra("mName", mName);
                    intent.putExtra("totalPrice", plusTotal);

                    startActivity(intent);
                    break;

            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String connectInsertData(){ // 장바구니에 넣기
        String result = null;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where <- "insert"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_CartNetworkTask networkTask = new LMW_CartNetworkTask(OrderSummaryActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            Object obj = networkTask.execute().get();
            result = (String) obj;
            ///////////////////////////////////////////////////////////////////////////////////////

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    // 이미지
    private void sendImageRequest(String s) {

        String url = "http://" + macIP + ":8080/tify/" + s;
        Log.v(TAG, "ImageUrl : " + url);
        Glide.with(this).load(url).into(tv_mPhoto);
    }

}