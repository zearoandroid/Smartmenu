package com.zearoconsulting.smartmenu.presentation.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.Tables;
import com.zearoconsulting.smartmenu.presentation.presenter.TableSelectListener;

import java.util.List;

/**
 * Created by saravanan on 11-11-2016.
 */

public class DMTableAdapter extends RecyclerView.Adapter<DMTableAdapter.TableItemHolder> {

    List<Tables> mKOTTableList;
    TableSelectListener listener;
    List<Long> mTableIdList;

    public DMTableAdapter(List<Tables> kotTableList, List<Long> tableIdList) {
        this.mKOTTableList = kotTableList;
        this.mTableIdList = tableIdList;
    }

    @Override
    public DMTableAdapter.TableItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.table_item_row, null);
        DMTableAdapter.TableItemHolder mh = new DMTableAdapter.TableItemHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(TableItemHolder holder, int pos) {

        final Tables tables = mKOTTableList.get(pos);
        try {

            if(tables.getOrderAvailable().equalsIgnoreCase("Y")){
                holder.btnTable.setBackgroundResource(R.drawable.round_red_button);
            }else if(tables.getOrderAvailable().equalsIgnoreCase("N")){
                holder.btnTable.setBackgroundResource(R.drawable.round_grey_button);
            }
            holder.btnTable.setText(tables.getTableName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != mKOTTableList ? mKOTTableList.size() : 0);
    }

    public void selectedItem(int pos) {
        try {
            Tables tables = mKOTTableList.get(pos);
            listener.OnTableSelectedListener(tables);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnTableSelectListener(TableSelectListener paramTableSelectListener) {
        this.listener = paramTableSelectListener;
    }

    public class TableItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        protected Button btnTable;

        public TableItemHolder(View view) {
            super(view);
            mView = view;
            this.btnTable = (Button) view.findViewById(R.id.btnTable);
            this.btnTable.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectedItem(getAdapterPosition());
        }
    }

    public void updateTables(List<Tables> kotTableList){

        mKOTTableList = kotTableList;

        for(int i=0; i<mKOTTableList.size(); i++){
            notifyItemChanged(i);
        }
    }
}
