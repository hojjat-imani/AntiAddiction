package com.hojjat.untiaddiction;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hojjat on 3/17/2015.
 */
public class InstalledPackagesInfo {
    String tag = "InstalledPackagesInfo";
    Context context;

    public InstalledPackagesInfo(Context context) {
        this.context = context;
    }

    public ArrayList<PkgInfo> getInstalledApps() {
        ArrayList<PkgInfo> res = new ArrayList<PkgInfo>();
        final PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);
        for(ResolveInfo app : apps){
            PkgInfo pkgInfo = new PkgInfo();
            pkgInfo.appName = app.loadLabel(context.getPackageManager()).toString();
            pkgInfo.icon = app.loadIcon(context.getPackageManager());
            pkgInfo.packageName = app.activityInfo.packageName;
            res.add(pkgInfo);
        }
        Log.d(tag , "getAllInstalledApps " + res.size());
        return res;
    }
}