package com.example.tify.Taehyun.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
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

    EditText card_Number,card_valid,card_birth,card_password;
    ImageView card_check_agree,card_personal_IV,card_corporation_IV;
    TextView card_agree_exp;
    Button card_cancel,card_success;

    //키보드 내림
    LinearLayout card_detail;
    InputMethodManager inputMethodManager;

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
        //글자수 제한
        function();
    }

    private void function() {

        //글자수 제한
        card_Number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        card_valid.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        card_birth.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        card_password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});


        //유효날짜 가운데에 '/'슬래시 넣기

        card_valid.addTextChangedListener(new TextWatcher() {
            private int beforeLenght = 0;
            private int afterLenght = 0;

            //입력 혹은 삭제 전의 길이와 지금 길이를 비교하기 위해 beforeTextChanged에 저장
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeLenght = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //아무글자도 없는데 지우려고 하면 로그띄우기 에러방지
                if (s.length() <= 0) {
                    Log.d("addTextChangedListener", "onTextChanged: Intput text is wrong (Type : Length)");
                    return;
                }
                //특수문자 입력 방지
                char inputChar = s.charAt(s.length() - 1);
                if (inputChar != '/' && (inputChar < '0' || inputChar > '9')) {
                    card_birth.getText().delete(s.length() - 1, s.length());
                    return;
                }
                afterLenght = s.length();

                String tel = String.valueOf(card_birth.getText());
                tel.substring(0,1);
                Log.v("하이픈", "after" + String.valueOf(afterLenght));

                if (beforeLenght < afterLenght) {// 타자를 입력 중이면
                    if (s.toString().indexOf("01") < 0 && afterLenght == 2) { //subSequence로 지정된 문자열을 반환해서 "-"폰을 붙여주고 substring
                        card_birth.setText(s.toString().subSequence(0, 2) + "-" + s.toString().substring(2, s.length()));

                    } else if (s.toString().indexOf("01") < 0 && afterLenght == 6) {
                        card_birth.setText(s.toString().subSequence(0, 6) + "-" + s.toString().substring(6, s.length()));

                    } else {
                        if (afterLenght == 4 && s.toString().indexOf("-") < 0) { //subSequence로 지정된 문자열을 반환해서 "-"폰을 붙여주고 substring
                            card_birth.setText(s.toString().subSequence(0, 3) + "-" + s.toString().substring(3, s.length()));

                        } else if (s.toString().indexOf("02") < 0 && afterLenght == 9) {
                            card_birth.setText(s.toString().subSequence(0, 8) + "-" + s.toString().substring(8, s.length()));

                        }
                    }
                }
                card_birth.setSelection(card_birth.length());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        //layout 터치시 키보드 내림
        card_detail = findViewById(R.id.card_detail);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  //OS에서 지원해주는 메소드이다.
        card_detail.setOnClickListener(cClickListener);

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

                    if(limitT1 == 0) {
                        limit--;
                        limitT1++;
                        card_personal_IV.setImageResource(R.drawable.personal_black);
                        cInfo = "";
                        //DB에 보낼값.@drawable/


                    }else if(limitT1 == 1 && limit < 1) {
                        limit++;
                        limitT1--;
                        card_personal_IV.setImageResource(R.drawable.personal_light);
                        cInfo = "개인";

                        Toast.makeText(Mypage_CardDetailActivity.this,cInfo,Toast.LENGTH_SHORT).show();

                    }
                    break;
                    //법인
                case R.id.card_corporation_IV:
                    if(limitT2 == 0 && limit < 1) {
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
                //키보드 화면 터치시 숨김.
                case R.id.card_detail:

                    inputMethodManager.hideSoftInputFromWindow(card_detail.getWindowToken(),0);
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

                    String cardResult = Validate(getNumberCheck);
                    if (cardResult == "" || cardResult == "null" || getNumberCheck.length() <= 16 || 13 <= getNumberCheck.length() ){
                        new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                                .setTitle("카드 정보를 다시 확인하세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        card_Number.requestFocus();
                    }else {


                    cPassword = card_password.getText().toString();
                    cYMM = card_valid.getText().toString();
                    cBirthday = card_birth.getText().toString();

                    if (cPassword.length() <= 0 && cPassword.length() < 2){
                        new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                                .setTitle("카드 비밀번호를 입력하세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        card_password.requestFocus();
                    } else if (cYMM.length() <= 0 && cYMM.length() < 4){
                        new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                                .setTitle("유효 날짜를 입력하세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        card_valid.requestFocus();

                    } else if (cBirthday.length() <= 0 && cBirthday.length() < 6){
                        new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                                .setTitle("생년월일을 입력하세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        card_birth.requestFocus();
                    } else {
                       String result = connectInsert();
                        if(result.equals("1")){
                            Toast.makeText(Mypage_CardDetailActivity.this,"성공 .",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Mypage_CardDetailActivity.this,"실패 .",Toast.LENGTH_SHORT).show();

                        }
                        intent = new Intent(Mypage_CardDetailActivity.this,Mypage_CardRegistrationActivity.class)
                                .putExtra("uNo",uNo)
                                .putExtra("macIP",macIP);
                        setResult(RValue,intent);
                    }
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

    //카드 등록 네트워크 타스크
    private String connectInsert() {

        String result = null;

        urlAddress = urlAddr + "&cCardNo=" + cCardNo + "&cYMM=" + cYMM  + "&cPassword=" + cPassword+
                "&cBirthday=" + cBirthday + "&cInfo=" + cInfo
                + "&cCardCompany=" + cCardCompany + "&user_uNo=" + uNo ;

        try {
            NetworkTask_TaeHyun insertNetworkTask = new NetworkTask_TaeHyun(Mypage_CardDetailActivity.this, urlAddress, "insert");
            Object obj = insertNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    //카드 정규식
    public String Validate(String getNumberCheck){

        String cCardCompany = null;

      //앞에 오는 숫자 체크 위해
      int prefix_master = Integer.parseInt(getNumberCheck.substring(0, 2));
      int prefix_DINERS_CLUB = Integer.parseInt(getNumberCheck.substring(0, 3));


          //Visa
          if (getNumberCheck.startsWith("4") && getNumberCheck.length() >= 13
                  && getNumberCheck.length() <= 16){

              if (!Pattern.matches(ptVisa,card_Number.getText().toString())){
                  cCardCompany = "VISA";
              }else {
//                  Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
                  Toast.makeText(Mypage_CardDetailActivity.this,"Visa.",Toast.LENGTH_SHORT).show();
              }
          }

          //Master
          if (prefix_master >= 51 && prefix_master<= 55){
              if (!Pattern.matches(ptMasterCard,card_Number.getText().toString())){
                  cCardCompany = "MASTERCARD";
              }else {
//                  Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
                  Toast.makeText(Mypage_CardDetailActivity.this,"MASTERCARD",Toast.LENGTH_SHORT).show();
              }
          }

          //Amex
          if (getNumberCheck.length() == 15 && (getNumberCheck.startsWith("34") || getNumberCheck
                  .startsWith("37"))){
              if (!Pattern.matches(ptAmeExp,card_Number.getText().toString())){
                  cCardCompany = "Amex";
              }else {
//                  Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
                  Toast.makeText(Mypage_CardDetailActivity.this,"Amex",Toast.LENGTH_SHORT).show();
              }
          }

          //DINERS_CLUB And CARTE_BLANCHE
          if (getNumberCheck.length() == 14  && (prefix_DINERS_CLUB >= 300 && prefix_DINERS_CLUB <= 305)
                  && (getNumberCheck.startsWith("36") || getNumberCheck.startsWith("38"))){
              if (!Pattern.matches(ptDinClb,card_Number.getText().toString())){
                  cCardCompany = "DINERS_CLUB And CARTE_BLANCHE";
              }else {
//                  Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
                  Toast.makeText(Mypage_CardDetailActivity.this,"DINERS_CLUB",Toast.LENGTH_SHORT).show();
              }
          }
        ///DISCOVER
          if (getNumberCheck.length() == 16 && getNumberCheck.startsWith("6011")){
              if (!Pattern.matches(ptDiscover,card_Number.getText().toString())){
                  cCardCompany = "DISCOVER";
              }else {
//                  Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
                  Toast.makeText(Mypage_CardDetailActivity.this,"DISCOVER",Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(Mypage_CardDetailActivity.this,"카드정보가 틀립니다.",Toast.LENGTH_SHORT).show();
                Toast.makeText(Mypage_CardDetailActivity.this,"JCB",Toast.LENGTH_SHORT).show();
            }
        }

        return cCardCompany;
  }




}//--end