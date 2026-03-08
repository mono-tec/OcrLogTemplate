package io.github.monotec.ocrlogger.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.monotec.ocrlogger.database.SendLogDao
import io.github.monotec.ocrlogger.model.SendLogEntry
import io.github.monotec.ocrlogger.ocr.GuideInfo
import io.github.monotec.ocrlogger.ocr.OcrAnalyzerRoi
import io.github.monotec.ocrlogger.settings.OcrReadMode
import io.github.monotec.ocrlogger.settings.SettingsRepository
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

private enum class CaptureState { Idle, Scanning, Confirmed }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    dao: SendLogDao,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var hasCameraPermission by rememberSaveable {
        mutableStateOf(context.hasPermission(Manifest.permission.CAMERA))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    var state by rememberSaveable { mutableStateOf(CaptureState.Idle) }
    var candidateText by rememberSaveable { mutableStateOf("") }
    var confirmedText by rememberSaveable { mutableStateOf("") }
    var userEdited by rememberSaveable { mutableStateOf(false) }
    var zoomRatio by rememberSaveable { mutableFloatStateOf(1f) }
    var cameraHandle by remember { mutableStateOf<CameraHandle?>(null) }

    var lastAppliedMs by rememberSaveable { mutableLongStateOf(0L) }
    val throttleMs = 500L

    var saving by rememberSaveable { mutableStateOf(false) }
    var saveStatus by rememberSaveable { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val textToSave = remember(confirmedText) { confirmedText.trim() }

    val repository = remember { SettingsRepository(context) }
    val readMode by repository.ocrReadMode.collectAsStateWithLifecycle(
        initialValue = OcrReadMode.ASCII_ONLY
    )

    val modeDescription1 = when (readMode) {
        OcrReadMode.ASCII_ONLY -> "ASCII文字のみ対象です"
        OcrReadMode.UNLIMITED -> "文字種制限なしで読取します"
    }

    val modeDescription2 = when (readMode) {
        OcrReadMode.ASCII_ONLY -> "（半角英数字・記号・スペース）"
        OcrReadMode.UNLIMITED -> "（日本語・英数字・記号を含む）"
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padScaffold ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padScaffold)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (!hasCameraPermission) {
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("カメラ権限が必要です")
                        Button(
                            onClick = {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        ) {
                            Text("権限を許可")
                        }
                    }
                }
            } else {
                if (state == CaptureState.Scanning) {
                    Card {
                        CameraPreviewWithOcr(
                            zoomRatio = zoomRatio,
                            onZoomRatioChanged = { zoomRatio = it },
                            onTextUpdated = { raw ->
                                if (userEdited) return@CameraPreviewWithOcr

                                val now = System.currentTimeMillis()
                                if (now - lastAppliedMs < throttleMs) return@CameraPreviewWithOcr

                                val normalized = when (readMode) {
                                    OcrReadMode.ASCII_ONLY -> normalizeAsciiWithSpace(raw)
                                    OcrReadMode.UNLIMITED -> normalizeUnlimited(raw)
                                }
                                if (normalized.isBlank()) return@CameraPreviewWithOcr
                                if (normalized == candidateText) return@CameraPreviewWithOcr

                                candidateText = normalized
                                lastAppliedMs = now
                            },
                            onCameraHandle = { handle ->
                                cameraHandle = handle
                            }
                        )
                    }

                    Text(
                        text = if (candidateText.isBlank()) {
                            "読取中…（文字が検出されると表示されます）"
                        } else {
                            "読取候補：$candidateText"
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Card {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "「読込開始」でカメラを起動します",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = modeDescription1,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = modeDescription2,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = confirmedText,
                onValueChange = {
                    confirmedText = it
                    userEdited = true
                },
                label = { Text("保存する文字（編集可）") },
                placeholder = { Text("例: ABC 123") },
                singleLine = true,
                enabled = !saving,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (state) {
                    CaptureState.Idle -> {
                        Button(
                            onClick = {
                                if (hasCameraPermission) {
                                    state = CaptureState.Scanning
                                    candidateText = ""
                                    userEdited = false
                                    lastAppliedMs = 0L
                                    saveStatus = null
                                }
                            },
                            enabled = hasCameraPermission && !saving,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("読込開始")
                        }
                    }

                    CaptureState.Scanning -> {
                        OutlinedButton(
                            onClick = {
                                cameraHandle?.stop()
                                cameraHandle = null
                                state = CaptureState.Idle
                                candidateText = ""
                            },
                            enabled = !saving,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("キャンセル")
                        }

                        Button(
                            onClick = {
                                confirmedText = candidateText
                                cameraHandle?.stop()
                                cameraHandle = null
                                state = CaptureState.Confirmed
                                userEdited = false
                                saveStatus = null
                            },
                            enabled = candidateText.isNotBlank() && !saving,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("確定")
                        }
                    }

                    CaptureState.Confirmed -> {
                        OutlinedButton(
                            onClick = {
                                state = CaptureState.Scanning
                                candidateText = ""
                                userEdited = false
                                lastAppliedMs = 0L
                                saveStatus = null
                            },
                            enabled = hasCameraPermission && !saving,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("再読込")
                        }

                        Button(
                            onClick = {
                                if (textToSave.isBlank()) return@Button

                                scope.launch {
                                    saving = true
                                    val result = try {
                                        dao.insert(
                                            SendLogEntry(
                                                timestampEpochMillis = System.currentTimeMillis(),
                                                text = textToSave,
                                                result = "OK"
                                            )
                                        )
                                        Result.success("保存OK")
                                    } catch (t: Throwable) {
                                        Result.failure(t)
                                    }
                                    saving = false

                                    val msg = if (result.isSuccess) {
                                        result.getOrNull() ?: "保存OK"
                                    } else {
                                        "保存NG: ${result.exceptionOrNull()?.message ?: "Unknown"}"
                                    }

                                    saveStatus = msg
                                    snackbarHostState.showSnackbar(msg)
                                }
                            },
                            enabled = textToSave.isNotBlank() && !saving,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (saving) "保存中..." else "保存")
                        }
                    }
                }
            }

            if (!saveStatus.isNullOrBlank()) {
                Card {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("保存結果: $saveStatus")
                    }
                }
            }
        }
    }
}

private fun normalizeAsciiWithSpace(raw: String): String {
    val s = raw.replace(Regex("[\\t\\r\\n]+"), " ").trim()
    val filtered = s.filter { it.code in 0x20..0x7E }
    return filtered.replace(Regex(" +"), " ")
}

private fun normalizeUnlimited(raw: String): String {
    return raw
        .replace(Regex("[\\t\\r\\n]+"), " ")
        .trim()
        .replace(Regex(" +"), " ")
}

private fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

private interface CameraHandle {
    fun stop()
}

@Composable
private fun CameraPreviewWithOcr(
    zoomRatio: Float,
    onZoomRatioChanged: (Float) -> Unit,
    onTextUpdated: (String) -> Unit,
    onCameraHandle: (CameraHandle) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    var cameraControl by remember { mutableStateOf<androidx.camera.core.CameraControl?>(null) }
    var maxZoom by remember { mutableFloatStateOf(3f) }
    var guideInfo by remember { mutableStateOf<GuideInfo?>(null) }

    LaunchedEffect(Unit) {
        val provider = ProcessCameraProvider.getInstance(context).get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val executor = Executors.newSingleThreadExecutor()
        analysis.setAnalyzer(
            executor,
            OcrAnalyzerRoi(
                guideInfoProvider = { guideInfo },
                onTextDetected = onTextUpdated
            )
        )

        val selector = CameraSelector.DEFAULT_BACK_CAMERA

        provider.unbindAll()
        val camera = provider.bindToLifecycle(
            lifecycleOwner,
            selector,
            preview,
            analysis
        )

        cameraControl = camera.cameraControl
        maxZoom = camera.cameraInfo.zoomState.value?.maxZoomRatio ?: 3f

        onCameraHandle(object : CameraHandle {
            override fun stop() {
                provider.unbindAll()
            }
        })
    }

    LaunchedEffect(zoomRatio, cameraControl) {
        cameraControl?.setZoomRatio(zoomRatio)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 280.dp)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .onSizeChanged { s ->
                    val vw = s.width
                    val vh = s.height

                    val boxW = vw * 0.80f
                    val boxH = vh * 0.35f
                    val left = (vw - boxW) / 2f
                    val top = (vh - boxH) / 2f

                    guideInfo = GuideInfo(
                        viewW = vw,
                        viewH = vh,
                        rectLeft = left,
                        rectTop = top,
                        rectW = boxW,
                        rectH = boxH
                    )
                }
        ) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )
            OcrGuideOverlay(modifier = Modifier.fillMaxSize())
        }

        Text("ズーム: ${"%.1f".format(zoomRatio)}x")

        Slider(
            value = zoomRatio.coerceIn(1f, maxZoom),
            onValueChange = { onZoomRatioChanged(it) },
            valueRange = 1f..maxZoom
        )
    }
}

@Composable
private fun OcrGuideOverlay(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val boxWidth = size.width * 0.80f
        val boxHeight = size.height * 0.35f

        val left = (size.width - boxWidth) / 2f
        val top = (size.height - boxHeight) / 2f

        val rectSize = Size(boxWidth, boxHeight)
        val rectTopLeft = Offset(left, top)

        drawRect(
            color = Color.Black.copy(alpha = 0.25f),
            size = size
        )

        drawRoundRect(
            color = Color.White.copy(alpha = 0.95f),
            topLeft = rectTopLeft,
            size = rectSize,
            cornerRadius = CornerRadius(18f, 18f),
            style = Stroke(
                width = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f), 0f)
            )
        )

        val cornerLen = 28f
        val stroke = 6f
        val c = Color.White.copy(alpha = 0.95f)

        drawLine(c, rectTopLeft, rectTopLeft + Offset(cornerLen, 0f), strokeWidth = stroke)
        drawLine(c, rectTopLeft, rectTopLeft + Offset(0f, cornerLen), strokeWidth = stroke)

        val rt = rectTopLeft + Offset(rectSize.width, 0f)
        drawLine(c, rt, rt + Offset(-cornerLen, 0f), strokeWidth = stroke)
        drawLine(c, rt, rt + Offset(0f, cornerLen), strokeWidth = stroke)

        val lb = rectTopLeft + Offset(0f, rectSize.height)
        drawLine(c, lb, lb + Offset(cornerLen, 0f), strokeWidth = stroke)
        drawLine(c, lb, lb + Offset(0f, -cornerLen), strokeWidth = stroke)

        val rb = rectTopLeft + Offset(rectSize.width, rectSize.height)
        drawLine(c, rb, rb + Offset(-cornerLen, 0f), strokeWidth = stroke)
        drawLine(c, rb, rb + Offset(0f, -cornerLen), strokeWidth = stroke)
    }
}