package edu.wangzhiyusc.hw9test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by wangz on 2016/12/1.
 */

public class DetailBillFragment extends Fragment {
    private View viewContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.page_detail_bill, container, false);

        final ImageView fv_logo = (ImageView) viewContent.findViewById(R.id.detail_bill_favorite);
        fv_logo.setImageResource(R.drawable.unselected_star);
        fv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fv_logo.setImageResource(R.drawable.selected_star);
            }
        });
        TextView tv = (TextView)viewContent.findViewById(R.id.detail_bill_table_id);
        tv.setText(getArguments().getString("bill_id"));
        tv = (TextView)viewContent.findViewById(R.id.detail_bill_table_title);
        tv.setText(getArguments().getString("official_title", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_bill_table_type);
        tv.setText(getArguments().getString("bill_type", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_bill_table_sponsor);
        tv.setText(getArguments().getString("sponsor", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_bill_table_chamber);
        tv.setText(getArguments().getString("chamber", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_bill_table_status);
        tv.setText(getArguments().getString("status", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_bill_table_date);
        tv.setText(getArguments().getString("introduced_on", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_bill_table_congressurl);
        tv.setText(getArguments().getString("congress_url", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_bill_table_version);
        tv.setText(getArguments().getString("version_status", "N.A."));
        tv = (TextView)viewContent.findViewById(R.id.detail_bill_table_billurl);
        tv.setText(getArguments().getString("bill_url", "N.A."));

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Bill Info");
        return viewContent;
    }
}
