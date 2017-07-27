package com.nationality;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.nationality.utils.Constants;

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        splashHandlerStart(2500);

    }

    public void splashHandlerStart(int timeOut){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Constants.getLoggedinStatus(SplashActivity.this)){

                    Intent in = new Intent(SplashActivity.this, LandingActivity.class);
                    startActivity(in);
                    finish();
                }
                else {
                    Intent in = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        }, timeOut);
    }

}