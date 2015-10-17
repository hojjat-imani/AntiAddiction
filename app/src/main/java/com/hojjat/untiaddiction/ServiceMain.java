package com.hojjat.untiaddiction;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hojjat.untiaddiction.Util.Constants;

import java.util.Calendar;
import java.util.List;


/**
 * Created by hojjat on 8/12/15.
 */
public class ServiceMain extends Service {
    private static final String Tag = ServiceMain.class.getName();
    BroadcastReceiver screenOnOffBroadcastReceiver;
    Handler appUsageChecker;
    ActivityManager activityManager;
    BlockHelper blockHelper;

    AlarmManager dailyDataResetManager;
    PendingIntent dailyDataResetIntent;

    private long APP_USAGE_CHECK_PERIOD = 500;
    private int ONGOING_NOTIFICATION_ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        blockHelper = new BlockHelper(ServiceMain.this);
        registerScreenOnOffReciever();
        startServiceInForground();
        setDailyDataResetTask();
        startMonitoringAppUsage();
    }

    private void startServiceInForground() {
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle("Fucking Notif")
                .setContentText("this is fucking notif");
        startForeground(ONGOING_NOTIFICATION_ID, nb.build());
    }

    private void registerScreenOnOffReciever() {
        screenOnOffBroadcastReceiver = new BroadcastReceiverScreenOnOff(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenOnOffBroadcastReceiver, intentFilter);
    }

    public void notifyScreenGotOn() {
        startMonitoringAppUsage();
    }

    public void notifyScreenGotOff() {
        stopMonitoringAppUsage();
    }

    private void startMonitoringAppUsage() {
        appUsageChecker = new Handler();
        AppUsageCheck appUsageCheck = new AppUsageCheck();
        appUsageChecker.postDelayed(appUsageCheck, APP_USAGE_CHECK_PERIOD);
    }

    private void stopMonitoringAppUsage() {
        appUsageChecker = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenOnOffBroadcastReceiver);
        dailyDataResetManager.cancel(dailyDataResetIntent);
    }

    private class AppUsageCheck implements Runnable {

        String activeApp = null;
        long prevCheckTime;
        long now;

        public AppUsageCheck() {
            prevCheckTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            blockHelper.addAppUsage(activeApp, (now = System.currentTimeMillis()) - prevCheckTime);
            prevCheckTime = now;
            activeApp = currentActiveApplicationName();
            Log.d(Tag, "activeApp:" + activeApp);
            if (blockHelper.appMustBeBlocked(activeApp)) {
                BlockApp();
                blockHelper.addAppAsAlerted(activeApp);
            }
            if (appUsageChecker != null) {
                appUsageChecker.postDelayed(this, APP_USAGE_CHECK_PERIOD);
            }
        }

        private String currentActiveApplicationName() {
            if (Build.VERSION.SDK_INT <= 20) {
                List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
                if (runningTasks != null && runningTasks.size() > 0)
                    return runningTasks.get(0).topActivity.getPackageName();
            } else {
                List<ActivityManager.RunningAppProcessInfo> runningTasks = activityManager.getRunningAppProcesses();
                if (runningTasks != null && runningTasks.size() > 0)
                    return runningTasks.get(0).processName;
            }
            return null;
        }
    }

    private void BlockApp() {
        Intent intent = null;
        if (blockHelper.getLimitType().equals(BlockHelper.LimitType.Block))
            intent = new Intent(getBaseContext(), ActivityBlock.class);
        else
            intent = new Intent(getBaseContext(), ActivityBlock.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    private void setDailyDataResetTask(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Constants.DAILY_RESET_TIME_HOUR);
        calendar.set(Calendar.MINUTE, Constants.DAILY_RESET_TIME_MINUTE);
        calendar.set(Calendar.SECOND, Constants.DAILY_RESET_TIME_SECOND);
        dailyDataResetIntent = PendingIntent.getService(this, 0,
                new Intent(this, DailyDataReset.class),PendingIntent.FLAG_UPDATE_CURRENT);
        dailyDataResetManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        dailyDataResetManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, dailyDataResetIntent);
    }
    private class DailyDataReset extends Service{
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            blockHelper.resetDailyData();
        }
    }
}