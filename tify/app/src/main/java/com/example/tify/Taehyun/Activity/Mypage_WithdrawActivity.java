package com.example.tify.Taehyun.Activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.tify.R;
import com.example.tify.Taehyun.NetworkTask.NetworkTask_TaeHyun;

public class Mypage_WithdrawActivity extends Dialog {

    private Context context;
    private int uNo;
    private String ipurl;
    private String urlAddr;



    public Mypage_WithdrawActivity(Context context, int uNo, String ipurl) {
        super(context);
        this.context = context;
        this.uNo = uNo;
        this.ipurl = ipurl;

    }

    public void callFunction(final String userDeleteMessage) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.kth_activity_mypage_withdraw);

        dialog.show();


        final Button mpd_okbtn = dialog.findViewById(R.id.mpd_okbtn);
        final Button mpd_cancelbtn = dialog.findViewById(R.id.mpd_cancelbtn);

        mpd_okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //seq 번호 넘겨받음
                    connectGetData();

                    String result = connectGetData();

                    if(result.equals("1")){
                        dialog.dismiss();
                        Intent intent = new Intent(context, Mypage_UserDeleteActivity.class);
                        context.startActivity(intent);
                    }else {

                    }
            }
        });

        mpd_cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }

    private String connectGetData(){
        String result = null;

        try {
            urlAddr = ipurl+":8080/tify/mypage_userdelete.jsp?uNo="+uNo;

            NetworkTask_TaeHyun deleteNetwork = new NetworkTask_TaeHyun(context, urlAddr,"delete");
            Object obj = deleteNetwork.execute().get();
            result = (String) obj;

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


}