package com.playableads.demo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.text.TextUtils.isEmpty;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/6/14.
 */

public class UserConfig {
    private static final String _APP_ID = "5C5419C7-A2DE-88BC-A311-C3E7A646F6AF";
    private static final String _VIDEO_UNIT_ID = "3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC";
    private static final String _INTERSTITIAL_UNIT_ID = "19393189-C4EB-3886-60B9-13B39407064E";

    private static final String SP_NAME = "zp.user.config";
    private static final String AUTOLOAD = "a";
    private static final String CHANNEL_ID = "b";
    private static final String TEST_ENV = "c";
    private static final String AUTOLOAD_INTERSTITIAL = "d";
    private static final String GDPR_CONSENT = "e";

    private static final String VIDEO_APP_ID = "f";
    private static final String VIDEO_UNIT_ID = "aa";

    private static final String INTERSTITIAL_APP_ID = "ab";
    private static final String INTERSTITIAL_UNIT_ID = "ac";

    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private static UserConfig adConfig;

    public synchronized static UserConfig getInstance(Context ctx) {
        if (adConfig == null) {
            adConfig = new UserConfig(ctx);
        }
        return adConfig;
    }

    @SuppressLint("CommitPrefEdits")
    private UserConfig(Context ctx) {
        sp = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setAutoload(boolean autoload) {
        editor.putBoolean(AUTOLOAD, autoload).apply();
    }

    public boolean isAutoload() {
        return sp.getBoolean(AUTOLOAD, false);
    }

    public void setInterstitialAutoload(boolean autoload) {
        editor.putBoolean(AUTOLOAD_INTERSTITIAL, autoload).apply();
    }

    public boolean isInterstitialAutoload() {
        return sp.getBoolean(AUTOLOAD_INTERSTITIAL, false);
    }

    public void setChannelId(String channelId) {
        editor.putString(CHANNEL_ID, channelId).apply();
    }

    public String getChannelId() {
        return sp.getString(CHANNEL_ID, "");
    }

    public boolean isTestEnv() {
        return sp.getBoolean(TEST_ENV, false);
    }

    public void setTestEnv(boolean testEnv) {
        editor.putBoolean(TEST_ENV, testEnv).apply();
    }

    public void setGdprConsent(boolean consent) {
        editor.putBoolean(GDPR_CONSENT, consent).apply();
    }

    public boolean gdprConsent() {
        return sp.getBoolean(GDPR_CONSENT, true);
    }

    public void setVideoAppId(String id) {
        editor.putString(VIDEO_APP_ID, id).apply();
    }

    public String getVideoAppId() {
        String id = sp.getString(VIDEO_APP_ID, null);
        return isEmptyStr(id) ? _APP_ID : id;
    }

    public void setVideoUnitId(String id) {
        editor.putString(VIDEO_UNIT_ID, id).apply();
    }

    public String getVideoUnitId() {
        String id = sp.getString(VIDEO_UNIT_ID, null);
        return isEmptyStr(id) ? _VIDEO_UNIT_ID : id;
    }

    public void setInterstitialAppId(String id) {
        editor.putString(INTERSTITIAL_APP_ID, id).apply();
    }

    public String getInterstitialAppId() {
        String id = sp.getString(INTERSTITIAL_APP_ID, null);
        return isEmptyStr(id) ? _APP_ID : id;
    }

    public void setInterstitialUnitId(String id) {
        editor.putString(INTERSTITIAL_UNIT_ID, id).apply();
    }

    public String getInterstitialUnitId() {
        String id = sp.getString(INTERSTITIAL_UNIT_ID, null);
        return isEmptyStr(id) ? _INTERSTITIAL_UNIT_ID : id;
    }

    private boolean isEmptyStr(String str) {
        if (isEmpty(str)) {
            return true;
        }
        return isEmpty(str.trim());
    }
}
