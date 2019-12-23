package cn.ecookshipuji.banner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import cn.admob.admobgensdk.ad.banner.ADMobGenBannerView;
import cn.admob.admobgensdk.ad.listener.ADMobGenBannerAdListener;
import cn.ecookshipuji.MyApplication;
import cn.ecookshipuji.R;

/**
 * @author : ciba
 * @date : 2018/6/28
 * @description : Banner广告适用于固定位置，不建议放在ListView、RecyclerView、ViewPager等控件中
 */

public class BannerActivity extends Activity {
    private static final String TAG = "ADMobGen_Log";
    private FrameLayout flContainer;
    private ADMobGenBannerView adMobGenBannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        flContainer = findViewById(R.id.flContainer);

        // 初始化Banner广告
        // 第二个参数是广告位序号（默认为0，用于支持单样式多广告位，无需要可以填0或者使用其他构造方法）
        adMobGenBannerView = new ADMobGenBannerView(this, MyApplication.adIndex);

        // 设置刷新间隔（30s~120s，其中百度广告是30s刷新间隔且不可改变），不设置默认不刷新（不适用百度）
        // 头条Banner已经升级为模板广告，如果需要开启自动刷新，需要后台开启并设置刷新时间
        adMobGenBannerView.setRefreshTime(30);

        // 切换为广点通Banner2.0（2019.04.11后广点通不能新建Banner广告位，只能新建Banner2.0，如果申请的是Banner2.0的广告位需要设置这个，之前申请的Banner无需设置）
        adMobGenBannerView.setGdt2(MyApplication.gdtBanner2);

        // 测试发现安卓版本在8.0及以上部分小米手机，当APP长时间处于后台时，Bugly有几率收集到WebView相关的Native Crash信息（暂时无法手动复现），目前测试发现有可能是头条的Banner广告导致的，后续将持续跟进此问题
        // setRemoveTouTiaoBannerInXiaoMiUp8设置为true（默认为false）将不在安卓8.0及以上的小米手机中获取头条的Banner广告
        adMobGenBannerView.setRemoveTouTiaoBannerInXiaoMiUp8(true);

        // 设置广告监听
        adMobGenBannerView.setListener(new ADMobGenBannerAdListener() {
            @Override
            public void onADExposure() {
                Log.e(TAG, "广告展示曝光回调，但不一定是曝光成功了，比如一些网络问题导致上报失败 ::::: ");
            }

            @Override
            public void onADFailed(String s) {
                Log.e(TAG, "广告获取失败了 ::::: " + s);
                releaseBannerAd();
            }

            @Override
            public void onADReceiv() {
                Log.e(TAG, "广告获取成功了 ::::: ");
            }

            @Override
            public void onADClick() {
                Log.e(TAG, "广告被点击了 ::::: ");
            }

            @Override
            public void onAdClose() {
                Log.e(TAG, "广告被关闭了 ::::: ");
            }
        });
        // 把广告控件添加到容器
        flContainer.addView(adMobGenBannerView);
        // 开始获取广告
        adMobGenBannerView.loadAd();
    }

    @Override
    protected void onDestroy() {
        releaseBannerAd();
        super.onDestroy();
    }

    /**
     * 释放广告资源
     */
    private void releaseBannerAd() {
        if (adMobGenBannerView != null) {
            ViewParent parent = adMobGenBannerView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(adMobGenBannerView);
            }
            adMobGenBannerView.destroy();
            adMobGenBannerView = null;
        }
        flContainer.setVisibility(View.GONE);
    }

    public static void jumpHere(Context context) {
        context.startActivity(new Intent(context, BannerActivity.class));
    }
}
