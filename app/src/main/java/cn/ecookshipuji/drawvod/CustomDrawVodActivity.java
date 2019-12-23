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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.admob.admobgensdk.ad.constant.ADMobGenAdPlaforms;
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
public class CustomDrawVodActivity extends Activity {
    private static final String TAG = "ADMobGen_Log";
    private VerticalViewPager verticalViewPager;
    private ViewPager horViewPager;
    private ADMobGenDrawVod adMobGenDrawVod;
    private AlertDialog alertDialog;
    private List<IADMobGenDrawVod> adMobGenDrawVodList;

    public static void jumpHere(Context context) {
        context.startActivity(new Intent(context, CustomDrawVodActivity.class));
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
        Log.e(TAG, "onDestroy::::::::::::::::: ");
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
        // TODO: 2019/12/3 ADMobGenDrawVod广告由于头条已没有新建广告位的入口，列入过时
        // 第二个参数是广告位序号（默认为0，用于支持单样式多广告位，无需要可以填0或者使用其他构造方法）
        // 第三个参数是16：9的视频是否铺满显示(铺满在某些非16：9手机可能会有拉伸)，默认false
        // adMobGenDrawVod = new ADMobGenDrawVod(this, MyApplication.adIndex);
        adMobGenDrawVod = new ADMobGenDrawVod(this, MyApplication.adIndex);
        // 9：16的视频是否铺满显示(铺满在某些非9：16手机可能会有拉伸)，默认false
        adMobGenDrawVod.verVideoFullScreen(true);
        // 是否只展示视频，其他广告元素自行加载（广告标题、广告描述、广告标记(广告两个字)、广告来源（头条、广点通、艾狄墨博图标）、广告点击控件等）
        adMobGenDrawVod.onlyVideo(true);

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
                Toast.makeText(CustomDrawVodActivity.this, error, Toast.LENGTH_SHORT).show();
                dismissLoadingDialog();
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

                    View view = LayoutInflater
                            .from(container.getContext())
                            .inflate(R.layout.layout_draw_vod, null, false);

                    FrameLayout flDrawVod = view.findViewById(R.id.flDrawVod);
                    ImageView ivPlatform = view.findViewById(R.id.ivPlatform);
                    TextView tvTitle = view.findViewById(R.id.tvTitle);
                    TextView tvDes = view.findViewById(R.id.tvDes);
                    ImageView ivImage = view.findViewById(R.id.ivImage);
                    TextView tvAction = view.findViewById(R.id.tvAction);

                    // 将draw vod 广告视图放入容器
                    ViewGroup parent = (ViewGroup) drawVod.getView().getParent();
                    if (parent != null) {
                        parent.removeView(drawVod.getView());
                    }
                    flDrawVod.addView(drawVod.getView());

                    // 设置广告来源图标（DrawVod视频目前仅支持头条）
                    ivPlatform.setImageResource(ADMobGenAdPlaforms.PLAFORM_TOUTIAO.equals(drawVod.getPlatform())
                            ? R.drawable.admobgensdk_toutiao_icon_only : R.drawable.admobgensdk_admob_icon_only);

                    // 设置广告标题
                    tvTitle.setText(drawVod.getTitle());

                    // 设置广告描述
                    tvDes.setText(drawVod.getDes());

                    // 设置Action(广告类型描述：查看详情、下载安装等)
                    tvAction.setText(drawVod.getAction());

                    // 加载广告图片
                    Glide.with(container.getContext())
                            .load(drawVod.getImage())
                            .into(ivImage);

                    // 重要!!渲染视频和点击（如果设置了onlyVideo，一定要传入广告响应点击的控件）
                    List<View> clickViewList = new ArrayList<>();
                    clickViewList.add(ivImage);
                    clickViewList.add(tvAction);
                    drawVod.render(clickViewList);

                    container.addView(view);
                    return view;
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
