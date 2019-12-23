package cn.ecookshipuji;

import android.app.Application;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import cn.admob.admobgensdk.ad.constant.ADMobGenAdPlaforms;
import cn.admob.admobgensdk.common.ADMobGenSDK;
import cn.admob.admobgensdk.entity.ADMobGenSdkConfig;

/**
 * @author : ciba
 * @date : 2018/7/11
 * @description : replace your description
 */

public class MyApplication extends Application {
    /**
     * 广告位序号
     */
    public static int adIndex = 0;
    /**
     * 是否横屏展示DrawVod广告
     */
    public static boolean horDrawVod = false;
    /**
     * 是否是自定义DrawVod广告类型
     */
    public static boolean customDrawVod = false;
    /**
     * 是否是广点通Banner2.0
     */
    public static boolean gdtBanner2 = true;
    private static final String APP_ID = "2482522";

    private static final String[] PLATFORMS = {
            ADMobGenAdPlaforms.PLAFORM_ADMOB
            , ADMobGenAdPlaforms.PLAFORM_GDT
            , ADMobGenAdPlaforms.PLAFORM_TOUTIAO
            , ADMobGenAdPlaforms.PLAFORM_BAIDU
            , ADMobGenAdPlaforms.PLAFORM_INMOBI
            , ADMobGenAdPlaforms.PLAFORM_MOBVSITA
    };

    @Override
    public void onCreate() {
        super.onCreate();
        long millis = System.currentTimeMillis();
        ADMobGenSDK.instance().initSdk(this, new ADMobGenSdkConfig.Builder()
                // 修改为自己的appId
                .appId(APP_ID)
                // PLATFORMS只需要选择所需的广告平台(其中PLAFORM_ADMOB是必须的)
                .platforms(PLATFORMS)
                // 如果APP在其他进程使用了WebView（没有可不设置）可通过这个方法来兼容，也可自行通过WebView.setDataDirectorySuffix()兼容
                // .webViewOtherProcessName(suffix)
                .build());
        Log.e("ADMobGen_Log", "init sdk need time : " + (System.currentTimeMillis() - millis) + "ms");

        // 添加bugly初始化（该初始化与广告SDK无关，广告SDK中不包含bugly相关内容，仅供Demo错误信息收集使用）
        CrashReport.initCrashReport(getApplicationContext(), "6b2f5f5e4c", BuildConfig.DEBUG);
    }
}
