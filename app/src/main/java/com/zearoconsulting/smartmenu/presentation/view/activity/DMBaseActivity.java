package com.zearoconsulting.smartmenu.presentation.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.data.AppDataManager;
import com.zearoconsulting.smartmenu.data.DBHelper;
import com.zearoconsulting.smartmenu.data.SMDataSource;
import com.zearoconsulting.smartmenu.domain.parser.JSONParser;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.utils.AppConstants;

/**
 * Created by saravanan on 18-10-2016.
 */

public class DMBaseActivity extends AppCompatActivity {

    public AppDataManager mAppManager;
    public SMDataSource mDBHelper;
    public JSONParser mParser;
    public ProgressDialog mProDlg;

    private SpringSystem mSpringSystem;
    public Spring mSpring;
    private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(100, 30);
    public boolean tabletSize;
    public Thread hThread;
    public ReboundListener mReboundListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabletSize = getResources().getBoolean(R.bool.isTablet);
        if(tabletSize) {
            AppConstants.isMobile = false;
        }
        else{
            AppConstants.isMobile = true;
        }

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAppManager = AndroidApplication.getInstance().getAppManager();
        mDBHelper = AndroidApplication.getInstance().getSMDataSource();

        mParser = new JSONParser(AndroidApplication.getAppContext(), mAppManager, mDBHelper);

        mSpringSystem = SpringSystem.create();
        mSpring = mSpringSystem.createSpring();
        mSpring.setSpringConfig(ORIGAMI_SPRING_CONFIG);

    }

    @Override
    protected void onPause() {
        super.onPause();
        AndroidApplication.activityPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidApplication.activityResumed();
    }

    public void showNetworkErrorDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AndroidApplication.getAppContext());

        // set title
        alertDialogBuilder.setTitle("Warning");

        // set dialog message
        alertDialogBuilder
                .setMessage("Please Check Your Internet Connection!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}


