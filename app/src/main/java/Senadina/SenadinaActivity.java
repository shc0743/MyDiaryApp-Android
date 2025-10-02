package Senadina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.furieau.apps.mydiaryappnative.HumanVerificationActivity;
import com.furieau.apps.mydiaryappnative.LauncherActivity;
import com.furieau.apps.mydiaryappnative.R;
import com.furieau.apps.mydiaryappnative.util.SimpleAESCipher;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SenadinaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_senadina);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.senadina);
        }

        WebView w = findViewById(R.id.webviewSenadina);
        if (w != null) {
            w.getSettings().setJavaScriptEnabled(true);
            w.getSettings().setDomStorageEnabled(true);
            w.getSettings().setAllowFileAccess(true);
            try {
                InputStream inputStream = getResources().openRawResource(R.raw.senadina);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String encrypted = new String(buffer, StandardCharsets.UTF_8);
                String decrypted = SimpleAESCipher.decrypt(encrypted, SimpleAESCipher.decrypt(getString(R.string.senadina_encrypt_key_encrypted), "希娜狄雅不一样！！！"));
                String htmlContent = "<div style=\"white-space: pre-wrap;\">" + decrypted + "</div>";
                w.loadDataWithBaseURL("file:///android_res/raw/", htmlContent, "text/html", "UTF-8", null);
            } catch (Exception e) {
                w.loadData("加载失败...", "text/html", "UTF-8");
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}