package io.github.monotec.ocrlogger.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "send_log_entry")
data class SendLogEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestampEpochMillis: Long,
    val text: String,
    val result: String
)