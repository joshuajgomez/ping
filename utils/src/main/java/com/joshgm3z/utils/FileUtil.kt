package com.joshgm3z.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

class FileUtil {
    companion object {
        fun createImageFile(context: Context): File {
            // Create an image file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                "JPEG_${timeStamp}_", //prefix
                ".jpg", //suffix
                storageDir //directory
            )
        }

        fun getUri(context: Context, file: File): Uri = FileProvider.getUriForFile(
            context,
            "com.joshgm3z.ping.fileprovider",
            file
        )
    }
}