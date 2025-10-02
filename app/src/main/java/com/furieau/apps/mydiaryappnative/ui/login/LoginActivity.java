package com.furieau.apps.mydiaryappnative.ui.login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.furieau.apps.mydiaryappnative.LauncherActivity;
import com.furieau.apps.mydiaryappnative.R;
import com.furieau.apps.mydiaryappnative.databinding.ActivityLoginBinding;
import Senadina.SenadinaActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> SenadinaLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText usernameEditText = binding.username;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginButton.setOnClickListener(v -> {
            if (usernameEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "需要输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }

            loadingProgressBar.setVisibility(View.VISIBLE);

            String welcome = getString(R.string.welcome, usernameEditText.getText().toString());
            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            getSharedPreferences("MyDiaryPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("is_logged_in", true)
                    .apply();
            getSharedPreferences("MyDiaryPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("username", usernameEditText.getText().toString())
                    .apply();

            if (usernameEditText.getText().toString().equals("希娜狄雅")) {
                Intent intent = new Intent(LoginActivity.this, SenadinaActivity.class);
                SenadinaLauncher.launch(intent);
                return;
            }

            Intent intent = new Intent(LoginActivity.this, LauncherActivity.class);
            startActivity(intent);
            finish();
        });

        SenadinaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent intent2 = new Intent(LoginActivity.this, LauncherActivity.class);
                    startActivity(intent2);
                    finish();
                }
        );
    }
}