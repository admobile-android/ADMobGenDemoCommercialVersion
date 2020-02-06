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

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.admob.admobgensdk.ad.listener.ADMobGenNativeUnifiedListener;
import cn.admob.admobgensdk.ad.nativead.ADMobGenNativeUnified;
import cn.admob.admobgensdk.entity.IADMobGenNativeUnifiedAd;
import cn.ecookshipuji.MyApplication;
import cn.ecookshipuji.R;
import cn.ecookshipuji.widget.MySmartRefreshLayout;

/**
 * @author : ciba
 * @date : 2018/6/23
 * @description : 自渲染2.0广告示例
 */

public class NativeUnifiedActivity extends Activity implements OnRefreshLoadMoreListener {
    private static final String TAG = "ADMobGen_Log";
    private List<Object> mDataList = new ArrayList<>();
    private NativeUnifiedCustomAdapter nativeUnifiedCustomAdapter;
    private MySmartRefreshLayout refreshLayout;
    private ADMobGenNativeUnified adMobGenNativeUnified;
    private int loadType;

    public static void jumpHere(Context context) {
        Intent intent = new Intent(context, NativeUnifiedActivity.class);
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

        // 第二个参数是广告位序号（默认为0，用于支持单样式多广告位，无需要可以填0或者使用其他构造方法）
        adMobGenNativeUnified = new ADMobGenNativeUnified(this, MyApplication.adIndex);

        adMobGenNativeUnified.setListener(new ADMobGenNativeUnifiedListener() {
            @Override
            public void onADReceiv(List<IADMobGenNativeUnifiedAd> nativeUnifiedAdList) {
                Log.e(TAG, "NativeUnifiedActivity onADReceiv::::: ");
                finishLoad(nativeUnifiedAdList);
            }

            @Override
            public void onADClick(IADMobGenNativeUnifiedAd iadMobGenNativeUnifiedAd) {
                Log.e(TAG, " NativeUnifiedActivity onADClick::::: ");
            }

            @Override
            public void onADExposure(IADMobGenNativeUnifiedAd iadMobGenNativeUnifiedAd) {
                // MOBVSITA平台的 iadMobGenNativeUnifiedAd 对象为null
                Log.e(TAG, " NativeUnifiedActivity onADExposure::::: ");
            }

            @Override
            public void onADFailed(String s) {
                Log.e(TAG, " NativeUnifiedActivity onADFailed::::: " + s);
                finishLoad(null);
            }
        });

        nativeUnifiedCustomAdapter = new NativeUnifiedCustomAdapter(mDataList);
        mRecyclerView.setAdapter(nativeUnifiedCustomAdapter);

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
    protected void onResume() {
        super.onResume();
        // 恢复广告状态（有广点通平台必须调用该方法）
        if (adMobGenNativeUnified != null) {
            adMobGenNativeUnified.resume();
        }
    }

    @Override
    protected void onDestroy() {
        // 广告资源释放
        if (adMobGenNativeUnified != null) {
            adMobGenNativeUnified.destroy();
            adMobGenNativeUnified = null;
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
        nativeUnifiedCustomAdapter.notifyDataSetChanged();

        // 获取自渲染2.0广告，一次最多请求3条，请求成功的数量小于等于请求数量
        adMobGenNativeUnified.loadAd(2);
    }

    /**
     * 获取结束
     */
    private void finishLoad(List<IADMobGenNativeUnifiedAd> nativeUnifiedAdList) {
        refreshLayout.finish(loadType, nativeUnifiedAdList != null && nativeUnifiedAdList.size() > 0, false);
        if (nativeUnifiedAdList != null && nativeUnifiedAdList.size() > 0) {
            mDataList.addAll(nativeUnifiedAdList);
        }
        nativeUnifiedCustomAdapter.notifyDataSetChanged();
    }

    /**
     * 释放广告视图
     */
    private void clearData() {
        mDataList.clear();
        nativeUnifiedCustomAdapter.notifyDataSetChanged();
    }
}