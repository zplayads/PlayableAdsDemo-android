package com.playableads.demo.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.playableads.AtmosplayAdsBanner;
import com.playableads.BannerListener;
import com.playableads.PlayableAds;
import com.playableads.demo.R;
import com.playableads.demo.ToolBarActivity;
import com.playableads.demo.util.UserConfig;
import com.playableads.entity.BannerSize;
import com.playableads.presenter.widget.AtmosBannerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTextChanged;

import static android.text.TextUtils.isEmpty;
import static com.playableads.entity.BannerSize.BANNER_320x50;
import static com.playableads.entity.BannerSize.BANNER_728x90;
import static com.playableads.entity.BannerSize.SMART_BANNER;

/**
 * Description:
 * <p>
 * Created by lgd on 2019-09-04.
 */
public class BannerSample extends ToolBarActivity {
    private static final String TAG = "BannerSample";

    private static final String BANNER_APP_ID = "5C5419C7-A2DE-88BC-A311-C3E7A646F6AF";
    private static final String BANNER_UNIT_ID = "F22F347B-3D57-0C70-0B13-EFCFDF402EBA";

    @BindView(R.id.logTextView)
    TextView mLogView;
    @BindView(R.id.unit_id)
    EditText mUnitIdEdit;
    @BindView(R.id.unitIdClearer)
    View mUnitIdClearer;
    @BindView(R.id.app_id)
    EditText mAppIdEdit;
    @BindView(R.id.appIdClearer)
    View mAppIdClearer;

    @BindView(R.id.bannerContainerView)
    ViewGroup mBannerContainer;

    AtmosplayAdsBanner mBanner;
    BannerSize mBannerSize = BANNER_320x50;
    UserConfig mConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        ButterKnife.bind(this);

        showUpAction();
        showSettingsButton();
        setTitle("Banner");

        mAppIdEdit.setText(BANNER_APP_ID);
        mUnitIdEdit.setText(BANNER_UNIT_ID);

        mConfig = UserConfig.getInstance(this);
    }

    @OnClick(R.id.loadAd)
    public void loadAd() {
        addLog("loadAd");
        if (mBanner != null) {
            mBanner.loadAd();
            return;
        }
        mBanner = new AtmosplayAdsBanner(this, mAppIdEdit.getText().toString().trim(), mUnitIdEdit.getText().toString().trim());
        mBanner.setBannerSize(mBannerSize);
        mBanner.setBannerContainer(mBannerContainer);
        mBanner.setChannelId(mConfig.getChannelId());
        mBanner.setBannerListener(new BannerListener() {
            @Override
            public void onBannerPrepared(AtmosBannerView view) {
                addLog("onBannerPrepared");
            }

            @Override
            public void onBannerPreparedFailed(int code, String error) {
                addLog("onBannerPreparedFailed errorCode: " + code + ", message: " + error);
            }

            @Override
            public void onBannerClicked() {
                addLog("onBannerClicked");
            }
        });
        mBanner.loadAd();
    }

    @OnClick(R.id.destroy)
    void destroyBanner() {
        if (mBanner != null) {
            mBanner.destroy();
            mBanner = null;
        }
    }

    @OnLongClick(R.id.logTextView)
    boolean clearLog() {
        mLogView.setText("");
        return true;
    }

    @OnClick(R.id.appIdClearer)
    void clearAppIdEdit() {
        mAppIdEdit.setText("");
        mAppIdEdit.requestFocus();
    }

    @OnClick(R.id.unitIdClearer)
    void clearUnitIdEdit() {
        mUnitIdEdit.setText("");
        mUnitIdEdit.requestFocus();
    }

    @OnTextChanged(R.id.unit_id)
    void unitIdChanged(CharSequence s, int start, int before, int count) {
        if (isEmpty(s.toString().trim())) {
            mUnitIdClearer.setVisibility(View.GONE);
        } else {
            mUnitIdClearer.setVisibility(View.VISIBLE);
        }
    }

    @OnTextChanged(R.id.app_id)
    void appIdChanged(CharSequence s, int start, int before, int count) {
        if (isEmpty(s.toString().trim())) {
            mAppIdClearer.setVisibility(View.GONE);
        } else {
            PlayableAds.init(this, s.toString().trim());
            mAppIdClearer.setVisibility(View.VISIBLE);
        }
    }

    public void on320x50ModelClicked(View view) {
        mBannerSize = BANNER_320x50;
        refreshBannerSize();
    }

    public void on728x90ModelClicked(View view) {
        mBannerSize = BANNER_728x90;
        refreshBannerSize();
    }

    public void onSmartModelClicked(View view) {
        mBannerSize = SMART_BANNER;
        refreshBannerSize();
    }

    private void refreshBannerSize() {
        if (mBanner != null) {
            mBanner.setBannerSize(mBannerSize);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyBanner();
    }

    private void addLog(final String log) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogView.setText(String.format("%s\n%s", log, mLogView.getText()));
            }
        });
    }

    public static void launch(Context context) {
        Intent i = new Intent(context, BannerSample.class);
        context.startActivity(i);
    }
}
