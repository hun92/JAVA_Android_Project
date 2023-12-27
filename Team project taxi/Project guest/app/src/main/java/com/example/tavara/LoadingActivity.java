package com.example.tavara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        Handler handler = new Handler();
        handler.postDelayed(new SplashHandler(), 2000);
    }

    public class SplashHandler implements Runnable {

        public void run() {
            Intent goMain = new Intent(getApplicationContext(), MainActivity.class); // 콤마 뒤에 인트로 후 이동할 액티비티 지정
            startActivity(goMain);

            //보안정책
            StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(threadPolicy);
        }
    }



}