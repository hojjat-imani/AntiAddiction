package com.hojjat.untiaddiction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by hojjat on 8/13/15.
 */
public class BroadcastReceiverScreenOnOff extends BroadcastReceiver {
    String tag = this.getClass().getName();
    ServiceMain serviceMain;

    public BroadcastReceiverScreenOnOff(ServiceMain serviceMain){
        this.serviceMain = serviceMain;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(tag, "broadcast received     -" + action);
        if (action.equals(Intent.ACTION_SCREEN_ON)) {
            serviceMain.notifyScreenGotOn();
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            serviceMain.notifyScreenGotOff();
        }
    }
}

