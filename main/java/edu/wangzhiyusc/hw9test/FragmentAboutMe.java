package edu.wangzhiyusc.hw9test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by wangz on 2016/12/1.
 */

public class FragmentAboutMe extends Fragment {
    private View viewContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if(viewContent == null) {
            viewContent = inflater.inflate(R.layout.fragment_about_me, null);
            ImageView img = (ImageView) viewContent.findViewById(R.id.about_me_photo);
            img.setImageResource(R.mipmap.author_photo);
            TextView tv = (TextView) viewContent.findViewById(R.id.about_me_name);
            tv.setText("Zhiyuan Wang");
            tv = (TextView)viewContent.findViewById(R.id.about_me_uscid);
            tv.setText("3290218825");
        }

        return viewContent;
    }
}
