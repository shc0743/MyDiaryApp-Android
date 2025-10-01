package com.furieau.apps.mydiaryappnative.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.furieau.apps.mydiaryappnative.LauncherActivity;
import com.furieau.apps.mydiaryappnative.LicenseActivity;
import com.furieau.apps.mydiaryappnative.R;
import com.furieau.apps.mydiaryappnative.WelcomeGuideActivity;
import com.furieau.apps.mydiaryappnative.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText usernameEditText = binding.username;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "需要输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingProgressBar.setVisibility(View.VISIBLE);

                String welcome = getString(R.string.welcome) + usernameEditText.getText().toString();
                Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                getSharedPreferences("MyDiaryPrefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("is_logged_in", true)
                        .apply();
                getSharedPreferences("MyDiaryPrefs", MODE_PRIVATE)
                        .edit()
                        .putString("username", usernameEditText.getText().toString())
                        .apply();

                Intent intent = new Intent(LoginActivity.this, LauncherActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}