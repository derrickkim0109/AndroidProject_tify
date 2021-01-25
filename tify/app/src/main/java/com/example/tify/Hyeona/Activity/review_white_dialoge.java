package com.example.tify.Hyeona.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.tify.Hyeona.Bean.Bean_review_store;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_review;
import com.example.tify.R;
import com.example.tify.ShareVar;

import org.w3c.dom.Text;

public class review_white_dialoge extends Dialog {
    LinearLayout ll_hide;
    InputMethodManager inputMethodManager ;
    private Context context;
    private int sSeqNo;
    //데이터상 storekeeper_skSeqNo 임 확인필수
    private int uNo;
    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

    private String urlAddr = "http://" + MacIP + ":8080/tify/review_white_storeinfo.jsp?";
    private Bean_review_store bean_review_store = new Bean_review_store();
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
        dialog.show();

        final Button review_white_complete= dialog.findViewById(R.id.review_white_complete);
        final TextView review_store_name = dialog.findViewById(R.id.review_store_name);
        final ImageView review_add_image = findViewById(R.id.review_add_image);
        final ImageView review_cancel = findViewById(R.id.review_cancel);
        final EditText review_content = findViewById(R.id.review_content);

        ll_hide = findViewById(R.id.detail_ll_hide);

        View.OnClickListener monClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(ll_hide.getWindowToken(),0);
                final ImageView review_add_image = findViewById(R.id.review_add_image);
                review_add_image.setVisibility(View.INVISIBLE);
            }
        };

        //레이아웃 정보 받아옴

        connectGetData();
        // 가게 정보 받아오는 네트워크 타스크
        Log.v("이미지값 확인","ㅇ"+sImage);
        //
        review_store_name.setText(sName);


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
            Log.v("이거다",urlAddr);
            CUDNetworkTask_review mCUDNetworkTask_review = new CUDNetworkTask_review(context, urlAddr,"select_review_storeinfo");
            Object obj = mCUDNetworkTask_review.execute().get();
            bean_review_store = (Bean_review_store) obj;

            //받아온 정보
            sImage = bean_review_store.getsImage();
            Log.v("이거다",sImage);
            sName = bean_review_store.getsName();
            final ImageView review_store_image = findViewById(R.id.review_store_image);
            Glide.with(context).load("http://" + MacIP  + ":8080/tify/"+ sImage).into(review_store_image);
            // 가게이름이랑 이미지 받아옴


        }catch (Exception e){
            e.printStackTrace();
        }
    };



}
