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
    compile 'com.playableads:playableads:1.0.6'
    compile 'com.google.android.gms:play-services-ads:11.0.4'
}
```

## 2.2 同步项目
点击菜单栏“同步”(Sync Project with Gradle Files)按钮，下载依赖

# 3 代码接入
## 3.1 初始化SDK
调用```PlayableAds.init(context, APPID, adUnitID)```代码初始化SDK
## 3.2 请求广告
调用```PlayableAds.getInstance().requestPlayableAds(playPreloadingListener)```加载广告，listener回调方法说明：
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
PlayableAds.getInstance().requestPlayableAds(new PlayPreloadingListener() {
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
调用```PlayableAds.getInstance().presentPlayableAD(this, playLoadingListener)```展示广告，listener回调方法说明：
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
PlayableAds.getInstance().presentPlayableAD(activity, new PlayLoadingListener() {
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
-keep class com.playableads.PlayableAds {
    public void onDestroy();
    public static com.playableads.PlayableAds getInstance();
    public void requestPlayableAds(com.playableads.PlayPreloadingListener);
    public void requestPlayableAds(java.lang.String, com.playableads.PlayPreloadingListener);
    public synchronized static com.playableads.PlayableAds init(android.content.Context, java.lang.String, java.lang.String);
    public void presentPlayableAD(android.content.Context, com.playableads.PlayLoadingListener);
    public boolean canPresentAd();
}
```

# 5 代码示例
[这里](https://github.com/yumimobi/PlayableAdsDemo-android.git)是完整Demo
```
public class MainActivity extends Activity {
    private static final int REQUEST_CODE = 2;
    private TextView info;
    private EditText mAppIdEdit;
    private EditText mUnitIdEdit;
    private ScrollView mScrollView;
    PlayableAds mAds;
    private View mPresentView;
    private View mRequestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.text);
        mAppIdEdit = (EditText) findViewById(R.id.appId);
        mUnitIdEdit = (EditText) findViewById(R.id.unitId);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mRequestView = findViewById(R.id.request);
        mPresentView = findViewById(R.id.present);
        mPresentView.setEnabled(false);

        mAds = PlayableAds.init(this, "androidDemoApp", "androidDemoAdUnit");
    }

    public void request(View view) {
        mRequestView.setEnabled(false);
        mPresentView.setEnabled(false);
        checkWritePermission();

        String appId = mAppIdEdit.getText().toString();
        String unitId = mUnitIdEdit.getText().toString();
        if (!TextUtils.isEmpty(appId) && !TextUtils.isEmpty(unitId)) {
            mAds = PlayableAds.init(this, appId, unitId);
        }

        mAds.requestPlayableAds(mPreloadingListener);
        setInfo(getString(R.string.start_request));
    }

    private PlayPreloadingListener mPreloadingListener = new PlayPreloadingListener() {

        @Override
        public void onLoadFinished() {
            setInfo(getString(R.string.pre_cache_finished));
            mPresentView.setEnabled(true);
            mRequestView.setEnabled(true);
        }

        @Override
        public void onLoadFailed(int errorCode, String msg) {
            setInfo(String.format(getString(R.string.load_failed), errorCode, msg));
            mRequestView.setEnabled(true);
        }
    };

    public void present(View view) {
        if (!mAds.canPresentAd()) {
            Toast.makeText(this, R.string.loading_ad, Toast.LENGTH_SHORT).show();
            return;
        }
        mAds.presentPlayableAD(this, new PlayLoadingListener() {
            @Override
            public void playableAdsIncentive() {
                setInfo(getString(R.string.ads_incentive));
                mPresentView.setEnabled(false);
                mRequestView.setEnabled(true);
            }

            @Override
            public void onAdsError(int code, String msg) {
                setInfo(getString(R.string.ads_error, code, msg));
            }
        });
    }

    private void setInfo(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (info != null) {
                    info.append(msg + "\n\n");
                }
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAds.onDestroy();
    }

    private void checkWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                setInfo(getString(R.string.open_write_permission));
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                setInfo(getString(R.string.open_phone_permission));
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
            }
        }
    }
}

```

## 补充说明
* android端暂时只能请求一个广告，不能批量请求。
* 每次广告请求只能展示一次，展示完成后需要重新请求广告
* 由于广告资源较大，请尽可能早的请求广告。
* 请保证应用有电话权限、存储权限，否则可能出现一直没有广告的状态。