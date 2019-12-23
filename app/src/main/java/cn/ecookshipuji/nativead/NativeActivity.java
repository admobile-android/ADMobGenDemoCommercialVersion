package cn.ecookshipuji.nativead;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.admob.admobgensdk.ad.listener.ADMobGenNativeListener;
import cn.admob.admobgensdk.ad.nativead.ADMobGenNative;
import cn.admob.admobgensdk.entity.IADMobGenNativeAd;
import cn.ecookshipuji.MyApplication;
import cn.ecookshipuji.R;
import cn.ecookshipuji.widget.MySmartRefreshLayout;

/**
 * @author : ciba
 * @date : 2018/6/23
 * @description : Native广告是自渲染模式广告，返回广告数据，用户可以自渲染广告视图
 */

public class NativeActivity extends Activity implements OnRefreshLoadMoreListener {
    private static final String TAG = "ADMobGen_Log";
    private List<Object> mDataList = new ArrayList<>();
    private NativeCustomAdapter mAdapter;
    private MySmartRefreshLayout refreshLayout;
    private ADMobGenNative adMobGenNative;
    private int loadType;

    public static void jumpHere(Context context) {
        Intent intent = new Intent(context, NativeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        refreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView mRecyclerView = findViewById(R.id.lvList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: 2019/11/28 ADMobGenNative广告已经过时，后续将不再维护，建议使用 ADMobGenNativeUnified（自渲染2.0）广告代替，具体内容可参考NativeUnifiedActivity
        // 第二个参数是广告位序号（默认为0，用于支持单样式多广告位，无需要可以填0或者使用其他构造方法）
        adMobGenNative = new ADMobGenNative(this, MyApplication.adIndex);
        // 设置广告曝光校验最小间隔时间(0~200)，默认为200ms，在RecyclerView或ListView这种列表中不建议设置更小值，在一些特定场景（如Dialog或者固定位置可根据要求设置更小值）
        // adMobGenNative.setExposureDelay(200);

        adMobGenNative.setListener(new ADMobGenNativeListener() {
            @Override
            public void onADReceiv(List<IADMobGenNativeAd> nativeAdList) {
                finishLoad(nativeAdList);
            }

            @Override
            public void onADClick(IADMobGenNativeAd iadMobGenNativeAd) {
                Log.e(TAG, " NativeActivity onADClick::::: " + iadMobGenNativeAd.getTitle());
            }

            @Override
            public void onADExposure(IADMobGenNativeAd iadMobGenNativeAd) {
                Log.e(TAG, " NativeActivity onADExposure::::: " + iadMobGenNativeAd.getTitle());
            }

            @Override
            public void onADFailed(String s) {
                Toast.makeText(NativeActivity.this.getApplicationContext(), "错误信息: " + s, Toast.LENGTH_SHORT).show();
                finishLoad(null);
            }
        });
        mAdapter = new NativeCustomAdapter(this, mDataList);
        mRecyclerView.setAdapter(mAdapter);

        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadType = MySmartRefreshLayout.TYPE_LOAD_MORE;
        loadData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        loadType = MySmartRefreshLayout.TYPE_FRESH;
        clearData();
        loadData();
    }

    @Override
    protected void onDestroy() {
        // 广告资源释放
        if (adMobGenNative != null) {
            adMobGenNative.destroy();
            adMobGenNative = null;
        }
        super.onDestroy();
    }

    /**
     * 模拟获取数据
     */
    private void loadData() {
        List<String> tempDataList = new ArrayList<>();
        for (int i = 0; i < 20; ++i) {
            tempDataList.add("No." + i + " Normal Data");
        }
        mDataList.addAll(tempDataList);
        mAdapter.notifyDataSetChanged();

        // 获取原生广告，一次最多请求3条，请求成功的数量小于等于请求数量
        adMobGenNative.loadAd(2);
    }

    /**
     * 获取结束
     */
    private void finishLoad(List<IADMobGenNativeAd> nativeAdList) {
        refreshLayout.finish(loadType, nativeAdList != null && nativeAdList.size() > 0, false);
        if (nativeAdList != null && nativeAdList.size() > 0) {
            mDataList.addAll(nativeAdList);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 释放广告视图
     */
    private void clearData() {
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
    }
}