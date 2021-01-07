package com.example.tify.Jiseok.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tify.R;

public class JoinActivity extends AppCompatActivity {
    EditText etTel, etAuthentication, etEmail, etNickName;
    Button btnAuthentication, btnGo, btnGoGo;
    ImageView imgProfile, imgProfilePlus;
    TextView time_counter;
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)
    static final int SMS_RECEIVE_PERMISSON = 1;
    TelephonyManager telManager;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjs_activity_join);
        telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        Log.v("여기","번호 : "+telManager.getLine1Number());
        // 1번 프레임
        etTel = findViewById(R.id.join_et_tel);
        btnAuthentication = findViewById(R.id.join_btn_authentication);
        etAuthentication = findViewById(R.id.join_et_authenticationNum);
        btnGo = findViewById(R.id.join_btn_go);
        // 2번 프레임
        imgProfile = findViewById(R.id.join_img_profile);
        imgProfilePlus = findViewById(R.id.join_img_profilePlus);
        etEmail = findViewById(R.id.join_et_email);
        etNickName = findViewById(R.id.join_et_nickName);
        btnGoGo = findViewById(R.id.join_btn_gogo);



        btnAuthentication.setOnClickListener(firstFrameClickListener);
        btnGo.setOnClickListener(firstFrameClickListener);

        imgProfilePlus.setOnClickListener(secondFrameClickListener);
        btnGoGo.setOnClickListener(secondFrameClickListener);

    }

    //1번 프레임 클릭액션
    View.OnClickListener firstFrameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.join_btn_authentication:

                    //권한이 부여되어 있는지 확인

                    int permissonCheck= ContextCompat.checkSelfPermission(JoinActivity.this, Manifest.permission.RECEIVE_SMS);
                    if(permissonCheck == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(getApplicationContext(), "SMS 수신권한 있음", Toast.LENGTH_SHORT).show();
                        sendmsg2();
                        countDownTimer();
                    }else{
                        Toast.makeText(getApplicationContext(), "SMS 수신권한 없음", Toast.LENGTH_SHORT).show();

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
            }
        }
    };


    //2번 프레임 클릭액션
    View.OnClickListener secondFrameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


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
            public void onFinish() { //시간이 다 되면 다이얼로그 종료
                Toast.makeText(JoinActivity.this,"시간종료",Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    public void sendmsg(){
        String phoneNo = "(065) 555-1212";
        String sms = "폰에서 자동으로 보내는 메시지 입니다.";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
            Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.v("여기","에러 : "+e);
            Toast.makeText(getApplicationContext(), "전송 오류!"+e, Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
    }

    public void sendmsg2(){
        String phoneNo = "(065) 555-1212";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms : "+phoneNo));
        intent.putExtra("sms_body","문자");
        startActivity(intent);
    }

}