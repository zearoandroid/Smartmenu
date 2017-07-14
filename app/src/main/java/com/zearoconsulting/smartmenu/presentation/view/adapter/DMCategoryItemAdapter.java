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
import com.zearoconsulting.smartmenu.data.DBHelper;
import com.zearoconsulting.smartmenu.presentation.model.POSLineItem;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.presenter.IDMListeners;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.Common;

import java.util.List;

/**
 * Created by saravanan on 23-10-2016.
 */
public class DMCategoryItemAdapter extends RecyclerView.Adapter<DMCategoryItemAdapter.CategoryItemHolder>  {

    private static List<Product> mProductList;
    private Context mContext;
    private IDMListeners mListener;
    long mCategoryId;

    public DMCategoryItemAdapter(Context context, List<Product> productList, long categoryId, IDMListeners listener) {
        this.mProductList = productList;
        this.mContext = context;
        this.mCategoryId = categoryId;
        this.mListener = listener;
    }

    @Override
    public DMCategoryItemAdapter.CategoryItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_item, null);
        DMCategoryItemAdapter.CategoryItemHolder mh = new DMCategoryItemAdapter.CategoryItemHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(DMCategoryItemAdapter.CategoryItemHolder holder,final int pos) {
        final Product model = mProductList.get(pos);

        if(AndroidApplication.getLanguage().equalsIgnoreCase("en"))
            holder.name.setText(model.getProdName());
        else
            holder.name.setText(model.getProdArabicName());

        holder.name.getBackground().setAlpha(180);
        holder.price.setText(AppConstants.currencyCode+" "+ Common.valueFormatter(model.getSalePrice()));

        Glide.with(mContext)
                .load(mProductList.get(pos).getProdImage())
                .into( holder.imageView);

    }

    @Override
    public int getItemCount() {
        return (null != mProductList ? mProductList.size() : 0);
    }

    public class CategoryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView name;
        protected TextView price;
        protected ImageView imageView;
        public final View mView;

        public CategoryItemHolder(View view) {
            super(view);
            mView = view;
            this.name = (TextView) view.findViewById(R.id.title);
            this.price = (TextView) view.findViewById(R.id.price);
            this.imageView = (ImageView) view.findViewById(R.id.category_image);
            this.imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectedItem(getAdapterPosition());
        }
    }

    public void selectedItem(int pos) {
        try {
            Product model = mProductList.get(pos);
            if (null != mListener)
            {
                // Notify the active callbacks interface
                mListener.onSelectedItem(model.getCategoryId(),model,pos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
