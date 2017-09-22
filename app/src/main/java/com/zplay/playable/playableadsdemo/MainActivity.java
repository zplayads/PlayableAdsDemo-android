package com.zplay.playable.playableadsdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.playableads.PlayLoadingListener;
import com.playableads.PlayPreloadingListener;
import com.playableads.PlayableAds;

public class MainActivity extends Activity {
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
        info = findViewById(R.id.text);
        mAppIdEdit = findViewById(R.id.appId);
        mUnitIdEdit = findViewById(R.id.unitId);
        mScrollView = findViewById(R.id.scrollView);
        mRequestView = findViewById(R.id.request);
        mPresentView = findViewById(R.id.present);
        mPresentView.setEnabled(false);

        // 务必进行初始化
        mAds = PlayableAds.init(this, "androidDemoApp", "androidDemoAdUnit");
    }

    // 请求广告
    public void request(View view) {
        mRequestView.setEnabled(false);
        mPresentView.setEnabled(false);
        checkWritePermission();

        String appId = mAppIdEdit.getText().toString();
        String unitId = mUnitIdEdit.getText().toString();
        if (!TextUtils.isEmpty(appId) && !TextUtils.isEmpty(unitId)) {
            mAds = PlayableAds.init(this, appId, unitId);
        }

        // 尽可能早的请求广告，每次广告展示成功后必须重新执行该方法请求下一个广告
        mAds.requestPlayableAds(mPreloadingListener);
        setInfo(getString(R.string.start_request));
    }

    private PlayPreloadingListener mPreloadingListener = new PlayPreloadingListener() {

        @Override
        public void onLoadFinished() {
            // 广告加载完成，此时您可以展示广告了
            setInfo(getString(R.string.pre_cache_finished));
            mPresentView.setEnabled(true);
            mRequestView.setEnabled(true);
        }

        @Override
        public void onLoadFailed(int errorCode, String msg) {
            // 广告加载失败，请根据错误码和错误信息定位问题
            setInfo(String.format(getString(R.string.load_failed), errorCode, msg));
            mRequestView.setEnabled(true);
        }
    };

    public void present(View view) {
        // 可以使用该方法检测广告是否加载完成
        if (!mAds.canPresentAd()) {
            Toast.makeText(this, R.string.loading_ad, Toast.LENGTH_SHORT).show();
            return;
        }
        // 调用该方法展示广告，请确保广告加载完成，否则展示失败
        mAds.presentPlayableAD(this, new PlayLoadingListener() {
            @Override
            public void playableAdsIncentive() {
                // 广告正确展示，此时广告已经产生收益，您可以给用户奖励货其他
                setInfo(getString(R.string.ads_incentive));
                mPresentView.setEnabled(false);
                mRequestView.setEnabled(true);
            }

            @Override
            public void onAdsError(int errorCode, String msg) {
                // 广告展示失败，请根据错误码和错误信息定位问题
                setInfo(getString(R.string.ads_error, errorCode, msg));
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
        // 请确保有此方法
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

