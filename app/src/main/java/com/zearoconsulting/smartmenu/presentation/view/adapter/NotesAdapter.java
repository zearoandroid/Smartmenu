package com.zearoconsulting.smartmenu.presentation.view.adapter;

import android.content.Context;

import com.zearoconsulting.smartmenu.presentation.model.Notes;

import java.util.List;

/**
 * Created by saravanan on 24-12-2016.
 */

public class NotesAdapter extends RadioAdapter<Notes> {
    public NotesAdapter(Context context, List<Notes> items) {
        super(context, items);
    }

    @Override
    public void onBindViewHolder(RadioAdapter.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        viewHolder.mText.setText(mItems.get(i).getNotesName());
    }

    public String getSelectedNotes(){
        return  mItems.get(getSelectedItem()).getNotesName();
    }
}
