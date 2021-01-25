package com.example.tify.Hyeona.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Adapter.review_adapter;
import com.example.tify.Hyeona.Bean.Bean_review_review;
import com.example.tify.Hyeona.Bean.Bean_review_store;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_review;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.NetworkTask.ImageNetworkTask_TaeHyun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// 주문내역에서 픽업 완료된 것만 리뷰쓰기가 가능
public class review_white extends AppCompatActivity {
    LinearLayout ll_hide;
    InputMethodManager inputMethodManager ;
    private Context context;
    private int sSeqNo;
    //데이터상 storekeeper_skSeqNo 임 확인필수
    private int uNo;

    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

    private String urlAddr = "http://" + MacIP + ":8080/tify/review_white_storeinfo.jsp?";
    private String urlAddr2 = "http://" + MacIP + ":8080/tify/review_white_update.jsp?";


    private Bean_review_store bean_review_store = new Bean_review_store();
    private String sName;
    private String sImage;
    private String review_contentString;
    String imageurl;
    String img_path = null;// 최종 file name
    Button review_white_complete;
    ImageView review_add_image;
    ImageView review_cancel;
    EditText review_content ;
    String f_ext = null;
    File tempSelectFile;
    String imgName = null;
    String db_review_content;
    int oNo;

    String devicePath = Environment.getDataDirectory().getAbsolutePath() + "/data/com.android.tify/"; //// 외부쓰레드 에서 메인 UI화면을 그릴때 사용 인데 뭔지모르겟음

    //갤러리
    private final int REQ_CODE_SELECT_IMAGE = 300; // Gallery Return Code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_activity_review_white);
        Intent intent = getIntent();
        uNo = intent.getIntExtra("uNo",0);
        oNo = intent.getIntExtra("oNo", 0);
//        sSeqNo = intent.getIntExtra("skSeqNo",0);
        sSeqNo = 1;

        review_white_complete = findViewById(R.id.review_white_complete);
        review_add_image = findViewById(R.id.review_add_image);
        review_cancel = findViewById(R.id.review_cancel);
        review_content = findViewById(R.id.review_content);
        //Glide.with(this).load("http://" + macIP + ":8080/tify/null_image.jpg").into(review_add_image);
        //기본이미지 세팅
        ActivityCompat.requestPermissions(review_white.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE); //카메라 권한? 몰라..

        //키보드 화면 터치시 숨기기위해 선언.
        ll_hide = findViewById(R.id.detail_ll_hide);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  //OS에서 지원해주는 메소드이다.

        //키보드 화면 터치시 숨김.
        ll_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(ll_hide.getWindowToken(),0);
            }
        });

        // 사진 넣기 관련 이벤트
        review_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지 넣는 버튼 눌렀을 때
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                    //카메라, 갤러리를 진행시킴
            }
        });

        review_white_complete.setOnClickListener(new Button.OnClickListener() {
            //완료버튼 눌렀을때 실행
            @Override
            public void onClick(View v) {
                connectImage();
                review_contentString = review_content.getText().toString();

                if (review_contentString.equals("")){
                    new AlertDialog.Builder(review_white.this)
                            .setMessage("후기를 입력해주세요.")
                            .setPositiveButton("확인",null)
                            .show();

                }else if (review_add_image.equals("")){
                    new AlertDialog.Builder(review_white.this)
                            .setMessage("후기에는 사진을 꼭 입력해주셔야합니다.")
                            .setPositiveButton("확인",null)
                            .show();
                }
                else {
                    //여기는 리뷰 내용 저장하는곳
                    String result = connectUpdate();
                    connectoReview();
                    if(result.equals("1")){
                        new AlertDialog.Builder(review_white.this)
                                .setMessage("리뷰를 등록했습니다.")
                                .setPositiveButton("확인",null)
                                .show();
                    }else{
                        //실패
                    }
                    Intent intent1 = new Intent(review_white.this, JiseokMainActivity.class );
                    startActivity(intent1);
                }
          };
        });
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (requestCode == REQ_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
                try {
                    //이미지의 URI를 얻어 경로값으로 반환.
                    img_path = getImagePathToUri(data.getData());


                    //이미지를 비트맵형식으로 반환
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                    Bitmap image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 400, true);
                    review_add_image.setImageBitmap(image_bitmap_copy);

                    // 파일 이름 및 경로 바꾸기(임시 저장, 경로는 임의로 지정 가능)
                    String date = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
                    String imageName = date + "." + f_ext;
                    tempSelectFile = new File(devicePath , imageName);
                    OutputStream out = new FileOutputStream(tempSelectFile);
                    image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    // 임시 파일 경로로 위의 img_path 재정의
                    img_path = devicePath + imageName;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }


        public String getImagePathToUri(Uri data) {

            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(data, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            //이미지의 경로 값
            String imgPath = cursor.getString(column_index);
            Log.v("이미지", "Image Path :" + imgPath);

            //이미지의 이름 값
            imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1).replaceAll("\\p{Z}","");

            // 확장자 명 저장
            f_ext = imgPath.substring(imgPath.length()-3, imgPath.length());

            return imgPath;
        }//end of getImagePathToUri()


        private String connectUpdate() {
            //여기서 업데이트 실행한다!!!
            Log.v("이미지", "connectUpdate()");
            db_review_content = review_content.getText().toString();
            String result = null;
            String urlAddr2_1 = urlAddr2 + "rContent="+db_review_content+"&user_uNo="+uNo+"&storekeeper_skSeqNo="+sSeqNo+"&rImage="+imgName;
            Log.v("이미지", "url!! :" + urlAddr2_1);
            Log.v("이미지", "url!! :" + imgName);
            try {
                CUDNetworkTask_review CUDNetworkTask_review = new CUDNetworkTask_review(review_white.this, urlAddr2_1, "update");
                Object obj = CUDNetworkTask_review.execute().get();
                result = (String) obj;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    private void connectImage(){
        imageurl = "http://" + MacIP + ":8080/tify/multipartRequest.jsp";
        ImageNetworkTask_TaeHyun imageNetworkTask = new ImageNetworkTask_TaeHyun(review_white.this,review_add_image,img_path,imageurl);
        try {
            Integer result = imageNetworkTask.execute(100).get();

            switch (result){
                case 1:

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    //
                    //              Device에 생성한 임시 파일 삭제
                    //
                    //////////////////////////////////////////////////////////////////////////////////////////////
                    File file = new File(img_path);
                    file.delete();
                    break;
                case 0:
                    Toast.makeText(review_white.this, "Error", Toast.LENGTH_SHORT).show();
                    break;
            }
            //////////////////////////////////////////////////////////////////////////////////////////////
        }catch (Exception e){
            e.printStackTrace();
        }
    }

        @Override
        protected void onResume() {
            super.onResume();
            connectGetData();
        }
        private void connectGetData(){
        //기본정보 받아오기
            try {
                String urlAddr3 = urlAddr+"storekeeper_skSeqNo="+sSeqNo;
                Log.v("이거다123",urlAddr3);
                CUDNetworkTask_review mCUDNetworkTask_review = new CUDNetworkTask_review(review_white.this, urlAddr3,"select_review_storeinfo");
                Object obj = mCUDNetworkTask_review.execute().get();
                bean_review_store = (Bean_review_store) obj;

                //받아온 정보

                final ImageView review_store_image = findViewById(R.id.review_store_image);
                sImage = bean_review_store.getsImage();
                Glide.with(this).load("http://" + MacIP + ":8080/tify/"+ sImage).into(review_store_image);

                final TextView review_store_name = findViewById(R.id.review_store_name);
                sName = bean_review_store.getsName();
                review_store_name.setText(sName);

            }catch (Exception e){
                e.printStackTrace();
            }
        };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
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
        title.setText("리뷰작성");
        //ImageButton cart = findViewById(R.id.cart);
        //cart.setVisibility(View.GONE);
        //장바구니 없애려면 위에거 살리면 됨
        //액션바 양쪽 공백 없애기
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        return true;
    }



    private String connectoReview() {// 가져온 oNo이용해 oReview Update
        //여기서 업데이트 실행한다!!!
        Log.v("이미지", "connectoReview()");

        String result = null;
        String urlAddress = "http://" + MacIP + ":8080/tify/lmw_order_update_oreview.jsp?oNo=" + oNo;
        Log.v("이미지", urlAddress);

        try {
            CUDNetworkTask_review CUDNetworkTask_review = new CUDNetworkTask_review(review_white.this, urlAddress, "update");
            Object obj = CUDNetworkTask_review.execute().get();
            result = (String) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}