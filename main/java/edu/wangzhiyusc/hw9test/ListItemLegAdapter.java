package edu.wangzhiyusc.hw9test;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangz on 2016/11/30.
 */

public class ListItemLegAdapter extends SimpleAdapter {

    private final Context context;

    public List<String> urls = new ArrayList<String>();

    public ListItemLegAdapter(Context context, List<? extends Map<String, ?>> data,
                              @LayoutRes int resource, String[] from, @IdRes int[] to, List<String> urls){
        super(context, data, resource, from, to);
        this.context = context;
        this.urls = urls;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        ImageView img = (ImageView)v.findViewById(R.id.list_item_leg_img);
        String url = urls.get(position);
        Picasso.with(context).load(url).resize(240,240).centerCrop().into(img);
        ImageView fwd = (ImageView)v.findViewById(R.id.list_item_leg_forward);
        //String fwd_icon = "https://icons8.com/7afbacb2-6f47-4ba0-84fc-c545aecc18b1";
        //Picasso.with(context).load(fwd_icon).resize(240,240).centerCrop().into(fwd);
        //fwd.setImageDrawable(R.drawable.list_item_foward);
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

    /** An image view which always remains square with respect to its width. */
    public final class SquaredImageView extends ImageView {
        public SquaredImageView(Context context) {
            super(context);
        }

        public SquaredImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        }
    }

}




