package com.zearoconsulting.smartmenu.domain.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.zearoconsulting.smartmenu.AndroidApplication;
import com.zearoconsulting.smartmenu.data.AppDataManager;
import com.zearoconsulting.smartmenu.data.DBHelper;
import com.zearoconsulting.smartmenu.domain.parser.JSONParser;
import com.zearoconsulting.smartmenu.utils.AppConstants;
import com.zearoconsulting.smartmenu.utils.NetworkUtil;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class TableStatusService extends IntentService {

    private static final String TAG = "TableStatusService";
    private AppDataManager mAppManager;
    private DBHelper mDBHelper;
    private JSONParser mParser;
    private Timer t = new Timer();

    public TableStatusService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            System.out.println("Service started .... \n");

            mAppManager = AndroidApplication.getInstance().getAppManager();
            mDBHelper = AndroidApplication.getInstance().getDBHelper();
            mParser = new JSONParser(AndroidApplication.getAppContext(), mAppManager, mDBHelper);

            AppConstants.URL = AppConstants.kURLHttp+mAppManager.getServerAddress()+":"+mAppManager.getServerPort()+AppConstants.kURLServiceName+ AppConstants.kURLMethodApi;

            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    // just call the handler every 5 Seconds
                    try {
                        if (!NetworkUtil.getConnectivityStatusString().equals(AppConstants.NETWORK_FAILURE)) {

                            String results = getResponse();

                            if (!results.equalsIgnoreCase("")) {
                                //mParser.parseTableStatus(results, null);
                            }
                        }else{
                            Log.e(TAG,"NO INTERNET CONNECTION");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 100, 5000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getResponse() {
        String retSrc = "";
        StringBuilder result;

        JSONObject errJson = new JSONObject();

        try {
            URL url = new URL(AppConstants.URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();

            //GET_KOT_HEADER_LINE_DATA
            JSONObject mJsonObj = mParser.getParams(AppConstants.GET_TABLE_STATUS);
            os.write(mJsonObj.toString().getBytes());
            os.flush();

            System.out.println("Output from Server .... \n");

            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            retSrc = result.toString();

            Log.i(TAG,retSrc);

            System.out.println("Polling Service Data: " + retSrc);

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            retSrc = errJson.toString();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retSrc;
    }
}
