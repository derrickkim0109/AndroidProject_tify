package com.example.tify.Jiseok.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tify.Jiseok.NetworkTask.CJS_NetworkTask;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.util.Random;

public class JoinPayPasswordActivity extends AppCompatActivity {
    String TAG = "여기, JoinPayPasswordActivity";
    Context context ;
    ShareVar shareVar =new ShareVar();
    String MacIP= shareVar.getMacIP();

    Button btnRearrangement;
    ImageButton btnDelete;
    Button [] btn = new Button[10];
    String result = "0"; // null 값에서 길이 체크하면 터짐
    String userpw=null;
    int count=0;//비밀번호 틀린 횟수
//    String passCheck="";// 결제비밀번호 설정이 돼있는지 체크
    ImageView[] img = new ImageView[6];
    Random random = new Random();
    String [] btnNum = new String[10];//랜덤 10개수를 받아서 버튼에 넣음

    String payPassword1=null;
    String payPassword2=null;// 비밀번호 확인
    int payCheck=0; // 0일때 payPassword1, 1일때 payPassword2

    TextView tvComment,tvCount,tvTitle;

    String userTel;
    String userEmail;
    String userProfile;
    String userNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.cjs_activity_join_pay_password);

        Intent intent = getIntent();
        userTel = intent.getStringExtra("userTel");
        userEmail = intent.getStringExtra("userEmail");
        userNickName = intent.getStringExtra("userNickName");
        userProfile = intent.getStringExtra("userProfile");

        Log.v(TAG,"userTel : "+userTel);
        Log.v(TAG,"userEmail : "+userEmail);
        Log.v(TAG,"userNickName : "+userNickName);
        Log.v(TAG,"userProfile : "+userProfile);

        btn[0]=findViewById(R.id.join_pay_btn0);
        btn[1]=findViewById(R.id.join_pay_btn1);
        btn[2]=findViewById(R.id.join_pay_btn2);
        btn[3]=findViewById(R.id.join_pay_btn3);
        btn[4]=findViewById(R.id.join_pay_btn4);
        btn[5]=findViewById(R.id.join_pay_btn5);
        btn[6]=findViewById(R.id.join_pay_btn6);
        btn[7]=findViewById(R.id.join_pay_btn7);
        btn[8]=findViewById(R.id.join_pay_btn8);
        btn[9]=findViewById(R.id.join_pay_btn9);
        btnRearrangement=findViewById(R.id.join_pay_btn_rearrangement);
        btnDelete=findViewById(R.id.join_pay_btn_delete);

        img[0]=findViewById(R.id.join_pay_img_dot1);
        img[1]=findViewById(R.id.join_pay_img_dot2);
        img[2]=findViewById(R.id.join_pay_img_dot3);
        img[3]=findViewById(R.id.join_pay_img_dot4);
        img[4]=findViewById(R.id.join_pay_img_dot5);
        img[5]=findViewById(R.id.join_pay_img_dot6);

        tvComment = findViewById(R.id.join_pay_tv_countComment);
        tvCount = findViewById(R.id.join_pay_tv_count);
        tvTitle = findViewById(R.id.join_pay_tv_title);
        rearrangement(); // 키패드 배열
//        userpw=password();//비밀번호 받아옴

        for(int i=0;i<10;i++){
            btn[i].setOnClickListener(numClickListener);
        }
        btnRearrangement.setOnClickListener(numClickListener);
        btnDelete.setOnClickListener(deleteClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            // 뒤돌아오기 버튼눌렀을때 다시못돌아오게
            case 1:
                new AlertDialog.Builder(JoinPayPasswordActivity.this)
                        .setTitle("잘못된 접근입니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(JoinPayPasswordActivity.this,JiseokMainActivity.class),1);
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }


    View.OnClickListener numClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 숫자버튼 클
            if(result.length()<7) {
                switch (v.getId()) {
                    case R.id.join_pay_btn0:
                        result += btn[0].getText().toString();
                        break;
                    case R.id.join_pay_btn1:
                        result += btn[1].getText().toString();
                        break;
                    case R.id.join_pay_btn2:
                        result += btn[2].getText().toString();
                        break;
                    case R.id.join_pay_btn3:
                        result += btn[3].getText().toString();
                        break;
                    case R.id.join_pay_btn4:
                        result += btn[4].getText().toString();
                        break;
                    case R.id.join_pay_btn5:
                        result += btn[5].getText().toString();
                        break;
                    case R.id.join_pay_btn6:
                        result += btn[6].getText().toString();
                        break;
                    case R.id.join_pay_btn7:
                        result += btn[7].getText().toString();
                        break;
                    case R.id.join_pay_btn8:
                        result += btn[8].getText().toString();
                        break;
                    case R.id.join_pay_btn9:
                        result += btn[9].getText().toString();
                        break;
                    case R.id.join_pay_btn_rearrangement:
                        rearrangement();
                        break;
                    default:
                        break;
                }
            }
            // 버튼 누르고 난후 이미지 색
            resultToStar();

            switch (result.length()){
                case 7:
                    switch (payCheck){
                        // 첫번째 비밀번호 입력
                        case 0:
                            rearrangement();
                            payPassword1=result.substring(1,7);
                            result="0";
                            payCheck=1;
                            Log.v(TAG,"payPassword1 : "+payPassword1+", result : "+result);
                            resultToStar();
                            tvTitle.setText("다시한번\n입력해주세요.");
                            break;
                            // 확인 비밀번호 입력
                        case 1:
                            payPassword2=result.substring(1,7);
                            Log.v(TAG,"payPassword2 : "+payPassword2+", result : "+result);
                            // 확인비밀번호가 일치할때
                            if(payPassword1.equals(payPassword2)){


                                //디비에 회원정보 저장
                                insertUserInfo();
                                String userseq = Integer.toString(selectUserSeq());
                                insertRewardTable(userseq);

                               //자동로그인
                                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.putString("userEmail", userEmail);
                                autoLogin.putInt("userSeq", Integer.parseInt(userseq));
                                autoLogin.putString("userNickName", userNickName);
                                autoLogin.commit();

                                new AlertDialog.Builder(JoinPayPasswordActivity.this)
                                        .setTitle("회원가입이 완료되었습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(JoinPayPasswordActivity.this,JiseokMainActivity.class);
                                                startActivityForResult(intent,1);
                                            }
                                        })
                                        .show();

                            }else{

                                rearrangement();
                                count++;
                                result="0";
                                resultToStar();
                                tvComment.setVisibility(View.VISIBLE);
                                tvCount.setVisibility(View.VISIBLE);
                                tvCount.setText("틀린횟수 ("+count+"/5)");
                                Log.v(TAG,"틀린횟수 : "+count);
                                // 5번틀리면 초기화
                                switch (count){
                                    case 5:

                                        new AlertDialog.Builder(JoinPayPasswordActivity.this)
                                                .setTitle("비밀번호를 다시 입력해주세요.")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        tvComment.setVisibility(View.INVISIBLE);
                                                        tvCount.setVisibility(View.INVISIBLE);
                                                        tvTitle.setText("결제 비밀번호를\n입력해주세요");
                                                        rearrangement();
                                                        payCheck=0;
                                                        count=0;
                                                        result="0";
                                                        resultToStar();
                                                    }
                                                })
                                                .show();
                                        break;
                                }
                            }
                            break;
                    }
                    break;
                default:
                    break;

            }

        }
    };




    // 지우기버튼
    View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (result.length() == 1) {
            } else result = result.substring(0, result.length() - 1);

            resultToStar();
        }
    };

    @SuppressLint("ResourceType")
    private void resultToStar(){
        // 색 없는 별로 전체 초기화
        for(int i=0;i<6;i++){
            img[i].setImageResource(R.raw.dot02);
        }
        // 비밀번호 길이만큼 별 색칠
        for (int i = 0; i < result.length() - 1; i++) {
            img[i].setImageResource(R.raw.dot01);
        }
    }

    // 재배열
    private void rearrangement(){
        rand();
        setBtn();
    }


//    //디비에서 받은 비밀번호
//    private String password(){
//        String pass = "123456";
//        return pass;
//    }

    //0~9 중복없이 뽑기
    public void rand(){
        String [] str = new String[10];
        for(int i=0;i<10;i++){
            str[i]= Integer.toString(random.nextInt(10));
            for(int j=0;j<i;j++){
                if(str[i].equals(str[j])){
                    i --;
                    break;
                }
            }
        }
        btnNum=str;
    }

    //버튼에 숫자넣기
    private void setBtn(){
        for(int i=0;i<10;i++){
            btn[i].setText(btnNum[i]);
        }
    }


    //회원가입
    private void insertUserInfo(){
       try {
           if(userProfile==null){
                userProfile="ic_person.png";
           }
           String urlAddr = "http://" + MacIP + ":8080/tify/insertUserInfo.jsp?uEmail=" + userEmail + "&uNickName=" + userNickName + "&uTelNo=" + userTel + "&uImage=" + userProfile + "&uPayPassword=" + payPassword2;
           Log.v("여기", "insertUserInfo : " + urlAddr);
           CJS_NetworkTask cjs_networkTask = new CJS_NetworkTask(JoinPayPasswordActivity.this, urlAddr, "insertUserInfo");
           cjs_networkTask.execute().get();
       }catch (Exception e){

       }

    }

    //이메일 중복체
    private int selectUserSeq(){
        int utc= 0;
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/userSeqSelect.jsp?uEmail="+ userEmail;

            CJS_NetworkTask cjs_networkTask = new CJS_NetworkTask(JoinPayPasswordActivity.this, urlAddr, "uNoSelect");
            Object obj = cjs_networkTask.execute().get();

            utc= (int) obj;
        }catch (Exception e){

        }
        return utc;
    }



    // 리워드테이블 셋팅
    private void insertRewardTable(String seq){
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/insertReward.jsp?uNo="+seq;
            CJS_NetworkTask cjs_networkTask = new CJS_NetworkTask(JoinPayPasswordActivity.this, urlAddr, "insertReward");
            cjs_networkTask.execute().get();

        }catch (Exception e){

        }

    }


}