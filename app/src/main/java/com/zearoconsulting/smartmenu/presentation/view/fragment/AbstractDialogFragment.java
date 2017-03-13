package com.zearoconsulting.smartmenu.presentation.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.data.AppDataManager;
import com.zearoconsulting.smartmenu.data.DBHelper;
import com.zearoconsulting.smartmenu.data.SMDataSource;
import com.zearoconsulting.smartmenu.domain.parser.JSONParser;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;

/**
 * Created by saravanan on 20-10-2016.
 */

public class AbstractDialogFragment extends DialogFragment {

    public AppDataManager mAppManager;
    public SMDataSource mDBHelper;
    public JSONParser mParser;

    private SpringSystem mSpringSystem;
    public Spring mSpring;
    private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(100, 30);
    public ReboundListener mReboundListener;
    public ProgressDialog mProDlg;

    /*
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppManager = AndroidApplication.getInstance().getAppManager();
        mDBHelper = AndroidApplication.getInstance().getSMDataSource();

        mParser = new JSONParser(AndroidApplication.getAppContext(), mAppManager, mDBHelper);

        mSpringSystem = SpringSystem.create();
        mSpring = mSpringSystem.createSpring();
        mSpring.setSpringConfig(ORIGAMI_SPRING_CONFIG);

    }
}
