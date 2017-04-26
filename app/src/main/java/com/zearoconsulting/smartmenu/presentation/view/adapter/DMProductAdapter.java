package com.zearoconsulting.smartmenu.presentation.view.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.ProductMultiImage;
import com.zearoconsulting.smartmenu.utils.Common;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by saravanan on 23-10-2016.
 */

public class DMProductAdapter extends PagerAdapter {

    private static List<ProductMultiImage> mProductList;
    private Context mContext;
    private ViewHolder holder;
    private String mProdName;
    private String mProdArabicName;

    public DMProductAdapter(Context paramContext, List<ProductMultiImage> paramArrayList,String prodName,String prodArabicName) {
        this.mContext = paramContext;
        this.mProductList = paramArrayList;
        this.mProdName = prodName;
        this.mProdArabicName = prodArabicName;
    }

    @Override
    public Object instantiateItem(ViewGroup paramViewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        this.holder = new ViewHolder();
        ViewGroup localView = (ViewGroup) inflater.inflate(R.layout.item_pager_content_layout, paramViewGroup, false);
        init(localView, this.holder);
        setItemInfoView(position, this.holder);
        paramViewGroup.addView(localView);
        return localView;
    }


    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
        paramViewGroup.removeView((View) paramObject);
    }

    @Override
    public int getCount() {
        return this.mProductList.size();
    }

    @Override
    public boolean isViewFromObject(View paramView, Object paramObject) {
        return paramView == paramObject;
    }

    private void init(View paramView, ViewHolder paramViewHolder) {
        paramViewHolder.itemName = ((TextView) paramView.findViewById(R.id.item_name));
        paramViewHolder.itemImage = ((ImageView) paramView.findViewById(R.id.item_image));
        paramViewHolder.itemVideo = ((VideoView) paramView.findViewById(R.id.item_video));
        paramViewHolder.itemBackgroundBlack = ((ImageView) paramView.findViewById(R.id.item_bgBlack));
        paramViewHolder.itemPlayIcon = ((ImageView) paramView.findViewById(R.id.item_playIcon));
    }

    public void setItemInfoView(int paramInt, final ViewHolder paramViewHolder) {
        final ProductMultiImage model = mProductList.get(paramInt);
        try {

            if(AndroidApplication.getLanguage().equalsIgnoreCase("en")) {
                paramViewHolder.itemName.setText(mProdName);
            }else {
                paramViewHolder.itemName.setText(mProdArabicName);
            }

            paramViewHolder.itemPlayIcon.setVisibility(View.INVISIBLE);
            paramViewHolder.itemVideo.setVisibility(View.INVISIBLE);
            paramViewHolder.itemBackgroundBlack.setVisibility(View.INVISIBLE);

            if (!model.getProdMultiPathType().equalsIgnoreCase("image")) {

                Uri localUri = Uri.parse(model.getProdMultiPath());
                paramViewHolder.itemVideo.setVideoURI(localUri);

                //paramViewHolder.itemImage.setVisibility(View.INVISIBLE);
                paramViewHolder.itemPlayIcon.setVisibility(View.VISIBLE);
                paramViewHolder.itemBackgroundBlack.setVisibility(View.INVISIBLE);
                paramViewHolder.itemVideo.setVisibility(View.INVISIBLE);
            }

            paramViewHolder.itemPlayIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    paramViewHolder.itemPlayIcon.setVisibility(View.INVISIBLE);
                    paramViewHolder.itemBackgroundBlack.setVisibility(View.VISIBLE);
                    paramViewHolder.itemVideo.setVisibility(View.VISIBLE);
                    paramViewHolder.itemVideo.start();
                }
            });

            paramViewHolder.itemVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer paramAnonymousMediaPlayer) {
                    paramViewHolder.itemImage.setVisibility(View.VISIBLE);
                    paramViewHolder.itemPlayIcon.setVisibility(View.VISIBLE);
                    paramViewHolder.itemBackgroundBlack.setVisibility(View.INVISIBLE);
                    paramViewHolder.itemVideo.setVisibility(View.INVISIBLE);
                }
            });

            Glide.with(mContext)
                    .load(model.getProdMultiPath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .override(Common.getDeviceWidth(mContext), 512)
                    .fitCenter()
                    .into(holder.itemImage);

        } catch (Exception localException1) {
            localException1.printStackTrace();
        }
    }

    public class ViewHolder {
        protected ImageView itemBackgroundBlack;
        protected ImageView itemImage;
        protected ImageView itemPlayIcon;
        protected VideoView itemVideo;
        protected TextView itemName;

        public ViewHolder() {
        }
    }

}
