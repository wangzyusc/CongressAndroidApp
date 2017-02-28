package edu.wangzhiyusc.hw9test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangz on 2016/11/30.
 */

public class DetailLegFragment extends Fragment {
    private View viewContent;
    private String bioguide_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        viewContent = inflater.inflate(R.layout.page_detail_leg, container, false);
        /*TextView tv = (TextView) viewContent.findViewById(R.id.page_detail_leg_id);
        tv.setText(getArguments().getString("bioguide_id"));*///tv.setText(bioguide_id);
        String fb_img_url = "http://cs-server.usc.edu:45678/hw/hw8/images/f.png";
        String tw_img_url = "http://cs-server.usc.edu:45678/hw/hw8/images/t.png";
        String wb_img_url = "http://cs-server.usc.edu:45678/hw/hw8/images/w.png";
        String de_img_url = "http://cs-server.usc.edu:45678/hw/hw8/images/d.png";
        String re_img_url = "http://cs-server.usc.edu:45678/hw/hw8/images/r.png";
        final ImageView fv_logo = (ImageView) viewContent.findViewById(R.id.detail_leg_favorite);
        ImageView fb_logo = (ImageView) viewContent.findViewById(R.id.detail_leg_facebook);
        ImageView tw_logo = (ImageView) viewContent.findViewById(R.id.detail_leg_flicker);
        ImageView wb_logo = (ImageView) viewContent.findViewById(R.id.detail_leg_website);
        fv_logo.setImageResource(R.drawable.unselected_star);
        Picasso.with(getContext()).load(fb_img_url).resize(100,100).centerCrop().into(fb_logo);
        Picasso.with(getContext()).load(tw_img_url).resize(100,100).centerCrop().into(tw_logo);
        Picasso.with(getContext()).load(wb_img_url).resize(100,100).centerCrop().into(wb_logo);
        final String facebook_id = getArguments().getString("facebook_id", "null");
        final String twitter_id = getArguments().getString("twitter_id", "null");
        final String website = getArguments().getString("website", "null");
        fv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fv_logo.setImageResource(R.drawable.selected_star);
            }
        });
        fb_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(facebook_id == null){
                    String output = "facebook_id == null";
                    Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
                }
                else if(facebook_id.equals("null")==true){
                    String output = "facebook_id.equals('null') == true";
                    Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/"+facebook_id));
                    startActivity(browserIntent);
                }
            }
        });
        tw_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(twitter_id == null){
                    String output = "twitter_id == null";
                    Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
                }
                else if(twitter_id.equals("null")==true){
                    String output = "twitter_id.equals('null') == true";
                    Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/"+twitter_id));
                    startActivity(browserIntent);
                }
            }
        });
        wb_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(website == null){
                    String output = "website == null";
                    Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
                }
                else if(website.equals("null")==true){
                    String output = "website.equals('null') == true";
                    Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                    startActivity(browserIntent);
                }
            }
        });
        String photo_url_pre = "https://theunitedstates.io/images/congress/original/";
        String photo_url_suf = ".jpg";
        ImageView photo = (ImageView) viewContent.findViewById(R.id.detail_leg_photo);
        Picasso.with(getContext()).load(photo_url_pre + getArguments().getString("bioguide_id")+photo_url_suf).into(photo);
        ImageView partylogo = (ImageView) viewContent.findViewById(R.id.detail_leg_partylogo);
        TextView partytext = (TextView) viewContent.findViewById(R.id.detail_leg_partytext);
        String party = getArguments().getString("party");
        if(party.equals("R")){
            party = "Republican";
            Picasso.with(getContext()).load(re_img_url).resize(100, 100).centerCrop().into(partylogo);
        }else if(party.equals("D")){
            party = "Democratic";
            Picasso.with(getContext()).load(de_img_url).resize(100, 100).centerCrop().into(partylogo);
        }
        partytext.setText(party);
        //Set the list view for both headers and data
        /*String[] tableheaders = getResources().getStringArray(R.array.leg_detail_title);
        ListView tableheaderlist = (ListView)viewContent.findViewById(R.id.detail_leg_th);
        ArrayAdapter<String> headerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, tableheaders);
        tableheaderlist.setAdapter(headerArrayAdapter);*/
        /*List<String> tabledata = new ArrayList<String>();
        tabledata.add(getArguments().getString("title") + " " + getArguments().getString("last_name")
                + ", " + getArguments().getString("first_name"));
        tabledata.add(getArguments().getString("oc_email", "N.A."));
        tabledata.add(getArguments().getString("chamber", "N.A."));
        tabledata.add(getArguments().getString("phone", "N.A."));
        tabledata.add(getArguments().getString("term_start", "N.A."));
        tabledata.add(getArguments().getString("term_end", "N.A."));
        tabledata.add(getArguments().getString("term", "N.A."));
        tabledata.add(getArguments().getString("office", "N.A."));
        tabledata.add(getArguments().getString("state", "N.A."));
        tabledata.add(getArguments().getString("fax", "N.A."));
        tabledata.add(getArguments().getString("birthday", "N.A."));
        ListView tabledatalist = (ListView)viewContent.findViewById(R.id.detail_leg_td);
        ArrayAdapter<String> dataArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, tabledata);
        tabledatalist.setAdapter(dataArrayAdapter);*/
        TextView tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_name);
        tv.setText(getArguments().getString("title") + " " + getArguments().getString("last_name")
                + ", " + getArguments().getString("first_name"));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_email);
        tv.setText(getArguments().getString("oc_email", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_chamber);
        tv.setText(getArguments().getString("chamber", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_contact);
        tv.setText(getArguments().getString("phone", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_start);
        tv.setText(getArguments().getString("term_start", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_end);
        tv.setText(getArguments().getString("term_end", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_term);
        tv.setText(getArguments().getString("term", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_office);
        tv.setText(getArguments().getString("office", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_state);
        tv.setText(getArguments().getString("state", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_fax);
        tv.setText(getArguments().getString("fax", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_birthday);
        tv.setText(getArguments().getString("birthday", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_facebook);
        tv.setText(getArguments().getString("facebook_id", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_twitter);
        tv.setText(getArguments().getString("twitter_id", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_leg_table_website);
        tv.setText(getArguments().getString("website", "N.A."));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Legislator Info");
        return viewContent;
    }
}
