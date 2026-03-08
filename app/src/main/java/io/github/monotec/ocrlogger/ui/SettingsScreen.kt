package io.github.monotec.ocrlogger.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.monotec.ocrlogger.settings.OcrReadMode
import io.github.monotec.ocrlogger.settings.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { SettingsRepository(context) }
    val mode = repository.ocrReadMode.collectAsStateWithLifecycle(
        initialValue = OcrReadMode.ASCII_ONLY
    ).value

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "OCR読取モード",
                    style = MaterialTheme.typography.titleMedium
                )

                OcrModeRow(
                    text = "ASCIIのみ",
                    description = "半角英数字・記号・スペースを対象に読み取ります。\n型番・品番・シリアル番号の読取に向いています。",
                    selected = mode == OcrReadMode.ASCII_ONLY,
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            repository.saveOcrReadMode(OcrReadMode.ASCII_ONLY)
                        }
                    }
                )

                OcrModeRow(
                    text = "無制限",
                    description = "文字種制限なしで読み取ります。\n現在はラテン文字OCRライブラリを使用しているため、日本語OCRは将来対応予定です。",
                    selected = mode == OcrReadMode.UNLIMITED,
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            repository.saveOcrReadMode(OcrReadMode.UNLIMITED)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun OcrModeRow(
    text: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 48.dp, top = 2.dp)
        )
    }
}