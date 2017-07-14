package com.zearoconsulting.smartmenu.presentation.view.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.bumptech.glide.Glide;
import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.domain.receivers.KOTDataReceiver;
import com.zearoconsulting.smartmenu.domain.services.KOTService;
import com.zearoconsulting.smartmenu.domain.services.TableStatusService;
import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.model.Organization;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.presentation.view.fragment.CartViewFragment;
import com.zearoconsulting.smartmenu.presentation.view.fragment.FeedbackFragment;
import com.zearoconsulting.smartmenu.presentation.view.fragment.InfoFragment;
import com.zearoconsulting.smartmenu.presentation.view.fragment.SecurityCodeConfirmationFragment;
import com.zearoconsulting.smartmenu.presentation.view.fragment.TableSelectionViewFragment;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.FileUtils;

import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class DM_Menu extends DMBaseActivity implements OnMenuItemClickListener {

    private FancyButton infoButton;
    private FancyButton feedbackButton;
    private FancyButton languageButton;
    private FancyButton tableSelectionButton;
    private FancyButton syncButton;
    private FancyButton logoutButton;
    private Button mMenuButton;
    private Button batteryDisplay;
    private BroadcastReceiver BatteryLevelReceiver;
    private int mProgressStatus = 0;
    private Intent mIntent;
    TableSelectionViewFragment tableSelectionViewFragment;
    FeedbackFragment feedbackFragment;
    InfoFragment infoFragment;
    Fragment foundFragment;
    FragmentManager localFragmentManager;

    private int mAppMode;
    ImageView mLayoutMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dm__menu);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mReboundListener = new ReboundListener();

        feedbackButton = (FancyButton) findViewById(R.id.feedbackButton);
        infoButton = (FancyButton) findViewById(R.id.infoButton);
        languageButton = (FancyButton) findViewById(R.id.languageButton);
        tableSelectionButton = (FancyButton) findViewById(R.id.tableSelectionButton);
        syncButton = (FancyButton) findViewById(R.id.manualSyncButton);
        logoutButton = (FancyButton) findViewById(R.id.logoutButton);
        mMenuButton = (Button) findViewById(R.id.menu);
        batteryDisplay = (Button) findViewById(R.id.batteryDisplay);
        mLayoutMenu = (ImageView) findViewById(R.id.background);

        localFragmentManager = getSupportFragmentManager();
        foundFragment = getFragmentManager().findFragmentByTag("TableSelectionFragment");

        //mIntent = new Intent(this, TableStatusService.class);
        //this.startService(mIntent);

        //get the image from sdcard and set to background

        Organization organization = mDBHelper.getOrganizationDetail(mAppManager.getOrgID());
        //Resources res = getResources();
        //Bitmap bitmaps = BitmapFactory.decodeFile(organization.getOrgImage());
        //BitmapDrawable bd = new BitmapDrawable(res, bitmaps);
        //mLayoutMenu.setBackgroundDrawable(bd);

        if (organization == null) {
            Toast.makeText(DM_Menu.this, "Data not available. Please give manual sync.", Toast.LENGTH_LONG).show();
        } else {
            Glide.with(DM_Menu.this)
                    .load(organization.getOrgImage())
                    .into(mLayoutMenu);
        }

        boolean isAvail = mDBHelper.checkAllCategory();
        if (!isAvail) {
            Category category = new Category();
            category.setCategoryId(0);
            category.setCategoryName("All (Quick Menu)");
            category.setCategoryValue("All (Quick Menu)");

            // Retrieve the image from the res folder
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_product);
            String imagePath = FileUtils.storeImage("", 0, bitmap);
            category.setCategoryImage(imagePath);
            category.setShowDigitalMenu("Y");
            category.setCategoryArabicName("القائمة السريعة");
            category.setClientId(mAppManager.getClientID());
            category.setOrgId(mAppManager.getOrgID());

            //add category
            mDBHelper.addCategory(category);
        }


        // Add an OnTouchListener to the root view.
        infoButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(infoButton);
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
                                infoFragment = new InfoFragment();
                                infoFragment.show(localFragmentManager, "InfoFragment");
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

        // Add an OnTouchListener to the root view.
        feedbackButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(feedbackButton);
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
                                feedbackFragment = new FeedbackFragment();
                                feedbackFragment.show(localFragmentManager, "FeedbackFragment");
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

        // Add an OnTouchListener to the root view.
        languageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(languageButton);
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
                                //show language selection popup
                                PopupMenu popupMenu = new PopupMenu(DM_Menu.this, languageButton);
                                popupMenu.setOnMenuItemClickListener(DM_Menu.this);
                                popupMenu.inflate(R.menu.language);
                                popupMenu.show();
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

        // Add an OnTouchListener to the root view.
        tableSelectionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(tableSelectionButton);
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
                                mAppMode = mAppManager.getAppMode();
                                if (mAppMode == 1)
                                    SecurityCodeConfirmationFragment.newInstance(DM_Menu.this, "TABLE").show(localFragmentManager, "TABLE");
                                else if (mAppMode == 0) {
                                    TableSelectionViewFragment tableSelectionViewFragment = new TableSelectionViewFragment();
                                    tableSelectionViewFragment.show(localFragmentManager, "TableSelectionFragment");
                                }
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

        // Add an OnTouchListener to the root view.
        syncButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(syncButton);
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
                                SecurityCodeConfirmationFragment.newInstance(DM_Menu.this, "SYNC").show(localFragmentManager, "SYNC");
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

        // Add an OnTouchListener to the root view.
        logoutButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(logoutButton);
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
                                mAppMode = mAppManager.getAppMode();
                                if (mAppMode == 1)
                                    SecurityCodeConfirmationFragment.newInstance(DM_Menu.this, "LOGOUT").show(localFragmentManager, "LOGOUT");
                                else if (mAppMode == 0) {
                                    logout();
                                }
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

        // Add an OnTouchListener to the root view.
        mMenuButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(mMenuButton);
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
                                navigateToCategories();
                                //mDBHelper.updateTempTerminal();
                                /*if (!mAppManager.getRoleName().equalsIgnoreCase("waiter")) {
                                    Intent intent = new Intent(DM_Menu.this, DM_Categories.class);
                                    startActivity(intent);
                                } else if (mAppManager.getRoleName().equalsIgnoreCase("waiter")) {
                                    if (AppConstants.tableID == 0) {
                                        Toast.makeText(DM_Menu.this, "Select Table", Toast.LENGTH_LONG).show();
                                    } else if (AndroidApplication.getInstance().getSelectedCoverList().size() == 0) {
                                        Toast.makeText(DM_Menu.this, "Select Covers", Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent intent = new Intent(DM_Menu.this, DM_Categories.class);
                                        startActivity(intent);
                                    }
                                }*/

                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });
    }

    public void navigateToCategories() {
        if (!mAppManager.getRoleName().equalsIgnoreCase("waiter")) {
            Intent intent = new Intent(DM_Menu.this, DM_Categories.class);
            startActivity(intent);
        } else if (mAppManager.getRoleName().equalsIgnoreCase("waiter")) {
            if (AppConstants.tableID == 0) {
                Toast.makeText(DM_Menu.this, "Select Table", Toast.LENGTH_LONG).show();
            } else if (AndroidApplication.getInstance().getSelectedCoverList().size() == 0) {
                Toast.makeText(DM_Menu.this, "Select Covers", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(DM_Menu.this, DM_Categories.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Add a listener to the spring
        mSpring.addListener(mReboundListener);

        //register receiver
        registerReceivers();

        //check the role and show the table
        if (AppConstants.tableID == 0 && mAppManager.getRoleName().equalsIgnoreCase("waiter")) {
            //show the table selection dialog
            //FragmentManager localFragmentManager = getSupportFragmentManager();
            //new TableSelectionViewFragment().show(localFragmentManager, "TableSelectionFragment");

            //foundFragment = getFragmentManager().findFragmentByTag("TableSelectionFragment");
            if (tableSelectionViewFragment == null) {
                tableSelectionViewFragment = new TableSelectionViewFragment();
                tableSelectionViewFragment.show(localFragmentManager, "TableSelectionFragment");
            }
        }

        hideSoftKeyboard();

    }

    @Override
    protected void onPause() {
        super.onPause();

        //Remove a listener to the spring
        mSpring.removeListener(mReboundListener);

        //unregister receiver
        unregisterReceivers();
    }

    public void registerReceivers() {

        this.BatteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context paramAnonymousContext, Intent intent) {

                // Get the battery scale
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                // get the battery level
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);


                // Calculate the battery charged percentage
                float percentage = level / (float) scale;
                mProgressStatus = (int) ((percentage) * 100);

                // Show the battery charged percentage text inside progress bar
                batteryDisplay.setText("%" + mProgressStatus);

                //check if connected to power
                int status = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

                if (status != 0) {
                    if (mProgressStatus < 20) {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_0_charging);
                    } else if (mProgressStatus < 40) {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_20);
                    } else if (mProgressStatus < 60) {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_40);
                    } else if (mProgressStatus < 80) {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_60);
                    } else if (mProgressStatus < 100) {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_80);
                    } else {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_100);
                    }
                } else {
                    if (mProgressStatus < 20) {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_0);
                    } else if (mProgressStatus < 40) {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_20);
                    } else if (mProgressStatus < 60) {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_40);
                    } else if (mProgressStatus < 80) {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_60);
                    } else if (mProgressStatus < 100) {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_80);
                    } else {
                        batteryDisplay.setBackgroundResource(R.drawable.battery_100);
                    }
                }


            }
        };

        IntentFilter localIntentFilter1 = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        registerReceiver(this.BatteryLevelReceiver, localIntentFilter1);
    }

    public void unregisterReceivers() {
        try {
            unregisterReceiver(this.BatteryLevelReceiver);
        } catch (Exception localException) {
            localException.printStackTrace();
            //Crashlytics.log("MainActivity - unregisterReceiver");
        }
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.english:
                String languageToLoad = "en"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                AndroidApplication.setLanguage(languageToLoad);
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());

                Intent intent = new Intent(DM_Menu.this, DM_Menu.class);
                startActivity(intent);
                finish();
                break;
            case R.id.arabic:
                languageToLoad = "ar"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                AndroidApplication.setLanguage(languageToLoad);
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());

                intent = new Intent(DM_Menu.this, DM_Menu.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    public void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DM_Menu.this);

        alertDialog.setTitle("Logout"); // Sets title for your alertbox

        alertDialog.setMessage("Are you sure you want to Logout ?"); // Message to be displayed on alertbox

        alertDialog.setIcon(R.mipmap.ic_launcher); // Icon for your alertbox

        /* When positive (yes/ok) is clicked */
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mAppManager.setLoggedIn(false);
                mAppManager.setRemindMe("N");
                Intent mIntent = new Intent(DM_Menu.this, DM_Login.class);
                startActivity(mIntent);
                finish();
            }
        });

        /* When negative (No/cancel) button is clicked*/
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}
