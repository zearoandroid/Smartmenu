package com.zearoconsulting.smartmenu.presentation.view.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.domain.net.NetworkDataRequestThread;
import com.zearoconsulting.smartmenu.presentation.model.KOTLineItems;
import com.zearoconsulting.smartmenu.presentation.model.Organization;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.model.Tables;
import com.zearoconsulting.smartmenu.presentation.presenter.RemoveCartListener;
import com.zearoconsulting.smartmenu.presentation.view.activity.DM_Menu;
import com.zearoconsulting.smartmenu.presentation.view.adapter.DMCartAdapter;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.presentation.view.dialogs.NetworkErrorDialog;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.Common;
import com.zearoconsulting.smartmenu.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by saravanan on 01-11-2016.
 */

public class CartViewFragment extends AbstractDialogFragment {

    TextView mTableNameView;
    TextView mTotalPriceView;
    FancyButton mBtnSupmit;
    FancyButton mBtnCancel;
    RecyclerView cartListView;

    List<KOTLineItems> mKOTLineItemList;
    private DMCartAdapter mCartAdapter;
    private double totalPrice;
    private String mShowPrice;

    RemoveCartListener removeCartListener = new RemoveCartListener() {
        @Override
        public void OnRemoveCartListener(KOTLineItems kotModel) {
            try {
                //delete the selected item
                mDBHelper.deleteKOTLineItem(AppConstants.tableID, kotModel.getKotLineId());

                //update the cart count number
                ((OnCartUpdatedListener) getActivity()).onCartUpdated();

                //update the total price
                updateTotal();

            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    };

    final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.getData().getInt("Type");
            String jsonStr = msg.getData().getString("OUTPUT");

            switch (type) {
                case AppConstants.POST_KOT_DATA:
                    mParser.parseKOTResponse(jsonStr, mHandler);
                    break;
                case AppConstants.POST_KOT_DATA_RESPONSE:
                    mDBHelper.updateKOTLineItems(AppConstants.tableID);
                    mDBHelper.deleteKOTLineItems(AppConstants.tableID);
                    AppConstants.tableID = 0;
                    AppConstants.noOfCovers = 0;
                    gotoMainPage();
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
        return inflater.inflate(R.layout.cart_layout, container, false);
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

        this.mTableNameView = ((TextView) paramView.findViewById(R.id.title));
        this.mTotalPriceView = ((TextView) paramView.findViewById(R.id.total));
        this.mBtnSupmit = ((FancyButton) paramView.findViewById(R.id.submit));
        this.mBtnCancel = ((FancyButton) paramView.findViewById(R.id.cancel));
        this.cartListView = ((RecyclerView) paramView.findViewById(R.id.cart_table));

        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;

        Tables tables = mDBHelper.getTableData(mAppManager.getClientID(),mAppManager.getOrgID(),AppConstants.tableID);
        if(tables!=null)
            this.mTableNameView.setText(tables.getTableName()+" ("+String.valueOf(AppConstants.tableID)+") ");

        mKOTLineItemList = mDBHelper.getKOTLineItems(AppConstants.tableID);

        for(int i=0; i<mKOTLineItemList.size(); i++){
            List<Product> extraLineItems = mDBHelper.getKOTExtraLineItems(AppConstants.tableID, mKOTLineItemList.get(i).getKotLineId());

            double price = 0;

            for(int j=0; j<extraLineItems.size(); j++){
                price = price + extraLineItems.get(j).getTotalPrice();
            }

            mKOTLineItemList.get(i).setTotalPrice(mKOTLineItemList.get(i).getTotalPrice()+price);
        }

        Organization organization = mDBHelper.getOrganizationDetail(mAppManager.getOrgID());
        mShowPrice = organization.getShowPrice();

        mCartAdapter = new DMCartAdapter(mKOTLineItemList, mShowPrice);
        cartListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DefaultItemAnimator animator = new DefaultItemAnimator();
        //animator.setRemoveDuration(1000);
        //cartListView.setItemAnimator(animator);
        cartListView.setAdapter(mCartAdapter);

        mProDlg = new ProgressDialog(getActivity());
        mProDlg.setIndeterminate(true);
        mProDlg.setCancelable(false);

        //set the addcart listener
        mCartAdapter.setOnRemoveCartListener(removeCartListener);

        //updateTotal
        updateTotal();

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
                                CartViewFragment.this.dismiss();
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
                                postKOTData();
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

    }

    private void updateTotal() {
        try {
            totalPrice = mDBHelper.sumOfCartItemsTotal(AppConstants.tableID);
            if(mShowPrice.equalsIgnoreCase("Y")) {
                this.mTotalPriceView.setText("Total: " + AppConstants.currencyCode + " " + Common.valueFormatter(totalPrice));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postKOTData() {

        if(!AppConstants.isPasswordValidated){
            FragmentManager localFragmentManager = getActivity().getSupportFragmentManager();
            SecurityCodeConfirmationFragment.newInstance(getActivity(), "ORDER").show(localFragmentManager, "ORDER");
        }else{
            if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
                try {

                    mProDlg.setMessage("Posting data...");
                    mProDlg.show();

                    JSONObject mJsonObj = mParser.getParams(AppConstants.POST_KOT_DATA);
                    mJsonObj.put("tableId", AppConstants.tableID);
                    mJsonObj.put("coversCount", AppConstants.noOfCovers);
                    mJsonObj.put("kotType", "SM");
                    mJsonObj.put("tokens", mParser.getKOTOrderItems());

                    JSONArray tokenArray = mParser.getKOTOrderItems();

                    if(tokenArray.length()!=0){
                        Log.i("KOTJson", mJsonObj.toString());

                        NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.POST_KOT_DATA);
                        thread.start();
                    }else {
                        AppConstants.tableID = 0;
                        gotoMainPage();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //show network failure dialog or toast
                NetworkErrorDialog.buildDialog(getActivity()).show();
            }
        }
    }

    public interface OnCartUpdatedListener {
        public void onCartUpdated();
    }

    private void gotoMainPage(){
        mProDlg.dismiss();
        //update the cart count number
        ((OnCartUpdatedListener) getActivity()).onCartUpdated();
        dismiss();

        AppConstants.isPasswordValidated = false;

        Intent i = new Intent(getActivity(), DM_Menu.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}
