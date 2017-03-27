package com.zearoconsulting.smartmenu.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.presenter.IDMListeners;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saravanan on 23-10-2016.
 */
public class DMAllProductAdapter extends RecyclerView.Adapter<DMAllProductAdapter.CategoryItemHolder>  {

    private static List<Product> mProductList;
    private Context mContext;
    private IDMListeners mListener;
    long mCategoryId;

    public DMAllProductAdapter(Context context, List<Product> productList, long categoryId, IDMListeners listener) {
        this.mProductList = productList;
        this.mContext = context;
        this.mCategoryId = categoryId;
        this.mListener = listener;
    }

    @Override
    public DMAllProductAdapter.CategoryItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_single_column, null);
        DMAllProductAdapter.CategoryItemHolder mh = new DMAllProductAdapter.CategoryItemHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(DMAllProductAdapter.CategoryItemHolder holder, final int pos) {
        final Product model = mProductList.get(pos);

        if(AndroidApplication.getLanguage().equalsIgnoreCase("en")) {
            holder.name.setText(model.getProdName());
            holder.desc.setText(model.getDescription());
        }else {
            holder.name.setText(model.getProdArabicName());
            holder.desc.setText(model.getProdArabicDescription());
        }
        //holder.name.getBackground().setAlpha(180);
        holder.price.setText("+ "+AppConstants.currencyCode+" "+ Common.valueFormatter(model.getSalePrice()));

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
        protected TextView desc;
        protected Button price;
        protected ImageView imageView;
        public final View mView;

        public CategoryItemHolder(View view) {
            super(view);
            mView = view;
            this.name = (TextView) view.findViewById(R.id.item_title);
            this.desc = (TextView) view.findViewById(R.id.item_description);
            this.price = (Button) view.findViewById(R.id.btn_prod_price);
            this.imageView = (ImageView) view.findViewById(R.id.list_image);
            this.price.setOnClickListener(this);
            this.mView.setOnClickListener(this);
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
                mListener.onSelectedItem(0,model,pos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFilter(List<Product> productModels) {
        mProductList = new ArrayList<>();
        mProductList.addAll(productModels);
        notifyDataSetChanged();
    }
}
