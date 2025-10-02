package com.furieau.apps.mydiaryappnative.util;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.furieau.apps.mydiaryappnative.R;
import java.util.Arrays;

public class AESCipherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aescipher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.encrypt);
        }

        EditText inp = findViewById(R.id.editTextTextMultiLine2),
                out = findViewById(R.id.editTextTextMultiLine),
                pwd = findViewById(R.id.editTextTextPassword);
        Button enc = findViewById(R.id.button6),
                dec = findViewById(R.id.button7);
        enc.setOnClickListener(v -> {
            try {
                out.setText(SimpleAESCipher.encrypt(inp.getText().toString(), pwd.getText().toString()));
            } catch (Exception e) {
                out.setText("操作失败: " + Arrays.toString(e.getStackTrace()));
            }
        });
        dec.setOnClickListener(v -> {
            try {
                out.setText(SimpleAESCipher.decrypt(inp.getText().toString(), pwd.getText().toString()));
            } catch (Exception e) {
                out.setText("操作失败: " + Arrays.toString(e.getStackTrace()));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}