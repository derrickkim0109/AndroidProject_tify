package com.example.tify;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      ImageView backgroundImageView = findViewById(R.id.gifTest);
        Glide.with(this).load(R.raw.gps_setting).into(backgroundImageView);
    }
}