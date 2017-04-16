package com.zearoconsulting.smartmenu.presentation.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.utils.AppConstants;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by saravanan on 23-12-2016.
 */

public class CoverFragment extends AbstractDialogFragment {

    private FancyButton mBtnSubmit;
    private EditText mEdtCoverCount;

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
        return inflater.inflate(R.layout.cover_layout, container, false);
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
        getDialog().setCanceledOnTouchOutside(false);


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

        this.mBtnSubmit = (FancyButton) paramView.findViewById(R.id.submit);
        this.mEdtCoverCount = (EditText) paramView.findViewById(R.id.edtCoverNo);

        this.mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mEdtCoverCount.getText().toString().trim().length()!=0) {
                    AppConstants.noOfCovers = Integer.parseInt(mEdtCoverCount.getText().toString());

                    // Check if no view has focus:
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    dismiss();
                }else
                    mEdtCoverCount.setError("Please enter number of covers");
            }
        });

    }
}
