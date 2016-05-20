package com.polybookshare.master.campusbookshare;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by MASTER on 5/3/16.
 */
public class KlassAdapter extends BaseAdapter {

    private List<Klass> m_klasses;
    private Context context;

    public KlassAdapter(List<Klass> klasses, Context context) {
        this.m_klasses = klasses;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.m_klasses.size();
    }

    public Object getItem(int position) {
        return this.m_klasses.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("view", "view");
        if (convertView == null)
            return new KlassView(this.context, this.m_klasses.get(position));
        else {
            ((KlassView) convertView).setKlass(this.m_klasses.get(position));
            return convertView;
        }
    }

}
