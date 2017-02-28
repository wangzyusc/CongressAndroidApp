package edu.wangzhiyusc.hw9test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.os.AsyncTask;
import android.util.Log;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by wangz on 2016/11/25.
 */

public class JSONfetcher {

    private String murl;
    private String maction;
    private InputStream myStream;
    //private JSONObject jObj;
    private String json;

    public JSONfetcher(String url, String action){
        this.murl = url;
        this.maction = action;
        myStream = null;
        //jObj = null;
        json = "";
        myStream = getStream(murl + maction);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    myStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            myStream.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
    }

    public JSONObject getJSONobj(){
        JSONObject jObj = null;
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
    }

    private InputStream getStream(String link) {
        try {
            URL url = new URL(link);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(1000);
            return urlConnection.getInputStream();
        } catch (Exception e) {
            Log.e("GetStream Error", e.toString());
            return null;
        }
    }

    public String getJSONStr(){
        return json;
    }

}




