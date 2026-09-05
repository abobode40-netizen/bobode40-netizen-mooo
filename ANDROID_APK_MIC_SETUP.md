# دليل إعداد الميكروفون وأذونات الـ WebView لتحويل التطبيق إلى APK (أندرويد)

يوضح هذا المستند التقني الخطوات المباشرة لإعداد أذونات الميكروفون وتفعيل بروتوكولات الصوت عبر `WebView` في تطبيق أندرويد لضمان عمل ميزة تسجيل التلاوة وتمرين الصوت بنجاح عند تحويل موقع الويب إلى تطبيق أندرويد (`APK`).

---

## 1. إضافة أذونات الصوت والميكروفون في ملف `AndroidManifest.xml`

يجب التأكد من إضافة الأذونات التالية داخل ملف `AndroidManifest.xml` الخاص بتطبيق الأندرويد لتميل النظام بالسماح باستخدام الميكروفون وسماعات الرأس:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.quran.app">

    <!-- 🎙️ أذونات الميكروفون وسماعات الرأس والصوت -->
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />

    <!-- 🌐 أذونات الاتصال بالإنترنت لتشغيل القراءات والتفاسير -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:allowBackup="true"
        android:hardwareAccelerated="true"
        android:icon="@mipmap/ic_launcher"
        android:label="المصحف الشريف"
        android:supportsRtl="true"
        android:theme="@style/Theme.AppCompat.NoActionBar">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:configChanges="orientation|screenSize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

## 2. تفعيل إذن WebRTC Audio في `WebChromeClient` (كود أندرويد)

متصفح الـ `WebView` المدمج في أندرويد لا يمنح إذن الميكروفون تلقائياً لصفحات الويب التي تطلب `navigator.mediaDevices.getUserMedia`. يجب تفعيل `WebChromeClient` والرد بطلب `grant` كما يلي:

### بلغة Java (`MainActivity.java`):

```java
package com.quran.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private static final int MIC_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. طلب إذن الميكروفون المباشر من النظام إذا لم يكن ممنوحاً
        checkMicrophonePermission();

        webView = findViewById(R.id.webview);

        // 2. ضبط إعدادات الـ WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowFileAccess(true);

        // 3. ضبط WebViewClient لفتح الروابط داخل التطبيق
        webView.setWebViewClient(new WebViewClient());

        // 4. تفعيل WebChromeClient لمنح إذن WebRTC Audio لصفحة الويب
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // منح إذن الصوت والميكروفون لصفحة الويب مباشرة
                        request.grant(request.getResources());
                    }
                });
            }
        });

        // 5. تحميل موقع الويب الخاص بالتطبيق
        webView.loadUrl("https://ais-dev-mkvwcipusuils24u7e62uk-370477156971.europe-west2.run.app");
    }

    private void checkMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MIC_PERMISSION_REQUEST_CODE);
        }
    }
}
```

### بلغة Kotlin (`MainActivity.kt`):

```kotlin
package com.quran.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. طلب إذن الميكروفون
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 101)
        }

        webView = findViewById(R.id.webview)
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
        }

        webView.webViewClient = WebViewClient()

        // 2. تفعيل منح أذونات الميكروفون للـ WebView
        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                runOnUiThread {
                    request.grant(request.resources)
                }
            }
        }

        webView.loadUrl("https://ais-dev-mkvwcipusuils24u7e62uk-370477156971.europe-west2.run.app")
    }
}
```

---

## 3. توصيات جودة التسجيل وتفادي صدى الصوت (Headphones Tip)

- **استخدام سماعات الرأس (Headphones):** يُنصح دائماً بإرشاد القارئ لاستخدام سماعة الرأس لمنع ارتداد صدى مكبر الصوت (Acoustic Echo) ولضمان التقاط نبرات التلاوة وصوت القارئ بأعلى دقة وأصغى نقاء.

---

## 4. خطوات إعادة تصنيع الـ APK

1. افتح مشروع أندرويد في **Android Studio** أو عبر منصات تحويل الويب إلى APK (مثل Website2APK / Capacitor / WebIntoApp).
2. استبدل ملف `AndroidManifest.xml` بالملف الموضح أعلاه.
3. تأكد من إضافة كود `WebChromeClient` في النشاط الرئيسي (`MainActivity`).
4. قم بعمل **Build > Generate Signed Bundle / APK** للحصول على ملف الـ APK النهائي الشغال بالكامل.
