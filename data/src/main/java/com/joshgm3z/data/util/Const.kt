package com.joshgm3z.data.util

sealed class FileType {
    data object Image : FileType()
    open class File : FileType()
    data object Pdf : File()
    data object Exe : File()
}

fun getFileType(type: String): FileType? {
    if (type.isEmpty()) return null
    return when (type) {
        "pdf" -> FileType.Pdf
        "jpeg", "jpg", "png" -> FileType.Image
        else -> FileType.File()
    }
}