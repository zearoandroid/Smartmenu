package com.zearoconsulting.smartmenu.presentation.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.Organization;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by saravanan on 17-11-2016.
 */

public class InfoFragment extends AbstractDialogFragment {

    private FancyButton mBtnCancel;
    private TextView mTxtDescription;
    private TextView mTxtLoggedUser;

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

        Organization organization = mDBHelper.getOrganizationDetail(mAppManager.getOrgID());
        if(organization!=null)
            this.mTxtDescription.setText(organization.getOrgDescription());

        mTxtLoggedUser.setText("Hello "+mAppManager.getUserName());

        this.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    }
