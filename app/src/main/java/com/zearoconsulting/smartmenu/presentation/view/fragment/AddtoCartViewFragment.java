package com.zearoconsulting.smartmenu.presentation.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.presentation.model.Notes;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.presenter.AddCartListener;
import com.zearoconsulting.smartmenu.presentation.view.activity.DM_CategoryItems;
import com.zearoconsulting.smartmenu.presentation.view.activity.DM_SelectedProduct;
import com.zearoconsulting.smartmenu.presentation.view.activity.Dm_Products;
import com.zearoconsulting.smartmenu.presentation.view.adapter.AddOnAdapter;
import com.zearoconsulting.smartmenu.presentation.view.adapter.NotesAdapter;
import com.zearoconsulting.smartmenu.presentation.view.component.GridSpacingItemDecoration;
import com.zearoconsulting.smartmenu.presentation.view.component.ReboundListener;
import com.zearoconsulting.smartmenu.utils.AppConstants;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by saravanan on 01-11-2016.
 */

public class AddtoCartViewFragment extends AbstractDialogFragment implements AdapterView.OnItemSelectedListener {

    private static int MAX_VALUE = 100;
    private static int MIN_VALUE = 1;

    TextView foodDesc;
    TextView foodName;
    TextView foodPrice;
    TextView numOfOrder;
    TextView addNote;
    EditText note;
    Spinner mSpnNotes;
    FancyButton buttonAdd;
    FancyButton buttonCancel;
    FancyButton buttonDown;
    FancyButton buttonUp;
    ImageView imageOfFood;
    RelativeLayout relativeLayout;

    LinearLayout mAddOnLayout;
    LinearLayout mChoiceOfLayout;
    List<Product> mRelatedProductList;
    List<Notes> notesList;
    List<String> mNotesList;
    NotesAdapter mNotesAdapter;
    AddOnAdapter mAddOnAdapter;
    private RecyclerView mRelatedProductView;
    private RecyclerView mNotesSelectionView;
    private Product product;
    private String noteToCart="";
    private int quantity;
    private double totalPrice;
    private String selectedNotes="";

    private String page = "ProductDetail";

    AddCartListener addCartListener = new AddCartListener() {
        @Override
        public void OnAddCartListener(Product productEntity, boolean isChecked) {
            updateTotal(productEntity, isChecked);
        }
    };
    private boolean isAddTabed = false;

    public AddtoCartViewFragment() {
        // Required empty public constructor
    }

    public Dialog onCreateDialog(Bundle paramBundle) {
        Dialog localDialog = super.onCreateDialog(paramBundle);
        localDialog.getWindow().requestFeature(1);
        return localDialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.addtocartviewcontroller_layout, container, false);
        mReboundListener = new ReboundListener();

        Bundle bundle = getArguments();
        if(bundle!=null){
            page = bundle.getString("Page");
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Add a listener to the spring
        mSpring.addListener(mReboundListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        //Remove a listener to the spring
        mSpring.removeListener(mReboundListener);
    }

    @Override
    public void onViewCreated(View paramView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(paramView, savedInstanceState);

        getDialog().getWindow().setSoftInputMode(3);
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCanceledOnTouchOutside(true);


        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode, android.view.KeyEvent event) {

                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    //Stop back event here!!!
                    return true;
                } else
                    return false;
            }
        });

        this.foodName = ((TextView) paramView.findViewById(R.id.foodName));
        this.foodDesc = ((TextView) paramView.findViewById(R.id.foodDesc));
        this.foodPrice = ((TextView) paramView.findViewById(R.id.foodPrice));
        this.numOfOrder = ((TextView) paramView.findViewById(R.id.numberOfOrder));
        this.addNote = ((TextView) paramView.findViewById(R.id.addNote));
        this.note = ((EditText) paramView.findViewById(R.id.textNote));
        this.buttonAdd = ((FancyButton) paramView.findViewById(R.id.buttonAdd));
        this.buttonCancel = ((FancyButton) paramView.findViewById(R.id.buttonCancel));
        this.buttonDown = ((FancyButton) paramView.findViewById(R.id.spinnerMinus));
        this.buttonUp = ((FancyButton) paramView.findViewById(R.id.spinnerPlus));
        this.imageOfFood = ((ImageView) paramView.findViewById(R.id.foodImage));
        this.relativeLayout = ((RelativeLayout) paramView.findViewById(R.id.RelativeLayout1));
        mSpnNotes = ((Spinner) paramView.findViewById(R.id.spnNotes));

        mRelatedProductView = ((RecyclerView) paramView.findViewById(R.id.addOnSelectionView));
        mNotesSelectionView = ((RecyclerView) paramView.findViewById(R.id.notesSelectionView));
        mAddOnLayout = ((LinearLayout) paramView.findViewById(R.id.addOnLayout));
        mChoiceOfLayout = ((LinearLayout) paramView.findViewById(R.id.choiceOfLayout));

        mAddOnLayout.setVisibility(View.GONE);
        mChoiceOfLayout.setVisibility(View.GONE);

        if(page.equalsIgnoreCase("ProductDetail")){
            product = ((DM_SelectedProduct) getActivity()).getCurrentItem();
        }else{
            product = ((Dm_Products) getActivity()).getCurrentItem();
        }

        mRelatedProductList = mDBHelper.getRelatedProduct(mAppManager.getClientID(), mAppManager.getOrgID(), product.getProdId());
        notesList = mDBHelper.getNotes(mAppManager.getClientID(), mAppManager.getOrgID(), product.getProdId());

        Glide.with(this)
                .load(product.getProdImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this.imageOfFood);

        this.foodName.setText(this.product.getProdName());
        this.foodDesc.setText(this.product.getDescription());
        this.foodPrice.setText(AppConstants.currencyCode+" "+String.valueOf(this.product.getSalePrice()));

        this.totalPrice = this.product.getSalePrice();

        this.quantity = MIN_VALUE;

        this.buttonDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (AddtoCartViewFragment.this.quantity > AddtoCartViewFragment.MIN_VALUE) {
                    AddtoCartViewFragment.this.quantity = AddtoCartViewFragment.this.quantity - 1;
                    AddtoCartViewFragment.this.numOfOrder.setText(String.valueOf(AddtoCartViewFragment.this.quantity));
                }
            }
        });

        this.buttonUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (AddtoCartViewFragment.this.quantity < AddtoCartViewFragment.MAX_VALUE) {
                    AddtoCartViewFragment.this.quantity = AddtoCartViewFragment.this.quantity + 1;
                    AddtoCartViewFragment.this.numOfOrder.setText(String.valueOf(AddtoCartViewFragment.this.quantity));
                }
            }
        });

        if (mRelatedProductList.size() != 0) {
            mAddOnLayout.setVisibility(View.VISIBLE);
            mRelatedProductView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            int spanCount = 3;
            int spacing = 10;
            boolean includeEdge = true;
            mRelatedProductView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

            mAddOnAdapter = new AddOnAdapter(mRelatedProductList);
            mRelatedProductView.setAdapter(mAddOnAdapter);
            mAddOnAdapter.notifyDataSetChanged();

            mAddOnAdapter.setOnAddCartListener(addCartListener);
        }

        if (notesList.size() != 0) {
            mChoiceOfLayout.setVisibility(View.VISIBLE);
            mNotesSelectionView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            int spanCount = 3;
            int spacing = 10;
            boolean includeEdge = true;
            mNotesSelectionView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

            mNotesAdapter = new NotesAdapter(getActivity(), notesList);
            mNotesSelectionView.setAdapter(mNotesAdapter);
            mNotesAdapter.notifyDataSetChanged();

            // Spinner click listener
            mSpnNotes.setOnItemSelectedListener(this);

            mNotesList = mDBHelper.getNotesList(mAppManager.getClientID(), mAppManager.getOrgID(), product.getProdId());

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mNotesList);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            mSpnNotes.setAdapter(dataAdapter);
        }



        // Add an OnTouchListener to the root view.
        this.buttonCancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(buttonCancel);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // When pressed start solving the spring to 1.
                        mSpring.setEndValue(1);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // When released start solving the spring to 0.
                        mSpring.setEndValue(0);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AddtoCartViewFragment.this.dismiss();
                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });

        // Add an OnTouchListener to the root view.
        this.buttonAdd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReboundListener.animateView(buttonAdd);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // When pressed start solving the spring to 1.
                        mSpring.setEndValue(1);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // When released start solving the spring to 0.
                        mSpring.setEndValue(0);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (AppConstants.tableID != 0 && !isAddTabed) {

                                        StringBuilder sbExtras=new StringBuilder();

                                        if (mAddOnAdapter != null && mRelatedProductList.size() != 0) {
                                            List<Product> relatedProductList = mAddOnAdapter.getSelectedProductList();

                                            for (int i = 0; i < relatedProductList.size(); i++) {
                                                if (relatedProductList.get(i).getSelected().equalsIgnoreCase("Y")) {
                                                    sbExtras.append(relatedProductList.get(i).getProdName()+", ");
                                                }
                                            }
                                        }

                                        if(sbExtras.toString().length()!=0)
                                        noteToCart = sbExtras.toString();

                                        if(selectedNotes.equalsIgnoreCase("Choose one"))
                                            selectedNotes = "";

                                        if (mNotesAdapter != null && notesList.size() != 0) {
                                            if (AddtoCartViewFragment.this.note.getText().toString().trim().length() != 0 && selectedNotes.length() != 0)
                                                noteToCart = noteToCart + selectedNotes + "," + AddtoCartViewFragment.this.note.getText().toString();
                                            else if (AddtoCartViewFragment.this.note.getText().toString().trim().length() == 0 && selectedNotes.length() != 0)
                                                noteToCart = noteToCart + selectedNotes;
                                            else if (AddtoCartViewFragment.this.note.getText().toString().trim().length() != 0 && selectedNotes.length() == 0)
                                                noteToCart = noteToCart + AddtoCartViewFragment.this.note.getText().toString();
                                        }else {
                                            noteToCart = noteToCart + AddtoCartViewFragment.this.note.getText().toString();
                                        }

                                        if(noteToCart.length()!=0)
                                            if (noteToCart.endsWith(","))
                                                noteToCart = noteToCart.substring(0, noteToCart.length() - 1);

                                        long kotLineId = mDBHelper.getLastRowId();

                                        mDBHelper.addKOTLineItems(kotLineId, AppConstants.tableID, 0, product, AddtoCartViewFragment.this.quantity, noteToCart, "N", 0 , "N");

                                        if (mAddOnAdapter != null && mRelatedProductList.size() != 0) {
                                            List<Product> relatedProductList = mAddOnAdapter.getSelectedProductList();

                                            for (int i = 0; i < relatedProductList.size(); i++) {
                                                if (relatedProductList.get(i).getSelected().equalsIgnoreCase("Y")) {
                                                    relatedProductList.get(i).setTerminalId(product.getTerminalId());
                                                    mDBHelper.addKOTLineItems(kotLineId, AppConstants.tableID, 0, relatedProductList.get(i), AddtoCartViewFragment.this.quantity, "", "N", kotLineId, "Y");
                                                }
                                            }
                                        }

                                        isAddTabed = true;
                                        AddtoCartViewFragment.this.dismiss();
                                        if(page.equalsIgnoreCase("ProductDetail")){
                                            ((DM_SelectedProduct) AddtoCartViewFragment.this.getActivity()).refreshCartNumber();
                                        }else{
                                            ((Dm_Products) AddtoCartViewFragment.this.getActivity()).refreshCartNumber();
                                        }
                                    } else {
                                        AddtoCartViewFragment.this.dismiss();
                                        if(page.equalsIgnoreCase("ProductDetail")){
                                            ((DM_SelectedProduct) AddtoCartViewFragment.this.getActivity()).gotoMenuPage();
                                        }else{
                                            ((Dm_Products) AddtoCartViewFragment.this.getActivity()).gotoMenuPage();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, 200);
                        break;
                }
                return true;
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {
        selectedNotes = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private void updateTotal(Product productEntity, boolean isChecked) {
        if (isChecked) {
            this.totalPrice = this.totalPrice + productEntity.getSalePrice();
            this.foodPrice.setText(AppConstants.currencyCode+" "+String.valueOf(this.totalPrice));
        } else {
            this.totalPrice = this.totalPrice - productEntity.getSalePrice();
            this.foodPrice.setText(AppConstants.currencyCode+" "+String.valueOf(this.totalPrice));
        }
    }
}
