package com.cultivatingcows.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cultivatingcows.Models.SpecialMap;

import java.util.List;

/**
 * Created by colleenminor on 7/26/16.
 */
public class SpecialMapAdapter extends BaseAdapter{
    protected Context mContext;
    protected List<SpecialMap> mSpecialMaps;

    public SpecialMapAdapter(Context context, List<SpecialMap> specialMaps) {
        mContext = context;
        mSpecialMaps = specialMaps;
    }

    @Override
    public int getCount() {
        return mSpecialMaps.size();
    }
    //
    @Override
    public Object getItem(int position) {
        return mSpecialMaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    //
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SpecialMap specialMap = mSpecialMaps.get(position);
        return convertView;
    }

    private static class ViewHolder {
        TextView categoryText;
        TextView runningExpensesSumText;
    }


}
