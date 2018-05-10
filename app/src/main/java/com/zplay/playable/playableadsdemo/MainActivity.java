package com.zplay.playable.playableadsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.playableads.PlayPreloadingListener;
import com.playableads.PlayableAds;
import com.playableads.SimplePlayLoadingListener;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private static final String APP_ID = "5C5419C7-A2DE-88BC-A311-C3E7A646F6AF";
    private static final String AD_UNIT_ID = "3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC";
    private static final String Ad_UNIT_ID_INTERSTITIAL = "19393189-C4EB-3886-60B9-13B39407064E";

    private TextView info;
    private ScrollView mScrollView;

    private PlayableAds mAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = findViewById(R.id.text);
        mScrollView = findViewById(R.id.scrollView);

        mAds = PlayableAds.init(this, APP_ID);

        ((ToggleButton) findViewById(R.id.switchAutoLoad)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAds.setAutoLoadAd(isChecked);
                if (isChecked) {
                    requestAds();
                }
            }
        });
        requestAds();
    }

    private void requestAds() {
        requestRewardedVideo(null);
        requestInterstitial(null);
    }

    public void requestRewardedVideo(View view) {
        mAds.requestPlayableAds(AD_UNIT_ID, new PlayPreloadingListener() {

            @Override
            public void onLoadFinished() {
                setInfo(getString(R.string.pre_cache_finished, getShortId(AD_UNIT_ID)));
            }

            @Override
            public void onLoadFailed(int errorCode, String msg) {
                setInfo(getString(R.string.load_failed, getShortId(AD_UNIT_ID), errorCode, msg));
            }
        });
        setInfo(getString(R.string.start_request, getShortId(AD_UNIT_ID)));
    }

    public void presentRewardedVideo(View view) {
        mAds.presentPlayableAD(AD_UNIT_ID, new SimplePlayLoadingListener() {
            @Override
            public void onVideoStart() {
                setInfo(getString(R.string.ads_start, getShortId(AD_UNIT_ID)));
            }

            @Override
            public void onVideoFinished() {
                setInfo(getString(R.string.ads_video_finished, getShortId(AD_UNIT_ID)));
            }

            @Override
            public void playableAdsIncentive() {
                setInfo(getString(R.string.ads_incentive, getShortId(AD_UNIT_ID)));
            }

            @Override
            public void onLandingPageInstallBtnClicked() {
                setInfo(getString(R.string.ads_install_button_clicked, getShortId(AD_UNIT_ID)));
            }

            @Override
            public void onAdClosed() {
                setInfo(getString(R.string.ads_ad_closed, getShortId(AD_UNIT_ID)));
            }

            @Override
            public void onAdsError(int errorCode, String msg) {
                setInfo(getString(R.string.ads_error, getShortId(AD_UNIT_ID), errorCode, msg));
            }
        });
    }

    public void requestInterstitial(View view) {
        mAds.requestPlayableAds(Ad_UNIT_ID_INTERSTITIAL, new PlayPreloadingListener() {
            @Override
            public void onLoadFinished() {
                setInfo(String.format(getString(R.string.pre_cache_finished), getShortId(Ad_UNIT_ID_INTERSTITIAL)));
            }

            @Override
            public void onLoadFailed(int errorCode, String msg) {
                setInfo(getString(R.string.load_failed, getShortId(Ad_UNIT_ID_INTERSTITIAL), errorCode, msg));
            }
        });
        setInfo(getString(R.string.start_request, getShortId(Ad_UNIT_ID_INTERSTITIAL)));
    }

    public void presentInterstitial(View view) {
        mAds.presentPlayableAD(Ad_UNIT_ID_INTERSTITIAL, new SimplePlayLoadingListener() {

            @Override
            public void onVideoStart() {
                setInfo(getString(R.string.ads_start, getShortId(Ad_UNIT_ID_INTERSTITIAL)));
            }

            @Override
            public void onVideoFinished() {
                setInfo(getString(R.string.ads_video_finished, getShortId(Ad_UNIT_ID_INTERSTITIAL)));
            }

            @Override
            public void onLandingPageInstallBtnClicked() {
                setInfo(getString(R.string.ads_install_button_clicked, getShortId(Ad_UNIT_ID_INTERSTITIAL)));
            }

            @Override
            public void onAdClosed() {
                setInfo(getString(R.string.ads_ad_closed, getShortId(Ad_UNIT_ID_INTERSTITIAL)));
            }

            @Override
            public void onAdsError(int errorCode, String msg) {
                setInfo(getString(R.string.ads_error, getShortId(Ad_UNIT_ID_INTERSTITIAL), errorCode, msg));
            }

        });
    }

    String getShortId(String id) {
        if (TextUtils.isEmpty(id)) {
            return "";
        } else if (id.length() < 7) {
            return id;
        } else {
            return id.substring(0, 3) + "..." + id.substring(id.length() - 3);
        }
    }


    private void setInfo(final String msg) {
        Log.d(TAG, msg);
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

    public void clearLog(View view) {
        info.setText("");
    }
}

