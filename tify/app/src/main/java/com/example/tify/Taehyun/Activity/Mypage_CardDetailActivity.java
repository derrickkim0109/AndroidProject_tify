package com.example.tify.Taehyun.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
   //키패드 내리기
    LinearLayout card_detail_ll ;
    InputMethodManager inputMethodManager ;


    EditText card_Number1,card_Number2,card_Number3,card_Number4,card_validyear, card_validmm,card_birth,card_password;
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
    String cCardNo, cPassword, cYear, cMM, cBirthday, cCardCompany= "";
    String cInfo = "개인";


    String getNumberCheck = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kth_activity_mypage__card_detail);
        init();
        intent = getIntent();
        inheritance();

        urlAddr = "http://" + macIP + ":8080/tify/mypage_card_insert.jsp?";
        function();

    }

    private void function() {
        card_Number1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        card_Number2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        card_Number3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        card_Number4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

        card_validyear.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        card_validmm.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        card_birth.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        card_password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
    }

    private void inheritance() {
        macIP = intent.getStringExtra("macIP");

        //보낼 data
        uNo = intent.getIntExtra("uNo",0);    }

    private void init() {
        actionBar = getSupportActionBar();

        //내용 작성할 곳.
        card_Number1 = findViewById(R.id.card_Number1);
        card_Number2 = findViewById(R.id.card_Number2);
        card_Number3 = findViewById(R.id.card_Number3);
        card_Number4= findViewById(R.id.card_Number4);

        card_validyear = findViewById(R.id.card_validyear);
        card_validmm = findViewById(R.id.card_validmm);
        card_birth = findViewById(R.id.card_birth);
        card_password = findViewById(R.id.card_password);

        //누르면 색 바뀌는 버튼들
        card_personal_IV = findViewById(R.id.card_personal_IV);
        card_corporation_IV = findViewById(R.id.card_corporation_IV);
        card_check_agree = findViewById(R.id.card_check_agree);

        card_personal_IV.setOnClickListener(cClickListener);
        card_corporation_IV.setOnClickListener(cClickListener);
        card_check_agree.setOnClickListener(cClickListener);
        //키패드 내리기
        card_detail_ll = findViewById(R.id.card_detail_ll);
        card_detail_ll.setOnClickListener(cClickListener);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  //OS에서 지원해주는 메소드이다.

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


                    }else if(limitT1 == 1) {
                        limit++;
                        limitT1--;
                        card_personal_IV.setImageResource(R.drawable.personal_light);
                        cInfo = "개인";
                        Toast.makeText(Mypage_CardDetailActivity.this,cInfo,Toast.LENGTH_SHORT).show();
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
                case R.id.card_detail_ll:
                    inputMethodManager.hideSoftInputFromWindow(card_detail_ll.getWindowToken(),0);
                    break;
            }
        }
    };

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //더보기
                case R.id.card_agree_exp:

                    break;
                    //등록하기
                case R.id.card_success:
                    getNumberCheck = card_Number1.getText().toString().trim() + card_Number2.getText().toString().trim()
                            + card_Number3.getText().toString().trim() + card_Number4.getText().toString().trim();

                    Validate();

                    cCardNo = getNumberCheck;
                    cPassword = card_password.getText().toString().trim();
                    cYear = card_validyear.getText().toString().trim();
                    cMM = card_validmm.getText().toString().trim();
                    cBirthday = card_birth.getText().toString().trim();

                    if (cPassword.length() == 0){
                        Toast.makeText(Mypage_CardDetailActivity.this,"카드 비밀번호를 입력하세요",Toast.LENGTH_SHORT).show();
                    }else if (cYear.length() == 0 ) {
                        Toast.makeText(Mypage_CardDetailActivity.this, "만기 날짜를 입력하세요", Toast.LENGTH_SHORT).show();
                    }else if (cMM.length() == 0 ){
                        Toast.makeText(Mypage_CardDetailActivity.this,"만기 날짜를 입력하세요",Toast.LENGTH_SHORT).show();

                    } else if (cBirthday.length() == 0){
                        Toast.makeText(Mypage_CardDetailActivity.this,"생년월일을 입력하세요",Toast.LENGTH_SHORT).show();
                    } else {

                        //insert NetworkTask
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
                "&cYear=" + cYear + "&cMM="+ cMM + "&cBirthday=" + cBirthday + "&cCardCompany=" + cCardCompany +"&cInfo=" + cInfo ;

        try {
            NetworkTask_TaeHyun insertNetworkTask = new NetworkTask_TaeHyun(Mypage_CardDetailActivity.this, urlAddress, "insert");
            Object obj = insertNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    public void Validate(){

        getNumberCheck = card_Number1.getText().toString().trim() + card_Number2.getText().toString().trim()
                + card_Number3.getText().toString().trim() + card_Number4.getText().toString().trim();

      //앞에 오는 숫자 체크 위해
      int prefix_master = Integer.parseInt(getNumberCheck.substring(0, 2));
      int prefix_DINERS_CLUB = Integer.parseInt(getNumberCheck.substring(0, 3));

      if(getNumberCheck.getBytes().length<=0){
          new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                  .setTitle("신용카드 정보를 확인해 주세요.")
                  .setPositiveButton("확인",null)
                  .show();
          card_Number1.requestFocus();

          //Visa
          if (getNumberCheck.startsWith("4") && (getNumberCheck.length() == 13 || getNumberCheck.length() == 16)){
              cCardCompany = "VISA";
          }else {
              Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
          }
          }

          //Master
          if (prefix_master >= 51 && prefix_master<= 55 && getNumberCheck.length() == 14){
              cCardCompany = "MASTERCARD";
          }
          else {
              Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
          }

          //Amex
          if (getNumberCheck.length() == 13 && (getNumberCheck.startsWith("34") || getNumberCheck
                  .startsWith("37"))){
              cCardCompany = "AMEX";
          }else {
              Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
          }


          //DINERS_CLUB And CARTE_BLANCHE
          if (getNumberCheck.length() == 14  && (prefix_DINERS_CLUB >= 300 && prefix_DINERS_CLUB <= 305)
                  && (getNumberCheck.startsWith("36") || getNumberCheck.startsWith("38"))){
              cCardCompany = "DINERS_CLUB And CARTE_BLANCHE";
          }else {
              Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
          }

        ///DISCOVER
          if (getNumberCheck.length() == 16 && getNumberCheck.startsWith("6011")){
              cCardCompany = "DISCOVER";
          }else {
              Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
          }

          //JCB
        if ((getNumberCheck.length() == 16 && getNumberCheck.startsWith("3"))
                || (getNumberCheck.length() == 15 && (getNumberCheck
                .startsWith("2131") || getNumberCheck
                .startsWith("1800")))){
            cCardCompany = "JCB";
        }else {
            Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
        }
        if(cCardCompany == ""){

            Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
        }

      }///---if END


}//--end