package com.zplay.playable.playableadsdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.playableads.PlayPreloadingListener;
import com.playableads.PlayableAds;
import com.playableads.SimplePlayLoadingListener;


public class MainActivity extends Activity {
    private static final String APP_ID = "androidDemoApp";
    private static final String AD_UNIT_ID = "androidDemoAdUnit";
    private TextView info;
    private EditText mUnitIdEdit;
    private ScrollView mScrollView;
    PlayableAds mAds;
    private String mUnitId = AD_UNIT_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = findViewById(R.id.text);
        mUnitIdEdit = findViewById(R.id.unitId);
        mScrollView = findViewById(R.id.scrollView);

        // 务必进行初始化，将androidDemoApp与androidDemoAdUnit替换为通过审核的appId和广告位Id
        mAds = PlayableAds.init(this, APP_ID);
        mAds.setCacheCountPerUnitId(1);

        // 模拟多个广告位
        mUnitIdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String unitId = s.toString().trim();
                if (TextUtils.isEmpty(unitId)) {
                    mUnitId = AD_UNIT_ID;
                } else {
                    mUnitId = unitId;
                }
            }
        });
    }

    // 请求广告
    public void request(View view) {
        checkWritePermission();

        String unitId = mUnitIdEdit.getText().toString();

        if (!TextUtils.isEmpty(unitId)) {
            mUnitId = unitId;
        }

        // 尽可能早的请求广告，每次广告展示成功后必须重新执行该方法请求下一个广告
        mAds.requestPlayableAds(mUnitId, mPreloadingListener);
        setInfo(getString(R.string.start_request));
    }

    private PlayPreloadingListener mPreloadingListener = new PlayPreloadingListener() {

        @Override
        public void onLoadFinished() {
            // 广告加载完成，此时您可以展示广告了
            setInfo(getString(R.string.pre_cache_finished));
        }

        @Override
        public void onLoadFailed(int errorCode, String msg) {
            // 广告加载失败，请根据错误码和错误信息定位问题
            setInfo(String.format(getString(R.string.load_failed), errorCode, msg));
        }
    };

    public void present(View view) {
        // 调用该方法展示广告，请确保广告加载完成，否则展示失败
        mAds.presentPlayableAD(mUnitId, new SimplePlayLoadingListener() {
            @Override
            public void playableAdsIncentive() {
                // 广告正确展示，此时广告已经产生收益，您可以给用户奖励货其他
                setInfo(getString(R.string.ads_incentive));

                // 启动下次请求
                setInfo(mUnitId + " " + getString(R.string.start_request));
                mAds.requestPlayableAds(mUnitId, new PlayPreloadingListener() {
                    @Override
                    public void onLoadFinished() {
                        setInfo(mUnitId + " " + getString(R.string.pre_cache_finished));
                    }

                    @Override
                    public void onLoadFailed(int errorCode, String msg) {
                        setInfo(mUnitId + " " + msg);
                    }
                });
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

