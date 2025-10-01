package com.furieau.apps.mydiaryappnative;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.furieau.apps.mydiaryappnative.ui.login.LoginActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LauncherActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyDiaryPrefs";
    private static final String KEY_FIRST_RUN = "first_run";
    private static final String KEY_ACCEPTED_LICENSE = "accepted_license";
    private static final String KEY_ACCEPTED_PRIVACY = "accepted_privacy";
    private static final int SPLASH_DELAY = 100;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // 延迟执行检查，模拟加载过程
        new Handler().postDelayed(this::checkAppState, SPLASH_DELAY);
    }

    static public boolean isRooted() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su -c id");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null && line.contains("uid=0")) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    private void checkAppState() {
        boolean isFirstRun = prefs.getBoolean(KEY_FIRST_RUN, true);
        boolean hasAcceptedLicense = prefs.getBoolean(KEY_ACCEPTED_LICENSE, false);
        boolean hasAcceptedPrivacy = prefs.getBoolean(KEY_ACCEPTED_PRIVACY, false);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        boolean isVerified = prefs.getBoolean("is_verified", false);
        if (isRooted()) isVerified = false;

        Intent intent;

        if (isFirstRun) {
            // 第一次运行，显示欢迎引导页
            intent = new Intent(this, WelcomeGuideActivity.class);
        } else if (!hasAcceptedLicense) {
            // 需要接受许可证
            intent = new Intent(this, LicenseActivity.class);
        } else if (!hasAcceptedPrivacy) {
            // 需要接受隐私政策
            intent = new Intent(this, LicenseActivity.class);
            intent.putExtra("TITLE_RES", R.string.privacy_policy);
            intent.putExtra("CONTENT_RES", R.raw.privacy_policy);
            intent.putExtra("TYPE", "privacy");
        } else if (!isLoggedIn) {
            // 需要登录
            intent = new Intent(this, LoginActivity.class);
        } else if (!isVerified) {
            // 需要完成人机验证
            intent = new Intent(this, HumanVerificationActivity.class);
        } else {
            // 所有检查通过，进入主界面
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish(); // 结束 LauncherActivity，防止返回
    }
}