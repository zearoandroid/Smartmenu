package com.zearoconsulting.smartmenu.presentation.presenter;

import com.zearoconsulting.smartmenu.presentation.model.KOTLineItems;

/**
 * Created by saravanan on 02-11-2016.
 */

public abstract interface RemoveCartListener {

    public abstract void OnRemoveCartListener(KOTLineItems kotModel);
}
