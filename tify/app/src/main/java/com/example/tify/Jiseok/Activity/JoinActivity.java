package com.example.tify.Jiseok.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tify.R;

import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {
    LinearLayout ly1,ly2;
    EditText etTel, etAuthentication, etEmail, etNickName;
    Button btnAuthentication, btnGo, btnGoGo;
    ImageView imgProfile, imgProfilePlus;
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
    String patternEmail="/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;";
    String patternNickName="/^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\\*]+$/";


    String getTextCheck = null; // editText null 체크용 변수



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjs_activity_join);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);
            }
        }

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


        btnAuthentication.setOnClickListener(firstFrameClickListener);
        btnGo.setOnClickListener(firstFrameClickListener);

        imgProfilePlus.setOnClickListener(secondFrameClickListener);
        btnGoGo.setOnClickListener(secondFrameClickListener);


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


                    //권한이 부여되어 있는지 확인
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
                    break;

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
                    if(!Pattern.matches(patternNickName,etNickName.getText().toString())){
                        new AlertDialog.Builder(JoinActivity.this)
                                .setTitle("한글,영어,숫자만 입력 가능합니다.")
                                .setPositiveButton("확인",null)
                                .show();
                        etNickName.requestFocus();
                        break;
                    }
                    //-------------------------- 정규식 종료 --------------------------




            }

        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processCommand(intent);
    }

    private void processCommand(Intent intent){
        if(intent != null){
            String sender = intent.getStringExtra("sender");
            String date = intent.getStringExtra("receivedDate");
            String content = intent.getStringExtra("contents");
            Log.v("번호확인",content.substring(0,3));
            etAuthentication.setText(content.substring(6,14));

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



}//--------------------------------