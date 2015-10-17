package com.hojjat.untiaddiction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.hojjat.untiaddiction.Util.Util;

import java.util.Date;

/**
 * Created by hojjat on 8/13/15.
 */
public class BroadcastRecieverBootCompleted extends BroadcastReceiver{
    String tag = this.getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(tag, "broadcast received     -" + action);
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Util.startMainService(context);
        }
    }
}
