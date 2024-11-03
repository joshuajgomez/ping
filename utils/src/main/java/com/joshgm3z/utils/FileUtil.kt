package com.joshgm3z.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FileUtil @Inject constructor(
    private val context: Context,
) {
    companion object {
        private fun createImageFile(context: Context): File {
            // Create an image file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                "JPEG_${timeStamp}_", //prefix
                ".jpg", //suffix
                storageDir //directory
            )
        }

        fun getUri(context: Context): Uri? = FileProvider.getUriForFile(
            context,
            "com.joshgm3z.ping.fileprovider",
            createImageFile(context)
        )

        fun getFileSizeString(context: Context, fileUri: Uri): String {
            getContentResolver(context, fileUri)?.let {
                val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                val fileSize = it.getLong(sizeIndex)

                val fileSizeInKB = fileSize / 1024
                return if (fileSizeInKB >= 1024) {
                    "${fileSizeInKB / 1024} MB"
                } else {
                    "$fileSizeInKB KB"
                }
            } ?: return ""
        }

        fun getFileName(context: Context, fileUri: Uri): String {
            getContentResolver(context, fileUri)?.let {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val fileName = it.getString(nameIndex).toString()
                return fileName
            } ?: return ""
        }

        @SuppressLint("Recycle")
        private fun getContentResolver(context: Context, fileUri: Uri): Cursor? {
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(fileUri, null, null, null, null)
            cursor?.moveToFirst()
            return cursor
        }

        fun getFileTypeString(context: Context, fileUri: Uri): String {
            val contentResolver = context.contentResolver
            val mime = MimeTypeMap.getSingleton()
            val type = mime.getExtensionFromMimeType(contentResolver.getType(fileUri))
            return type ?: ""
        }
    }

    fun getFileName(fileUri: Uri): String {
        return getFileName(context, fileUri)
    }

    fun getFileSizeString(imageUri: Uri): String {
        return getFileSizeString(context, imageUri)
    }

    fun getFileTypeString(fileUri: Uri): String {
        return getFileTypeString(context, fileUri)
    }
}
