package com.android.tify_store.Minwoo.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.tify_store.Minwoo.NetworkTask.ImageNetworkTask_TaeHyun;
import com.android.tify_store.Minwoo.NetworkTask.LMW_MenuNetworkTask;
import com.android.tify_store.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuInsertActivity extends AppCompatActivity {

    // 메뉴 등록 화면

    String TAG = "MenuInsertActivity";

    // DB Connect
    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo;
    String strResult = null;
    String CResult = null;

    // layout
    CircleImageView civ_mImage;
    EditText et_mName;
    EditText et_mPrice;
    EditText et_mCommnet;
    RadioButton rb_SizeUpOk = null;
    RadioButton rb_SizeUpNo = null;
    RadioGroup radioGroup_SizeUP = null;
    RadioButton rb_AddShotOk = null;
    RadioButton rb_AddShotNo = null;
    RadioGroup radioGroup_AddShot = null;
    RadioButton rb_Drink = null;
    RadioButton rb_Dessert = null;
    RadioGroup radioGroup = null;
    Button btn_Insert = null;

    int typeRGCheck = 0;
    int sizeUpRGCheck = 1;
    int addShotRGCheck = 1;

    // 이미지
    //image_server
    //intent로 받을 이미지 DB (Mypage에서)
    String mImage = null;
    String imageurl = null;
    //카메라, 갤러리
    private final int REQ_CODE_SELECT_IMAGE = 300; // Gallery Return Code

    String imgName = null;
    String img_path = null;// 최종 file name
    String f_ext = null;
    File tempSelectFile;

    String devicePath = Environment.getDataDirectory().getAbsolutePath() + "/data/com.android.tify/";
    //// 외부쓰레드 에서 메인 UI화면을 그릴때 사용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_menu_insert);

        ActivityCompat.requestPermissions(MenuInsertActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE); // 사진 사용권한 확인

        SharedPreferences sharedPreferences = getSharedPreferences("openStatus", MODE_PRIVATE);
        strResult = sharedPreferences.getString("openResult", "null");
        Log.v(TAG, "오픈 상태 : " + strResult);

        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        skSeqNo = intent.getIntExtra("skSeqNo", 0);

        // layout 설정
        civ_mImage = findViewById(R.id.activity_MenuInsert_CIV_mImage);
        et_mName = findViewById(R.id.activity_MenuInsert_ET_mName);
        et_mPrice = findViewById(R.id.activity_MenuInsert_ET_mPrice);
        et_mCommnet = findViewById(R.id.activity_MenuInsert_ET_mComment);
        radioGroup = (RadioGroup)findViewById(R.id.activity_MenuInsert_RG_RadioGroup);
        rb_Drink = findViewById(R.id.activity_MenuInsert_RB_Drink);
        rb_Dessert = findViewById(R.id.activity_MenuInsert_RB_Other);
        radioGroup_SizeUP = (RadioGroup)findViewById(R.id.activity_MenuInsert_RG_RadioGroupSizeUp);
        rb_SizeUpOk = findViewById(R.id.activity_MenuInsert_RB_SizeUpOK);
        rb_SizeUpNo = findViewById(R.id.activity_MenuInsert_RB_SizeUpNO);
        radioGroup_AddShot = (RadioGroup)findViewById(R.id.activity_MenuInsert_RG_RadioGroupAddShot);
        rb_AddShotOk = findViewById(R.id.activity_MenuInsert_RB_AddShotOK);
        rb_AddShotNo = findViewById(R.id.activity_MenuInsert_RB_AddShotNo);
        btn_Insert = findViewById(R.id.activity_MenuInsert_Btn_Insert);


        mImage = "null_image.jpg"; // 초기엔 등록된 이미지가 없으므로 특정 이미지 출력
        sendImageRequest(mImage);

        // 클릭 리스너
        btn_Insert.setOnClickListener(mClickListener);
        civ_mImage.setOnClickListener(mClickListener);

        // 라디오 그룹 설정
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        radioGroup_SizeUP.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        radioGroup_AddShot.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.lmw_menuInsert_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 툴바 메뉴 생성

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_storeinfo_open_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(MenuInsertActivity.this, MenuListActivity.class);

                intent.putExtra("macIP", macIP);
                intent.putExtra("skSeqNo", skSeqNo);

                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String connectMenuInsert(){ // 입력
        String result = null;

        try {
            LMW_MenuNetworkTask networkTask = new LMW_MenuNetworkTask(MenuInsertActivity.this, urlAddr, where);

            Object obj = networkTask.execute().get();
            result = (String) obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    View.OnClickListener mClickListener = new View.OnClickListener() {
        Intent intent = null;
        String mName = null;
        String mPrice = null;
        String mComment = null;

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activity_MenuInsert_CIV_mImage: // 사진추가

                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

                    break;
                case R.id.activity_MenuInsert_Btn_Insert: // 입력

                    mName = et_mName.getText().toString();
                    mPrice = et_mPrice.getText().toString();
                    mComment = et_mCommnet.getText().toString();

                    if(imgName == null){
                        imgName = "null_image.jpg";
                    }

                    if(mName.length() == 0 || mPrice.length() == 0 || mComment.length() == 0){ // 아무런 정보를 입력하지 않았을 경우
                        Toast.makeText(MenuInsertActivity.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else{ // 모든 정보를 입력했을 경우
                        where = "insert";
                        urlAddr = "http://" + macIP + ":8080/tify/lmw_menu_insert.jsp?storekeeper_skSeqNo=" + skSeqNo + "&mName=" + mName + "&mPrice=" + mPrice + "&mSizeUp=" + sizeUpRGCheck + "&mShut=" + addShotRGCheck + "&mImage=" + imgName + "&mType=" + typeRGCheck + "&mComment=" + mComment;

                        CResult = connectMenuInsert();

                        if(CResult.equals("1")){ // DB Action 성공
                            Toast.makeText(MenuInsertActivity.this, "메뉴 등록 성공!", Toast.LENGTH_SHORT).show();

                            connectImage();

                            intent = new Intent(MenuInsertActivity.this, MenuListActivity.class);
                            intent.putExtra("macIP", macIP);
                            intent.putExtra("skSeqNo", skSeqNo);
                            startActivity(intent);
                        }else{ // DB Action 실패
                            Toast.makeText(MenuInsertActivity.this, "메뉴 등록 실패! \n관리자에게 연락바랍니다.", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
            }

        }
    };

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) { // 메뉴 타입 및 사이즈업, 샷추가 가능 여부
            Log.v(TAG, "버튼 클릭 : " + checkedId);

            switch (checkedId) {
                case R.id.activity_MenuInsert_RB_Drink:
                    typeRGCheck = 0;
                    break;
                case R.id.activity_MenuInsert_RB_Other:
                    typeRGCheck = 1;
                    break;
                case R.id.activity_MenuInsert_RB_SizeUpOK:
                    sizeUpRGCheck = 1;
                    break;
                case R.id.activity_MenuInsert_RB_SizeUpNO:
                    sizeUpRGCheck = 0;
                    break;
                case R.id.activity_MenuInsert_RB_AddShotOK:
                    addShotRGCheck = 1;
                    break;
                case R.id.activity_MenuInsert_RB_AddShotNo:
                    addShotRGCheck = 0;
                    break;

            }
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("test", "onPrepareOptionsMenu - 옵션메뉴가 " +
                "화면에 보여질때 마다 호출됨");

        if(strResult.equals("1")){ // 오픈한 경우 색칠된 아이콘을 띄워 오픈상태 쉽게 확인 가능
            Log.v(TAG, "오픈 상태 : 오픈");
            menu.getItem(0).setIcon(R.drawable.ic_action_storeopen_open);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    // 이미지
    private void sendImageRequest(String s) {

        String url = "http://" + macIP + ":8080/tify/" + s;
        Log.v(TAG, "ImageUrl : " + url);
        Glide.with(this).load(url).into(civ_mImage);
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
                civ_mImage.setImageBitmap(image_bitmap_copy);

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

    private void connectImage(){
        imageurl = "http://" + macIP + ":8080/tify/multipartRequest.jsp";
        Log.v("찾는중", "1 imageurl :" + imageurl);
        ImageNetworkTask_TaeHyun imageNetworkTask = new ImageNetworkTask_TaeHyun(MenuInsertActivity.this,civ_mImage,img_path,imageurl);
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
                    Toast.makeText(MenuInsertActivity.this, "이미지서버 저장 성공 !", Toast.LENGTH_SHORT).show();

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    //
                    //              Device에 생성한 임시 파일 삭제
                    //
                    //////////////////////////////////////////////////////////////////////////////////////////////
                    File file = new File(img_path);
                    file.delete();
                    break;
                case 0:
                    Toast.makeText(MenuInsertActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    break;
            }
            //////////////////////////////////////////////////////////////////////////////////////////////
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}