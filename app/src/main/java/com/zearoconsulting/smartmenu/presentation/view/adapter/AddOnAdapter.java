package com.zearoconsulting.smartmenu.presentation.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.presenter.AddCartListener;
import com.zearoconsulting.smartmenu.utils.Common;

import java.util.List;

/**
 * Created by saravanan on 24-12-2016.
 */

public class AddOnAdapter extends
        RecyclerView.Adapter<AddOnAdapter.ViewHolder> {

    List<Product> mRelatedProductList;
    AddCartListener listener;

    public AddOnAdapter(List<Product> relatedProductList) {
        this.mRelatedProductList = relatedProductList;
    }

    // Create new views
    @Override
    public AddOnAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.addon_row_item, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddOnAdapter.ViewHolder viewHolder, int position) {

        final int pos = position;

        viewHolder.tvProductName.setText(mRelatedProductList.get(pos).getProdName()+" ("+ Common.valueFormatter(mRelatedProductList.get(pos).getSalePrice())+")");

        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if(cb.isChecked() == true){
                    mRelatedProductList.get(pos).setSelected("Y");
                    listener.OnAddCartListener(mRelatedProductList.get(pos),true);
                }else{
                    mRelatedProductList.get(pos).setSelected("N");
                    listener.OnAddCartListener(mRelatedProductList.get(pos),false);
                }
            }
        });

    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return mRelatedProductList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvProductName;
        public CheckBox chkSelected;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvProductName = (TextView) itemLayoutView.findViewById(R.id.tvProduct);
            chkSelected = (CheckBox) itemLayoutView
                    .findViewById(R.id.chkSelected);

        }
    }

    public List<Product> getSelectedProductList(){
        return mRelatedProductList;
    }

    public void setOnAddCartListener(AddCartListener paramAddCartListener)
    {
        this.listener = paramAddCartListener;
    }
}

