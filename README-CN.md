* [1 概述](#1-概述)
    * [1.1 面向读者](#11-面向读者)
    * [1.2 开发环境](#12-开发环境)
    * [1.3 术语介绍](#13-术语介绍)
* [2 SDK接入](#2-sdk接入)
    * [2.1 Android Studio (推荐)](#21-android-studio-推荐)
        * [2.1.1 添加依赖](#211-添加依赖)
        * [2.1.2 同步项目](#212-同步项目)
    * [2.2 Eclipse](#22-eclipse)
        * [2.2.1 导入 SDK jar 文件](#221-导入-sdk-jar-文件)
        * [2.2.2 注册 ZPLAYAds SDK 组件](#222-注册-zplayads-sdk-组件)
* [3 代码接入](#3-代码接入)
    * [3.1 激励视频](#31-激励视频)
        * [3.1.1 初始化激励视频SDK](#311-初始化激励视频sdk)
        * [3.1.2 请求广告](#312-请求广告)
        * [3.1.3 展示广告](#313-展示广告)
        * [3.1.4 其它方法说明](#314-其它方法说明)
    * [3.2 插屏广告](#32-插屏广告)
        * [3.2.1 初始化插屏SDK](#321-初始化插屏sdk)
        * [3.2.2 请求广告](#322-请求广告)
        * [3.2.3 展示广告](#323-展示广告)
        * [3.2.4 其它方法说明](#324-其它方法说明)
    * [3.3 可玩原生](#33-可玩原生)
        * [3.3.1 原生广告接入（托管渲染）](#331-原生广告接入托管渲染)
        * [3.3.2 原生广告接入（自渲染）](#332-原生广告接入自渲染)
* [4 其它](#4-其它)
    * [4.1 混淆设置](#41-混淆设置)
    * [4.2 状态码及含意](#42-状态码及含意)
    * [4.3 FAQ](#43-faq)
* [5 测试](#5-测试)    
 

# 1 概述

## 1.1 面向读者
本产品面向需要在Android中接入ZPLAYAds SDK的Android开发人员

## 1.2 开发环境
- 操作系统：WinAll, Linux, Mac
- 开发环境：Android Studio 2.0及以上
- 部署目标：Android 4.0及以上

## 1.3 术语介绍
APP_ID: 应用广告，是您在ZPLAYAds平台创建媒体时获取的ID；

AD_UNIT_ID: 广告位ID，是ZPLAYAds平台为您的应用创建的广告位置的ID。

# 2 SDK接入
## 2.1 Android Studio (推荐)
### 2.1.1 添加依赖
在app项目的build.gradle中添加以下代码
```
dependencies {
    compile 'com.playableads:playableads:2.3.1'
    
    // 可选依赖
    compile 'com.google.android.gms:play-services-ads:10.0.1'
}
```

### 2.1.2 同步项目
点击菜单栏“同步”(Sync Project with Gradle Files)按钮，下载依赖

## 2.2 Eclipse 
### 2.2.1 导入 SDK jar 文件
将 [zplayads.jar](https://github.com/zplayads/PlayableAdsDemo-android/raw/master/eclipseJar/zplayads-2.3.1.jar) 放到Eclipse 项目 libs 文件夹下，并添加到 build path。添加 build path 步骤如下：
1. 在Eclipse 中右击项目，选择 Build Path -> Configure Build Path... 弹出 java Build Path 窗口；
2. 选择 Libraries 标签，点击 Add JARs... 按钮；
3. 选择下载好的 jar 文件，完成导入。

注：zplayads.jar此文件在Eclipse环境中可直接使用，若您开发环境为Android Studio请参考2.1接入方法。

### 2.2.2 注册 ZPLAYAds SDK 组件
向 AndroidManifest.xml 中注册 ZPLAYAds SDK 需要的组件
1. 权限
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
```
2. activity 与 receiver
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

# 3 代码接入
注：您在测试中可使用如下id进行测试，测试id不会产生收益，应用上线时请使用您申请的正式id。

|广告形式|  APP_ID  |  AD_UNIT_ID|
|--------|----------|------------|
|激励视频|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC|
|插屏广告|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|19393189-C4EB-3886-60B9-13B39407064E|
|原生托管渲染|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|0246FB55-3042-9F29-D4AB-21C6349EEE83|
|原生自渲染|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|BB8452AD-06E7-140B-00DC-FD6CB6B40FAA|
## 3.1 激励视频
### 3.1.1 初始化激励视频SDK
调用```PlayableAds.init(context, APP_ID)```代码初始化激励视频SDK，如
```
PlayableAds mRewardVideo = PlayableAds.init(context, "5C5419C7-A2DE-88BC-A311-C3E7A646F6AF");
```
### 3.1.2 请求广告
调用```mRewardVideo.requestPlayableAds(AD_UNIT_ID, playPreloadingListener)```加载广告，listener回调方法说明：
```
public interface PlayPreloadingListener {
    // 广告加载完成
    void onLoadFinished();
    // 广告加载失败
    void onLoadFailed(int errorCode, String msg);
}
```

请求示例：
```
mRewardVideo.requestPlayableAds("3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC", new PlayPreloadingListener() {
    @Override
    public void onLoadFinished() {
        // 广告加载完成，可以调用presentPlayableAD(...)方法展示广告了
    }

    @Override
    public void onLoadFailed(int errorCode, String message) {
        // 广告加载失败，根据错误码和错误信息定位问题
    }
})
```

### 3.1.3 展示广告
调用```mRewardVideo.presentPlayableAD(AD_UNIT_ID, playLoadingListener)```展示广告，listener回调方法说明：
```
public interface PlayLoadingListener {
    // 可玩广告开始播放
    void onVideoStart();

    // 可玩广告播放完成，展示落地页
    void onVideoFinished();

    // 广告奖励回调，此时可给用户下发奖励
    void playableAdsIncentive();

    // 展示过程中出现错误
    void onAdsError(int code, String msg);

    // 用户点击安装按钮
    void onLandingPageInstallBtnClicked();

    // 整个广告事务完成
    void onAdClosed();
}
```
展示示例：
```
mRewardVideo.presentPlayableAD("3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC", new PlayLoadingListener() {

    @Override
    void onVideoStart(){
        // 可玩广告开始展示，可以在此处处理应用逻辑，比如关闭应用声音，以避免应用声音与广告声音重叠。
    }

    @Override
    public void playableAdsIncentive() {
        // 广告展示完成，回到原页面，此时可以给用户奖励了。
    }

     @Override
    void onAdClosed(){
        // 可玩广告展示结束，可以在此处处理应用逻辑，比如打开应用声音。
    }

});
```

### 3.1.4 其它方法说明

```void setAutoLoadAd(boolean)```SDK默认初次请求展示完毕后，自动加载下一条广告，可以通过该方法关闭自动加载下一条广告功能。

```boolean canPresentAd(AD_UNIT_ID)``` 通过该方法判断此广告位是否有可展示的广告

完整代码示例请参考[PlayableAdSample](./app/src/main/java/com/zplay/playable/playableadsdemo/sample/PlayableAdSample.java)

## 3.2 插屏广告
### 3.2.1 初始化插屏SDK
调用```PlayableInterstitial.init(context, APP_ID)```代码初始化插屏SDK
如
```
PlayableInterstitial mInterstitial = PlayableInterstitial.init(context, "5C5419C7-A2DE-88BC-A311-C3E7A646F6AF");
```
### 3.2.2 请求广告
调用```mInterstitial.requestPlayableAds(AD_UNIT_ID, playPreloadingListener)```加载广告，listener回调方法说明：
```
public interface PlayPreloadingListener {
    // 广告加载完成
    void onLoadFinished();
    // 广告加载失败
    void onLoadFailed(int errorCode, String msg);
}
```

请求示例：
```
mInterstitial.requestPlayableAds("19393189-C4EB-3886-60B9-13B39407064E", new PlayPreloadingListener() {
    @Override
    public void onLoadFinished() {
        // 广告加载完成，可以调用presentPlayableAd(...)方法展示广告了
    }

    @Override
    public void onLoadFailed(int errorCode, String message) {
        // 广告加载失败，根据错误码和错误信息定位问题
    }
})
```

### 3.2.3 展示广告
调用```mInterstitial.presentPlayableAd(AD_UNIT_ID, playLoadingListener)```展示广告，listener回调方法说明：
```
public interface PlayLoadingListener {
    // 可玩广告开始播放
    void onVideoStart();

    // 可玩广告播放完成，展示落地页
    void onVideoFinished();

    // 注意: 插屏广告不触发此回调
    void playableAdsIncentive();

    // 展示过程中出现错误
    void onAdsError(int code, String msg);

    // 用户点击安装按钮
    void onLandingPageInstallBtnClicked();

    // 整个广告事务完成
    void onAdClosed();
}
```
展示示例：
```
mInterstitial.presentPlayableAd("3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC", new PlayLoadingListener() {
    @Override
    void onVideoStart(){
        // 可玩广告开始展示，可以在此处处理应用逻辑，比如关闭应用声音，以避免应用声音与广告声音重叠。
    }

    @Override
    void onAdClosed(){
        // 可玩广告展示结束，可以在此处处理应用逻辑，比如打开应用声音。
    }

    ...
});
```

### 3.2.4 其它方法说明

```void setAutoload(boolean)```SDK默认初次请求展示完毕后，自动加载下一条广告，可以通过该方法关闭自动加载下一条广告功能。

```boolean canPresentAd(AD_UNIT_ID)``` 通过该方法判断此广告位是否有可展示的广告

完整代码示例请参考[InterstitialSample](./app/src/main/java/com/zplay/playable/playableadsdemo/sample/InterstitialSample.java)

## 3.3 可玩原生

您接入原生广告时可以选择接入托管渲染或者自渲染

### 3.3.1 原生广告接入（托管渲染）

> 托管渲染是ZPLAY Ads推出的自动渲染广告样式的原生广告。此种方式简化了原生广告的接入流程，您无需处理广告渲染相关事宜，使得原生广告的接入更加便捷。

a. 初始化
```
PlayableNativeExpressAd mPlayableNativeAd = new PlayableNativeExpressAd(mContext, APP_ID, AD_UNIT_ID)
```
设置加载广告监听事件
```
mPlayableNativeAd.setNativeAdLoadListener(new NativeAdLoadListener() {
    @Override
    public void onNativeAdLoaded(NativeAd nativeAd) {
        // 广告请求成功，将nativeAd放入Adapter data中备用
    }

    @Override
    public void onNativeAdFailed(int errorCode, String message) {
        // 广告请求失败，可根据errorCode查看本文档“状态码及含义”部分，以便快速定位问题
    }
});
```

b. 创建模板布局

以RecyclerView为信息流载体为例，创建NativeTemplateViewHolder itemView布局如下：
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

c. 加载广告

调用此方法```mPlayableNativeAd.loadAd()```进行广告加载

d. 渲染广告

在RecyclerView Adapter中，执行此回调```onBindViewHolder(ViewHolder holder, int position)```
```
NativeAd nativeAd = mNativeAds.get(position);
if (nativeAd != null) {
    nativeAd.renderAdView(nativeTemplateViewHolder.nativeTemplateView);
}
```
e. 广告展示或点击的监听回调

```
nativeAd.setNativeEventListener(new NativeEventListener() {
    @Override
    public void onAdImpressed(View view) {
        // 广告被展示
    }
    @Override
    public void onAdClicked(View view) {
        // 广告被点击
    }
});
```

完整代码示例请参考[NativeAdRecyclerViewSample](./app/src/main/java/com/zplay/playable/playableadsdemo/sample/NativeAdRecyclerViewSample.java)

### 3.3.2 原生广告接入（自渲染）

>原生自渲染广告是ZPLAY Ads推出的一种高度灵活的原生广告。您可根据自己的需求自行拼接广告样式，使广告展示更契合您的应用。

a. 初始化

```
PlayableNativeAd mPlayableNativeAd = new PlayableNativeAd(mContext, APP_ID, AD_UNIT_ID)
```

b. 添加NativeAdRender用以设置广告布局

在自定义布局中，包含以下元素

    - mainImageId: 用来显示广告大图ImageView的id
    - iconImageId: 用来显示广告iconImageView的id
    - titleId: 用来显示广告标题TextView的id
    - textId: 用来显示广告描述TextView的id
    - buttonId: 用来显示“免安装试玩”Button的id
    - playerId: 用来播放原生视频广告VideoView的id
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
**注意：** 原生自渲染广告必须设置Render类，否则广告无法正常显示

c. 添加请求监听方法及创建广告View
```
 mPlayableNativeAd.setNativeAdLoadListener(new NativeAdLoadListener() {
    @Override
    public void onNativeAdLoaded(NativeAd nativeAd) {
        // 已请求到广告之后，创建广告View，mNativeView为广告View的父容器，如示例Demo中的LinearLayout
        View view = nativeAd.createAdView(YourActivity.this, mNativeView);
        nativeAd.renderAdView(view);
        mNativeView.addView(view);
    }

    @Override
    public void onNativeAdFailed(int errorCode, String message) {
        // 广告请求失败，可根据errorCode查看文档以便快速定位问题
    }
});
```
d. 添加展示监听方法（可选）

**如有需要**，可以添加广告展示或点击的监听回调，如下：
```
nativeAd.setNativeEventListener(new NativeEventListener() {
    @Override
    public void onAdImpressed(View view) {
        // 广告被展示
    }

    @Override
    public void onAdClicked(View view) {
        // 广告被点击
    }
});
```
e. 请求广告
```
mPlayableNativeAd.loadAd()
```

完整代码示例请参考[NativeAdSample](./app/src/main/java/com/zplay/playable/playableadsdemo/sample/NativeAdSample.java)

# 4 其它
## 4.1 混淆设置
如果项目做混淆，请将以下代码放到proguard-rules.pro文件
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

## 4.2 状态码及含意

|状态码|描述|补充|
|-----|----|---|
|1001|request constructed error|构建请求参数时出错，无法获取设备token(adversting_id, android_id, imei, mac_addr其中的任意一个)|
|1002|request parameters error|请求参数不匹配，检查传入的APP_ID, UNIT_ID是否正确|
|1003|lack of WRITE_EXTERNAL_STORAGE|无法向设备写入文件，无法缓存广告物料|
|2002|preload finished|广告预加载完成|
|2004|ads has filled|广告已经在加载或已经加载完成|
|2005|no ad|服务器无广告返回|
|2006|no connection error|无法连接网络，检查联网是否正常|
|2007|timeout error|网络连接超时，检查网络或切换网络再重试请求|
|2008|server error|广告服务出现异常，请稍后重试|
|5001|context is null|context为空，检查是否正确传入context值|
|5002|network error|网络错误|

## 4.3 FAQ
在接入过程中如果遇到问题，或者可玩SDK有什么不足之处，[欢迎提issue](https://github.com/zplayads/PlayableAdsDemo-android/issues/new?title=%5B%E7%AE%80%E5%8D%95%E6%8F%8F%E8%BF%B0%E4%B8%80%E4%B8%8B%E8%A6%81%E6%B1%87%E6%8A%A5%E7%9A%84%E9%97%AE%E9%A2%98%5D&body=%E8%AF%B7%E4%BF%AE%E6%94%B9%E4%B8%8A%E6%96%B9%E7%9A%84%E6%A0%87%E9%A2%98%E6%9D%A5%E7%AE%80%E8%A6%81%E6%8F%8F%E8%BF%B0%E8%A6%81%E6%B1%87%E6%8A%A5%E7%9A%84%E9%97%AE%E9%A2%98%EF%BC%8C%E5%B9%B6%E6%8A%8A%E8%AF%A6%E7%BB%86%E7%9A%84%E5%86%85%E5%AE%B9%E5%86%99%E5%9C%A8%E8%BF%99%E9%87%8C%EF%BC%8C%E5%A6%82%E6%9E%9C%E5%8F%AF%E8%83%BD%E7%9A%84%E8%AF%9D%E8%AF%B7%E9%99%84%E4%B8%8A%E9%94%99%E8%AF%AF%E6%97%A5%E5%BF%97)，我们会在第一时间处理您提出的问题，万分感谢。

# 5 测试

您在测试中可使用如下id进行测试，测试id不会产生收益，应用上线时请使用您申请的正式id。

|广告形式|  APP_ID  |  AD_UNIT_ID|
|--------|----------|------------|
|激励视频|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC|
|插屏广告|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|19393189-C4EB-3886-60B9-13B39407064E|
|原生托管渲染|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|0246FB55-3042-9F29-D4AB-21C6349EEE83|
|原生自渲染|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|BB8452AD-06E7-140B-00DC-FD6CB6B40FAA|
