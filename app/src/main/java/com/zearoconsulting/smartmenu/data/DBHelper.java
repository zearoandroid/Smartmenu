package com.zearoconsulting.smartmenu.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zearoconsulting.smartmenu.presentation.model.BPartner;
import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.model.Customer;
import com.zearoconsulting.smartmenu.presentation.model.KOTHeader;
import com.zearoconsulting.smartmenu.presentation.model.KOTLineItems;
import com.zearoconsulting.smartmenu.presentation.model.Notes;
import com.zearoconsulting.smartmenu.presentation.model.Organization;
import com.zearoconsulting.smartmenu.presentation.model.POSLineItem;
import com.zearoconsulting.smartmenu.presentation.model.POSOrders;
import com.zearoconsulting.smartmenu.presentation.model.POSPayment;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.model.ProductMultiImage;
import com.zearoconsulting.smartmenu.presentation.model.Role;
import com.zearoconsulting.smartmenu.presentation.model.Tables;
import com.zearoconsulting.smartmenu.presentation.model.Terminals;
import com.zearoconsulting.smartmenu.presentation.model.Warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by saravanan on 24-05-2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "smartMenuDB";

    //organization table
    private static final String TABLE_ORGANIZATION = "organization";

    //warehouse table
    private static final String TABLE_WAREHOUSE = "warehouse";

    // role table
    private static final String TABLE_ROLE = "role";

    // role table
    private static final String TABLE_ROLE_ACESS = "roleAccess";

    // category table
    private static final String TABLE_CATEGORY = "category";

    // product table
    private static final String TABLE_PRODUCT = "product";

    // productNotes table
    private static final String TABLE_PRODUCT_NOTES = "productNotes";

    // BPartner table
    private static final String TABLE_BPARTNERS = "businessPartner";

    // BPartner table
    private static final String TABLE_SUPERVISOR = "supervisor";

    // kot tables table
    private static final String TABLE_KOT_TABLE = "kotTables";

    // kot terminals table
    private static final String TABLE_KOT_TERMINALS = "kotTerminals";

    // kot header table
    private static final String TABLE_KOT_HEADER = "kotHeader";

    // kot line item detail table
    private static final String TABLE_KOT_LINES = "kotLineItems";

    private static final String TABLE_PRODUCT_IMAGES = "productImages";

    //COLUMNS
    private static final String KEY_ID = "_id";
    private static final String KEY_ORG_ID = "orgId";
    private static final String KEY_CLIENT_ID = "clientId";
    private static final String KEY_ORG_NAME = "orgName";
    private static final String KEY_ORG_ARABIC_NAME = "orgArabicName";
    private static final String KEY_ORG_IMAGE = "orgImage";
    private static final String KEY_ORG_PHONE = "orgPhone";
    private static final String KEY_ORG_EMAIL = "orgEmail";
    private static final String KEY_ORG_ADDRESS = "orgAddress";
    private static final String KEY_ORG_CITY = "orgCity";
    private static final String KEY_ORG_COUNTRY = "orgCountry";
    private static final String KEY_ORG_WEB_URL = "orgWebUrl";
    private static final String KEY_ORG_DESCRIPTION = "orgDescription";
    private static final String KEY_ORG_SHOW_PRICE = "orgShowPrice";

    private static final String KEY_WAREHOUSE_ID = "warehouseId";
    private static final String KEY_WAREHOUSE_NAME = "warehouseName";

    private static final String KEY_ROLE_ID = "roleId";
    private static final String KEY_ROLE_NAME = "roleName";

    private static final String KEY_CATEGORY_ID = "categoryId";
    private static final String KEY_CATEGORY_NAME = "categoryName";
    private static final String KEY_CATEGORY_ARABIC_NAME = "categoryArabicName";
    private static final String KEY_CATEGORY_VALUE = "categoryValue";
    private static final String KEY_CATEGORY_IMAGE = "categoryImage";
    private static final String KEY_SHOWN_DIGITAL_MENU = "shownDigitalMenu";

    private static final String KEY_BP_ID = "bpId";
    private static final String KEY_CUSTOMER_NAME = "customerName";
    private static final String KEY_PRICELIST_ID = "pricelistId";
    private static final String KEY_CUSTOMER_VALUE = "value";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NUMBER = "mobilenumber";

    private static final String KEY_PARENT_PRODUCT_ID = "parentProductId";
    private static final String KEY_PRODUCT_ID = "productId";
    private static final String KEY_PRODUCT_NAME = "productName";
    private static final String KEY_PRODUCT_ARABIC_NAME = "productArabicName";
    private static final String KEY_PRODUCT_VALE = "productValue";
    private static final String KEY_PRODUCT_UOM_ID = "uomId";
    private static final String KEY_PRODUCT_UOM_VALUE = "uomValue";
    private static final String KEY_PRODUCT_QTY = "qty";
    private static final String KEY_PRODUCT_STD_PRICE = "stdPrice";
    private static final String KEY_PRODUCT_COST_PRICE = "costPrice";
    private static final String KEY_PRODUCT_TOTAL_PRICE = "totalPrice";
    private static final String KEY_PRODUCT_IMAGE = "productImage";
    private static final String KEY_PRODUCT_VIDEO = "productVideo";
    private static final String KEY_PRODUCT_CALORIES = "calories";
    private static final String KEY_PRODUCT_PREPARATION_TIME = "preparationTime";
    private static final String KEY_PRODUCT_DESCRIPTION = "description";
    private static final String KEY_PRODUCT_TERMINAL_ID = "terminalId";
    private static final String KEY_PRODUCT_ARABIC_CALORIES = "prodArabicCalories";
    private static final String KEY_PRODUCT_ARABIC_PREPARATION_TIME = "prodArabicPreTime";
    private static final String KEY_PRODUCT_ARABIC_DESCRIPTION = "prodArabicDesc";

    private static final String KEY_PRODUCT_NOTES_ID = "notesId";
    private static final String KEY_PRODUCT_NOTES_NAME = "notesName";

    private static final String KEY_PRODUCT_TYPE = "productType";
    private static final String KEY_IS_DEFAULT = "isDefault";
    private static final String KEY_IS_PRINTED = "isPrinted";
    private static final String KEY_AUTHORIZE_CODE = "authorizeCode";

    private static final String KEY_KOT_TABLE_ID = "kotTableId";
    private static final String KEY_KOT_TABLE_NAME = "kotTableName";
    private static final String KEY_IS_ORDER_AVAILABLE = "isOrderAvailable";

    private static final String KEY_KOT_TERMINAL_ID = "kotTerminalId";
    private static final String KEY_KOT_TERMINAL_NAME = "kotTerminalName";
    private static final String KEY_KOT_TERMINAL_IP = "kotTerminalIP";

    private static final String KEY_KOT_NUMBER = "kotNumber";
    private static final String KEY_INVOICE_NUMBER = "invoiceNumber";
    private static final String KEY_KOT_TOTAL_AMOUNT = "kotTotalAmount";
    private static final String KEY_KOT_STATUS = "kotStatus";
    private static final String KEY_KOT_ITEM_NOTES = "kotItemNotes";
    private static final String KEY_KOT_LINE_ID = "kotLineId";
    private static final String KEY_KOT_REF_LINE_ID = "kotRefLineId";
    private static final String KEY_KOT_EXTRA_PRODUCT = "isExtraProduct";
    private static final String KEY_IS_DELETED = "isDeleted";

    private static final String KEY_IS_COVERS_LEVEL = "isCoversLevel";
    private static final String KEY_M_TABLE_GROUP_ID = "m_table_group_id";
    private static final String KEY_COVERS_DETAILS = "covers_details";
    private static final String KEY_M_TABLES_ID = "m_tables_id";


    //create query for TABLE_ORGANIZATION
    private static final String ORGANIZATION_CREATE_QUERY = "CREATE TABLE "
            + TABLE_ORGANIZATION + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_ORG_NAME + " TEXT, " + KEY_ORG_ARABIC_NAME + " TEXT, "
            + KEY_ORG_IMAGE + " TEXT, " + KEY_ORG_ADDRESS + " TEXT, " + KEY_ORG_PHONE + " TEXT, " + KEY_ORG_EMAIL + " TEXT, " + KEY_ORG_CITY + " TEXT, "
            + KEY_ORG_COUNTRY + " TEXT, " + KEY_ORG_WEB_URL + " TEXT, " + KEY_ORG_DESCRIPTION + " TEXT, " + KEY_ORG_SHOW_PRICE + " TEXT, " + KEY_IS_DEFAULT + " TEXT);";

    //create query for TABLE_WAREHOUSE
    private static final String WAREHOUSE_CREATE_QUERY = "CREATE TABLE "
            + TABLE_WAREHOUSE + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_WAREHOUSE_ID + " NUMERIC, " + KEY_WAREHOUSE_NAME
            + " TEXT, " + KEY_IS_DEFAULT + " TEXT);";

    //create query for TABLE_ROLE
    private static final String ROLE_CREATE_QUERY = "CREATE TABLE "
            + TABLE_ROLE + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_ROLE_ID + " NUMERIC, " + KEY_ROLE_NAME
            + " TEXT, " + KEY_IS_DEFAULT + " TEXT);";

    //create query for TABLE_ROLE
    private static final String ROLE_ACCESS_CREATE_QUERY = "CREATE TABLE "
            + TABLE_ROLE_ACESS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_ROLE_ID + " NUMERIC);";

    //create query for TABLE_ROLE
    private static final String CATEGORY_CREATE_QUERY = "CREATE TABLE "
            + TABLE_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_CATEGORY_ID + " NUMERIC, " + KEY_CATEGORY_NAME + " TEXT, "
            + KEY_CATEGORY_ARABIC_NAME + " TEXT, " + KEY_CATEGORY_VALUE + " TEXT, " + KEY_CATEGORY_IMAGE + " TEXT," + KEY_SHOWN_DIGITAL_MENU + " TEXT);";

    //create query for TABLE_PRODUCT
    private static final String PRODUCT_CREATE_QUERY = "CREATE TABLE "
            + TABLE_PRODUCT + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_CATEGORY_ID
            + " NUMERIC, " + KEY_PARENT_PRODUCT_ID + " NUMERIC, " + KEY_PRODUCT_ID + " NUMERIC, " + KEY_PRODUCT_NAME + " TEXT, "
            + KEY_PRODUCT_VALE + " TEXT, " + KEY_PRODUCT_UOM_ID + " NUMERIC, " + KEY_PRODUCT_UOM_VALUE + " TEXT, "
            + KEY_PRODUCT_QTY + " INTEGER, " + KEY_PRODUCT_STD_PRICE + " NUMERIC, "
            + KEY_PRODUCT_COST_PRICE + " NUMERIC, " + KEY_PRODUCT_IMAGE + " TEXT," + KEY_PRODUCT_ARABIC_NAME + " TEXT,"
            + KEY_PRODUCT_DESCRIPTION + " TEXT," + KEY_SHOWN_DIGITAL_MENU + " TEXT," + KEY_PRODUCT_VIDEO + " TEXT,"
            + KEY_PRODUCT_CALORIES + " TEXT," + KEY_PRODUCT_PREPARATION_TIME + " TEXT, " + KEY_PRODUCT_TERMINAL_ID + " NUMERIC,"
            + KEY_PRODUCT_ARABIC_CALORIES + " TEXT," + KEY_PRODUCT_ARABIC_PREPARATION_TIME + " TEXT, " + KEY_PRODUCT_ARABIC_DESCRIPTION + " TEXT);";

    //create query for TABLE_BPARTNER
    private static final String BPARTNER_CREATE_QUERY = "CREATE TABLE "
            + TABLE_BPARTNERS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_BP_ID + " NUMERIC, " + KEY_CUSTOMER_NAME + " TEXT, " + KEY_CUSTOMER_VALUE
            + " NUMERIC, " + KEY_PRICELIST_ID + " NUMERIC, " + KEY_EMAIL + " TEXT, " + KEY_NUMBER + " NUMERIC);";

    //create query for TABLE_SUPERVISOR
    private static final String SUPERVISOR_CREATE_QUERY = "CREATE TABLE "
            + TABLE_SUPERVISOR + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_ORG_ID + " NUMERIC, " + KEY_AUTHORIZE_CODE + " TEXT);";

    //create query for KOT_TABLES
    private static final String KOT_TABLES_CREATE_QUERY = "CREATE TABLE "
            + TABLE_KOT_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_KOT_TABLE_ID + " NUMERIC, " + KEY_KOT_TABLE_NAME + " TEXT, " + KEY_IS_ORDER_AVAILABLE + " TEXT, " + KEY_IS_COVERS_LEVEL + " TEXT, " + KEY_M_TABLE_GROUP_ID + " NUMERIC);";

    //create query for KOT_TERMINALS
    private static final String KOT_TERMINALS_CREATE_QUERY = "CREATE TABLE "
            + TABLE_KOT_TERMINALS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_KOT_TERMINAL_ID + " NUMERIC, " + KEY_KOT_TERMINAL_NAME + " TEXT, "
            + KEY_KOT_TERMINAL_IP + " TEXT );";

    //create query for KOT_HEADER
    private static final String KOT_HEADER_CREATE_QUERY = "CREATE TABLE "
            + TABLE_KOT_HEADER + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_KOT_TABLE_ID + " NUMERIC, " + KEY_KOT_NUMBER + " NUMERIC, "
            + KEY_INVOICE_NUMBER + " NUMERIC, " + KEY_KOT_TERMINAL_ID + " NUMERIC, " +  KEY_COVERS_DETAILS + " TEXT);";

    //create query for TABLE_KOT_LINES
    private static final String KOT_LINE_ITEM_CREATE_QUERY = "CREATE TABLE "
            + TABLE_KOT_LINES + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_KOT_LINE_ID + " NUMERIC, " + KEY_KOT_TABLE_ID
            + " NUMERIC, " + KEY_KOT_NUMBER + " NUMERIC, " + KEY_CATEGORY_ID + " NUMERIC, " + KEY_PRODUCT_ID + " NUMERIC, " + KEY_PRODUCT_NAME + " TEXT, "
            + KEY_PRODUCT_ARABIC_NAME + " TEXT, " + KEY_PRODUCT_VALE + " TEXT, " + KEY_PRODUCT_UOM_ID + " NUMERIC, " + KEY_PRODUCT_UOM_VALUE + " TEXT, "
            + KEY_PRODUCT_STD_PRICE + " NUMERIC, " + KEY_PRODUCT_COST_PRICE + " INTEGER, "
            + KEY_KOT_TERMINAL_ID + " NUMERIC, " + KEY_PRODUCT_QTY + " INTEGER, " + KEY_PRODUCT_TOTAL_PRICE + " NUMERIC, "
            + KEY_KOT_ITEM_NOTES + " TEXT, " + KEY_IS_PRINTED + " TEXT, " + KEY_KOT_REF_LINE_ID + " NUMERIC, " + KEY_KOT_EXTRA_PRODUCT + " TEXT, " + KEY_IS_DELETED + " TEXT, " + KEY_M_TABLES_ID + " NUMERIC);";

    private static final String PRODUCT_IMAGES_CREATE_QUERY = "CREATE TABLE "
            + TABLE_PRODUCT_IMAGES + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_PRODUCT_ID + " NUMERIC, "
            + KEY_PRODUCT_IMAGE + " TEXT, " + KEY_PRODUCT_TYPE + " TEXT);";

    private static final String PRODUCT_NOTES_CREATE_QUERY = "CREATE TABLE "
            + TABLE_PRODUCT_NOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CLIENT_ID + " NUMERIC, " + KEY_ORG_ID + " NUMERIC, " + KEY_PRODUCT_ID + " NUMERIC, "
            + KEY_PRODUCT_NOTES_ID + " NUMERIC, " + KEY_PRODUCT_NOTES_NAME + " TEXT);";


    private static DBHelper sInstance;

    public static synchronized DBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ORGANIZATION_CREATE_QUERY);
        db.execSQL(WAREHOUSE_CREATE_QUERY);
        db.execSQL(ROLE_CREATE_QUERY);
        db.execSQL(ROLE_ACCESS_CREATE_QUERY);
        db.execSQL(CATEGORY_CREATE_QUERY);
        db.execSQL(PRODUCT_CREATE_QUERY);
        db.execSQL(BPARTNER_CREATE_QUERY);
        db.execSQL(SUPERVISOR_CREATE_QUERY);

        db.execSQL(KOT_TABLES_CREATE_QUERY);
        db.execSQL(KOT_TERMINALS_CREATE_QUERY);
        db.execSQL(KOT_HEADER_CREATE_QUERY);
        db.execSQL(KOT_LINE_ITEM_CREATE_QUERY);
        db.execSQL(PRODUCT_IMAGES_CREATE_QUERY);
        db.execSQL(PRODUCT_NOTES_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KOT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KOT_HEADER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KOT_LINES);
            db.execSQL(KOT_TABLES_CREATE_QUERY);
            db.execSQL(KOT_HEADER_CREATE_QUERY);
            db.execSQL(KOT_LINE_ITEM_CREATE_QUERY);
        }
    }
}
