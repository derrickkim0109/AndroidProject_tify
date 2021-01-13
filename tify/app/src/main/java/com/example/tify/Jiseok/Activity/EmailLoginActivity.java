package com.example.tify.Jiseok.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tify.Jiseok.Bean.Bean_Login_cjs;
import com.example.tify.Jiseok.NetworkTask.CJS_NetworkTask;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.util.regex.Pattern;

public class EmailLoginActivity extends AppCompatActivity {
    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();
    EditText userEmail,userPwd;
    Button btnLogin;
    TextView tvJoin;
    String patternEmail="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Bean_Login_cjs bean_login_cjs = new Bean_Login_cjs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjs_activity_email_login);
        ImageView giflogin = findViewById(R.id.giflogin);
        Glide.with(this).load(R.drawable.login_image04).into(giflogin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //액션바 삭제

        userEmail = findViewById(R.id.email_join_et_userEmail);
        userPwd = findViewById(R.id.email_join_et_userPwd);
        btnLogin = findViewById(R.id.email_join_btn_login);
        tvJoin = findViewById(R.id.email_join_ev_join);


        btnLogin.setOnClickListener(ClickListener);
        tvJoin.setOnClickListener(ClickListener);


    }

    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 로그인
                case R.id.email_join_btn_login:
                    String uEmail = userEmail.getText().toString();
                    String uPwd = userPwd.getText().toString();
                    if(uEmail.getBytes().length<=0){
                        new AlertDialog.Builder(EmailLoginActivity.this)
                                .setTitle("이메일을 입력해 주세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        userEmail.requestFocus();
                        break;

                    }
                    if(uPwd.getBytes().length<=0){
                        new AlertDialog.Builder(EmailLoginActivity.this)
                                .setTitle("비밀번호를 입력해 주세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        userPwd.requestFocus();
                        break;
                    }
                    if(!Pattern.matches(patternEmail,userEmail.getText().toString())){
                        new AlertDialog.Builder(EmailLoginActivity.this)
                                .setTitle("이메일형식을 확인해 주세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        userEmail.requestFocus();
                        break;
                    }
                    //로그인 액션
                    switch (emailLogin(uEmail,uPwd).getCount()){
                        case 1:
                            Toast.makeText(EmailLoginActivity.this,"로그인성공",Toast.LENGTH_SHORT).show();
                            //자동로그인
                            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor autoLogin = auto.edit();
                            autoLogin.putString("userEmail",bean_login_cjs.getuEmail() );
                            autoLogin.putInt("userSeq", bean_login_cjs.getuNo());
                            autoLogin.putString("userNickName", bean_login_cjs.getuNickName());
                            autoLogin.commit();
                            startActivity(new Intent(EmailLoginActivity.this,JiseokMainActivity.class));
                            break;
                        default:
                            Toast.makeText(EmailLoginActivity.this,"로그인실패",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    break;
                    //회원가입
                case R.id.email_join_ev_join:
                    startActivity(new Intent(EmailLoginActivity.this,JoinActivity.class));
                    break;
            }
        }
    };

    private Bean_Login_cjs emailLogin(String email,String pwd){
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/emailLogin.jsp?uEmail=" + email + "&uPayPassword=" + pwd;
            CJS_NetworkTask cjs_networkTask = new CJS_NetworkTask(EmailLoginActivity.this, urlAddr, "emailLogin");
            Object obj = cjs_networkTask.execute().get();

            bean_login_cjs = (Bean_Login_cjs) obj;
            Log.v("emailLogin","getCount"+bean_login_cjs.getCount());
            Log.v("emailLogin","getuNo"+bean_login_cjs.getuNo());
            Log.v("emailLogin","getuEmail"+bean_login_cjs.getuEmail());
            Log.v("emailLogin","getuNickName"+bean_login_cjs.getuNickName());

        }catch (Exception e){

        }


        return bean_login_cjs;
    }
}