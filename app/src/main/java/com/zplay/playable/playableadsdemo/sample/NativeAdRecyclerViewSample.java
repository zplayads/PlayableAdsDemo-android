package com.zplay.playable.playableadsdemo.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zplay.playable.playableadsdemo.R;
import com.zplay.playable.playableadsdemo.ToolBarActivity;
import com.zplay.playable.playableadsdemo.util.LogTextView;
import com.zplay.playable.playableadsdemo.util.UserConfig;
import com.playableads.PlayableNativeExpressAd;
import com.playableads.nativead.NativeAd;
import com.playableads.nativead.NativeAdExpressView;
import com.playableads.nativead.NativeAdLoadListener;
import com.playableads.nativead.NativeEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.zplay.playable.playableadsdemo.MainActivity.APP_ID;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/8/27.
 */

public class NativeAdRecyclerViewSample extends ToolBarActivity {
    private static final String NATIVE_AD_UNIT_ID = "0246FB55-3042-9F29-D4AB-21C6349EEE83";
    private static final String[] sAppAndUnitId = new String[]{APP_ID, NATIVE_AD_UNIT_ID};

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.arvn_log_view)
    LogTextView mLogView;
    ArrayList<String> mData;
    CustomerRecyclerAdapter mCustomerRecyclerAdapter;

    @BindView(R.id.app_id)
    EditText mAppIdEdit;
    @BindView(R.id.clear)
    View mClear;
    @BindView(R.id.unit_id)
    EditText mAdUnitIdEdit;
    @BindView(R.id.clear2)
    View mClear2;

    private PlayableNativeExpressAd mPlayableNativeAd;

    NativeEventListener mNativeEventListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_recycler_view);
        ButterKnife.bind(this);

        showUpAction();
        showSettingsButton();

        mAppIdEdit.setText(sAppAndUnitId[0]);
        mAdUnitIdEdit.setText(sAppAndUnitId[1]);

        mData = new ArrayList<>();
        mCustomerRecyclerAdapter = new CustomerRecyclerAdapter(mData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mCustomerRecyclerAdapter);

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

        initNativeAd(sAppAndUnitId[0], sAppAndUnitId[1]);
    }

    private void initNativeAd(String appId, String unitId) {
        mPlayableNativeAd = new PlayableNativeExpressAd(this, appId, unitId);
        mPlayableNativeAd.setChannelId(UserConfig.getInstance(this).getChannelId());
        mPlayableNativeAd.setNativeAdLoadListener(new NativeAdLoadListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                mLogView.addLog("onNativeAdLoaded");
                nativeAd.setNativeEventListener(mNativeEventListener);
                mCustomerRecyclerAdapter.addAdContent(mData.size(), nativeAd);
            }

            @Override
            public void onNativeAdFailed(int errorCode, String message) {
                mLogView.addLog("onNativeAdFailed: " + message);
            }
        });
    }

    private void loadMoreData() {
        int start = mData.size();
        for (int i = 0; i < 5; i++) {
            mData.add("item " + (start + i));
        }
    }

    public void loadMore(View view) {
        loadMoreData();
        mCustomerRecyclerAdapter.notifyDataSetChanged();
        mPlayableNativeAd.loadAd();
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

    public void clearLog(View view) {
        mLogView.clearLog();
    }

    static class CustomerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_ZPLAY_AD = 1;
        private ArrayList<String> mData;
        private SparseArray<NativeAd> mNativeAds;

        CustomerRecyclerAdapter(ArrayList<String> data) {
            mData = data;
            mNativeAds = new SparseArray<>();
        }

        void addAdContent(int position, NativeAd nativeAd) {
            if (position > mData.size()) {
                position = mData.size();
            }
            mNativeAds.put(position, nativeAd);
            mData.add(position, "nativeAd");
            notifyItemRangeChanged(position, mData.size() - position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ZPLAY_AD) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_native_layout, parent, false);
                return new NativeTemplateViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plain_layout, parent, false);
                return new PlainVH(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == VIEW_TYPE_ZPLAY_AD) {
                NativeTemplateViewHolder nativeAdVH = (NativeTemplateViewHolder) holder;
                NativeAd nativeAd = mNativeAds.get(position);
                if (nativeAd != null) {
                    nativeAd.renderAdView(nativeAdVH.nativeTemplateView);
                }
            } else {
                final PlainVH plainVH = (PlainVH) holder;
                plainVH.text.setText(mData.get(position));
                plainVH.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "clicked: " + mData.get(plainVH.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            String data = mData.get(position);
            if (TextUtils.equals(data, "nativeAd")) {
                return VIEW_TYPE_ZPLAY_AD;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    static class PlainVH extends RecyclerView.ViewHolder {
        TextView text;

        PlainVH(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.pl_text);
        }
    }

    static class NativeTemplateViewHolder extends RecyclerView.ViewHolder {
        NativeAdExpressView nativeTemplateView;

        NativeTemplateViewHolder(View itemView) {
            super(itemView);
            nativeTemplateView = (NativeAdExpressView) itemView.findViewById(R.id.adRichView);
        }
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, NativeAdRecyclerViewSample.class);
        context.startActivity(intent);
    }
}


