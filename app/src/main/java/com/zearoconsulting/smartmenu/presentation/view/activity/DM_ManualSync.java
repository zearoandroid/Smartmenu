package com.zearoconsulting.smartmenu.presentation.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.domain.net.NetworkDataRequestThread;
import com.zearoconsulting.smartmenu.domain.receivers.ConnectivityReceiver;
import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.presentation.view.dialogs.NetworkErrorDialog;
import com.zearoconsulting.smartmenu.presentation.view.fragment.CartViewFragment;
import com.zearoconsulting.smartmenu.presentation.view.fragment.LoadingDialogFragment;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.FileUtils;
import com.zearoconsulting.smartmenu.utils.NetworkUtil;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


public class DM_ManualSync extends DMBaseActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{

    private FancyButton mBtnSync;
    private List<Category> mCategoryList;
    // variable to track event time
    private long mLastClickTime = 0;

    final Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            int type  = msg.getData().getInt("Type");
            String jsonStr = msg.getData().getString("OUTPUT");
            switch (type){
                case AppConstants.GET_TABLES:
                    mParser.parseTables(jsonStr, mHandler);
                    break;
                case AppConstants.TABLES_RECEIVED:
                    getTerminals();
                    break;
                case AppConstants.GET_TERMINALS:
                    mParser.parseTerminals(jsonStr, mHandler);
                    break;
                case AppConstants.TERMINALS_RECEIVED:
                    getCategory();
                    break;
                case AppConstants.GET_CATEGORY:
                    mParser.parseCategorysJson(jsonStr, mHandler);
                    break;
                case AppConstants.CATEGORY_RECEIVED:
                    getAllProducts();
                    break;
                case AppConstants.GET_ALL_PRODUCTS:
                    mParser.parseProductJson(jsonStr, mHandler);
                    break;
                case AppConstants.PRODUCTS_RECEIVED:
                    //finish();
                    downloadVideos();
                    break;
                case AppConstants.SERVER_ERROR:
                    mProDlg.dismiss();
                    mBtnSync.setVisibility(View.VISIBLE);
                    //show the server error dialog
                    Toast.makeText(DM_ManualSync.this,"Server data error",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_dm__manual_sync);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mReboundListener = new ReboundListener();

        mProDlg = new ProgressDialog(this);
        mProDlg.setIndeterminate(true);
        mProDlg.setCancelable(false);
        mBtnSync = (FancyButton)findViewById(R.id.btnManualSync);

        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;

        // Add an OnTouchListener to the root view.
        /*mBtnSync.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(mBtnSync);
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
                                    mDBHelper.deleteSmartMenuTables();
                                    addCategory();
                                    getTables();

                                }
                            }, 200);

                        break;
                }
                return true;
            }
        });*/

        mBtnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Preventing multiple clicks, using threshold of 1 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                mDBHelper.deleteSmartMenuTables();
                addCategory();
                getTables();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onMenuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

    private void addCategory(){
        try{
            boolean isAvail = mDBHelper.checkAllCategory();
            if(!isAvail){
                Category category = new Category();
                category.setCategoryId(0);
                category.setCategoryName("All (Quick Menu)");
                category.setCategoryValue("All (Quick Menu)");

                // Retrieve the image from the res folder
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.no_product);
                String imagePath = FileUtils.storeImage("", 0, bitmap);
                category.setCategoryImage(imagePath);
                category.setShowDigitalMenu("Y");
                category.setCategoryArabicName("القائمة السريعة");
                category.setClientId(mAppManager.getClientID());
                category.setOrgId(mAppManager.getOrgID());

                //add category
                mDBHelper.addCategory(category);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void getCategory() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {

            mProDlg.setMessage("Getting category data...");
            //mProDlg.show();

            //GET THE CATEGORY DATA
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_CATEGORY);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_CATEGORY);
            thread.start();
        } else {
            mProDlg.dismiss();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(DM_ManualSync.this).show();
        }
    }

    private void getAllProducts() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            mProDlg.setMessage("Getting product data...");
            //mProDlg.show();
            try {
                JSONObject mJsonObj = mParser.getParams(AppConstants.GET_ALL_PRODUCTS);
                mJsonObj.put("categoryId", 0);
                mJsonObj.put("pricelistId", mAppManager.getPriceListID());
                mJsonObj.put("costElementId", mAppManager.getCostElementID());
                NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_ALL_PRODUCTS);
                thread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //show network failure dialog or toast

            NetworkErrorDialog.buildDialog(DM_ManualSync.this).show();
        }
    }

    public void getTables() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {

            mProDlg.setMessage("Getting Table data...");
            mProDlg.show();

            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_TABLES);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_TABLES);
            thread.start();
        } else {
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(DM_ManualSync.this).show();
        }
    }

    public void getTerminals() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {

            mProDlg.setMessage("Getting Terminal data...");
            //mProDlg.show();

            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_TERMINALS);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_TERMINALS);
            thread.start();
        } else {
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(DM_ManualSync.this).show();
        }
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            if(mProDlg.isShowing())
                mProDlg.dismiss();
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.activity_dm__manual_sync), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        AndroidApplication.getInstance().setConnectivityListener(this);

        //Add a listener to the spring
        mSpring.addListener(mReboundListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Remove a listener to the spring
        mSpring.removeListener(mReboundListener);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
    }

    private void downloadVideos(){
        try{
            DownloadVideoTask mDVTask = new DownloadVideoTask();
            mDVTask.execute("");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class DownloadVideoTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                List<Product> productList = mDBHelper.getAllProductIfVideoAvailable();

                for (int i = 0; i < productList.size(); i++) {

                    if (!productList.get(i).getProdVideoPath().equalsIgnoreCase("N")) {
                        URL url = new URL(productList.get(i).getProdVideoPath());
                        HttpURLConnection c = (HttpURLConnection) url.openConnection();
                        c.setRequestMethod("GET");
                        c.setDoOutput(true);
                        c.connect();

                        String videoPath = FileUtils.storeVideo(productList.get(i).getProdId(), c.getInputStream());

                        mDBHelper.updateProductVideoPath(productList.get(i).getProdId(), videoPath);

                        mDBHelper.addProductImage(mAppManager.getClientID(),mAppManager.getOrgID(),productList.get(i).getProdId(), videoPath, "video");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {
            mProDlg.dismiss();
            mAppManager.setLoggedIn(true);
            mBtnSync.setVisibility(View.VISIBLE);
            finish();
        }

    }
}

