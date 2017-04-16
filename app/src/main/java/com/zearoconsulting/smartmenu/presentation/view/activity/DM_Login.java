package com.zearoconsulting.smartmenu.presentation.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.presentation.view.fragment.LoadingDialogFragment;
import com.zearoconsulting.smartmenu.presentation.view.fragment.ServerConfigFragment;
import com.zearoconsulting.smartmenu.utils.Common;
import com.zearoconsulting.smartmenu.utils.FileUtils;

import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class DM_Login extends DMBaseActivity {

    private EditText mEdtUserName;
    private EditText mEdtUserPassword;
    private FancyButton mLoginBtn;
    private String username,password;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ImageView mImgCofig;

    private RadioGroup mRadGroupMode;
    private RadioButton mRadWaiter;
    private RadioButton mRadCustomer;

    private int mAppMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_dm__login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mReboundListener = new ReboundListener();
        mEdtUserName= (EditText) findViewById(R.id.usernameText);
        mEdtUserPassword= (EditText) findViewById(R.id.passwordText);
        mLoginBtn = (FancyButton)findViewById(R.id.buttonLogin);
        mImgCofig = (ImageView)findViewById(R.id.configImgView);
        mRadGroupMode = (RadioGroup)findViewById(R.id.radGroupMode);
        mRadWaiter = (RadioButton) findViewById(R.id.radWaiter);
        mRadCustomer = (RadioButton) findViewById(R.id.radCustomer);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                Log.e("value", "Permission already Granted, Now you can save image.");
            } else {
                requestPermission();
            }
        } else {
            Log.e("value", "Not required for requesting runtime permission");
        }

        //check server address is already available or not
        if(mAppManager.getServerAddress().equals("")){
            FileUtils.deleteImages();
            FileUtils.deleteVideos();
            showConfiguration();
        }

        mImgCofig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfiguration();
            }
        });

        mAppMode = mAppManager.getAppMode();
        if(mAppMode == 0)
            ((RadioButton)mRadGroupMode.getChildAt(0)).setChecked(true);
        else if(mAppMode == 1)
            ((RadioButton)mRadGroupMode.getChildAt(1)).setChecked(true);

        mRadGroupMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radWaiter) {
                    mAppManager.setAppMode(0);
                    Toast.makeText(getApplicationContext(), "Mode: Waiter",
                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radCustomer) {
                    mAppManager.setAppMode(1);
                    Toast.makeText(getApplicationContext(), "Mode: Customer",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

        // Add an OnTouchListener to the root view.
        mLoginBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(mLoginBtn);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // When pressed start solving the spring to 1.
                        mSpring.setEndValue(1);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // When released start solving the spring to 0.
                        mSpring.setEndValue(0);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                signIn();
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mAppManager.getUserName().equals("") && mAppManager.getUserLoggedIn()){
            mEdtUserName.setText(mAppManager.getUserName());
            mEdtUserPassword.setText(mAppManager.getUserPassword());
        }else{
            mEdtUserName.setText("");
            mEdtUserPassword.setText("");
        }

        //Add a listener to the spring
        mSpring.addListener(mReboundListener);


    }

    @Override
    protected void onPause() {
        super.onPause();

        //Remove a listener to the spring
        mSpring.removeListener(mReboundListener);
    }

    private void signIn(){

        //Crashlytics.log("Login");

        username = mEdtUserName.getText().toString().trim();
        password = mEdtUserPassword.getText().toString().trim();

        int selectedId = mRadGroupMode.getCheckedRadioButtonId();

        mAppMode = mAppManager.getAppMode();

        // find which radioButton is checked by id
        if(selectedId == mRadWaiter.getId()) {
            mAppManager.setAppMode(0);
        } else if(selectedId == mRadCustomer.getId()) {
            mAppManager.setAppMode(1);
        } else {
            mAppManager.setAppMode(-1);
            Toast.makeText(getApplicationContext(), "Please select any one of the mode",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(mEdtUserName.getText().toString().trim().equals("")){
            mEdtUserName.setError("Username should not be empty");
        }else if(mEdtUserPassword.getText().toString().trim().equals("")){
            mEdtUserPassword.setError("Password should not be empty");
        }
        else if (!mAppManager.getUserName().equalsIgnoreCase(username)){
            mDBHelper.deleteDhukanTables();
            mAppManager.clearUserSessionData();
            mAppManager.setUsername(username);
            mAppManager.setPassword(password);
            if(selectedId == mRadWaiter.getId()) {
                mAppManager.setAppMode(0);
            } else if(selectedId == mRadCustomer.getId()) {
                mAppManager.setAppMode(1);
            }
            showLoading();
        }else{
            mDBHelper.deleteDhukanTables();
            mAppManager.clearUserSessionData();
            mAppManager.setUsername(username);
            mAppManager.setPassword(password);
            if(selectedId == mRadWaiter.getId()) {
                mAppManager.setAppMode(0);
            } else if(selectedId == mRadCustomer.getId()) {
                mAppManager.setAppMode(1);
            }
            showLoading();
        }
    }

    private void showConfiguration()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ServerConfigFragment serverConfigFragment = new ServerConfigFragment();
        serverConfigFragment.show(fragmentManager, "ServerConfigFragment");
    }

    private void showLoading()
    {
        //check server address is already available or not
        if(mAppManager.getServerAddress().equals("")){
            Toast.makeText(DM_Login.this,"Please config server details...",Toast.LENGTH_SHORT).show();
        }else{
            FragmentManager localFragmentManager = getSupportFragmentManager();
            LoadingDialogFragment.newInstance(DM_Login.this, username, password).show(localFragmentManager, "loading");
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(DM_Login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(DM_Login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(DM_Login.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(DM_Login.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can save image .");
                } else {
                    Log.e("value", "Permission Denied, You cannot save image.");
                }
                break;
        }
    }
}
