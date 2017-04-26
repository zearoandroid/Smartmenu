package com.zearoconsulting.smartmenu.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.model.ProductMultiImage;
import com.zearoconsulting.smartmenu.presentation.presenter.IDMListeners;
import java.util.List;

/**
 * Created by saravanan on 23-10-2016.
 */

public class DMBottomProductAdapter extends RecyclerView.Adapter<DMBottomProductAdapter.BottomItemHolder>  {

    private static List<ProductMultiImage> mProductList;
    private Context mContext;
    private IDMListeners mListener;
    private int selectedPos = 0;

    public DMBottomProductAdapter(Context context, List<ProductMultiImage> productList, IDMListeners listener) {
        this.mProductList = productList;
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public DMBottomProductAdapter.BottomItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_indicator, null);
        DMBottomProductAdapter.BottomItemHolder mh = new DMBottomProductAdapter.BottomItemHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(final DMBottomProductAdapter.BottomItemHolder holder, final int pos) {
        final ProductMultiImage model = mProductList.get(pos);

        try{

            holder.imageMask.setAlpha(0.4f);
            holder.imageMask.setBackgroundResource(R.color.black);

            if(pos == selectedPos){
                // This is the first item, you need to select this
                holder.mView.setSelected(true);
                holder.imageMask.setAlpha(0.0f);
                holder.imageMask.setBackgroundResource(R.color.transparent);
            }

            if (!model.getProdMultiPathType().equalsIgnoreCase("image")) {
                Glide.with(mContext)
                        .load(R.drawable.play_icon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into( holder.imageView);
            }else{
                Glide.with(mContext)
                        .load(mProductList.get(pos).getProdMultiPath())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into( holder.imageView);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener)
                {
                    holder.imageMask.setAlpha(0f);
                    holder.imageMask.setBackgroundResource(R.color.transparent);
                    selectedPos = pos;
                    // Notify the active callbacks interface that an item has been selected.
                    mListener.onSelectedProductImage(pos);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != mProductList ? mProductList.size() : 0);
    }

    public static class BottomItemHolder extends RecyclerView.ViewHolder{
        protected TextView name;
        protected ImageView imageView;
        protected ImageView imageMask;
        public final View mView;

        public BottomItemHolder(View view) {
            super(view);
            mView = view;
            this.name = (TextView) view.findViewById(R.id.item_name);
            this.imageView = (ImageView) view.findViewById(R.id.item_image);
            this.imageMask = (ImageView) view.findViewById(R.id.image_mask);
        }
    }

    public void highLightItem(int position){
        selectedPos = position;
        notifyDataSetChanged();
        notifyItemChanged(selectedPos);
    }

}
