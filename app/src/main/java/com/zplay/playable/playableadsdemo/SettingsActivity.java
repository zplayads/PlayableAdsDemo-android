package com.zplay.playable.playableadsdemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.playableads.demo.R;
import com.zplay.playable.playableadsdemo.util.UserConfig;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/6/14.
 */

public class SettingsActivity extends ToolBarActivity {
    private static final String CACHE_PATH = "/ZPLAYAds/cache";

    @BindView(R.id.aa_channel_id)
    EditText mChannelId;
    @BindView(R.id.aa_clear)
    View mClear;
    UserConfig mConfig;
    @BindView(R.id.autoload_model)
    SwitchCompat mAutoloadSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);
        ButterKnife.bind(this);
        showUpAction();
        mConfig = UserConfig.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChannelId.setText(mConfig.getChannelId());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConfig.setChannelId(mChannelId.getText().toString().trim());
    }

    @OnClick(R.id.aa_clear_cache)
    void clearCache() {
        final File cacheDir = new File(getFilesDir() + CACHE_PATH);
        if (!cacheDir.exists() || (cacheDir.listFiles() == null || cacheDir.listFiles().length == 0)) {
            Toast.makeText(SettingsActivity.this, "No cache data.", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Clear cache will kill the app, do you want to clear it?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                return myDeleteFile(cacheDir.getPath());
                            }

                            @Override
                            protected void onPostExecute(Boolean aBoolean) {
                                super.onPostExecute(aBoolean);
                                if (aBoolean) {
                                    System.exit(0);
                                }
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                })
                .setNegativeButton("No", null)
                .create().show();
    }

    @OnTextChanged(R.id.aa_channel_id)
    void chanelTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.getTrimmedLength(s) > 0) {
            mClear.setVisibility(View.VISIBLE);
        } else {
            mClear.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.aa_clear)
    void clearChannelEdit() {
        mChannelId.setText("");
    }

    private boolean myDeleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return true;
        }
        if (!file.isDirectory()) {
            return file.delete();
        }

        File[] files = file.listFiles();
        for (File f : files) {
            myDeleteFile(f.getPath());
        }
        return file.delete();
    }

    public static void launch(Context ctx) {
        Intent i = new Intent(ctx, SettingsActivity.class);
        ctx.startActivity(i);
    }
}
