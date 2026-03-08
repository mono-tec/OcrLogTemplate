package io.github.monotec.ocrlogger.validation

import java.text.Normalizer

data class ValidationResult(
    val ok: Boolean,
    val normalized: String,
    val message: String? = null
)

/**
 * 仕様（Phase1）:
 * - 許可: A-Z, 0-9
 * - 大文字化
 * - 全角→半角（NFKC）
 * - trim
 * - 1..32
 */
fun normalizeAndValidate(raw: String, maxLen: Int = 32): ValidationResult {
    val trimmed = raw.trim()
    if (trimmed.isEmpty()) {
        return ValidationResult(false, "", "文字が未入力です")
    }

    // 全角→半角など（NFKC） + 大文字化
    val nfkc = Normalizer.normalize(trimmed, Normalizer.Form.NFKC).uppercase()

    // 許可文字のみ抽出
    val filtered = nfkc.filter { it in 'A'..'Z' || it in '0'..'9' }

    // 禁止文字が含まれていたか（警告用）
    val hadInvalid = nfkc.any { !(it in 'A'..'Z' || it in '0'..'9') }

    if (filtered.isEmpty()) {
        return ValidationResult(false, "", "英数字（A-Z,0-9）のみ入力してください")
    }
    if (filtered.length > maxLen) {
        return ValidationResult(false, filtered.take(maxLen), "最大${maxLen}文字までです")
    }

    // 「無効文字が混ざってた」場合はNGにする運用（仕様どおり）
    if (hadInvalid) {
        return ValidationResult(false, filtered, "英数字以外が含まれています（削除してから確定してください）")
    }

    return ValidationResult(true, filtered, "OK")
}