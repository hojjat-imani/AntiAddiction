package com.hojjat.untiaddiction;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hojjat.untiaddiction.Util.PersianReshape;
import com.hojjat.untiaddiction.Util.Util;

import java.util.ArrayList;

/**
 * Created by hojjat on 8/22/15.
 */
public class Adapter extends BaseAdapter {
    String tag = "AdapterAppsList";
    Context context;
    ArrayList<PkgInfo> installedApps;
    LayoutInflater inflater;
    BlockHelper blockHelper;

    public Adapter(ArrayList<PkgInfo> installedApps, Context context) {
        this.installedApps = installedApps;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        blockHelper = new BlockHelper(context);
    }

    @Override
    public int getCount() {
        return (null != installedApps ? installedApps.size() : 0);
    }

    @Override
    public PkgInfo getItem(int position) {
        return (null != installedApps ? installedApps.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PkgInfo pkgInfo = installedApps.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_app_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.appName = (TextView) convertView.findViewById(R.id.appName);
            viewHolder.usage = (TextView) convertView.findViewById(R.id.usage);
            viewHolder.limit = (TextView) convertView.findViewById(R.id.limit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Util.setFont(context, Util.FontFamily.Default, Util.FontWeight.Regular, viewHolder.limit, viewHolder.usage, viewHolder.appName);
        viewHolder.icon.setImageDrawable(pkgInfo.icon);
        Log.d(tag, "adapter pkg name: " + pkgInfo.packageName);
        Log.d(tag , "usage :" + blockHelper.getAppUsageTime(pkgInfo.packageName));
        Util.setText(viewHolder.appName , pkgInfo.appName, viewHolder.usage, "استفاده امروز:" + blockHelper.getAppUsageTime(pkgInfo.packageName));
        if (blockHelper.isLimited(pkgInfo.packageName))
            viewHolder.limit.setText("is limited");
        else
            viewHolder.limit.setVisibility(View.GONE);
        return convertView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView appName;
        TextView usage;
        TextView limit;
    }
}
