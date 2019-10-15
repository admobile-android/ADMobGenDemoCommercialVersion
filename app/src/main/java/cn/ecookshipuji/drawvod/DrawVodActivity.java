package cn.ecookshipuji.drawvod;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import cn.admob.admobgensdk.ad.drawvod.ADMobGenDrawVod;
import cn.admob.admobgensdk.ad.listener.ADMobGenDrawVodListener;
import cn.admob.admobgensdk.ad.listener.ADMobGenDrawVodVideoListener;
import cn.admob.admobgensdk.common.ADMobGenSDK;
import cn.admob.admobgensdk.entity.IADMobGenDrawVod;
import cn.ecookshipuji.MyApplication;
import cn.ecookshipuji.R;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * @author ciba
 * @description ：DrawVod 广告适用于全屏的视频列表（类似抖音小视频）
 * @date 2019/3/6
 */
public class DrawVodActivity extends Activity {
    private static final String TAG = "ADMobGen_Log";
    private VerticalViewPager verticalViewPager;
    private ViewPager horViewPager;
    private ADMobGenDrawVod adMobGenDrawVod;
    private AlertDialog alertDialog;
    private List<IADMobGenDrawVod> adMobGenDrawVodList;

    public static void jumpHere(Context context) {
        context.startActivity(new Intent(context, DrawVodActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ADMobGenSDK.instance().fullScreen(this);
        setContentView(R.layout.activity_draw_vod);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy::::::::::::::::: " );
        clearData();
        if (adMobGenDrawVod != null) {
            adMobGenDrawVod.destroy();
            adMobGenDrawVod = null;
        }
        super.onDestroy();
    }

    private void initView() {
        verticalViewPager = findViewById(R.id.verticalViewPager);
        horViewPager = findViewById(R.id.horViewPager);
    }

    private void initData() {
        // 第二个参数是广告位序号（默认为0，用于支持单样式多广告位，无需要可以填0或者使用其他构造方法）
        // adMobGenDrawVod = new ADMobGenDrawVod(this, MyApplication.adIndex);
        adMobGenDrawVod = new ADMobGenDrawVod(this, MyApplication.adIndex);
        // 设置DrawVod广告监听
        adMobGenDrawVod.setListener(new ADMobGenDrawVodListener() {
            @Override
            public void onADReceiv(List<IADMobGenDrawVod> drawVodList) {
                Log.e(TAG, "广告获取成功::::::: ");
                adMobGenDrawVodList = drawVodList;
                dismissLoadingDialog();
                setAdapter();
            }

            @Override
            public void onADClick(IADMobGenDrawVod drawVod) {
                Log.e(TAG, "广告被点击::::::: ");
            }

            @Override
            public void onADExposure(IADMobGenDrawVod drawVod) {
                Log.e(TAG, "广告展示曝光回调，但不一定是曝光成功了，比如一些网络问题导致上报失败::::::: ");
            }

            @Override
            public void onADFailed(String error) {
                Log.e(TAG, "广告获取失败::::::: " + error);
                Toast.makeText(DrawVodActivity.this, error, Toast.LENGTH_SHORT).show();
                dismissLoadingDialog();
                finish();
            }
        });

        // 展示loading弹框
        showLoadingDialog();
        // 开始加载draw视频广告（一次最多加载3条），请求成功的数量小于等于请求数量
        adMobGenDrawVod.loadAd(3);
    }

    private void setAdapter() {
        if (adMobGenDrawVodList != null && adMobGenDrawVodList.size() > 0) {
            PagerAdapter pagerAdapter = new PagerAdapter() {
                @Override
                public int getCount() {
                    return adMobGenDrawVodList.size();
                }

                @Override
                public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                    return view == o;
                }

                @NonNull
                @Override
                public Object instantiateItem(@NonNull ViewGroup container, int position) {
                    IADMobGenDrawVod drawVod = adMobGenDrawVodList.get(position);
                    // 有需要可设置视频播放状态监听
                    drawVod.setADMobGenVideoListener(new ADMobGenDrawVodVideoListener() {
                        @Override
                        public void onVideoError(IADMobGenDrawVod iadMobGenDrawVod, String s) {
                            Log.e(TAG, "onVideoError: ");
                        }

                        @Override
                        public void onVideoLoad(IADMobGenDrawVod iadMobGenDrawVod) {
                            Log.e(TAG, "onVideoLoad: ");
                        }

                        @Override
                        public void onVideoStart(IADMobGenDrawVod iadMobGenDrawVod) {
                            Log.e(TAG, "onVideoStart: ");
                        }

                        @Override
                        public void onVideoPause(IADMobGenDrawVod iadMobGenDrawVod) {
                            Log.e(TAG, "onVideoPause: ");
                        }

                        @Override
                        public void onVideoComplete(IADMobGenDrawVod iadMobGenDrawVod) {
                            // 目前头条的视频没有此回调
                            Log.e(TAG, " onVideoComplete ::::: ");
                        }
                    });
                    // 渲染和设置广告点击事件（不设置广告将无法点击按钮将无法点击，影响收益）
                    drawVod.render();
                    container.addView(drawVod.getView());
                    return drawVod.getView();
                }

                @Override
                public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                    container.removeView((View) object);
                }
            };
            // 加载横向滑动的ViewPager还是竖直方向的
            if (MyApplication.horDrawVod) {
                horViewPager.setVisibility(View.VISIBLE);
                verticalViewPager.setVisibility(View.GONE);
                horViewPager.setAdapter(pagerAdapter);
            } else {
                horViewPager.setVisibility(View.GONE);
                verticalViewPager.setVisibility(View.VISIBLE);
                verticalViewPager.setAdapter(pagerAdapter);
            }
        }
    }

    /**
     * loading
     */
    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("loading...");
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * 隐藏loading
     */
    private void dismissLoadingDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    /**
     * 释放广告数据
     */
    private void clearData() {
        if (adMobGenDrawVodList != null && adMobGenDrawVodList.size() > 0) {
            for (int i = 0; i < adMobGenDrawVodList.size(); i++) {
                IADMobGenDrawVod drawVod = adMobGenDrawVodList.get(i);
                drawVod.destroy();
            }
            adMobGenDrawVodList.clear();
        }
    }
}
