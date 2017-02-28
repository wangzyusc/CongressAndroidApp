package edu.wangzhiyusc.hw9test;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.util.Arrays;

/**
 * Created by wangz on 2016/11/22.
 */

public class FragmentFavorite extends Fragment {

    private View viewContent;
    private TabLayout tablayout;
    private ViewPager viewpager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if(viewContent == null){
            viewContent = inflater.inflate(R.layout.fragment_favorite, container, false);
            TabHost host = (TabHost) viewContent.findViewById(R.id.tabhost_fav);
            host.setup();

            String[] titles = getResources().getStringArray(R.array.fav_tab_titles);

            TabHost.TabSpec spec = host.newTabSpec("LEGISLATORS");
            spec.setContent(R.id.tab_fav_leg);
            spec.setIndicator("LEGISLATORS");
            host.addTab(spec);

            spec = host.newTabSpec("BILLS");
            spec.setContent(R.id.tab_fav_bill);
            spec.setIndicator("BILLS");
            host.addTab(spec);

            spec = host.newTabSpec("COMMITTEES");
            spec.setContent(R.id.tab_fav_com);
            spec.setIndicator("COMMITTEES");
            host.addTab(spec);
        }
        return viewContent;
    }
}
