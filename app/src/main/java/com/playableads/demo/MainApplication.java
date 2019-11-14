package com.playableads.demo;

import android.app.Application;

import com.playableads.PlayableAdsSettings;
import com.playableads.entity.GDPRStatus;

/**
 * Creator: lgd
 * Date: 17-9-13
 * Description:
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PlayableAdsSettings.enableAutoRequestPermissions(true);
        PlayableAdsSettings.setGDPRConsent(GDPRStatus.PERSONALIZED);
    }
}
