package cn.ecookshipuji.js;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.admob.admobgensdk.ad.js.AdmobileJsNativeAdInterface;

/**
 * @author ciba
 * @description 描述
 * @date 2019/10/9
 */
public class JsAdActivity extends FragmentActivity {
    private WebView webView;
    private AdmobileJsNativeAdInterface admobileJsNativeAdInterface;

    public static void jumpHere(Context context) {
        Intent intent = new Intent(context, JsAdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
        setContentView(webView);

        // TODO: 2019/10/9 Step1 : 加载网页（网页已经注入需要的广告JS）
        webView.loadUrl("http://www.admobile.top/testjs.html");
    }

    private void initWebView() {
        webView = new WebView(getApplicationContext());
        WebSettings webSettings = webView.getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        // TODO: 2019/10/9 Step2 : 一定要支持JS
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        //禁用文字缩放
        webSettings.setTextZoom(100);
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true);
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //不保存密码
        webSettings.setSavePassword(false);
        //移除部分系统JavaScript接口
        removeJavascriptInterfaces();
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);

        // TODO: 2019/10/9 Step3 :  添加JS广告接口支持
        admobileJsNativeAdInterface = new AdmobileJsNativeAdInterface(this, webView);
        webView.addJavascriptInterface(admobileJsNativeAdInterface, "admobileJsNativeAdInterface");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // TODO: 2019/10/9 Step4 :  页面加载完毕的时候调用
                if (admobileJsNativeAdInterface != null) {
                    admobileJsNativeAdInterface.onAdmobileJsNativeAdReady();
                }
            }
        });
    }

    private void removeJavascriptInterfaces() {
        try {
            if (Build.VERSION.SDK_INT < 17) {
                webView.removeJavascriptInterface("searchBoxJavaBridge_");
                webView.removeJavascriptInterface("accessibility");
                webView.removeJavascriptInterface("accessibilityTraversal");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放WebView
        if (webView != null) {
            try {
                webView.setVisibility(View.GONE);
                webView.clearHistory();
                webView.clearView();
                webView.removeAllViews();
                webView.clearCache(true);
                webView.destroy();
                ViewGroup parent = (ViewGroup) webView.getParent();
                if (parent != null) {
                    parent.removeView(webView);
                }
                webView = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 释放JS广告
        if (admobileJsNativeAdInterface != null) {
            admobileJsNativeAdInterface.destroy();
            admobileJsNativeAdInterface = null;
        }
    }
}
