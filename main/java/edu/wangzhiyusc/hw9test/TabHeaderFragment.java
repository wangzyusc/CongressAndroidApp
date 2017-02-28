package edu.wangzhiyusc.hw9test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wangz on 2016/11/24.
 */

public class TabHeaderFragment extends Fragment {

    private String mTitle;

    public void setTitle(String mTitle){
        this.mTitle = mTitle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.tab_header_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_header_text);
        textView.setText(this.mTitle);
        return view;
    }
}
