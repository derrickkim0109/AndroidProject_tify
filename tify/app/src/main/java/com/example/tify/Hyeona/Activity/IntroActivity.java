package com.example.tify.Hyeona.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.tify.R;


public class IntroActivity extends Activity {

    /** Duration of wait **/
    // 스플래시 시간 설정 (1.5초)
    private final int SPLASH_DISPLAY_LENGTH = 1500;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.cha_activity_intro);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                //스플래시 이후 진행되는 슬라이드 뷰 페이저
                Intent mainIntent = new Intent(IntroActivity.this, ViewPagerActivity.class);
                IntroActivity.this.startActivity(mainIntent);
                IntroActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}