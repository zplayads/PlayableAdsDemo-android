- [1 Overview](#1-overview)
    - [1.1 Introduction](#11-introduction)
    - [1.2 Development Environment](#12-development-environment)
    - [1.3 ZPLAY Ads Account Requirements](#13-zplay-ads-account-requirements)
- [2 Import ZPLAYAds SDK](#2-import-zplayads-sdk)
    - [2.1 Android Studio (preferred)](#21-android-studio-preferred)
        - [2.1.1 Add dependencies](#211-add-dependencies)
        - [2.1.2 Sync Project](#212-sync-project)
    - [2.2 Eclipse](#22-eclipse)
        - [2.2.1 Import SDK jar](#221-import-sdk-jar)
        - [2.2.2 Regitsters ZPLAYAds SDK componets](#222-regitsters-zplayads-sdk-componets)
- [3 Integrates ZPLAYAds SDK](#3-integrates-zplayads-sdk)
    - [3.1 Reward Video](#31-reward-video)
        - [3.1.1 Initialize Reward Video SDK](#311-initialize-reward-video-sdk)
        - [3.1.2 Request Ad](#312-request-ad)
        - [3.1.3 Show Ads/Obtain Rewards](#313-show-adsobtain-rewards)
        - [3.1.4 Other Methods](#314-other-methods)
    - [3.2 Interstitial](#32-interstitial)
        - [3.2.1 Initialize interstitial SDK](#321-initialize-interstitial-sdk)
        - [3.2.2 Request Ad](#322-request-ad)
        - [3.2.3 Show Ads](#323-show-ads)
        - [3.2.4 Other Methods](#324-other-methods)
    - [3.3 Native Ad](#33-native-ad)
        - [3.3.1 Integrate Native Ad (Managed Rendering)](#331-integrate-native-ad-managed-rendering)
        - [3.3.2 Integrate Native Ad (Self Rendering)](#332-integrate-native-ad-self-rendering)
- [4 Others](#4-others)
    - [4.1 Sets proguard file](#41-sets-proguard-file)
    - [4.2 State Code and Description](#42-state-code-and-description)
    - [4.3 FAQ](#43-faq)

# 1 Overview
## 1.1 Introduction
This guide is designed for developers who are going to integrate the ZPLAY Ads SDK into their Android Apps via Android.  Please contact service@zplayads.com, if you need any assistance in this work.

## 1.2 Development Environment
- OS：WinAll, Linux, Mac
- IDE：Android Studio 2.0 and above
- Deploy Target：Android 4.0 and above

## 1.3 ZPLAY Ads Account Requirements
An account is required on our platform before SDK integration can be completed.  The following App specific data items are the minimum needed to proceed.

APP_ID: An ID for your App, obtained when setting up the App for monetization within your account on the ZPLAY Ads website.

AD_UNIT_ID: An ID for a specific ad placement within your App, as generated for your Apps within your account on the ZPLAY Ads website. 

# 2 Import ZPLAYAds SDK
Please follow the steps below to add the SDK. 

## 2.1 Android Studio (preferred)
### 2.1.1 Add dependencies
Add following codes in build.gradle file of project
```
dependencies {
    compile 'com.playableads:playableads:2.3.0'
    
    // Optional dependence
    compile 'com.google.android.gms:play-services-ads:10.0.1'
}
```

### 2.1.2 Sync Project
Click "Sync Project with Gradle Files" button on the Android Studio's menu bar to download dependence files.


## 2.2 Eclipse 
### 2.2.1 Import SDK jar
Import [zplayads.jar](./eclipseJar) into the Eclipse project's libs, and configure the build path, the main steps as follows,
1. Right click the project on Eclipse, select Build Path and Configure Build Path...;
2. Chooses Libraries table, click Add JARs... button;
3. Select the jar file.

NOTE: zplayads.jar only can be used in Eclipse, if your IDE is Android Studio please refer to 2.1 part.

### 2.2.2 Regitsters ZPLAYAds SDK componets
Registers necessary componets into AndroidManifest
1. permissions
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
```
2. activity and receiver
```
<activity
    android:name="com.playableads.presenter.PlayableADActivity"
    android:configChanges="orientation|screenSize|keyboardHidden"
    android:hardwareAccelerated="true"
    android:screenOrientation="portrait"
    android:theme="@android:style/Theme.NoTitleBar.Fullscreen" />

<activity
    android:name="com.playableads.presenter.NativeAdLandingPageActivity"
    android:configChanges="orientation|screenSize|keyboardHidden"
    android:hardwareAccelerated="true"
    android:screenOrientation="portrait"
    android:theme="@android:style/Theme.NoTitleBar.Fullscreen" />

<receiver android:name="com.playableads.PlayableReceiver">
    <intent-filter>
        <action android:name="android.intent.action.DOWNLOAD_COMPLETE" />
    </intent-filter>
</receiver>
```

# 3 Integrates ZPLAYAds SDK

Note: You can use the following test id when you are testing. Test id won't generate revenue. Please use official id when you release your App.

|AD_TYPE|  APP_ID  |  AD_UNIT_ID|
|--------|----------|------------|
|Reward Video|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC|
|Interstitial|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|19393189-C4EB-3886-60B9-13B39407064E|
|Native Managed Rendering|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|0246FB55-3042-9F29-D4AB-21C6349EEE83|
|Native Self Rendering|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|BB8452AD-06E7-140B-00DC-FD6CB6B40FAA|

## 3.1 Reward Video
### 3.1.1 Initialize Reward Video SDK
Calls```PlayableAds.init(context, APP_ID)``` to initialize Reward Video SDK, e.g.
```
PlayableAds mRewardVideo = PlayableAds.init(context, "5C5419C7-A2DE-88BC-A311-C3E7A646F6AF");
```

### 3.1.2 Request Ad

To pre-load an ad may take several seconds, so it's recommended to initialize the SDK and load ads as early as possible. 

Calls ```mRewardVideo.requestPlayableAds(AD_UNIT_ID, playPreloadingListener)``` to load ad，the all methods of theplayPreloadingListener as below：
```
public interface PlayPreloadingListener {
    // reward video has been loaded
    void onLoadFinished();
    // reward video load failed, you can read the message to find out what went wrong with the download.
    void onLoadFailed(int errorCode, String message);
}
```

code snippet for request ad：
```
mRewardVideo.requestPlayableAds("3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC", new PlayPreloadingListener() {
    @Override
    public void onLoadFinished() {
        // Ad has been loaded, you can invoke mRewardVideo.presentPlayableAD(...) to show the ad
    }

    @Override
    public void onLoadFailed(int errorCode, String message) {
        // Ad load failed
    }
})
```

### 3.1.3 Show Ads/Obtain Rewards
When an ad is ready to display, you can show it using following method.
```
mRewardVideo.presentPlayableAD(AD_UNIT_ID, playLoadingListener)
```
You can confirm the completed ad show with this listener callback.  
```
public interface PlayLoadingListener {
    // the ad game start playing
    void onVideoStart();

    // the ad game end, game landing page will showing
    void onVideoFinished();

    // // This is a callback of completing the whole event (showing, playing, quitting from landing page), which means the reward shall be given
    void playableAdsIncentive();

    // error occurs when showing ad
    void onAdsError(int code, String msg);

    // user click the install button
    void onLandingPageInstallBtnClicked();

    // ad's finally event
    void onAdClosed();
}
```

Code snippet for show an ad:
```
mRewardVideo.presentPlayableAD("3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC", new PlayLoadingListener() {
    @Override
    void onVideoStart(){
        // Reward Video start playing, if your app has sound, you can turn it off here to avoid playing the sound of apps and ads at the same time.
    }

    @Override
    public void playableAdsIncentive() {
        // Ad impression success, use this to judge if the reward should be given.
    }

    @Override
    void onAdClosed(){
        // Ad has been closed, if you turn off the app's sound in video start, you can turn it on in here
    }
);
```

### 3.1.4 Other Methods

```void setAutoLoadAd(boolean)```The SDK automatically loads the next advertisement after displayed an ad by default. This method can be used to disable automatic loading of the next advertisement.

```boolean canPresentAd(AD_UNIT_ID)``` This method can determin whether an add has been loaded.

The [PlayableAdSample](./app/src/main/java/com/zplay/playable/playableadsdemo/sample/PlayableAdSample.java) file is the Reward Video sample code.

## 3.2 Interstitial
### 3.2.1 Initialize interstitial SDK
Calls ```PlayableInterstitial.init(context, APP_ID)``` to initialize Reward Video SDK, e.g.
```
PlayableInterstitial mInterstitial = PlayableInterstitial.init(context, "5C5419C7-A2DE-88BC-A311-C3E7A646F6AF");
```

### 3.2.2 Request Ad

To pre-load an ad may take several seconds, so it's recommended to initialize the SDK and load ads as early as possible. 

Calls the following method to pre-load playable ad

```
mInterstitial.requestPlayableAds(AD_UNIT_ID, playPreloadingListener);
```
You can judge the availability of an ad by this listener callback.
```
public interface PlayPreloadingListener {
    // Interstitial has been loaded
    void onLoadFinished();
    // Fail to load an ad
    void onLoadFailed(int errorCode, String msg);
}
```

Code Sample：

```
mInterstitial.requestPlayableAds("3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC", new PlayPreloadingListener() {
    @Override
    public void onLoadFinished() {
        // Ad preload successfully, call method presentPlayableAd(...)to show playable ad.
    }

    @Override
    public void onLoadFailed(int errorCode, String message) {
        // Ad preload failed, locate problem according error code and error messages.
    }
})
```

### 3.2.3 Show Ads
When an ad is ready to display, you can show it using following method.
```
mInterstitial.presentPlayableAD(AD_UNIT_ID, playLoadingListener)
```
You can confirm the completed ad show with this listener callback.  
```
public interface PlayLoadingListener {
    // the ad game start playing
    void onVideoStart();

    // the ad game end, game landing page will showing
    void onVideoFinished();

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
mInterstitial.presentPlayableAD("3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC", new PlayLoadingListener() {
    @Override
    void onVideoStart(){
        // Interstitial start playing, if your app has sound, you can turn it off here to avoid playing the sound of apps and ads at the same time.
    }

    @Override
    void onAdClosed(){
        // Ad has been closed, if you turn off the app's sound in video start, you can turn it on in here
    }

    ...
});
```

### 3.2.4 Other Methods

```void setAutoload(boolean)```The SDK automatically loads the next advertisement after displayed an ad by default. This method can be used to disable automatic loading of the next advertisement.

```boolean canPresentAd(AD_UNIT_ID)``` This method can determin whether an add has been loaded.

The [InterstitialSample](./app/src/main/java/com/zplay/playable/playableadsdemo/sample/InterstitialSample.java) file is the sample code for interstitial.

## 3.3 Native Ad

When you integrate native ad, you can choose Managed Rendering or Self Rendering. 

### 3.3.1 Integrate Native Ad (Managed Rendering)

> Managed rendering is a rendering mode of native ad. In this mode, ad will be rendered automatically. This approach simplifies the process of accessing native ad, and you can integrate native ad more convient since you do not need to deal with ad rendering related issues.

a. Initialize
```
PlayableNativeExpressAd mPlayableNativeAd = new PlayableNativeExpressAd(mContext, mAppId, mAdUnitId)
```
Set listener event of loading ads:
```
mPlayableNativeAd.setNativeAdLoadListener(new NativeAdLoadListener() {
    @Override
    public void onNativeAdLoaded(NativeAd nativeAd) {
        // Success to request ad, put nativeAd into Adapter for future use
    }

    @Override
    public void onNativeAdFailed(int errorCode, String message) {
        // Fail to request ad, check "State Code and Description" section of this doc according to errorCode to locate problem quickly
    }
});
```
b. Creat Template Layout  
Suppose RecyclerView is carrier of information flow, the layout of NativeTemplateViewHolder itemView we creat is as follows: 
```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="#eee">
    <com.playableads.nativead.NativeAdExpressView
        android:id="@+id/nativeAdExpressView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="10dp" />
</FrameLayout>
```

c. Load Ad

Call the following method to load ad

```mPlayableNativeAd.loadAd()```

d. Render Ad

In RecyclerView Adapter, fire the function ```onBindViewHolder(ViewHolder holder, int position)```

```
NativeAd nativeAd = mNativeAds.get(position);
if (nativeAd != null) {
    nativeAd.renderAdView(nativeTemplateViewHolder.nativeTemplateView);
}
```
e. Listener callback of ad impression/click

```
nativeAd.setNativeEventListener(new NativeEventListener() {
    @Override
    public void onAdImpressed(View view) {
        // Ad has been impressed
    }
    @Override
    public void onAdClicked(View view) {
        // Ad has been clicked
    }
});
```

The [NativeAdRecyclerViewSample](./app/src/main/java/com/zplay/playable/playableadsdemo/sample/NativeAdRecyclerViewSample.java) file is the sample code for the native ad(Managed Rendering).

### 3.3.2 Integrate Native Ad (Self Rendering)

> Self rendering is another rendering mode, which has high flexibility, of native ad. You can splice ad style according to your needs to make ad more suitable for your app.  

a. Initialize

```
PlayableNativeAd mPlayableNativeAd = new PlayableNativeAd(mContext, mAppId, mAdUnitId)
```

b. Add NativeRender to Set Layout of Ad 

Following elements are included in custom layout:

    - mainImageId: id that used to display ImageView of ad's main image
    - iconImageId: id that used to display iconImageView of ad's icon
    - titleId: id that used to display TextView of ad's title
    - textId: id that used to display TextView of ad's description
    - buttonId: id that used to display "INSTALL NOW" Button
    - playerId: id that used to play VideoView of ad's video
```
ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_ad_layout)
                .mainImageId(R.id.nal_image)
                .iconImageId(R.id.nal_icon)
                .titleId(R.id.nal_title)
                .textId(R.id.nal_description)
                .buttonId(R.id.nal_button)
                .playerId(R.id.nal_player)
                .build();
NativeAdRender nativeAdRender = new NativeAdRender(viewBinder);
mPlayableNativeAd.setAdRender(nativeAdRender);
```
**Attention:** NativeAdRender must be set in self rendering mode. Otherwise, ad can not be displayed properly.

c. Add Request Listener Method and Creat Ad's View
```
 mPlayableNativeAd.setNativeAdLoadListener(new NativeAdLoadListener() {
    @Override
    public void onNativeAdLoaded(NativeAd nativeAd) {
        // When request succeeded, creat ad View. mNativeView is parent container of ad View (like LinearLayout in Demo)
        View view = nativeAd.createAdView(YourActivity.this, mNativeView);
        nativeAd.renderAdView(view);
        mNativeView.addView(view);
    }

    @Override
    public void onNativeAdFailed(int errorCode, String message) {
        // When failed to request ad, check "State Code and Description" section of this doc according to errorCode to locate problem quickly
    }
});
```
d. Add Impression Listener Method (Optional)

**If necessery**, you can add listener callback of ad's impression/click as follows: 
```
nativeAd.setNativeEventListener(new NativeEventListener() {
    @Override
    public void onAdImpressed(View view) {
        // Ad has been impressed
    }

    @Override
    public void onAdClicked(View view) {
        // Ad has been clicked
    }
});
```
e. Request Ad
```
mPlayableNativeAd.loadAd()
```

The [NativeAdSample](./app/src/main/java/com/zplay/playable/playableadsdemo/sample/NativeAdSample.java) file is the sample code of native ad(Self Rendering).

# 4 Others
## 4.1 Sets proguard file
If the project need to be proguarded, put the following code into the proguard.pro file.
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
-keep class com.playableads.PlayableInterstitial {
    public static com.playableads.PlayableInterstitial getInstance();
    public synchronized static com.playableads.PlayableInterstitial init(android.content.Context, java.lang.String);
    public <methods>;
}
# ZPLAYAds native ad
-keep class com.playableads.PlayableNativeAd {
    public <methods>;
}
-keep class com.playableads.PlayableNativeExpressAd {
    public <methods>;
}
-keep class com.playableads.nativead.NativeAdRender {
    public <methods>;
}
-keep class com.playableads.nativead.ViewBinder.NativeViewHolder {
    public <methods>;
}
-keep class com.playableads.nativead.NativeAdExpressView {
    public <methods>;
}
-keep class com.playableads.nativead.NativeAdLoadListener {*;}
-keep class com.playableads.nativead.NativeEventListener {*;}
-keep class com.playableads.nativead.NativeAd {
    public <methods>;
}
-keep class com.playableads.nativead.ViewBinder$* {*;}
```

## 4.2 State Code and Description

|state code|description|notes|
|-----|----|---|
|1001|request constructed error|error occurrs while constructing the request parameter, unable to get device token(any of adversting_id, android_id, imei, mac_addr)|
|1002|request parameters error|request parameter does not match, please check whether APP_ID, UNIT_ID has been passed correctly|
|1003|lack of WRITE_EXTERNAL_STORAGE|unable to write file to device, unable to cache advertising material|
|2002|preload finished|ad had been preloaded|
|2004|ads has filled|ad is loading or has been loaded|
|2005|no ad|no ad returned from server|
|2006|no connection error|can not connect to the internet, check the internet connection|
|2007|timeout error|network connection timeout, check network or switch network and retry request|
|2008|server error|an exception may occur in advertisement service, please try again later|
|5001|context is null|context is null, please check whether context has been passed correctly|
|5002|network error|network error|

## 4.3 FAQ

If you have any problems during the process of integration, or if you found any issue in the SDK, please feel free to ask questions and [push an issue](https://github.com/zplayads/PlayableAdsDemo-android/issues/new?title=[Describe%20the%20issue%20briefly]&body=Write%20here%20with%20the%20detail%20message%20of%20the%20issue.%20If%20you%20have%20any%20error%20log%20about%20the%20issue,%20please%20attach%20here,%20too.%20Thanks%20a%20lot%20)

