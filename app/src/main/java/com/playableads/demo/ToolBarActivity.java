package com.playableads.demo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.playableads.PlayableAdsSettings;
import com.playableads.constants.ConstantsHelper;
import com.playableads.demo.util.UserConfig;
import com.playableads.entity.GDPRStatus;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/9/26.
 */

public abstract class ToolBarActivity extends AppCompatActivity {
    private FrameLayout mContentView;
    private Toolbar mToolbar;
    private boolean showSettingButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_toolbar);
        mContentView = (FrameLayout) findViewById(R.id.content_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(10);
        }
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserConfig.getInstance(this).isTestEnv()) {
            ConstantsHelper.useTestEnv();
        } else {
            ConstantsHelper.useProductEnv();
        }

        if (UserConfig.getInstance(this).gdprConsent()) {
            PlayableAdsSettings.setGDPRConsent(GDPRStatus.PERSONALIZED);
        } else {
            PlayableAdsSettings.setGDPRConsent(GDPRStatus.NON_PERSONALIZED);
        }
    }

    protected void showUpAction() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    protected void showSettingsButton() {
        showSettingButton = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showSettingButton) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                SettingsActivity.launch(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentView.addView(LayoutInflater.from(this).inflate(layoutResID, mContentView, false));
    }

    @Override
    public void setContentView(View view) {
        mContentView.addView(view);
    }
}
