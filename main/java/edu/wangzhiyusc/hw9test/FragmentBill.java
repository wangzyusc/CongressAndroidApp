package edu.wangzhiyusc.hw9test;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangz on 2016/11/22.
 */

public class FragmentBill extends Fragment {

    private View viewContent;
    private JSONArray results;

    private BillData[] billDataActive;
    private BillData[] billDataNew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if(viewContent == null){

            viewContent = inflater.inflate(R.layout.fragment_bill, container, false);

            TabHost host = (TabHost) viewContent.findViewById(R.id.tabhost_bill);
            host.setup();

            String[] titles = getResources().getStringArray(R.array.bill_tab_titles);

            TabHost.TabSpec spec = host.newTabSpec("ACTIVE BILLS");
            spec.setContent(R.id.tab_bill_active);
            spec.setIndicator("ACTIVE BILLS");
            host.addTab(spec);

            spec = host.newTabSpec("NEW BILLS");
            spec.setContent(R.id.tab_bill_new);
            spec.setIndicator("NEW BILLS");
            host.addTab(spec);

            activeBillJSONHandler activeJSONH = new activeBillJSONHandler();
            activeJSONH.execute();

            newBillJSONHandler newJSONH = new newBillJSONHandler();
            newJSONH.execute();
        }


        return viewContent;
    }

    private class activeBillJSONHandler extends AsyncTask<String, String, JSONObject> {

        private static final String serverLink = "http://app29882-env.us-west-2.elasticbeanstalk.com/?type=";
        private static final String action_leg = "legislators";
        private static final String active_bill = "bills&active=true";
        private static final String action_com = "committees";
        private static final String tag_result = "results";
        private static final String tag_bioguide_id = "bioguide_id";
        private static final String tag_chamber = "chamber";
        private static final String tag_first_name = "first_name";

        private static final String TAG = "jsonHandler_Bill";

        //JSONArray results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute...");
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            Log.i(TAG, "doInBackground...");
            JSONfetcher jParser = new JSONfetcher(serverLink, active_bill);
            return jParser.getJSONobj();
        }
        @Override
        protected void onPostExecute(JSONObject json) {

            Log.i(TAG, "onPostExecute...");
            try {
                // Getting JSON Array
                results = json.getJSONArray(tag_result);
                Log.i(TAG, "The length of JSONArray is " + results.length());
                //Sort by introduced time
                billDataActive = new BillData[results.length()];
                for(int i=0; i<results.length();i++){
                    billDataActive[i] = new BillData();
                    JSONObject bill = results.getJSONObject(i);
                    billDataActive[i].put("bill_id", bill.getString("bill_id"));
                    billDataActive[i].put("bill_type", bill.getString("bill_type"));
                    billDataActive[i].put("chamber", bill.getString("chamber"));
                    billDataActive[i].put("introduced_on", bill.getString("introduced_on"));
                    billDataActive[i].put("official_title", bill.getString("official_title"));
                    if(bill.has("short_title")) billDataActive[i].put("short_title", bill.getString("short_title"));
                    JSONObject sponsor = bill.getJSONObject("sponsor");
                    billDataActive[i].put("sponsor", sponsor.getString("title") + " " + sponsor.getString("last_name")
                            + ", " + sponsor.getString("first_name"));
                    JSONObject urls = bill.getJSONObject("urls");
                    billDataActive[i].put("congress_url", urls.getString("congress"));
                    if(bill.has("last_version")){
                        JSONObject lastversion = bill.getJSONObject("last_version");
                        billDataActive[i].put("version_status", lastversion.getString("version_name"));
                        billDataActive[i].put("bill_url", lastversion.getJSONObject("urls").getString("pdf"));
                    }
                    else{
                        billDataActive[i].put("version_status", "N.A.");
                        billDataActive[i].put("bill_url", "N.A.");
                    }
                    if(bill.has("history")){
                        billDataActive[i].put("version", ((bill.getJSONObject("history").getBoolean("active"))? "Active" : "New"));
                    }
                    else{
                        billDataActive[i].put("version", "N.A.");
                    }
                }
                Arrays.sort(billDataActive);

                List<HashMap<String, Object>> activebill_data = new ArrayList<HashMap<String, Object>>();

                for (int i = 0; i < billDataActive.length ; i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();

                    map.put("bill_id", billDataActive[i].get("bill_id"));
                    String title = "N.A.";
                    if (billDataActive[i].containsKey("short_title") == false){
                        title = (String) billDataActive[i].get("short_title");
                        //Log.i("Short_title", (String) billDataActive[i].get("short_title"));
                    }
                    else{
                        title = (String) billDataActive[i].get("official_title");
                        //Log.i("Official_title", (String) billDataActive[i].get("official_title"));
                    }
                    map.put("bill_tt", title);
                    map.put("bill_date", billDataActive[i].get("introduced_on"));
                    //Log.i("ActiveBill", (String) map.get("bill_tt"));
                    activebill_data.add(map);
                }
                String[] from = new String[]{"bill_id", "bill_tt", "bill_date"};
                int[] to = new int[]{R.id.list_item_bill_id, R.id.list_item_bill_title, R.id.list_item_bill_date};

                ListItemBillAdapter mActiveBillAdapter = new ListItemBillAdapter(getActivity().getBaseContext(),
                        activebill_data, R.layout.list_item_bill, from, to);
                ListView activebill_list = (ListView)viewContent.findViewById(R.id.list_bill_active);
                activebill_list.setAdapter(mActiveBillAdapter);

                activebill_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        Fragment billDetail = new DetailBillFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("bill_id", (String) billDataActive[position].get("bill_id"));
                        bundle.putString("official_title", (String) billDataActive[position].get("official_title"));
                        bundle.putString("short_title", (String) billDataActive[position].get("short_title"));
                        bundle.putString("bill_type", (String) billDataActive[position].get("bill_type"));
                        bundle.putString("chamber", (String) billDataActive[position].get("chamber"));
                        bundle.putString("sponsor", (String) billDataActive[position].get("sponsor"));
                        bundle.putString("status", (String) billDataActive[position].get("status"));
                        bundle.putString("introduced_on", (String) billDataActive[position].get("introduced_on"));
                        bundle.putString("congress_url", (String) billDataActive[position].get("congress_url"));
                        bundle.putString("version_status", (String) billDataActive[position].get("version_status"));
                        bundle.putString("bill_url", (String) billDataActive[position].get("bill_url"));
                        billDetail.setArguments(bundle);
                        transaction.replace(R.id.id_content_frame, billDetail);
                        transaction.commit();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class newBillJSONHandler extends AsyncTask<String, String, JSONObject> {

        private static final String serverLink = "http://app29882-env.us-west-2.elasticbeanstalk.com/?type=";
        private static final String action_leg = "legislators";
        private static final String new_bill = "bills&active=false";
        private static final String action_com = "committees";
        private static final String tag_result = "results";
        private static final String tag_bioguide_id = "bioguide_id";
        private static final String tag_chamber = "chamber";
        private static final String tag_first_name = "first_name";

        private static final String TAG = "jsonHandler_Bill";

        //JSONArray results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute...");
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            Log.i(TAG, "doInBackground...");
            JSONfetcher jParser = new JSONfetcher(serverLink, new_bill);
            return jParser.getJSONobj();
        }
        @Override
        protected void onPostExecute(JSONObject json) {

            Log.i(TAG, "onPostExecute...");
            try {
                // Getting JSON Array
                results = json.getJSONArray(tag_result);
                Log.i(TAG, "The length of JSONArray is " + results.length());
                //Sort by introduced time
                billDataNew = new BillData[results.length()];
                for(int i=0; i<results.length();i++){
                    billDataNew[i] = new BillData();
                    JSONObject bill = results.getJSONObject(i);
                    billDataNew[i].put("bill_id", bill.getString("bill_id"));
                    billDataNew[i].put("bill_type", bill.getString("bill_type"));
                    billDataNew[i].put("chamber", bill.getString("chamber"));
                    billDataNew[i].put("introduced_on", bill.getString("introduced_on"));
                    billDataNew[i].put("official_title", bill.getString("official_title"));
                    //billDataNew[i].put("official_title", "This should be a title.");
                    if(bill.has("short_title")) billDataNew[i].put("short_title", bill.getString("short_title"));
                    JSONObject sponsor = bill.getJSONObject("sponsor");
                    billDataNew[i].put("sponsor", sponsor.getString("title") + " " + sponsor.getString("last_name")
                            + ", " + sponsor.getString("first_name"));
                    JSONObject urls = bill.getJSONObject("urls");
                    billDataNew[i].put("congress_url", urls.getString("congress"));
                    if(bill.has("last_version")){
                        JSONObject lastversion = bill.getJSONObject("last_version");
                        billDataNew[i].put("version_status", lastversion.getString("version_name"));
                        billDataNew[i].put("bill_url", lastversion.getJSONObject("urls").getString("pdf"));
                    }
                    else{
                        billDataNew[i].put("version_status", "N.A.");
                        billDataNew[i].put("bill_url", "N.A.");
                    }

                }
                Arrays.sort(billDataNew);

                List<HashMap<String, Object>> newbill_data = new ArrayList<HashMap<String, Object>>();

                for (int i = 0; i < billDataNew.length ; i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();

                    map.put("bill_id", billDataNew[i].get("bill_id"));
                    if (billDataNew[i].containsKey("short_title") == false){
                        map.put("bill_tt", billDataNew[i].get("short_title"));
                    }
                    else{
                        map.put("bill_tt", billDataNew[i].get("official_title"));
                    }
                    map.put("bill_date", billDataNew[i].get("introduced_on"));

                    newbill_data.add(map);
                }
                String[] from = new String[]{"bill_id", "bill_tt", "bill_date"};
                int[] to = new int[]{R.id.list_item_bill_id, R.id.list_item_bill_title, R.id.list_item_bill_date};

                ListItemBillAdapter mNewBillAdapter = new ListItemBillAdapter(getActivity().getBaseContext(),
                        newbill_data, R.layout.list_item_bill, from, to);
                ListView newbill_list = (ListView)viewContent.findViewById(R.id.list_bill_new);
                newbill_list.setAdapter(mNewBillAdapter);

                newbill_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        Fragment billDetail = new DetailBillFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("bill_id", (String) billDataActive[position].get("bill_id"));
                        bundle.putString("official_title", (String) billDataActive[position].get("official_title"));
                        bundle.putString("short_title", (String) billDataActive[position].get("short_title"));
                        bundle.putString("bill_type", (String) billDataActive[position].get("bill_type"));
                        bundle.putString("chamber", (String) billDataActive[position].get("chamber"));
                        bundle.putString("sponsor", (String) billDataActive[position].get("sponsor"));
                        bundle.putString("status", (String) billDataActive[position].get("status"));
                        bundle.putString("introduced_on", (String) billDataActive[position].get("introduced_on"));
                        bundle.putString("congress_url", (String) billDataActive[position].get("congress_url"));
                        bundle.putString("version_status", (String) billDataActive[position].get("version_status"));
                        bundle.putString("bill_url", (String) billDataActive[position].get("bill_url"));
                        billDetail.setArguments(bundle);
                        transaction.replace(R.id.id_content_frame, billDetail);
                        transaction.commit();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
