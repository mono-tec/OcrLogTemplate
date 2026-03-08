package io.github.monotec.ocrlogger.settings

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_settings")

class SettingsRepository(
    private val context: Context
) {
    companion object {
        private val OCR_READ_MODE = stringPreferencesKey("ocr_read_mode")
    }

    val ocrReadMode: Flow<OcrReadMode> = context.dataStore.data.map { preferences ->
        when (preferences[OCR_READ_MODE]) {
            OcrReadMode.UNLIMITED.name -> OcrReadMode.UNLIMITED
            else -> OcrReadMode.ASCII_ONLY
        }
    }

    suspend fun saveOcrReadMode(mode: OcrReadMode) {
        context.dataStore.edit { preferences: MutablePreferences ->
            preferences[OCR_READ_MODE] = mode.name
        }
    }
}