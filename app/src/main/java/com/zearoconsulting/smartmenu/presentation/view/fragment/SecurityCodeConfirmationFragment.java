package com.zearoconsulting.smartmenu.presentation.view.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.view.activity.DM_Login;
import com.zearoconsulting.smartmenu.presentation.view.activity.DM_ManualSync;
import com.zearoconsulting.smartmenu.presentation.view.activity.DM_Menu;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.utils.AppConstants;

import mehdi.sakout.fancybuttons.FancyButton;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by saravanan on 17-11-2016.
 */

public class SecurityCodeConfirmationFragment extends AbstractDialogFragment{

    private static Context context;
    private EditText mEdtUserPassword;
    private FancyButton mBtnSupmit;
    private FancyButton mBtnCancel;
    private String mViewType;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    public static SecurityCodeConfirmationFragment newInstance(Context paramContext, String paramString1) {
        SecurityCodeConfirmationFragment securityCodeConfirmationFragment = new SecurityCodeConfirmationFragment();
        Bundle localBundle = new Bundle();
        localBundle.putString("viewType", paramString1);
        securityCodeConfirmationFragment.setArguments(localBundle);
        context = paramContext;
        return securityCodeConfirmationFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mReboundListener = new ReboundListener();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security, container, false);
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

        this.mViewType = getArguments().getString("viewType", "");

        getDialog().getWindow().setSoftInputMode(3);
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCanceledOnTouchOutside(false);


        myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);

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

        this.mEdtUserPassword= (EditText) paramView.findViewById(R.id.passwordText);
        this.mBtnSupmit = ((FancyButton) paramView.findViewById(R.id.submit));
        this.mBtnCancel = ((FancyButton) paramView.findViewById(R.id.cancel));

        // Add an OnTouchListener to the root view.
        this.mBtnCancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(mBtnCancel);
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
                                SecurityCodeConfirmationFragment.this.dismiss();
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

        // Add an OnTouchListener to the root view.
        this.mBtnSupmit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(mBtnSupmit);
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
                                //post data to server
                                validatePassword();
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

    }

    private void validatePassword(){
        String originalPassword = mAppManager.getUserPassword();
        if (mEdtUserPassword.getText().toString().trim().equalsIgnoreCase(originalPassword)){
            if(mViewType.equalsIgnoreCase("SYNC")){
                dismiss();
                Intent syncIntent = new Intent(context, DM_ManualSync.class);
                startActivity(syncIntent);
            }else if(mViewType.equalsIgnoreCase("TABLE")){
                FragmentManager localFragmentManager = getActivity().getSupportFragmentManager();
                TableSelectionViewFragment tableSelectionViewFragment = new TableSelectionViewFragment();
                tableSelectionViewFragment.show(localFragmentManager, "TableSelectionFragment");
                dismiss();
            }else if(mViewType.equalsIgnoreCase("ORDER")){
                AppConstants.isPasswordValidated = true;
                dismiss();
            }else if(mViewType.equalsIgnoreCase("LOGOUT")){
                AppConstants.isPasswordValidated = true;
                mAppManager.setLoggedIn(false);

                myClip = ClipData.newPlainText("text", "");
                myClipboard.setPrimaryClip(myClip);

                Intent mIntent = new Intent(context, DM_Login.class);
                startActivity(mIntent);
                getActivity().finish();
                dismiss();
            }
        }else{
            mEdtUserPassword.setError("Please enter valid password");
        }
    }
}
