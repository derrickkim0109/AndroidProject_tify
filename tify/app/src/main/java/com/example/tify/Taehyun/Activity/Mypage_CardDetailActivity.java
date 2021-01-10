package com.example.tify.Taehyun.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tify.R;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;

import java.util.regex.Pattern;

public class Mypage_CardDetailActivity extends AppCompatActivity {

   //field
   final static String TAG = "Mypage_CardDetailActivity";

    //url
    //jsp적을 주소
    String urlAddr = null;
    //
    String urlAddress = null;

    ActionBar actionBar = null;

    EditText card_Number,card_valid,card_birth,card_password;
    ImageView card_check_agree,card_personal_IV,card_corporation_IV;
    TextView card_agree_exp;
    Button card_cancel,card_success;



    //카드 개인, 법인, 동의 여부 위해.
    int limit = 1;
    int limitT1 = 0;
    int limitT2 = 0;
    int limitT3 = 0;

    Intent intent = null;
    final static int RValue = 0;

    //DB
    int uNo = 0;
    String macIP = null;
    //카드 번호, 비번, 유효기간, 생년월일, 카드결제 회사, 카드 정보(개인,법인)
    String cCardNo,cPassword,cYMM, cBirthday,cCardCompany,cInfo = "";

    String ptVisa = "^4[0-9]{6,}$";
    String ptMasterCard = "^5[1-5][0-9]{5,}$";
    String ptAmeExp = "^3[47][0-9]{5,}$";
    String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
    String ptCARTE_BLANCHE = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
    String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
    String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
    String getNumberCheck = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_mypage__card_detail);
        init();
        intent = getIntent();
        inheritance();

        urlAddr = "http://" + macIP + ":8080/tify/mypage_card_insert.jsp?";

    }

    private void inheritance() {
        macIP = intent.getStringExtra("macIP");

        //보낼 data
        uNo = intent.getIntExtra("uNo",0);    }

    private void init() {
        actionBar = getSupportActionBar();

        //내용 작성할 곳.
        card_Number = findViewById(R.id.card_Number);
        card_valid = findViewById(R.id.card_valid);
        card_birth = findViewById(R.id.card_birth);
        card_password = findViewById(R.id.card_password);

        //누르면 색 바뀌는 버튼들
        card_personal_IV = findViewById(R.id.card_personal_IV);
        card_corporation_IV = findViewById(R.id.card_corporation_IV);
        card_check_agree = findViewById(R.id.card_check_agree);

        card_personal_IV.setOnClickListener(cClickListener);
        card_corporation_IV.setOnClickListener(cClickListener);
        card_check_agree.setOnClickListener(cClickListener);

        //더보기 버튼  - 다이얼
        card_agree_exp = findViewById(R.id.card_agree_exp);
        card_agree_exp.setOnClickListener(mClickListener);

        //취소, 등록하기 버튼
        card_cancel = findViewById(R.id.card_cancel);
        card_success = findViewById(R.id.card_success);

        card_cancel.setOnClickListener(mClickListener);
        card_success.setOnClickListener(mClickListener);

    }
    //Back 버튼
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back by clicking back button of actionbar
        return super.onSupportNavigateUp();
    }

    View.OnClickListener cClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //개인
                case R.id.card_personal_IV:

                    if(limitT1 == 0 && limit < 2) {
                        limit--;
                        limitT1++;
                        card_personal_IV.setImageResource(R.drawable.personal_black);
                        cInfo = "";
                        //DB에 보낼값.@drawable/

                        Toast.makeText(Mypage_CardDetailActivity.this,cInfo,Toast.LENGTH_SHORT).show();

                    }else if(limitT1 == 1) {
                        limit++;
                        limitT1--;
                        card_personal_IV.setImageResource(R.drawable.personal_light);
                        cInfo = "개인";
                    }
                    break;
                    //법인
                case R.id.card_corporation_IV:
                    if(limitT2 == 0 && limit < 2) {
                        limit++;
                        limitT2++;
                        //DB에 보낼값.
                        card_corporation_IV.setImageResource(R.drawable.corporation_light);

                        cInfo = "법인";
                        Toast.makeText(Mypage_CardDetailActivity.this,cInfo,Toast.LENGTH_SHORT).show();

                    }else if(limitT2 == 1) {
                        limit--;
                        limitT2--;
                        card_corporation_IV.setImageResource(R.drawable.corporation_black);
                        cInfo = "";
                    }
                    break;
                    //동의 버튼
                case R.id.card_check_agree:
                    if(limitT3 == 0) {
                        limitT3++;
                        card_check_agree.setImageResource(R.drawable.ic_action_agree1_check);
                        //DB에 보낼값.

                        Toast.makeText(Mypage_CardDetailActivity.this,"동의합니다.",Toast.LENGTH_SHORT).show();

                    }else if(limitT3 == 1) {
                        limitT3--;
                        card_check_agree.setImageResource(R.drawable.ic_action_agree_check);
                    }
                    break;
            }
        }
    };

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //더보기
                case R.id.card_agree_exp:

                    break;
                    //등록하기
                case R.id.card_success:
                    getNumberCheck = card_Number.getText().toString();
                    Validate(getNumberCheck);

                    cCardNo = getNumberCheck;
                    cPassword = card_password.getText().toString();
                    cYMM = card_valid.getText().toString();
                    cBirthday = card_birth.getText().toString();

                    if (cPassword.length() == 0){
                        Toast.makeText(Mypage_CardDetailActivity.this,"카드 비밀번호를 입력하세요",Toast.LENGTH_SHORT).show();
                    } else if (cYMM.length() == 0){
                        Toast.makeText(Mypage_CardDetailActivity.this,"만기 날짜를 입력하세요",Toast.LENGTH_SHORT).show();

                    } else if (cBirthday.length() == 0){
                        Toast.makeText(Mypage_CardDetailActivity.this,"생년월일을 입력하세요",Toast.LENGTH_SHORT).show();
                    } else {
                       String result = connectInset();
                        if(result.equals("1")){
                            Toast.makeText(Mypage_CardDetailActivity.this,"성공 .",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Mypage_CardDetailActivity.this,"실패 .",Toast.LENGTH_SHORT).show();

                        }
                        intent = new Intent(Mypage_CardDetailActivity.this,Mypage_CardRegistrationActivity.class)
                                .putExtra("result",result)
                                .putExtra("uNo",uNo)
                                .putExtra("macIP",macIP);
                        setResult(RValue,intent);
                    }
                    break;
                    //취소하기
                case R.id.card_cancel:
                    intent = new Intent(Mypage_CardDetailActivity.this,MypageActivity.class)
                            .putExtra("uNo",uNo)
                            .putExtra("macIP",macIP);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private String connectInset() {

        String result = null;

        urlAddress = urlAddr + "&cCardNo=" + cCardNo + "&cPassword=" + cPassword +
                "&cYMM=" + cYMM + "&cBirthday=" + cBirthday + "&cInfo=" + cInfo;

        try {
            NetworkTask_TaeHyun insertNetworkTask = new NetworkTask_TaeHyun(Mypage_CardDetailActivity.this, urlAddress, "insert");
            Object obj = insertNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    public void Validate(String getNumberCheck){

      //앞에 오는 숫자 체크 위해
      int prefix_master = Integer.parseInt(getNumberCheck.substring(0, 2));
      int prefix_DINERS_CLUB = Integer.parseInt(getNumberCheck.substring(0, 3));

      if(getNumberCheck.getBytes().length<=0){
          new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                  .setTitle("신용카드 정보를 확인해 주세요.")
                  .setPositiveButton("확인",null)
                  .show();
          card_Number.requestFocus();

          //Visa
          if (getNumberCheck.startsWith("4") && getNumberCheck.length() >= 13
                  && getNumberCheck.length() <= 16){

              if (!Pattern.matches(ptVisa,card_Number.getText().toString())){
                  cCardCompany = "VISA";
              }else {
                  Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
              }
          }

          //Master
          if (prefix_master >= 51 && prefix_master<= 55){
              if (!Pattern.matches(ptMasterCard,card_Number.getText().toString())){
                  cCardCompany = "MASTERCARD";
              }else {
                  Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
              }
          }

          //Amex
          if (getNumberCheck.length() == 15 && (getNumberCheck.startsWith("34") || getNumberCheck
                  .startsWith("37"))){
              if (!Pattern.matches(ptAmeExp,card_Number.getText().toString())){
                  cCardCompany = "MASTERCARD";
              }else {
                  Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
              }
          }

          //DINERS_CLUB And CARTE_BLANCHE
          if (getNumberCheck.length() == 14  && (prefix_DINERS_CLUB >= 300 && prefix_DINERS_CLUB <= 305)
                  && (getNumberCheck.startsWith("36") || getNumberCheck.startsWith("38"))){
              if (!Pattern.matches(ptDinClb,card_Number.getText().toString())){
                  cCardCompany = "DINERS_CLUB And CARTE_BLANCHE";
              }else {
                  Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
              }
          }
        ///DISCOVER
          if (getNumberCheck.length() == 16 && getNumberCheck.startsWith("6011")){
              if (!Pattern.matches(ptDiscover,card_Number.getText().toString())){
                  cCardCompany = "DISCOVER";
              }else {
                  Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
              }
          }
          //JCB
        if ((getNumberCheck.length() == 16 && getNumberCheck.startsWith("3"))
                || (getNumberCheck.length() == 15 && (getNumberCheck
                .startsWith("2131") || getNumberCheck
                .startsWith("1800")))){

            if (!Pattern.matches(ptJcb,card_Number.getText().toString())){
                cCardCompany = "JCB";
            }else {
                Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
            }
        }


      }///---if END

  }




}//--end