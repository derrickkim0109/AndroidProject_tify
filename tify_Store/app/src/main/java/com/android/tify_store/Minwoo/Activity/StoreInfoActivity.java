package com.android.tify_store.Minwoo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tify_store.Minwoo.Bean.Store;
import com.android.tify_store.Minwoo.NetworkTask.LMW_StoreNetworkTask;
import com.android.tify_store.R;

import java.util.ArrayList;

public class StoreInfoActivity extends AppCompatActivity {

    String TAG = "StoreInfoActivity";

    // DB Connect
    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo;
    int skStatus = 0;
    String strResult = null;

    ArrayList<Store> stores;
    ArrayList<Store> list;

    // layout
    ImageView iv_storePhoto;
    TextView tv_storeName;
    TextView tv_storeAddress;
    EditText et_storeName;
    EditText et_storeAddress;
    EditText et_storeTel;
    EditText et_storeTime;
    EditText et_storeComment;
    RadioButton rb_packaging = null;
    RadioButton rb_all = null;
    RadioGroup radioGroup = null;
    Button btn_Insert = null;

    int rgCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_store_info);

        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        skSeqNo = intent.getIntExtra("skSeqNo", 0);

        // layout 설정
        iv_storePhoto = findViewById(R.id.activity_StoreInfo_IV_StorePhoto);
        tv_storeName = findViewById(R.id.activity_StoreInfo_TV_sName);
        tv_storeAddress = findViewById(R.id.activity_StoreInfo_TV_sAddress);
        et_storeName = findViewById(R.id.activity_StoreInfo_ET_StoreName);
        et_storeAddress = findViewById(R.id.activity_StoreInfo_ET_StoreAddress);
        et_storeTel = findViewById(R.id.activity_StoreInfo_ET_StoreTel);
        et_storeComment = findViewById(R.id.activity_StoreInfo_ET_StoreComment);
        et_storeTime = findViewById(R.id.activity_StoreInfo_ET_StoreTime);
        radioGroup = (RadioGroup)findViewById(R.id.activity_StoreInfo_RG_StorePackaging);
        rb_packaging = findViewById(R.id.activity_StoreInfo_RB_OnlyPackaging);
        rb_all = findViewById(R.id.activity_StoreInfo_RB_All);
        btn_Insert = findViewById(R.id.activity_StoreInfo_Btn_Insert);

        // 데이터 불러오기
        list = new ArrayList<Store>();
        list = connectGetData(); // 데이터 불러오기

        if (list.size() == 0){ // 등록된 데이터가 없다

        }else{ // 있다

            iv_storePhoto.setImageResource(R.drawable.starbucks); // 이미지 받기
            tv_storeName.setText(list.get(0).getsName());
            tv_storeAddress.setText(list.get(0).getsAddress());
            et_storeName.setText(list.get(0).getsName());
            et_storeAddress.setText(list.get(0).getsAddress());
            et_storeTel.setText(list.get(0).getsTelNo());
            et_storeComment.setText(list.get(0).getsComment());
            et_storeTime.setText(list.get(0).getsRunningTime());

            btn_Insert.setText("수정");

        }

        // 클릭 리스너
        btn_Insert.setOnClickListener(mClickListener);
        iv_storePhoto.setOnClickListener(mClickListener);

        // 라디오 그룹 설정
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.lmw_storeInfo_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        SharedPreferences sharedPreferences = getSharedPreferences("openStatus", MODE_PRIVATE);
        strResult = sharedPreferences.getString("openResult", "null");
        Log.v(TAG, "오픈 상태 : " + strResult);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()){
                    case R.id.toolbar_storeInfo_open: // 매장 오픈
                        Log.v(TAG, "매장오픈");

                        if(strResult.equals("null")){ // 오픈한 적이 없을 때
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(StoreInfoActivity.this);
                            builder1.setTitle("<매장 오픈>");
                            builder1.setMessage("확인을 누르시면 영업이 시작됩니다. \n시작하시겠습니까?");
                            builder1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    urlAddr = "http://" + macIP + ":8080/tify/lmw_storekeeper_update.jsp?skSeqNo=" + skSeqNo + "&skStatus=" + skStatus;
                                    where = "update";
                                    strResult = connectStore();
                                    Log.v(TAG, "strResult : " + strResult);
                                    Toast.makeText(StoreInfoActivity.this, "매장을 오픈했습니다!", Toast.LENGTH_SHORT).show();

                                    item.setIcon(R.drawable.ic_action_storeopen_open);
                                }
                            });
                            builder1.setNegativeButton("취소", null);
                            builder1.create().show();


                        }else{ // 이미 오픈했을 때

                            Toast.makeText(StoreInfoActivity.this, "이미 오픈되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.toolbar_storeInfo_edit:
                        et_storeName.setEnabled(true);
                        et_storeAddress.setEnabled(true);
                        et_storeTel.setEnabled(true);
                        et_storeComment.setEnabled(true);
                        et_storeTime.setEnabled(true);
                        rb_packaging.setEnabled(true);
                        rb_all.setEnabled(true);
                        iv_storePhoto.setClickable(true);
                        btn_Insert.setEnabled(true);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 툴바 메뉴 생성

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_storeinfo_edit_toolbar, menu);
        menuInflater.inflate(R.menu.menu_storeinfo_open_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(StoreInfoActivity.this, MainActivity.class);

                intent.putExtra("macIP", macIP);
                intent.putExtra("skSeqNo", skSeqNo);

                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String connectStore(){ // skStatus 1로 바꾸기
        String result = null;
        skStatus = 1;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where <- "insert"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_StoreNetworkTask networkTask = new LMW_StoreNetworkTask(StoreInfoActivity.this, urlAddr, where);
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

        String sName = null;
        String sTelNo = null;
        String sRunningTime = null;
        String sAddress = null;
        String sImage = "2312323"; // 이미지값 받기
        int sPackaging = -1;
        String sComment = null;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StoreInfoActivity.this, StoreInfoActivity.class);

            switch (v.getId()){
                case R.id.activity_StoreInfo_Btn_Insert:

                    if(btn_Insert.getText().toString().equals("입력")){ // 처음 입력할 경우
                        sName = et_storeName.getText().toString();
                        sAddress = et_storeAddress.getText().toString();
                        sTelNo = et_storeTel.getText().toString();
                        sComment = et_storeComment.getText().toString();
                        sRunningTime = et_storeTime.getText().toString();

                        urlAddr = "http://" + macIP + ":8080/tify/lmw_store_insert.jsp?storekeeper_skSeqNo=" + skSeqNo + "&sName=" + sName + "&sTelNo=" + sTelNo + "&sRunningTime=" + sRunningTime + "&sAddress=" + sAddress + "&sImage=" + sImage + "&sPackaging=" + rgCheck + "&sComment=" + sComment;
                        where = "insert";
                        strResult = connectStore();

                        Toast.makeText(StoreInfoActivity.this, "입력 완료!", Toast.LENGTH_SHORT).show();

                        intent.putExtra("macIP", macIP);
                        intent.putExtra("skSeqNo", skSeqNo);
                        startActivity(intent);

                    }else { // 수정일 경우
                        sName = et_storeName.getText().toString();
                        sAddress = et_storeAddress.getText().toString();
                        sTelNo = et_storeTel.getText().toString();
                        sComment = et_storeComment.getText().toString();
                        sRunningTime = et_storeTime.getText().toString();

                        urlAddr = "http://" + macIP + ":8080/tify/lmw_store_update.jsp?storekeeper_skSeqNo=" + skSeqNo + "&sName=" + sName + "&sTelNo=" + sTelNo + "&sRunningTime=" + sRunningTime + "&sAddress=" + sAddress + "&sImage=" + sImage + "&sPackaging=" + rgCheck + "&sComment=" + sComment;
                        where = "update";
                        strResult = connectStore();

                        Toast.makeText(StoreInfoActivity.this, "수정 완료!", Toast.LENGTH_SHORT).show();

                        intent.putExtra("macIP", macIP);
                        intent.putExtra("skSeqNo", skSeqNo);
                        startActivity(intent);

                    }
                    break;

                case R.id.activity_StoreInfo_IV_StorePhoto: // 사진 선택했을 경우 수정

                    break;

            }

        }
    };

    private ArrayList<Store> connectGetData(){
        ArrayList<Store> beanList = new ArrayList<Store>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_store_select.jsp?storekeeper_skSeqNo=" + skSeqNo;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_StoreNetworkTask networkTask = new LMW_StoreNetworkTask(StoreInfoActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            stores = (ArrayList<Store>) obj;
            Log.v(TAG, "stores.size() : " + stores.size());

            beanList = stores;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.v(TAG, "버튼 클릭 : " + checkedId);

            RadioButton radio_btn = (RadioButton) findViewById(checkedId);
				Toast.makeText(StoreInfoActivity.this, radio_btn.getText().toString(), Toast.LENGTH_SHORT).show();

				switch (checkedId) {
				case R.id.activity_StoreInfo_RB_OnlyPackaging:
				    rgCheck = 0;
					break;
				case R.id.activity_StoreInfo_RB_All:
				    rgCheck = 1;
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
            menu.getItem(1).setIcon(R.drawable.ic_action_storeopen_open);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("openStatus",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("openResult", strResult);
        editor.commit();

        Log.v(TAG, "onPause : " + strResult);
    }
}