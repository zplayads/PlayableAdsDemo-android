# 1 概述


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
    compile 'com.playableads:playableads:2.0.7'
    
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

|广告形式|  App_ID  |  Ad_Unit_id|
|--------|----------|------------|
|激励视频|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC|
|插屏广告|5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|19393189-C4EB-3886-60B9-13B39407064E|

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
PlayableAds.getInstance().requestPlayableAds("3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC", new PlayPreloadingListener() {
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
    // 可玩广告开始播放
    void onVideoStart();

    // 可玩广告播放完成，展示落地页
    void onVideoFinished();

    // 广告展示完毕，此时可给用户下发奖励
    // 若您的广告位是插屏广告形式，不会执行此回调
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
PlayableAds.getInstance().presentPlayableAD("3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC", new PlayLoadingListener() {

    @Override
    public void playableAdsIncentive() {
        // 广告展示完成，回到原页面，此时可以给用户奖励了。
        // 若您的广告位是插屏广告形式，不会执行此回调
    }

    @Override
    public void onAdsError(int errorCode, String message) {
        // 广告展示失败，根据错误码和错误信息定位问题
    }
});
```

## 3.4 其它方法说明

```void setAutoLoadAd(boolean)```SDK默认初次请求展示完毕后，自动加载下一条广告，可以通过该方法关闭自动加载下一条广告功能。

```boolean canPresentAd(adUnitId)``` 通过该方法判断此广告位是否有可展示的广告

# 4 参数配置
如果项目做混淆，请将以下代码放到proguard-rules.pro文件或自定义文件中
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

## * 状态码及含意

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


在接入过程中如果遇到问题，或者可玩SDK有什么不足之处，[欢迎提issue](https://github.com/zplayads/PlayableAdsDemo-android/issues/new?title=%5B%E7%AE%80%E5%8D%95%E6%8F%8F%E8%BF%B0%E4%B8%80%E4%B8%8B%E8%A6%81%E6%B1%87%E6%8A%A5%E7%9A%84%E9%97%AE%E9%A2%98%5D&body=%E8%AF%B7%E4%BF%AE%E6%94%B9%E4%B8%8A%E6%96%B9%E7%9A%84%E6%A0%87%E9%A2%98%E6%9D%A5%E7%AE%80%E8%A6%81%E6%8F%8F%E8%BF%B0%E8%A6%81%E6%B1%87%E6%8A%A5%E7%9A%84%E9%97%AE%E9%A2%98%EF%BC%8C%E5%B9%B6%E6%8A%8A%E8%AF%A6%E7%BB%86%E7%9A%84%E5%86%85%E5%AE%B9%E5%86%99%E5%9C%A8%E8%BF%99%E9%87%8C%EF%BC%8C%E5%A6%82%E6%9E%9C%E5%8F%AF%E8%83%BD%E7%9A%84%E8%AF%9D%E8%AF%B7%E9%99%84%E4%B8%8A%E6%BA%90%E4%BB%A3%E7%A0%81)，我们会在第一时间处理您提出的问题，万分感谢。
