package io.github.monotec.ocrlogger.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class LicenseGroup(
    val licenseName: String,
    val note: String? = null,
    val libraries: List<String>
)

@Composable
fun LicenseScreen(
    modifier: Modifier = Modifier
) {
    val groups = listOf(
        LicenseGroup(
            licenseName = "Apache License 2.0",
            libraries = listOf(
                "Jetpack Compose",
                "CameraX",
                "Room",
                "DataStore",
                "Navigation Compose",
                "Material Icons Extended"
            )
        ),
        LicenseGroup(
            licenseName = "Google Fonts / Material Icons",
            note = "Google Fonts および Android Studio Image Asset Studio の Material icons / symbols を使用しています。",
            libraries = listOf(
                "Home menu icon (camera_scan)",
                "Android Studio Image Asset Studio clip art",
                "Material Icons / Material Symbols"
            )
        ),
        LicenseGroup(
            licenseName = "Google ML Kit Terms",
            note = "Text Recognition は Google ML Kit を使用しています。配布や表記の詳細は公式ドキュメントをご確認ください。",
            libraries = listOf(
                "ML Kit Text Recognition"
            )
        )

    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(groups) { group ->
                LicenseGroupCard(group = group)
            }
        }
    }
}

@Composable
private fun LicenseGroupCard(
    group: LicenseGroup
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = group.licenseName,
                style = MaterialTheme.typography.titleMedium
            )

            if (!group.note.isNullOrBlank()) {
                Text(
                    text = group.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            group.libraries.forEach { lib ->
                Text(
                    text = "• $lib",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}