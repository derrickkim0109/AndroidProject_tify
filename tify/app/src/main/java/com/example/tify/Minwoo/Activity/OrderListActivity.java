
package com.example.tify.Minwoo.Activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.Minwoo.Adapter.CartAdapter;
import com.example.tify.Minwoo.Adapter.OrderListAdapter;
import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.Minwoo.NetworkTask.LMW_CartNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderNetworkTask;
import com.example.tify.R;
import com.example.tify.ShareVar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderListActivity extends AppCompatActivity {

    String TAG = "OrderListActivity";

    // 통신
    private Handler mHandler;
    InetAddress serverAddr;
    Socket socket;
    PrintWriter sendWriter;
    private String ip = "211.195.53.163";
    private int port = 8888;
    String strStatus = null;

    private ArrayList<Order> data = null;
    private OrderListAdapter adapter = null;
    private ListView listView = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;

    private ArrayList<Order> list;

    String macIP;
    String urlAddr = null;
    String where = null;
    int user_uSeqNo = 0;
    String from;

    TextView tv_UserName;

    int store_sSeqNo = 0;
    String sName;
    String userEmail;
    String sAddress;
    String sImage;
    String sTime = null;
    String sTelNo;
    int sPackaging;
    String sComment;
    int skStatus;
    String userName;

    // 통신 ------------- 소켓 닫는 거
//    @Override
//    protected void onStop() {
//        super.onStop();
//        try {
//            if(from.equals("BeforePayActivity")){
//                sendWriter.close();
//                socket.close();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    // -----------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_order_list);
        Log.v(TAG, "OrderListActivity onCreate");

        // 받은 값 저장
        Intent intent = getIntent();
        ShareVar shareVar = new ShareVar();
        macIP = shareVar.getMacIP();
        store_sSeqNo = intent.getIntExtra("store_sSeqNo", 0);
        Log.v(TAG, "skSeqNo : " + store_sSeqNo);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        user_uSeqNo = auto.getInt("userSeq",0); // 지석씨랑 화면 연결되면 쓰기
        userName = auto.getString("userNickName",null); // 지석씨랑 화면 연결되면 쓰기

        // 고객 이름
        tv_UserName = findViewById(R.id.activity_OrderList_TV_cName);
        tv_UserName.setText(userName + "님의 주문내역"); // 값 받아서 넣기

        from = intent.getStringExtra("from");
        Log.v(TAG, "from : " + from);


            if(from.equals("BeforePayActivity2")){// 장바구니에서 결제한 경우
                connectDeleteData(); // 카트 비우기
            }



        // 통신 ------------------------------------------------
        ///////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////
        mHandler = new Handler();

//        // 통신 -------------------------- 점주에게 접수 요청
//                    strStatus = "주문이 들어왔습니다!! \n주문내역을 확인해주세요.";
//                    Log.v(TAG, "Customer 주는 값 : " + strStatus);
//                    new Thread() { // 주는 스레드
//                        @Override
//                        public void run() {
//                            super.run();
//                            try {
//                                if(from.equals("BeforePayActivity")){
//                                    sendWriter.println(strStatus);
//                                    sendWriter.flush();
//                                }
////                            message.setText("");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//        // ------------------------------
//
//        new Thread() {
//            public void run() { // 받는 스레드
//
//                try {
//                    if(from == null){
//
//
//                    }else{
//                        InetAddress serverAddr = InetAddress.getByName(ip);
//                        socket = new Socket(serverAddr, port);
//                        sendWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"euc-kr")),true);
//                        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(),"euc-kr"));
//                        while(true){
//                            Log.v("통신 순서", "순서 1 - 받는 스레드");
//
//                            strStatus = input.readLine();
//                            Log.v("통신 확인(tify)", "Customer 받은 값 : " + strStatus);
//
//                            if(strStatus!=null){ // 점주가 변화를 줄 때 반응하는 부분 (여길 바꿔보자)
//                                mHandler.post(new msgUpdate(strStatus));
//                            }
//                        }
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } }}.start();


//        if(strStatus != null){ // 점주가 요청에 반응했을 때
//            Toast.makeText(BeforePayActivity.this, "주문이 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show();
//        }
        ///////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////

        //리사이클러뷰에 있는 아이디를 찾기
        recyclerView = findViewById(R.id.orderList_recycler_view);

        //리사이클러뷰 규격 만들기
        recyclerView.setHasFixedSize(true);

        //레이아웃 매니저 만들기
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 기본 구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        list = new ArrayList<Order>();
        list = connectGetData(); // db를 통해 받은 데이터를 담는다.

        Log.v(TAG, "list size : " + list.size());

        if(list.size() == 0){
            finish();

            Intent intent1 = new Intent(OrderListActivity.this, EmptyOrderListActivity.class);
            startActivity(intent1);
        }


        adapter.setOnItemClickListener(mClickListener);
    }

    OrderListAdapter.OnItemClickListener mClickListener = new OrderListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);

            int order_oNo = list.get(position).getoNo();
            String order_oInsertDate = list.get(position).getoInsertDate();

            intent.putExtra("macIP", macIP);
            intent.putExtra("user_uSeqNo", user_uSeqNo);
            intent.putExtra("store_sSeqNo", store_sSeqNo);
            intent.putExtra("order_oNo", order_oNo);
            intent.putExtra("order_oInsertDate", order_oInsertDate);

            startActivity(intent);
        }
    };

    private ArrayList<Order> connectGetData(){
        ArrayList<Order> beanList = new ArrayList<Order>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_order_select.jsp?user_uNo=" + user_uSeqNo;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_OrderNetworkTask networkTask = new LMW_OrderNetworkTask(OrderListActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            data = (ArrayList<Order>) obj;
            Log.v(TAG, "data.size() : " + data.size());

            adapter = new OrderListAdapter(OrderListActivity.this, R.layout.lmw_activity_orderlist_recycler_item, data, user_uSeqNo, store_sSeqNo);
            recyclerView.setAdapter(adapter);

            beanList = data;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    private String connectDeleteData(){ // 장바구니 전체 삭제
        String result = null;

        urlAddr = "http://" + macIP + ":8080/tify/lmw_cartlist_delete_all.jsp?user_uSeqNo=" + user_uSeqNo + "&store_sSeqNo=" + store_sSeqNo;
        Log.v(TAG, "전체삭제 url : " + urlAddr);
        where = "delete";

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where <- "insert"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_CartNetworkTask networkTask = new LMW_CartNetworkTask(OrderListActivity.this, urlAddr, where);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(OrderListActivity.this, JiseokMainActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
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
        title.setText("주문내역");

        ImageButton cart = findViewById(R.id.cart);
        cart.setVisibility(View.INVISIBLE);
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
                Log.v(TAG, "from : " + from); // from에 따라 다르게 보내주기 (EmptyOrderList에도 똑같이 해주기)

                Intent intent = null;

                if(from.equals("JiseokMainActivity")){
                    intent = new Intent(OrderListActivity.this, JiseokMainActivity.class);
                    startActivity(intent);
                }else{
                    SharedPreferences sharedPreferences = getSharedPreferences("storeInfo", MODE_PRIVATE);
                    store_sSeqNo = sharedPreferences.getInt("skSeqNo", 0);
                    sImage = sharedPreferences.getString("sImage", null);
                    sAddress = sharedPreferences.getString("sAddress", null);
                    sName = sharedPreferences.getString("sName", null);
                    sTime = sharedPreferences.getString("sTime", null);
                    sTelNo = sharedPreferences.getString("sTelNo", null);
                    sPackaging = sharedPreferences.getInt("sPackaging", 0);
                    sComment = sharedPreferences.getString("sComment", null);
                    skStatus = sharedPreferences.getInt("skStatus", 0);

                    intent = new Intent(OrderListActivity.this, StoreInfoActivity.class);
                    intent.putExtra("skSeqNo", Integer.toString(store_sSeqNo));
                    intent.putExtra("from", "OrderListActivity");
                    startActivity(intent);
                }


            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        return true;
    }

    // 통신 ------------------------------------------
    class msgUpdate implements Runnable{ // 받아서 작동하는 메소드
        private String msg;
        public msgUpdate(String str) {this.msg=str;}

        @Override
        public void run() {
            createNotification();

            Log.v("통신 순서", "순서 1 - 받는 스레드");

            Log.v("통신 확인(tify)", "msgUpdate msg : " + msg);
//            status.setText(status.getText().toString()+msg+"\n");

//            AlertDialog.Builder builder = new AlertDialog.Builder(OrderListActivity.this);
//            builder.setTitle("주문 결과");
//            builder.setMessage(msg + " \n test"); // 문장이 길 때는 String에 넣어서 사용하면 된다.
//            builder.setIcon(R.mipmap.ic_launcher); // 아이콘은 mipmap에 넣고 사용한다.
//            builder.show();

//            Toast.makeText(OrderListActivity.this, "주문이 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show();

        }
    }
    // -----------------------------------------------

    private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("주문 접수 알림!");
        builder.setContentText("요청하신 주문이 정상적으로 접수되었습니다.");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }


    private void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(1);
    }

    private String connectInsertStoreOrder(){

        urlAddr = "http://" + macIP + ":8080/tify/lmw_cartlist_delete_all.jsp?user_uSeqNo=" + user_uSeqNo + "&store_sSeqNo=" + store_sSeqNo;
        where = "insert";
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
                LMW_OrderNetworkTask networkTask = new LMW_OrderNetworkTask(OrderListActivity.this, urlAddr, where);
                Object obj = networkTask.execute().get();
                result = (String)obj;


            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


}