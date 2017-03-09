package com.zearoconsulting.smartmenu.presentation.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.KOTLineItems;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.presenter.RemoveCartListener;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.Common;
import java.util.List;

/**
 * Created by saravanan on 01-11-2016.
 */

public class DMCartAdapter extends RecyclerView.Adapter<DMCartAdapter.CartItemHolder> {

    List<KOTLineItems> mKOTLineItemList;
    RemoveCartListener listener;

    private String mShowPrice;

    public DMCartAdapter(List<KOTLineItems> kotLineItemList, String showPrice) {
        this.mKOTLineItemList = kotLineItemList;
        this.mShowPrice = showPrice;
    }

    @Override
    public DMCartAdapter.CartItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_row, null);
        DMCartAdapter.CartItemHolder mh = new DMCartAdapter.CartItemHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(CartItemHolder holder, int pos) {

        final KOTLineItems kotModel = mKOTLineItemList.get(pos);
        try {
            final Product product = kotModel.getProduct();
            holder.name.setText(product.getProdName());
            holder.note.setText(kotModel.getNotes());
            holder.totalQty.setText("x"+String.valueOf(kotModel.getQty()));

            if(mShowPrice.equalsIgnoreCase("Y")){
                holder.price.setText(AppConstants.currencyCode + " " + Common.valueFormatter(kotModel.getTotalPrice()));
            }

            holder.btnRemove.setVisibility(View.VISIBLE);
            if(kotModel.getPrinted().equalsIgnoreCase("Y")){
                holder.btnRemove.setVisibility(View.INVISIBLE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != mKOTLineItemList ? mKOTLineItemList.size() : 0);
    }

    public class CartItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView name;
        protected TextView note;
        protected TextView totalQty;
        protected TextView price;
        protected ImageView btnRemove;
        public final View mView;

        public CartItemHolder(View view) {
            super(view);
            mView = view;
            this.name = (TextView) view.findViewById(R.id.item_name);
            this.note = (TextView) view.findViewById(R.id.item_note);
            this.totalQty = (TextView) view.findViewById(R.id.totalQty);
            this.price = (TextView) view.findViewById(R.id.item_price);
            this.btnRemove = (ImageView) view.findViewById(R.id.btn_remove);
            this.btnRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            deleteItem(getAdapterPosition());
        }
    }

    public void deleteItem(int pos){
        try {
            KOTLineItems kotModel = mKOTLineItemList.get(pos);
            Product product = kotModel.getProduct();
            if(kotModel.getPrinted().equalsIgnoreCase("N")){
                listener.OnRemoveCartListener(kotModel);
                mKOTLineItemList.remove(pos);
                notifyItemRemoved(pos);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOnRemoveCartListener(RemoveCartListener paramAddCartListener)
    {
        this.listener = paramAddCartListener;
    }
}
