package com.zearoconsulting.smartmenu.presentation.presenter;

import com.zearoconsulting.smartmenu.presentation.model.Tables;

/**
 * Created by saravanan on 11-11-2016.
 */

public abstract interface CoverSelectListener {

    public abstract void OnSelectedCover(Tables tableEntity);

    public abstract void CoverSelected();
}
