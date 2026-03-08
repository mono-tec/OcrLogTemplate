package io.github.monotec.ocrlogger.export

import io.github.monotec.ocrlogger.model.SendLogEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvFormatter {

    private val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN)

    fun buildCsv(logs: List<SendLogEntry>): String {
        val header = "timestamp,text,result"

        val body = logs.joinToString("\n") { log ->
            val timestamp = sdf.format(Date(log.timestampEpochMillis))

            listOf(
                escapeCsv(timestamp),
                escapeCsv(log.text),
                escapeCsv(log.result)
            ).joinToString(",")
        }

        return if (body.isBlank()) {
            header
        } else {
            "$header\n$body"
        }
    }

    private fun escapeCsv(value: String): String {
        val escaped = value.replace("\"", "\"\"")
        return "\"$escaped\""
    }
}