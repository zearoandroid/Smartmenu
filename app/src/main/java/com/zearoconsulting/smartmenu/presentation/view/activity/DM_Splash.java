package com.zearoconsulting.smartmenu.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import com.zearoconsulting.smartmenu.R;

public class DM_Splash extends DMBaseActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dm__splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(mAppManager.getUserLoggedIn() == true){
                    mIntent = new Intent(DM_Splash.this, DM_Menu.class);
                }else{
                    mIntent = new Intent(DM_Splash.this, DM_Login.class);
                }
                startActivity(mIntent);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
