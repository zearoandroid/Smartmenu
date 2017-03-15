package com.zearoconsulting.smartmenu.presentation.view.fragment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.domain.net.NetworkDataRequestThread;
import com.zearoconsulting.smartmenu.domain.receivers.KOTDataReceiver;
import com.zearoconsulting.smartmenu.domain.services.KOTService;
import com.zearoconsulting.smartmenu.presentation.model.KOTLineItems;
import com.zearoconsulting.smartmenu.presentation.model.Tables;
import com.zearoconsulting.smartmenu.presentation.presenter.TableSelectListener;
import com.zearoconsulting.smartmenu.presentation.view.activity.DM_Menu;
import com.zearoconsulting.smartmenu.presentation.view.adapter.DMTableAdapter;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.presentation.view.dialogs.NetworkErrorDialog;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.NetworkUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

import static android.content.Context.ALARM_SERVICE;
import static com.zearoconsulting.smartmenu.utils.AppConstants.isTableVisible;

/**
 * Created by saravanan on 11-11-2016.
 */

public class TableSelectionViewFragment extends AbstractDialogFragment{

    RecyclerView tableListView;
    List<Tables> mKOTTableList;
    private DMTableAdapter mTableAdapter;
    List<Long> mTableIdList, mParsingTableIdList;
    private FancyButton mBtnCancel;
    Handler updateHandler = new Handler();
    Runnable runnable;
    private TextView mTxtTitle;

    final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.getData().getInt("Type");
            String jsonStr = msg.getData().getString("OUTPUT");

            switch (type) {
                case AppConstants.GET_TABLE_STATUS:
                    mParsingTableIdList = mParser.parseTableStatus(jsonStr, mHandler);
                    mProDlg.dismiss();
                    //Update adapter colors
                    updateTableViews();
                    break;
                case AppConstants.TABLE_STATUS_RECEIVED:
                    mProDlg.dismiss();
                    break;
                case AppConstants.GET_TABLE_KOT_DETAILS:
                    mParser.parseTableKOTDataResponse(jsonStr,mHandler);
                    break;
                case AppConstants.TABLE_KOT_DETAILS_RECEIVED:
                    mProDlg.dismiss();
                    List<KOTLineItems> mKotLienItemList = mDBHelper.getKOTLineItems(AppConstants.tableID);
                    if(mKotLienItemList.size() == 0){
                        showCoverEntryDialog();
                    }else{
                        dismiss();
                    }
                    break;
                case AppConstants.SERVER_ERROR:
                    mProDlg.dismiss();
                    //show the server error dialog
                    Toast.makeText(getActivity(), "Server data error", Toast.LENGTH_SHORT).show();
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    TableSelectListener tableSelectListener = new TableSelectListener() {
        @Override
        public void OnTableSelectedListener(Tables tableEntity) {
            try {
                Tables table = mDBHelper.getTableData(mAppManager.getClientID(),mAppManager.getOrgID(),tableEntity.getTableId());
                AppConstants.tableID = tableEntity.getTableId();
                if(table.getOrderAvailable().equalsIgnoreCase("Y")){
                    mDBHelper.deleteKOTLineItems(AppConstants.tableID);
                    List<KOTLineItems> mKotLienItemList = mDBHelper.getKOTLineItems(AppConstants.tableID);
                    if(mKotLienItemList.size() == 0){
                        getTableKOT();
                    }
                }else{
                    mDBHelper.deleteKOTLineItems(AppConstants.tableID);
                    showCoverEntryDialog();
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    };

    public Dialog onCreateDialog(Bundle paramBundle) {
        Dialog localDialog = super.onCreateDialog(paramBundle);
        localDialog.getWindow().requestFeature(1);
        return localDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mReboundListener = new ReboundListener();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.table_selection_layout, container, false);
    }

    @Override
    public void onResume() {

        try {
            isTableVisible = true;
            updateHandler.postDelayed(runnable, 5000);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        isTableVisible = false;
        updateHandler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void onViewCreated(View paramView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(paramView, savedInstanceState);

        getDialog().getWindow().setSoftInputMode(3);
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCanceledOnTouchOutside(true);


        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode, android.view.KeyEvent event) {

                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    //Stop back event here!!!
                    return true;
                } else
                    return false;
            }
        });

        mProDlg = new ProgressDialog(getActivity());
        mProDlg.setIndeterminate(true);
        mProDlg.setCancelable(false);

        this.mTxtTitle = (TextView) paramView.findViewById(R.id.title);
        this.mBtnCancel = (FancyButton)paramView.findViewById(R.id.backButton);
        this.tableListView = ((RecyclerView) paramView.findViewById(R.id.table_selection));
        mKOTTableList = mDBHelper.getTables(mAppManager.getClientID(),mAppManager.getOrgID());
        mTableIdList = mDBHelper.getTableIds(mAppManager.getClientID(),mAppManager.getOrgID());

        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;

        mTableAdapter = new DMTableAdapter(mKOTTableList, mTableIdList);
        tableListView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(1000);
        tableListView.setItemAnimator(animator);
        tableListView.setAdapter(mTableAdapter);

        //set the table select listener
        mTableAdapter.setOnTableSelectListener(tableSelectListener);

        this.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        runnable = new Runnable(){

            public void run() {
                //updateTableViews(); // some action(s)
                getTableStatus1();
                updateHandler.postDelayed(this, 5000);
            }

        };

        //updateHandler.postDelayed(runnable, 5000);
    }

    private void getTableStatus(){
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            try {

                mProDlg.setMessage("Getting tables status...");
                //mProDlg.show();
                mTxtTitle.setText("Select Table (Refreshing...)");

                JSONObject mJsonObj = mParser.getParams(AppConstants.GET_TABLE_STATUS);
                Log.i("KOTJson", mJsonObj.toString());

                NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_TABLE_STATUS);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(getActivity()).show();
        }
    }

    private void getTableKOT(){
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            try {

                mProDlg.setMessage("Getting tables details...");
                mProDlg.show();

                AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;
                JSONObject mJsonObj = mParser.getParams(AppConstants.GET_TABLE_KOT_DETAILS);
                mJsonObj.put("tableId", AppConstants.tableID);
                Log.i("KOTJson", mJsonObj.toString());

                //NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.GET_TABLE_KOT_DETAILS);
                //thread.start();

                AndroidApplication.getInstance().cancelPendingRequests(this);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppConstants.URL, mJsonObj,
                        new Response.Listener<JSONObject>(){

                            @Override
                            public void onResponse(JSONObject response) {
                                mParser.parseTableKOTDataResponse(response.toString(),mHandler);
                            }
                        }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        mProDlg.dismiss();
                    }
                }
                );

                // Adding request to request queue
                AndroidApplication.getInstance().addToRequestQueue(jsonObjReq);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(getActivity()).show();
        }
    }

    public void updateTableViews(){
        mTxtTitle.setText("Select Table");
        List<Long> mTableIdLists = mDBHelper.getTableIdsLocalAvailable();
        for(int i=0;i<mTableIdLists.size(); i++){
            mDBHelper.updateTableStatusAvailable(mTableIdLists.get(i));
        }
        mKOTTableList = mDBHelper.getTables(mAppManager.getClientID(),mAppManager.getOrgID());
        mTableAdapter.updateTables(mKOTTableList);
    }

    private void showCoverEntryDialog(){
        FragmentManager localFragmentManager = getActivity().getSupportFragmentManager();
        CoverFragment coverFragment = new CoverFragment();
        coverFragment.show(localFragmentManager, "CoverFragment");
        dismiss();
    }

    private void getTableStatus1(){
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            try {

                mTxtTitle.setText("Select Table (Refreshing...)");

                AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;
                JSONObject mJsonObj = mParser.getParams(AppConstants.GET_TABLE_STATUS);
                Log.i("KOTJson", mJsonObj.toString());

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppConstants.URL, mJsonObj,
                        new Response.Listener<JSONObject>(){

                            @Override
                            public void onResponse(JSONObject response) {
                                mParsingTableIdList = mParser.parseTableStatus(response.toString(), null);
                                //Update adapter colors
                                updateTableViews();
                            }
                        }, new Response.ErrorListener(){
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                    //mProDlg.dismiss();
                                }
                            }
                        );

                // Adding request to request queue
                AndroidApplication.getInstance().addToRequestQueue(jsonObjReq);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //show network failure dialog or toast
            NetworkErrorDialog.buildDialog(getActivity()).show();
        }
    }
}
