package cn.ecookshipuji;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import cn.admob.admobgensdk.ad.constant.InformationAdType;
import cn.ecookshipuji.banner.BannerActivity;
import cn.ecookshipuji.drawvod.CustomDrawVodActivity;
import cn.ecookshipuji.drawvod.DrawVodActivity;
import cn.ecookshipuji.information.InformationActivity;
import cn.ecookshipuji.interstitial.InterstitialActivity;
import cn.ecookshipuji.js.JsAdActivity;
import cn.ecookshipuji.nativead.NativeActivity;
import cn.ecookshipuji.rewardvod.RewardVodActivity;
import cn.ecookshipuji.splash.SplashActivity2;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        findViewById(R.id.tvSplash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity2.jumpHere(MainActivity.this);
            }
        });
        findViewById(R.id.tvRewardVod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardVodActivity.jumpHere(MainActivity.this);
            }
        });
        findViewById(R.id.tvInterstitial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterstitialActivity.jumpHere(MainActivity.this);
            }
        });
        findViewById(R.id.tvBanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerActivity.jumpHere(MainActivity.this);
            }
        });
        findViewById(R.id.tvNative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NativeActivity.jumpHere(MainActivity.this);
            }
        });

        findViewById(R.id.tvDrawVod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.customDrawVod) {
                    CustomDrawVodActivity.jumpHere(MainActivity.this);
                } else {
                    DrawVodActivity.jumpHere(MainActivity.this);
                }
            }
        });
        findViewById(R.id.tvJs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsAdActivity.jumpHere(v.getContext());
            }
        });
        findViewById(R.id.tvInformation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationActivity.jumpHere(MainActivity.this, InformationAdType.NORMAL);
            }
        });
        findViewById(R.id.tvInformationImageOnly).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationActivity.jumpHere(MainActivity.this, InformationAdType.ONLY_IMAGE);
            }
        });
        findViewById(R.id.tvInformationRightImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationActivity.jumpHere(MainActivity.this, InformationAdType.RIGHT_IMAGE);
            }
        });
        findViewById(R.id.tvInformationLeftImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationActivity.jumpHere(MainActivity.this, InformationAdType.LEFT_IMAGE);
            }
        });
        findViewById(R.id.tvInformationBottomImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationActivity.jumpHere(MainActivity.this, InformationAdType.BOTTOM_IMAGE);
            }
        });
        findViewById(R.id.tvInformationvertric).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationActivity.jumpHere(MainActivity.this, InformationAdType.VERTICALPICFLOW);
            }
        });
        findViewById(R.id.tvInformationThreeImages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationActivity.jumpHere(MainActivity.this, InformationAdType.THREE_PIC_FLOW);
            }
        });
        findViewById(R.id.tvInformationTwoImageTwoText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationActivity.jumpHere(MainActivity.this, InformationAdType.TWO_PIC_TWO_TEXT_FLOW);
            }
        });
        findViewById(R.id.ivSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.jumpHere(MainActivity.this);
            }
        });
    }
}
