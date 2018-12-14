package com.ran.watchsale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 开机启动监听
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(LOG_TAG, "onReceive(), action=" + intent.getAction());
        context.startService(new Intent(context, MainService.class));
    }
}
