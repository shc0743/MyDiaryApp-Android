package com.furieau.apps.mydiaryappnative.ui;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.furieau.apps.mydiaryappnative.LauncherActivity;
import com.furieau.apps.mydiaryappnative.R;
import com.furieau.apps.mydiaryappnative.databinding.ActivityConfirmResetPrefBinding;

public class ConfirmResetPrefActivity extends AppCompatActivity {
    private ActivityConfirmResetPrefBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_reset_pref);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = ActivityConfirmResetPrefBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button contBtn = binding.button4;
        final CheckBox cb = binding.checkBox;
        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb.isChecked()) {
                    new AlertDialog.Builder(ConfirmResetPrefActivity.this)
                            .setTitle("操作取消")
                            .setMessage("看起来你点错了^_^")
                            .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface v, int b) {
                                    Intent intent = new Intent(ConfirmResetPrefActivity.this, LauncherActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences("MyDiaryPrefs", MODE_PRIVATE);
                prefs.edit().clear().apply();
                Intent intent = new Intent(ConfirmResetPrefActivity.this, LauncherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        final Button cancBtn = binding.button5;
        cancBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmResetPrefActivity.this, LauncherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_activity_ui_confirm_reset_pref);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}