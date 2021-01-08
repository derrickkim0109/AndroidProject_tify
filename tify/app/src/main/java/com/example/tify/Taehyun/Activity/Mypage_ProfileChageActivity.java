package com.example.tify.Taehyun.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.bumptech.glide.Glide;
import com.example.tify.R;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;

import java.io.File;

public class Mypage_ProfileChageActivity extends AppCompatActivity {
    //2021.01.07-태현
    //field
    final static String TAG = "ProfileChageActivity";

    //url
    //jsp적을 주소
    String urlAddr = null;
    //
    String urlAddress = null;


    //intent로 받을 이미지 DB (Mypage에서)
    String mImage = null;

    //DB -> update
    int uNo = 0;
    String uTelNo = null;
    String uPayPassword = null;
    String uNickName, uImage = null;
    String macIP = null;

    //image
    CircularImageView profileIv;
    TextView profile_tv_change;

    //XML for findID
    EditText nickname, telNo = null;
    Button btn_update = null;

    Intent intent = null;

    //레이아웃 터치시 키보드내림
    InputMethodManager inputMethodManager;
    LinearLayout profile_ll;

    // 결제 비번 변경구간
    LinearLayout profile_payPasswordCG = null;

    //회원탈퇴
    TextView profile_withdraw = null;
    final String userDeleteMessage = "";

    //카메라, 갤러리
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 100;
    private static final int IMAGE_PICK_GALLERY_CODE = 101;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri;
    ActionBar actionBar;
    private String imageName;
    ///////////
    private String img_path = null;// 최종 file name
    private String f_ext = null;
    File tempSelectFile;
    Uri resultUri = null;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //          Tomcat Server의 IP Address와 Package이름은 수정 하여야 함
    //           2021.01.07 -태현
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    String devicePath = Environment.getDataDirectory().getAbsolutePath() + "/data/com.android.mypeople/Taehyun/Activity";
    String imageurlJsp = null; // 이미지 서버 저장을 위한 JSP
    //// 외부쓰레드 에서 메인 UI화면을 그릴때 사용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_profile_chage);
        //findViewById
        init();
        //EditText를 위한 기능추가
        function();

        //Mypage에서 값들 받기
        intent = getIntent();
        inheritance();

        //url + jsp
        urlAddr = "http://" + macIP + ":8080/tify/mypage_update.jsp?";


        //이미지 불러오기
//        if (mImage.equals("") || mImage.equals("null")) {
//            profileIv.setImageResource(R.drawable.ic_person);
//        } else {
            sendImageRequest(mImage);
//        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //          //이미지 불러오는 메소드
    //           2021.01.07 -태현
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void sendImageRequest(String s) {

        String url = "http://" + macIP + ":8080/tify/" + s;
        Glide.with(this).load(url).into(profileIv);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //          //값들 상속 받기
    //           2021.01.07 -태현
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void inheritance() {
        // 띄울 Data
        nickname.setText(intent.getStringExtra("uNickName"));
        telNo.setText(intent.getStringExtra("uTelNo"));
        mImage = intent.getStringExtra("uImage");

        //IP
        macIP = intent.getStringExtra("macIP");

        //보낼 data
        uNo = intent.getIntExtra("uNo",0);
        uPayPassword = intent.getStringExtra("uPayPassword");

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //          ////자동 하이픈 //// 글자수 제한
    //           2021.01.07 -태현
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void function() {
        //하이픈
        telNo.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        //글자수 제한
        nickname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        telNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //          //findViewById
    //           2021.01.07 -태현
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void init() {


        nickname = findViewById(R.id.profile_et_nickname);
        telNo = findViewById(R.id.profile_et_phone);

        btn_update = findViewById(R.id.profile_btn_update);
        profile_ll = findViewById(R.id.profile_ll);

        btn_update.setOnClickListener(mClickListener); // 업데이트 버튼
        profile_ll.setOnClickListener(mClickListener); // 키보드 내림
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  //OS에서 지원해주는 메소드이다.

        profileIv = findViewById(R.id.profile_IV);
        profile_tv_change = findViewById(R.id.profile_change);

        profileIv.setOnClickListener(imgClickListener);  // 카메라 갤러리
        profile_tv_change.setOnClickListener(imgClickListener); // 카메라 갤러리 변경하기_TextView

        actionBar = getSupportActionBar(); // 카메라 액션바.

        //결제 비번 변경
        profile_payPasswordCG = findViewById(R.id.profile_payPasswordCG);
        profile_payPasswordCG.setOnClickListener(tClickListener);


        //회원탈퇴
        profile_withdraw = findViewById(R.id.profile_withdraw);
        profile_withdraw.setOnClickListener(tClickListener);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //          업데이트 버튼 / 레이아웃 전방위 터치시 키보드 내림
    //           2021.01.07 -태현
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //업데이트
                case R.id.profile_btn_update:
                    connectUpdate();
                    break;

                    //키보드 내리기
                case R.id.profile_ll:
                    inputMethodManager.hideSoftInputFromWindow(profile_ll.getWindowToken(), 0);
                    break;

            }
        }
    };
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //          프로필 사진 / 변경하기 텍스트 누름 -> 카메라/ 갤러리 가기
    //           2021.01.07 -태현
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    View.OnClickListener imgClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.profile_IV:

                    break;
                case R.id.profile_change:

                    break;
            }
        }
    };
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //          프로필 사진, 닉네임, 전화번호 띄우기.
    //           2021.01.07 -태현
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String connectUpdate() {
        Log.v(TAG, "connectUpdate()");

        String result = null;
        urlAddress = urlAddr + "uNo=" + uNo + "&uImage=" + imageName + "&uTelNo=" + uTelNo
                + "&uNickName=" + uNickName;

        Log.v(TAG, "url!! :" + urlAddress);

        try {
            NetworkTask_TaeHyun updateNetworkTask = new NetworkTask_TaeHyun(Mypage_ProfileChageActivity.this, urlAddress, "update");
            Object obj = updateNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //          결제 비번 변경 , 회원 탈퇴
    //           2021.01.08 -태현
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    View.OnClickListener tClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //결제
                case R.id.profile_payPasswordCG:
                    intent = new Intent(Mypage_ProfileChageActivity.this, Mypage_PayPasswordActivity.class)
                            .putExtra("uNo",uNo)
                            .putExtra("uPayPassword", uPayPassword);
                    startActivity(intent);
                    break;

                case R.id.profile_withdraw:
                    Mypage_WithdrawActivity myPage_customDialog = new Mypage_WithdrawActivity(Mypage_ProfileChageActivity.this,uNo,macIP);
                    myPage_customDialog.callFunction(userDeleteMessage);

                    break;
            }
        }
    };


}////---END