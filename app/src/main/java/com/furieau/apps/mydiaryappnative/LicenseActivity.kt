package com.furieau.apps.mydiaryappnative

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.furieau.apps.mydiaryappnative.ui.theme.MyDiaryAppNativeTheme
import androidx.core.content.edit

class LicenseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val titleRes = intent.getIntExtra("TITLE_RES", R.string.license_title)
        val contentRes = intent.getIntExtra("CONTENT_RES", R.raw.license)
        val type = intent.getStringExtra("TYPE") ?: "license"
        setContent {
            MyDiaryAppNativeTheme {
                var licenseText by remember { mutableStateOf("加载中...") }

                // 加载许可证文本
                LaunchedEffect(Unit) {
                    licenseText = loadLicenseText(contentRes)
                }

                LicenseScreen(
                    title = stringResource(titleRes),
                    licenseText = licenseText,
                    onContinue = {
                        saveLicenseAccepted(type)
                        val intent = Intent(this, LauncherActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onCancel = {
                        finishAffinity()
                    }
                )
            }
        }
    }

    private fun loadLicenseText(resid: Int): String {
        return try {
            resources.openRawResource(resid)
                .bufferedReader()
                .use { it.readText() }
        } catch (e: Exception) {
            "Failed to load LICENSE: $e"
        }
    }

    private fun saveLicenseAccepted(type: String) {
        val prefs = getSharedPreferences("MyDiaryPrefs", MODE_PRIVATE)
        prefs.edit {
            when(type) {
                "license" -> putBoolean("accepted_license", true)
                "privacy" -> putBoolean("accepted_privacy", true)
            }
        }
    }
}

@Composable
fun LicenseScreen(
    title: String,
    licenseText: String,  // 从参数传入
    onContinue: () -> Unit,
    onCancel: () -> Unit
) {
    var isAgreed by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            BottomButtons(
                isAgreed = isAgreed,
                onContinue = onContinue,
                onCancel = onCancel
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = licenseText,
                style = MaterialTheme.typography.bodySmall,  // 小一点字号
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isAgreed,
                    onCheckedChange = { isAgreed = it }
                )
                Text(
                    text = stringResource(R.string.license_agree_text),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun BottomButtons(
    isAgreed: Boolean,
    onContinue: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 取消按钮
        Button(
            onClick = onCancel,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("取消")
        }

        // 继续按钮
        Button(
            onClick = onContinue,
            enabled = isAgreed  // 只有同意后才能点击
        ) {
            Text("继续")
        }
    }
}
