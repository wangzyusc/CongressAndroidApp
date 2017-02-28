package edu.wangzhiyusc.hw9test;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangz on 2016/11/22.
 */

public class FragmentLegislator extends Fragment {

    private View viewContent;
    private TextView sideIndexItem;

    private TabHost tabHost;

    private JSONArray results = null;

    private LegByStatePersonData[] legdataByState;
    private LegByNamePersonData[] legdataByHouse;
    private LegByNamePersonData[] legdataBySenate;

    private Map<String, Integer> stateIndexList;
    private Map<String, Integer> houseIndexList;
    private Map<String, Integer> senateIndexList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if(viewContent == null) {
            sideIndexItem = (TextView) inflater.inflate(R.layout.side_index_item, container, false);

            viewContent = inflater.inflate(R.layout.fragment_legislator, container, false);

            TabHost host = (TabHost) viewContent.findViewById(R.id.tabhost_leg);
            host.setup();

            String[] titles = getResources().getStringArray(R.array.leg_tab_titles);

            TabHost.TabSpec spec = host.newTabSpec("BY STATE");
            spec.setContent(R.id.tab_leg_state);
            spec.setIndicator("BY STATE");
            host.addTab(spec);

            spec = host.newTabSpec("HOUSE");
            spec.setContent(R.id.tab_leg_house);
            spec.setIndicator("HOUSE");
            host.addTab(spec);

            spec = host.newTabSpec("SENATE");
            spec.setContent(R.id.tab_leg_senate);
            spec.setIndicator("SENATE");
            host.addTab(spec);

            jsonHandler jsonH = new jsonHandler();
            jsonH.execute();

        }

        return viewContent;
    }

    private void getIndexList(){
        stateIndexList = new LinkedHashMap<String, Integer>();
        houseIndexList = new LinkedHashMap<String, Integer>();
        senateIndexList = new LinkedHashMap<String, Integer>();

        for(int i = 0; i < legdataByState.length; i++){
            String index = ((String) legdataByState[i].get("state_name")).substring(0, 1);
            if(stateIndexList.get(index) == null){
                stateIndexList.put(index, i);
            }
        }

        for(int i=0; i < legdataByHouse.length; i++){
            String index = ((String) legdataByHouse[i].get("last_name")).substring(0, 1);
            if(houseIndexList.get(index) == null){
                houseIndexList.put(index, i);
            }
        }

        for(int i=0; i < legdataBySenate.length; i++){
            String index = ((String) legdataBySenate[i].get("last_name")).substring(0, 1);
            if(senateIndexList.get(index) == null){
                senateIndexList.put(index, i);
            }
        }
        Log.i("FragmentLegislator", "Have got index list");
    }

    private void displayIndex(){
        Log.i("FragmentLegislator", "Should display side index list");
        final LinearLayout indexLayoutState = (LinearLayout) viewContent.findViewById(R.id.side_index_leg_state);
        final LinearLayout indexLayoutHouse = (LinearLayout) viewContent.findViewById(R.id.side_index_leg_house);
        final LinearLayout indexLayoutSenate = (LinearLayout) viewContent.findViewById(R.id.side_index_leg_senate);
        final ListView listViewState = (ListView)viewContent.findViewById(R.id.list_leg_state);
        final ListView listViewHouse = (ListView)viewContent.findViewById(R.id.list_leg_house);
        final ListView listViewSenate = (ListView)viewContent.findViewById(R.id.list_leg_senate);
        TextView textView;
        List<String> indexlistState = new ArrayList<String>(stateIndexList.keySet());
        List<String> indexlistHouse = new ArrayList<String>(houseIndexList.keySet());
        List<String> indexlistSenate = new ArrayList<String>(senateIndexList.keySet());
        for(String index:indexlistState){
            textView = (TextView) ((LayoutInflater)(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
                    .inflate(R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView selectedIndex = (TextView) v;
                    listViewState.setSelection(stateIndexList.get(selectedIndex.getText()));
                }
            });
            indexLayoutState.addView(textView);
        }
        for(String index:indexlistHouse){
            textView = (TextView) ((LayoutInflater)(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
                    .inflate(R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView selectedIndex = (TextView) v;
                    listViewHouse.setSelection(houseIndexList.get(selectedIndex.getText()));
                }
            });
            indexLayoutHouse.addView(textView);
        }
        for(String index:indexlistSenate){
            textView = (TextView) ((LayoutInflater)(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
                    .inflate(R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView selectedIndex = (TextView) v;
                    listViewSenate.setSelection(senateIndexList.get(selectedIndex.getText()));
                }
            });
            indexLayoutSenate.addView(textView);
        }
    }

    private class jsonHandler extends AsyncTask<String, String, JSONObject> {

        private static final String serverLink = "http://app29882-env.us-west-2.elasticbeanstalk.com/?type=";
        private static final String action_leg = "legislators";
        private static final String action_bill = "bills";
        private static final String action_com = "committees";
        private static final String tag_result = "results";
        private static final String tag_bioguide_id = "bioguide_id";
        private static final String tag_chamber = "chamber";
        private static final String tag_first_name = "first_name";

        private static final String TAG = "jsonHandler_Legislator";

        //JSONArray results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute...");
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            Log.i(TAG, "doInBackground...");
            JSONfetcher fetcher = new JSONfetcher(serverLink, action_leg);
            return fetcher.getJSONobj();
        }
        @Override
        protected void onPostExecute(JSONObject json) {

            Log.i(TAG, "onPostExecute...");
            try {
                // Getting JSON Array
                results = json.getJSONArray(tag_result);
                Log.i(TAG, "The length of JSONArray is " + results.length());
                int housecount = 0, senatecount = 0;
                for(int i=0;i<results.length();i++){
                    JSONObject person = results.getJSONObject(i);
                    if(person.getString("chamber").equals("house")) housecount++;
                    if(person.getString("chamber").equals("senate")) senatecount++;
                }
                legdataByState = new LegByStatePersonData[results.length()];
                legdataByHouse = new LegByNamePersonData[housecount];
                legdataBySenate = new LegByNamePersonData[senatecount];
                housecount = 0;
                senatecount = 0;
                for(int i=0; i < results.length(); i++){
                    legdataByState[i] = new LegByStatePersonData();
                    LegByStatePersonData personData = legdataByState[i];
                    JSONObject person = results.getJSONObject(i);
                    personData.put("bioguide_id", person.getString("bioguide_id"));
                    personData.put("first_name", person.getString("first_name"));
                    personData.put("last_name", person.getString("last_name"));
                    personData.put("state_name", person.getString("state_name"));
                    personData.put("chamber", person.getString("chamber"));
                    personData.put("party", person.getString("party"));
                    personData.put("state", person.getString("state"));
                    personData.put("title", person.getString("title"));
                    personData.put("phone", person.getString("phone"));
                    personData.put("oc_email", person.getString("oc_email"));
                    personData.put("term_start", person.getString("term_start"));
                    personData.put("term_end", person.getString("term_end"));
                    if(person.has("fax") == true){
                        personData.put("fax", person.getString("fax"));
                    }
                    personData.put("office", person.getString("office"));
                    personData.put("birthday", person.getString("birthday"));
                    personData.put("website", person.getString("website"));
                    String facebook_id = "null";
                    if(person.has("facebook_id") == true){
                        facebook_id = person.getString("facebook_id");
                    }
                    personData.put("facebook_id", facebook_id);
                    if(person.has("twitter_id") == true){
                        personData.put("twitter_id", person.getString("twitter_id"));
                    }
                    else {
                        personData.put("twitter_id", "null");
                    }
                    if(person.getString("chamber").equals("house")){
                        personData.put("district", person.getString("district"));
                    }
                    //Leg By house and senate
                    if(person.getString("chamber").equals("house")){
                        legdataByHouse[housecount] = new LegByNamePersonData();
                        legdataByHouse[housecount].put("bioguide_id", person.getString("bioguide_id"));
                        legdataByHouse[housecount].put("first_name", person.getString("first_name"));
                        legdataByHouse[housecount].put("last_name", person.getString("last_name"));
                        legdataByHouse[housecount].put("state_name", person.getString("state_name"));
                        legdataByHouse[housecount].put("chamber", person.getString("chamber"));
                        legdataByHouse[housecount].put("party", person.getString("party"));
                        legdataByHouse[housecount].put("state", person.getString("state"));
                        legdataByHouse[housecount].put("title", person.getString("title"));
                        legdataByHouse[housecount].put("phone", person.getString("phone"));
                        legdataByHouse[housecount].put("oc_email", person.getString("oc_email"));
                        legdataByHouse[housecount].put("term_start", person.getString("term_start"));
                        legdataByHouse[housecount].put("term_end", person.getString("term_end"));
                        if(person.has("fax") == true){
                            legdataByHouse[housecount].put("fax", person.getString("fax"));
                        }
                        legdataByHouse[housecount].put("office", person.getString("office"));
                        legdataByHouse[housecount].put("birthday", person.getString("birthday"));
                        legdataByHouse[housecount].put("website", person.getString("website"));
                        legdataByHouse[housecount].put("district", person.getString("district"));
                        if(person.has("facebook_id") == true){
                            legdataByHouse[housecount].put("facebook_id", person.getString("facebook_id"));
                        }
                        if(person.has("twitter_id") == true){
                            legdataByHouse[housecount].put("twitter_id", person.getString("twitter_id"));
                        }
                        housecount++;
                    }
                    else if(person.getString("chamber").equals("senate")){
                        legdataBySenate[senatecount] = new LegByNamePersonData();
                        legdataBySenate[senatecount].put("bioguide_id", person.getString("bioguide_id"));
                        legdataBySenate[senatecount].put("first_name", person.getString("first_name"));
                        legdataBySenate[senatecount].put("last_name", person.getString("last_name"));
                        legdataBySenate[senatecount].put("state_name", person.getString("state_name"));
                        legdataBySenate[senatecount].put("chamber", person.getString("chamber"));
                        legdataBySenate[senatecount].put("party", person.getString("party"));
                        legdataBySenate[senatecount].put("state", person.getString("state"));
                        legdataBySenate[senatecount].put("title", person.getString("title"));
                        legdataBySenate[senatecount].put("phone", person.getString("phone"));
                        legdataBySenate[senatecount].put("oc_email", person.getString("oc_email"));
                        legdataBySenate[senatecount].put("term_start", person.getString("term_start"));
                        legdataBySenate[senatecount].put("term_end", person.getString("term_end"));
                        if(person.has("fax") == true){
                            legdataBySenate[senatecount].put("fax", person.getString("fax"));
                        }
                        legdataBySenate[senatecount].put("office", person.getString("office"));
                        legdataBySenate[senatecount].put("birthday", person.getString("birthday"));
                        legdataBySenate[senatecount].put("website", person.getString("website"));
                        if(person.has("facebook_id") == true){
                            legdataBySenate[senatecount].put("facebook_id", person.getString("facebook_id"));
                        }
                        if(person.has("twitter_id") == true){
                            legdataBySenate[senatecount].put("twitter_id", person.getString("twitter_id"));
                        }
                        senatecount++;
                    }
                }
                Arrays.sort(legdataByState);
                Arrays.sort(legdataByHouse);
                Arrays.sort(legdataBySenate);
                /*List<HashMap<String, Object>> bystate_data = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> byhouse_data = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> bysenate_data = new ArrayList<HashMap<String, Object>>();*/
                List<LegByStatePersonData> bystate_data = new ArrayList<LegByStatePersonData>();
                List<LegByNamePersonData> byhouse_data = new ArrayList<LegByNamePersonData>();
                List<LegByNamePersonData> bysenate_data = new ArrayList<LegByNamePersonData>();

                String url_pre = "https://theunitedstates.io/images/congress/original/";
                String url_suf = ".jpg";
                List<String> bystate_urls = new ArrayList<String>();
                List<String> byhouse_urls = new ArrayList<String>();
                List<String> bysenate_urls = new ArrayList<String>();

                for (int i = 0; i < legdataByState.length; i++) {
                    LegByStatePersonData map = new LegByStatePersonData();
                    map.put("list_item_leg_name", legdataByState[i].get("last_name")
                            + ", " + legdataByState[i].get("first_name"));
                    if(legdataByState[i].get("chamber").equals("house")){
                        map.put("list_item_leg_info", "(" + legdataByState[i].get("party") + ")"
                                + legdataByState[i].get("state_name") + " - District " + legdataByState[i].get("district"));
                        //byhouse_data.add(map);
                        //byhouse_urls.add(url_pre + legdataByState[i].get("bioguide_id") + url_suf);
                    }
                    if(legdataByState[i].get("chamber").equals("senate")){
                        map.put("list_item_leg_info", "(" + legdataByState[i].get("party") + ")"
                                + legdataByState[i].get("state_name") + " - District 0");
                        //bysenate_data.add(map);
                        //bysenate_urls.add(url_pre + legdataByState[i].get("bioguide_id") + url_suf);
                    }
                    bystate_data.add(map);
                    bystate_urls.add(url_pre + legdataByState[i].get("bioguide_id") + url_suf);
                }
                for (int i = 0; i < legdataByHouse.length; i++) {
                    LegByNamePersonData map = new LegByNamePersonData();
                    map.put("list_item_leg_name", legdataByHouse[i].get("last_name")
                            + ", " + legdataByHouse[i].get("first_name"));
                    map.put("list_item_leg_info", "(" + legdataByHouse[i].get("party") + ")"
                            + legdataByHouse[i].get("state_name") + " - District " + legdataByHouse[i].get("district"));
                    byhouse_data.add(map);
                    byhouse_urls.add(url_pre + legdataByHouse[i].get("bioguide_id") + url_suf);
                }
                for (int i = 0; i < legdataBySenate.length; i++) {
                    LegByNamePersonData map = new LegByNamePersonData();
                    map.put("list_item_leg_name", legdataBySenate[i].get("last_name")
                            + ", " + legdataBySenate[i].get("first_name"));
                    map.put("list_item_leg_info", "(" + legdataBySenate[i].get("party") + ")"
                            + legdataBySenate[i].get("state_name") + " - District " + legdataBySenate[i].get("district"));
                    bysenate_data.add(map);
                    bysenate_urls.add(url_pre + legdataBySenate[i].get("bioguide_id") + url_suf);
                }
                String[] from = new String[]{"list_item_leg_name", "list_item_leg_info"};
                int[] to = new int[]{R.id.list_item_leg_name, R.id.list_item_leg_info};

                //Set ListView Adapter
                ListItemLegAdapter mStateAdapter = new ListItemLegAdapter(getActivity().getBaseContext(),
                        bystate_data, R.layout.list_item_leg, from, to, bystate_urls);
                ListItemLegAdapter mHouseAdapter = new ListItemLegAdapter(getActivity().getBaseContext(),
                        byhouse_data, R.layout.list_item_leg, from, to, byhouse_urls);
                ListItemLegAdapter mSenateAdapter = new ListItemLegAdapter(getActivity().getBaseContext(),
                        bysenate_data, R.layout.list_item_leg, from, to, bysenate_urls);

                ListView bystate_list = (ListView)viewContent.findViewById(R.id.list_leg_state);
                bystate_list.setAdapter(mStateAdapter);
                ListView byhouse_list = (ListView)viewContent.findViewById(R.id.list_leg_house);
                byhouse_list.setAdapter(mHouseAdapter);
                ListView bysenate_list = (ListView)viewContent.findViewById(R.id.list_leg_senate);
                bysenate_list.setAdapter(mSenateAdapter);

                //Set ListView Item On Click Listener
                bystate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        Fragment legDetail = new DetailLegFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("bioguide_id", (String) legdataByState[position].get("bioguide_id"));
                        bundle.putString("title", (String) legdataByState[position].get("title"));
                        bundle.putString("first_name", (String) legdataByState[position].get("first_name"));
                        bundle.putString("last_name", (String) legdataByState[position].get("last_name"));
                        bundle.putString("chamber", (String) legdataByState[position].get("chamber"));
                        bundle.putString("party", (String) legdataByState[position].get("party"));
                        bundle.putString("oc_email", (String) legdataByState[position].get("oc_email"));
                        bundle.putString("phone", (String) legdataByState[position].get("phone"));
                        bundle.putString("term_start", (String) legdataByState[position].get("term_start"));
                        bundle.putString("term_end", (String) legdataByState[position].get("term_end"));
                        bundle.putString("office", (String) legdataByState[position].get("office"));
                        bundle.putString("state", (String) legdataByState[position].get("state"));
                        bundle.putString("fax", (String) legdataByState[position].get("fax"));
                        bundle.putString("birthday", (String) legdataByState[position].get("birthday"));
                        bundle.putString("facebook_id", (String) legdataByState[position].get("facebook_id"));
                        bundle.putString("twitter_id", (String) legdataByState[position].get("twitter_id"));
                        bundle.putString("website", (String) legdataByState[position].get("website"));
                        legDetail.setArguments(bundle);
                        transaction.replace(R.id.id_content_frame, legDetail);
                        transaction.commit();
                    }
                });
                
                byhouse_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        Fragment legDetail = new DetailLegFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("bioguide_id", (String) legdataByHouse[position].get("bioguide_id"));
                        bundle.putString("title", (String) legdataByHouse[position].get("title"));
                        bundle.putString("first_name", (String) legdataByHouse[position].get("first_name"));
                        bundle.putString("last_name", (String) legdataByHouse[position].get("last_name"));
                        bundle.putString("chamber", (String) legdataByHouse[position].get("chamber"));
                        bundle.putString("party", (String) legdataByHouse[position].get("party"));
                        bundle.putString("oc_email", (String) legdataByHouse[position].get("oc_email"));
                        bundle.putString("phone", (String) legdataByHouse[position].get("phone"));
                        bundle.putString("term_start", (String) legdataByHouse[position].get("term_start"));
                        bundle.putString("term_end", (String) legdataByHouse[position].get("term_end"));
                        bundle.putString("office", (String) legdataByHouse[position].get("office"));
                        bundle.putString("state", (String) legdataByHouse[position].get("state"));
                        bundle.putString("fax", (String) legdataByHouse[position].get("fax"));
                        bundle.putString("birthday", (String) legdataByHouse[position].get("birthday"));
                        bundle.putString("facebook_id", (String) legdataByHouse[position].get("facebook_id"));
                        bundle.putString("twitter_id", (String) legdataByHouse[position].get("twitter_id"));
                        bundle.putString("website", (String) legdataByHouse[position].get("website"));
                        legDetail.setArguments(bundle);
                        transaction.replace(R.id.id_content_frame, legDetail);
                        transaction.commit();
                    }
                });

                bysenate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        Fragment legDetail = new DetailLegFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("bioguide_id", (String) legdataBySenate[position].get("bioguide_id"));
                        bundle.putString("title", (String) legdataBySenate[position].get("title"));
                        bundle.putString("first_name", (String) legdataBySenate[position].get("first_name"));
                        bundle.putString("last_name", (String) legdataBySenate[position].get("last_name"));
                        bundle.putString("chamber", (String) legdataBySenate[position].get("chamber"));
                        bundle.putString("party", (String) legdataBySenate[position].get("party"));
                        bundle.putString("oc_email", (String) legdataBySenate[position].get("oc_email"));
                        bundle.putString("phone", (String) legdataBySenate[position].get("phone"));
                        bundle.putString("term_start", (String) legdataBySenate[position].get("term_start"));
                        bundle.putString("term_end", (String) legdataBySenate[position].get("term_end"));
                        bundle.putString("office", (String) legdataBySenate[position].get("office"));
                        bundle.putString("state", (String) legdataBySenate[position].get("state"));
                        bundle.putString("fax", (String) legdataBySenate[position].get("fax"));
                        bundle.putString("birthday", (String) legdataBySenate[position].get("birthday"));
                        bundle.putString("facebook_id", (String) legdataBySenate[position].get("facebook_id"));
                        bundle.putString("twitter_id", (String) legdataBySenate[position].get("twitter_id"));
                        bundle.putString("website", (String) legdataBySenate[position].get("website"));
                        legDetail.setArguments(bundle);
                        transaction.replace(R.id.id_content_frame, legDetail);
                        transaction.commit();
                    }
                });

                //Set Side Index List
                getIndexList();
                displayIndex();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

}
