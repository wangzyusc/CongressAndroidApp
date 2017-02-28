package edu.wangzhiyusc.hw9test;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by wangz on 2016/12/1.
 */

public class ListItemComAdapter extends SimpleAdapter {
    private final Context context;

    public ListItemComAdapter(Context context, List<? extends Map<String, ?>> data,
                               @LayoutRes int resource, String[] from, @IdRes int[] to){
        super(context, data, resource, from, to);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        ImageView fwd = (ImageView)v.findViewById(R.id.list_item_com_fwd);
        fwd.setImageResource(R.drawable.list_item_foward);
        return v;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
