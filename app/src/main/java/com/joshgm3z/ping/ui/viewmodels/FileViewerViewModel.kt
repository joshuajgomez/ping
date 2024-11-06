package com.joshgm3z.ping.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.joshgm3z.ping.graph.PdfViewerRoute
import com.joshgm3z.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject

sealed class FileViewerUiState {
    data class Empty(val fileUrl: String = "") : FileViewerUiState()
    data class Ready(val bitmapList: List<Bitmap>) : FileViewerUiState()
}

@HiltViewModel
class FileViewerViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val fileUrl: String = savedStateHandle
        .toRoute<PdfViewerRoute>()
        .fileLocalUrl

    private val _uiState = MutableStateFlow<FileViewerUiState>(FileViewerUiState.Empty(fileUrl))
    val uiState = _uiState.asStateFlow()

    fun fetchBitmapList(
        context: Context,
    ) {
        Logger.debug("fileUrl = [$fileUrl]")
        _uiState.value = try {
            context.contentResolver.openFileDescriptor((File(fileUrl).toUri()), "r")
                ?.let {
                    val renderer = PdfRenderer(it)
                    val bitmapList = mutableListOf<Bitmap>()
                    for (i in 0 until renderer.pageCount) {
                        renderer.openPage(i).apply {
                            createBitmap(width, height).let { bitmap ->
                                render(
                                    bitmap, null, null,
                                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                )
                                bitmapList.add(bitmap)
                            }
                            close()
                        }
                    }
                    renderer.close()
                    it.close()
                    FileViewerUiState.Ready(bitmapList)
                } ?: FileViewerUiState.Empty(fileUrl)
        } catch (e: Exception) {
            Logger.error(e.stackTraceToString())
            FileViewerUiState.Empty(fileUrl)
        }
    }
}
