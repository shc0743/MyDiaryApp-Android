package com.furieau.apps.mydiaryappnative;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.furieau.apps.mydiaryappnative.ui.login.LoginActivity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class HumanVerificationActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_human_verification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("人机验证");
        }

        WebView w = findViewById(R.id.webviewCaptcha);
        if (w != null) {
            w.getSettings().setJavaScriptEnabled(true);
            w.getSettings().setDomStorageEnabled(true);
            w.getSettings().setAllowFileAccess(true);
            try {
                InputStream inputStream = getResources().openRawResource(R.raw.captcha);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String htmlContent = new String(buffer, StandardCharsets.UTF_8);
                w.loadDataWithBaseURL("file:///android_res/raw/", htmlContent, "text/html", "UTF-8", null);
                Toast.makeText(this, "请完成人机验证", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                w.loadData("加载失败...", "text/html", "UTF-8");
                Toast.makeText(this, "人机验证加载失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            w.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String url = request.getUrl().toString();
                    if (url.startsWith("captchapass://")) {
                        // 通过验证
                        SharedPreferences prefs = getSharedPreferences("MyDiaryPrefs", MODE_PRIVATE);
                        prefs.edit().putBoolean("is_verified", true).apply();
                        // 跳转
                        Intent intent = new Intent(HumanVerificationActivity.this, LauncherActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}