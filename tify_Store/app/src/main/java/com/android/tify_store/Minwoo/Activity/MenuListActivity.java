package com.android.tify_store.Minwoo.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.tify_store.Minwoo.Adapter.MenuListAdapter;
import com.android.tify_store.Minwoo.Bean.Menu;
import com.android.tify_store.Minwoo.NetworkTask.ImageNetworkTask_TaeHyun;
import com.android.tify_store.Minwoo.NetworkTask.LMW_MenuNetworkTask;
import com.android.tify_store.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuListActivity extends AppCompatActivity {

    // 메뉴 리스트 화면

    String TAG = "MenuListActivity";

    private ArrayList<Menu> menuList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MenuListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager = null;

    private ArrayList<Menu> list;

    // 이미지
    //image_server
    //intent로 받을 이미지 DB (Mypage에서)
    String mImage = null;
    String imageurl = null;
    //카메라, 갤러리
    private final int REQ_CODE_SELECT_IMAGE = 300; // Gallery Return Code

    String imgName = null;
    ///////////
    String img_path = null;// 최종 file name
    String f_ext = null;
    File tempSelectFile;

    String devicePath = Environment.getDataDirectory().getAbsolutePath() + "/data/com.android.tify/";
    //// 외부쓰레드 에서 메인 UI화면을 그릴때 사용

    // DB Connect
    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo;
    int skStatus = 0;
    String strResult = null;
    String UDResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_menu_list);

        ActivityCompat.requestPermissions(MenuListActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        SharedPreferences sharedPreferences = getSharedPreferences("openStatus", MODE_PRIVATE);
        strResult = sharedPreferences.getString("openResult", "null");
        Log.v(TAG, "오픈 상태 : " + strResult);

        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        skSeqNo = intent.getIntExtra("skSeqNo", 0);

        //리사이클러뷰에 있는 아이디를 찾기
        recyclerView = findViewById(R.id.menulist_recycler_view);

        //리사이클러뷰 규격 만들기
        recyclerView.setHasFixedSize(true);

        //레이아웃 매니저 만들기
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 기본 구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 데이터 불러오기
        list = new ArrayList<Menu>();
        list = connectGetData(); // 데이터 불러오기

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.lmw_menulist_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()){
                    case R.id.toolbar_Menu_Add: //메뉴 입력

                        intent = new Intent(MenuListActivity.this, MenuInsertActivity.class);

                        intent.putExtra("macIP", macIP);
                        intent.putExtra("skSeqNo", skSeqNo);

                        startActivity(intent);

                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) { // 툴바 메뉴 생성
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_storeinfo_open_toolbar, menu);
        menuInflater.inflate(R.menu.menu_menulist_insert_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(MenuListActivity.this, MainActivity.class);

                intent.putExtra("macIP", macIP);
                intent.putExtra("skSeqNo", skSeqNo);

                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Menu> connectGetData(){ // READ
        ArrayList<Menu> beanList = new ArrayList<Menu>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_menulist.jsp?store_sSeqNo=" + skSeqNo;
        Log.v(TAG, "urlAddr : " + urlAddr);

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_MenuNetworkTask networkTask = new LMW_MenuNetworkTask(MenuListActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            menuList = (ArrayList<Menu>) obj;
            Log.v(TAG, "menuList.size() : " + menuList.size());

            mAdapter = new MenuListAdapter(MenuListActivity.this, R.layout.lmw_activity_menulist_recycler_item, menuList, macIP);
            recyclerView.setAdapter(mAdapter);

            beanList = menuList;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }



    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        Log.d("test", "onPrepareOptionsMenu - 옵션메뉴가 " +
                "화면에 보여질때 마다 호출됨");

        if(strResult.equals("1")){
            Log.v(TAG, "오픈 상태 : 오픈");
            menu.getItem(0).setIcon(R.drawable.ic_action_storeopen_open);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAdapter.setOnItemClickListener(new MenuListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) { // 아이템을 클릭했을 경우

                AlertDialog.Builder builder = new AlertDialog.Builder(MenuListActivity.this);
                builder.setTitle("<메뉴 편집>");
                builder.setMessage("원하시는 기능을 눌러주세요.");
                builder.setNeutralButton("삭제", new DialogInterface.OnClickListener() { // 해당 메뉴 장바구니에서 삭제 클릭
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // 삭제를 눌렀을 경우 다시 한번 확인

                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuListActivity.this);
                        builder.setTitle("<메뉴 삭제>");
                        builder.setMessage("정말 삭제하시겠습니까?");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                urlAddr = "http://" + macIP + ":8080/tify/lmw_menu_delete.jsp?mNo=" + list.get(position).getmNo();
                                where = "delete";

                                UDResult = connectMenuUD(); // 해당 메뉴 삭제

                                if(UDResult.equals("1")){
                                    Intent intent = new Intent(MenuListActivity.this, MenuListActivity.class);

                                    intent.putExtra("macIP", macIP);
                                    intent.putExtra("skSeqNo", skSeqNo);

                                    startActivity(intent);

                                    Toast.makeText(MenuListActivity.this, "선택하신 메뉴가 삭제되었습니다.", Toast.LENGTH_SHORT).show(); // 정상 삭제 확인
                                }else{
                                    Toast.makeText(MenuListActivity.this, "오류 발생! \n관리자에게 연락바랍니다.", Toast.LENGTH_SHORT).show(); // 삭제 중 오류 처리
                                }
                            }
                        });

                        builder.setNegativeButton("취소", null);
                        builder.create().show();
                    }
                });
                builder.setNegativeButton("수정", new DialogInterface.OnClickListener() { // 수정 버튼 클릭
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(MenuListActivity.this, MenuUpdateActivity.class);

                        intent.putExtra("macIP", macIP);
                        intent.putExtra("skSeqNo", skSeqNo);
                        intent.putExtra("mNo", list.get(position).getmNo());
                        intent.putExtra("mImage", list.get(position).getmImage());

                        startActivity(intent);

                    }
                });
                builder.setPositiveButton("취소", null);
                builder.create().show();
            }
        });
    }

    private String connectMenuUD(){ // 수정, 삭제
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
            LMW_MenuNetworkTask networkTask = new LMW_MenuNetworkTask(MenuListActivity.this, urlAddr, where);
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

}