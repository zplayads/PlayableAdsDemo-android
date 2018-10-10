   * [1 Overview (v2.2.0)](#1-overview-v220)
      * [1.1 Introduction](#11-introduction)
      * [1.2 Development Environment](#12-development-environment)
      * [1.3 ZPLAY Ads Account Requirements](#13-zplay-ads-account-requirements)
   * [2 SDK Integration](#2-sdk-integration)
      * [2.1 Add Dependence](#21-add-dependence)
      * [2.2 Sync Project](#22-sync-project)
   * [3 Access Code](#3-access-code)
      * [3.1 Rewarded Video/Interstitial Ad](#31-rewarded-videointerstitial-ad)
         * [3.1.1 Initialize SDK](#311-initialize-sdk)
         * [3.1.2 Request Ad](#312-request-ad)
         * [3.1.3 Show Ads/Obtain Rewards](#313-show-adsobtain-rewards)
         * [3.1.4 Other Methods](#314-other-methods)
      * [3.2 Native Ad](#32-native-ad)
         * [3.2.1 Access Native Ad (Managed Rendering)](#321-access-native-ad-managed-rendering)
         * [3.2.2 Access Native Ad (Self Rendering)](#322-access-native-ad-self-rendering)
      * [* State Code and Description](#-state-code-and-description)

# 1 Overview (v2.2.0)


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

## 2.1 Add Dependence
Add following codes in build.gradle file of project
```
dependencies {
    compile 'com.playableads:playableads:2.2.0'
    
    // Optional dependence
    compile 'com.google.android.gms:play-services-ads:11.0.4'
}
```

## 2.2 Sync Project
Click "Sync Project with Gradle Files" button on menu bar to download dependence files.

# 3 Access Code

Note: You can use the following test id when you are testing. Test id won't generate revenue. Please use official id when you release your App.

|ad_type|  App_ID  |  Ad_Unit_id|
|--------|----------|------------|
|Rewarded Video|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC|
|Interstitial|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|19393189-C4EB-3886-60B9-13B39407064E|
|Native Managed Rendering|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|0246FB55-3042-9F29-D4AB-21C6349EEE83|
|Native Self Rendering|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|BB8452AD-06E7-140B-00DC-FD6CB6B40FAA|

## 3.1 Rewarded Video/Interstitial Ad
### 3.1.1 Initialize SDK
When you initialize the SDK, you need to provide your APPID and adUnitID (as previously registered on zplayads.com) into the marked places. 

Call the method below to initialize SDK
```
PlayableAds.init(context, APPID)
```

### 3.1.2 Request Ad

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

## 3.1.3 Show Ads/Obtain Rewards
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

## 3.1.4 Other Methods

```void setAutoLoadAd(boolean)```The SDK automatically loads the next advertisement after displayed an ad by default. This method can be used to disable automatic loading of the next advertisement.

```boolean canPresentAd(adUnitId)``` This method can determin whether an add has been loaded.

## 3.2 Native Ad

When you access native ad, you can choose to access Managed Rendering or Self Rendering. 

### 3.2.1 Access Native Ad (Managed Rendering)

> Managed rendering is a rendering mode of native ad. In this mode, ad will be rendered automatically. This approach simplifies the process of accessing native ad, and you can access native ad more convient since you do not need to deal with ad rendering related issues.

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

### 3.2.2 Access Native Ad (Self Rendering)

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
    - palyerId: id that used to play VideoView of ad's video
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

# 4 Proguard
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

## * State Code and Description

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

**NOTE**: If you have any problems during the process of access, or if you found any issue in the SDK, please feel free to ask questions. Please refer to [here](https://help.github.com/articles/creating-an-issue).

