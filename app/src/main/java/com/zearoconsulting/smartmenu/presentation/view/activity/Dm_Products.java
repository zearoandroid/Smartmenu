package com.zearoconsulting.smartmenu.presentation.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.model.KOTLineItems;
import com.zearoconsulting.smartmenu.presentation.model.POSLineItem;
import com.zearoconsulting.smartmenu.presentation.model.POSOrders;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.presenter.IDMListeners;
import com.zearoconsulting.smartmenu.presentation.view.adapter.DMAllProductAdapter;
import com.zearoconsulting.smartmenu.presentation.view.adapter.DMCategoryItemAdapter;
import com.zearoconsulting.smartmenu.presentation.view.component.GridSpacingItemDecoration;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.presentation.view.fragment.AddtoCartViewFragment;
import com.zearoconsulting.smartmenu.presentation.view.fragment.CartViewFragment;
import com.zearoconsulting.smartmenu.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.zearoconsulting.smartmenu.utils.AppConstants.isTableVisible;

public class Dm_Products extends DMBaseActivity implements SearchView.OnQueryTextListener, IDMListeners, CartViewFragment.OnCartUpdatedListener {

    private RecyclerView mProductRecyclerView;
    private List<Product> mProductList;
    private long mCategoryId = 0;
    private String mCategoryName = "";
    private String mCategoryArabicName = "";
    private DMCategoryItemAdapter mProdAdapter = null;
    private DMAllProductAdapter mAllProdAdapter = null;
    private Product mProduct;

    int spanCount = 0; // NUMBER OF COLUMNS
    int spacing = 0; // SPACING ISSUE

    private FancyButton mBackButton;
    private TextView mTxtCategoryTitle;
    private TextView mCartNumber;
    private FancyButton mCartButton;
    List<KOTLineItems> mKOTLineItemList;
    FragmentManager localFragmentManager;
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dm__products);

        mReboundListener = new ReboundListener();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                mCategoryId = extras.getLong("categoryId");
                mCategoryName = extras.getString("categoryName");
                mCategoryArabicName = extras.getString("categoryArabicName");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        localFragmentManager = getSupportFragmentManager();

        mBackButton = (FancyButton) findViewById(R.id.back_button);
        mTxtCategoryTitle = (TextView) findViewById(R.id.categories_title);
        mProductRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_products);
        mCartButton = (FancyButton) findViewById(R.id.cart_button);
        mCartNumber = (TextView) findViewById(R.id.cart_number);
        mSearchView = (SearchView)findViewById(R.id.edtSearchProduct);

        mSearchView.setOnQueryTextListener(Dm_Products.this);

        if (AndroidApplication.getLanguage().equalsIgnoreCase("en"))
            mTxtCategoryTitle.setText(mCategoryName);
        else
            mTxtCategoryTitle.setText(mCategoryArabicName);

        mSearchView.setVisibility(View.GONE);

        if (mCategoryId != 0){
            mProductRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            spanCount = 2;
            spacing = 50;
            boolean includeEdge = true;
            mProductRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            mProductList = mDBHelper.getDMProduct(mAppManager.getClientID(), mAppManager.getOrgID(), mCategoryId);
        }else{
            mSearchView.setVisibility(View.VISIBLE);
            //Turn iconified to false:
            mSearchView.setIconified(false);
            mProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mProductRecyclerView.setBackgroundColor(Color.parseColor("#FFEBEDEF"));
            mProductList = mDBHelper.getAllProduct(mAppManager.getClientID(), mAppManager.getOrgID());
        }

        if (mProductList!=null) {
            if (mProdAdapter == null && mProductList.size() != 0 && mCategoryId!=0) {
                mProdAdapter = new DMCategoryItemAdapter(this, mProductList, mCategoryId, this);
                mProductRecyclerView.setAdapter(mProdAdapter);
                mProdAdapter.notifyDataSetChanged();
            }else{
                mAllProdAdapter = new DMAllProductAdapter(this, mProductList, mCategoryId, this);
                mProductRecyclerView.setAdapter(mAllProdAdapter);
                mAllProdAdapter.notifyDataSetChanged();
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

        isTableVisible = false;

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

    }

    @Override
    public void onSelectedItem(long categoryId, Product product, int position) {

        mProduct = product;
        if(categoryId!=0) {
            Intent intent = new Intent(Dm_Products.this, DM_SelectedProduct.class);
            //add data to a bundle
            Bundle extras = new Bundle();
            extras.putInt("position", position);
            extras.putLong("categoryId", mCategoryId);
            extras.putString("categoryName", mCategoryName);
            extras.putString("categoryArabicName", mCategoryArabicName);
            extras.putLong("productId", mProduct.getProdId());
            //add bundle to intent
            intent.putExtras(extras);
            startActivity(intent);
        }else {
            AddtoCartViewFragment addToCartDialog = new AddtoCartViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Page", "ProductList");
            addToCartDialog.setArguments(bundle);
            addToCartDialog.show(localFragmentManager, "addToCartFragment");
        }
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

    public Product getCurrentItem() {
        return mProduct;
    }

    public void gotoMenuPage() {
        Intent i = new Intent(Dm_Products.this, DM_Menu.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Product> filteredModelList = filter(mProductList, query);
        mAllProdAdapter.setFilter(filteredModelList);
        return true;
    }

    private List<Product> filter(List<Product> models, String query) {
        query = query.toLowerCase();

        final List<Product> filteredModelList = new ArrayList<>();
        for (Product model : models) {
            final String text = model.getProdName().toLowerCase();
            final String value = model.getProdValue();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }else if (value.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
