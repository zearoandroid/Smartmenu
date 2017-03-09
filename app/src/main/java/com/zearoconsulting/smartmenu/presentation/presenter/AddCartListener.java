package com.zearoconsulting.smartmenu.presentation.presenter;

import com.zearoconsulting.smartmenu.presentation.model.Product;

/**
 * Created by saravanan on 01-11-2016.
 */

public abstract interface AddCartListener {

    public abstract void OnAddCartListener(Product productEntity,boolean isChecked);
}
