package com.krsolutions.tardy.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.View;

import com.krsolutions.tardy.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View view = this.findViewById(R.id.imageView);
        scheduleSplash(this,view);
    }

    void scheduleSplash(Context base,View view){
        Long splashDuration=getSplashDuration();
        Handler handler = new Handler();
        handler.postDelayed(routeToMain(base, view),splashDuration);
    }

    Long getSplashDuration(){return 500L;}

    Runnable routeToMain(final Context base, final View view){
        return  new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(base,MainActivity.class);
                ActivityOptions options =
                        ActivityOptions.makeClipRevealAnimation(view,(int)view.getX(),(int)view.getY(),view.getWidth(),100);
                base.startActivity(intent, options.toBundle());
                finish();
            }
        };
    }
}
