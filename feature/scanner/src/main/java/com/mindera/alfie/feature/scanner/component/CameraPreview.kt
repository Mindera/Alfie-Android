package com.mindera.alfie.feature.scanner.component

import androidx.camera.core.ImageAnalysis
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import timber.log.Timber
import java.util.concurrent.Executors

/**
 * CameraX preview with a frame analyzer, hosted in Compose.
 *
 * Uses [LifecycleCameraController] rather than a raw `ProcessCameraProvider`: binding to the
 * lifecycle is what makes teardown automatic — the camera is released on `ON_STOP` and rebound on
 * `ON_START`, so backgrounding the app mid-scan does not leave the device held or crash on
 * return. The controller resolves the provider itself while still exposing the two things this
 * screen depends on: the backpressure strategy, and attaching or detaching the analyzer.
 *
 * [isActive] gates the analyzer, not the preview: once a barcode has won, frames stop being
 * handed to ML Kit while the image stays on screen through the navigation transition.
 *
 * Known, benign: entering the scanner opens the camera twice —
 * `OPENING → OPEN → CLOSING → OPENING → OPEN`, the second open landing ~300ms in, alongside a
 * second "Surface requested by Preview". [PreviewView]'s surface is recreated once during initial
 * layout, which invalidates the Preview use case and makes CameraX rebind. It is not caused by
 * this file's ordering: attaching the analyzer before [LifecycleCameraController.bindToLifecycle],
 * attaching the preview surface before it, and swapping
 * [PreviewView.ImplementationMode.COMPATIBLE] for `PERFORMANCE` were each measured and all three
 * produce the identical five transitions. Don't re-chase it from here — the cost is one extra
 * camera open on entry, and removing it means not letting `PreviewView` own the surface.
 */
@Composable
internal fun CameraPreview(
    analyzer: ImageAnalysis.Analyzer,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onBindFailure: (Throwable) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(LifecycleCameraController.IMAGE_ANALYSIS)
            // ML Kit inference is slower than the frame rate: drop stale frames instead of
            // queueing them, or latency compounds until the preview visibly lags.
            imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
        }
    }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            // TextureView, not SurfaceView: the nav host sits inside two nested Scaffolds and a
            // SurfaceView can punch through Compose's clipping and z-order.
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    LaunchedEffect(Unit) {
        // Throws on a device with no usable camera, which is the CameraUnavailable path.
        runCatching {
            controller.bindToLifecycle(lifecycleOwner)
            previewView.controller = controller
        }.onFailure { throwable ->
            Timber.e(throwable, "Failed to bind the camera")
            onBindFailure(throwable)
        }
    }

    LaunchedEffect(isActive) {
        if (isActive) {
            controller.setImageAnalysisAnalyzer(analysisExecutor, analyzer)
        } else {
            controller.clearImageAnalysisAnalyzer()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            controller.clearImageAnalysisAnalyzer()
            controller.unbind()
            analysisExecutor.shutdown()
        }
    }

    AndroidView(factory = { previewView }, modifier = modifier)
}
