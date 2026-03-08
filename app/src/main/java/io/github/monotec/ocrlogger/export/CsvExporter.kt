package io.github.monotec.ocrlogger.export

import android.content.Context
import android.os.Environment
import io.github.monotec.ocrlogger.model.SendLogEntry
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExporter {

    fun exportLogs(
        context: Context,
        logs: List<SendLogEntry>
    ): String {
        val fileName = buildFileName()

        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val appDir = File(downloadDir, "OCRLog")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        val file = File(appDir, fileName)
        val csvText = CsvFormatter.buildCsv(logs)

        file.writeText(csvText, Charsets.UTF_8)

        return file.absolutePath
    }

    private fun buildFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN)
        return "ocr_log_${sdf.format(Date())}.csv"
    }
}