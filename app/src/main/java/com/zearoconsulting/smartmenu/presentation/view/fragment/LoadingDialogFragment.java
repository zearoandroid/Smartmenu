package com.zearoconsulting.smartmenu.presentation.view.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.domain.net.NetworkDataRequestThread;
import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.view.activity.DM_Menu;
import com.zearoconsulting.smartmenu.presentation.view.dialogs.NetworkErrorDialog;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.FileUtils;
import com.zearoconsulting.smartmenu.utils.NetworkUtil;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingDialogFragment extends AbstractDialogFragment {


    private static Context context;
    private String username, password;
    private TextView statusText, progressText;
    private ProgressWheel progressWheel;
    private List<Long> mDefaultIdList;
    private long mCategoryId = 0;
    private  Intent mIntent;

    final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.getData().getInt("Type");
            String jsonStr = msg.getData().getString("OUTPUT");

            switch (type) {
                case AppConstants.GET_ORGANIZATION_DATA:
                    mParser.parseOrgJson(jsonStr, mHandler);
                    break;
                case AppConstants.CALL_AUTHENTICATE:
                    mParser.parseLoginJson(jsonStr, mHandler);
                    break;
                case AppConstants.ORGANIZATION_DATA_RECEIVED:
                    authenticate();
                    break;
                case AppConstants.LOGIN_SUCCESS:
                    getDefaultCustomerData();
                    break;
                case AppConstants.LOGIN_FAILURE:
                    progressWheel.stopSpinning();
                    //show the server error dialog
                    Toast.makeText(context, "Invalid user name or password", Toast.LENGTH_SHORT).show();
                    dismiss();
                    break;
                case AppConstants.GET_CASH_CUSTOMER_DATA:
                    mParser.parseCommonJson(jsonStr, mHandler);
                    break;
                case AppConstants.COMMON_DATA_RECEIVED:
                    if (mAppManager.getUserName().equalsIgnoreCase(username) && mDBHelper.getAllProduct(mAppManager.getClientID(),mAppManager.getOrgID()).size() != 0) {
                        mAppManager.setLoggedIn(true);
                        progressWheel.stopSpinning();
                        dismiss();
                        mIntent = new Intent(context, DM_Menu.class);
                        startActivity(mIntent);
                        getActivity().finish();
                    } else {
                        mDBHelper.deleteSmartMenuTables();
                        getTables();
                    }
                    break;
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
                    //progressWheel.stopSpinning();
                    getAllProducts();
                    break;
                case AppConstants.GET_ALL_PRODUCTS:
                    mParser.parseProductJson(jsonStr, mHandler);
                    break;
                case AppConstants.PRODUCTS_RECEIVED:
                    downloadVideos();
                    /*mAppManager.setLoggedIn(true);
                    mIntent = new Intent(context, DM_Menu.class);
                    startActivity(mIntent);
                    getActivity().finish();*/
                    break;
                case AppConstants.NO_DATA_RECEIVED:
                    progressWheel.stopSpinning();
                    dismiss();
                    break;
                case AppConstants.SERVER_ERROR:
                    progressWheel.stopSpinning();
                    Toast.makeText(context, "Server data error", Toast.LENGTH_SHORT).show();
                    dismiss();
                    break;
                case AppConstants.DEVICE_NOT_REGISTERED:
                    progressWheel.stopSpinning();
                    Toast.makeText(context, "Device not registered to server!", Toast.LENGTH_SHORT).show();
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public LoadingDialogFragment() {
        // Required empty public constructor
    }

    public static LoadingDialogFragment newInstance(Context paramContext, String paramString1, String paramString2) {
        LoadingDialogFragment localLoadingDialogFragment = new LoadingDialogFragment();
        Bundle localBundle = new Bundle();
        localBundle.putString("username", paramString1);
        localBundle.putString("password", paramString2);
        localLoadingDialogFragment.setArguments(localBundle);
        context = paramContext;
        return localLoadingDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading_dialog, container, false);
    }

    @Override
    public void onViewCreated(View paramView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(paramView, savedInstanceState);

        this.username = getArguments().getString("username", "");
        this.password = getArguments().getString("password", "");

        getDialog().getWindow().setSoftInputMode(3);
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCanceledOnTouchOutside(false);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode,android.view.KeyEvent event) {

                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK))
                {
                    //Stop back event here!!!
                    return true;
                }
                else
                    return false;
            }
        });

        this.statusText = ((TextView) paramView.findViewById(R.id.status_text));
        this.progressText = ((TextView) paramView.findViewById(R.id.progress_text));
        this.progressWheel = ((ProgressWheel) paramView.findViewById(R.id.progress_wheel));
        this.progressWheel.spin();
        this.statusText.setText("Please wait");

        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;

        retrieveTabletMenu();
    }

    private void retrieveTabletMenu() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_ORGANIZATION_DATA);

            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_ORGANIZATION_DATA);
            thread.start();
        } else {
            progressWheel.stopSpinning();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    private void getCategory() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            //GET THE CATEGORY DATA
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_CATEGORY);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_CATEGORY);
            thread.start();
        } else {
            progressWheel.stopSpinning();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    private void getAllProducts() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            try {
                JSONObject mJsonObj = mParser.getParams(AppConstants.GET_ALL_PRODUCTS);
                mJsonObj.put("categoryId", mCategoryId);
                mJsonObj.put("pricelistId", mAppManager.getPriceListID());
                mJsonObj.put("costElementId", mAppManager.getCostElementID());
                NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_ALL_PRODUCTS);
                thread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            progressWheel.stopSpinning();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    private void authenticate(){
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            try {
                mDefaultIdList = mDBHelper.getDefaultIds();
                mAppManager.setOrgID(mDefaultIdList.get(0));
                mAppManager.setWarehouseID(mDefaultIdList.get(1));
                mAppManager.setRoleID(mDefaultIdList.get(2));
                mAppManager.setRoleName(mDBHelper.getRoleName(mDefaultIdList.get(2)));

                //call authenticate api
                JSONObject mJsonObj = mParser.getParams(AppConstants.CALL_AUTHENTICATE);

                NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL,"",mHandler,mJsonObj.toString(),AppConstants.CALL_AUTHENTICATE);
                thread.start();
            }catch (Exception e){
                e.printStackTrace();
                progressWheel.stopSpinning();
            }

        }else{
            progressWheel.stopSpinning();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    private void getDefaultCustomerData() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_CASH_CUSTOMER_DATA);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_CASH_CUSTOMER_DATA);
            thread.start();
        } else {
            progressWheel.stopSpinning();
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    public void getTables() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_TABLES);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_TABLES);
            thread.start();
        } else {
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
    }

    public void getTerminals() {
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_TERMINALS);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_TERMINALS);
            thread.start();
        } else {
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(context).show();
        }
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
            progressWheel.stopSpinning();
            mAppManager.setLoggedIn(true);
            mIntent = new Intent(context, DM_Menu.class);
            startActivity(mIntent);
            getActivity().finish();
        }

    }
}
