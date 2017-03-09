package com.zearoconsulting.smartmenu.presentation.presenter;

import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.model.Product;

/**
 * Created by saravanan on 23-10-2016.
 */

public interface IDMListeners {
    void onSelectedCategory(Category category);
    void onSelectedItem(long categoryId,Product mProduct,int position);
    void onSelectedProductImage(int position);
}
