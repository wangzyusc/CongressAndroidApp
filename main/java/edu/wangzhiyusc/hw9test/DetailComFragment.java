package edu.wangzhiyusc.hw9test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by wangz on 2016/12/1.
 */

public class DetailComFragment extends Fragment {
    private View viewContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.page_detail_com, container, false);

        final ImageView fv_logo = (ImageView) viewContent.findViewById(R.id.detail_com_favorite);
        fv_logo.setImageResource(R.drawable.unselected_star);
        fv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fv_logo.setImageResource(R.drawable.selected_star);
            }
        });
        TextView tv = (TextView)viewContent.findViewById(R.id.detail_com_table_id);
        tv.setText(getArguments().getString("committee_id"));
        tv = (TextView)viewContent.findViewById(R.id.detail_com_table_name);
        tv.setText(getArguments().getString("name", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_com_table_chamber);
        tv.setText(getArguments().getString("chamber", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_com_table_parent);
        tv.setText(getArguments().getString("parent_committee_id", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_com_table_contact);
        tv.setText(getArguments().getString("phone", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_com_table_office);
        tv.setText(getArguments().getString("office", "N.A."));
        String url;
        if(getArguments().getString("chamber").equals("house")){
            url = "http://cs-server.usc.edu:45678/hw/hw8/images/h.png";
        }
        else{
            url = "http://cs-server.usc.edu:45678/hw/hw8/images/s.svg";
        }
        //ImageView img = (ImageView) viewContent.findViewById(R.id.detail_com_table_chamberlogo);
        //Picasso.with(getContext()).load(url).resize(100,100).into(img);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Committee Info");
        return viewContent;
    }

}
