package cn.ecookshipuji.interstitial;

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

import cn.admob.admobgensdk.ad.interstitial.ADMobGenInterstitial;
import cn.admob.admobgensdk.ad.listener.ADMobGenInterstitialAdListener;
import cn.admob.admobgensdk.ad.listener.SingleClickListener;
import cn.admob.admobgensdk.entity.IADMobGenInterstitial;
import cn.ecookshipuji.MyApplication;
import cn.ecookshipuji.R;

/**
 * @author ciba
 * @description ：插屏广告示例
 * @date 2018/12/20
 */
public class InterstitialActivity extends Activity {
    private IADMobGenInterstitial iadMobGenInterstitial;
    private AlertDialog alertDialog;

    public static void jumpHere(Context context) {
        context.startActivity(new Intent(context, InterstitialActivity.class));
    }

    private static final String TAG = "ADMobGen_Log";
    private ADMobGenInterstitial adMobGenInterstitial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_vod);

        Button btnLoadAd = findViewById(R.id.btnLoadAd);
        btnLoadAd.setText("获取插屏广告");
        btnLoadAd.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                releaseInterstitialAd();
                loadInterstitialAd();
            }
        });

        Button btnShowAd = findViewById(R.id.btnShowAd);
        btnShowAd.setText("展示插屏广告");
        btnShowAd.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                showInterstitialAd();
            }
        });

    }

    @Override
    protected void onDestroy() {
        releaseInterstitialAd();
        super.onDestroy();
    }

    /**
     * 释放插屏广告资源
     */
    private void releaseInterstitialAd() {
        if (adMobGenInterstitial != null) {
            adMobGenInterstitial.destroy();
            adMobGenInterstitial = null;
        }
        iadMobGenInterstitial = null;
    }

    /**
     * 获取插屏广告
     */
    private void loadInterstitialAd() {
        showLoadingDialog();
        // 第二个参数是广告位序号（默认为0，用于支持单样式多广告位，无需要可以填0或者使用其他构造方法）
        adMobGenInterstitial = new ADMobGenInterstitial(this, MyApplication.adIndex);
        adMobGenInterstitial.setListener(new ADMobGenInterstitialAdListener() {
            @Override
            public void onADReceive(IADMobGenInterstitial iadMobGenInterstitial) {
                toast("插屏获取成功啦~");
                dismissLoadingDialog();
                InterstitialActivity.this.iadMobGenInterstitial = iadMobGenInterstitial;
                Log.e(TAG, "插屏广告获取成功::::::: ");
            }

            @Override
            public void onADExposure(IADMobGenInterstitial iadMobGenInterstitial) {
                Log.e(TAG, "广告展示曝光回调，但不一定是曝光成功了，比如一些网络问题导致上报失败::::::: ");
            }

            @Override
            public void onADClick(IADMobGenInterstitial iadMobGenInterstitial) {
                Log.e(TAG, "广告被点击::::::: ");
            }

            @Override
            public void onADClose(IADMobGenInterstitial iadMobGenInterstitial) {
                Log.e(TAG, "插屏关闭回调::::::: ");
            }

            @Override
            public void onADFailed(String s) {
                toast("插屏获取失败啦~");
                dismissLoadingDialog();
                Log.e(TAG, "插屏广告获取失败::::::: " + s);
            }
        });
        adMobGenInterstitial.loadAd();
    }

    /**
     * 展示插屏广告
     */
    private void showInterstitialAd() {
        if (iadMobGenInterstitial != null) {
            if (iadMobGenInterstitial.hasShown()) {
                toast("插屏广告已经观看过了");
            } else if (iadMobGenInterstitial.hasExpired()) {
                toast("插屏广告已经过期了");
            } else {
                iadMobGenInterstitial.show(InterstitialActivity.this);
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
