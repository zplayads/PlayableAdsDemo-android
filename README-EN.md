   * [1 Overview](#1-overview)
      * [1.1 Introduction](#11-introduction)
      * [1.2 Development Environment](#12-development-environment)
      * [1.3 ZPLAY Ads Account Requirements](#13-zplay-ads-account-requirements)
   * [2 Import ZPLAYAds SDK](#2-import-zplayads-sdk)
      * [2.1 Android Studio (recommend)](#21-android-studio-recommend)
         * [2.1.1 Add dependencies](#211-add-dependencies)
         * [2.1.2 Sync Project](#212-sync-project)
      * [2.2 Eclipse](#22-eclipse)
         * [2.2.1 Import SDK jar](#221-import-sdk-jar)
         * [2.2.2 Regitsters ZPLAYAds SDK componets](#222-regitsters-zplayads-sdk-componets)
   * [3 Integrates ZPLAYAds SDK](#3-integrates-zplayads-sdk)
      * [3.1 Banner](#31-banner)
         * [3.1.1 Initialization and Request](#311-initialization-and-request)
         * [3.1.2 Events](#312-events)
         * [3.1.3 Other methods](#313-other-methods)
      * [3.2 Reward Video](#32-reward-video)
         * [3.2.1 Initialization and Request](#321-initialization-and-request)
         * [3.2.2 Show](#322-show)
         * [3.2.3 other methods](#323-other-methods)
      * [3.3 Interstitial](#33-interstitial)
         * [3.3.1 Initialization and Request](#331-initialization-and-request)
         * [3.3.2 Show](#332-show)
         * [3.3.3 Other methods](#333-other-methods)
      * [3.4 Native Ad](#34-native-ad)
         * [3.4.1 Integrate Native Ad (Managed Rendering)](#341-integrate-native-ad-managed-rendering)
         * [3.4.2 Integrate Native Ad (Self Rendering)](#342-integrate-native-ad-self-rendering)
   * [5 Others](#5-others)
      * [5.1 Sets proguard file](#51-sets-proguard-file)
      * [5.2 State Code and Description](#52-state-code-and-description)
      * [5.3 FAQ](#53-faq)
   * [6 Test](#6-test)

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

## 2.1 Android Studio (recommend)
### 2.1.1 Add dependencies
Add following codes in build.gradle file of project
```
dependencies {
    implementation 'com.playableads:playableads:2.6.0'
}
```

### 2.1.2 Sync Project
Click "Sync Project with Gradle Files" button on the Android Studio's menu bar to download dependence files.


## 2.2 Eclipse 
### 2.2.1 Import SDK jar
Import [zplayads-2.6.0.jar](https://github.com/zplayads/PlayableAdsDemo-android/raw/master/eclipseJar/zplayads-2.6.0.jar) into the Eclipse project's libs, and configure the build path, the main steps as follows,
1. Right click the project on Eclipse, select Build Path and Configure Build Path...;
2. Chooses Libraries table, click Add JARs... button;
3. Select the jar file.

also need move [assets](https://github.com/zplayads/PlayableAdsDemo-android/tree/master/eclipseJar/assets) resources to your project assets directory.

### 2.2.2 Regitsters ZPLAYAds SDK componets
Registers necessary componets into AndroidManifest
1. permissions
```
<!-- required permissions -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- optional permissions -->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
```
2. activity and receiver
```xml
<activity
    android:name="com.playableads.presenter.PlayableADActivity"
    android:configChanges="orientation|screenSize|keyboardHidden"
    android:hardwareAccelerated="true"
    android:theme="@android:style/Theme.NoTitleBar.Fullscreen" />

<activity
    android:name="com.playableads.presenter.NativeAdLandingPageActivity"
    android:configChanges="orientation|screenSize|keyboardHidden"
    android:hardwareAccelerated="true"
    android:screenOrientation="portrait"
    android:theme="@android:style/Theme.NoTitleBar.Fullscreen" />

<activity
    android:name="com.playableads.presenter.WebActivity"
    android:configChanges="orientation|screenSize|keyboardHidden"
    android:hardwareAccelerated="true"
    android:theme="@android:style/Theme.NoTitleBar.Fullscreen" />

<receiver android:name="com.playableads.PlayableReceiver">
    <intent-filter>
        <action android:name="android.intent.action.DOWNLOAD_COMPLETE" />
    </intent-filter>
</receiver>
```

# 3 Integrates ZPLAYAds SDK

You should use the testing id in test phase. Testing id won't generate revenue. Please use official id when you release your App.

## 3.1 Banner
### 3.1.1 Initialization and Request 
```java
// Create Banner object
// context: activity or context
// APP_ID: application id
// AD_UNIT_ID: ad unit id
mBanner = new AtmosplayAdsBanner(context, APP_ID, AD_UNIT_ID);

// Set Banner size
// BannerSize:
//  - BANNER_320x50: request a Banner with a shape of 320dp x 50dp
//  - BANNER_728x90: request a Banner with a shape of 728dp x 90dp
//  - SMART_BANNER: request a Banner based on your device which if it is tablet the shape will be screen width x 90dp, otherwise, if it is landscpe model the shape will be screen width x 32sp, others will be screen width x 50dp
mBanner.setBannerSize(BannerSize);

// (optional)Set Banner container
// When you set the container, the Banner is automatically filled into the container when it is ready then refresh the Banner at next banner is ready.
// When you do not set the container, you should get Banner view when banner is loaded and then put it into your container, also you should take care of it when it comes new Banner view is ready
mBanner.setBannerContainer(ViewGroup);

// Load an advertising
mBanner.loadAd();
```

### 3.1.2 Events
When you need some Banner lifecycle events like loaded, failed or clicked, you can do as follows:
```java
mBanner.setBannerListener(bannerListener)
```
BannerListener definition
```java
interface BannerListener {
    // Fired when Banner is ready
    // ！！note！！ if you set a container before, the banner will be automatically filled into the container
    void onBannerPrepared(AtmosBannerView banner);
    // Fired when Banner load failed, you will get error code and error message
    void onBannerPreparedFailed(int code, String error);
    // Fired when Banner view was clicked
    void onBannerClicked();
}
```
### 3.1.3 Other methods
```java
// Set channel id, when you publish your app to different app market you may use it
mBanner.setChannelId(string);
// Destroy the Banner object
mBanner.destory()
```

The [BannerSample](./app/src/main/java/com/zplay/playable/playableadsdemo/sample/BannerSample.java) file is the Banner sample code.

## 3.2 Reward Video
### 3.2.1 Initialization and Request
```java
// Create Reward video object
// context: activity or context
// APP_ID: application ID
mRewardVideo = PlayableAds.init(context, APP_ID)

// Request reward video
// AD_UNIT_ID: ad unit id
// playPreloadingListener：you can get request events from the object's callbacks
// To pre-load an ad may take several seconds, so it's recommended to initialize the SDK and load ads as early as possible. 
mRewardVideo.requestPlayableAds(AD_UNIT_ID, playPreloadingListener)
```
PlayPreloadingListener definition
```java
interface PlayPreloadingListener {
    // reward video has been loaded
    void onLoadFinished();
    // reward video load failed, you can read the message to find out what went wrong with the download.
    void onLoadFailed(int errorCode, String message);
}
```

### 3.2.2 Show
```java
// Show reward video
// AD_UNIT_ID: the same with ad unit id which you use it to load ad
// playLoadingListener: you can get show events from the object's callbacks
mRewardVideo.presentPlayableAD(AD_UNIT_ID, playLoadingListener)
```
PlayLoadingListener definition
```java
interface PlayLoadingListener {
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

### 3.2.3 other methods
```java
// The SDK automatically loads the next advertisement after displayed an ad by default. This method can be used to disable automatic loading of the next advertisement.
void setAutoLoadAd(boolean)
// This method can determin whether an add has been loaded.
boolean canPresentAd(AD_UNIT_ID)
// Destroy ad object
void destroy()
```

The [RewardVideoSample](./app/src/main/java/com/zplay/playable/playableadsdemo/sample/RewardVideoSample.java) file is the Reward Video sample code.

## 3.3 Interstitial
### 3.3.1 Initialization and Request
```java
// Create interstitial object
// context: activity or context
// APP_ID: application ID
mInterstitial = PlayableInterstitial.init(context, APP_ID)

// Request ad
// AD_UNIT_ID: ad unit id
// playPreloadingListener: you can get request events from the object's callbacks
mInterstitial.requestPlayableAds(AD_UNIT_ID, playPreloadingListener)
```

PlayPreloadingListener definition
```java
interface PlayPreloadingListener {
    // Interstitial has been loaded
    void onLoadFinished();
    // Fail to load an ad
    void onLoadFailed(int errorCode, String msg);
}
```

### 3.3.2 Show
```java
// Show ad
// AD_UNIT_ID: the same with ad unit id which you use it to load ad
// playLoadingListener: you can get request events from the object's callbacks
// To pre-load an ad may take several seconds, so it's recommended to initialize the SDK and load ads as early as possible. 
mInterstitial.presentPlayableAd(AD_UNIT_ID, playLoadingListener)
```
PlayLoadingListener definition
```java
interface PlayLoadingListener {
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

### 3.3.3 Other methods

```java
// The SDK automatically loads the next advertisement after displayed an ad by default. 
// This method can be used to disable automatic loading of the next advertisement.
void setAutoload(boolean)
// This method can determin whether an add has been loaded.
boolean canPresentAd(AD_UNIT_ID)
// Destroy ad object
void destroy()
```

The [InterstitialSample](./app/src/main/java/com/zplay/playable/playableadsdemo/sample/InterstitialSample.java) file is the sample code for interstitial.

## 3.4 Native Ad

When you integrate native ad, you can choose Managed Rendering or Self Rendering. 

### 3.4.1 Integrate Native Ad (Managed Rendering)

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

### 3.4.2 Integrate Native Ad (Self Rendering)

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

# 5 Others
## 5.1 Sets proguard file
If the project need to be proguarded, put the following code into the proguard.pro file.
```
# ZPLAYAds
-keep class com.playableads.**{*;}
```

## 5.2 State Code and Description

| state code | description                    | notes                                                                                                                                |
| ---------- | ------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------ |
| 1001       | request constructed error      | error occurrs while constructing the request parameter, unable to get device token(any of adversting_id, android_id, imei, mac_addr) |
| 1002       | request parameters error       | request parameter does not match, please check whether APP_ID, UNIT_ID has been passed correctly                                     |
| 1003       | lack of WRITE_EXTERNAL_STORAGE | unable to write file to device, unable to cache advertising material                                                                 |
| 2002       | preload finished               | ad had been preloaded                                                                                                                |
| 2004       | ads has filled                 | ad is loading or has been loaded                                                                                                     |
| 2005       | no ad                          | no ad returned from server                                                                                                           |
| 2006       | no connection error            | can not connect to the internet, check the internet connection                                                                       |
| 2007       | timeout error                  | network connection timeout, check network or switch network and retry request                                                        |
| 2008       | server error                   | an exception may occur in advertisement service, please try again later                                                              |
| 5001       | context is null                | context is null, please check whether context has been passed correctly                                                              |
| 5002       | network error                  | network error                                                                                                                        |

## 5.3 FAQ

If you have any problems during the process of integration, or if you found any issue in the SDK, please feel free to ask questions and [push an issue](https://github.com/zplayads/PlayableAdsDemo-android/issues/new?title=[Describe%20the%20issue%20briefly]&body=Write%20here%20with%20the%20detail%20message%20of%20the%20issue.%20If%20you%20have%20any%20error%20log%20about%20the%20issue,%20please%20attach%20here,%20too.%20Thanks%20a%20lot%20)

# 6 Test
You can use the following testing id when you are testing. Testing id won't generate revenue. Please use official id when you release your App.

| AD_TYPE                  | APP_ID                               | AD_UNIT_ID                           |
| ------------------------ | ------------------------------------ | ------------------------------------ |
| Banner                   | 5C5419C7-A2DE-88BC-A311-C3E7A646F6AF | F22F347B-3D57-0C70-0B13-EFCFDF402EBA |
| Reward Video             | 5C5419C7-A2DE-88BC-A311-C3E7A646F6AF | 3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC |
| Interstitial             | 5C5419C7-A2DE-88BC-A311-C3E7A646F6AF | 19393189-C4EB-3886-60B9-13B39407064E |
| Native Managed Rendering | 5C5419C7-A2DE-88BC-A311-C3E7A646F6AF | 0246FB55-3042-9F29-D4AB-21C6349EEE83 |
| Native Self Rendering    | 5C5419C7-A2DE-88BC-A311-C3E7A646F6AF | BB8452AD-06E7-140B-00DC-FD6CB6B40FAA |
