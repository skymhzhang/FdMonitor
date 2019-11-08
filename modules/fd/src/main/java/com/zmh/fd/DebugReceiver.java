package com.zmh.fd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

/**
 * 增加一个调试广播
 * 对于release版本，无法抓取关键调试信息时，可以写在这里，通过广播打印出来
 * <p>
 * adb shell am broadcast -a com.test.fd.DebugReceiver --es param "count"
 *
 * @author skymhzhang
 */
@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "AlibabaAvoidManuallyCreateThread"})
public class DebugReceiver extends BroadcastReceiver {
    private static final String TAG = "zmh_DebugReceiver";
    private static final String ACTION = "com.test.fd.DebugReceiver";

    private final static String COUNT = "count";
    private final static String SAVE = "save";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("DebugReceiver", "onReceive");
        if (intent == null) {
            Log.e(TAG, "onReceive intent = null, return");
            return;
        }
        String key = intent.getStringExtra("param");
        if (key == null) {
            return;
        }
        switch (key) {
            case COUNT:
                FdManager.dumpFdCount();
                break;
            case SAVE:
                FdManager.saveAllInfo2File(context);
                break;
            default:
                break;
        }
    }

    /**
     * 彩蛋打开时才注册,只在主进程注册
     */
    public static void register(Context context) {
        Log.e(TAG, "register123");
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        context.registerReceiver(new DebugReceiver(), intentFilter, null, handler);
    }

}