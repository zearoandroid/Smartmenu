package com.zearoconsulting.smartmenu.presentation.view.adapter;

import android.graphics.Color;
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

    public DMCoverAdapter(List<Tables> kotTableList, List<Long> kotCoverIDs) {
        this.mKOTCoverList = kotTableList;
        this.mKOTCoverIds = kotCoverIDs;
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
                holder.btnTable.setBackgroundColor(redColor);
                holder.btnTable.setTag("Y");
                holder.buttonColor = redColor;
            } else if (covers.getOrderAvailable().equalsIgnoreCase("N") && !selectAllChecked) {
                holder.btnTable.setBackgroundColor(grayColor);
                holder.btnTable.setTag("N");
                holder.buttonColor = grayColor;
            }

            if (selectAllChecked) {
                holder.btnTable.setBackgroundResource(R.drawable.round_blue_button);
                holder.buttonColor = R.drawable.round_blue_button;
            }/* else if (!selectAllChecked) {
                holder.btnTable.setBackgroundResource(R.drawable.round_grey_button);
                holder.buttonColor = R.drawable.round_grey_button;
            }*/

            if (covers.getOrderGroup() == 1) {
                holder.btnTable.setBackgroundColor(colors[0]);
            } else if (covers.getOrderGroup() == 2) {
                holder.btnTable.setBackgroundColor(colors[1]);
            } else if (covers.getOrderGroup() == 3) {
                holder.btnTable.setBackgroundColor(colors[2]);
            } else if (covers.getOrderGroup() == 4) {
                holder.btnTable.setBackgroundColor(colors[3]);
            } else if (covers.getOrderGroup() == 5) {
                holder.btnTable.setBackgroundColor(colors[4]);
            } else if (covers.getOrderGroup() == 6) {
                holder.btnTable.setBackgroundColor(colors[5]);
            } else if (covers.getOrderGroup() == 7) {
                holder.btnTable.setBackgroundColor(colors[6]);
            } else if (covers.getOrderGroup() == 8) {
                holder.btnTable.setBackgroundColor(colors[7]);
            } else if (covers.getOrderGroup() == 9) {
                holder.btnTable.setBackgroundColor(colors[8]);
            } else if (covers.getOrderGroup() == 10) {
                holder.btnTable.setBackgroundColor(colors[9]);
            }/* else if (covers.getOrderGroup() == 11) {
                holder.btnTable.setBackgroundColor(colors[10]);
            } else if (covers.getOrderGroup() == 12) {
                holder.btnTable.setBackgroundColor(colors[11]);
            } else if (covers.getOrderGroup() == 13) {
                holder.btnTable.setBackgroundColor(colors[12]);
            }*/

            holder.btnTable.setText(covers.getTableName());
        } catch (Exception e) {
            e.printStackTrace();
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
            this.btnTable.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (btnTable.getTag().toString().equals("N")) {
                if (buttonColor == grayColor) {
                    btnTable.setBackgroundColor(blueColor);
                    buttonColor = blueColor;
                } else if (buttonColor == blueColor) {
                    btnTable.setBackgroundColor(grayColor);
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
