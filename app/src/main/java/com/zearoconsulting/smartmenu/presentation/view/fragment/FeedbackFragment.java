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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.domain.net.NetworkDataRequestThread;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.presentation.view.dialogs.NetworkErrorDialog;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.Common;
import com.zearoconsulting.smartmenu.utils.NetworkUtil;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONObject;

import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by saravanan on 16-11-2016.
 */

public class FeedbackFragment extends AbstractDialogFragment{

    private DiscreteSeekBar hospitalitySeek;
    private DiscreteSeekBar productsSeek;
    private DiscreteSeekBar serviceSeek;
    private DiscreteSeekBar environmentSeek;
    private EditText txtComments;
    private EditText txtEmail;
    private EditText txtName;
    private EditText txtPhone;
    private TextView valHospitality;
    private TextView valProducts;
    private TextView valService;
    private TextView valEnvironment;
    private FancyButton mBtnSupmit;
    private FancyButton mBtnCancel;

    final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.getData().getInt("Type");
            String jsonStr = msg.getData().getString("OUTPUT");

            switch (type) {
                case AppConstants.POST_FEEDBACK_DATA:
                    mParser.parseFeedbackResponse(jsonStr, mHandler);
                    break;
                case AppConstants.FEEDBACK_RESPONSE_RECEIVED:
                    clearFeedback();
                    mProDlg.dismiss();
                    dismiss();
                    break;
                case AppConstants.SERVER_ERROR:
                    mProDlg.dismiss();
                    //show the server error dialog
                    Toast.makeText(getActivity(), "Server data error", Toast.LENGTH_SHORT).show();
                    dismiss();
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
        return inflater.inflate(R.layout.feedback_layout, container, false);
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

        mProDlg = new ProgressDialog(getActivity());
        mProDlg.setIndeterminate(true);
        mProDlg.setCancelable(false);

        this.mBtnSupmit = ((FancyButton) paramView.findViewById(R.id.submit));
        this.mBtnCancel = ((FancyButton) paramView.findViewById(R.id.cancel));
        this.hospitalitySeek = ((DiscreteSeekBar)paramView.findViewById(R.id.seekHospitality));
        this.productsSeek = ((DiscreteSeekBar)paramView.findViewById(R.id.seekProducts));
        this.serviceSeek = ((DiscreteSeekBar)paramView.findViewById(R.id.seekService));
        this.environmentSeek = ((DiscreteSeekBar)paramView.findViewById(R.id.seekEnvironment));
        this.valHospitality = ((TextView)paramView.findViewById(R.id.valHospitality));
        this.valProducts = ((TextView)paramView.findViewById(R.id.valProducts));
        this.valService = ((TextView)paramView.findViewById(R.id.valService));
        this.valEnvironment = ((TextView)paramView.findViewById(R.id.valEnvironment));
        this.txtComments = ((EditText)paramView.findViewById(R.id.editComments));
        this.txtName = ((EditText)paramView.findViewById(R.id.editName));
        this.txtPhone = ((EditText)paramView.findViewById(R.id.editPhone));
        this.txtEmail = ((EditText)paramView.findViewById(R.id.editEmail));

        AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;

        //set all seekbar min value
        this.hospitalitySeek.setMin(1);
        this.productsSeek.setMin(1);
        this.serviceSeek.setMin(1);
        this.environmentSeek.setMin(1);

        //set all seekbar max value
        this.hospitalitySeek.setMax(5);
        this.productsSeek.setMax(5);
        this.serviceSeek.setMax(5);
        this.environmentSeek.setMax(5);

        //hospitalitySeek onProgressChangeListener
        this.hospitalitySeek.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener()
        {
            public void onProgressChanged(DiscreteSeekBar paramAnonymousDiscreteSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
            {
                FeedbackFragment.this.hospitalitySeek.setProgress(paramAnonymousInt);
                FeedbackFragment.this.valHospitality.setText(String.valueOf(paramAnonymousInt) + "/5");
            }

            public void onStartTrackingTouch(DiscreteSeekBar paramAnonymousDiscreteSeekBar) {}

            public void onStopTrackingTouch(DiscreteSeekBar paramAnonymousDiscreteSeekBar) {}
        });

        //productsSeek onProgressChangeListener
        this.productsSeek.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener()
        {
            public void onProgressChanged(DiscreteSeekBar paramAnonymousDiscreteSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
            {
                FeedbackFragment.this.productsSeek.setProgress(paramAnonymousInt);
                FeedbackFragment.this.valProducts.setText(String.valueOf(paramAnonymousInt) + "/5");
            }

            public void onStartTrackingTouch(DiscreteSeekBar paramAnonymousDiscreteSeekBar) {}

            public void onStopTrackingTouch(DiscreteSeekBar paramAnonymousDiscreteSeekBar) {}
        });

        //serviceSeek onProgressChangeListener
        this.serviceSeek.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener()
        {
            public void onProgressChanged(DiscreteSeekBar paramAnonymousDiscreteSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
            {
                FeedbackFragment.this.serviceSeek.setProgress(paramAnonymousInt);
                FeedbackFragment.this.valService.setText(String.valueOf(paramAnonymousInt) + "/5");
            }

            public void onStartTrackingTouch(DiscreteSeekBar paramAnonymousDiscreteSeekBar) {}

            public void onStopTrackingTouch(DiscreteSeekBar paramAnonymousDiscreteSeekBar) {}
        });

        //environmentSeek onProgressChangeListener
        this.environmentSeek.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener()
        {
            public void onProgressChanged(DiscreteSeekBar paramAnonymousDiscreteSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
            {
                FeedbackFragment.this.environmentSeek.setProgress(paramAnonymousInt);
                FeedbackFragment.this.valEnvironment.setText(String.valueOf(paramAnonymousInt) + "/5");
            }

            public void onStartTrackingTouch(DiscreteSeekBar paramAnonymousDiscreteSeekBar) {}

            public void onStopTrackingTouch(DiscreteSeekBar paramAnonymousDiscreteSeekBar) {}
        });

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
                                FeedbackFragment.this.dismiss();
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
                                postFeedbackData();
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });


    }

    public void clearFeedback()
    {
        this.valHospitality.setText("1/5");
        this.valProducts.setText("1/5");
        this.valEnvironment.setText("1/5");
        this.valService.setText("1/5");
        this.txtComments.setText("");
        this.txtEmail.setText("");
        this.txtName.setText("");
        this.txtPhone.setText("");
        this.hospitalitySeek.setProgress(1);
        this.productsSeek.setProgress(1);
        this.environmentSeek.setProgress(1);
        this.serviceSeek.setProgress(1);
    }

    private void postFeedbackData(){

        if(txtName.getText().toString().trim().equals("")){
            txtName.setError("name should not be empty");
            return;
        }else if(txtEmail.getText().toString().trim().equals("")){
            txtEmail.setError("Password should not be empty");
            return;
        }

        if(!Common.isEmailIdValid(txtEmail.getText().toString().trim())){
            txtEmail.setError(getString(R.string.email_address_error));
            return;
        }else{
            if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {
                try {

                    mProDlg.setMessage("Posting feedback data...");
                    mProDlg.show();

                    JSONObject mJsonObj = mParser.getParams(AppConstants.POST_FEEDBACK_DATA);
                    JSONObject mFeedbackObj = mParser.getFeedbackObj(this.hospitalitySeek.getProgress(),this.productsSeek.getProgress(),this.serviceSeek.getProgress(),this.environmentSeek.getProgress(),txtComments.getText().toString(),txtName.getText().toString(),txtPhone.getText().toString(),txtEmail.getText().toString());
                    mJsonObj.put("Feedback", mFeedbackObj);
                    Log.i("FeedbackJson", mJsonObj.toString());

                    NetworkDataRequestThread thread = new NetworkDataRequestThread(AppConstants.URL, "", mHandler, mJsonObj.toString(), AppConstants.POST_FEEDBACK_DATA);
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //show network failure dialog or toast
                NetworkErrorDialog.buildDialog(getActivity()).show();
            }
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
