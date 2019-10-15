package cn.ecookshipuji.rewardvod;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import cn.admob.admobgensdk.ad.listener.SimpleADMobGenRewardVodAdListener;
import cn.admob.admobgensdk.ad.listener.SingleClickListener;
import cn.admob.admobgensdk.ad.rewardvod.ADMobGenRewardVod;
import cn.admob.admobgensdk.ad.rewardvod.IADMobGenRewardVod;
import cn.ecookshipuji.MyApplication;
import cn.ecookshipuji.R;

/**
 * @author ciba
 * @description ：激励视频是全屏视频广告，播放过程中不可返回退出
 * @date 2018/12/20
 */
public class RewardVodActivity extends Activity {
    private IADMobGenRewardVod mIadMobGenRewardVod;
    private AlertDialog alertDialog;

    public static void jumpHere(Context context) {
        context.startActivity(new Intent(context, RewardVodActivity.class));
    }

    private static final String TAG = "ADMobGen_Log";
    private ADMobGenRewardVod adMobGenRewardVod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_vod);
        findViewById(R.id.btnLoadAd).setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                releaseRewardVodAd();
                loadRewardVodAd();
            }
        });
        findViewById(R.id.btnShowAd).setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                showRewardVideo();
            }
        });

    }

    @Override
    protected void onDestroy() {
        releaseRewardVodAd();
        super.onDestroy();
    }

    /**
     * 释放激励视频广告资源
     */
    private void releaseRewardVodAd() {
        if (adMobGenRewardVod != null) {
            adMobGenRewardVod.destroy();
            adMobGenRewardVod = null;
        }
        mIadMobGenRewardVod = null;
    }

    /**
     * 获取激励视频广告
     */
    private void loadRewardVodAd() {
        showLoadingDialog();
        // 第二个参数是广告位序号（默认为0，用于支持单样式多广告位，无需要可以填0或者使用其他构造方法）
        adMobGenRewardVod = new ADMobGenRewardVod(this, MyApplication.adIndex);
        adMobGenRewardVod.setListener(new SimpleADMobGenRewardVodAdListener() {
            @Override
            public void onADReceiv(IADMobGenRewardVod iadMobGenRewardVod) {
                toast("激励视频获取成功啦~");
                dismissLoadingDialog();
                mIadMobGenRewardVod = iadMobGenRewardVod;
                Log.e(TAG, "激励视频广告获取成功::::::: ");
            }

            @Override
            public void onVideoCached(IADMobGenRewardVod iadMobGenRewardVod) {
                // 建议在此回调后开始展示激励视频（可以无卡顿无loading播放，Mobvsita的激励视频需要在此回调后播放）
                Log.e(TAG, "激励视频广告缓存成功::::::: ");
            }

            @Override
            public void onADExposure(IADMobGenRewardVod iadMobGenRewardVod) {
                Log.e(TAG, "广告展示曝光回调，但不一定是曝光成功了，比如一些网络问题导致上报失败::::::: ");
            }

            @Override
            public void onADClick(IADMobGenRewardVod iadMobGenRewardVod) {
                Log.e(TAG, "广告被点击::::::: ");
            }

            @Override
            public void onReward(IADMobGenRewardVod iadMobGenRewardVod) {
                Log.e(TAG, "获取的奖励回调::::::: ");
            }

            @Override
            public void onVideoComplete(IADMobGenRewardVod iadMobGenRewardVod) {
                Log.e(TAG, "该回调不一定都有，激励视频播放完成回调::::::: ");
            }

            @Override
            public void onADClose(IADMobGenRewardVod iadMobGenRewardVod) {
                Log.e(TAG, "激励视频关闭回调::::::: ");
            }

            @Override
            public void onADFailed(String s) {
                toast("激励视频获取失败啦~");
                dismissLoadingDialog();
                Log.e(TAG, "激励视频广告获取失败::::::: " + s);

            }
        });
        adMobGenRewardVod.loadAd();
    }

    /**
     * 展示激励视频广告
     */
    private void showRewardVideo() {
        if (mIadMobGenRewardVod != null) {
            if (mIadMobGenRewardVod.hasShown()) {
                toast("视频已经观看过了");
            } else if (mIadMobGenRewardVod.hasExpired()) {
                toast("视频已经过期了");
            } else {
                mIadMobGenRewardVod.showRewardVideo(RewardVodActivity.this);
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
