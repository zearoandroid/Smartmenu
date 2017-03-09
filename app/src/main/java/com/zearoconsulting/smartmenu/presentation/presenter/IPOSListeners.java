package com.zearoconsulting.smartmenu.presentation.presenter;

import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.model.POSLineItem;
import com.zearoconsulting.smartmenu.presentation.model.POSOrders;
import com.zearoconsulting.smartmenu.presentation.model.Product;

/**
 * Created by saravanan on 27-05-2016.
 */
public interface IPOSListeners {

    void onTabSelected(int position);
    void onUpdateProductToCart(long categoryId,Product mProduct, boolean addOrRemove);
    void addOrRemoveItemFromCart(long categoryId, POSLineItem mProduct, boolean addOrRemove);
    void onSelectedOrder(POSOrders orders);
    void onSelectedCategory(Category category);
}
