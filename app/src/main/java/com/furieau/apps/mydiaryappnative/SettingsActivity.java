package com.furieau.apps.mydiaryappnative;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.furieau.apps.mydiaryappnative.ui.ConfirmResetPrefActivity;
import com.furieau.apps.mydiaryappnative.ui.about.AboutActivity;
import com.furieau.apps.mydiaryappnative.ui.about.OpenSourceLicensesActivity;
import com.furieau.apps.mydiaryappnative.ui.about.PermissionExplanationActivity;
import com.furieau.apps.mydiaryappnative.ui.about.ProjectPageOverlayActivity;
import com.furieau.apps.mydiaryappnative.util.AESCipherActivity;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_activity_settings);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private SharedPreferences prefs;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // 注册监听器
            Objects.requireNonNull(getPreferenceManager().getSharedPreferences())
                    .registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            // 注销监听器
            Objects.requireNonNull(getPreferenceManager().getSharedPreferences())
                    .unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            prefs = requireContext().getSharedPreferences("MyDiaryPrefs", MODE_PRIVATE);
            setupPreferenceClickListeners();
            initializeExistingValues();
        }


        private void setupPreferenceClickListeners() {
            // 退出登录
            Preference logout = findPreference("logout");
            if (logout != null) logout.setOnPreferenceClickListener(preference -> {
                showLogoutConfirmation();
                return true;
            });

            // 重置偏好
            Preference delacc = findPreference("delete_account");
            if (delacc != null) delacc.setOnPreferenceClickListener(preference -> {
                showResetPrefConfirmation();
                return true;
            });

            // 隐私政策
            Preference pp = findPreference("privacy_policy");
            if (pp != null) pp.setOnPreferenceClickListener(preference -> {
                prefs.edit().putBoolean("accepted_privacy", false).apply();
                // 跳转到隐私政策页面
                Intent intent = new Intent(requireActivity(), LicenseActivity.class);
                intent.putExtra("TITLE_RES", R.string.privacy_policy);
                intent.putExtra("CONTENT_RES", R.raw.privacy_policy);
                intent.putExtra("TYPE", "privacy");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                requireActivity().finish();
                return true;
            });

            // 关于
            Preference about = findPreference("about_app"),
                    osl = findPreference("open_source_license"),
                    pex = findPreference("permission_explain"),
                    pmp = findPreference("product_mainpage");
            if (about != null && osl != null && pex != null && pmp != null) {
                about.setOnPreferenceClickListener(preference -> {
                    Intent intent = new Intent(requireActivity(), AboutActivity.class);
                    startActivity(intent);
                    return true;
                });
                osl.setOnPreferenceClickListener(preference -> {
                    Intent intent = new Intent(requireActivity(), OpenSourceLicensesActivity.class);
                    startActivity(intent);
                    return true;
                });
                pex.setOnPreferenceClickListener(preference -> {
                    Intent intent = new Intent(requireActivity(), PermissionExplanationActivity.class);
                    startActivity(intent);
                    return true;
                });
                pmp.setOnPreferenceClickListener(preference -> {
                    Intent intent = new Intent(requireActivity(), ProjectPageOverlayActivity.class);
                    startActivity(intent);
                    return true;
                });

            }

            // 调试
            Preference oaca = findPreference("open_aescipher");
            if (oaca != null) oaca.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(requireActivity(), AESCipherActivity.class);
                startActivity(intent);
                return true;
            });
        }

        private void initializeExistingValues() {
            EditTextPreference usernamePref = findPreference("yourname");
            String currentUsername = prefs.getString("username", "");
            if (usernamePref != null) {
                usernamePref.setText(currentUsername);
            }
            ListPreference acceptLicensePref = findPreference("acceptlicense");
            boolean currentLicenseStatus = prefs.getBoolean("accepted_license", false);
            if (acceptLicensePref != null) {
                acceptLicensePref.setValue(currentLicenseStatus ? "accept" : "decline");
            }

        }

        private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        // 同步所有设置到 MyDiaryPrefs
                        switch (Objects.requireNonNull(key)) {
                            case "yourname":
                                String name = sharedPreferences.getString(key, "");
                                prefs.edit().putString("username", name).apply();
                                break;

                            case "acceptlicense":
                                String licenseStatus = sharedPreferences.getString(key, "decline");
                                prefs.edit().putBoolean("accepted_license", licenseStatus.equals("accept")).apply();
                                handleLicenseStatusChange(licenseStatus);
                                break;
                        }
                    }
                };

        private void handleLicenseStatusChange(String status) {
            if ("decline".equals(status)) {
//                system("pkill -9 com.furieau.apps.mydiaryappnative");
                // 显示一个对话框，告知用户必须接受许可证
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("许可证未接受")
                        .setMessage("您必须接受许可证才能继续使用应用。")
                        .setPositiveButton("查看许可证", (dialog, which) -> {
                            Intent intent = new Intent(requireActivity(), LicenseActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        })
                        .setNegativeButton("退出应用", (dialog, which) -> {
                            // 退出应用
                            requireActivity().finishAffinity();
                        })
                        .setCancelable(false) // 禁止通过返回键取消
                        .show();
            }
        }

        private void showLogoutConfirmation() {
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("退出登录")
                    .setMessage("确定要退出登录吗？")
                    .setPositiveButton("退出", (dialog, which) -> performLogout())
                    .setNegativeButton("取消", null)
                    .show();
        }

        private void performLogout() {
            prefs.edit()
                    .putBoolean("is_logged_in", false)
                    .remove("username")
                    .apply();

            Intent intent = new Intent(requireContext(), LauncherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        }

        private void showResetPrefConfirmation() {
            Intent intent = new Intent(requireContext(), ConfirmResetPrefActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();  // 调用返回功能
        return true;
    }
}