# 1 Overview


## 1.1 Introduction
Integration of this SDK gives the publisher the capability to call playable ads from ZPLAY Ads into available interstitial Android inventory globally.  This guide is designed for developers who are going to integrate the ZPLAY Ads SDK into their Android Apps via Androidstudio.  Please contact support@zplayads.com if you need any assistance in this work.

## 1.2 Development Environment
- OS：WinAll, Linux, Mac
- IDE：Android Studio 2.x
- Deploy Target：Android 4.0 and above

## 1.3 ZPLAY Ads Account Requirements
An account is required on our platform before SDK integration can be completed.  The following App specific data items are the minimum needed to proceed.

APPID: An ID for your App, obtained when setting up the App for monetization within your account on the ZPLAY Ads website.

adUnitID: An ID for a specific ad placement within your App, as generated for your Apps within your account on the ZPLAY Ads website. 

# 2 SDK Integration


## 2.1 Add dependence
Add following code in build.gradle file of project
```
dependencies {
    compile 'com.playableads:playableads:1.0.6'
    compile 'com.google.android.gms:play-services-ads:11.0.4'
}
```

## 2.2 Update project
Click "Sync Project with Gradle Files" button on menu bar to download dependence

# 3 Access Code
## 3.1 Initialize SDK
When you initialize the SDK, you need to provide your APPID and adUnitID (as previously registered on en.zplayads.com) into the marked places. 

Call the method below  to initialize SDK
```
PlayableAds.init(context, APPID, adUnitID)
```
## 3.2 Request Ad
To pre-load an ad may take several seconds, so it's recommended to initialize the SDK and load ads as early as possible. 

Call the following method to pre-load playable ad

```
PlayableAds.getInstance().requestPlayableAds(playPreloadingListener)
```
## 3.3 Ad ready for display?

You can judge the availability of an ad by this callback. Then you'll be able to manage your game's setting according to the ad being ready or not.
```
public interface PlayPreloadingListener {
    // Ad preload successfully
    void onLoadFinished();
    // Ad preload failed
    void onLoadFailed(int errorCode, String msg);
}
```
Code Sample：

```
PlayableAds.getInstance().requestPlayableAds(new PlayPreloadingListener() {
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
## 3.3 Show Ads
When an ad is ready to display, you can show it using following method.

```
PlayableAds.getInstance().presentPlayableAD(this, playLoadingListener)
```

## 3.4 Obtain Reward
To use ZPLAY Ads as a rewarded ad, it’s very important to give the reward properly. To do so, please use the following callback code. 

```
public interface PlayLoadingListener {
    // Ad impression success, use this to judge if the reward should be given.
    void playableAdsIncentive();
    // Ad impression fail,locate problem according error code and error messages.
    void onAdsError(int code, String msg);
}
```
Code Sample:
```
PlayableAds.getInstance().presentPlayableAD(activity, new PlayLoadingListener() {
    @Override
    public void playableAdsIncentive() {
        // Ad impression success, use this to judge if the reward should be given.
    }

    @Override
    public void onAdsError(int errorCode, String message) {
        // Ad impression fail,locate problem according error code and error messages.
    }
});
```


# 4 Proguard
If the project need to be proguarded, put the following code into the proguard.pro file or a custom file.
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

# 5 Code Sample
Click [HERE](https://github.com/yumimobi/PlayableAdsDemo-android.git) to download Demo
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

## Tips
* The Android SDK can only request one ad at a time and does not surpport batch requests. 
* Each ad can be shown once， the ad needs to be requested after being shown.
* To pre-load an ad may take several seconds, so it's recommended to initialize the SDK and load ads as early as possible. 
* To ensure adequate advertising， please sure device phone permission and storage permissions.
