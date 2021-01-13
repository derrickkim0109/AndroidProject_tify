package com.example.tify.Taehyun.Activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tify.R;
import com.example.tify.ShareVar;

public class OrderPage_PaymentActivity extends AppCompatActivity {

    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.cha_payment);



    }

}
