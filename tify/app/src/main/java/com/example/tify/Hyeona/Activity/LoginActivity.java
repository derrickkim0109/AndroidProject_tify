package com.example.tify.Hyeona.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tify.Jiseok.Activity.EmailLoginActivity;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Jiseok.Activity.JoinActivity;
import com.example.tify.R;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

public class LoginActivity extends AppCompatActivity {
    String userEmail;
    int userSeq;
    TextView emailJoin;
    Button btnEmailLogin;
    private SessionCallback sessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sessionCallback = new SessionCallback(); // SessionCallback 초기화
        Session.getCurrentSession().addCallback(sessionCallback); // 현재 세션에 콜백 붙임
        //Session.getCurrentSession().checkAndImplicitOpen(); // 자동로그인

        //자동로그인
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        userEmail = auto.getString("userEmail",null);
        userSeq = auto.getInt("userSeq",0);
        if(userEmail !=null && userSeq!= 0){
            startActivity(new Intent(LoginActivity.this, JiseokMainActivity.class));
        }
        //자동로그인 끝

        emailJoin = findViewById(R.id.login_tv_emailJoin);
        btnEmailLogin = findViewById(R.id.login_btn_emailLogin);
        emailJoin.setOnClickListener(onClickListener);
        btnEmailLogin.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_tv_emailJoin:
                    Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                    startActivity(intent);
                    break;
                case R.id.login_btn_emailLogin:
                    startActivity(new Intent(LoginActivity.this, EmailLoginActivity.class));
                    break;

            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) { // 카카오로그인 액티비티에서 넘어온 경우일 때 실행
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback); // 현재 액티비티 제거 시 콜백도 같이 제거
    }

    private class SessionCallback implements ISessionCallback {


        @Override
        public void onSessionOpened() {
            //로그인 세션이 열렸을 때.
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    // 로그인에 실패했을 때. 인터넷 연결이 불안정한 경우도 해당
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    // 로그인 도중 세션이 비정상적인 이유로 닫혔을 때.
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    //로그인 성공
                    Intent intentk = new Intent(getApplicationContext(), JoinActivity.class);
                    intentk.putExtra("userEmail", result.getKakaoAccount().getEmail().toString());
                    intentk.putExtra("userNickName", result.getNickname().toString());
                    if(result.getKakaoAccount().getPhoneNumber()!=null) intentk.putExtra("userTel", result.getKakaoAccount().getPhoneNumber().toString());
                    startActivity(intentk);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            //로그인 세션이 정상적으로 열리지 않았을 때.
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
            Log.v("여기",""+e.toString());

        }
    }
}