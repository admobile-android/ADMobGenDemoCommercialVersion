package cn.ecookshipuji.fullscreenvod;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.admob.admobgensdk.ad.fullscreenvod.ADMobGenFullScreenVod;
import cn.admob.admobgensdk.ad.fullscreenvod.IADMobGenFullScreenVod;
import cn.admob.admobgensdk.ad.listener.SimpleADMobGenFullScreenVodAdListener;
import cn.admob.admobgensdk.ad.listener.SingleClickListener;
import cn.ecookshipuji.MyApplication;
import cn.ecookshipuji.R;

/**
 * @author ciba
 * @description ：全屏视频广告，播放一定时长即可退出，无需等到视频播放完毕
 * @date 2018/12/20
 */
public class FullScreenVodActivity extends Activity {
    private IADMobGenFullScreenVod mIADMobGenFullScreenVod;
    private AlertDialog alertDialog;

    public static void jumpHere(Context context) {
        context.startActivity(new Intent(context, FullScreenVodActivity.class));
    }

    private static final String TAG = "ADMobGen_Log";
    private ADMobGenFullScreenVod adMobGenFullScreenVod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_vod);

        Button btnLoadAd = findViewById(R.id.btnLoadAd);
        btnLoadAd.setText("加载全屏视频广告");

        Button btnShowAd = findViewById(R.id.btnShowAd);
        btnShowAd.setText("展示全屏视频广告");

        btnLoadAd.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                releaseFullScreenVodAd();
                loadFullScreenVodAd();
            }
        });
        btnShowAd.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                showFullScreenVod();
            }
        });

    }

    @Override
    protected void onDestroy() {
        releaseFullScreenVodAd();
        super.onDestroy();
    }

    /**
     * 释放全屏视频广告资源
     */
    private void releaseFullScreenVodAd() {
        if (adMobGenFullScreenVod != null) {
            adMobGenFullScreenVod.destroy();
            adMobGenFullScreenVod = null;
        }
        mIADMobGenFullScreenVod = null;
    }

    /**
     * 获取全屏视频广告
     */
    private void loadFullScreenVodAd() {
        showLoadingDialog();
        // 第二个参数是广告位序号（默认为0，用于支持单样式多广告位，无需要可以填0或者使用其他构造方法）
        adMobGenFullScreenVod = new ADMobGenFullScreenVod(this, MyApplication.adIndex);
        adMobGenFullScreenVod.setListener(new SimpleADMobGenFullScreenVodAdListener() {
            @Override
            public void onADReceive(IADMobGenFullScreenVod iadMobGenFullScreenVod) {
                toast("全屏视频获取成功啦~");
                dismissLoadingDialog();
                mIADMobGenFullScreenVod = iadMobGenFullScreenVod;
                Log.e(TAG, "全屏视频广告获取成功::::::: ");
            }

            @Override
            public void onVideoCached(IADMobGenFullScreenVod iadMobGenFullScreenVod) {
                // 建议在此回调后开始展示全屏视频
                Log.e(TAG, "全屏视频广告缓存成功::::::: ");
            }

            @Override
            public void onADExposure(IADMobGenFullScreenVod iadMobGenFullScreenVod) {
                Log.e(TAG, "广告展示曝光回调，但不一定是曝光成功了，比如一些网络问题导致上报失败::::::: ");
            }

            @Override
            public void onADClick(IADMobGenFullScreenVod iadMobGenFullScreenVod) {
                Log.e(TAG, "广告被点击::::::: ");
            }

            @Override
            public void onVideoComplete(IADMobGenFullScreenVod iadMobGenFullScreenVod) {
                Log.e(TAG, "全屏视频播放完成回调::::::: ");
            }

            @Override
            public void onADClose(IADMobGenFullScreenVod iadMobGenFullScreenVod) {
                Log.e(TAG, "全屏视频关闭回调::::::: ");
            }

            @Override
            public void onSkipVideo(IADMobGenFullScreenVod iadMobGenFullScreenVod) {
                Log.e(TAG, "全屏视频跳过回调::::::: ");
            }

            @Override
            public void onADFailed(String s) {
                toast("全屏视频获取失败啦~");
                dismissLoadingDialog();
                Log.e(TAG, "全屏视频广告获取失败::::::: " + s);

            }
        });
        adMobGenFullScreenVod.loadAd();
    }

    /**
     * 展示全屏视频广告
     */
    private void showFullScreenVod() {
        if (mIADMobGenFullScreenVod != null) {
            if (mIADMobGenFullScreenVod.hasShown()) {
                toast("视频已经观看过了");
            } else if (mIADMobGenFullScreenVod.hasExpired()) {
                toast("视频已经过期了");
            } else {
                mIADMobGenFullScreenVod.showFullScreenVod(this);
            }
        }
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("loading...");
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void dismissLoadingDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}
