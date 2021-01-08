package com.example.tify.Taehyun.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.bumptech.glide.Glide;
import com.example.tify.R;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
                    //이미지 서버에 올리기 연결
//                    connectImage(); // 오류 나서 잠굼
                    snickname = nickname.getText().toString();
                    stelNo = telNo.getText().toString();

                    if (snickname.equals("")){
                        Toast.makeText(Mypage_ProfileChageActivity.this,"닉네임을 입력하세요.",Toast.LENGTH_SHORT).show();
                    }else if (stelNo.equals("")){
                        Toast.makeText(Mypage_ProfileChageActivity.this,"전화번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                    }else if (uImage.equals("") || uImage.equals("null")){
                        uImage = "ic_person.png";
                    }else {
                        String result = connectUpdate();

                        if(result.equals("1")){
                            Toast.makeText(Mypage_ProfileChageActivity.this,"업데이트 완료.",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Mypage_ProfileChageActivity.this,"업데이트 실패.",Toast.LENGTH_SHORT).show();
                            break;
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
                case R.id.profile_IV:
                    imagePickDialog();// 2021.01.08 - 태현
                    break;
                case R.id.profile_change:
                    imagePickDialog();// 2021.01.08 - 태현
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

    private void imagePickDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    if (!checkCameraPermissions()){
                        requestCameraPermission();
                    }else {
                        pickFromCamera();
                    }

                }
                else if (which == 1){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }else {
                        pickFromGallery();
                    }
                }
            }
        });

        builder.create().show();
    }

    private void pickFromGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);


    }


    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermissions(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back by clicking back button of actionbar
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(this, "Camera & Storage permission are required", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {

            if (requestCode == IMAGE_PICK_GALLERY_CODE && resultCode == Activity.RESULT_OK){
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == Activity.RESULT_OK){
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;

                    Toast.makeText(this, "" + imageUri, Toast.LENGTH_SHORT).show();


                    profileIv.setImageURI(resultUri);
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error = result.getError();
                    Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
                }
            }
            bitMap(data);

        }catch (Exception e){
            e.printStackTrace();

        }



        super.onActivityResult(requestCode, resultCode, data);
    }
    public void bitMap(Intent data) throws IOException {

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
        imageName = date + "." + f_ext;
        tempSelectFile = new File(devicePath , imageName);
        OutputStream out = new FileOutputStream(tempSelectFile);
        image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        // 임시 파일 경로로 위의 img_path 재정의
        img_path = devicePath + imageName;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);


        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        this.imageName = imgName;
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

//    private void connectImage(){
//        imageurl = "http://" + macIP + ":8080/mypeople/multipartRequest.jsp";
//        ImageNetworkTask imageNetworkTask = new ImageNetworkTask(Mypage_ProfileChageActivity.this,profileIv,img_path,imageurl);
//        //////////////////////////////////////////////////////////////////////////////////////////////
//        //
//        //              NetworkTask Class의 doInBackground Method의 결과값을 가져온다.
//        //
//        //////////////////////////////////////////////////////////////////////////////////////////////
//        try {
//            Integer result = imageNetworkTask.execute(100).get();
//            //////////////////////////////////////////////////////////////////////////////////////////////
//            //
//            //              doInBackground의 결과값으로 Toast생성
//            //
//            //////////////////////////////////////////////////////////////////////////////////////////////
//            switch (result){
//                case 1:
//                    Toast.makeText(DetailViewActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//
//                    //////////////////////////////////////////////////////////////////////////////////////////////
//                    //
//                    //              Device에 생성한 임시 파일 삭제
//                    //
//                    //////////////////////////////////////////////////////////////////////////////////////////////
//                    File file = new File(img_path);
//                    file.delete();
//                    break;
//                case 0:
//                    Toast.makeText(DetailViewActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//            //////////////////////////////////////////////////////////////////////////////////////////////
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

}////---END