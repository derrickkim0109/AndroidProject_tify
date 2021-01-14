package com.example.tify.Hyeona.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.tify.Hyeona.Adapter.search_storeAdapter;
import com.example.tify.Hyeona.Bean.Bean_main_search;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_mainsearch;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.util.ArrayList;


public class main_search extends AppCompatActivity {
    private ImageView main_img_SearchBtn;
    private EditText main_SearchText;
    LinearLayout ll_hide;
    InputMethodManager inputMethodManager ;
    int userSeq;
    private ArrayList<Bean_main_search> searchs = null;
    String userNickName;
    String myLocation;

    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();
    String search_text;

    private search_storeAdapter adapter;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_main_search);

        //키보드 화면 터치시 숨기기위해 선언.
        ll_hide = findViewById(R.id.detail_ll_hide);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  //OS에서 지원해주는 메소드이다.
        //키보드 화면 터치시 숨김.
        ll_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(ll_hide.getWindowToken(),0);
            }
        });
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();

        //userSeq = auto.getInt("userSeq", 0);
        userNickName = auto.getString("userNickName", null);
        myLocation = auto.getString("myLocation", null);

        main_SearchText = findViewById(R.id.main_SearchText);
        main_img_SearchBtn = findViewById(R.id.main_img_SearchBtn);

        main_img_SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(main_SearchText.getText().length()==0){
                    new AlertDialog.Builder(main_search.this)
                            .setTitle("검색어를 입력하세요")
                            .setPositiveButton("확인",null)
                            .show();
                    main_SearchText.requestFocus();
                }else{
                    search_text = String.valueOf(main_SearchText.getText());
                    connectGetData(search_text);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.cha_custom_actionbar, null);

        actionBar.setCustomView(actionbar);
        TextView title = findViewById(R.id.title);
        title.setText("");

        ImageButton cart = findViewById(R.id.cart);
        cart.setVisibility(View.INVISIBLE);
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

    @Override
    protected void onResume()
    {
        super.onResume();
    }
    private void connectGetData(String s){
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/maincafe_search.jsp?search="+search_text;
            //여기 변경 포인트
            CUDNetworkTask_mainsearch CUDNetworkTask_mainsearch = new CUDNetworkTask_mainsearch(urlAddr,"search_select");
            Object obj = CUDNetworkTask_mainsearch.execute().get();
            searchs = (ArrayList<Bean_main_search>) obj;

            recyclerView = findViewById(R.id.search_store);
            recyclerView.setHasFixedSize(true);
            //레이아웃 매니저 만들기
            //layoutManager = new LinearLayoutManager(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);

            recyclerView.setLayoutManager(gridLayoutManager);
            adapter = new search_storeAdapter(main_search.this, R.layout.cha_maincafe_content,searchs,myLocation);
            //어댑터에게 보내기
            recyclerView.setAdapter(adapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
