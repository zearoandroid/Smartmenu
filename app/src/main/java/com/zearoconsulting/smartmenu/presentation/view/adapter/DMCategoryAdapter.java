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
import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.presenter.IDMListeners;

import java.util.List;

/**
 * Created by saravanan on 23-10-2016.
 */

public class DMCategoryAdapter extends RecyclerView.Adapter<DMCategoryAdapter.CategoryHolder> {

    private static List<Category> mCategoryList;
    private Context mContext;
    private IDMListeners mListener;

    public static class CategoryHolder extends RecyclerView.ViewHolder{
        protected TextView name;
        protected ImageView imageView;
        public final View mView;

        public CategoryHolder(View view) {
            super(view);
            mView = view;
            this.name = (TextView) view.findViewById(R.id.title);
            this.imageView = (ImageView) view.findViewById(R.id.category_image);
        }
    }

    public DMCategoryAdapter(Context context, List<Category> categoryList, IDMListeners listener) {
        this.mCategoryList = categoryList;
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public DMCategoryAdapter.CategoryHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_item, null);
        DMCategoryAdapter.CategoryHolder mh = new DMCategoryAdapter.CategoryHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(DMCategoryAdapter.CategoryHolder holder, int pos) {

        if(mCategoryList!=null) {
            final Category model = mCategoryList.get(pos);


            if(AndroidApplication.getLanguage().equalsIgnoreCase("en"))
                holder.name.setText(model.getCategoryName());
            else
                holder.name.setText(model.getCategoryArabicName());

            holder.name.getBackground().setAlpha(180);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.onSelectedCategory(model);
                }
            });

            //String path =  mDBHelper.getCategoryImage(model.getCategoryId());
            String path = model.getCategoryImage();

            Glide.with(mContext)
                    .load(path)
                    .into( holder.imageView);


        }
    }

    @Override
    public int getItemCount() {
        return (null != mCategoryList ? mCategoryList.size() : 0);
    }
}

