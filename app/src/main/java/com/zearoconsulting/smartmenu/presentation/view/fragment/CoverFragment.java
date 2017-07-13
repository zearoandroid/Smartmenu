package com.zearoconsulting.smartmenu.presentation.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.KOTLineItems;
import com.zearoconsulting.smartmenu.presentation.model.Tables;
import com.zearoconsulting.smartmenu.presentation.presenter.CoverSelectListener;
import com.zearoconsulting.smartmenu.presentation.view.adapter.DMCoverAdapter;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by saravanan on 23-12-2016.
 */

public class CoverFragment extends AbstractDialogFragment {
    List<Tables> mKOTCoverList;
    List<Long> mKOTCoverIds;
    private DMCoverAdapter mCoverAdapter;

    private FancyButton mBtnSubmit, mBtnCancel;
    //private EditText mEdtCoverCount;
    private RecyclerView coverListView;
    private CheckBox mSelectAllCovers;
    private TextView mTxtUserName;
    private TextView selectedTableName;

    CoverSelectListener onCoverSelectListener = new CoverSelectListener() {
        @Override
        public void OnSelectedCover(Tables coverEntity) {
            if (coverEntity.getOrderAvailable().equalsIgnoreCase("N")) {
                AndroidApplication.getInstance().setSelectedCoverList(coverEntity.getTableId(), true);
            }
        }

        @Override
        public void CoverSelected() {
            dismissAllowingStateLoss();
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

        //Clear Previous Selected Cover List
        AndroidApplication.getInstance().clearCoverList();

        this.coverListView = ((RecyclerView) paramView.findViewById(R.id.cover_selection));
        this.mSelectAllCovers = (CheckBox) paramView.findViewById(R.id.select_all_covers);

        this.mTxtUserName = (TextView) paramView.findViewById(R.id.txtUserName);
        this.selectedTableName = (TextView) paramView.findViewById(R.id.selected_table_name);
        mTxtUserName.setText(("Hello " + mAppManager.getUserName()));
        selectedTableName.setText(("Table Name:  "+TableSelectionViewFragment.mSelectedTable));

        mKOTCoverList = mDBHelper.getCovers(mAppManager.getClientID(), mAppManager.getOrgID(), AppConstants.tableID);
        mKOTCoverIds = mDBHelper.getCoverIDs(mAppManager.getClientID(), mAppManager.getOrgID(), AppConstants.tableID);

        //AppConstants.URL = AppConstants.kURLHttp + mAppManager.getServerAddress() + ":" + mAppManager.getServerPort() + AppConstants.kURLServiceName + AppConstants.kURLMethodApi;

        mCoverAdapter = new DMCoverAdapter(mKOTCoverList, mKOTCoverIds);
        coverListView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(1000);
        coverListView.setItemAnimator(animator);
        coverListView.setAdapter(mCoverAdapter);

        //set the table select listener
        mCoverAdapter.setOnCoverSelectListener(onCoverSelectListener);

        this.mBtnSubmit = (FancyButton) paramView.findViewById(R.id.submit);
        this.mBtnCancel = (FancyButton) paramView.findViewById(R.id.cancel);
        //this.mEdtCoverCount = (EditText) paramView.findViewById(R.id.edtCoverNo);

        this.mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Long> array = AndroidApplication.getInstance().getSelectedCoverList();
                for (Long l : array) {
                    Log.i("Selected ID: ", "" + l);
                }

                if (array.size() > 0) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    dismiss();
                } else {
                    Toast.makeText(AndroidApplication.getAppContext(), "Please select covers...", Toast.LENGTH_LONG).show();
                }

                /*if (mEdtCoverCount.getText().toString().trim().length() != 0) {
                    AppConstants.noOfCovers = Integer.parseInt(mEdtCoverCount.getText().toString());
                    // Check if no view has focus:
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } else
                    mEdtCoverCount.setError("Please enter number of covers");*/
            }
        });

        this.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidApplication.getInstance().clearCoverList();
                dismiss();
            }
        });

        mSelectAllCovers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (Tables covers : mKOTCoverList) {
                        AndroidApplication.getInstance().setSelectedCoverList(covers.getTableId(), false);
                    }
                    mCoverAdapter.selectAllCovers(true);
                } else {
                    for (Tables covers : mKOTCoverList) {
                        AndroidApplication.getInstance().setSelectedCoverList(covers.getTableId(), true);
                    }
                    mCoverAdapter.selectAllCovers(false);
                }
            }
        });
        List<KOTLineItems> kotLineItemList = mDBHelper.getKOTLineItems(AppConstants.tableID);
        if (kotLineItemList.size() > 0) {
            mSelectAllCovers.setVisibility(View.GONE);
        }

        mCoverAdapter.updateCovers();
    }
}
