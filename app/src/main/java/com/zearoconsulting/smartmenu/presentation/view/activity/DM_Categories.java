package com.zearoconsulting.smartmenu.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.model.KOTLineItems;
import com.zearoconsulting.smartmenu.presentation.model.POSLineItem;
import com.zearoconsulting.smartmenu.presentation.model.POSOrders;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.presenter.IDMListeners;
import com.zearoconsulting.smartmenu.presentation.view.adapter.DMCategoryAdapter;
import com.zearoconsulting.smartmenu.presentation.view.component.GridSpacingItemDecoration;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.presentation.view.fragment.CartViewFragment;
import com.zearoconsulting.smartmenu.utils.AppConstants;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class DM_Categories extends DMBaseActivity implements IDMListeners, CartViewFragment.OnCartUpdatedListener {

    private RecyclerView mCategoryRecyclerView;
    int spanCount = 0; // NUMBER OF COLUMNS
    int spacing = 0; // SPACING ISSUE
    private List<Category> mCategoryList;
    private DMCategoryAdapter mCategoryAdapter;
    private long mCategoryId = 0;
    private FancyButton mBackButton;
    private TextView mCartNumber;
    private FancyButton mCartButton;
    List<KOTLineItems> mKOTLineItemList;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the animation for page navigation
       // overridePendingTransition(R.anim.slide_in_right, R.anim.hold);

        setContentView(R.layout.activity_dm_category);

        mReboundListener = new ReboundListener();

        mBackButton = (FancyButton)findViewById(R.id.back_button);
        mCartButton = (FancyButton) findViewById(R.id.cart_button);
        mCartNumber = (TextView) findViewById(R.id.cart_number);
        mCategoryRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_category);
        mCategoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        spanCount = 3;
        spacing = 40;
        boolean includeEdge = true;
        mCategoryRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        mCategoryList = mDBHelper.getDMCategory(mAppManager.getClientID(),mAppManager.getOrgID());

        List<Product> productList = mDBHelper.getAllProduct(mAppManager.getClientID(),mAppManager.getOrgID());

        if (mCategoryList != null) {
            if (mCategoryAdapter == null && mCategoryList.size() != 0) {
                mCategoryAdapter = new DMCategoryAdapter(this, mCategoryList, this);
                mCategoryRecyclerView.setAdapter(mCategoryAdapter);
                mCategoryAdapter.notifyDataSetChanged();
            }
        }

        if (mAppManager.getRoleName().equalsIgnoreCase("waiter")) {
            mCartButton.setVisibility(View.VISIBLE);
        }else{
            mCartButton.setVisibility(View.GONE);
        }

        // Add an OnTouchListener to the root view.
        mBackButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(mBackButton);
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
                                //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                                finish();
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

        // Add an OnTouchListener to the root view.
        mCartButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(mCartButton);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // When pressed start solving the spring to 1.
                        mSpring.setEndValue(1);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // When released start solving the spring to 0.
                        mSpring.setEndValue(0);
                        mKOTLineItemList = mDBHelper.getKOTLineItems(AppConstants.tableID);

                        if(mKOTLineItemList.size()!=0){
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    FragmentManager localFragmentManager = getSupportFragmentManager();
                                    new CartViewFragment().show(localFragmentManager, "CartFragment");
                                }
                            }, 200);
                        }

                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Add a listener to the spring
        mSpring.addListener(mReboundListener);

        onCartUpdated();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Remove a listener to the spring
        mSpring.removeListener(mReboundListener);
    }

    @Override
    public void onSelectedCategory(Category category) {
        mCategoryId = category.getCategoryId();
        if(mCategoryId == 0)
            mIntent = new Intent(DM_Categories.this, Dm_Products.class);
        else
        mIntent = new Intent(DM_Categories.this, Dm_Products.class);

        //add data to a bundle
        Bundle extras = new Bundle();
        extras.putLong("categoryId", category.getCategoryId());
        extras.putString("categoryName", category.getCategoryName());
        extras.putString("categoryArabicName", category.getCategoryArabicName());
        //add bundle to intent
        mIntent.putExtras(extras);
        startActivity(mIntent);
    }

    @Override
    public void onSelectedItem(long categoryId, Product mProduct, int position) {

    }

    @Override
    public void onSelectedProductImage(int position) {

    }

    public void refreshCartNumber() {
        int qty = mDBHelper.sumOfCartItems(AppConstants.tableID);
        if (qty > 0) {
            mCartNumber.setVisibility(View.VISIBLE);
            mCartNumber.setText(String.valueOf(qty));
        } else {
            mCartNumber.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCartUpdated() {
        refreshCartNumber();
    }
}
