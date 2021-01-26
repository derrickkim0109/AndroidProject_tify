package com.example.tify.Taehyun.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;

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
    ImageView card_personal_IV,card_corporation_IV;
    CheckBox card_check_agree;
    TextView card_agree_exp;
    Button card_cancel,card_success;

    int check = 1;

    //카드 개인, 법인, 동의 여부 위해.
    int limit = 1;
    int limitT1 = 0;
    int limitT2 = 0;
    int limitT3 = 0;

    Intent intent = null;
    final static int RValue = 0;

    //DB
    int uNo = 0;
    //IP
    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

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

        urlAddr = "http://" + MacIP + ":8080/tify/mypage_card_insert.jsp?";

        //글자수 제한
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
                //키보드 화면 터치시 숨김.
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
                check=1;
            switch (v.getId()){
                //더보기
                case R.id.card_agree_exp:

                    break;
                    //등록하기
                case R.id.card_success:

                    getNumberCheck = card_Number1.getText().toString().trim() + card_Number2.getText().toString().trim()
                            + card_Number3.getText().toString().trim() + card_Number4.getText().toString().trim();


                    Log.v(TAG,"cCardCompany" + cCardCompany);
                    cCardNo = getNumberCheck;
                    cPassword = card_password.getText().toString().trim();
                    cYear = card_validyear.getText().toString().trim();
                    cMM = card_validmm.getText().toString().trim();
                    cBirthday = card_birth.getText().toString().trim();

                    //앞에 오는 숫자 체크 위해


                    if(getNumberCheck.length()==0) {
                        new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                                .setTitle("신용카드 정보를 확인해 주세요.")
                                .setPositiveButton("확인", null)
                                .show();
                        if (card_Number1.length() == 0){
                            card_Number1.requestFocus();
                            break;
                        }
                        if (card_Number2.length() == 0){
                            card_Number2.requestFocus();
                            break;
                        }
                        if (card_Number3.length() == 0){
                            card_Number3.requestFocus();
                            break;
                        }
                        if (card_Number4.length() == 0){
                            card_Number4.requestFocus();
                            break;
                        }

                    }else if (getNumberCheck.length() > 0) {
                    //Visa
                    if (getNumberCheck.startsWith("4")) {
                        cCardCompany = "VISA";
                        Toast.makeText(Mypage_CardDetailActivity.this, cCardCompany, Toast.LENGTH_SHORT).show();
                        check = 0;
                    }
                    //Master

                    if ((getNumberCheck.startsWith("51")) && getNumberCheck.startsWith("55")) {
                        cCardCompany = "MASTERCARD";
                        Toast.makeText(Mypage_CardDetailActivity.this, cCardCompany, Toast.LENGTH_SHORT).show();
                        check = 0;

                    }
                    //Amex
                    if ((getNumberCheck.startsWith("34") || getNumberCheck
                            .startsWith("37"))) {
                        cCardCompany = "AMEX";
                        Toast.makeText(Mypage_CardDetailActivity.this, cCardCompany, Toast.LENGTH_SHORT).show();
                        check = 0;

                    }
                    //DINERS_CLUB And CARTE_BLANCHE
                    if ((getNumberCheck.startsWith("36") || getNumberCheck.startsWith("38"))) {
                        cCardCompany = "DINERS_CLUB And CARTE_BLANCHE";
                        Toast.makeText(Mypage_CardDetailActivity.this, cCardCompany, Toast.LENGTH_SHORT).show();
                        check = 0;

                    }
                    ///DISCOVER
                    if (getNumberCheck.startsWith("6011")) {
                        cCardCompany = "DISCOVER";
                        Toast.makeText(Mypage_CardDetailActivity.this, cCardCompany, Toast.LENGTH_SHORT).show();
                        check = 0;

                    }
                    //JCB
                    if (((getNumberCheck
                            .startsWith("2131") || getNumberCheck
                            .startsWith("1800")))) {
                        cCardCompany = "JCB";
                        Toast.makeText(Mypage_CardDetailActivity.this, cCardCompany, Toast.LENGTH_SHORT).show();
                        check = 0;

                    }
                }

                    ////////////////////////////////////////////////////////////
                    //                                                        //
                    //                                                        //
                    //                    /유효성 검사  //   2021.01.07 -태현     //
                    ////////////////////////////////////////////////////////////

                    if (cYear.length() <= 0){
                        new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                                .setTitle("유효 날짜를 입력하세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        card_validyear.requestFocus();
                        break;

                    }
                    if (cMM.length() <= 0){
                        new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                                .setTitle("유효 날짜를 입력하세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        card_validmm.requestFocus();
                        break;
                    }

                    if (cBirthday.length() <= 0 && cBirthday.length() < 6){
                        new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                                .setTitle("생년월일을 입력하세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        card_birth.requestFocus();
                        break;
                    }
                    if (cPassword.length() <= 0 && cPassword.length() < 2){
                        new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                                .setTitle("카드 비밀번호를 입력하세요.")
                                .setPositiveButton("확인",null)
                                .show();
                        card_password.requestFocus();
                        break;
                    }
                    if (card_check_agree.isSelected()){
                        new AlertDialog.Builder(Mypage_CardDetailActivity.this)
                                .setTitle("동의를 체크해 주세요.")
                                .setPositiveButton("확인", null)
                                .show();
                    }
                    Log.v(TAG,"Check = " + check);
                    if (check == 0) {
                        Log.v(TAG, "cCardCompany" + cCardCompany);

                        //NetworkTask 입력 부분
                        String result = connectInsert();
                        Log.v(TAG, "result" + result);


                        if (result.equals("1")) {
                            Toast.makeText(Mypage_CardDetailActivity.this, "성공 .", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Mypage_CardDetailActivity.this, "실패 .", Toast.LENGTH_SHORT).show();

                        }
                        // 페이지 이동
                        intent = new Intent(Mypage_CardDetailActivity.this, Mypage_CardRegistrationActivity.class)
                                .putExtra("uNo", uNo)
                                .putExtra("MacIP", MacIP);
                        startActivity(intent);

                    }
                    else if (check == 1){
                        Toast.makeText(Mypage_CardDetailActivity.this, "정보를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();

                    }


                    break;
                    //취소하기
                case R.id.card_cancel:
                    intent = new Intent(Mypage_CardDetailActivity.this,MypageActivity.class)
                            .putExtra("uNo",uNo)
                            .putExtra("MacIP",MacIP);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    //카드 등록 네트워크 타스크
    @SuppressLint("LongLogTag")
    private String connectInsert() {

        String result = null;
        urlAddress = urlAddr + "&cCardNo=" + cCardNo + "&cPassword=" + cPassword + "&uNo="+ uNo +
                "&cYear=" + cYear + "&cMM="+ cMM + "&cBirthday=" + cBirthday + "&cCardCompany=" + cCardCompany +"&cInfo=" + cInfo ;
        Log.v(TAG,"urlAddress : " + urlAddress );

        try {
            NetworkTask_TaeHyun insertNetworkTask = new NetworkTask_TaeHyun(Mypage_CardDetailActivity.this, urlAddress, "insert");
            Object obj = insertNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }
    ////////////////////////////////////////////////////////////
    //                                                        //
    //                                                        //
    //                    / 액션바 //   2021.01.07 -태현     //
    ////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayHomeAsUpEnabled(false);
        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowTitleEnabled(false);
        //홈 아이콘을 숨김처리합니다.
        actionBar.setDisplayShowHomeEnabled(false);

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.cha_custom_actionbar, null);

        actionBar.setCustomView(actionbar);
        TextView title = findViewById(R.id.title);
        title.setText("카드등록");

        ImageButton cart = findViewById(R.id.cart);
        cart.setVisibility(View.VISIBLE);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


//         장바구니 없애려면 위에거 살리면 됨
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        return true;
    }
    //---------------------------------
}//--end