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

import com.android.tify_store.Minwoo.Bean.Store;
import com.android.tify_store.Minwoo.NetworkTask.LMW_MenuNetworkTask;
import com.android.tify_store.Minwoo.NetworkTask.LMW_StoreNetworkTask;
import com.android.tify_store.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuUpdateActivity extends AppCompatActivity {

    String TAG = "MenuUpdateActivity";

    ArrayList<com.android.tify_store.Minwoo.Bean.Menu> menus;
    ArrayList<com.android.tify_store.Minwoo.Bean.Menu> list;

    // DB Connect
    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo;
    int mNo;
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
    Button btn_Update = null;

    int typeRGCheck = 0;
    int sizeUpRGCheck = 1;
    int addShotRGCheck = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_menu_update);

        SharedPreferences sharedPreferences = getSharedPreferences("openStatus", MODE_PRIVATE);
        strResult = sharedPreferences.getString("openResult", "null");
        Log.v(TAG, "오픈 상태 : " + strResult);

        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        skSeqNo = intent.getIntExtra("skSeqNo", 0);
        mNo = intent.getIntExtra("mNo", 0);


        // layout 설정
        civ_mImage = findViewById(R.id.activity_MenuUpdate_CIV_mImage);
        et_mName = findViewById(R.id.activity_MenuUpdate_ET_mName);
        et_mPrice = findViewById(R.id.activity_MenuUpdate_ET_mPrice);
        et_mCommnet = findViewById(R.id.activity_MenuUpdate_ET_mComment);
        radioGroup = (RadioGroup)findViewById(R.id.activity_MenuUpdate_RG_RadioGroup);
        rb_Drink = findViewById(R.id.activity_MenuUpdate_RB_Drink);
        rb_Dessert = findViewById(R.id.activity_MenuUpdate_RB_Other);
        radioGroup_SizeUP = (RadioGroup)findViewById(R.id.activity_MenuUpdate_RG_RadioGroupSizeUp);
        rb_SizeUpOk = findViewById(R.id.activity_MenuUpdate_RB_SizeUpOK);
        rb_SizeUpNo = findViewById(R.id.activity_MenuUpdate_RB_SizeUpNO);
        radioGroup_AddShot = (RadioGroup)findViewById(R.id.activity_MenuUpdate_RG_RadioGroupAddShot);
        rb_AddShotOk = findViewById(R.id.activity_MenuUpdate_RB_AddShotOK);
        rb_AddShotNo = findViewById(R.id.activity_MenuUpdate_RB_AddShotNo);
        btn_Update = findViewById(R.id.activity_MenuUpdate_Btn_Update);

        // 데이터 불러오기
        list = new ArrayList<com.android.tify_store.Minwoo.Bean.Menu>();
        list = connectGetData(); // 데이터 불러오기

        if (list.size() == 0){ // 등록된 데이터가 없다

        }else{ // 있다

            civ_mImage.setImageResource(R.drawable.americano); // 이미지 받기
            et_mName.setText(list.get(0).getmName());
            et_mPrice.setText(Integer.toString(list.get(0).getmPrice()));
            et_mCommnet.setText(list.get(0).getmComment());

            Log.v(TAG, "타입 " + list.get(0).getmType() + "");
            Log.v(TAG, "사이즈업 " + list.get(0).getmSizeUp() + "");
            Log.v(TAG, "샷추가 " + list.get(0).getmShut()+ "");

            if(list.get(0).getmType() == 0){ // 메뉴는 0이면 음료, 1이면 디저트
                rb_Drink.setChecked(true);
            }else{
                rb_Dessert.setChecked(true);
            }
            if(list.get(0).getmSizeUp() == 0){ // 사이즈업은 0이면 불가, 1이면 가능
                rb_SizeUpNo.setChecked(true);
            }else{
                rb_SizeUpOk.setChecked(true);
            }
            if(list.get(0).getmShut() == 0){ // 샷추가는 0이면 불가, 1이면 가능
                rb_AddShotNo.setChecked(true);
            }else{
                rb_AddShotOk.setChecked(true);
            }

            btn_Update.setText("수정");

        }

        // 클릭 리스너
        btn_Update.setOnClickListener(mClickListener);
        civ_mImage.setOnClickListener(mClickListener);

        // 라디오 그룹 설정
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        radioGroup_SizeUP.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        radioGroup_AddShot.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.lmw_menuupdate_toolbar); // 상단 툴바
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
                Intent intent = new Intent(MenuUpdateActivity.this, MenuListActivity.class);

                intent.putExtra("macIP", macIP);
                intent.putExtra("skSeqNo", skSeqNo);

                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String connectMenuUpdate(){ // 수정
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
            LMW_MenuNetworkTask networkTask = new LMW_MenuNetworkTask(MenuUpdateActivity.this, urlAddr, where);
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

    private ArrayList<com.android.tify_store.Minwoo.Bean.Menu> connectGetData(){
        ArrayList<com.android.tify_store.Minwoo.Bean.Menu> beanList = new ArrayList<com.android.tify_store.Minwoo.Bean.Menu>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_menulist_one.jsp?mNo=" + mNo;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_MenuNetworkTask networkTask = new LMW_MenuNetworkTask(MenuUpdateActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            menus = (ArrayList<com.android.tify_store.Minwoo.Bean.Menu>) obj;
            Log.v(TAG, "stores.size() : " + menus.size());

            beanList = menus;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }
    View.OnClickListener mClickListener = new View.OnClickListener() {

        String mName = null;
        String mPrice = null;
        String mImage = "2312323"; // 이미지값 받기
        String mComment = null;

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activity_MenuUpdate_CIV_mImage: // 사진추가

                    break;
                case R.id.activity_MenuUpdate_Btn_Update: // 입력

                    mName = et_mName.getText().toString();
                    mPrice = et_mPrice.getText().toString();
                    mComment = et_mCommnet.getText().toString();

                    if(mName.length() == 0 || mPrice.length() == 0 || mComment.length() == 0){
                        Toast.makeText(MenuUpdateActivity.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        where = "update";
                        urlAddr = "http://" + macIP + ":8080/tify/lmw_menu_update.jsp?mNo=" + mNo + "&mName=" + mName + "&mPrice=" + mPrice + "&mSizeUp=" + sizeUpRGCheck + "&mShut=" + addShotRGCheck + "&mImage=" + mImage + "&mType=" + typeRGCheck + "&mComment=" + mComment;

                        CResult = connectMenuUpdate();

                        if(CResult.equals("1")){
                            Toast.makeText(MenuUpdateActivity.this, "메뉴 수정 성공!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MenuUpdateActivity.this, MenuListActivity.class);
                            intent.putExtra("macIP", macIP);
                            intent.putExtra("skSeqNo", skSeqNo);
                            startActivity(intent);

                            finish();
                        }else{
                            Toast.makeText(MenuUpdateActivity.this, "메뉴 수정 실패! \n관리자에게 연락바랍니다.", Toast.LENGTH_SHORT).show();

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
                case R.id.activity_MenuUpdate_RB_Drink:
                    typeRGCheck = 0;
                    break;
                case R.id.activity_MenuUpdate_RB_Other:
                    typeRGCheck = 1;
                    break;
                case R.id.activity_MenuUpdate_RB_SizeUpOK:
                    sizeUpRGCheck = 1;
                    break;
                case R.id.activity_MenuUpdate_RB_SizeUpNO:
                    sizeUpRGCheck = 0;
                    break;
                case R.id.activity_MenuUpdate_RB_AddShotOK:
                    addShotRGCheck = 1;
                    break;
                case R.id.activity_MenuUpdate_RB_AddShotNo:
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