package com.zearoconsulting.smartmenu.utils;

import android.os.Environment;

/**
 * Created by saravanan on 24-05-2016.
 * AppConstants for dhukan pos
 */
public class AppConstants {

    public static final String kURLHttp="http://";
    public static final String kURLServiceName="/CCLWebService/CCLPOSResource";
    public static final String kURLMethodTest="/test";
    public static final String kURLMethodApi="/api";

    /**POS PRODUCTION API URL*/
    //public static final String URL = "http://empower-erp.com:8080/ZearoPOSWebService/POSResource/api";

    /**POS UAT API URL*/
    //public static final String URL = "http://demo.empower-erp.com:8080/ZearoPOSWebService/POSResource/api";

    public static String URL = "http://demo.empower-erp.com:8080/CCLWebService/CCLPOSResource/api";

    /** call collect organization key */
    public static final int GET_ORGANIZATION_DATA = 1;

    /** received organization key */
    public static final int ORGANIZATION_DATA_RECEIVED = 2;

    /** call authenticate key */
    public static final int CALL_AUTHENTICATE = 3;

    /** authenticate success key */
    public static final int LOGIN_SUCCESS = 4;

    /** authenticate failure key */
    public static final int LOGIN_FAILURE = 5;

    /** call get cash customer data key */
    public static final int GET_CASH_CUSTOMER_DATA = 6;

    /** received get cash customer data key */
    public static final int CASH_CUSTOMER_DATA_RECEIVED = 7;

    /** call business partners key */
    public static final int GET_BPARTNERS = 8;

    /** response customer data key */
    public static final int CUSTOMER_DATA_RECEIVED = 9;

    /** response common data key */
    public static final int COMMON_DATA_RECEIVED = 10;

    /** get pos number key */
    public static final int GET_POS_NUMBER = 11;

    /** response pos number key */
    public static final int POS_NUMBER_RECEIVED = 12;

    /** get category key */
    public static final int GET_CATEGORY = 13;

    /** response category key */
    public static final int CATEGORY_RECEIVED = 14;

    /** call get products key */
    public static final int GET_PRODUCTS = 15;

    /** get all products key*/
    public static final int GET_ALL_PRODUCTS = 16;

    /** response product received key */
    public static final int PRODUCTS_RECEIVED = 17;

    /** call get product price key */
    public static final int GET_PRODUCT_PRICE = 18;

    /** response product price received key */
    public static final int PRODUCT_PRICE_RECEIVED = 19;

    /** call get tables key */
    public static final int GET_TABLES = 20;

    /** response tables received key */
    public static final int TABLES_RECEIVED = 21;

    /** call get terminals key */
    public static final int GET_TERMINALS = 22;

    /** response terminals received key */
    public static final int TERMINALS_RECEIVED = 23;

    /** call get kot header key */
    public static final int POST_KOT_DATA = 24;

    /** response kot header received key */
    public static final int POST_KOT_DATA_RESPONSE = 25;

    /** call get kot table line items key */
    public static final int GET_TABLE_LINE_ITEMS = 26;

    /** get kot table status received key */
    public static final int GET_TABLE_STATUS = 27;

    /** response kot table status received key */
    public static final int TABLE_STATUS_RECEIVED = 28;

    public static final int GET_TABLE_KOT_DETAILS = 29;

    public static final int TABLE_KOT_DETAILS_RECEIVED = 30;

    /** call release work order key */
    public static final int CALL_RELEASE_POS_ORDER = 31;

    /** response release work order success key */
    public static final int POS_ORDER_RELEASED_SUCCESS = 32;

    /** response release work order failure key */
    public static final int POS_ORDER_RELEASED_FAILURE = 33;

    /** call data loaded key */
    public static final int POST_FEEDBACK_DATA = 34;

    /** call data loaded key */
    public static final int FEEDBACK_RESPONSE_RECEIVED = 35;

    /** call data loaded key */
    public static final int DATA_LOADED = 36;

    /** response no data received key */
    public static final int NO_DATA_RECEIVED = 37;

    /** response server error key */
    public static final int SERVER_ERROR = 38;

    /** response network error key */
    public static final int NETWORK_ERROR = 39;

    public static final int DEVICE_NOT_REGISTERED = 40;

    /** notify response failure key */
    public static final String NETWORK_FAILURE = "Not connected to Internet";

    //public static long posID = 1004173;

    /** initializing posid */
    public static long posID = 0;

    /** initializing tableID */
    public static long tableID = 0;

    /** initializing noOfCovers */
    public static int noOfCovers = 0;

    /** initialized default currency code format */
    public static String currencyCode = "QR";

    /**initiated product fragment its visible status*/
    public static boolean isFistTimeVisible = false;

    /**initiated order posted default status */
    public static boolean isOrderPosted = false;

    /**initiated order printed default status */
    public static boolean isKotParsing = false;

    /**initiated device identity */
    public static boolean isMobile = false;

    /**initiated table visible default */
    public static boolean isTableVisible = false;

    public static boolean isPasswordValidated = false;
}
