package io.github.monotec.ocrlogger.ocr

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.math.max
import kotlin.math.min

class OcrAnalyzerRoi(
    private val guideInfoProvider: () -> GuideInfo?,
    private val onTextDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage == null) {
            image.close()
            return
        }

        val rotation = image.imageInfo.rotationDegrees
        val input = InputImage.fromMediaImage(mediaImage, rotation)

        recognizer.process(input)
            .addOnSuccessListener { visionText ->
                val guide = guideInfoProvider()

                if (guide == null) {
                    onTextDetected(visionText.text.orEmpty())
                    return@addOnSuccessListener
                }

                val (imgW, imgH) = rotatedSize(image.width, image.height, rotation)

                val roi = viewRectToImageRectFillCenter(
                    viewW = guide.viewW,
                    viewH = guide.viewH,
                    rectLeft = guide.rectLeft,
                    rectTop = guide.rectTop,
                    rectW = guide.rectW,
                    rectH = guide.rectH,
                    imgW = imgW,
                    imgH = imgH
                )

                val linesInRoi = visionText.textBlocks
                    .flatMap { it.lines }
                    .filter { line ->
                        val b = line.boundingBox ?: return@filter false
                        val cx = (b.left + b.right) / 2
                        val cy = (b.top + b.bottom) / 2
                        roi.contains(cx, cy)
                    }
                    .map { it.text.trim() }
                    .filter { it.isNotBlank() }

                onTextDetected(linesInRoi.joinToString(separator = "\n"))
            }
            .addOnFailureListener {
                // no-op
            }
            .addOnCompleteListener {
                image.close()
            }
    }

    private fun rotatedSize(w: Int, h: Int, rotation: Int): Pair<Int, Int> =
        if (rotation == 90 || rotation == 270) Pair(h, w) else Pair(w, h)

    private fun viewRectToImageRectFillCenter(
        viewW: Int,
        viewH: Int,
        rectLeft: Float,
        rectTop: Float,
        rectW: Float,
        rectH: Float,
        imgW: Int,
        imgH: Int
    ): Rect {
        val srcW = imgW.toFloat()
        val srcH = imgH.toFloat()

        val scale = max(viewW / srcW, viewH / srcH)

        val scaledW = srcW * scale
        val scaledH = srcH * scale

        val offsetX = (scaledW - viewW) / 2f
        val offsetY = (scaledH - viewH) / 2f

        val x0 = (rectLeft + offsetX) / scale
        val y0 = (rectTop + offsetY) / scale
        val x1 = (rectLeft + rectW + offsetX) / scale
        val y1 = (rectTop + rectH + offsetY) / scale

        val left = clampInt(x0.toInt(), 0, imgW - 1)
        val top = clampInt(y0.toInt(), 0, imgH - 1)
        val right = clampInt(x1.toInt(), left + 1, imgW)
        val bottom = clampInt(y1.toInt(), top + 1, imgH)

        return Rect(left, top, right, bottom)
    }

    private fun clampInt(v: Int, minV: Int, maxV: Int): Int =
        min(maxV, max(minV, v))
}