package com.playableads.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.playableads.constants.ConstantsHelper;
import com.playableads.demo.util.UserConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/9/19.
 */

public class SplashActivity extends Activity {
    private Handler mHandler;
    @BindView(R.id.as_text)
    TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if (UserConfig.getInstance(this).isTestEnv()) {
            ConstantsHelper.useTestEnv();
        } else {
            ConstantsHelper.useProductEnv();
        }
        mTextView.setText(String.format("%s\n%s", mTextView.getText(), ConstantsHelper.getVersion()));
        mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 1500);
    }

    private void startMainActivity() {
        MainActivity.launch(SplashActivity.this);
        finish();
    }

    public void finishActivity(View view) {
        mHandler.removeMessages(0);
        startMainActivity();
    }
}