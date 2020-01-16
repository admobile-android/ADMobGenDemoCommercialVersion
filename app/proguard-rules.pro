-ignorewarnings
# v4、v7
-keep class android.support.v4.**{public *;}
-keep class android.support.v7.**{public *;}

# ADMobGenSdk混淆
-dontwarn cn.admob.admobgensdk.**
-keep class cn.admob.admobgensdk.**{*;}
-keep interface cn.admob.admobgensdk.**{*;}
-keep class com.android.**{*;}
-keep class com.ciba.**{ *; }
-keep interface com.ciba.**{ *; }
-dontwarn org.apache.commons.**
-keep class org.apache.**{*;}

# AdMob广告SDK混淆
-keep class admsdk.library.**{*;}

# 广点通广告SDK混淆
-keep class com.qq.e.** {public protected *;}
-keep class MTT.ThirdAppInfoNew {*;}
-keep class com.tencent.** {*;}

# 百度广告SDK混淆
-keepclassmembers class * extends android.app.Activity {public void *(android.view.View);}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.baidu.mobads.*.**{*;}

## 头条广告混淆
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep class com.androidquery.callback.** {*;}
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.* {*;}

## imobi广告SDK混淆
-keepattributes SourceFile,LineNumberTable
-keep class com.inmobi.** { *; }
-dontwarn com.inmobi.**
-keep public class com.google.android.gms.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{public *;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{public *;}
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**
-keep class com.moat.** {*;}
-dontwarn com.moat.**
-keep class com.integralads.avid.library.** {*;}

## mobvsita广告SDK混淆
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mintegral.** {*; }
-keep interface com.mintegral.** {*; }
-dontwarn com.mintegral.**
-keep class com.alphab.** {*; }
-keep interface com.alphab.** {*; }

## 资源文件混淆配置
-keep class **.R$* { *; }
-keep public class **.R$*{
   public static final int *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

# OAID混淆
-keep class com.bun.miitmdid.core.** {*;}