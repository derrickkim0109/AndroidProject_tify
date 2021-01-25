package com.android.tify_store.Minwoo.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tify_store.Minwoo.Bean.Store;
import com.android.tify_store.Minwoo.NetworkTask.ImageNetworkTask_TaeHyun;
import com.android.tify_store.Minwoo.NetworkTask.LMW_StoreNetworkTask;
import com.android.tify_store.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StoreInfoActivity extends AppCompatActivity {

    // 가게 정보 화면

    String TAG = "StoreInfoActivity";

    // DB Connect
    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo;
    int skStatus = 0;
    String strResult = null;

    ArrayList<Store> stores;
    ArrayList<Store> list;

    // 이미지
    //image_server
    //intent로 받을 이미지 DB (Mypage에서)
    String mImage = null;
    String imageurl = null;
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

    // layout
    ImageView iv_storePhoto;
    TextView tv_storeName;
    TextView tv_storeAddress;
    EditText et_storeName;
    EditText et_storeAddress;
    EditText et_storeTel;
    EditText et_storeTime;
    EditText et_storeComment;
    RadioButton rb_packaging = null;
    RadioButton rb_all = null;
    RadioGroup radioGroup = null;
    Button btn_Insert = null;

    int rgCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_store_info);

        ActivityCompat.requestPermissions(StoreInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        skSeqNo = intent.getIntExtra("skSeqNo", 0);

        // layout 설정
        iv_storePhoto = findViewById(R.id.activity_StoreInfo_IV_StorePhoto);
        tv_storeName = findViewById(R.id.activity_StoreInfo_TV_sName);
        tv_storeAddress = findViewById(R.id.activity_StoreInfo_TV_sAddress);
        et_storeName = findViewById(R.id.activity_StoreInfo_ET_StoreName);
        et_storeAddress = findViewById(R.id.activity_StoreInfo_ET_StoreAddress);
        et_storeTel = findViewById(R.id.activity_StoreInfo_ET_StoreTel);
        et_storeComment = findViewById(R.id.activity_StoreInfo_ET_StoreComment);
        et_storeTime = findViewById(R.id.activity_StoreInfo_ET_StoreTime);
        radioGroup = (RadioGroup)findViewById(R.id.activity_StoreInfo_RG_StorePackaging);
        rb_packaging = findViewById(R.id.activity_StoreInfo_RB_OnlyPackaging);
        rb_all = findViewById(R.id.activity_StoreInfo_RB_All);
        btn_Insert = findViewById(R.id.activity_StoreInfo_Btn_Insert);


        // 데이터 불러오기
        list = new ArrayList<Store>();
        list = connectGetData(); // 데이터 불러오기

        if (list.size() == 0){ // 등록된 데이터가 없다

        }else{ // 있다
            Log.v(TAG, "list.get(0).getsImage() : " + list.get(0).getsImage());
            mImage = list.get(0).getsImage();

            sendImageRequest(mImage);

            tv_storeName.setText(list.get(0).getsName());
            tv_storeAddress.setText(list.get(0).getsAddress());
            et_storeName.setText(list.get(0).getsName());
            et_storeAddress.setText(list.get(0).getsAddress());
            et_storeTel.setText(list.get(0).getsTelNo());
            et_storeComment.setText(list.get(0).getsComment());
            et_storeTime.setText(list.get(0).getsRunningTime());

            btn_Insert.setText("수정");

        }

        // 클릭 리스너
        btn_Insert.setOnClickListener(mClickListener);
        iv_storePhoto.setOnClickListener(mClickListener);

        // 라디오 그룹 설정
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.lmw_storeInfo_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        SharedPreferences sharedPreferences = getSharedPreferences("openStatus", MODE_PRIVATE);
        strResult = sharedPreferences.getString("openResult", "null");
        Log.v(TAG, "오픈 상태 : " + strResult);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()){
                    case R.id.toolbar_storeInfo_open: // 매장 오픈
                        Log.v(TAG, "매장오픈");

                        if(strResult.equals("null")){ // 오픈한 적이 없을 때
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(StoreInfoActivity.this);
                            builder1.setTitle("<매장 오픈>");
                            builder1.setMessage("확인을 누르시면 영업이 시작됩니다. \n시작하시겠습니까?");
                            builder1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    urlAddr = "http://" + macIP + ":8080/tify/lmw_storekeeper_update.jsp?skSeqNo=" + skSeqNo + "&skStatus=" + 1;
                                    where = "update";
                                    strResult = connectStore();
                                    Log.v(TAG, "strResult : " + strResult);
                                    Toast.makeText(StoreInfoActivity.this, "매장을 오픈했습니다!", Toast.LENGTH_SHORT).show();

                                    item.setIcon(R.drawable.ic_action_storeopen_open);
                                }
                            });
                            builder1.setNegativeButton("취소", null);
                            builder1.create().show();


                        }else{ // 이미 오픈했을 때

                            Toast.makeText(StoreInfoActivity.this, "이미 오픈되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.toolbar_storeInfo_edit:
                        et_storeName.setEnabled(true);
                        et_storeAddress.setEnabled(true);
                        et_storeTel.setEnabled(true);
                        et_storeComment.setEnabled(true);
                        et_storeTime.setEnabled(true);
                        rb_packaging.setEnabled(true);
                        rb_all.setEnabled(true);
                        iv_storePhoto.setClickable(true);
                        btn_Insert.setEnabled(true);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 툴바 메뉴 생성

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_storeinfo_edit_toolbar, menu);
        menuInflater.inflate(R.menu.menu_storeinfo_open_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(StoreInfoActivity.this, MainActivity.class);

                intent.putExtra("macIP", macIP);
                intent.putExtra("skSeqNo", skSeqNo);

                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String connectStore(){ // skStatus 1로 바꾸기 => 매장 오픈
        String result = null;

        try {

            LMW_StoreNetworkTask networkTask = new LMW_StoreNetworkTask(StoreInfoActivity.this, urlAddr, where);

            Object obj = networkTask.execute().get();
            result = (String) obj;

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {

        String sName = null;
        String sTelNo = null;
        String sRunningTime = null;
        String sAddress = null;
        int sPackaging = -1;
        String sComment = null;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StoreInfoActivity.this, StoreInfoActivity.class);

            switch (v.getId()){
                case R.id.activity_StoreInfo_Btn_Insert: // 매장 정보 등록

                    sName = et_storeName.getText().toString();
                    sAddress = et_storeAddress.getText().toString();
                    sTelNo = et_storeTel.getText().toString();
                    sComment = et_storeComment.getText().toString();
                    sRunningTime = et_storeTime.getText().toString();

                    if(sName == null || sAddress == null || sTelNo == null || sComment == null || sRunningTime == null){ // 모든 정보 입력받기
                        Toast.makeText(StoreInfoActivity.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        if(btn_Insert.getText().toString().equals("입력")){ // 처음 입력할 경우

                            urlAddr = "http://" + macIP + ":8080/tify/lmw_store_insert.jsp?storekeeper_skSeqNo=" + skSeqNo + "&sName=" + sName + "&sTelNo=" + sTelNo + "&sRunningTime=" + sRunningTime + "&sAddress=" + sAddress + "&sImage=" + imgName + "&sPackaging=" + rgCheck + "&sComment=" + sComment;
                            where = "insert";
                            strResult = connectStore();
                            connectImage();

                            Toast.makeText(StoreInfoActivity.this, "입력 완료!", Toast.LENGTH_SHORT).show();

                            intent.putExtra("macIP", macIP);
                            intent.putExtra("skSeqNo", skSeqNo);
                            startActivity(intent);

                        }else { // 수정일 경우

                            urlAddr = "http://" + macIP + ":8080/tify/lmw_store_update.jsp?storekeeper_skSeqNo=" + skSeqNo + "&sName=" + sName + "&sTelNo=" + sTelNo + "&sRunningTime=" + sRunningTime + "&sAddress=" + sAddress + "&sImage=" + imgName + "&sPackaging=" + rgCheck + "&sComment=" + sComment;
                            where = "update";
                            strResult = connectStore();
                            connectImage();

                            Toast.makeText(StoreInfoActivity.this, "수정 완료!", Toast.LENGTH_SHORT).show();

                            intent.putExtra("macIP", macIP);
                            intent.putExtra("skSeqNo", skSeqNo);
                            startActivity(intent);

                        }
                    }

                    break;

                case R.id.activity_StoreInfo_IV_StorePhoto: // 사진 선택했을 경우 수정
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);// 2021.01.08 - 태현
                    break;

            }

        }
    };

    private ArrayList<Store> connectGetData(){
        ArrayList<Store> beanList = new ArrayList<Store>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_store_select.jsp?storekeeper_skSeqNo=" + skSeqNo;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_StoreNetworkTask networkTask = new LMW_StoreNetworkTask(StoreInfoActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            stores = (ArrayList<Store>) obj;
            Log.v(TAG, "stores.size() : " + stores.size());

            beanList = stores;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.v(TAG, "버튼 클릭 : " + checkedId);

            RadioButton radio_btn = (RadioButton) findViewById(checkedId);
				Toast.makeText(StoreInfoActivity.this, radio_btn.getText().toString(), Toast.LENGTH_SHORT).show();

				switch (checkedId) {
				case R.id.activity_StoreInfo_RB_OnlyPackaging:
				    rgCheck = 0;
					break;
				case R.id.activity_StoreInfo_RB_All:
				    rgCheck = 1;
					break;

				}
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("test", "onPrepareOptionsMenu - 옵션메뉴가 " +
                "화면에 보여질때 마다 호출됨");

        if(strResult.equals("1")){
            Log.v(TAG, "오픈 상태 : 오픈");
            menu.getItem(1).setIcon(R.drawable.ic_action_storeopen_open);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("openStatus",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("openResult", strResult);
        editor.commit();

        Log.v(TAG, "onPause : " + strResult);
    }

    // 이미지
    private void sendImageRequest(String s) {

        String url = "http://" + macIP + ":8080/tify/" + s;
        Log.v(TAG, "ImageUrl : " + url);
        Glide.with(this).load(url).into(iv_storePhoto);
    }

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
                iv_storePhoto.setImageBitmap(image_bitmap_copy);

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
        Log.v(TAG, "imaName : " + imgName);
        if(imgName == null){
            imgName = "null_image.jpg";
        }else{
            imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

            // 확장자 명 저장
            f_ext = imgPath.substring(imgPath.length()-3, imgPath.length());
        }



        return imgPath;
    }//end of getImagePathToUri()

    private void connectImage(){
        imageurl = "http://" + macIP + ":8080/tify/multipartRequest.jsp";
        Log.v("찾는중", "1 imageurl :" + imageurl);
        ImageNetworkTask_TaeHyun imageNetworkTask = new ImageNetworkTask_TaeHyun(StoreInfoActivity.this,iv_storePhoto,img_path,imageurl);
        //////////////////////////////////////////////////////////////////////////////////////////////
        //
        //              NetworkTask Class의 doInBackground Method의 결과값을 가져온다.
        //
        //////////////////////////////////////////////////////////////////////////////////////////////
        try {
            Integer result = imageNetworkTask.execute(100).get();
            Log.v("찾는중", "2 result :" + result);
            //////////////////////////////////////////////////////////////////////////////////////////////
            //
            //              doInBackground의 결과값으로 Toast생성
            //
            //////////////////////////////////////////////////////////////////////////////////////////////
            switch (result){
                case 1:
                    Toast.makeText(StoreInfoActivity.this, "이미지서버 저장 성공 !", Toast.LENGTH_SHORT).show();

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    //
                    //              Device에 생성한 임시 파일 삭제
                    //
                    //////////////////////////////////////////////////////////////////////////////////////////////
                    File file = new File(img_path);
                    file.delete();
                    break;
                case 0:
                    Toast.makeText(StoreInfoActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    break;
            }
            //////////////////////////////////////////////////////////////////////////////////////////////
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}