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
import android.widget.TabHost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangz on 2016/11/22.
 */

public class FragmentCommittee extends Fragment {

    private View viewContent;
    private JSONArray results;

    private ComData[] comdataByHouse;
    private ComData[] comdataBySenate;
    private ComData[] comdataByJoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if(viewContent == null){
            viewContent = inflater.inflate(R.layout.fragment_committee, container, false);
            TabHost host = (TabHost) viewContent.findViewById(R.id.tabhost_com);
            host.setup();

            String[] titles = getResources().getStringArray(R.array.com_tab_titles);

            TabHost.TabSpec spec = host.newTabSpec("HOUSE");
            spec.setContent(R.id.tab_com_house);
            spec.setIndicator("HOUSE");
            host.addTab(spec);

            spec = host.newTabSpec("SENATE");
            spec.setContent(R.id.tab_com_senate);
            spec.setIndicator("SEANTE");
            host.addTab(spec);

            spec = host.newTabSpec("JOINT");
            spec.setContent(R.id.tab_com_joint);
            spec.setIndicator("JOINT");
            host.addTab(spec);

            jsonHandler jsonH = new jsonHandler();
            jsonH.execute();
        }

        return viewContent;
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

        private static final String TAG = "jsonHandler_Committees";

        //JSONArray results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute...");
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            Log.i(TAG, "doInBackground...");
            JSONfetcher fetcher = new JSONfetcher(serverLink, action_com);
            return fetcher.getJSONobj();
        }
        @Override
        protected void onPostExecute(JSONObject json) {

            Log.i(TAG, "onPostExecute...");
            try {
                // Getting JSON Array
                results = json.getJSONArray(tag_result);
                Log.i(TAG, "The length of JSONArray is " + results.length());
                int housecount = 0, senatecount = 0, jointcount = 0;
                for(int i=0; i<results.length(); i++){
                    JSONObject com = results.getJSONObject(i);
                    if(com.getString("chamber").equals("house")) housecount++;
                    if(com.getString("chamber").equals("senate")) senatecount++;
                    if(com.getString("chamber").equals("joint")) jointcount++;
                }
                comdataByHouse = new ComData[housecount];
                comdataBySenate = new ComData[senatecount];
                comdataByJoint = new ComData[jointcount];
                housecount = 0;
                senatecount = 0;
                jointcount = 0;
                for(int i=0; i < results.length(); i++){
                    JSONObject com = results.getJSONObject(i);
                    if(com.getString("chamber").equals("house")){
                        comdataByHouse[housecount] = new ComData();
                        comdataByHouse[housecount].put("committee_id", com.getString("committee_id"));
                        comdataByHouse[housecount].put("name", com.getString("name"));
                        comdataByHouse[housecount].put("chamber", "House");
                        if(com.has("parent_committee_id")){
                            comdataByHouse[housecount].put("parent_committee_id", com.getString("parent_committee_id"));
                        }
                        else{
                            comdataByHouse[housecount].put("parent_committee_id", "N.A.");
                        }
                        if(com.has("phone")){
                            comdataByHouse[housecount].put("phone", com.getString("phone"));
                        }
                        else{
                            comdataByHouse[housecount].put("phone", "N.A.");
                        }
                        if(com.has("office")){
                            comdataByHouse[housecount].put("office", com.getString("office"));
                        }
                        else{
                            comdataByHouse[housecount].put("office", "N.A.");
                        }
                        housecount++;
                    }
                    else if(com.getString("chamber").equals("senate")){
                        comdataBySenate[senatecount] = new ComData();
                        if(com.has("committee_id")){
                            String id = com.getString("committee_id");
                            comdataBySenate[senatecount].put("committee_id", id);
                        }
                        else {
                            comdataBySenate[senatecount].put("committee_id", "N.A");
                        }
                        comdataBySenate[senatecount].put("name", com.getString("name"));
                        comdataBySenate[senatecount].put("chamber", "Senate");
                        if(com.has("parent_committee_id")){
                            comdataBySenate[senatecount].put("parent_committee_id", com.getString("parent_committee_id"));
                        }
                        else{
                            comdataBySenate[senatecount].put("parent_committee_id", "N.A.");
                        }
                        if(com.has("phone")){
                            comdataBySenate[senatecount].put("phone", com.getString("phone"));
                        }
                        else{
                            comdataBySenate[senatecount].put("phone", "N.A.");
                        }
                        if(com.has("office")){
                            comdataBySenate[senatecount].put("office", com.getString("office"));
                        }
                        else{
                            comdataBySenate[senatecount].put("office", "N.A.");
                        }
                        senatecount++;
                    }
                    else if(com.getString("chamber").equals("joint")){
                        comdataByJoint[jointcount] = new ComData();
                        comdataByJoint[jointcount].put("committee_id", com.getString("committee_id"));
                        comdataByJoint[jointcount].put("name", com.getString("name"));
                        comdataByJoint[jointcount].put("chamber", "Joint");
                        if(com.has("parent_committee_id")){
                            comdataByJoint[jointcount].put("parent_committee_id", com.getString("parent_committee_id"));
                        }
                        else{
                            comdataByJoint[jointcount].put("parent_committee_id", "N.A.");
                        }
                        if(com.has("phone")){
                            comdataByJoint[jointcount].put("phone", com.getString("phone"));
                        }
                        else{
                            comdataByJoint[jointcount].put("phone", "N.A.");
                        }
                        if(com.has("office")){
                            comdataByJoint[jointcount].put("office", com.getString("office"));
                        }
                        else{
                            comdataByJoint[jointcount].put("office", "N.A.");
                        }
                        jointcount++;
                    }
                }
                Arrays.sort(comdataByJoint);
                Arrays.sort(comdataByHouse);
                Arrays.sort(comdataBySenate);

                List<ComData> byjoint_data = new ArrayList<ComData>();
                List<ComData> byhouse_data = new ArrayList<ComData>();
                List<ComData> bysenate_data = new ArrayList<ComData>();

                for (int i = 0; i < comdataByJoint.length; i++) {
                    ComData map = new ComData();
                    map.put("com_id", comdataByJoint[i].get("committee_id"));
                    map.put("com_name", comdataByJoint[i].get("name"));
                    map.put("com_chamber", comdataByJoint[i].get("chamber"));
                    byjoint_data.add(map);
                }
                for (int i = 0; i < comdataByHouse.length; i++) {
                    ComData map = new ComData();
                    map.put("com_id", comdataByHouse[i].get("committee_id"));
                    map.put("com_name", comdataByHouse[i].get("name"));
                    map.put("com_chamber", comdataByHouse[i].get("chamber"));
                    byhouse_data.add(map);
                }
                for (int i = 0; i < comdataBySenate.length; i++) {
                    ComData map = new ComData();
                    map.put("com_id", comdataBySenate[i].get("committee_id"));
                    map.put("com_name", comdataBySenate[i].get("name"));
                    map.put("com_chamber", comdataBySenate[i].get("chamber"));
                    bysenate_data.add(map);
                }
                String[] from = new String[]{"com_id", "com_name", "com_chamber"};
                int[] to = new int[]{R.id.list_item_com_id, R.id.list_item_com_name, R.id.list_item_com_chamber};

                //Set ListView Adapter
                ListItemComAdapter mJointAdapter = new ListItemComAdapter(getActivity().getBaseContext(),
                        byjoint_data, R.layout.list_item_com, from, to);
                ListItemComAdapter mHouseAdapter = new ListItemComAdapter(getActivity().getBaseContext(),
                        byhouse_data, R.layout.list_item_com, from, to);
                ListItemComAdapter mSenateAdapter = new ListItemComAdapter(getActivity().getBaseContext(),
                        bysenate_data, R.layout.list_item_com, from, to);

                ListView byjoint_list = (ListView)viewContent.findViewById(R.id.list_com_joint);
                byjoint_list.setAdapter(mJointAdapter);
                ListView byhouse_list = (ListView)viewContent.findViewById(R.id.list_com_house);
                byhouse_list.setAdapter(mHouseAdapter);
                ListView bysenate_list = (ListView)viewContent.findViewById(R.id.list_com_senate);
                bysenate_list.setAdapter(mSenateAdapter);

                //Set ListView Item On Click Listener
                byjoint_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        Fragment comDetail = new DetailComFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("committee_id", (String) comdataByJoint[position].get("committee_id"));
                        bundle.putString("name", (String) comdataByJoint[position].get("name"));
                        bundle.putString("chamber", "Joint");
                        bundle.putString("parent_committee_id", (String) comdataByJoint[position].get("parent_committee_id"));
                        bundle.putString("phone", (String) comdataByJoint[position].get("phone"));
                        bundle.putString("office", (String) comdataByJoint[position].get("office"));
                        comDetail.setArguments(bundle);
                        transaction.replace(R.id.id_content_frame, comDetail);
                        transaction.commit();
                    }
                });

                byhouse_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        Fragment comDetail = new DetailComFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("committee_id", (String) comdataByHouse[position].get("committee_id"));
                        bundle.putString("name", (String) comdataByHouse[position].get("name"));
                        bundle.putString("chamber", "House");
                        bundle.putString("parent_committee_id", (String) comdataByHouse[position].get("parent_committee_id"));
                        bundle.putString("phone", (String) comdataByHouse[position].get("phone"));
                        bundle.putString("office", (String) comdataByHouse[position].get("office"));
                        comDetail.setArguments(bundle);
                        transaction.replace(R.id.id_content_frame, comDetail);
                        transaction.commit();
                    }
                });

                bysenate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        Fragment comDetail = new DetailComFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("committee_id", (String) comdataBySenate[position].get("committee_id"));
                        bundle.putString("name", (String) comdataBySenate[position].get("name"));
                        bundle.putString("chamber", "Senate");
                        bundle.putString("parent_committee_id", (String) comdataBySenate[position].get("parent_committee_id"));
                        bundle.putString("phone", (String) comdataBySenate[position].get("phone"));
                        bundle.putString("office", (String) comdataBySenate[position].get("office"));
                        comDetail.setArguments(bundle);
                        transaction.replace(R.id.id_content_frame, comDetail);
                        transaction.commit();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
