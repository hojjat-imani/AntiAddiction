package com.hojjat.untiaddiction;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by hojjat on 8/19/15.
 */

public class PkgInfo {
    String tag = "PkgInfo";
    String appName = "";
    String packageName = "";
    String versionName = "";
    int versionCode = 0;
    Drawable icon;
    boolean isBlockedByapp;

    public void prettyPrint() {
        Log.d(tag, appName + "\t" + packageName + "\t" + versionName + "\t" + versionCode);
    }
}