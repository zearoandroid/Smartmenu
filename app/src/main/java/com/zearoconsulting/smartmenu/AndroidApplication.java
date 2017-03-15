package com.zearoconsulting.smartmenu;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.zearoconsulting.smartmenu.data.AppDataManager;
import com.zearoconsulting.smartmenu.data.DBHelper;
import com.zearoconsulting.smartmenu.data.SMDataSource;

import io.fabric.sdk.android.Fabric;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by saravanan on 31-10-2016.
 */

public class AndroidApplication extends Application {

    private static AndroidApplication sInstance;
    private static AppDataManager mManager;
    private static DBHelper mDBHelper;
    private static String mLanguage = "en";
    private static boolean activityVisible;
    private static SMDataSource mSMDataSource;

    private RequestQueue mRequestQueue;
    public static final String TAG = AndroidApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        sInstance = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/GothamRounded-Medium.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        //printHashKey();

    }

    public static AndroidApplication getInstance(){
        if (sInstance == null) {
            sInstance = new AndroidApplication();
        }
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

    public AppDataManager getAppManager(){

        if(mManager == null){
            mManager = new AppDataManager(getApplicationContext());
        }

        return mManager;
    }

    public DBHelper getDBHelper(){

        if(mDBHelper == null){
            mDBHelper = new DBHelper(getAppContext());
        }

        return mDBHelper;
    }

    public SMDataSource getSMDataSource(){
        if(mSMDataSource == null){
            mSMDataSource = new SMDataSource(getAppContext());
            mSMDataSource.open();
        }

        return mSMDataSource;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public static Typeface getGothamRoundedLight(){
        return Typeface.createFromAsset( sInstance.getApplicationContext().getAssets(),
                "fonts/GothamRounded-Light.otf");
    }

    public static Typeface getGothamRoundedMedium(){
        return Typeface.createFromAsset( sInstance.getApplicationContext().getAssets(),
                "fonts/GothamRounded-Medium.otf");
    }

    public static Typeface getGothamRoundedBold(){
        return Typeface.createFromAsset( sInstance.getApplicationContext().getAssets(),
                "fonts/GothamRounded-Bold.otf");
    }

    public static Typeface getGothamRoundedBook(){
        return Typeface.createFromAsset( sInstance.getApplicationContext().getAssets(),
                "fonts/GothamRounded-Book.otf");
    }

    public void setConnectivityListener(com.zearoconsulting.smartmenu.domain.receivers.ConnectivityReceiver.ConnectivityReceiverListener listener) {
        com.zearoconsulting.smartmenu.domain.receivers.ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.zearoconsulting.smartmenu",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("SmartMenuHashKey:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static void setLanguage(String language){
        mLanguage = language;
    }

    public static String getLanguage(){
        return mLanguage;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
