package com.playableads.constants;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/4/17.
 */

public class ConstantsHelper {
    public static void useProductEnv() {
        BusinessConstants.HOST = "https://pa-engine.zplayads.com";
    }

    public static void useTestEnv() {
        BusinessConstants.HOST = "http://101.201.78.229:8999";
    }

    public static String getVersion() {
        return BusinessConstants.VERSION;
    }
}
