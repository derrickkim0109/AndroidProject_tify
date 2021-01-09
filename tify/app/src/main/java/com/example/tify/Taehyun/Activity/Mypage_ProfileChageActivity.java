package com.example.tify.Taehyun.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.bumptech.glide.Glide;
import com.example.tify.R;
import com.example.tify.Taehyun.NetworkTask.ImageNetworkTask_TaeHyun;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    //image_server
    String imageurl = null;


    //XML for findID
    EditText nickname, telNo = null;
    Button btn_update = null;

    Intent intent = null;
    String snickname = null, stelNo =null;

    //레이아웃 터치시 키보드내림
    InputMethodManager inputMethodManager;
    LinearLayout profile_ll;

    // 결제 비번 변경구간
    LinearLayout profile_payPasswordCG = null;

    //회원탈퇴
    TextView profile_withdraw = null;
    final String userDeleteMessage = "";

    //카메라, 갤러리
    private final int REQ_CODE_SELECT_IMAGE = 300; // Gallery Return Code

    String imgName = null;
    ///////////
    String img_path = null;// 최종 file name
    String f_ext = null;
    File tempSelectFile;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //          Tomcat Server의 IP Address와 Package이름은 수정 하여야 함
    //           2021.01.07 -태현
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    String devicePath = Environment.getDataDirectory().getAbsolutePath() + "/data/com.android.tify/";
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
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //          사용자에게 사진(Media) 사용 권한 받기
        //
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ActivityCompat.requestPermissions(Mypage_ProfileChageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        //url + jsp
        urlAddr = "http://" + macIP + ":8080/tify/mypage_update.jsp?";

        //카메라, 갤러리 허용


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

        profileIv = findViewById(R.id.profile_profile_IV);
        profile_tv_change = findViewById(R.id.profile_change);

        profileIv.setOnClickListener(imgClickListener);  // 카메라 갤러리
        profile_tv_change.setOnClickListener(imgClickListener); // 카메라 갤러리 변경하기_TextView


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
                    //이미지 서버에 올리기 연결
                    connectImage();
                    snickname = nickname.getText().toString();
                    stelNo = telNo.getText().toString();

                    if (snickname.equals("")){
                        Toast.makeText(Mypage_ProfileChageActivity.this,"닉네임을 입력하세요.",Toast.LENGTH_SHORT).show();
                    }else if (stelNo.equals("")){
                        Toast.makeText(Mypage_ProfileChageActivity.this,"전화번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String result = connectUpdate();
                        if(result.equals("1")){
                            Toast.makeText(Mypage_ProfileChageActivity.this,"성공 .",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Mypage_ProfileChageActivity.this,"업데이트 실패 .",Toast.LENGTH_SHORT).show();

                        }
                        finish();
                    }
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
                case R.id.profile_profile_IV:
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);// 2021.01.08 - 태현
                    break;
                case R.id.profile_change:
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);// 2021.01.08 - 태현
                    break;
            }
        }
    };

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
    ////////////////////////////////////////////////////////////
    //                                                        //
    //                                                        //
    //        //사진 //2020.12.27-태현         //
    //                                                        //
    //                                                        //
    ////////////////////////////////////////////////////////////




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                //이미지의 URI를 얻어 경로값으로 반환.
                img_path = getImagePathToUri(data.getData());
                Log.v(TAG, "image path :" + img_path);
                Log.v(TAG, "Data :" +String.valueOf(data.getData()));

                //이미지를 비트맵형식으로 반환
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                Bitmap image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                profileIv.setImageBitmap(image_bitmap_copy);

                // 파일 이름 및 경로 바꾸기(임시 저장, 경로는 임의로 지정 가능)
                String date = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
                String imageName = date + "." + f_ext;
                tempSelectFile = new File(devicePath , imageName);
                OutputStream out = new FileOutputStream(tempSelectFile);
                image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                // 임시 파일 경로로 위의 img_path 재정의
                img_path = devicePath + imageName;
                Log.v(TAG,"fileName :" + img_path);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getImagePathToUri(Uri data) {

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.v(TAG, "Image Path :" + imgPath);

        //이미지의 이름 값
        imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        // 확장자 명 저장
        f_ext = imgPath.substring(imgPath.length()-3, imgPath.length());

        return imgPath;
    }//end of getImagePathToUri()

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //          프로필 사진, 닉네임, 전화번호 업데이트 .
    //           2021.01.07 -태현
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String connectUpdate() {
        Log.v(TAG, "connectUpdate()");
        uTelNo = telNo.getText().toString();
        uNickName = nickname.getText().toString();

        String result = null;
        urlAddress = urlAddr + "uNo=" + uNo + "&uImage=" + imgName + "&uTelNo=" + uTelNo
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

    private void connectImage(){
        imageurl = "http://" + macIP + ":8080/tify/multipartRequest.jsp";
        ImageNetworkTask_TaeHyun imageNetworkTask = new ImageNetworkTask_TaeHyun(Mypage_ProfileChageActivity.this,profileIv,img_path,imageurl);
        //////////////////////////////////////////////////////////////////////////////////////////////
        //
        //              NetworkTask Class의 doInBackground Method의 결과값을 가져온다.
        //
        //////////////////////////////////////////////////////////////////////////////////////////////
        try {
            Integer result = imageNetworkTask.execute(100).get();
            //////////////////////////////////////////////////////////////////////////////////////////////
            //
            //              doInBackground의 결과값으로 Toast생성
            //
            //////////////////////////////////////////////////////////////////////////////////////////////
            switch (result){
                case 1:
                    Toast.makeText(Mypage_ProfileChageActivity.this, "이미지서버 저장 성공 !", Toast.LENGTH_SHORT).show();

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    //
                    //              Device에 생성한 임시 파일 삭제
                    //
                    //////////////////////////////////////////////////////////////////////////////////////////////
                    File file = new File(img_path);
                    file.delete();
                    break;
                case 0:
                    Toast.makeText(Mypage_ProfileChageActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    break;
            }
            //////////////////////////////////////////////////////////////////////////////////////////////
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}////---END