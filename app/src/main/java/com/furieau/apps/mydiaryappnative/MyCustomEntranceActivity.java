package com.furieau.apps.mydiaryappnative;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MyCustomEntranceActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000; // 2秒延迟，可以显示广告或加载数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        // 延迟执行检查，模拟加载过程
        new Handler().postDelayed(this::checkAppState, SPLASH_DELAY);
    }

    private void checkAppState() {
        Intent intent = new Intent(this, LauncherActivity.class);
        startActivity(intent);
        finish();
    }
}