package com.example.tify.Taehyun.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Jiseok.Activity.JoinPayPasswordActivity;
import com.example.tify.Jiseok.NetworkTask.CJS_NetworkTask;
import com.example.tify.Jiseok.NetworkTask.CJS_NetworkTask_Mypage;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.util.Random;

public class Mypage_PayPasswordActivity extends AppCompatActivity {
    ////////////////////////////////////////////////////////////
    //                                                        //
    //              //   2021.01.07 -태현
    //              결제 비밀번호 변경 구간
    ////////////////////////////////////////////////////////////

    ShareVar shareVar =new ShareVar();
    String MacIP= shareVar.getMacIP();

    String userEmail;
    int userSeq;
    String userNickName;
    String myLocation;

        //field
    String TAG = "Mypage_PayPasswordActivity";
    Button btnRearrangement;
    ImageButton btnDelete;
    Button [] btn = new Button[10];
    String result = "0"; // null 값에서 길이 체크하면 터짐
    String userpw=null;
    int count=0;//비밀번호 틀린 횟수
        //String passCheck="";// 결제비밀번호 설정이 돼있는지 체크
    ImageView[] img = new ImageView[6];
    Random random = new Random();
    String [] btnNum = new String[10];//랜덤 10개수를 받아서 버튼에 넣음

    String payPassword1=null;
    String payPassword2=null;// 비밀번호 확인
    String payPassword3=null;// 비밀번호 확인
    int payCheck=0; // 0일때 payPassword1, 1일때 payPassword2
    int payCheck2=0; // 0일때 payPassword1, 1일때 payPassword2, 2일때 payPassword3

    String uTelNo;
    String uImage;

    TextView tvComment,tvCount,tvTitle;

    Intent intent = null;
    //DB - intent로 받아오는값
    String uPayPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_mypage_pay_password);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        userEmail = auto.getString("userEmail", null);
        userSeq = auto.getInt("userSeq", 0);
        userNickName = auto.getString("userNickName", null);
        myLocation = auto.getString("myLocation", null);

        init();
        intent = getIntent();
        inheritance();

        if(selectPwd()=="null"){
            tvTitle.setText("결제비밀번호를\n입력해 주세요.");
        }




        Intent intent = getIntent();
        uTelNo=intent.getStringExtra("uTelNo");
        uImage=intent.getStringExtra("uImage");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2:
                new AlertDialog.Builder(Mypage_PayPasswordActivity.this)
                        .setTitle("잘못된 접근입니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                intent = new Intent(Mypage_PayPasswordActivity.this, Mypage_ProfileChageActivity.class)
                                        .putExtra("uTelNo", uTelNo)
                                        .putExtra("uImage", uImage)
                                        .putExtra("MacIP", MacIP)
                                        .putExtra("uNo", userSeq)
                                        .putExtra("uNickName", userNickName);

                                startActivityForResult(intent,2);
                                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            }
                        })
                        .show();
                break;
        }
    }

    private void inheritance() {
        uPayPassword = intent.getStringExtra("uPayPassword");
    }

    private void init() {
        btn[0]=findViewById(R.id.mypage_pay_btn0);
        btn[1]=findViewById(R.id.mypage_pay_btn1);
        btn[2]=findViewById(R.id.mypage_pay_btn2);
        btn[3]=findViewById(R.id.mypage_pay_btn3);
        btn[4]=findViewById(R.id.mypage_pay_btn4);
        btn[5]=findViewById(R.id.mypage_pay_btn5);
        btn[6]=findViewById(R.id.mypage_pay_btn6);
        btn[7]=findViewById(R.id.mypage_pay_btn7);
        btn[8]=findViewById(R.id.mypage_pay_btn8);
        btn[9]=findViewById(R.id.mypage_pay_btn9);
        btnRearrangement=findViewById(R.id.mypage_pay_btn_rearrangement);
        btnDelete=findViewById(R.id.mypage_pay_btn_delete);

        img[0]=findViewById(R.id.mypage_pay_img_dot1);
        img[1]=findViewById(R.id.mypage_pay_img_dot2);
        img[2]=findViewById(R.id.mypage_pay_img_dot3);
        img[3]=findViewById(R.id.mypage_pay_img_dot4);
        img[4]=findViewById(R.id.mypage_pay_img_dot5);
        img[5]=findViewById(R.id.mypage_pay_img_dot6);

        tvComment = findViewById(R.id.mypage_pay_tv_countComment);
        tvCount = findViewById(R.id.mypage_pay_tv_count);
        tvTitle = findViewById(R.id.mypage_pay_tv_title);
        rearrangement(); // 키패드 배열

        for(int i=0;i<10;i++){
            btn[i].setOnClickListener(numClickListener);
        }
        btnRearrangement.setOnClickListener(numClickListener);
        btnDelete.setOnClickListener(deleteClickListener);
    }


    // 숫자 클릭
    View.OnClickListener numClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(result.length()<7) {
                switch (v.getId()) {
                    case R.id.mypage_pay_btn0:
                        result += btn[0].getText().toString();
                        break;
                    case R.id.mypage_pay_btn1:
                        result += btn[1].getText().toString();
                        break;
                    case R.id.mypage_pay_btn2:
                        result += btn[2].getText().toString();
                        break;
                    case R.id.mypage_pay_btn3:
                        result += btn[3].getText().toString();
                        break;
                    case R.id.mypage_pay_btn4:
                        result += btn[4].getText().toString();
                        break;
                    case R.id.mypage_pay_btn5:
                        result += btn[5].getText().toString();
                        break;
                    case R.id.mypage_pay_btn6:
                        result += btn[6].getText().toString();
                        break;
                    case R.id.mypage_pay_btn7:
                        result += btn[7].getText().toString();
                        break;
                    case R.id.mypage_pay_btn8:
                        result += btn[8].getText().toString();
                        break;
                    case R.id.mypage_pay_btn9:
                        result += btn[9].getText().toString();
                        break;
                    case R.id.mypage_pay_btn_rearrangement:
                        rearrangement();
                        break;
                    default:
                        break;
                }
            }
            resultToStar();
            switch (result.length()){
                case 7:
                    // 비밀번호 설정이 안돼있을때
                    if(uPayPassword.equals("null")){
                        Log.v("비밀번호 설정 x","start");
                        switch (payCheck){
                            // 첫번째 비밀번호 입력
                            case 0:
                                payPassword1=result.substring(1,7);
                                result="0";
                                payCheck=1;

                                resultToStar();
                                tvTitle.setText("다시한번\n입력해주세요.");
                                break;
                            // 확인 비밀번호 입력
                            case 1:
                                payPassword2=result.substring(1,7);

                                // 확인비밀번호가 일치할때
                                if(payPassword1.equals(payPassword2)){
                                    Toast.makeText(Mypage_PayPasswordActivity.this,"yes",Toast.LENGTH_SHORT).show();

                                    //디비에 비밀번호 저장
                                    updatePwd();

                                    new AlertDialog.Builder(Mypage_PayPasswordActivity.this)
                                            .setTitle("결제비밀번호가 저장되었습니다.")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    intent = new Intent(Mypage_PayPasswordActivity.this, Mypage_ProfileChageActivity.class)
                                                            .putExtra("uTelNo", uTelNo)
                                                            .putExtra("uImage", uImage)
                                                            .putExtra("MacIP", MacIP)
                                                            .putExtra("uNo", userSeq)
                                                            .putExtra("uNickName", userNickName);

                                                    startActivityForResult(intent,2);
                                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                                }
                                            })
                                            .show();

                                }else{
                                    Toast.makeText(Mypage_PayPasswordActivity.this,"no",Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(Mypage_PayPasswordActivity.this,"5번틀렷으니 초기화",Toast.LENGTH_SHORT).show();
                                            new AlertDialog.Builder(Mypage_PayPasswordActivity.this)
                                                    .setTitle("비밀번호를 다시 입력해주세요.")
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            tvComment.setVisibility(View.INVISIBLE);
                                                            tvCount.setVisibility(View.INVISIBLE);
                                                            tvTitle.setText("결제 비밀번호를\n입력해주세요");
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
                        // 비밀번호가 설정돼있을때 현재비밀번호부터 체크
                    }else{
                        Log.v("비밀번호설정 ok","start");
                        switch (payCheck2){
                            //현재 비밀번호 체크
                            case 0:
                                payPassword1=result.substring(1,7);
                                //디비에잇는 번호와 입력한 번호가 같을때
                                if(payPassword1.equals(selectPwd())) {

                                    result = "0";
                                    payCheck2 = 1;
                                    Log.v(TAG, "payPassword1 : " + payPassword1 + ", result : " + result);
                                    resultToStar();
                                    rearrangement();
                                    tvTitle.setText("비밀번호\n재설정1.");

                                    // 디비에있는 번호와 입력한 번호가 다를때
                                }else{

                                    result = "0";
                                    payCheck2 = 0;
                                    Log.v(TAG, "payPassword1 : " + payPassword1 + ", result : " + result);
                                    new AlertDialog.Builder(Mypage_PayPasswordActivity.this)
                                            .setTitle("비밀번호를 확인해주세요.")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
//                                                    vTitle.setText("결제 비밀번호를\n입력해주세요");
                                                    rearrangement();
                                                    resultToStar();
                                                }
                                            })
                                            .show();
                                }
                                break;
                                // 디비에있는 번호와 같을때 새로운비밀번호 1
                            case 1:
                                rearrangement();
                                payPassword2=result.substring(1,7);
                                result="0";
                                payCheck2=2;
                                Log.v("비밀번호 설정 ok","payPassword2 : "+payPassword2+", result : "+result);
                                resultToStar();
                                tvTitle.setText("다시한번\n입력해주세요.");
                                break;
                                // 디비에있는 번호와 같을때 새로운비밀번호 재확인
                            case 2:
                                payPassword3=result.substring(1,7);
                                Log.v("비밀번호 설정 ok","payPassword3 : "+payPassword3+", result : "+result);
                                if(payPassword2.equals(payPassword3)){
                                    //디비에 회원정보 저장
                                    updatePwd();

                                    new AlertDialog.Builder(Mypage_PayPasswordActivity.this)
                                            .setTitle("비밀번호 변경 완료.")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    intent = new Intent(Mypage_PayPasswordActivity.this, Mypage_ProfileChageActivity.class)
                                                            .putExtra("uTelNo", uTelNo)
                                                            .putExtra("uImage", uImage)
                                                            .putExtra("MacIP", MacIP)
                                                            .putExtra("uNo", userSeq)
                                                            .putExtra("uNickName", userNickName);

                                                    startActivityForResult(intent,2);
                                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                                }
                                            })
                                            .show();

                                }else{
                                    Toast.makeText(Mypage_PayPasswordActivity.this,"no",Toast.LENGTH_SHORT).show();
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


                                            new AlertDialog.Builder(Mypage_PayPasswordActivity.this)
                                                    .setTitle("비밀번호를 다시 입력해주세요.")
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            tvComment.setVisibility(View.INVISIBLE);
                                                            tvCount.setVisibility(View.INVISIBLE);
                                                            tvTitle.setText("현재 비밀번호를\n입력해주세요");
                                                            rearrangement();
                                                            payCheck2=0;
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
                    }



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


    //디비에 결제비밀번호 받아오기
    private String selectPwd(){
        String Pwd = "null";

        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/cjs_MyPageSelectPwd.jsp?uNo="+userSeq;
            Log.v("dd",urlAddr);
            CJS_NetworkTask_Mypage cjs_networkTask = new CJS_NetworkTask_Mypage(Mypage_PayPasswordActivity.this, urlAddr, "selectPayPassword");
            Object obj = cjs_networkTask.execute().get();

            Pwd= (String) obj;
            Log.v("내비밀번호는 : ",Pwd);

        }catch (Exception e){

        }
        return Pwd;
    }

    private void updatePwd(){
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/cjs_MyPageUpdatePwd.jsp?uNo="+userSeq+"&uPayPassword="+payPassword2;
            CJS_NetworkTask_Mypage cjs_networkTask = new CJS_NetworkTask_Mypage(Mypage_PayPasswordActivity.this, urlAddr, "updatePwd");
            cjs_networkTask.execute().get();

        }catch (Exception e){

        }
    }




}///-END