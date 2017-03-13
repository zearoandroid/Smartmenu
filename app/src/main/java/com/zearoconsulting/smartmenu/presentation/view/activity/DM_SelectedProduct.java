package com.zearoconsulting.smartmenu.presentation.view.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.model.KOTLineItems;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.model.ProductMultiImage;
import com.zearoconsulting.smartmenu.presentation.presenter.AddCartListener;
import com.zearoconsulting.smartmenu.presentation.presenter.IDMListeners;
import com.zearoconsulting.smartmenu.presentation.view.adapter.DMBottomProductAdapter;
import com.zearoconsulting.smartmenu.presentation.view.adapter.DMProductAdapter;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.presentation.view.fragment.AddtoCartViewFragment;
import com.zearoconsulting.smartmenu.presentation.view.fragment.CartViewFragment;
import com.zearoconsulting.smartmenu.presentation.view.fragment.FeedbackFragment;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.zearoconsulting.smartmenu.utils.AppConstants.isTableVisible;

public class DM_SelectedProduct extends DMBaseActivity implements IDMListeners, CartViewFragment.OnCartUpdatedListener{

    AddCartListener addCartListener = new AddCartListener() {
        @Override
        public void OnAddCartListener(Product productEntity, boolean isChecked) {
            setCurrentItem(productEntity);
        }
    };

    private Product mProduct;
    private Context context;
    private long mCategoryId = 0;
    private long mProductId = 0;
    private int selectedItemPos = 0;
    private String mCategoryName = "";
    private String mCategoryArabicName = "";
    FeedbackFragment feedbackFragment;
    FragmentManager localFragmentManager;
    private boolean isOpen;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private ViewPager mPager;
    private TextView mCartNumber;
    private DMProductAdapter mDMProdAdapter;
    private DMBottomProductAdapter mDMBottomProdAdapter;
    private RecyclerView mBottomRecyclerView;
    private RelativeLayout mBottomLayout;
    private Button mCategoryButton;
    private ImageButton mUpDownButton;
    private Button mFeedbackButton;
    private FancyButton mBackButton;
    private FancyButton mCartButton;
    private Button mFBButton;
    private FancyButton mAddButton;
    List<KOTLineItems> mKOTLineItemList;
    List<ProductMultiImage> mMultiImageList;

    private LinearLayout caloryLayout;
    private TextView caloryText;
    private TextView description;
    private LinearLayout priceLayout;
    private TextView priceText;
    private LinearLayout timeLayout;
    private TextView timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dm__selected_product);

        mReboundListener = new ReboundListener();

        this.context = this;


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                mCategoryId = extras.getLong("categoryId");
                mCategoryName = extras.getString("categoryName");
                mCategoryArabicName = extras.getString("categoryArabicName");
                //selectedItemPos = extras.getInt("position");
                mProductId= extras.getLong("productId");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        localFragmentManager = getSupportFragmentManager();

        //setView ID's
        mBackButton = (FancyButton) findViewById(R.id.back_button);
        mCartButton = (FancyButton) findViewById(R.id.cart_button);
        mAddButton = (FancyButton) findViewById(R.id.add_button);
        mFBButton = (Button)findViewById(R.id.fb_button);
        mCartNumber = (TextView) findViewById(R.id.cart_number);
        mPager = (ViewPager) findViewById(R.id.pager);
        mBottomRecyclerView = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        mBottomLayout = (RelativeLayout) findViewById(R.id.container);
        mCategoryButton = (Button) findViewById(R.id.category_btn);
        mUpDownButton = (ImageButton) findViewById(R.id.updownButton);
        mFeedbackButton = (Button)findViewById(R.id.feedback_btn);

        caloryLayout = (LinearLayout)findViewById(R.id.calory_layout);
        timeLayout = (LinearLayout)findViewById(R.id.time_layout);
        priceLayout = (LinearLayout)findViewById(R.id.price_layout);
        description = (TextView)findViewById(R.id.description);
        caloryText = (TextView)findViewById(R.id.calory_text);
        timeText = (TextView)findViewById(R.id.time_text);
        priceText = (TextView)findViewById(R.id.price_text);

        mProduct = mDBHelper.getProduct(mAppManager.getClientID(),mAppManager.getOrgID(),mProductId);

        if(AndroidApplication.getLanguage().equalsIgnoreCase("en")) {
            mCategoryButton.setText(mProduct.getProdName());

            priceText.setText(AppConstants.currencyCode+" "+ Common.valueFormatter(mProduct.getSalePrice()));

            if(!mProduct.getCalories().equalsIgnoreCase("")){
                caloryLayout.setVisibility(View.VISIBLE);
                caloryText.setText(mProduct.getCalories());
            }

            if(!mProduct.getPreparationTime().equalsIgnoreCase("")){
                timeLayout.setVisibility(View.VISIBLE);
                timeText.setText(mProduct.getPreparationTime());
            }

            description.setText(mProduct.getDescription());

        }else {
            mCategoryButton.setText(mProduct.getProdArabicName());

            priceText.setText(AppConstants.currencyCode+" "+ Common.valueFormatter(mProduct.getSalePrice()));

            if(!mProduct.getProdArabicCalories().equalsIgnoreCase("")){
                caloryLayout.setVisibility(View.VISIBLE);
                caloryText.setText(mProduct.getProdArabicCalories());
            }

            if(!mProduct.getProdArabicPreTime().equalsIgnoreCase("")){
                timeLayout.setVisibility(View.VISIBLE);
                timeText.setText(mProduct.getProdArabicPreTime());
            }

            description.setText(mProduct.getProdArabicDescription());
        }

        mMultiImageList = mDBHelper.getProductImages(mProductId);
        mProduct.setImageList(mMultiImageList);

        mDMProdAdapter = new DMProductAdapter(this, mMultiImageList, mProduct.getProdName(), mProduct.getProdArabicName());
        mDMBottomProdAdapter = new DMBottomProductAdapter(this, mMultiImageList, this);
        mPager.setAdapter(mDMProdAdapter);

        if (mAppManager.getRoleName().equalsIgnoreCase("waiter")) {
            mCartButton.setVisibility(View.VISIBLE);
        }else{
            mCartButton.setVisibility(View.GONE);
        }

        //set the page of selected item from previous activity
        mPager.setCurrentItem(selectedItemPos);

        final LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(DM_SelectedProduct.this, LinearLayoutManager.HORIZONTAL, false);
        mBottomRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        mBottomRecyclerView.setAdapter(mDMBottomProdAdapter);
        mDMBottomProdAdapter.highLightItem(selectedItemPos);
        horizontalLayoutManagaer.scrollToPositionWithOffset(selectedItemPos, mMultiImageList.size());

        isOpen = true;

        mCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideBottomView();
            }
        });

        mUpDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideBottomView();
            }
        });

        mFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackFragment = new FeedbackFragment();
                feedbackFragment.show(localFragmentManager, "FeedbackFragment");
            }
        });


        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            // optional
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            // optional
            @Override
            public void onPageSelected(int position) {
                //change the bottom view
                selectedItemPos = position;

                int totalVisibleItems = horizontalLayoutManagaer.findLastVisibleItemPosition() - horizontalLayoutManagaer.findFirstVisibleItemPosition();
                int centeredItemPosition = totalVisibleItems / 2;
                mBottomRecyclerView.smoothScrollToPosition(position);
                mBottomRecyclerView.setScrollY(centeredItemPosition );

                mDMBottomProdAdapter.highLightItem(selectedItemPos);
            }

            // optional
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Add an OnTouchListener to the root view.
        mAddButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(mAddButton);
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
                                AddtoCartViewFragment addToCartDialog = new AddtoCartViewFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("Page", "ProductDetail");
                                addToCartDialog.setArguments(bundle);
                                addToCartDialog.show(localFragmentManager, "addToCartFragment");
                                //new AddtoCartViewFragment().show(localFragmentManager, "addToCartFragment");
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

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

        mFBButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(mFBButton);
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
                                launchFacebookDialog(mProduct);
                            }
                        }, 200);
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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                showHideBottomView();
            }
        }, 1000);
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
    public void onSelectedItem(long categoryId, Product mProduct, int position) {
        mPager.setCurrentItem(position);
    }

    @Override
    public void onSelectedProductImage(int position) {
        mPager.setCurrentItem(position);
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

    public void gotoMenuPage() {
        Intent i = new Intent(DM_SelectedProduct.this, DM_Menu.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void setCurrentItem(Product product) {
        mProduct = product;
    }

    public Product getCurrentItem() {
        return mProduct;
    }

    private void showHideBottomView() {
        int i = mBottomRecyclerView.getMeasuredHeight();
        if (isOpen) {
            mUpDownButton.setBackgroundResource(R.drawable.up_arrow);
            float[] arrayOfFloat2 = new float[1];
            arrayOfFloat2[0] = i;
            ObjectAnimator.ofFloat(mBottomLayout, "translationY", arrayOfFloat2).start();
            isOpen = false;
        } else {
            mUpDownButton.setBackgroundResource(R.drawable.down_arrow);
            float[] arrayOfFloat1 = new float[1];
            arrayOfFloat1[0] = 0.0F;
            ObjectAnimator.ofFloat(mBottomLayout, "translationY", arrayOfFloat1).start();
            isOpen = true;
        }
    }

    @Override
    public void onCartUpdated() {
        refreshCartNumber();
    }

    public void launchFacebookDialog(final Product productEntity)
    {
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);
        this.callbackManager = CallbackManager.Factory.create();
        this.shareDialog = new ShareDialog(this);
        ArrayList localArrayList = new ArrayList();
        LoginManager.getInstance().logInWithPublishPermissions((Activity)this.context, localArrayList);
        LoginManager.getInstance().registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                DM_SelectedProduct.this.shareDialog.registerCallback(DM_SelectedProduct.this.callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Log.d("FacebookShare", "success");
                        CharSequence localCharSequence = DM_SelectedProduct.this.getApplicationContext().getResources().getText(R.string.sharing_on_facebook);
                        Toast localToast = Toast.makeText(DM_SelectedProduct.this.getApplicationContext(), localCharSequence, Toast.LENGTH_LONG);
                        localToast.setGravity(17, 0, 0);
                        localToast.show();
                        DM_SelectedProduct.this.clearFacebookCredentials();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("FacebookShare", "cancelled");
                        DM_SelectedProduct.this.clearFacebookCredentials();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("FaebookShare", error.getLocalizedMessage());
                        DM_SelectedProduct.this.clearFacebookCredentials();
                    }
                });

                if (ShareDialog.canShow(ShareOpenGraphContent.class))
                {
                    DM_SelectedProduct.this.getIntent().getExtras();

                    Bitmap image = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.banner_logo);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();
                    SharePhotoContent photoContent = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();

                    ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                            .putString("og:type", "restaurant.menu_item")
                            .putString("og:title", productEntity.getProdName())
                            .putString("og:description", productEntity.getDescription())
                            .putString("restaurant","Chocolate Coffee Lounge")
                            .putString("restaurant:section", "ChocolateCoffee")
                            .build();

                    ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                            .setActionType("restaurant.visited")
                            .putObject("restaurant.menu_item", object)
                            .build();

                    ShareOpenGraphContent localShareOpenGraphContent = new ShareOpenGraphContent.Builder()
                            .setPreviewPropertyName("restaurant.menu_item")
                            .setAction(action)
                            .build();

                    Log.i("Image Path:", productEntity.getProdImage());

                    String filepath = productEntity.getProdImage();
                    File imagefile = new File(filepath);
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(imagefile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bm = BitmapFactory.decodeStream(fis);

                    Log.i("Image Strings:", Common.BitMapToString(bm));

                    ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                            .setContentTitle(productEntity.getProdName())
                            .setContentDescription(productEntity.getDescription())
                            .setContentUrl(Uri.parse("http://chocolatecoffee.net/"))
                            //.setImageUrl(Uri.parse(Common.BitMapToString(bm)))
                            .build();

                    shareDialog.show(shareLinkContent, ShareDialog.Mode.AUTOMATIC);
                }
            }

            @Override
            public void onCancel() {
                DM_SelectedProduct.this.clearFacebookCredentials();
            }

            @Override
            public void onError(FacebookException error) {
                DM_SelectedProduct.this.clearFacebookCredentials();
            }
        });
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
    {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        this.callbackManager.onActivityResult(paramInt1, paramInt2, paramIntent);
    }

    private void clearFacebookCredentials()
    {
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken(null);
        Profile.setCurrentProfile(null);
    }
}
