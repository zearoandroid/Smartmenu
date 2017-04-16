package com.zearoconsulting.smartmenu.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.model.KOTLineItems;
import com.zearoconsulting.smartmenu.presentation.model.Notes;
import com.zearoconsulting.smartmenu.presentation.model.Organization;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.model.ProductMultiImage;
import com.zearoconsulting.smartmenu.presentation.model.Tables;
import com.zearoconsulting.smartmenu.presentation.model.Terminals;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saravanan on 13-03-2017.
 */

public class SMDataSource {

    // Database fields
    private SQLiteDatabase db;
    private DBHelper dbHelper;

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

    public SMDataSource(Context context) {
        dbHelper = DBHelper.getInstance(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SQLiteDatabase getDb(){
        return  db;
    }

    //******************** POS MASTER RELATED STUFF'S METHODS (ORG, ROLE, WAREHOUSE, BPARTNER, CATEGORY, PRODUCT, AUTHORIZE) ***********************************//

    /**
     * @param org
     */
    public void addOrganization(Organization org) {

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CLIENT_ID, org.getClientId());
            values.put(KEY_ORG_ID, org.getOrgId());
            values.put(KEY_ORG_NAME, org.getOrgName());
            values.put(KEY_ORG_ARABIC_NAME, org.getOrgArabicName());
            values.put(KEY_ORG_IMAGE, org.getOrgImage());
            values.put(KEY_ORG_ADDRESS, org.getOrgAddress());
            values.put(KEY_ORG_PHONE, org.getOrgPhone());
            values.put(KEY_ORG_EMAIL, org.getOrgEmail());
            values.put(KEY_ORG_CITY, org.getOrgCity());
            values.put(KEY_ORG_COUNTRY, org.getOrgCountry());
            values.put(KEY_ORG_WEB_URL, org.getOrgWebUrl());
            values.put(KEY_ORG_DESCRIPTION, org.getOrgDescription());
            values.put(KEY_ORG_SHOW_PRICE, org.getShowPrice());
            values.put(KEY_IS_DEFAULT, org.getIsDefault());

            // Inserting Row
            db.insert(TABLE_ORGANIZATION, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void addCategory(Category category) {

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {

            Cursor mCount = db.rawQuery("select categoryId from category where categoryId='" + category.getCategoryId() + "'", null);
            //mCount.moveToFirst();
            long categoryId = 0;

            while (mCount.moveToNext()) {
                categoryId = mCount.getLong(0);
            }
            mCount.close();

            if (categoryId == 0) {

                ContentValues values = new ContentValues();
                values.put(KEY_CLIENT_ID, category.getClientId());
                values.put(KEY_ORG_ID, category.getOrgId());
                values.put(KEY_CATEGORY_ID, category.getCategoryId());
                values.put(KEY_CATEGORY_NAME, category.getCategoryName());
                values.put(KEY_CATEGORY_ARABIC_NAME, category.getCategoryArabicName());
                values.put(KEY_CATEGORY_VALUE, category.getCategoryValue());
                values.put(KEY_CATEGORY_IMAGE, category.getCategoryImage());
                values.put(KEY_SHOWN_DIGITAL_MENU, category.getShowDigitalMenu());

                // Inserting Row
                db.insert(TABLE_CATEGORY, null, values);
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * @param orgId
     * @param warehouseId
     * @param warehouseName
     */
    public void addWarehouse(long clientId, long orgId, long warehouseId, String warehouseName, String isDefault) {

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CLIENT_ID, clientId);
            values.put(KEY_ORG_ID, orgId);
            values.put(KEY_WAREHOUSE_ID, warehouseId);
            values.put(KEY_WAREHOUSE_NAME, warehouseName);
            values.put(KEY_IS_DEFAULT, isDefault);

            // Inserting Row
            db.insert(TABLE_WAREHOUSE, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * @param roleId
     * @param roleName
     */
    public void addRole(long roleId, String roleName, String isDefault) {

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ROLE_ID, roleId);
            values.put(KEY_ROLE_NAME, roleName);
            values.put(KEY_IS_DEFAULT, isDefault);

            // Inserting Row
            db.insert(TABLE_ROLE, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * @param roleId
     * @param orgId
     */
    public void addRoleAccess(long clientId, long orgId, long roleId) {

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CLIENT_ID, clientId);
            values.put(KEY_ROLE_ID, roleId);
            values.put(KEY_ORG_ID, orgId);

            // Inserting Row
            db.insert(TABLE_ROLE_ACESS, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * @param tables
     */
    public void addTables(Tables tables) {

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {

            Cursor mCount = db.rawQuery("select kotTableId from kotTables where kotTableId='" + tables.getTableId() + "'", null);
            //mCount.moveToFirst();
            long tableId = 0;

            while (mCount.moveToNext()) {
                tableId = mCount.getLong(0);
            }
            mCount.close();

            if (tableId != 0) {
                String strSQL = "update kotTables set kotTableName='" + tables.getTableName() + "', isOrderAvailable='" + tables.getOrderAvailable() + "' ;";
                db.execSQL(strSQL);
            } else {
                ContentValues values = new ContentValues();
                values.put(KEY_CLIENT_ID, tables.getClientId());
                values.put(KEY_ORG_ID, tables.getOrgId());
                values.put(KEY_KOT_TABLE_ID, tables.getTableId());
                values.put(KEY_KOT_TABLE_NAME, tables.getTableName());
                values.put(KEY_IS_ORDER_AVAILABLE, tables.getOrderAvailable());

                // Inserting Row
                db.insert(TABLE_KOT_TABLE, null, values);
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * @param terminals
     */

    public void addTerminals(Terminals terminals) {

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {
            Cursor mCount = db.rawQuery("select kotTerminalId from kotTerminals where kotTerminalId='" + terminals.getTerminalId() + "'", null);
            //mCount.moveToFirst();
            long terminalId = 0;

            while (mCount.moveToNext()) {
                terminalId = mCount.getLong(0);
            }
            mCount.close();

            if (terminalId != 0) {
                String strSQL = "update kotTerminals set kotTerminalName='" + terminals.getTerminalName() + "', kotTerminalIP='" + terminals.getTerminalIP() + "' where kotTerminalId='" + terminals.getTerminalId() + "' ;";
                db.execSQL(strSQL);
            } else {
                ContentValues values = new ContentValues();
                values.put(KEY_CLIENT_ID, terminals.getClientId());
                values.put(KEY_ORG_ID, terminals.getOrgId());
                values.put(KEY_KOT_TERMINAL_ID, terminals.getTerminalId());
                values.put(KEY_KOT_TERMINAL_NAME, terminals.getTerminalName());
                values.put(KEY_KOT_TERMINAL_IP, terminals.getTerminalIP());

                // Inserting Row
                db.insert(TABLE_KOT_TERMINALS, null, values);
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * @param categoryId
     * @param product
     */
    public void addProduct(long categoryId, Product product) {

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {

            Cursor mCount = db.rawQuery("select productId  from product where categoryId='" + categoryId + "' and productId='" + product.getProdId() + "' and parentProductId ='" + product.getParentId() + "' ", null);
            //mCount.moveToFirst();
            long productId = 0;

            while (mCount.moveToNext()) {
                productId = mCount.getLong(0);
            }
            mCount.close();

            if (productId == 0) {

                ContentValues values = new ContentValues();
                values.put(KEY_CLIENT_ID, product.getClientId());//KEY_CLIENT_ID
                values.put(KEY_ORG_ID, product.getOrgId());//KEY_ORG_ID
                values.put(KEY_CATEGORY_ID, categoryId);
                values.put(KEY_PARENT_PRODUCT_ID, product.getParentId());
                values.put(KEY_PRODUCT_ID, product.getProdId());
                values.put(KEY_PRODUCT_NAME, product.getProdName());
                values.put(KEY_PRODUCT_VALE, product.getProdValue());
                values.put(KEY_PRODUCT_UOM_ID, product.getUomId());
                values.put(KEY_PRODUCT_UOM_VALUE, product.getUomValue());
                values.put(KEY_PRODUCT_QTY, 1);
                values.put(KEY_PRODUCT_STD_PRICE, product.getSalePrice());
                values.put(KEY_PRODUCT_COST_PRICE, product.getCostPrice());
                values.put(KEY_PRODUCT_IMAGE, product.getProdImage());
                values.put(KEY_PRODUCT_ARABIC_NAME, product.getProdArabicName());
                values.put(KEY_PRODUCT_DESCRIPTION, product.getDescription());
                values.put(KEY_SHOWN_DIGITAL_MENU, product.getShowDigitalMenu());
                values.put(KEY_PRODUCT_VIDEO, product.getProdVideoPath());
                values.put(KEY_PRODUCT_CALORIES, product.getCalories());
                values.put(KEY_PRODUCT_PREPARATION_TIME, product.getPreparationTime());
                values.put(KEY_PRODUCT_TERMINAL_ID, product.getTerminalId());

                values.put(KEY_PRODUCT_ARABIC_CALORIES, product.getProdArabicCalories());
                values.put(KEY_PRODUCT_ARABIC_PREPARATION_TIME, product.getProdArabicPreTime());
                values.put(KEY_PRODUCT_ARABIC_DESCRIPTION, product.getProdArabicDescription());

                // Inserting Row
                db.insert(TABLE_PRODUCT, null, values);
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void addProductImage(long clientId, long orgId, long productId, String imagePath, String type) {

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CLIENT_ID, clientId);//KEY_CLIENT_ID
            values.put(KEY_ORG_ID, orgId);//KEY_ORG_ID
            values.put(KEY_PRODUCT_ID, productId);
            values.put(KEY_PRODUCT_IMAGE, imagePath);
            values.put(KEY_PRODUCT_TYPE, type);

            db.insert(TABLE_PRODUCT_IMAGES, null, values);

            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void addNotes(Notes notes) {

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {
            Cursor mCount = db.rawQuery("select notesId from productNotes where notesId='" + notes.getNotesId() + "'", null);
            //mCount.moveToFirst();
            long notesId = 0;

            while (mCount.moveToNext()) {
                notesId = mCount.getLong(0);
            }
            mCount.close();

            if (notesId != 0) {
                String strSQL = "update productNotes set notesName='" + notes.getNotesName() + "' where notesId='" + notes.getNotesId() + "' ;";
                db.execSQL(strSQL);
            } else {
                ContentValues values = new ContentValues();
                values.put(KEY_CLIENT_ID, notes.getClientId());
                values.put(KEY_ORG_ID, notes.getOrgId());
                values.put(KEY_PRODUCT_ID, notes.getProdcutId());
                values.put(KEY_PRODUCT_NOTES_ID, notes.getNotesId());
                values.put(KEY_PRODUCT_NOTES_NAME, notes.getNotesName());

                // Inserting Row
                db.insert(TABLE_PRODUCT_NOTES, null, values);
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public List<Long> getDefaultIds() {

        String isDefault = "Y";
        List<Long> defaultIdList = new ArrayList<Long>();
        List<Long> mRoleIdList;

        long orgId = 0;

        try {
            //get the default orgid
            Cursor cursor = db.rawQuery("select orgId from organization where isdefault='" + isDefault + "' ", null);
            while (cursor.moveToNext()) {
                orgId = cursor.getLong(0);
                defaultIdList.add(orgId);
            }
            cursor.close();

            //get the default warehouse id
            Cursor cursor2 = db.rawQuery("select warehouseId from warehouse where orgId='" + orgId + "' and isdefault='" + isDefault + "'", null);
            while (cursor2.moveToNext()) {
                defaultIdList.add(cursor2.getLong(0));
            }
            cursor2.close();

            //get the all role id based on org
            Cursor cursor3 = db.rawQuery("select roleId from roleAccess where orgId='" + orgId + "'", null);
            mRoleIdList = new ArrayList<Long>();
            while (cursor3.moveToNext()) {
                mRoleIdList.add(cursor3.getLong(0));
            }
            cursor3.close();

            for (int i = 0; i < mRoleIdList.size(); i++) {
                //get the default role id
                Cursor cursor4 = db.rawQuery("select roleId from role where isdefault='" + isDefault + "'", null);
                while (cursor4.moveToNext()) {
                    defaultIdList.add(cursor4.getLong(0));
                }
                cursor4.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return defaultIdList;
    }

    public Organization getOrganizationDetail(long orgId) {

        Organization org = null;

        try {
            Cursor cursor = db.rawQuery("select * from organization where orgId ='" + orgId + "'", null);

            while (cursor.moveToNext()) {
                org = new Organization();
                org.setClientId(cursor.getLong(1));
                org.setOrgId(cursor.getLong(2));
                org.setOrgName(cursor.getString(3));
                org.setOrgArabicName(cursor.getString(4));
                org.setOrgImage(cursor.getString(5));
                org.setOrgAddress(cursor.getString(6));
                org.setOrgPhone(cursor.getString(7));
                org.setOrgEmail(cursor.getString(8));
                org.setOrgCity(cursor.getString(9));
                org.setOrgCountry(cursor.getString(10));
                org.setOrgWebUrl(cursor.getString(11));
                org.setOrgDescription(cursor.getString(12));
                org.setShowPrice(cursor.getString(13));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return org;
    }

    public List<Tables> getTables(long clientId, long orgId) {

        List<Tables> tableList = new ArrayList<Tables>();
        Tables table = null;

        try {
            Cursor cursor = db.rawQuery("select kotTableId, kotTableName, isOrderAvailable from kotTables where clientId = '" + clientId + "' and orgId = '" + orgId + "' ORDER BY kotTableId ASC", null);

            while (cursor.moveToNext()) {
                table = new Tables();
                table.setTableId(cursor.getLong(0));
                table.setTableName(cursor.getString(1));
                table.setOrderAvailable(cursor.getString(2));
                tableList.add(table);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return tableList;
    }

    public Tables getTableData(long clientId, long orgId, long tableId) {

        Tables table = null;

        try {
            Cursor cursor = db.rawQuery("select kotTableId, kotTableName, isOrderAvailable from kotTables where clientId = '" + clientId + "' and orgId = '" + orgId + "' and kotTableId = '" + tableId + "' ", null);

            while (cursor.moveToNext()) {
                table = new Tables();
                table.setTableId(cursor.getLong(0));
                table.setTableName(cursor.getString(1));
                table.setOrderAvailable(cursor.getString(2));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return table;
    }

    public List<Long> getTableIds(long clientId, long orgId) {
        long tableId = 0;
        List<Long> mTableIdList = null;

        try {
            //Cursor cursor = db.rawQuery("select kotTableId from kotLineItems where clientId = '"+clientId+"' and orgId = '" + orgId + "' GROUP BY kotTableId ", null);
            Cursor cursor = db.rawQuery("select kotTableId from kotLineItems GROUP BY kotTableId ", null);

            mTableIdList = new ArrayList<Long>();
            while (cursor.moveToNext()) {
                mTableIdList.add(cursor.getLong(0));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return mTableIdList;
    }

    /**
     * @param roleId
     * @return
     */
    public String getRoleName(long roleId) {
        String roleName = null;
        try {
            Cursor cursor = db.rawQuery("select roleName from role where roleId='" + roleId + "';", null);

            while (cursor.moveToNext()) {
                roleName = cursor.getString(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return roleName;
    }

    /**
     * @return
     */
    public List<Category> getDMCategory(long clientId, long orgId) {
        List<Category> categoryList = new ArrayList<Category>();
        Category category = null;

        try {
            Cursor cursor = db.rawQuery("select categoryId, categoryName, categoryArabicName, categoryValue, categoryImage from category where clientId = '" + clientId + "' and orgId = '" + orgId + "' and shownDigitalMenu='Y' ORDER BY categoryName ASC", null);

            while (cursor.moveToNext()) {
                category = new Category();
                category.setCategoryId(cursor.getLong(0));
                category.setCategoryName(cursor.getString(1));
                category.setCategoryArabicName(cursor.getString(2));
                category.setCategoryValue(cursor.getString(3));
                category.setCategoryImage(cursor.getString(4));

                Cursor cur = db.rawQuery("select * from product where categoryId = '" + cursor.getLong(0) + "' and clientId = '" + clientId + "' and orgId = '" + orgId + "' ORDER BY productName COLLATE NOCASE ASC ", null);
                int cnt = cur.getCount();
                cur.close();

                if (cnt > 0)
                    categoryList.add(category);

                if(cursor.getLong(0)==0)
                    categoryList.add(category);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return categoryList;
    }

    /**
     * @param categoryId
     * @return
     */
    public List<Product> getDMProduct(long clientId, long orgId, long categoryId) {

        List<Product> productList = new ArrayList<Product>();
        Product product = null;

        try {
            Cursor cursor = db.rawQuery("select * from product where clientId = '" + clientId + "' and orgId = '" + orgId + "' and categoryId = '" + categoryId + "' and parentProductId=0 and shownDigitalMenu='Y' ORDER BY productName COLLATE NOCASE ASC ", null);

            while (cursor.moveToNext()) {
                product = new Product();
                product.setClientId(cursor.getLong(1)); //KEY_CLIENT_ID
                product.setOrgId(cursor.getLong(2)); //KEY_ORG_ID
                product.setCategoryId(cursor.getLong(3)); //KEY_CATEGORY_ID
                product.setParentId(cursor.getLong(4)); //KEY_PARENT_PRODUCT_ID
                product.setProdId(cursor.getLong(5)); //KEY_PRODUCT_ID
                product.setProdName(cursor.getString(6));//KEY_PRODUCT_NAME
                product.setProdValue(cursor.getString(7));//KEY_PRODUCT_VALE
                product.setUomId(cursor.getLong(8));//KEY_PRODUCT_UOM_ID
                product.setUomValue(cursor.getString(9));//KEY_PRODUCT_UOM_VALUE
                product.setDefaultQty(0);//KEY_PRODUCT_QTY -- cursor.getInt(10)
                product.setSalePrice(cursor.getDouble(11));//KEY_PRODUCT_STD_PRICE
                product.setCostPrice(cursor.getDouble(12));//KEY_PRODUCT_COST_PRICE
                product.setProdImage(cursor.getString(13));//KEY_PRODUCT_IMAGE
                product.setProdArabicName(cursor.getString(14));//KEY_PRODUCT_ARABIC_NAME
                product.setDescription(cursor.getString(15));//KEY_PRODUCT_DESCRIPTION
                product.setShowDigitalMenu(cursor.getString(16));//KEY_SHOWN_DIGITAL_MENU
                product.setProdVideoPath(cursor.getString(17));//KEY_PRODUCT_VIDEO
                product.setCalories(cursor.getString(18));//KEY_PRODUCT_CALORIES
                product.setPreparationTime(cursor.getString(19));//KEY_PRODUCT_PREPARATION_TIME
                product.setTerminalId(cursor.getLong(20));//KEY_PRODUCT_TERMINAL_ID
                product.setProdArabicCalories(cursor.getString(21));//KEY_PRODUCT_ARABIC_CALORIES
                product.setProdArabicPreTime(cursor.getString(22));//KEY_PRODUCT_ARABIC_PREPARATION_TIME
                product.setProdArabicDescription(cursor.getString(23));//KEY_PRODUCT_ARABIC_DESCRIPTION

                productList.add(product);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return productList;
    }

    public List<Product> getRelatedProduct(long clientId, long orgId, long productId) {

        List<Product> productList = new ArrayList<Product>();
        Product product = null;

        try {
            Cursor cursor = db.rawQuery("select * from product where clientId = '" + clientId + "' and orgId = '" + orgId + "' and parentProductId = '" + productId + "' ORDER BY productName COLLATE NOCASE ASC ", null);

            while (cursor.moveToNext()) {
                product = new Product();
                product.setClientId(cursor.getLong(1)); //KEY_CLIENT_ID
                product.setOrgId(cursor.getLong(2)); //KEY_ORG_ID
                product.setCategoryId(cursor.getLong(3)); //KEY_CATEGORY_ID
                product.setParentId(cursor.getLong(4)); //KEY_PARENT_PRODUCT_ID
                product.setProdId(cursor.getLong(5)); //KEY_PRODUCT_ID
                product.setProdName(cursor.getString(6));//KEY_PRODUCT_NAME
                product.setProdValue(cursor.getString(7));//KEY_PRODUCT_VALE
                product.setUomId(cursor.getLong(8));//KEY_PRODUCT_UOM_ID
                product.setUomValue(cursor.getString(9));//KEY_PRODUCT_UOM_VALUE
                product.setDefaultQty(0);//KEY_PRODUCT_QTY -- cursor.getInt(10)
                product.setSalePrice(cursor.getDouble(11));//KEY_PRODUCT_STD_PRICE
                product.setCostPrice(cursor.getDouble(12));//KEY_PRODUCT_COST_PRICE
                product.setProdImage(cursor.getString(13));//KEY_PRODUCT_IMAGE
                product.setProdArabicName(cursor.getString(14));//KEY_PRODUCT_ARABIC_NAME
                product.setDescription(cursor.getString(15));//KEY_PRODUCT_DESCRIPTION
                product.setShowDigitalMenu(cursor.getString(16));//KEY_SHOWN_DIGITAL_MENU
                product.setProdVideoPath(cursor.getString(17));//KEY_PRODUCT_VIDEO
                product.setCalories(cursor.getString(18));//KEY_PRODUCT_CALORIES
                product.setPreparationTime(cursor.getString(19));//KEY_PRODUCT_PREPARATION_TIME
                product.setTerminalId(cursor.getLong(20));//KEY_PRODUCT_TERMINAL_ID
                product.setProdArabicCalories(cursor.getString(21));//KEY_PRODUCT_ARABIC_CALORIES
                product.setProdArabicPreTime(cursor.getString(22));//KEY_PRODUCT_ARABIC_PREPARATION_TIME
                product.setProdArabicDescription(cursor.getString(23));//KEY_PRODUCT_ARABIC_DESCRIPTION
                product.setSelected("N");

                productList.add(product);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return productList;
    }

    public Product getProduct(long clientId, long orgId, long prodId) {

        Product product = null;

        try {
            Cursor cursor = db.rawQuery("select * from product where clientId = '" + clientId + "' and orgId = '" + orgId + "' and productId = '" + prodId + "' ", null);

            while (cursor.moveToNext()) {
                product = new Product();
                product.setClientId(cursor.getLong(1)); //KEY_CLIENT_ID
                product.setOrgId(cursor.getLong(2)); //KEY_ORG_ID
                product.setCategoryId(cursor.getLong(3)); //KEY_CATEGORY_ID
                product.setParentId(cursor.getLong(4)); //KEY_PARENT_PRODUCT_ID
                product.setProdId(cursor.getLong(5)); //KEY_PRODUCT_ID
                product.setProdName(cursor.getString(6));//KEY_PRODUCT_NAME
                product.setProdValue(cursor.getString(7));//KEY_PRODUCT_VALE
                product.setUomId(cursor.getLong(8));//KEY_PRODUCT_UOM_ID
                product.setUomValue(cursor.getString(9));//KEY_PRODUCT_UOM_VALUE
                product.setDefaultQty(0);//KEY_PRODUCT_QTY -- cursor.getInt(10)
                product.setSalePrice(cursor.getDouble(11));//KEY_PRODUCT_STD_PRICE
                product.setCostPrice(cursor.getDouble(12));//KEY_PRODUCT_COST_PRICE
                product.setProdImage(cursor.getString(13));//KEY_PRODUCT_IMAGE
                product.setProdArabicName(cursor.getString(14));//KEY_PRODUCT_ARABIC_NAME
                product.setDescription(cursor.getString(15));//KEY_PRODUCT_DESCRIPTION
                product.setShowDigitalMenu(cursor.getString(16));//KEY_SHOWN_DIGITAL_MENU
                product.setProdVideoPath(cursor.getString(17));//KEY_PRODUCT_VIDEO
                product.setCalories(cursor.getString(18));//KEY_PRODUCT_CALORIES
                product.setPreparationTime(cursor.getString(19));//KEY_PRODUCT_PREPARATION_TIME
                product.setTerminalId(cursor.getLong(20));//KEY_PRODUCT_TERMINAL_ID
                product.setProdArabicCalories(cursor.getString(21));//KEY_PRODUCT_ARABIC_CALORIES
                product.setProdArabicPreTime(cursor.getString(22));//KEY_PRODUCT_ARABIC_PREPARATION_TIME
                product.setProdArabicDescription(cursor.getString(23));//KEY_PRODUCT_ARABIC_DESCRIPTION
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return product;
    }

    /**
     * @return
     */
    public List<Product> getAllProduct(long clientId, long orgId) {

        List<Product> productList = new ArrayList<Product>();
        Product product = null;

        try {
            Cursor cursor = db.rawQuery("select * from product where clientId = '" + clientId + "' and orgId = '" + orgId + "' ORDER BY productName COLLATE NOCASE ASC", null);

            while (cursor.moveToNext()) {
                product = new Product();
                product.setClientId(cursor.getLong(1)); //KEY_CLIENT_ID
                product.setOrgId(cursor.getLong(2)); //KEY_ORG_ID
                product.setCategoryId(cursor.getLong(3)); //KEY_CATEGORY_ID
                product.setParentId(cursor.getLong(4)); //KEY_PARENT_PRODUCT_ID
                product.setProdId(cursor.getLong(5)); //KEY_PRODUCT_ID
                product.setProdName(cursor.getString(6));//KEY_PRODUCT_NAME
                product.setProdValue(cursor.getString(7));//KEY_PRODUCT_VALE
                product.setUomId(cursor.getLong(8));//KEY_PRODUCT_UOM_ID
                product.setUomValue(cursor.getString(9));//KEY_PRODUCT_UOM_VALUE
                product.setDefaultQty(0);//KEY_PRODUCT_QTY -- cursor.getInt(10)
                product.setSalePrice(cursor.getDouble(11));//KEY_PRODUCT_STD_PRICE
                product.setCostPrice(cursor.getDouble(12));//KEY_PRODUCT_COST_PRICE
                product.setProdImage(cursor.getString(13));//KEY_PRODUCT_IMAGE
                product.setProdArabicName(cursor.getString(14));//KEY_PRODUCT_ARABIC_NAME
                product.setDescription(cursor.getString(15));//KEY_PRODUCT_DESCRIPTION
                product.setShowDigitalMenu(cursor.getString(16));//KEY_SHOWN_DIGITAL_MENU
                product.setProdVideoPath(cursor.getString(17));//KEY_PRODUCT_VIDEO
                product.setCalories(cursor.getString(18));//KEY_PRODUCT_CALORIES
                product.setPreparationTime(cursor.getString(19));//KEY_PRODUCT_PREPARATION_TIME
                product.setTerminalId(cursor.getLong(20));//KEY_PRODUCT_TERMINAL_ID
                product.setProdArabicCalories(cursor.getString(21));//KEY_PRODUCT_ARABIC_CALORIES
                product.setProdArabicPreTime(cursor.getString(22));//KEY_PRODUCT_ARABIC_PREPARATION_TIME
                product.setProdArabicDescription(cursor.getString(23));//KEY_PRODUCT_ARABIC_DESCRIPTION

                productList.add(product);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return productList;
    }

    public List<Product> getAllProductIfVideoAvailable() {

        List<Product> productList = new ArrayList<Product>();
        Product product = null;

        try {
            Cursor cursor = db.rawQuery("select productId, productVideo from product", null);

            while (cursor.moveToNext()) {
                product = new Product();
                product.setProdId(cursor.getLong(0));
                product.setProdVideoPath(cursor.getString(1));

                productList.add(product);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return productList;
    }

    public List<ProductMultiImage> getProductImages(long productId) {
        List<ProductMultiImage> multiImageList = new ArrayList<ProductMultiImage>();
        ProductMultiImage multiPath = null;
        try {
            Cursor cursor = db.rawQuery("select productImage, productType from productImages where productId = '" + productId + "' ", null);
            while (cursor.moveToNext()) {
                multiPath = new ProductMultiImage();
                multiPath.setProdId(productId);
                multiPath.setProdMultiPath(cursor.getString(0));
                multiPath.setProdMultiPathType(cursor.getString(1));
                multiImageList.add(multiPath);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return multiImageList;
    }

    public List<Notes> getNotes(long clientId, long orgId, long productId) {
        List<Notes> notesList = new ArrayList<Notes>();
        Notes notes = null;

        try {
            Cursor cursor = db.rawQuery("select notesId, notesName from productNotes where clientId = '" + clientId + "' and orgId = '" + orgId + "' and productId = '" + productId + "' ", null);

            while (cursor.moveToNext()) {
                notes = new Notes();
                notes.setNotesId(cursor.getLong(0));
                notes.setNotesName(cursor.getString(1));
                notesList.add(notes);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return notesList;
    }

    public List<String> getNotesList(long clientId, long orgId, long productId) {
        List<String> notesList = new ArrayList<String>();
        Notes notes = null;

        try {
            Cursor cursor = db.rawQuery("select notesId, notesName from productNotes where clientId = '" + clientId + "' and orgId = '" + orgId + "' and productId = '" + productId + "' ", null);

            notesList.add("Choose one");

            while (cursor.moveToNext()) {
                notes = new Notes();
                notes.setNotesId(cursor.getLong(0));
                notes.setNotesName(cursor.getString(1));
                notesList.add(cursor.getString(1));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return notesList;
    }

    public boolean checkAllCategory() {

        boolean isAvail = false;

        try{
            Cursor mCount = db.rawQuery("select categoryName from category where categoryId='" + 0 + "'", null);

            while (mCount.moveToNext()) {
                isAvail = true;
            }
            mCount.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
        }

        return isAvail;
    }

    public void updateProductVideoPath(long prodId, String videoPath) {

        try {
            String strSQL = "update product set productVideo='" + videoPath + "' where productId = '" + prodId + "' ;";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public List<Long> getOrderAvailableTableIds() {
        long tableId = 0;
        List<Long> mTableIdList = null;
        try {
            Cursor cursor = db.rawQuery("select kotTableId from kotTables where isOrderAvailable='Y' ", null);

            mTableIdList = new ArrayList<Long>();
            while (cursor.moveToNext()) {
                mTableIdList.add(cursor.getLong(0));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return mTableIdList;
    }

    public List<Long> getTableIdsLocalAvailable() {

        List<Long> mTableIdList = null;

        try {
            Cursor cursor = db.rawQuery("select kotTableId from kotLineItems where isPrinted='N' GROUP BY kotTableId ", null);

            mTableIdList = new ArrayList<Long>();
            while (cursor.moveToNext()) {
                mTableIdList.add(cursor.getLong(0));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return mTableIdList;
    }

    public void updateDefaultTableStatus() {
        String strSQL;

        try {
            strSQL = "update kotTables set isOrderAvailable='N';";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void updateTableStatusAvailable(long tableId) {
        String strSQL;

        try {
            if (tableId != 0)
                strSQL = "update kotTables set isOrderAvailable='Y' where kotTableId = '" + tableId + "';";
            else
                strSQL = "update kotTables set isOrderAvailable='N' where kotTableId = '" + tableId + "';";

            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    //******************** SMARTMENU KOT RELATED STUFF'S METHODS ***********************************//

    /**
     * @param tableId
     * @param product
     * @param qty
     * @param notes
     */
    public void addKOTLineItems(long kotLineId, long tableId, long kotNumber, Product product, int qty, String notes, String isPrinted, long rowId, String isExtraProduct, String isDeleted) {

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try {
            double totalPrice = 0;
            /*Cursor mCount = db.rawQuery("select qty from kotLineItems where kotTableId='" + tableId + "' and productId='" + product.getProdId() + "'", null);
            int exQty = 0;

            while (mCount.moveToNext()) {
                exQty = mCount.getInt(0);
            }
            mCount.close();

            if (exQty != 0) {

                qty = qty + exQty;
                totalPrice = qty * product.getSalePrice();

                String strSQL;
                if(!notes.equalsIgnoreCase("")){
                     strSQL = "update kotLineItems set isPrinted='N', kotItemNotes='"+notes+"', qty='" + qty
                            + "', totalPrice='" + totalPrice + "' where kotTableId='"
                            + tableId + "' and productId = '" + product.getProdId() + "' ;";
                }else{
                    strSQL = "update kotLineItems set isPrinted='N', qty='" + qty
                            + "', totalPrice='" + totalPrice + "' where kotTableId='"
                            + tableId + "' and productId = '" + product.getProdId() + "' ;";
                }

                db.execSQL(strSQL);

            } else {*/
            ContentValues values = new ContentValues();

            values.put(KEY_KOT_LINE_ID, kotLineId);
            values.put(KEY_KOT_TABLE_ID, tableId);
            values.put(KEY_KOT_NUMBER, kotNumber);

            values.put(KEY_CATEGORY_ID, product.getCategoryId());
            values.put(KEY_PRODUCT_ID, product.getProdId());
            values.put(KEY_PRODUCT_NAME, product.getProdName());
            values.put(KEY_PRODUCT_ARABIC_NAME, product.getProdArabicName());
            values.put(KEY_PRODUCT_VALE, product.getProdValue());
            values.put(KEY_PRODUCT_UOM_ID, product.getUomId());
            values.put(KEY_PRODUCT_UOM_VALUE, product.getUomValue());
            values.put(KEY_PRODUCT_STD_PRICE, product.getSalePrice());
            values.put(KEY_PRODUCT_COST_PRICE, product.getCostPrice());
            values.put(KEY_KOT_TERMINAL_ID, product.getTerminalId());

            values.put(KEY_PRODUCT_QTY, qty);
            totalPrice = qty * product.getSalePrice();
            values.put(KEY_PRODUCT_TOTAL_PRICE, totalPrice);
            values.put(KEY_KOT_ITEM_NOTES, notes);
            values.put(KEY_IS_PRINTED, isPrinted);
            values.put(KEY_KOT_REF_LINE_ID, rowId);
            values.put(KEY_KOT_EXTRA_PRODUCT, isExtraProduct);
            values.put(KEY_IS_DELETED, isDeleted);

            // Inserting Row
            db.insert(TABLE_KOT_LINES, null, values);

            db.setTransactionSuccessful();
            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public long getLastRowId() {
        long rowId = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT ROWID from kotLineItems order by ROWID DESC limit 1", null);

            while (cursor.moveToNext()) {
                rowId = cursor.getLong(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return rowId;
    }

    /**
     * @param tableId
     * @return
     */
    public List<KOTLineItems> getKOTLineItems(long tableId) {

        List<KOTLineItems> kotLineItemList = new ArrayList<KOTLineItems>();

        KOTLineItems kotLineItems = null;

        try {
            Cursor cursor = db.rawQuery("select * from kotLineItems where kotTableId='" + tableId + "' and isExtraProduct='N' ", null);

            while (cursor.moveToNext()) {
                kotLineItems = new KOTLineItems();
                Product product = new Product();

                kotLineItems.setRowId(cursor.getLong(0));
                kotLineItems.setKotLineId(cursor.getLong(1));
                kotLineItems.setTableId(cursor.getLong(2));
                kotLineItems.setKotNumber(cursor.getLong(3));

                product.setCategoryId(cursor.getLong(4));
                product.setProdId(cursor.getLong(5));
                product.setProdName(cursor.getString(6));
                product.setProdArabicName(cursor.getString(7));
                product.setProdValue(cursor.getString(8));
                product.setUomId(cursor.getLong(9));
                product.setUomValue(cursor.getString(10));
                product.setSalePrice(cursor.getDouble(11));
                product.setCostPrice(cursor.getDouble(12));
                product.setTerminalId(cursor.getLong(13));

                kotLineItems.setProduct(product);
                kotLineItems.setQty(cursor.getInt(14));
                kotLineItems.setTotalPrice(cursor.getDouble(15));
                kotLineItems.setNotes(cursor.getString(16));
                kotLineItems.setPrinted(cursor.getString(17));
                kotLineItems.setRefRowId(cursor.getLong(18));
                kotLineItems.setIsExtraProduct(cursor.getString(19));
                kotLineItems.setIsDeleted(cursor.getString(20));

                kotLineItemList.add(kotLineItems);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return kotLineItemList;
    }

    /**
     * @param tableId
     * @return
     */
    public List<Product> getKOTLineItems(long tableId, long terminalId) {

        List<Product> itemList = new ArrayList<Product>();

        try {
            Cursor cursor = db.rawQuery("select * from kotLineItems where kotTableId='" + tableId + "' and kotTerminalId = '" + terminalId + "' and isPrinted='N' and isExtraProduct='N' ", null);

            while (cursor.moveToNext()) {
                Product product = new Product();

                product.setRowId(cursor.getLong(0));
                product.setKotLineId(cursor.getLong(1));
                product.setTableId(cursor.getLong(2));

                product.setCategoryId(cursor.getLong(4));
                product.setProdId(cursor.getLong(5));
                product.setProdName(cursor.getString(6));
                product.setProdArabicName(cursor.getString(7));
                product.setProdValue(cursor.getString(8));
                product.setUomId(cursor.getLong(9));
                product.setUomValue(cursor.getString(10));
                product.setSalePrice(cursor.getDouble(11));
                product.setCostPrice(cursor.getDouble(12));
                product.setTerminalId(cursor.getLong(13));
                product.setQty(cursor.getInt(14));
                product.setTotalPrice(cursor.getDouble(15));
                product.setDescription(cursor.getString(16));

                itemList.add(product);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return itemList;
    }

    public List<Product> getKOTExtraLineItems(long tableId, long refRowId) {

        List<Product> itemList = new ArrayList<Product>();

        try {
            Cursor cursor = db.rawQuery("select * from kotLineItems where kotTableId='" + tableId + "' and kotRefLineId = '" + refRowId + "' and isPrinted='N' and isExtraProduct='Y' ", null);

            while (cursor.moveToNext()) {
                Product product = new Product();

                product.setRowId(cursor.getLong(0));
                product.setKotLineId(cursor.getLong(1));
                product.setTableId(cursor.getLong(2));

                product.setCategoryId(cursor.getLong(4));
                product.setProdId(cursor.getLong(5));
                product.setProdName(cursor.getString(6));
                product.setProdArabicName(cursor.getString(7));
                product.setProdValue(cursor.getString(8));
                product.setUomId(cursor.getLong(9));
                product.setUomValue(cursor.getString(10));
                product.setSalePrice(cursor.getDouble(11));
                product.setCostPrice(cursor.getDouble(12));
                product.setTerminalId(cursor.getLong(13));
                product.setQty(cursor.getInt(14));
                product.setTotalPrice(cursor.getDouble(15));
                product.setDescription(cursor.getString(16));

                itemList.add(product);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return itemList;
    }

    /**
     * @param tableId
     * @return
     */
    public int sumOfCartItems(long tableId) {
        int totalQty = 0;
        try {
            Cursor cursor = db.rawQuery("select sum(qty) from kotLineItems where kotTableId = '" + tableId + "' and isExtraProduct='N' ", null);

            while (cursor.moveToNext()) {
                totalQty = cursor.getInt(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return totalQty;
    }

    /**
     * @param tableId
     * @return
     */
    public double sumOfCartItemsTotal(long tableId) {
        double totalPrice = 0;
        try {
            Cursor cursor = db.rawQuery("select sum(totalPrice) from kotLineItems where kotTableId = '" + tableId + "'", null);

            while (cursor.moveToNext()) {
                totalPrice = cursor.getDouble(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return totalPrice;
    }

    /**
     * @param tableId
     * @return
     */
    public double sumOfTerminalItemsTotal(long tableId, long terminalId) {
        double totalPrice = 0;
        try {
            Cursor cursor = db.rawQuery("select sum(totalPrice) from kotLineItems where kotTableId = '" + tableId + "' and kotTerminalId = '" + terminalId + "' and isPrinted='N' ", null);

            while (cursor.moveToNext()) {
                totalPrice = cursor.getDouble(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return totalPrice;
    }

    public List<String> getKOTOrderedItemTerminals(long tableId) {

        List<String> kotTerminalList = new ArrayList<String>();
        try {
            Cursor cursor = db.rawQuery("select kotTerminalId from kotLineItems where kotTableId = '" + tableId + "' and isPrinted='N' GROUP BY kotTerminalId ", null);
            while (cursor.moveToNext()) {
                kotTerminalList.add(String.valueOf(cursor.getLong(0)));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return kotTerminalList;
    }

    public void updateKOTLineItems(long tableId) {
        try {
            String strSQL = "update kotLineItems set isPrinted='Y' where kotTableId = '" + tableId + "' ;";
            db.execSQL(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * @param tableId
     * @param kotLineId
     */
    public void deleteKOTLineItem(long tableId, long kotLineId) {

        try {
            db.execSQL("delete from kotLineItems where kotTableId = '" + tableId + "' and kotLineId = '" + kotLineId + "'");
            db.execSQL("delete from kotLineItems where kotTableId = '" + tableId + "' and kotRefLineId = '" + kotLineId + "'");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * @param tableId
     */
    public void deleteKOTLineItems(long tableId) {
        try {

            if (tableId != 0)
                db.execSQL("delete from kotLineItems where kotTableId = '" + tableId + "' ");
            else
                db.execSQL("delete from kotLineItems ");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    //******************** DELETE METHODS FOR MASTER TABLES ***********************************//

    /**
     * DELETE ALL DHUKAN RELATED TABLES
     */
    public void deleteDhukanTables() {

        db.beginTransaction();
        try {
            db.execSQL("delete from organization");
            db.execSQL("delete from warehouse");
            db.execSQL("delete from role");
            db.execSQL("delete from roleAccess");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteSmartMenuTables() {
        db.beginTransaction();
        try {
            db.execSQL("delete from kotTables");
            db.execSQL("delete from kotTerminals");
            db.execSQL("delete from category");
            db.execSQL("delete from product");
            db.execSQL("delete from productNotes");
            db.execSQL("delete from productImages");
            db.execSQL("delete from kotHeader");
            db.execSQL("delete from kotLineItems");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
