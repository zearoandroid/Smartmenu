package com.zearoconsulting.smartmenu.presentation.view.adapter;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.data.DBHelper;
import com.zearoconsulting.smartmenu.presentation.model.Tables;
import com.zearoconsulting.smartmenu.presentation.presenter.CoverSelectListener;

import java.util.List;

/**
 * Created by saravanan on 11-11-2016.
 */

public class DMCoverAdapter extends RecyclerView.Adapter<DMCoverAdapter.CoverItemHolder> {

    private List<Tables> mKOTCoverList;
    List<Long> mKOTCoverIds;
    private CoverSelectListener listener;
    private boolean selectAllChecked = false;
    private int colorPos;
    private int redColor = Color.rgb(255, 0, 0);
    private int grayColor = Color.rgb(160, 160, 160);
    private int blueColor = Color.rgb(20, 60, 86);
    private int[] colors = {
            Color.rgb(152, 0, 142),
            Color.rgb(152, 0, 0),
            Color.rgb(61, 242, 152),
            Color.rgb(15, 242, 152),
            Color.rgb(0, 86, 152),
            Color.rgb(0, 152, 112),
            Color.rgb(0, 152, 0),
            Color.rgb(152, 127, 0),
            Color.rgb(152, 41, 0),
            Color.rgb(215, 116, 116),
            Color.rgb(215, 116, 202),
            Color.rgb(116, 119, 215),
            Color.rgb(116, 215, 211)
    };
    private Drawable[] drawables;

    public DMCoverAdapter(List<Tables> kotTableList, List<Long> kotCoverIDs) {
        this.mKOTCoverList = kotTableList;
        this.mKOTCoverIds = kotCoverIDs;
        drawables = new Drawable[14];
        for (int i = 0; i < 13; i++) {
            Drawable drawable = AndroidApplication.getInstance().getResources().getDrawable(R.drawable.round_grey_button);
            drawable.setColorFilter(colors[i], PorterDuff.Mode.OVERLAY);
            this.drawables[i] = drawable;
        }
    }

    @Override
    public CoverItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.table_item_row, null);
        return new CoverItemHolder(v);
    }

    @Override
    public void onBindViewHolder(CoverItemHolder holder, int pos) {
        final Tables covers = mKOTCoverList.get(pos);
        try {
            if (covers.getOrderAvailable().equalsIgnoreCase("Y")) {
                holder.btnTable.setBackgroundResource(R.drawable.round_red_button);
                holder.btnTable.setTag("Y");
                holder.buttonColor = redColor;
            } else if (covers.getOrderAvailable().equalsIgnoreCase("N") && !selectAllChecked) {
                holder.btnTable.setBackgroundResource(R.drawable.round_grey_button);
                holder.btnTable.setTag("N");
                holder.buttonColor = grayColor;
            }

            if (selectAllChecked) {
                holder.btnTable.setBackgroundResource(R.drawable.round_blue_button);
                holder.buttonColor = R.drawable.round_blue_button;
            }

            if (covers.getOrderGroup() == 1) {
                colorChange(holder.btnTable, drawables[0]);
            } else if (covers.getOrderGroup() == 2) {
                colorChange(holder.btnTable, drawables[1]);
            } else if (covers.getOrderGroup() == 3) {
                colorChange(holder.btnTable, drawables[2]);
            } else if (covers.getOrderGroup() == 4) {
                colorChange(holder.btnTable, drawables[3]);
            } else if (covers.getOrderGroup() == 5) {
                colorChange(holder.btnTable, drawables[4]);
            } else if (covers.getOrderGroup() == 6) {
                colorChange(holder.btnTable, drawables[5]);
            } else if (covers.getOrderGroup() == 7) {
                colorChange(holder.btnTable, drawables[6]);
            } else if (covers.getOrderGroup() == 8) {
                colorChange(holder.btnTable, drawables[7]);
            } else if (covers.getOrderGroup() == 9) {
                colorChange(holder.btnTable, drawables[8]);
            } else if (covers.getOrderGroup() == 10) {
                colorChange(holder.btnTable, drawables[9]);
            }

            holder.btnTable.setText(covers.getTableName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void colorChange(View v, Drawable d) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(d);
        } else {
            v.setBackgroundDrawable(d);
        }
    }

    @Override
    public int getItemCount() {
        return (null != mKOTCoverList ? mKOTCoverList.size() : 0);
    }

    private void selectedItem(int pos) {
        try {
            Tables covers = mKOTCoverList.get(pos);
            listener.OnSelectedCover(covers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnCoverSelectListener(CoverSelectListener paramCoverSelectListener) {
        this.listener = paramCoverSelectListener;
    }

    public void selectAllCovers(boolean selectAll) {
        selectAllChecked = selectAll;
        notifyDataSetChanged();
    }

    class CoverItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View mView;
        Button btnTable;
        private int buttonColor = 0;

        CoverItemHolder(View view) {
            super(view);
            mView = view;
            this.btnTable = (Button) view.findViewById(R.id.btnTable);
            //this.btnTable.setForeground(AndroidApplication.getAppContext().getResources().getDrawable(R.drawable.round_red_button));
            this.btnTable.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (btnTable.getTag().toString().equals("N")) {
                if (buttonColor == grayColor) {
                    //btnTable.setBackgroundColor(blueColor);
                    btnTable.setBackgroundResource(R.drawable.round_blue_button);
                    buttonColor = blueColor;
                } else if (buttonColor == blueColor) {
                    btnTable.setBackgroundColor(grayColor);
                    btnTable.setBackgroundResource(R.drawable.round_grey_button);
                    buttonColor = grayColor;
                }
                selectedItem(getAdapterPosition());
            } else if (btnTable.getTag().toString().equals("Y")) {
                //Clear Previous List
                AndroidApplication.getInstance().clearCoverList();
                //Add Selected Covers
                AndroidApplication.getInstance().setAllCoverList(
                        AndroidApplication.getInstance()
                                .getSMDataSource()
                                .getExistingCoverIDsFromHeader(mKOTCoverList.get(getAdapterPosition()).getTableId()));
                listener.CoverSelected();
            }
        }
    }

    public void updateCovers() {
        for (String l : AndroidApplication.getInstance().getSMDataSource().getAllCoverHeaders()) {
            colorPos++;
            for (String s : l.split(",")) {
                for (Tables t : mKOTCoverList) {
                    if (t.getTableId() == Long.parseLong(s)) {
                        t.setOrderGroup(colorPos);
                        int curPos = mKOTCoverList.indexOf(t);
                        mKOTCoverList.set(curPos, t);
                        break;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
