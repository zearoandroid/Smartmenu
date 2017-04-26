package com.zearoconsulting.smartmenu.presentation.view.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zearoconsulting.smartmenu.BuildConfig;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.domain.net.NetworkDataRequestThread;
import com.zearoconsulting.smartmenu.presentation.model.KOTLineItems;
import com.zearoconsulting.smartmenu.presentation.model.Organization;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.NetworkUtil;

import org.json.JSONObject;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by saravanan on 17-11-2016.
 */

public class InfoFragment extends AbstractDialogFragment {

    private FancyButton mBtnCancel;
    private TextView mTxtDescription;
    private TextView mTxtLoggedUser, mTxtAppVersion;
    private Button mBtnUpdate;

    final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.getData().getInt("Type");
            String jsonStr = msg.getData().getString("OUTPUT");

            switch (type) {
                case AppConstants.SERVER_ERROR:
                    mProDlg.dismiss();
                    //show the server error dialog
                    Toast.makeText(getActivity(), "Server data error", Toast.LENGTH_SHORT).show();
                    dismissAllowingStateLoss();
                    break;
                case AppConstants.CHECK_UPDATE_AVAILABLE:
                    mParser.parseAppUpdateAvailable(jsonStr, mHandler);
                    break;
                case AppConstants.NO_UPDATE_AVAILABLE:
                    mProDlg.dismiss();
                    Toast.makeText(getActivity(), "There is no update available", Toast.LENGTH_SHORT).show();
                    break;
                case AppConstants.UPDATE_APP:
                    mProDlg.dismiss();
                    showAppInstallDialog();
                    break;
                default:
                    break;
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
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Add a listener to the spring
        mSpring.addListener(mReboundListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        //Remove a listener to the spring
        mSpring.removeListener(mReboundListener);
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

        this.mBtnCancel = (FancyButton) paramView.findViewById(R.id.backButton);
        this.mTxtDescription = (TextView) paramView.findViewById(R.id.txtDescription);
        this.mTxtLoggedUser = (TextView) paramView.findViewById(R.id.txtLoggedUser);
        this.mTxtAppVersion = (TextView) paramView.findViewById(R.id.txtAppVersion);
        this.mBtnUpdate = (Button) paramView.findViewById(R.id.btnAppUpdate);

        mProDlg = new ProgressDialog(getActivity());
        mProDlg.setIndeterminate(true);
        mProDlg.setCancelable(false);

        Organization organization = mDBHelper.getOrganizationDetail(mAppManager.getOrgID());
        if(organization!=null)
            this.mTxtDescription.setText(organization.getOrgDescription());

        mTxtAppVersion.setText("Version: "+ BuildConfig.VERSION_NAME);
        mTxtLoggedUser.setText("Hello "+mAppManager.getUserName());

        this.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        this.mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAppManager.getRemindMeStatus().equalsIgnoreCase("")){
                    try {
                        checkUpdateAvailable();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void checkUpdateAvailable(){
        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
            mProDlg.setMessage("Check update available");
            mProDlg.show();
            JSONObject mJsonObj = mParser.getParams(AppConstants.CHECK_UPDATE_AVAILABLE);
            NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.CHECK_UPDATE_AVAILABLE);
            thread.start();
        } else {
            //show network failure dialog or toast
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAppInstallDialog(){
        try {
            //show denomination screen
            FragmentManager localFragmentManager = getActivity().getSupportFragmentManager();
            AppUpdateFragment appUpdateFragment = new AppUpdateFragment();
            appUpdateFragment.show(localFragmentManager, "AppUpdateFragment");
        }catch (Exception e){
            e.printStackTrace();
        }

        dismissAllowingStateLoss();
    }
}
