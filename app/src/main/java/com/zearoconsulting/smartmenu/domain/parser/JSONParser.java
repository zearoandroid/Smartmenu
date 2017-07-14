package com.zearoconsulting.smartmenu.domain.parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.BuildConfig;
import com.zearoconsulting.smartmenu.R;
import com.zearoconsulting.smartmenu.data.AppDataManager;
import com.zearoconsulting.smartmenu.data.AppLog;
import com.zearoconsulting.smartmenu.data.DBHelper;
import com.zearoconsulting.smartmenu.data.SMDataSource;
import com.zearoconsulting.smartmenu.presentation.model.BPartner;
import com.zearoconsulting.smartmenu.presentation.model.Category;
import com.zearoconsulting.smartmenu.presentation.model.Customer;
import com.zearoconsulting.smartmenu.presentation.model.KOTHeader;
import com.zearoconsulting.smartmenu.presentation.model.Notes;
import com.zearoconsulting.smartmenu.presentation.model.Organization;
import com.zearoconsulting.smartmenu.presentation.model.POSLineItem;
import com.zearoconsulting.smartmenu.presentation.model.Product;
import com.zearoconsulting.smartmenu.presentation.model.Tables;
import com.zearoconsulting.smartmenu.presentation.model.Terminals;
import com.zearoconsulting.smartmenu.presentation.view.fragment.AddtoCartViewFragment;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.Common;
import com.zearoconsulting.smartmenu.utils.FileUtils;
import com.zearoconsulting.smartmenu.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import static com.zearoconsulting.smartmenu.utils.AppConstants.isTableVisible;

/**
 * Created by saravanan on 25-05-2016.
 */
public class JSONParser {

    private AppDataManager mAppManager;
    private SMDataSource mDBHelper;
    private Context mContext;
    private Bundle b = new Bundle();

    /**
     * @param context
     * @param appDataManager
     * @param dbHelper
     */
    public JSONParser(Context context, AppDataManager appDataManager, SMDataSource dbHelper) {

        this.mContext = context;
        this.mAppManager = appDataManager;
        this.mDBHelper = dbHelper;
    }

    /**
     * @param methodType
     * @return mJsonObj
     */
    public JSONObject getParams(int methodType) {

        JSONObject mJsonObj = new JSONObject();

        try {
            mJsonObj.put("macAddress", Common.getMacAddr());
            mJsonObj.put("username", mAppManager.getUserName());
            mJsonObj.put("password", mAppManager.getUserPassword());
            mJsonObj.put("userId", mAppManager.getUserID());
            mJsonObj.put("clientId", mAppManager.getClientID());
            mJsonObj.put("roleId", mAppManager.getRoleID());
            mJsonObj.put("orgId", mAppManager.getOrgID());
            mJsonObj.put("warehouseId", mAppManager.getWarehouseID());
            mJsonObj.put("businessPartnerId", mAppManager.getUserBPID());
            mJsonObj.put("remindMe", mAppManager.getRemindMeStatus());
            mJsonObj.put("version", BuildConfig.VERSION_NAME);
            mJsonObj.put("appName", "SmartMenu");

            switch (methodType) {
                case AppConstants.GET_ORGANIZATION_DATA:
                    mJsonObj.put("operation", "POSOrganization");
                    break;
                case AppConstants.CALL_AUTHENTICATE:
                    mJsonObj.put("operation", "POSLogin");
                    break;
                case AppConstants.GET_CASH_CUSTOMER_DATA:
                    mJsonObj.put("operation", "POSCashCustomer");
                    break;
                case AppConstants.GET_POS_NUMBER:
                    mJsonObj.put("operation", "POSOrderNumber");
                    break;
                case AppConstants.GET_CATEGORY:
                    mJsonObj.put("operation", "POSCategory");
                    break;
                case AppConstants.GET_PRODUCTS:
                    mJsonObj.put("operation", "POSProducts");
                    break;
                case AppConstants.GET_ALL_PRODUCTS:
                    mJsonObj.put("operation", "POSAllProducts");
                    break;
                case AppConstants.GET_PRODUCT_PRICE:
                    mJsonObj.put("operation", "POSProductPrice");
                    break;
                case AppConstants.GET_TABLES:
                    mJsonObj.put("operation", "POSTables");
                    break;
                case AppConstants.GET_TERMINALS:
                    mJsonObj.put("operation", "POSTerminals");
                    break;
                case AppConstants.POST_KOT_DATA:
                    mJsonObj.put("operation", "KOTData");
                    mJsonObj.put("currencyId", mAppManager.getCurrencyID());
                    mJsonObj.put("paymentTermId", mAppManager.getPaymentTermID());
                    mJsonObj.put("pricelistId", mAppManager.getPriceListID());
                    break;
                case AppConstants.GET_TABLE_LINE_ITEMS:
                    mJsonObj.put("operation", "KOTLineItems");
                    break;
                case AppConstants.CALL_RELEASE_POS_ORDER:
                    mJsonObj.put("operation", "POSReleaseOrder");
                    break;
                case AppConstants.GET_BPARTNERS:
                    mJsonObj.put("operation", "POSCustomers");
                    break;
                case AppConstants.GET_TABLE_STATUS:
                    mJsonObj.put("operation", "GetDraftedTables");
                    break;
                case AppConstants.POST_FEEDBACK_DATA:
                    mJsonObj.put("operation", "PosFeedback");
                    break;
                case AppConstants.GET_TABLE_KOT_DETAILS:
                    mJsonObj.put("operation", "getTableKot");
                    break;
                case AppConstants.CHECK_UPDATE_AVAILABLE:
                    mJsonObj.put("operation","checkAppUpdate");
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mJsonObj;
    }

    /**
     * @param posId
     * @param customer
     * @param totalLine
     * @param totalAmount
     * @param paidAmount
     * @param dueAmount
     * @return mHeaderObj
     */
    public JSONObject getHeaderObj(long posId, Customer customer, int totalLine, double totalAmount, double paidAmount, double dueAmount, double cashAmount, double cardAmount) {

        JSONObject mHeaderObj = new JSONObject();

        try {

            mHeaderObj.put("posId", AppConstants.posID);
            mHeaderObj.put("clientId", mAppManager.getClientID());
            mHeaderObj.put("orgId", mAppManager.getOrgID());
            mHeaderObj.put("userId", mAppManager.getUserID());
            mHeaderObj.put("businessPartnerId", customer.getBpId());
            mHeaderObj.put("periodId", mAppManager.getPeriodID());
            mHeaderObj.put("accountSchemaId", mAppManager.getAcctSchemaID());
            mHeaderObj.put("adTableId", mAppManager.getAdTableID());
            mHeaderObj.put("totalLines", totalLine);
            mHeaderObj.put("totalAmount", totalAmount);
            mHeaderObj.put("currencyId", mAppManager.getCurrencyID());
            mHeaderObj.put("paymentTermId", mAppManager.getPaymentTermID());
            mHeaderObj.put("warehouseId", mAppManager.getWarehouseID());
            mHeaderObj.put("pricelistId", mAppManager.getPriceListID());
            mHeaderObj.put("cashbookId", mAppManager.getCashbookID());
            mHeaderObj.put("paidAmount", paidAmount);
            mHeaderObj.put("dueAmount", 0);
            mHeaderObj.put("customerName", customer.getCustomerName());
            mHeaderObj.put("cashAmount", cashAmount);
            if (cashAmount == 0)
                mHeaderObj.put("IsCash", "N");
            else
                mHeaderObj.put("IsCash", "Y");
            mHeaderObj.put("cardAmount", cardAmount);
            if (cardAmount == 0)
                mHeaderObj.put("IsCard", "N");
            else
                mHeaderObj.put("IsCard", "Y");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mHeaderObj;
    }

    /**
     * @param valHospital
     * @param valProducts
     * @param valService
     * @param valEnvironment
     * @param comments
     * @param name
     * @param phone
     * @param email
     * @return mFeedbackObj
     */
    public JSONObject getFeedbackObj(int valHospital, int valProducts, int valService, int valEnvironment, String comments, String name, String phone, String email) {
        JSONObject mFeedbackObj = new JSONObject();
        try {
            mFeedbackObj.put("hospitality", Common.getFeedbackRange(valHospital));
            mFeedbackObj.put("Products", Common.getFeedbackRange(valProducts));
            mFeedbackObj.put("Service", Common.getFeedbackRange(valService));
            mFeedbackObj.put("Environment", Common.getFeedbackRange(valEnvironment));
            mFeedbackObj.put("Comments", comments);
            mFeedbackObj.put("Name", name);
            mFeedbackObj.put("PhoneNumber", phone);
            mFeedbackObj.put("Email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mFeedbackObj;
    }

    public JSONArray getPaymentObj(HashMap mPayments) {

        JSONArray mArrayItems = new JSONArray();

        try {
            // Get a set of the entries
            Set set = mPayments.entrySet();
            // Get an iterator
            Iterator i = set.iterator();
            // Display elements
            while (i.hasNext()) {
                JSONObject mPaymentOhj = new JSONObject();
                Map.Entry me = (Map.Entry) i.next();
                System.out.print(me.getKey() + ": ");
                System.out.println(me.getValue());

                String key = ((String) me.getKey());
                double amount = ((Double) me.getValue()).doubleValue();

                mPaymentOhj.put("paymenttype", key);
                mPaymentOhj.put("amount", amount);

                mArrayItems.put(mPaymentOhj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mArrayItems;
    }

    /**
     * @param lineItems
     * @return
     */
    public JSONArray getOrderItems(List<POSLineItem> lineItems) {

        JSONArray mArrayItems = new JSONArray();

        try {
            for (int i = 0; i < lineItems.size(); i++) {
                JSONObject jo = new JSONObject();
                jo.put("productId", lineItems.get(i).getProductId());
                jo.put("productCategoryId", lineItems.get(i).getCategoryId());
                jo.put("productName", lineItems.get(i).getProductName());
                jo.put("uomId", lineItems.get(i).getPosUOMId());
                jo.put("qty", lineItems.get(i).getPosQty());
                jo.put("productUOMValue", lineItems.get(i).getPosUOMValue());
                jo.put("actualPrice", lineItems.get(i).getStdPrice());
                //GET the discount type and calculate the value
                int mDiscType = lineItems.get(i).getDiscType();
                int discVal = lineItems.get(i).getDiscValue();
                double prodPrice = lineItems.get(i).getStdPrice();
                if (mDiscType == 0 && discVal != 0) {
                    prodPrice = prodPrice - (prodPrice * discVal / 100);
                } else {
                    prodPrice = prodPrice - discVal;
                }
                jo.put("price", prodPrice);
                jo.put("costPrice", lineItems.get(i).getCostPrice());
                mArrayItems.put(jo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mArrayItems;
    }

    /**
     * @return
     */
    public JSONArray getKOTOrderItems() {

        JSONArray mArrayTokens = new JSONArray();

        try {

            List<String> terminalList = mDBHelper.getKOTOrderedItemTerminals(AppConstants.tableID);
            for (int i = 0; i < terminalList.size(); i++) {

                long terminalID = Long.parseLong(terminalList.get(i));
                double totalPrice = mDBHelper.sumOfTerminalItemsTotal(AppConstants.tableID, terminalID);

                JSONObject jo = new JSONObject();

                jo.put("terminalId", terminalID);
                jo.put("totalAmount", totalPrice);
                jo.put("invoiceNumber", 0);

                List<Product> productList = mDBHelper.getKOTLineItems(AppConstants.tableID, terminalID);

                JSONArray mArrayItems = new JSONArray();
                for (int j = 0; j < productList.size(); j++) {
                    JSONObject jItemObj = new JSONObject();
                    jItemObj.put("productId", productList.get(j).getProdId());
                    jItemObj.put("productName", productList.get(j).getProdName());
                    jItemObj.put("productValue", productList.get(j).getProdValue());
                    jItemObj.put("categoryId", productList.get(j).getCategoryId());
                    jItemObj.put("productUOMId", productList.get(j).getUomId());
                    jItemObj.put("productUOMValue", productList.get(j).getUomValue());
                    jItemObj.put("sellingPrice", productList.get(j).getSalePrice());
                    jItemObj.put("qty", productList.get(j).getQty());
                    jItemObj.put("total", productList.get(j).getTotalPrice());
                    jItemObj.put("description", productList.get(j).getDescription());
                    jItemObj.put("tableId", productList.get(j).getCoverId());

                    List<Product> extraProductList = mDBHelper.getKOTExtraLineItems(AppConstants.tableID, productList.get(j).getKotLineId());

                    if (extraProductList.size() != 0)
                        jItemObj.put("relatedProductsArray", parseExtraProducts(extraProductList));

                    mArrayItems.put(jItemObj);

                    //check the extra items of current item
                }
                jo.put("products", mArrayItems);
                mArrayTokens.put(jo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mArrayTokens;
    }

    private JSONArray parseExtraProducts(List<Product> extraProductList) {
        JSONArray mArrayItems = new JSONArray();
        try {
            for (int j = 0; j < extraProductList.size(); j++) {
                JSONObject jItemObj = new JSONObject();
                jItemObj.put("productId", extraProductList.get(j).getProdId());
                jItemObj.put("productName", extraProductList.get(j).getProdName());
                jItemObj.put("productValue", extraProductList.get(j).getProdValue());
                jItemObj.put("categoryId", extraProductList.get(j).getCategoryId());
                jItemObj.put("productUOMId", extraProductList.get(j).getUomId());
                jItemObj.put("productUOMValue", extraProductList.get(j).getUomValue());
                jItemObj.put("sellingPrice", extraProductList.get(j).getSalePrice());
                jItemObj.put("qty", extraProductList.get(j).getQty());
                jItemObj.put("total", extraProductList.get(j).getTotalPrice());
                jItemObj.put("description", extraProductList.get(j).getDescription());
                jItemObj.put("tableId", extraProductList.get(j).getCoverId());

                mArrayItems.put(jItemObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mArrayItems;
    }

    public void parseKOTResponse(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {
                b.putInt("Type", AppConstants.POST_KOT_DATA_RESPONSE);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 1000) {
                if(json.has("appDownloadPath"))
                    mAppManager.saveAppPath(json.getString("appDownloadPath"));

                b.putInt("Type", AppConstants.UPDATE_APP);
                b.putString("OUTPUT", "");
            } else {
                b.putInt("Type", AppConstants.SERVER_ERROR);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }

        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    public void parseFeedbackResponse(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {
                b.putInt("Type", AppConstants.FEEDBACK_RESPONSE_RECEIVED);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 1000) {
                if(json.has("appDownloadPath"))
                    mAppManager.saveAppPath(json.getString("appDownloadPath"));

                b.putInt("Type", AppConstants.UPDATE_APP);
                b.putString("OUTPUT", "");
            } else {
                b.putInt("Type", AppConstants.SERVER_ERROR);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }

        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    public List<Long> parseTableStatus(String jsonStr, Handler mHandler) {
        List<Long> tableIdList = null;

        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        JSONArray jsonArray;
        int length = 0;
        long tableId = 0;

        AppConstants.isKotParsing = true;

        try {
            tableIdList = new ArrayList<Long>();
            json = new JSONObject(jsonStr);

            mDBHelper.updateDefaultTableStatus();

            AppLog.e("PARSER", "STARTED");

            if (json.getInt("responseCode") == 200) {

                AppLog.e("PARSER", json.toString());

                jsonArray = json.getJSONArray("tables");

                length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    tableId = obj.getLong("tableId");

                    mDBHelper.updateTableStatusAvailable(tableId);

                    tableIdList.add(tableId);
                }
            }else if (json.getInt("responseCode") == 101) {
                if(isTableVisible) {
                    AppLog.e("PARSER", "ALL TABLE IS EMPTY");
                    mDBHelper.deleteKOTLineItems(0);
                    mDBHelper.updateTableStatusAvailable(0);
                }
            }else if (json.getInt("responseCode") == 1000) {
                if(json.has("appDownloadPath"))
                    mAppManager.saveAppPath(json.getString("appDownloadPath"));

                b.putInt("Type", AppConstants.UPDATE_APP);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        } finally {

            AppLog.e("PARSER", "COMPLETED");
            /*if (length == tableIdList.size()) {
                b.putInt("Type", AppConstants.TABLE_STATUS_RECEIVED);
                b.putString("OUTPUT", "");
               // msg.setData(b);
               // mHandler.sendMessage(msg);
            }*/
        }

        return tableIdList;
    }

    /**
     * @param jsonStr
     * @param mHandler
     */
    public void parseOrgJson(String jsonStr, Handler mHandler) {

        Log.i("RESPONSE", jsonStr);
        JSONObject json;
        JSONArray orgArray;
        JSONArray roleArray;
        JSONArray roleAccessArray;
        JSONArray warehouseArray;

        Message msg = new Message();

        try {
            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {

                mDBHelper.deleteDhukanTables();

                mAppManager.setUserID(json.getLong("userId"));
                mAppManager.setUserBPID(json.getLong("businessPartnerId"));
                mAppManager.setClientID(json.getLong("clientId"));

                orgArray = json.getJSONArray("orgDetails");
                for (int i = 0; i < orgArray.length(); i++) {
                    JSONObject orgJson = (JSONObject) orgArray.get(i);
                    Organization mOrg = new Organization();
                    mOrg.setOrgId(orgJson.getLong("orgId"));
                    mOrg.setOrgName(orgJson.getString("orgName"));
                    mOrg.setIsDefault(orgJson.getString("isdefault"));

                    //checking org arabic name is available or not
                    if (orgJson.has("orgArabic"))
                        mOrg.setOrgArabicName(orgJson.getString("orgArabic"));
                    else
                        mOrg.setOrgArabicName("");

                    //checking org image is available or not
                    if (orgJson.has("orgImage")) {
                        String imagePath = FileUtils.storeImage(orgJson.getString("orgImage"), orgJson.getLong("orgId"), null);
                        mOrg.setOrgImage(imagePath);
                    } else
                        mOrg.setOrgImage("");

                    if(orgJson.has("orgbgImage")){
                        String imagePath = FileUtils.storeImage(orgJson.getString("orgbgImage"), orgJson.getLong("orgId"), null);
                        mOrg.setOrgImage(imagePath);
                    }else{
                        mOrg.setOrgImage("");
                    }

                    //checking org phone is available or not
                    if (orgJson.has("orgPhone"))
                        mOrg.setOrgPhone(orgJson.getString("orgPhone"));
                    else
                        mOrg.setOrgPhone("");

                    //checking org email is available or not
                    if (orgJson.has("orgEmail"))
                        mOrg.setOrgEmail(orgJson.getString("orgEmail"));
                    else
                        mOrg.setOrgEmail("");

                    //checking org address is available or not
                    if (orgJson.has("orgAddress"))
                        mOrg.setOrgAddress(orgJson.getString("orgAddress"));
                    else
                        mOrg.setOrgAddress("");

                    //checking org city is available or not
                    if (orgJson.has("orgCity"))
                        mOrg.setOrgCity(orgJson.getString("orgCity"));
                    else
                        mOrg.setOrgCity("");

                    //checking org country is available or not
                    if (orgJson.has("orgCountry"))
                        mOrg.setOrgCountry(orgJson.getString("orgCountry"));
                    else
                        mOrg.setOrgCountry("");

                    //checking org web address is available or not
                    if (orgJson.has("orgWebUrl"))
                        mOrg.setOrgWebUrl(orgJson.getString("orgWebUrl"));
                    else
                        mOrg.setOrgWebUrl("");

                    //checking org description is available or not
                    if (orgJson.has("orgDescription"))
                        mOrg.setOrgDescription(orgJson.getString("orgDescription"));
                    else
                        mOrg.setOrgDescription("");

                    if (orgJson.has("showPrice"))
                        mOrg.setShowPrice(orgJson.getString("showPrice"));
                    else
                        mOrg.setShowPrice("N");

                    mOrg.setClientId(mAppManager.getClientID());

                    mDBHelper.addOrganization(mOrg);
                }

                roleArray = json.getJSONArray("roleDetails");
                for (int i = 0; i < roleArray.length(); i++) {
                    JSONObject roleJson = (JSONObject) roleArray.get(i);
                    mDBHelper.addRole(roleJson.getLong("roleId"), roleJson.getString("roleName"), roleJson.getString("isdefault"));
                }

                roleAccessArray = json.getJSONArray("roleAccessDetails");
                for (int i = 0; i < roleAccessArray.length(); i++) {
                    JSONObject roleAccessJson = (JSONObject) roleAccessArray.get(i);
                    mDBHelper.addRoleAccess(mAppManager.getClientID(), roleAccessJson.getLong("orgId"), roleAccessJson.getLong("roleId"));
                }

                warehouseArray = json.getJSONArray("warehouseDetails");
                for (int i = 0; i < warehouseArray.length(); i++) {
                    JSONObject warehouseJson = (JSONObject) warehouseArray.get(i);
                    mDBHelper.addWarehouse(mAppManager.getClientID(), warehouseJson.getLong("orgId"), warehouseJson.getLong("warehouseId"), warehouseJson.getString("warehouseName"), warehouseJson.getString("isdefault"));
                }

                b.putInt("Type", AppConstants.ORGANIZATION_DATA_RECEIVED);
                b.putString("OUTPUT", "");

            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 101) {
                b.putInt("Type", AppConstants.LOGIN_FAILURE);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 1000) {
                if(json.has("appDownloadPath"))
                    mAppManager.saveAppPath(json.getString("appDownloadPath"));

                b.putInt("Type", AppConstants.UPDATE_APP);
                b.putString("OUTPUT", "");
            } else {
                b.putInt("Type", AppConstants.NO_DATA_RECEIVED);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }

        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    /**
     * @param jsonStr
     * @param mHandler
     */
    public void parseLoginJson(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {
                if (json.getString("isSalesRep").equalsIgnoreCase("Yes"))
                    mAppManager.setSalesRep(true);
                else
                    mAppManager.setSalesRep(false);

                b.putInt("Type", AppConstants.LOGIN_SUCCESS);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 1000) {
                if(json.has("appDownloadPath"))
                    mAppManager.saveAppPath(json.getString("appDownloadPath"));

                b.putInt("Type", AppConstants.UPDATE_APP);
                b.putString("OUTPUT", "");
            } else {
                b.putInt("Type", AppConstants.LOGIN_FAILURE);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }

        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    /**
     * @param jsonStr
     * @param mHandler
     */
    public void parseCommonJson(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {

                BPartner bPartner = new BPartner();
                bPartner.setBpNumber(json.getLong("businessPartnerId"));
                bPartner.setBpName(json.getString("customerName"));
                bPartner.setBpId(json.getLong("businessPartnerId"));
                bPartner.setBpPriceListId(json.getLong("pricelistId"));
                bPartner.setBpEmail("");
                bPartner.setBpNumber(0);

                mAppManager.saveCustomerData(bPartner);
                //mAppManager.saveCashCustomerData(json.getString("customerName"),json.getLong("businessPartnerId"));
                mAppManager.saveCommonData(json.getLong("costElementId"), json.getLong("currencyId"), json.getLong("cashBookId"), json.getLong("periodId"), json.getLong("paymentTermId"), json.getLong("adTableId"), json.getLong("accountSchemaId"), json.getLong("pricelistId"), json.getString("currencyCode"));

                b.putInt("Type", AppConstants.COMMON_DATA_RECEIVED);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 1000) {
                if(json.has("appDownloadPath"))
                    mAppManager.saveAppPath(json.getString("appDownloadPath"));

                b.putInt("Type", AppConstants.UPDATE_APP);
                b.putString("OUTPUT", "");
            } else {
                b.putInt("Type", AppConstants.NO_DATA_RECEIVED);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }

        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    public void parseTables(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        JSONArray jsonArray;
        List<Tables> tableList = null;
        int length = 0;
        try {

            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {

                tableList = new ArrayList<Tables>();
                jsonArray = json.getJSONArray("tables");

                length = jsonArray.length();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    Tables tables = new Tables();
                    tables.setClientId(mAppManager.getClientID());
                    tables.setOrgId(mAppManager.getOrgID());
                    tables.setTableId(obj.getLong("tablesId"));
                    tables.setTableName(obj.getString("tablesName"));
                    tables.setOrderAvailable("N");
                    tables.setIsCoversLevel(obj.getString("isCoversLevel"));
                    tables.setTableGroupId(obj.getLong("tableGroupId"));

                    mDBHelper.addTables(tables);

                    tableList.add(tables);
                }
            } else if (json.getInt("responseCode") == 1000) {
                if(json.has("appDownloadPath"))
                    mAppManager.saveAppPath(json.getString("appDownloadPath"));

                b.putInt("Type", AppConstants.UPDATE_APP);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        } finally {

            if (length == tableList.size()) {
                b.putInt("Type", AppConstants.TABLES_RECEIVED);
                b.putString("OUTPUT", "");
            }
            msg.setData(b);
            mHandler.sendMessage(msg);
        }
    }

    public void parseTerminals(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        JSONArray jsonArray;
        List<Terminals> terminalsList = null;
        int length = 0;
        try {

            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {

                terminalsList = new ArrayList<Terminals>();
                jsonArray = json.getJSONArray("terminals");

                length = jsonArray.length();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    Terminals terminals = new Terminals();
                    terminals.setClientId(mAppManager.getClientID());
                    terminals.setOrgId(mAppManager.getOrgID());
                    terminals.setTerminalId(obj.getLong("terminalId"));
                    terminals.setTerminalName(obj.getString("terminalName"));
                    terminals.setTerminalIP(obj.getString("terminalIP"));

                    mDBHelper.addTerminals(terminals);

                    terminalsList.add(terminals);
                }
            } else if (json.getInt("responseCode") == 1000) {
                if(json.has("appDownloadPath"))
                    mAppManager.saveAppPath(json.getString("appDownloadPath"));

                b.putInt("Type", AppConstants.UPDATE_APP);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        } finally {

            if (length == terminalsList.size()) {
                b.putInt("Type", AppConstants.TERMINALS_RECEIVED);
                b.putString("OUTPUT", "");
            }
            msg.setData(b);
            mHandler.sendMessage(msg);
        }
    }

    //Updated By Vijay(14-07-2017)
    public void parseTableKOTDataResponse(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        JSONArray jsonTokenArray, jsonProductArray;
        List<String> tokenList = null;
        int length = 0;
        Product mProduct = null;
        long kotNumber = 0, invoiceNumber = 0, terminalId = 0;
        String isPrinted = "Y", notes = "";
        int qty = 0;

        try {

            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {

                tokenList = new ArrayList<String>();
                jsonTokenArray = json.getJSONArray("tokens");

                length = jsonTokenArray.length();
                for (int i = 0; i < length; i++) {

                    JSONObject tokenObj = (JSONObject) jsonTokenArray.get(i);
                    jsonProductArray = tokenObj.getJSONArray("products");

                    kotNumber = tokenObj.getLong("KOTNumber");
                    //invoiceNumber = tokenObj.getLong("invoiceNumber");
                    isPrinted = tokenObj.getString("isPrinted");

                    terminalId = tokenObj.getLong("terminalId");

                    if(tokenObj.has("coversCount"))
                        AppConstants.noOfCovers = tokenObj.getInt("coversCount");

                    //add kot header
                    //long tablesId, long kotNumber, long invoiceNumber, long terminalId, String coversDetails
                    mDBHelper.addKOTHeader(AppConstants.tableID, kotNumber, tokenObj.getLong("invoiceNumber"), terminalId, tokenObj.getString("coversDetails"));
                    for (int j = 0; j < jsonProductArray.length(); j++) {

                        JSONObject prodObj = (JSONObject) jsonProductArray.get(j);

                        long kotRefLineId = prodObj.getLong("KotLineID");

                        mProduct = mDBHelper.getProduct(mAppManager.getClientID(), mAppManager.getOrgID(), prodObj.getLong("productId"));
                        qty = prodObj.getInt("qty");
                        notes = prodObj.getString("description");
                        mProduct.setTerminalId(terminalId);

                        mDBHelper.addKOTLineItems(prodObj.getLong("KotLineID"), AppConstants.tableID, kotNumber, mProduct, qty, notes, "Y", 0, "N", prodObj.getString("isDeleted"), prodObj.getLong("tableId"));
                        mDBHelper.updateTableStatusAvailable(prodObj.getLong("tableId"));
                        if(prodObj.has("relatedProductsArray")){

                            JSONArray jsonRelatedProductArray = prodObj.getJSONArray("relatedProductsArray");

                            for (int k = 0; k < jsonRelatedProductArray.length(); k++) {

                                JSONObject relatedProdObj = (JSONObject) jsonRelatedProductArray.get(k);

                                mProduct = mDBHelper.getProduct(mAppManager.getClientID(), mAppManager.getOrgID(), relatedProdObj.getLong("productId"));
                                qty = relatedProdObj.getInt("qty");
                                notes = relatedProdObj.getString("description");
                                mProduct.setTerminalId(terminalId);

                                mDBHelper.addKOTLineItems(relatedProdObj.getLong("KotLineID"), AppConstants.tableID, kotNumber, mProduct, qty, notes, "Y", kotRefLineId, "Y", prodObj.getString("isDeleted"), relatedProdObj.getLong("tableId"));
                                mDBHelper.updateTableStatusAvailable(relatedProdObj.getLong("tableId"));
                            }
                        }
                    }

                    tokenList.add(String.valueOf(kotNumber));

                    b.putInt("Type", AppConstants.TABLE_KOT_DETAILS_RECEIVED);
                    b.putString("OUTPUT", "");
                }
            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 101) {
                mDBHelper.deleteKOTLineItems(AppConstants.tableID);
                b.putInt("Type", AppConstants.TABLE_KOT_DETAILS_RECEIVED);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 1000) {
                if(json.has("appDownloadPath"))
                    mAppManager.saveAppPath(json.getString("appDownloadPath"));

                b.putInt("Type", AppConstants.UPDATE_APP);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        } finally {
            msg.setData(b);
            mHandler.sendMessage(msg);
        }
    }

    /**
     * @param jsonStr
     * @return
     */
    public void parseCategorysJson(String jsonStr, Handler mHandler) {
        Runnable myThread = new ParseCategoryThread(jsonStr, mHandler);
        new Thread(myThread).start();
    }

    /**
     * @param jsonStr
     * @return
     */
    public void parseProductJson(String jsonStr, Handler mHandler) {
        Runnable myThread = new ParseProductThread(jsonStr, mHandler);
        new Thread(myThread).start();
    }

    private void addRelatedProducts(JSONArray jsonArray, long parentProductId) {
        try {

            JSONArray jsonProductArray = jsonArray;

            for (int i = 0; i < jsonProductArray.length(); i++) {

                JSONObject obj = (JSONObject) jsonProductArray.get(i);
                Product product = new Product();
                long categoryId = obj.getLong("categoryId");
                long productId = obj.getLong("productId");
                product.setParentId(parentProductId);
                product.setProdId(obj.getLong("productId"));
                product.setProdName(obj.getString("productName"));
                product.setProdValue(obj.getString("productValue"));
                product.setUomId(obj.getLong("productUOMId"));
                product.setUomValue(obj.getString("productUOMValue"));
                product.setSalePrice(Double.parseDouble(obj.getString("sellingPrice")));
                product.setCostPrice(Double.parseDouble(obj.getString("costprice")));
                product.setTerminalId(obj.getLong("terminalId"));

                String imgPath = "";

                //load image to sdcard and store the path to db
                if (obj.has("productImage")) {
                    imgPath = FileUtils.storeImage(obj.getString("productImage"), obj.getLong("productId"), null);
                    product.setProdImage("");
                } else {
                    // Retrieve the image from the res folder
                    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                            R.drawable.no_product);
                    String imagePath = FileUtils.storeImage("", obj.getLong("productId"), bitmap);
                    product.setProdImage("");
                }

                if (obj.has("productArabicName")) {
                    product.setProdArabicName(obj.getString("productArabicName"));
                } else {
                    product.setProdArabicName("");
                }

                if (obj.has("description")) {
                    product.setDescription(obj.getString("description"));
                } else {
                    product.setDescription("");
                }

                if (obj.has("showDigitalMenu")) {
                    product.setShowDigitalMenu(obj.getString("showDigitalMenu"));
                } else {
                    product.setShowDigitalMenu("Y");
                }

                if (obj.has("productVideoPath")) {
                    product.setProdVideoPath(obj.getString("productVideoPath"));
                } else {
                    product.setProdVideoPath("N");
                }

                if (obj.has("calories")) {
                    product.setCalories(obj.getString("calories"));
                } else {
                    product.setCalories("");
                }

                if (obj.has("preparationTime")) {
                    product.setPreparationTime(obj.getString("preparationTime"));
                } else {
                    product.setPreparationTime("");
                }

                if (obj.has("caloriesArabic")) {
                    product.setProdArabicCalories(obj.getString("caloriesArabic"));
                } else {
                    product.setProdArabicCalories("");
                }

                if (obj.has("preparationTimeArabic")) {
                    product.setProdArabicPreTime(obj.getString("preparationTimeArabic"));
                } else {
                    product.setProdArabicPreTime("");
                }

                if (obj.has("descriptionArabic")) {
                    product.setProdArabicDescription(obj.getString("descriptionArabic"));
                } else {
                    product.setProdArabicDescription("");
                }

                if (!imgPath.equalsIgnoreCase(""))
                    mDBHelper.addProductImage(mAppManager.getClientID(), mAppManager.getOrgID(), productId, imgPath, "image");

                product.setClientId(mAppManager.getClientID());
                product.setOrgId(mAppManager.getOrgID());

                mDBHelper.addProduct(categoryId, product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addProductsNotes(JSONArray jsonArray, long productId) {
        try {
            JSONArray jsonNotesArray = jsonArray;

            for (int i = 0; i < jsonNotesArray.length(); i++) {

                JSONObject obj = (JSONObject) jsonNotesArray.get(i);
                Notes notes = new Notes();

                notes.setProdcutId(productId);
                notes.setNotesId(obj.getLong("productnotesId"));
                notes.setNotesName(obj.getString("notesName"));
                notes.setClientId(mAppManager.getClientID());
                notes.setOrgId(mAppManager.getOrgID());

                mDBHelper.addNotes(notes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ParseCategoryThread implements Runnable {

        String mJsonStr;
        Handler mHandler;

        Message msg = new Message();
        JSONObject json;
        JSONArray jsonArray;
        List<Category> categoryList = null;
        int length = 0;

        public ParseCategoryThread(String jsonStr, Handler handler) {
            // store parameter for later user
            mJsonStr = jsonStr;
            mHandler = handler;
        }

        @Override
        public void run() {
            try {

                json = new JSONObject(mJsonStr);
                if (json.getInt("responseCode") == 200) {

                    categoryList = new ArrayList<Category>();
                    jsonArray = json.getJSONArray("category");

                    length = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        Category category = new Category();
                        category.setCategoryId(obj.getLong("categoryId"));
                        category.setCategoryName(obj.getString("categoryName"));
                        category.setCategoryValue(obj.getString("categoryValue"));

                        //load image to sdcard and store the path to db
                        if (obj.has("categoryImage")) {
                            //String imagePath = FileUtils.storeImage(obj.getString("categoryImage"), obj.getLong("categoryId"), null);
                            category.setCategoryImage(obj.getString("categoryImage"));
                        } else {
                            // Retrieve the image from the res folder
                            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                    R.drawable.no_product);
                            String imagePath = FileUtils.storeImage("", obj.getLong("categoryId"), bitmap);
                            category.setCategoryImage(imagePath);
                        }

                        if (obj.has("showDigitalMenu")) {
                            category.setShowDigitalMenu(obj.getString("showDigitalMenu"));
                        } else {
                            category.setShowDigitalMenu("Y");
                        }

                        if (obj.has("categoryNameArabic")) {
                            category.setCategoryArabicName(obj.getString("categoryNameArabic"));
                        } else {
                            category.setCategoryArabicName("");
                        }

                        category.setClientId(mAppManager.getClientID());
                        category.setOrgId(mAppManager.getOrgID());

                        //add category
                        mDBHelper.addCategory(category);

                        categoryList.add(category);
                    }
                } else if (json.getInt("responseCode") == 301) {
                    b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                    b.putString("OUTPUT", "");
                } else if (json.getInt("responseCode") == 1000) {
                    if(json.has("appDownloadPath"))
                        mAppManager.saveAppPath(json.getString("appDownloadPath"));

                    b.putInt("Type", AppConstants.UPDATE_APP);
                    b.putString("OUTPUT", "");
                }
            } catch (Exception e) {
                e.printStackTrace();
                b.putInt("Type", AppConstants.SERVER_ERROR);
                b.putString("OUTPUT", "");
            } finally {

                if (length == categoryList.size()) {
                    b.putInt("Type", AppConstants.CATEGORY_RECEIVED);
                    b.putString("OUTPUT", "");
                }
                msg.setData(b);
                mHandler.sendMessage(msg);
            }
        }
    }

    public class ParseProductThread implements Runnable {

        Message msg = new Message();
        List<Product> productList = null;
        JSONObject json;
        JSONArray jsonArray;
        long categoryId, productId;
        int length = 0;
        String mJsonStr;
        Handler mHandler;
        String imgPath;

        public ParseProductThread(String jsonStr, Handler handler) {
            // store parameter for later user
            mJsonStr = jsonStr;
            mHandler = handler;
        }

        @Override
        public void run() {
            try {
                json = new JSONObject(mJsonStr);
                if (json.getInt("responseCode") == 200) {

                    productList = new ArrayList<Product>();
                    jsonArray = json.getJSONArray("products");
                    length = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        Product product = new Product();
                        categoryId = obj.getLong("categoryId");
                        productId = obj.getLong("productId");
                        product.setParentId(0);
                        product.setProdId(obj.getLong("productId"));
                        product.setProdName(obj.getString("productName"));
                        product.setProdValue(obj.getString("productValue"));
                        product.setUomId(obj.getLong("productUOMId"));
                        product.setUomValue(obj.getString("productUOMValue"));
                        product.setSalePrice(Double.parseDouble(obj.getString("sellingPrice")));
                        product.setCostPrice(Double.parseDouble(obj.getString("costprice")));
                        product.setTerminalId(obj.getLong("terminalId"));

                        imgPath = "";

                        if(obj.has("productMultiImage")){
                            JSONArray jsonProdArray = obj.getJSONArray("productMultiImage");
                            for (int j = 0; j < jsonProdArray.length(); j++) {
                                JSONObject prodObj = (JSONObject) jsonProdArray.get(0);
                                if(prodObj.has("productImage")){
                                    product.setProdImage(prodObj.getString("productImage"));
                                }else{
                                    // Retrieve the image from the res folder
                                    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                            R.drawable.no_product);
                                    String imagePath = FileUtils.storeImage("",obj.getLong("productId"),bitmap);
                                    product.setProdImage(imagePath);
                                }
                            }
                        }else{
                            // Retrieve the image from the res folder
                            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                    R.drawable.no_product);
                            String imagePath = FileUtils.storeImage("",obj.getLong("productId"),bitmap);
                            product.setProdImage(imagePath);
                        }

                        /*//load image to sdcard and store the path to db
                        if (obj.has("productImage")) {
                            imgPath = FileUtils.storeImage(obj.getString("productImage"), obj.getLong("productId"), null);
                            product.setProdImage(imgPath);
                        } else {
                            // Retrieve the image from the res folder
                            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                    R.drawable.no_product);
                            String imagePath = FileUtils.storeImage("", obj.getLong("productId"), bitmap);
                            product.setProdImage(imagePath);
                        }*/

                        if (obj.has("productArabicName")) {
                            product.setProdArabicName(obj.getString("productArabicName"));
                        } else {
                            product.setProdArabicName("");
                        }

                        if (obj.has("description")) {
                            product.setDescription(obj.getString("description"));
                        } else {
                            product.setDescription("");
                        }

                        if (obj.has("showDigitalMenu")) {
                            product.setShowDigitalMenu(obj.getString("showDigitalMenu"));
                        } else {
                            product.setShowDigitalMenu("Y");
                        }

                        if (obj.has("productVideoPath")) {
                            product.setProdVideoPath(obj.getString("productVideoPath"));
                        } else {
                            product.setProdVideoPath("N");
                        }

                        if (obj.has("calories")) {
                            product.setCalories(obj.getString("calories"));
                        } else {
                            product.setCalories("");
                        }

                        if (obj.has("preparationTime")) {
                            product.setPreparationTime(obj.getString("preparationTime"));
                        } else {
                            product.setPreparationTime("");
                        }

                        if (obj.has("caloriesArabic")) {
                            product.setProdArabicCalories(obj.getString("caloriesArabic"));
                        } else {
                            product.setProdArabicCalories("");
                        }

                        if (obj.has("preparationTimeArabic")) {
                            product.setProdArabicPreTime(obj.getString("preparationTimeArabic"));
                        } else {
                            product.setProdArabicPreTime("");
                        }

                        if (obj.has("descriptionArabic")) {
                            product.setProdArabicDescription(obj.getString("descriptionArabic"));
                        } else {
                            product.setProdArabicDescription("");
                        }

                        if (obj.has("prTime")) {
                            product.setPreparationTime(obj.getString("prTime"));
                        } else {
                            product.setPreparationTime("");
                        }

                        if (!imgPath.equalsIgnoreCase(""))
                            mDBHelper.addProductImage(mAppManager.getClientID(), mAppManager.getOrgID(), productId, imgPath, "image");

                        product.setClientId(mAppManager.getClientID());
                        product.setOrgId(mAppManager.getOrgID());

                        mDBHelper.addProduct(categoryId, product);
                        productList.add(product);

                        if (obj.has("productMultiImage")) {

                            JSONArray jsonImageArray = obj.getJSONArray("productMultiImage");
                            for (int j = 0; j < jsonImageArray.length(); j++) {

                                JSONObject imgObj = (JSONObject) jsonImageArray.get(j);
                                //String imagePath = FileUtils.storeMultiImage(imgObj.getString("productImage"), productId, null, j);
                                if(imgObj.has("productImage")){
                                    mDBHelper.addProductImage(mAppManager.getClientID(), mAppManager.getOrgID(), productId, imgObj.getString("productImage"), "image");
                                }

                            }
                        }

                        if (obj.has("relatedProductsArray")) {
                            addRelatedProducts(obj.getJSONArray("relatedProductsArray"), productId);
                        }

                        if (obj.has("productsNotesArray")) {
                            addProductsNotes(obj.getJSONArray("productsNotesArray"), productId);
                        }
                    }
                    b.putInt("Type", AppConstants.PRODUCTS_RECEIVED);
                    b.putString("OUTPUT", "");
                } else if (json.getInt("responseCode") == 301) {
                    b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                    b.putString("OUTPUT", "");
                } else if (json.getInt("responseCode") == 101) {
                    b.putInt("Type", AppConstants.SERVER_ERROR);
                    b.putString("OUTPUT", "");
                } else if (json.getInt("responseCode") == 1000) {
                    if(json.has("appDownloadPath"))
                        mAppManager.saveAppPath(json.getString("appDownloadPath"));

                    b.putInt("Type", AppConstants.UPDATE_APP);
                    b.putString("OUTPUT", "");
                }
            } catch (Exception e) {
                e.printStackTrace();
                b.putInt("Type", AppConstants.SERVER_ERROR);
                b.putString("OUTPUT", "");
            } finally {
                //if (length == productList.size()) {

                //}

                msg.setData(b);
                mHandler.sendMessage(msg);
            }
        }
    }

    public void parseAppUpdateAvailable(String jsonStr, Handler mHandler) {
        Log.i("RESPONSE", jsonStr);
        Message msg = new Message();
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            if (json.getInt("responseCode") == 200) {
                b.putInt("Type", AppConstants.NO_UPDATE_AVAILABLE);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 301) {
                b.putInt("Type", AppConstants.DEVICE_NOT_REGISTERED);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 700) {
                b.putInt("Type", AppConstants.NETWORK_ERROR);
                b.putString("OUTPUT", "");
            } else if (json.getInt("responseCode") == 1000) {
                if(json.has("appDownloadPath"))
                    mAppManager.saveAppPath(json.getString("appDownloadPath"));

                b.putInt("Type", AppConstants.UPDATE_APP);
                b.putString("OUTPUT", "");
            } else {
                b.putInt("Type", AppConstants.SERVER_ERROR);
                b.putString("OUTPUT", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            b.putInt("Type", AppConstants.SERVER_ERROR);
            b.putString("OUTPUT", "");
        }

        msg.setData(b);
        mHandler.sendMessage(msg);
    }
}
