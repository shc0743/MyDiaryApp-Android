package com.furieau.apps.mydiaryappnative;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnDiaryList, btnWriteDiary, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化按钮
        initViews();

        // 设置点击监听器
        setupClickListeners();
    }

    private void initViews() {
        btnDiaryList = findViewById(R.id.button);      // 日记列表按钮
        btnWriteDiary = findViewById(R.id.button2);    // 写日记按钮
        btnSettings = findViewById(R.id.button3);      // 设置按钮
    }

    private void setupClickListeners() {
        // 日记列表按钮点击事件
        btnDiaryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到日记列表页面
                Intent intent = new Intent(MainActivity.this, DiaryListActivity.class);
                startActivity(intent);
            }
        });

        // 写日记按钮点击事件
        btnWriteDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到写日记页面
                Intent intent = new Intent(MainActivity.this, DiaryEditorActivity.class);
                startActivity(intent);
            }
        });

        // 设置按钮点击事件
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到设置页面
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}