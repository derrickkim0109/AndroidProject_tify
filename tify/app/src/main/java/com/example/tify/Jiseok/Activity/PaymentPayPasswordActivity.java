package com.example.tify.Jiseok.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.tify.Hyeona.Activity.Payment_resultActivity;
import com.example.tify.Jiseok.NetworkTask.CJS_NetworkTask_Mypage;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Activity.Mypage_PayPasswordActivity;

import java.util.Random;

public class PaymentPayPasswordActivity extends AppCompatActivity {
    String TAG = "여기, PaymentPayPasswordActivity";
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
    int userSeq;
    String myLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjs_activity_payment_pay_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        userEmail = auto.getString("userEmail", null);
        userSeq = auto.getInt("userSeq", 0);
        userNickName = auto.getString("userNickName", null);
        myLocation = auto.getString("myLocation", "noLocation");

        btn[0]=findViewById(R.id.paymentPwd_btn0);
        btn[1]=findViewById(R.id.paymentPwd_btn1);
        btn[2]=findViewById(R.id.paymentPwd_btn2);
        btn[3]=findViewById(R.id.paymentPwd_btn3);
        btn[4]=findViewById(R.id.paymentPwd_btn4);
        btn[5]=findViewById(R.id.paymentPwd_btn5);
        btn[6]=findViewById(R.id.paymentPwd_btn6);
        btn[7]=findViewById(R.id.paymentPwd_btn7);
        btn[8]=findViewById(R.id.paymentPwd_btn8);
        btn[9]=findViewById(R.id.paymentPwd_btn9);

        btnRearrangement=findViewById(R.id.paymentPwd_btn_rearrangement);
        btnDelete=findViewById(R.id.paymentPwd_btn_delete);

        img[0]=findViewById(R.id.paymentPwd_img_dot1);
        img[1]=findViewById(R.id.paymentPwd_img_dot2);
        img[2]=findViewById(R.id.paymentPwd_img_dot3);
        img[3]=findViewById(R.id.paymentPwd_img_dot4);
        img[4]=findViewById(R.id.paymentPwd_img_dot5);
        img[5]=findViewById(R.id.paymentPwd_img_dot6);

        tvComment = findViewById(R.id.paymentPwd_tv_countComment);
        tvCount = findViewById(R.id.paymentPwd_tv_count);
        tvTitle = findViewById(R.id.paymentPwd_tv_title);

        for(int i=0;i<10;i++){
            btn[i].setOnClickListener(numClickListener);
        }
        btnRearrangement.setOnClickListener(numClickListener);
        btnDelete.setOnClickListener(deleteClickListener);

        rearrangement();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 678:
                new AlertDialog.Builder(PaymentPayPasswordActivity.this)
                        .setTitle("잘못된 접근입니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(PaymentPayPasswordActivity.this,JiseokMainActivity.class),678);
                            }
                        })
                        .show();
                break;
        }
    }


    View.OnClickListener numClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(result.length()<7) {
                switch (v.getId()) {
                    case R.id.paymentPwd_btn0:
                        result += btn[0].getText().toString();
                        break;
                    case R.id.paymentPwd_btn1:
                        result += btn[1].getText().toString();
                        break;
                    case R.id.paymentPwd_btn2:
                        result += btn[2].getText().toString();
                        break;
                    case R.id.paymentPwd_btn3:
                        result += btn[3].getText().toString();
                        break;
                    case R.id.paymentPwd_btn4:
                        result += btn[4].getText().toString();
                        break;
                    case R.id.paymentPwd_btn5:
                        result += btn[5].getText().toString();
                        break;
                    case R.id.paymentPwd_btn6:
                        result += btn[6].getText().toString();
                        break;
                    case R.id.paymentPwd_btn7:
                        result += btn[7].getText().toString();
                        break;
                    case R.id.paymentPwd_btn8:
                        result += btn[8].getText().toString();
                        break;
                    case R.id.paymentPwd_btn9:
                        result += btn[9].getText().toString();
                        break;
                    case R.id.paymentPwd_btn_rearrangement:
                        rearrangement();
                        break;
                }
            }
            Log.v("reulst",result);
            resultToStar();

            switch (result.length()){
                case 7:
                    payPassword1=result.substring(1,7);

                    if(payPassword1.equals(selectPwd())){
                        Toast.makeText(PaymentPayPasswordActivity.this,"결제성공",Toast.LENGTH_SHORT).show();
                        // 결제 성공 디비액션


                        new AlertDialog.Builder(PaymentPayPasswordActivity.this)
                                .setTitle("결제가 완료되었습니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 결제완료창으로 이동
                                        Intent intent = new Intent(PaymentPayPasswordActivity.this, Payment_resultActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }else{
                        Toast.makeText(PaymentPayPasswordActivity.this,"비밀번호 다름",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(PaymentPayPasswordActivity.this,"5번틀렷으니 초기화",Toast.LENGTH_SHORT).show();
                                new AlertDialog.Builder(PaymentPayPasswordActivity.this)
                                        .setTitle("비밀번호를 다시 입력해주세요.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                tvComment.setVisibility(View.INVISIBLE);
                                                tvCount.setVisibility(View.INVISIBLE);
                                                //tvTitle.setText("결제 비밀번호를\n입력해주세요");
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
            CJS_NetworkTask_Mypage cjs_networkTask = new CJS_NetworkTask_Mypage(PaymentPayPasswordActivity.this, urlAddr, "selectPayPassword");
            Object obj = cjs_networkTask.execute().get();

            Pwd= (String) obj;
            Log.v("내비밀번호는 : ",Pwd);

        }catch (Exception e){

        }
        return Pwd;
    }
}