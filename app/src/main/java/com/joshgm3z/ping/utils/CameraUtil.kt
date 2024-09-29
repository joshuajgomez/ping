package com.joshgm3z.ping.utils

import android.annotation.SuppressLint
import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.joshgm3z.ping.HomeActivity
import java.io.File
import java.util.concurrent.ScheduledExecutorService

class CameraUtil constructor(val activity: HomeActivity) {

    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
        ProcessCameraProvider.getInstance(activity)

    private val imageCapture = ImageCapture.Builder().build()

    // Just like ImageCapture, you can configure the Preview use case if you'd wish.
    val preview = Preview.Builder().build()

    // Provide PreviewView's Surface to CameraX. The preview will be drawn on it.
    fun takePicture(
        outputFileOptions: ImageCapture.OutputFileOptions,
        mainThreadExecutor: ScheduledExecutorService,
        param: ImageCapture.OnImageSavedCallback,
    ) {
        cameraProviderFuture.get().bindToLifecycle(
            activity,
            CameraSelector.DEFAULT_BACK_CAMERA,
            imageCapture
        )

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(activity), // Defines where the callbacks are run
            object : ImageCapture.OnImageCapturedCallback() {
                @OptIn(ExperimentalGetImage::class)
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    val image: Image? = imageProxy.image // Do what you want with the image
                    imageProxy.close() // Make sure to close the image
                }

                override fun onError(exception: ImageCaptureException) {
                    // Handle exception
                }
            }
        )
    }

    @SuppressLint("RestrictedApi")
    fun ll() {
        val imageFile =
            File("somePath/someName.jpg") // You can store the image in the cache for example using `cacheDir.absolutePath` as a path.
        val outputFileOptions = ImageCapture.OutputFileOptions
            .Builder(imageFile)
            .build()
        takePicture(
            outputFileOptions,
            CameraXExecutors.mainThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                }

                override fun onError(exception: ImageCaptureException) {
                }
            }
        )
    }


}