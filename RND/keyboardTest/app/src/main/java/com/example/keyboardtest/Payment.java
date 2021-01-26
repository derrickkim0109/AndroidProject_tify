package com.example.keyboardtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Payment extends AppCompatActivity {
//    TextView btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0,btnRearrangement,btnDelete;

    TextView btnRearrangement,btnDelete;
    TextView [] btn = new TextView[10];
    String result = "0"; // null 값에서 길이 체크하면 터짐
    String userpw=null;
    Toast toast=null;
    int count=0;//비밀번호 틀린 횟수
    String passCheck="";// 결제비밀번호 설정이 돼있는지 체크
    ImageView [] img = new ImageView[6];
    Random random = new Random();
    String [] btnNum = new String[10];//랜덤 10개수를 받아서 버튼에 넣음

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        userpw = password();//유저패스워드값 받아옴

        btn[0]=findViewById(R.id.btn0);
        btn[1]=findViewById(R.id.btn1);
        btn[2]=findViewById(R.id.btn2);
        btn[3]=findViewById(R.id.btn3);
        btn[4]=findViewById(R.id.btn4);
        btn[5]=findViewById(R.id.btn5);
        btn[6]=findViewById(R.id.btn6);
        btn[7]=findViewById(R.id.btn7);
        btn[8]=findViewById(R.id.btn8);
        btn[9]=findViewById(R.id.btn9);
        btnRearrangement=findViewById(R.id.btn_rearrangement);
        btnDelete=findViewById(R.id.btn_delete);

        img[0]=findViewById(R.id.img1);
        img[1]=findViewById(R.id.img2);
        img[2]=findViewById(R.id.img3);
        img[3]=findViewById(R.id.img4);
        img[4]=findViewById(R.id.img5);
        img[5]=findViewById(R.id.img6);

        rearrangement(); // 키패드 배열
        userpw=password();//비밀번호 받아옴

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

    View.OnClickListener numClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(result.length()<7) {
                switch (v.getId()) {
                    case R.id.btn0:
                        result += btn[0].getText().toString();
                        break;
                    case R.id.btn1:
                        result += btn[1].getText().toString();
                        break;
                    case R.id.btn2:
                        result += btn[2].getText().toString();
                        break;
                    case R.id.btn3:
                        result += btn[3].getText().toString();
                        break;
                    case R.id.btn4:
                        result += btn[4].getText().toString();
                        break;
                    case R.id.btn5:
                        result += btn[5].getText().toString();
                        break;
                    case R.id.btn6:
                        result += btn[6].getText().toString();
                        break;
                    case R.id.btn7:
                        result += btn[7].getText().toString();
                        break;
                    case R.id.btn8:
                        result += btn[8].getText().toString();
                        break;
                    case R.id.btn9:
                        result += btn[9].getText().toString();
                        break;
                    case R.id.btn_rearrangement:
                        rearrangement();
                        break;
                    default:
                        break;
                }
            }

            resultToStar();
            Log.v("여기","result : " + result+", pass : " + userpw);

            switch (result.length()){
                case 7:
                    //비밀번호 일치할때
                    if(result.substring(1,result.toString().length()).equals(userpw)) {
                        Intent intent = new Intent(Payment.this,MainActivity.class);
                        startActivity(intent);

                    //비밀번호 불일치할때
                    }else{
                        count++; // 비밀번호 틀린 횟수
                        result="0";
                        Log.v("여기",""+count);
                        resultToStar();
                    }
                    break;
            }

        }
    };

    View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (result.length() == 1) {
            } else result = result.substring(0, result.length() - 1);

            resultToStar();
        }
    };

    private void resultToStar(){
            // 색 없는 별로 전체 초기화
            for(int i=0;i<6;i++){
                img[i].setImageDrawable(getResources().getDrawable(R.drawable.no));
            }
            // 비밀번호 길이만큼 별 색칠
            for (int i = 0; i < result.length() - 1; i++) {
                img[i].setImageDrawable(getResources().getDrawable(R.drawable.yes));
            }
    }

    // 재배열
    private void rearrangement(){
        rand();
        setBtn();
    }

    //디비에서 받은 비밀번호
    private String password(){
        String pass = "123456";
        return pass;
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
}