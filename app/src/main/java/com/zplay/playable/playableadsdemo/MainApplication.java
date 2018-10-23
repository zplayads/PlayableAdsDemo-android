package com.zplay.playable.playableadsdemo;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Creator: lgd
 * Date: 17-9-13
 * Description:
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "baf3e2a5f2", false);
    }
}
