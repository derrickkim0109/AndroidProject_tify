package com.android.tify_store.Minwoo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.tify_store.Minwoo.Bean.Login;
import com.android.tify_store.Minwoo.NetworkTask.LMW_LoginNetworkTask;
import com.android.tify_store.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    // 로그인 화면

    String TAG = "LoginActivity";
    EditText et_Id;
    EditText et_Pw;
    Button loginBtn;

    ArrayList<Login> logins;
    ArrayList<Login> datas;

    // DB Connect
    String macIP;
    String urlAddr = null;
    String where = null;

    String id;
    String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_login);

        macIP = "172.30.1.20"; // IP설정

        // layout 설정
        et_Id = findViewById(R.id.ip);
        et_Pw = findViewById(R.id.pw);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 서버 접속 (id, pw는 점주가 요청한 값을 직접 DB에 입력한다.) => DB로 관리
                id = et_Id.getText().toString();
                pw = et_Pw.getText().toString();

                if(id.length() == 0 || pw.length() == 0){ // 둘중 하나라도 입력하지 않았을 경우
                    Toast.makeText(LoginActivity.this, "ID와 PW를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "입력값 없음");

                }else{ // 모두 입력했을 경우
                    Log.v(TAG, "입력값 있음");

                    datas = new ArrayList<Login>();
                    datas = connectGetData(); // 입력한 정보에 맞는 데이터가 존재하는지 확인

                    if(datas.get(0).getCnt() == 0){ // 존재하지 않을 경우
                        Toast.makeText(LoginActivity.this, "로그인 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    }else{ // 존재할 경우

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        intent.putExtra("macIP", macIP);
                        intent.putExtra("skSeqNo", datas.get(0).getSkSeqNo());
                        intent.putExtra("skStatus", datas.get(0).getSkStatus());
                        Log.v(TAG, "skSeqNo : " + datas.get(0).getSkSeqNo());
                        Log.v(TAG, "skStatus : " + datas.get(0).getSkStatus());

                        startActivity(intent);
                    }
                }



            }
        });

    }

    private ArrayList<Login> connectGetData(){
        ArrayList<Login> beanList = new ArrayList<Login>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_storekeeper_select.jsp?skId=" + id + "&skPw=" + pw;

        try {
            LMW_LoginNetworkTask networkTask = new LMW_LoginNetworkTask(LoginActivity.this, urlAddr, where);

            Object obj = networkTask.execute().get();
            logins = (ArrayList<Login>) obj;
            Log.v(TAG, "data.size() : " + logins.size());

            beanList = logins;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }


}