package com.example.tify.Hyeona.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Bean.Bean_review_store;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_review;
import com.example.tify.R;

import org.w3c.dom.Text;

public class review_white_dialoge extends Dialog {

    private Context context;
    private int sSeqNo;
    //데이터상 storekeeper_skSeqNo 임 확인필수
    private int uNo;
    private String macIP = "192.168.0.55";
    private String urlAddr = "http://" + macIP + ":8080/tify/review_white_storeinfo.jsp?";
    private Bean_review_store bean_review_store;
    private String sName;
    private String sImage;


    public review_white_dialoge(Context context, int sSeqNo, int uNo) {
        super(context);
        this.context = context;
        this.sSeqNo = sSeqNo;
        this.uNo = uNo;
    }

    public void callDialog() {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cha_review_white_dialoge);
        final Button review_white_complete= dialog.findViewById(R.id.review_white_complete);
        final TextView review_store_name = dialog.findViewById(R.id.review_store_name);
        final ImageView review_add_image = findViewById(R.id.review_add_image);
        final ImageView review_store_image = findViewById(R.id.review_store_image);

        final EditText review_content = findViewById(R.id.review_content);
        review_content.setFilters(new InputFilter[] {new InputFilter.LengthFilter(100)});
        //레이아웃 정보 받아옴

        connectGetData();
        // 가게 정보 받아오는 네트워크 타스크

        Glide.with(context).load("http://" + macIP + ":8080/tify/"+ sImage).into(review_store_image);
        review_store_name.setText(sName);

        dialog.show();
        review_white_complete.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void connectGetData(){
        try {
            urlAddr = urlAddr+"storekeeper_skSeqNo="+sSeqNo;
            CUDNetworkTask_review CUDNetworkTask_review = new CUDNetworkTask_review(context, urlAddr,"select_review_storeinfo");
            Object obj = CUDNetworkTask_review.execute().get();
            bean_review_store = (Bean_review_store) obj;

            //받아온 정보
            sImage = bean_review_store.getsImage();
            sName = bean_review_store.getsName();
            // 가게이름이랑 이미지 받아옴


        }catch (Exception e){
            e.printStackTrace();
        }
    };

}
