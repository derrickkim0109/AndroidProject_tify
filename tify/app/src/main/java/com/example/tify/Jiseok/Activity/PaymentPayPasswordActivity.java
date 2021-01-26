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
import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.example.tify.Taehyun.Activity.MypageActivity;
import com.example.tify.Taehyun.Activity.Mypage_PayPasswordActivity;
import com.example.tify.Taehyun.Activity.OrderPage_PaymentActivity;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;

import java.util.ArrayList;
import java.util.Random;

public class PaymentPayPasswordActivity extends AppCompatActivity {
    String TAG = "여기, PaymentPayPasswordActivity";
    Context context;
    ShareVar shareVar = new ShareVar();
    String MacIP = shareVar.getMacIP();

    Button btnRearrangement;
    ImageButton btnDelete;
    Button[] btn = new Button[10];
    String result = "0"; // null 값에서 길이 체크하면 터짐
    String userpw = null;
    int count = 0;//비밀번호 틀린 횟수
    //    String passCheck="";// 결제비밀번호 설정이 돼있는지 체크
    ImageView[] img = new ImageView[6];
    Random random = new Random();
    String[] btnNum = new String[10];//랜덤 10개수를 받아서 버튼에 넣음

    String payPassword1 = null;
    String payPassword2 = null;// 비밀번호 확인
    int payCheck = 0; // 0일때 payPassword1, 1일때 payPassword2

    TextView tvComment, tvCount, tvTitle;

    String userTel;
    String userEmail;
    String userProfile;
    String userNickName;
    int userSeq;
    String myLocation;
    int point;
    int oNo;
    int cNo;
    int store_SeqNo;
    String sName;
    int totalPrice;
    String cCardCompany;
    String card_cNo;
    String urlAddr2 =  "http://" + MacIP + ":8080/tify/orderPay_insert.jsp?";
    String urlAddress2;
    String urlAddress;

    //order === BEAN
    Order order = null;
    int dataSize = 0;
    ArrayList<String> urls = new ArrayList<String>();
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


        Intent intent = getIntent();
        point = intent.getIntExtra("point", 0);
        cNo = intent.getIntExtra("cNo", 0);
        store_SeqNo = intent.getIntExtra("store_SeqNo", 0);
        sName = intent.getStringExtra("sName");
        totalPrice = intent.getIntExtra("totalPrice", 0);
        cCardCompany = intent.getStringExtra("cCardCompany");
        card_cNo = intent.getStringExtra("card_cNo");
        urlAddress2 = intent.getStringExtra("urlAddress");
        dataSize = intent.getIntExtra("size", 0);


        btn[0] = findViewById(R.id.paymentPwd_btn0);
        btn[1] = findViewById(R.id.paymentPwd_btn1);
        btn[2] = findViewById(R.id.paymentPwd_btn2);
        btn[3] = findViewById(R.id.paymentPwd_btn3);
        btn[4] = findViewById(R.id.paymentPwd_btn4);
        btn[5] = findViewById(R.id.paymentPwd_btn5);
        btn[6] = findViewById(R.id.paymentPwd_btn6);
        btn[7] = findViewById(R.id.paymentPwd_btn7);
        btn[8] = findViewById(R.id.paymentPwd_btn8);
        btn[9] = findViewById(R.id.paymentPwd_btn9);

        btnRearrangement = findViewById(R.id.paymentPwd_btn_rearrangement);
        btnDelete = findViewById(R.id.paymentPwd_btn_delete);

        img[0] = findViewById(R.id.paymentPwd_img_dot1);
        img[1] = findViewById(R.id.paymentPwd_img_dot2);
        img[2] = findViewById(R.id.paymentPwd_img_dot3);
        img[3] = findViewById(R.id.paymentPwd_img_dot4);
        img[4] = findViewById(R.id.paymentPwd_img_dot5);
        img[5] = findViewById(R.id.paymentPwd_img_dot6);

        tvComment = findViewById(R.id.paymentPwd_tv_countComment);
        tvCount = findViewById(R.id.paymentPwd_tv_count);
        tvTitle = findViewById(R.id.paymentPwd_tv_title);

        for (int i = 0; i < 10; i++) {
            btn[i].setOnClickListener(numClickListener);
        }
        btnRearrangement.setOnClickListener(numClickListener);
        btnDelete.setOnClickListener(deleteClickListener);

        rearrangement();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 678:
                new AlertDialog.Builder(PaymentPayPasswordActivity.this)
                        .setTitle("잘못된 접근입니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(PaymentPayPasswordActivity.this, JiseokMainActivity.class), 678);
                            }
                        })
                        .show();
                break;
        }
    }


    View.OnClickListener numClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (result.length() < 7) {
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
           
            resultToStar();

            switch (result.length()) {
                case 7:
                    payPassword1 = result.substring(1, 7);

                    if (payPassword1.equals(selectPwd())) {

                        // 결제 성공 디비액션
                        connectOrderInsert();
                        connectOrderNumber();

                        String C = urlAddress2.substring(0,urlAddress2.indexOf("=",1)+1);
                        String D =  urlAddress2.substring(urlAddress2.indexOf("&",1),urlAddress2.length());

                        String FF = C + oNo + D;




                        if(dataSize != 0){
                            Intent intent = getIntent();
                            urls = intent.getStringArrayListExtra("urls");

                            for(int i = 0; i < dataSize; i++){
                                String A = urls.get(i).substring(0,urls.get(i).indexOf("=",1)+1);
                                String B = urls.get(i).substring(urls.get(i).indexOf("&",1),urls.get(i).length());
                                String AA = A + oNo + B;

                                connectOrderListInsert(AA);
                            }
                        }else{
                            connectOrderListInsert(FF);
                        }

                        new AlertDialog.Builder(PaymentPayPasswordActivity.this)
                                .setTitle("결제가 완료되었습니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 결제완료창으로 이동
                                        Intent intent = new Intent(PaymentPayPasswordActivity.this, Payment_resultActivity.class);
                                        intent.putExtra("oNo",oNo);

                                        intent.putExtra("point",point);
                                        intent.putExtra("store_sSeqNo", store_SeqNo);
                                        if(dataSize != 0){
                                            intent.putExtra("from", "BeforePayActivity2");
                                        }else{
                                            intent.putExtra("from", "BeforePayActivity");
                                        }
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    } else {

                        rearrangement();
                        count++;
                        result = "0";
                        resultToStar();
                        tvComment.setVisibility(View.VISIBLE);
                        tvCount.setVisibility(View.VISIBLE);
                        tvCount.setText("틀린횟수 (" + count + "/5)");


                        // 5번틀리면 초기화
                        switch (count) {
                            case 5:
                                new AlertDialog.Builder(PaymentPayPasswordActivity.this)
                                        .setTitle("비밀번호가 초기화 되었습니다\n 비밀번호를 다시 설정해 주세요.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                tvComment.setVisibility(View.INVISIBLE);
                                                tvCount.setVisibility(View.INVISIBLE);
                                                rearrangement();
                                                payCheck=0;
                                                count=0;
                                                result="0";
                                                resultToStar();
                                                updatePwd();
                                                startActivity(new Intent(PaymentPayPasswordActivity.this, MypageActivity.class));

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
    private void resultToStar() {
        // 색 없는 별로 전체 초기화
        for (int i = 0; i < 6; i++) {
            img[i].setImageResource(R.raw.dot02);
        }
        // 비밀번호 길이만큼 별 색칠
        for (int i = 0; i < result.length() - 1; i++) {
            img[i].setImageResource(R.raw.dot01);
        }
    }


    // 재배열
    private void rearrangement() {
        rand();
        setBtn();
    }

    //0~9 중복없이 뽑기
    public void rand() {
        String[] str = new String[10];
        for (int i = 0; i < 10; i++) {
            str[i] = Integer.toString(random.nextInt(10));
            for (int j = 0; j < i; j++) {
                if (str[i].equals(str[j])) {
                    i--;
                    break;
                }
            }
        }
        btnNum = str;
    }

    //버튼에 숫자넣기
    private void setBtn() {
        for (int i = 0; i < 10; i++) {
            btn[i].setText(btnNum[i]);
        }
    }

    //디비에 결제비밀번호 받아오기
    private String selectPwd() {
        String Pwd = "null";

        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/cjs_MyPageSelectPwd.jsp?uNo=" + userSeq;
            Log.v("dd", urlAddr);
            CJS_NetworkTask_Mypage cjs_networkTask = new CJS_NetworkTask_Mypage(PaymentPayPasswordActivity.this, urlAddr, "selectPayPassword");
            Object obj = cjs_networkTask.execute().get();

            Pwd = (String) obj;
            Log.v("내비밀번호는 : ", Pwd);

        } catch (Exception e) {

        }
        return Pwd;
    }


    //결제 네트워크 타스크  -> order  순서 1
    @SuppressLint("LongLogTag")
    private String connectOrderInsert() {

        urlAddress = urlAddr2 + "&user_uNo=" + userSeq + "&storekeeper_skSeqNo=" + store_SeqNo + "&store_sName=" + sName
                + "&oSum=" + totalPrice + "&oCardName=" + cCardCompany + "&oCardNo=" + card_cNo;
        Log.v("^^", urlAddress);

        String result = null;
        try {
            NetworkTask_TaeHyun insertNetworkTask = new NetworkTask_TaeHyun(PaymentPayPasswordActivity.this, urlAddress, "insert");
            Object obj = insertNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }


    // orderlist 테이블 Insert
    //결제 네트워크 타스크  --> order -> orderlist 순서 2

    private String connectOrderListInsert(String orderlistUrl) {
        String result = null;
        try {
            NetworkTask_TaeHyun insertNetworkTask = new NetworkTask_TaeHyun(PaymentPayPasswordActivity.this, orderlistUrl, "insert");
            Object obj = insertNetworkTask.execute().get();
            result = (String) obj;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    //oNo불러오기
    private void connectOrderNumber() {

        try {
            //임시값
            String urlAddr = "http://" + MacIP + ":8080/tify/order_numberselect.jsp?";

            String urlAddress = urlAddr + "user_uNo=" + userSeq;
            Log.v("dddd","dd"+urlAddress);
            NetworkTask_TaeHyun myPageNetworkTask = new NetworkTask_TaeHyun(PaymentPayPasswordActivity.this, urlAddress, "selectOrderNumber");
            Object obj = myPageNetworkTask.execute().get();
            order = (Order) obj;

            //DB
            oNo = order.getoNo();

            Log.v(TAG, "oNo : " + oNo);
            store_SeqNo = order.getStore_sSeqno();
            sName = order.getStore_sName();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePwd(){
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/cjs_MyPageUpdatePwd.jsp?uNo="+userSeq+"&uPayPassword=null";
            Log.v("왜안돼",urlAddr);
            CJS_NetworkTask_Mypage cjs_networkTask = new CJS_NetworkTask_Mypage(PaymentPayPasswordActivity.this, urlAddr, "updatePwd");
            cjs_networkTask.execute().get();

        }catch (Exception e){

        }
    }
}



