package com.zplay.playable.playableadsdemo.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zplay.playable.playableadsdemo.R;
import com.zplay.playable.playableadsdemo.ToolBarActivity;
import com.zplay.playable.playableadsdemo.util.LogTextView;
import com.zplay.playable.playableadsdemo.util.UserConfig;
import com.playableads.PlayableNativeAd;
import com.playableads.nativead.NativeAd;
import com.playableads.nativead.NativeAdLoadListener;
import com.playableads.nativead.NativeAdRender;
import com.playableads.nativead.NativeEventListener;
import com.playableads.nativead.ViewBinder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.zplay.playable.playableadsdemo.MainActivity.APP_ID;

/**
 * Description: self render native ad sample activity
 * <p>
 * Created by lgd on 2018/9/4.
 */

public class NativeAdSample extends ToolBarActivity {
    private static final String NATIVE_AD_UNIT_ID = "BB8452AD-06E7-140B-00DC-FD6CB6B40FAA";
    private static final String[] sAppAndUnitId = new String[]{APP_ID, NATIVE_AD_UNIT_ID};

    private PlayableNativeAd mPlayableNativeAd;
    @BindView(R.id.amn_native_placer)
    ViewGroup mNativeView;
    @BindView(R.id.amn_log)
    LogTextView mLogView;
    @BindView(R.id.app_id)
    EditText mAppIdEdit;
    @BindView(R.id.clear)
    View mClear;
    @BindView(R.id.unit_id)
    EditText mAdUnitIdEdit;
    @BindView(R.id.clear2)
    View mClear2;

    private NativeAdRender mNativeAdRender;

    private NativeEventListener mNativeEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        ButterKnife.bind(this);

        showUpAction();
        showSettingsButton();

        mAppIdEdit.setText(sAppAndUnitId[0]);
        mAdUnitIdEdit.setText(sAppAndUnitId[1]);

        mNativeEventListener = new NativeEventListener() {
            @Override
            public void onAdImpressed(View view) {
                mLogView.addLog("onAdImpressed");
            }

            @Override
            public void onAdClicked(View view) {
                mLogView.addLog("onAdClicked");
            }
        };

        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_ad_layout)
                .mainImageId(R.id.nal_image)
                .iconImageId(R.id.nal_icon)
                .titleId(R.id.nal_title)
                .textId(R.id.nal_description)
                .actionButtonId(R.id.nal_button)
                .playerId(R.id.nal_player)
                .build();
        mNativeAdRender = new NativeAdRender(viewBinder);

        initNativeAd(sAppAndUnitId[0], sAppAndUnitId[1]);
    }

    private void initNativeAd(String appId, String unitId) {
        mPlayableNativeAd = new PlayableNativeAd(this, appId, unitId);
        mPlayableNativeAd.setChannelId(UserConfig.getInstance(this).getChannelId());
        mPlayableNativeAd.setAdRender(mNativeAdRender);
        mPlayableNativeAd.setNativeAdLoadListener(new NativeAdLoadListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                mLogView.addLog("onNativeAdLoaded");
                View view = nativeAd.createAdView(NativeAdSample.this, mNativeView);
                nativeAd.renderAdView(view);
                nativeAd.setNativeEventListener(mNativeEventListener);
                mNativeView.addView(view);
            }

            @Override
            public void onNativeAdFailed(int errorCode, String message) {
                mLogView.addLog("onNativeAdFailed: " + message);
            }
        });
    }

    @OnClick(R.id.clear)
    void clearAppId() {
        mAppIdEdit.setText("");
        mClear.setVisibility(View.GONE);
    }

    @OnClick(R.id.clear2)
    void clearAdUnitId() {
        mAdUnitIdEdit.setText("");
        mClear2.setVisibility(View.GONE);
    }

    @OnTextChanged(R.id.app_id)
    public void unitAppIdChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            mClear.setVisibility(View.GONE);
        } else {
            mClear.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(s)) {
            return;
        }
        if (TextUtils.isEmpty(s)) {
            sAppAndUnitId[0] = APP_ID;
        } else {
            sAppAndUnitId[0] = s.toString();
        }
        initNativeAd(sAppAndUnitId[0], sAppAndUnitId[1]);
    }

    @OnTextChanged(R.id.unit_id)
    public void appUnitIdChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            mClear2.setVisibility(View.GONE);
        } else {
            mClear2.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(s)) {
            sAppAndUnitId[1] = NATIVE_AD_UNIT_ID;
        } else {
            sAppAndUnitId[1] = s.toString();
        }
        initNativeAd(sAppAndUnitId[0], sAppAndUnitId[1]);
    }

    public void loadAd(View view) {
        mPlayableNativeAd.loadAd();
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, NativeAdSample.class);
        context.startActivity(intent);
    }

    public void clearLog(View view) {
        mLogView.clearLog();
    }
}
