# 1 Overview (v2.0.7)


## 1.1 Introduction
This guide is designed for developers who are going to integrate the ZPLAY Ads SDK into their Android Apps via Android Studio.  Please contact service@zplayads.com, if you need any assistance in this work.

## 1.2 Development Environment
- OS：WinAll, Linux, Mac
- IDE：Android Studio 2.x
- Deploy Target：Android 4.0 and above

## 1.3 ZPLAY Ads Account Requirements
An account is required on our platform before SDK integration can be completed.  The following App specific data items are the minimum needed to proceed.

APPID: An ID for your App, obtained when setting up the App for monetization within your account on the ZPLAY Ads website.

adUnitID: An ID for a specific ad placement within your App, as generated for your Apps within your account on the ZPLAY Ads website. 

# 2 SDK Integration
Please follow the steps below to add the SDK. 

## 2.1 Add dependence
Add following codes in build.gradle file of project
```
dependencies {
    compile 'com.playableads:playableads:2.0.7'
    
    // Optional dependence
    compile 'com.google.android.gms:play-services-ads:11.0.4'
}
```

## 2.2 Sync project
Click "Sync Project with Gradle Files" button on menu bar to download dependence files.

# 3 Access Code
To pre-load an ad may take several seconds, so it’s recommended to initialize the SDK and load ads as early as possible. When you initialize the SDK, you need to provide your APPID and adUnitID (as previously registered on ZPLAYAds.com) into the relevant places. 

## 3.1 Initialize SDK
When you initialize the SDK, you need to provide your APPID and adUnitID (as previously registered on en.zplayads.com) into the marked places. 

Call the method below  to initialize SDK
```
PlayableAds.init(context, APPID)
```

Note: You can use the following test id when you are testing. Test id won't generate revenue, please use official id when you release your App.

|ad_type|  App_ID  |  Ad_Unit_id|
|--------|----------|------------|
|Rewarded Video |5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC|
|Interstitial|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|19393189-C4EB-3886-60B9-13B39407064E|

## 3.2 Request Ad
To pre-load an ad may take several seconds, so it's recommended to initialize the SDK and load ads as early as possible. 

Call the following method to pre-load playable ad

```
PlayableAds.getInstance().requestPlayableAds(adUnitId, playPreloadingListener);
```
You can judge the availability of an ad by this listener callback.
```
public interface PlayPreloadingListener {
    // An ad is loaded
    void onLoadFinished();
    // Fail to load an ad
    void onLoadFailed(int errorCode, String msg);
}
```

Code Sample：

```
PlayableAds.getInstance().requestPlayableAds("3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC", new PlayPreloadingListener() {
    @Override
    public void onLoadFinished() {
        // Ad preload successfully, call method presentPlayableAd(...)to show playable ad.
    }

    @Override
    public void onLoadFailed(int errorCode, String message) {
        //  Ad preload failed, locate problem according error code and error messages.
    }
})
```
## 3.3 Show Ads/Obtain Rewards
When an ad is ready to display, you can show it using following method.
```
PlayableAds.getInstance().presentPlayableAD(adUnitId, playLoadingListener)
```
You can confirm the completed ad show with this listener callback.  
```
public interface PlayLoadingListener {
    // the ad game start playing
    void onVideoStart();

    // the ad game end, game landing page will showing
    void onVideoFinished();

    // // This is a callback of completing the whole event (showing, playing, quitting from landing page), which means the reward shall be given
    // note: Interstitial will not fire the function
    void playableAdsIncentive();

    // error occurs when showing ad
    void onAdsError(int code, String msg);

    // user click the install button
    void onLandingPageInstallBtnClicked();

    // ad's finally event
    void onAdClosed();
}
```

Code Sample:
```
PlayableAds.getInstance().presentPlayableAD("3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC", new PlayLoadingListener() {
    @Override
    public void playableAdsIncentive() {
        // Ad impression success, use this to judge if the reward should be given. At this point, you can request the next ad
        // Interstitial will not fire the function
    }

    @Override
    public void onAdsError(int errorCode, String message) {
        // Ad impression fail,locate problem according error code and error messages.
    }
});
```

## 3.4 other methods

```void setAutoLoadAd(boolean)```The SDK automatically loads the next advertisement after displayed an ad by default. This method can be used to disable automatic loading of the next advertisement.

```boolean canPresentAd(adUnitId)``` This method can determin whether an add has been loaded.

# 4 Proguard
If the project need to be proguarded, put the following code into the proguard.pro file or a custom file.
```
# ZPLAYAds
-keep class com.playableads.PlayPreloadingListener {*;}
-keep class com.playableads.PlayLoadingListener {*;}
-keep class * implements com.playableads.PlayPreloadingListener {*;}
-keep class * implements com.playableads.PlayLoadingListener {*;}
-keep class com.playableads.PlayableReceiver {*;}
-keep class com.playableads.constants.StatusCode {*;}
-keep class com.playableads.MultiPlayLoadingListener {*;}
-keep class com.playableads.MultiPlayPreloadingListener {*;}
-keep class * implements com.playableads.MultiPlayLoadingListener {*;}
-keep class * implements com.playableads.MultiPlayPreloadingListener {*;}
-keep class com.playableads.PlayableAds {
    public static com.playableads.PlayableAds getInstance();
    public synchronized static com.playableads.PlayableAds init(android.content.Context, java.lang.String);
    public <methods>;
}
```

## * state code and description

|state code|description|
|-----|----|
|1001|request constructed error|
|1002|request parameters error.|
|1003|lack of WRITE_EXTERNAL_STORAGE|
|1004|lack of READ_PHONE_STATE|
|2001|payload has been loaded|
|2002|preload finished|
|2004|ads has filled|
|2005|no ad|
|5001|context is null|
|5002|network error|


**NOTE**: If you have any problems during the process of access, or if you found any issue in the SDK, please feel free to ask questions. Please refer to [here](https://help.github.com/articles/creating-an-issue).

