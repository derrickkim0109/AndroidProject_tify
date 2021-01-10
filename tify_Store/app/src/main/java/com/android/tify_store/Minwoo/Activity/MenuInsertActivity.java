package com.android.tify_store.Minwoo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.tify_store.Minwoo.NetworkTask.LMW_MenuNetworkTask;
import com.android.tify_store.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuInsertActivity extends AppCompatActivity {

    String TAG = "MenuInsertActivity";

    // DB Connect
    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo;
    String strResult = null;
    String CResult = null;

    // layout
    CircleImageView civ_mImage;
    EditText et_mName;
    EditText et_mPrice;
    EditText et_mCommnet;
    RadioButton rb_SizeUpOk = null;
    RadioButton rb_SizeUpNo = null;
    RadioGroup radioGroup_SizeUP = null;
    RadioButton rb_AddShotOk = null;
    RadioButton rb_AddShotNo = null;
    RadioGroup radioGroup_AddShot = null;
    RadioButton rb_Drink = null;
    RadioButton rb_Dessert = null;
    RadioGroup radioGroup = null;
    Button btn_Insert = null;

    int typeRGCheck = 0;
    int sizeUpRGCheck = 1;
    int addShotRGCheck = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_menu_insert);

        SharedPreferences sharedPreferences = getSharedPreferences("openStatus", MODE_PRIVATE);
        strResult = sharedPreferences.getString("openResult", "null");
        Log.v(TAG, "오픈 상태 : " + strResult);

        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        skSeqNo = intent.getIntExtra("skSeqNo", 0);

        // layout 설정
        civ_mImage = findViewById(R.id.activity_MenuInsert_CIV_mImage);
        et_mName = findViewById(R.id.activity_MenuInsert_ET_mName);
        et_mPrice = findViewById(R.id.activity_MenuInsert_ET_mPrice);
        et_mCommnet = findViewById(R.id.activity_MenuInsert_ET_mComment);
        radioGroup = (RadioGroup)findViewById(R.id.activity_MenuInsert_RG_RadioGroup);
        rb_Drink = findViewById(R.id.activity_MenuInsert_RB_Drink);
        rb_Dessert = findViewById(R.id.activity_MenuInsert_RB_Other);
        radioGroup_SizeUP = (RadioGroup)findViewById(R.id.activity_MenuInsert_RG_RadioGroupSizeUp);
        rb_SizeUpOk = findViewById(R.id.activity_MenuInsert_RB_SizeUpOK);
        rb_SizeUpNo = findViewById(R.id.activity_MenuInsert_RB_SizeUpNO);
        radioGroup_AddShot = (RadioGroup)findViewById(R.id.activity_MenuInsert_RG_RadioGroupAddShot);
        rb_AddShotOk = findViewById(R.id.activity_MenuInsert_RB_AddShotOK);
        rb_AddShotNo = findViewById(R.id.activity_MenuInsert_RB_AddShotNo);
        btn_Insert = findViewById(R.id.activity_MenuInsert_Btn_Insert);

        // 클릭 리스너
        btn_Insert.setOnClickListener(mClickListener);
        civ_mImage.setOnClickListener(mClickListener);

        // 라디오 그룹 설정
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        radioGroup_SizeUP.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        radioGroup_AddShot.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.lmw_menuInsert_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 툴바 메뉴 생성

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_storeinfo_open_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(MenuInsertActivity.this, MenuListActivity.class);

                intent.putExtra("macIP", macIP);
                intent.putExtra("skSeqNo", skSeqNo);

                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String connectMenuInsert(){ // 입력
        String result = null;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where <- "insert"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_MenuNetworkTask networkTask = new LMW_MenuNetworkTask(MenuInsertActivity.this, urlAddr, where);
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
    View.OnClickListener mClickListener = new View.OnClickListener() {

        String mName = null;
        String mPrice = null;
        String mImage = "2312323"; // 이미지값 받기
        String mComment = null;

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activity_MenuInsert_CIV_mImage: // 사진추가

                    break;
                case R.id.activity_MenuInsert_Btn_Insert: // 입력

                    mName = et_mName.getText().toString();
                    mPrice = et_mPrice.getText().toString();
                    mComment = et_mCommnet.getText().toString();

                    if(mName.length() == 0 || mPrice.length() == 0 || mComment.length() == 0){
                        Toast.makeText(MenuInsertActivity.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        where = "insert";
                        urlAddr = "http://" + macIP + ":8080/tify/lmw_menu_insert.jsp?storekeeper_skSeqNo=" + skSeqNo + "&mName=" + mName + "&mPrice=" + mPrice + "&mSizeUp=" + sizeUpRGCheck + "&mShut=" + addShotRGCheck + "&mImage=" + mImage + "&mType=" + typeRGCheck + "&mComment=" + mComment;

                        CResult = connectMenuInsert();

                        if(CResult.equals("1")){
                            Toast.makeText(MenuInsertActivity.this, "메뉴 등록 성공!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MenuInsertActivity.this, MenuListActivity.class);
                            intent.putExtra("macIP", macIP);
                            intent.putExtra("skSeqNo", skSeqNo);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MenuInsertActivity.this, "메뉴 등록 실패! \n관리자에게 연락바랍니다.", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
            }

        }
    };

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.v(TAG, "버튼 클릭 : " + checkedId);

            RadioButton radio_btn = (RadioButton) findViewById(checkedId);
//            Toast.makeText(MenuInsertActivity.this, radio_btn.getText().toString(), Toast.LENGTH_SHORT).show();

            switch (checkedId) {
                case R.id.activity_MenuInsert_RB_Drink:
                    typeRGCheck = 0;
                    break;
                case R.id.activity_MenuInsert_RB_Other:
                    typeRGCheck = 1;
                    break;
                case R.id.activity_MenuInsert_RB_SizeUpOK:
                    sizeUpRGCheck = 1;
                    break;
                case R.id.activity_MenuInsert_RB_SizeUpNO:
                    sizeUpRGCheck = 0;
                    break;
                case R.id.activity_MenuInsert_RB_AddShotOK:
                    addShotRGCheck = 1;
                    break;
                case R.id.activity_MenuInsert_RB_AddShotNo:
                    addShotRGCheck = 0;
                    break;

            }
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("test", "onPrepareOptionsMenu - 옵션메뉴가 " +
                "화면에 보여질때 마다 호출됨");

        if(strResult.equals("1")){
            Log.v(TAG, "오픈 상태 : 오픈");
            menu.getItem(0).setIcon(R.drawable.ic_action_storeopen_open);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}