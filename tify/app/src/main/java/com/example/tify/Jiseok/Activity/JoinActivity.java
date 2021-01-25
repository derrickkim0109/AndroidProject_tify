package com.example.tify.Jiseok.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.tify.Jiseok.NetworkTask.CJS_NetworkTask;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.NetworkTask.ImageNetworkTask_TaeHyun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {
    InputMethodManager inputMethodManager ;

    LinearLayout ly1,ly2,ly;
    EditText etTel, etAuthentication, etEmail, etNickName;
    Button btnAuthentication, btnGo, btnGoGo;
    ImageView imgProfilePlus;
    CircularImageView imgProfile;
    TextView time_counter;
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)
    static final int SMS_RECEIVE_PERMISSON = 1;

    private final int MY_PERMISSION_REQUEST_SMS = 1001;
    int timerCount = 0; // 타이머 누른 횟수
    int timeout = 0; // 타임아웃된 횟수
    String passPhone=null;
    String patternTel="^\\d{2,3}-\\d{3,4}-\\d{4}$";
    String patternEmail="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String patternNickName="^[가-힣ㄱ-ㅎa-zA-Z0-9._ -]{2,}\\$";


    String getTextCheck = null; // editText null 체크용 변수

    String MacIP= ShareVar.getMacIP();

    String imageurl;
    String img_path = null;// 최종 file name
    String f_ext = null;
    File tempSelectFile;
    String imgName = null;
    String db_review_content;
    String devicePath = Environment.getDataDirectory().getAbsolutePath() + "/data/com.android.tify/"; //// 외부쓰레드 에서 메인 UI화면을 그릴때 사용 인데 뭔지모르겟음
    //갤러리
    private final int REQ_CODE_SELECT_IMAGE = 300; // Gallery Return Code


    String userEmail=null;
    String userNickName=null;
    String userTel=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.cjs_activity_join);
        ActivityCompat.requestPermissions(JoinActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);



        ly = findViewById(R.id.join_ly_Layout);
        // 1번 프레임
        ly1 = findViewById(R.id.join_ly_fram1);
        etTel = findViewById(R.id.join_et_tel);
        btnAuthentication = findViewById(R.id.join_btn_authentication);
        etAuthentication = findViewById(R.id.join_et_authenticationNum);
        btnGo = findViewById(R.id.join_btn_go);
        // 2번 프레임
        ly2 = findViewById(R.id.join_ly_fram2);
        imgProfile = findViewById(R.id.join_img_profile);
        imgProfilePlus = findViewById(R.id.join_img_profilePlus);
        etEmail = findViewById(R.id.join_et_email);
        etNickName = findViewById(R.id.join_et_nickName);
        btnGoGo = findViewById(R.id.join_btn_gogo);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        userNickName = intent.getStringExtra("userNickName");
        userTel = intent.getStringExtra("userTel");


        btnAuthentication.setOnClickListener(firstFrameClickListener);
        btnGo.setOnClickListener(firstFrameClickListener);

        imgProfilePlus.setOnClickListener(secondFrameClickListener);
        btnGoGo.setOnClickListener(secondFrameClickListener);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  //OS에서 지원해주는 메소드이다.

        //키보드 화면 터치시 숨김.
        ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(ly.getWindowToken(),0);
            }
        });

        if(userTel!=null){
            etTel.setText(userTel);
        }


        etTel.addTextChangedListener(new TextWatcher() {//자동으로 "-" 생성해서 전화번호에 붙여주기


            private int beforeLenght = 0;
            private int afterLenght = 0;

            //입력 혹은 삭제 전의 길이와 지금 길이를 비교하기 위해 beforeTextChanged에 저장
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeLenght = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //아무글자도 없는데 지우려고 하면 로그띄우기 에러방지
                if (s.length() <= 0) {
                    Log.d("addTextChangedListener", "onTextChanged: Intput text is wrong (Type : Length)");
                    return;
                }
                //특수문자 입력 방지
                char inputChar = s.charAt(s.length() - 1);
                if (inputChar != '-' && (inputChar < '0' || inputChar > '9')) {
                    etTel.getText().delete(s.length() - 1, s.length());
                    return;
                }

                afterLenght = s.length();

                String tel = String.valueOf(etTel.getText());
                tel.substring(0,1);
                Log.v("하이픈", "after" + String.valueOf(afterLenght));

                if (beforeLenght < afterLenght) {// 타자를 입력 중이면
                    if (s.toString().indexOf("01") < 0 && afterLenght == 2) { //subSequence로 지정된 문자열을 반환해서 "-"폰을 붙여주고 substring
                        etTel.setText(s.toString().subSequence(0, 2) + "-" + s.toString().substring(2, s.length()));

                    } else if (s.toString().indexOf("01") < 0 && afterLenght == 6) {
                        etTel.setText(s.toString().subSequence(0, 6) + "-" + s.toString().substring(6, s.length()));

                    } else {
                        if (afterLenght == 4 && s.toString().indexOf("-") < 0) { //subSequence로 지정된 문자열을 반환해서 "-"폰을 붙여주고 substring
                            etTel.setText(s.toString().subSequence(0, 3) + "-" + s.toString().substring(3, s.length()));

                        } else if (s.toString().indexOf("02") < 0 && afterLenght == 9) {
                            etTel.setText(s.toString().subSequence(0, 8) + "-" + s.toString().substring(8, s.length()));

                        }
                    }
                }
                etTel.setSelection(etTel.length());

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 생략
            }

        });//자동으로 전화번호 누르기 끝

    }

    //1번 프레임 클릭액션
    View.OnClickListener firstFrameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 인증 버
                case R.id.join_btn_authentication:

                    getTextCheck = etTel.getText().toString();
                    if(getTextCheck.getBytes().length<=0){

                        new AlertDialog.Builder(JoinActivity.this)
                                .setTitle("전화번호를 입력해 주세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        etTel.requestFocus();
                        break;
                    }

                    if(!Pattern.matches(patternTel,etTel.getText().toString())){
                        new AlertDialog.Builder(JoinActivity.this)
                                .setTitle("전화번호를 확인해 주세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        etTel.requestFocus();
                        break;
                    }

                    if(userTelCount()==1){
                        new AlertDialog.Builder(JoinActivity.this)
                                .setTitle("이미 가입된 전화번호 입니다.")
                                .setPositiveButton("확인",null)
                                .show();
                        break;
                    }
                    // 메신저 보내기 권한 체크
                    if(ContextCompat.checkSelfPermission(JoinActivity.this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(JoinActivity.this,Manifest.permission.SEND_SMS)){

                            AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                            builder.setTitle("info");
                            builder.setMessage("This app won't work properly unless you grant SMS permission.");
                            builder.setIcon(R.drawable.ic_action_before);

                            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(JoinActivity.this,new String[] {Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }else {
                            ActivityCompat.requestPermissions(JoinActivity.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);
                        }
                    }


                    //sms 받는 권한 체
                    int permissonCheck= ContextCompat.checkSelfPermission(JoinActivity.this, Manifest.permission.RECEIVE_SMS);
                    if(permissonCheck == PackageManager.PERMISSION_GRANTED){
                        if(timerCount != 0) countDownTimer.cancel();
                        //수신권한 있을때
                        sendmsg();
                        countDownTimer();
                        timerCount++;
                        btnAuthentication.setText("재요청");
                        new AlertDialog.Builder(JoinActivity.this)
                                .setTitle("인증번호가 발송 되었습니다.")
                                .setMessage("제한시간 내에 인증번호를 입력해 주세요.")
                                .setPositiveButton("확인",null)
                                .show();
                    }else{// 수신권한 없을때
                        //권한설정 dialog에서 거부를 누르면
                        //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
                        //단, 사용자가 "Don't ask again"을 체크한 경우
                        //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
                        if(ActivityCompat.shouldShowRequestPermissionRationale(JoinActivity.this, Manifest.permission.RECEIVE_SMS)){
                            //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                            Toast.makeText(getApplicationContext(), "SMS권한이 필요합니다", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(JoinActivity.this, new String[]{ Manifest.permission.RECEIVE_SMS},       SMS_RECEIVE_PERMISSON);
                        }else{
                            ActivityCompat.requestPermissions(JoinActivity.this, new String[]{ Manifest.permission.RECEIVE_SMS}, SMS_RECEIVE_PERMISSON);
                        }
                    }
                    break; // 인증버튼 끝

                    // 1번프레임 계속하기버튼
                    case R.id.join_btn_go:

                        switch (timeout) {
                            case 0:
                            if (passPhone.equals(etAuthentication.getText().toString())) {
                                countDownTimer.cancel();
                                Toast.makeText(JoinActivity.this, "성공", Toast.LENGTH_SHORT).show();
                                new AlertDialog.Builder(JoinActivity.this)
                                        .setTitle("인증 되었습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ly1.setVisibility(View.INVISIBLE);
                                                ly2.setVisibility(View.VISIBLE);
                                                if(userEmail!=null&&userNickName!=null) {
                                                    etEmail.setText(userEmail);
                                                    etNickName.setText(userNickName);
                                                }
                                            }
                                        })
                                        .show();
                            }
                            else {
                                Toast.makeText(JoinActivity.this, "실패", Toast.LENGTH_SHORT).show();
                            }
                            break;
                            default:
                                new AlertDialog.Builder(JoinActivity.this)
                                        .setTitle("제한시간이 지났습니다..")
                                        .setMessage("인증번호 재요청을 눌러주세요.")
                                        .setPositiveButton("확인",null)
                                        .show();
                                break;
                        }// ----- timeout switch



                    }


            }

    };


    //2번 프레임 클릭액션
    View.OnClickListener secondFrameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 2번프레임 프로필플러스 버튼
                case R.id.join_img_profilePlus:
                    // 이미지 넣는 버튼 눌렀을 때
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                    //카메라, 갤러리를 진행시킴

                    break;
                    // 2번프레임 계속하기버튼
                case R.id.join_btn_gogo:
                    // email null check
                    getTextCheck = etEmail.getText().toString();
                    if(getTextCheck.getBytes().length<=0){
                        new AlertDialog.Builder(JoinActivity.this)
                                .setTitle("이메일을 입력해 주세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        etEmail.requestFocus();
                        break;
                    }
                    //nickname null check
                    getTextCheck = etNickName.getText().toString();
                    if(getTextCheck.getBytes().length<=0){
                        new AlertDialog.Builder(JoinActivity.this)
                                .setTitle("닉네임을 입력해 주세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        etNickName.requestFocus();
                        break;
                    }

                    if(!Pattern.matches(patternEmail,etEmail.getText().toString())){
                        new AlertDialog.Builder(JoinActivity.this)
                                .setTitle("이메일형식을 확인해 주세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        etEmail.requestFocus();
                        break;
                    }

//                    Log.v("제발",""+Pattern.matches(patternNickName,etNickName.getText().toString()));
//                    if(!Pattern.matches(patternNickName,etNickName.getText().toString())){
//                        new AlertDialog.Builder(JoinActivity.this)
//                                .setTitle("한글,영어,숫자만 입력 가능합니다.")
//                                .setPositiveButton("확인",null)
//                                .show();
//                        etNickName.requestFocus();
//                        break;
//                    }
                    //-------------------------- 정규식 종료 --------------------------크


                    // 이메일 중복체크
                    if(userEmailCheck()==1){
                        new AlertDialog.Builder(JoinActivity.this)
                                .setTitle("이미가입된 이메일 입니다.")
                                .setPositiveButton("확인",null)
                                .show();
                        etEmail.requestFocus();
                        break;
                    }



                    // 페이 페스워드로 이동
                    connectImage();
                    Intent intent2 = new Intent(JoinActivity.this,JoinPayPasswordActivity.class);
                    intent2.putExtra("userTel",etTel.getText().toString());
                    intent2.putExtra("userProfile",imgName);
                    intent2.putExtra("userEmail",etEmail.getText().toString());
                    intent2.putExtra("userNickName",etNickName.getText().toString());
                    startActivity(intent2);



            }

        }
    };


    // 인텐트가 새로 들어왓을때 실행되는 매소드
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //인증번호 setText
        processCommand(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                //이미지의 URI를 얻어 경로값으로 반환.
                img_path = getImagePathToUri(data.getData());
                Log.v("이미지", "image path :" + img_path);
                Log.v("이미지", "Data :" +String.valueOf(data.getData()));

                //이미지를 비트맵형식으로 반환
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                Bitmap image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 400, true);
                imgProfile.setImageBitmap(image_bitmap_copy);

                // 파일 이름 및 경로 바꾸기(임시 저장, 경로는 임의로 지정 가능)
                String date = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
                String imageName = date + "." + f_ext;
                tempSelectFile = new File(devicePath , imageName);
                OutputStream out = new FileOutputStream(tempSelectFile);
                image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                // 임시 파일 경로로 위의 img_path 재정의
                img_path = devicePath + imageName;
                Log.v("이미지","fileName :" + img_path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //인증번호 setText
    private void processCommand(Intent intent){
        if(intent != null){
            String sender = intent.getStringExtra("sender");
            String date = intent.getStringExtra("receivedDate");
            String content = intent.getStringExtra("contents");
            Log.v("번호확인",content.substring(0,3));
            etAuthentication.setText(content.substring(6,14));
            etAuthentication.requestFocus();
        }
    }

    //카운트 다운 메소드
    public void countDownTimer() {

        time_counter = findViewById(R.id.join_tv_timer);
        //줄어드는 시간을 나타내는 TextView

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }
                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.
            }

            @Override
            public void onFinish() { //시간이 다 되면 인증번호 초기화
                timeout++;
                passPhone="";
                new AlertDialog.Builder(JoinActivity.this)
                        .setTitle("제한시간이 지났습니다.")
                        .setMessage("인증번호 재요청을 눌러주세요.")
                        .setPositiveButton("확인",null)
                        .show();
            }

        }.start();
    }

    public void sendmsg(){
         //문자 인증코드 생성
            String[] str = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
            String newCode = new String();

            for (int x = 0; x < 8; x++) {
                int random = (int) (Math.random() * str.length);
                newCode += str[random];
            }
        passPhone=newCode;

        String phoneNo = "5554";
        String sms = "인증번호는 " +passPhone+" 입니다." ;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
            Log.v("여기","메세지 전송 완료.");
        } catch (Exception e) {
            Log.v("여기","메세지 에러 : "+e);

            e.printStackTrace();
        }
    }

    // 전화번호 중복체크
    private int userTelCount(){
        int utc= 0;
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/userTelSelect.jsp?usertel="+ etTel.getText().toString();
            Log.v("urlAddr",""+urlAddr);
            CJS_NetworkTask cjs_networkTask = new CJS_NetworkTask(JoinActivity.this, urlAddr, "userTelCount");
            Object obj = cjs_networkTask.execute().get();

            utc= (int) obj;
        }catch (Exception e){

        }
        return utc;
    }


    // 이메일 중복체크크
   private int userEmailCheck(){
        int utc= 0;
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/userEmailSelect.jsp?uEmail="+ etEmail.getText().toString();
            Log.v("여기","urlAddr : "+urlAddr);
            CJS_NetworkTask cjs_networkTask = new CJS_NetworkTask(JoinActivity.this, urlAddr, "userEmailCount");
            Object obj = cjs_networkTask.execute().get();

            utc= (int) obj;
            Log.v("여기","userEmailCheck : "+utc);
        }catch (Exception e){

        }
        return utc;
    }

    //이미지
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


    private void connectImage(){
        imageurl = "http://" + MacIP + ":8080/tify/multipartRequest.jsp";
        ImageNetworkTask_TaeHyun imageNetworkTask = new ImageNetworkTask_TaeHyun(JoinActivity.this,imgProfile,img_path,imageurl);
        try {
            Integer result = imageNetworkTask.execute(100).get();

            switch (result){
                case 1:
                    Toast.makeText(JoinActivity.this, "이미지서버 저장 성공 !", Toast.LENGTH_SHORT).show();

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    //
                    //              Device에 생성한 임시 파일 삭제
                    //
                    //////////////////////////////////////////////////////////////////////////////////////////////
                    File file = new File(img_path);
                    file.delete();
                    break;
                case 0:
                    Toast.makeText(JoinActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    break;
            }
            //////////////////////////////////////////////////////////////////////////////////////////////
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}//--------------------------------