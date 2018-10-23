package com.zplay.playable.playableadsdemo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/6/14.
 */

public class UserConfig {
    private static final String SP_NAME = "zp.user.config";
    private static final String CHANNEL_ID = "b";
    private static final String TEST_ENV = "c";

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


    public boolean setChannelId(String channelId) {
        return editor.putString(CHANNEL_ID, channelId).commit();
    }

    public String getChannelId() {
        return sp.getString(CHANNEL_ID, "");
    }

    public boolean isTestEnv() {
        return sp.getBoolean(TEST_ENV, false);
    }

    public boolean setTestEnv(boolean testEnv) {
        return editor.putBoolean(TEST_ENV, testEnv).commit();
    }

}
