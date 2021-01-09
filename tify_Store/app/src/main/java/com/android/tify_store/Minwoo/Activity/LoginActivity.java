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

    int result = -1;
    String strResult = null;

    String id;
    String pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_login);

        macIP = "172.30.1.27";

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
                    datas = connectGetData(); // 로그인 정보가 올바른지 확인

                    if(datas.get(0).getCnt() == 0){
                        Toast.makeText(LoginActivity.this, "로그인 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        strResult = connectUpdateData();
                        Log.v(TAG, "strResult : " + strResult);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        intent.putExtra("macIP", "172.30.1.27");
                        intent.putExtra("storekeeper_skSeqNo", datas.get(0).getSkSeqNo());

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
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_LoginNetworkTask networkTask = new LMW_LoginNetworkTask(LoginActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            logins = (ArrayList<Login>) obj;
            Log.v(TAG, "data.size() : " + logins.size());

            beanList = logins;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    private String connectUpdateData(){ // 로그인 성공하면 skStatus 1로 바꾸기
        String result = null;

        urlAddr = "http://" + macIP + ":8080/tify/lmw_storekeeper_update.jsp?skSeqNo=" + 1;
        where = "update";

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where <- "insert"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_LoginNetworkTask networkTask = new LMW_LoginNetworkTask(LoginActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            Object obj = networkTask.execute().get();
            result = (String) obj;
            ///////////////////////////////////////////////////////////////////////////////////////

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}