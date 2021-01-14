package com.example.tify.Jiseok.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.tify.R;
import com.example.tify.ShareVar;

public class AddressSearchActivity extends AppCompatActivity {
    ShareVar shareVar =new ShareVar();
    String macIP = shareVar.getMacIP();

    private WebView webView;

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("data", data);
            intent.putExtras(extra);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjs_activity_address_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //액션바 삭제

        init_webView();


    }

    public void init_webView() {

        // WebView 설정

        webView = (WebView) findViewById(R.id.addressSearch_web_view);

        // JavaScript 허용

        webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌

        webView.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        // web client 를 chrome 으로 설정

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });


        // webview url
        webView.loadUrl("http://"+macIP+":8080/tify/daum.html");

    }

}