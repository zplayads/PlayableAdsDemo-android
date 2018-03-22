# 1 概述 v2.0.2


## 1.1 面向读者
本产品面向需要在Android Studio中接入ZPLAYAds SDK的Android开发人员

## 1.2 开发环境
- 操作系统：WinAll, Linux, Mac
- 开发环境：Android Studio 2.x
- 部署目标：Android 4.0及以上

## 1.3 术语介绍
APPID: 应用广告，是您在ZPLAYAds平台创建媒体时获取的ID；

adUnitID: 广告位ID，是ZPLAYAds平台为您的应用创建的广告位置的ID。

# 2 SDK接入
## 2.1 添加依赖
在app项目的build.gradle中添加以下代码
```
dependencies {
    compile 'com.playableads:playableads:2.0.2'
    
    // 可选依赖
    compile 'com.google.android.gms:play-services-ads:11.0.4'
}
```

## 2.2 同步项目
点击菜单栏“同步”(Sync Project with Gradle Files)按钮，下载依赖

# 3 代码接入
## 3.1 初始化SDK
调用```PlayableAds.init(context, APPID)```代码初始化SDK

注：您在测试中可使用如下id进行测试，测试id不会产生收益，应用上线时请使用您申请的正式id。

|操作系统|  App_ID  |  Ad_Unit_id|
|--------|----------|------------|
|Android |5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC|
## 3.2 请求广告
调用```PlayableAds.getInstance().requestPlayableAds(adUnitId, playPreloadingListener)```加载广告，listener回调方法说明：
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
PlayableAds.getInstance().requestPlayableAds("androidDemoAdUnit", new PlayPreloadingListener() {
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

## 3.3 展示广告
调用```PlayableAds.getInstance().presentPlayableAD(adUnitId, playLoadingListener)```展示广告，listener回调方法说明：
```
public interface PlayLoadingListener {
    // 完成整个广告事务（游戏展示，游戏试玩和落地页退出）后的回调，表示可以下发奖励
    void playableAdsIncentive();
    // 展示过程中出现错误
    void onAdsError(int code, String msg);
}
```
展示示例：
```
PlayableAds.getInstance().presentPlayableAD("androidDemoAdUnit", new PlayLoadingListener() {
    @Override
    public void playableAdsIncentive() {
        // 广告展示完成，回到原页面，此时可以给用户奖励了。
    }

    @Override
    public void onAdsError(int errorCode, String message) {
        // 广告展示失败，根据错误码和错误信息定位问题
    }
});
```

# 4 混淆处理
如果项目做混淆，请将以下代码放到proguard-rules.pro文件或自定义文件中
```
# playableAds
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
    public void onDestroy();
    public static com.playableads.PlayableAds getInstance();
    public void requestPlayableAds(com.playableads.PlayPreloadingListener, java.lang.String);
    public void requestPlayableAds(java.lang.String, com.playableads.PlayPreloadingListener);
    public synchronized static com.playableads.PlayableAds init(android.content.Context, java.lang.String);
    public void presentPlayableAD(java.lang.String, com.playableads.PlayLoadingListener);
    public void presentPlayableAd(com.playableads.PlayLoadingListener);
    public boolean canPresentAd(java.lang.String);
    public void setMultiLoadingListener(com.playableads.MultiPlayLoadingListener);
    public void setMultiPreloadingListener(com.playableads.MultiPlayPreloadingListener);
    public void setCacheCountPerUnitId(int);
    public void setAutoLoadAd(boolean);
}
```

# 5 补充说明

## 5.1 尽早请求广告
由于广告资源较大（每条5到8M），请尽可能早的请求广告。

## 5.2 设备权限
请保证应用有电话权限、存储权限，否则可能出现一直没有广告的状态。

## 5.3 自动请求广告
SDK默认初次请求展示完毕后，自动加载下一条广告，可以通过```PlayableAds.getInstance().setAutoLoadAd(false)```关闭自动加载功能。

## 5.4 请求多次广告
可以通过```PlayableAds.getInstance().setCacheCountPerUnitId(cnt)```设置一个广告位可以提前缓存多个广告，该缓存一天内有效。

## 5.5 状态码及含意

|状态码|描述|补充|
|-----|----|---|
|1001|request constructed error|构建请求参数时出错，导致参数缺失|
|1002|request parameters error.|请求参数不匹配，如没有imei号、系统版过低等|
|1003|lack of WRITE_EXTERNAL_STORAGE|缺少存储卡权限|
|1004|lack of READ_PHONE_STATE|缺少手机状态权限|
|2001|payload has been loaded|广告已经加载完成，此时可以展示广告了|
|2002|preload finished|广告预加载完成|
|2004|ads has filled|广告已经在加载或已经加载完成|
|2005|no ad|服务器无广告返回|
|5001|context is null|初始化时传入的context为空|
|5002|network error|网络错误|

