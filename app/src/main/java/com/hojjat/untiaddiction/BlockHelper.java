package com.hojjat.untiaddiction;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

/**
 * Created by hojjat on 8/26/15.
 */

public class BlockHelper {
    String tag = this.getClass().getName();
    private Context context;

    String APPS_LIMIT_TIMES = "APPS_LIMIT_TIMES";
    String APPS_USAGE_TIMES = "APPS_USAGE_TIMES";
    String USER_PREFRENCES = "USER_PREFRENCES";
    String TODAY_ALERTED_APPS = "TODAY_ALERTED_APPS";
    String BLOCK_TYPE = "BLOCK_TYPE";

    SharedPreferences appsUsageTimes;
    SharedPreferences appsLimitTimes;
    SharedPreferences todayAlertedApps;
    SharedPreferences userPrefs;

    public enum LimitType {
        Alert("ALERT"), Block("BLOCK");

        private String value;

        LimitType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    public BlockHelper(Context context) {
        this.context = context;
        setUpPrefrences();
    }

    private void setUpPrefrences() {
        Log.d(tag, "settting up prefrenceses ...");
        appsUsageTimes = context.getSharedPreferences(APPS_USAGE_TIMES, Context.MODE_PRIVATE);
        appsLimitTimes = context.getSharedPreferences(APPS_LIMIT_TIMES, Context.MODE_PRIVATE);
        userPrefs = context.getSharedPreferences(USER_PREFRENCES, Context.MODE_PRIVATE);
        todayAlertedApps = context.getSharedPreferences(TODAY_ALERTED_APPS, Context.MODE_PRIVATE);
    }

    public long getAppUsageTime(String pkgName) {
//        Log.d(tag, "called: getAppUsageTime(" + pkgName + ")");
        if (appsUsageTimes == null)
            setUpPrefrences();
        return appsUsageTimes.getLong(pkgName, 0);
    }

    public void addAppUsage(String pkgName, long time) {
//        Log.d(tag, "called: addAppUsage(" + pkgName + "," + time + ")");
        if (pkgName != null)
            appsUsageTimes.edit().putLong(pkgName, getAppUsageTime(pkgName) + time).commit();

    }

    public boolean appMustBeBlocked(String pkgName) {
        if (appsUsageTimes == null || appsLimitTimes == null || todayAlertedApps == null)
            setUpPrefrences();
        if (appsLimitTimes.contains(pkgName))
            if (appsUsageTimes.getLong(pkgName, 0) >= appsLimitTimes.getLong(pkgName, 0))
                if (getLimitType() == LimitType.Block || !todayAlertedApps.contains(pkgName))
                    return true;
        return false;
    }

    public void limitApp(String pkgName, long limitTime) {
        if (appsUsageTimes == null)
            setUpPrefrences();
        appsLimitTimes.edit().putLong(pkgName, limitTime).commit();
    }

    public boolean isLimited(String pkgName) {
        if (appsLimitTimes == null)
            setUpPrefrences();
        if (appsLimitTimes.contains(pkgName))
            return true;
        return false;
    }

    public LimitType getLimitType() {
        if (userPrefs == null)
            setUpPrefrences();
        if (userPrefs.getString(BLOCK_TYPE, LimitType.Block.getValue()).equals(LimitType.Block.getValue()))
            return LimitType.Block;
        else
            return LimitType.Alert;
    }

    public void setLimitType(LimitType limitType) {
        if (userPrefs == null)
            setUpPrefrences();
        userPrefs.edit().putString(BLOCK_TYPE, limitType.getValue()).commit();
    }

    public void addAppAsAlerted(String pkgName){
        if(todayAlertedApps == null)
            setUpPrefrences();
        todayAlertedApps.edit().putBoolean(pkgName, true).commit();
    }

    public void resetDailyData(){
        if (appsUsageTimes == null || todayAlertedApps == null)
            setUpPrefrences();
        appsUsageTimes.edit().clear().commit();
        todayAlertedApps.edit().clear().commit();
    }
}