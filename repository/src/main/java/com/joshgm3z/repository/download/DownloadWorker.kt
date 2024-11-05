package com.joshgm3z.repository.download

import android.app.DownloadManager
import android.content.Context
import com.joshgm3z.utils.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DownloadManagerModule {
    @Provides
    fun providesDownloadManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
}

data class DownloadTask(
    val chatId: String,
    val url: String,
    val destinationUrl: String,
    var downloadState: DownloadState = DownloadState.Pending,
    val downloadedSize: Long = 0,
    val totalSize: Long = 0,
)

enum class DownloadState {
    Pending,
    Started,
    Finished,
    Stopped,
    Failed,
}

@Singleton
class DownloadWorker @Inject constructor(
    private val scope: CoroutineScope,
    private val downloadService: DownloadService
) {

    private val _downloadTaskFlow = MutableStateFlow<DownloadTask?>(null)
    val downloadTaskFlow: StateFlow<DownloadTask?> = _downloadTaskFlow

    private var isStopped = false
        set(value) {
            field = value
            if (value) {
                _downloadTaskFlow.update {
                    it?.copy(
                        downloadState = DownloadState.Stopped
                    )
                }
            }
        }

    fun download(
        downloadTask: DownloadTask,
    ) {
        Logger.debug("downloadTask = [${downloadTask}]")
        _downloadTaskFlow.value = downloadTask
        scope.launch {
            downloadService
                .downloadFile(downloadTask.url)
                .saveFile(downloadTask.destinationUrl)
        }
    }

    private fun notifyProgress(currentSize: Long) {
        _downloadTaskFlow.update {
            /*val progress =
                if (it!!.totalSize > 0)
                    ((currentSize * 100) / it.totalSize)
                else 0
            it.copy(
                progress = progress,
                down = currentSize
            )*/
            it?.copy(downloadedSize = currentSize)
        }
    }

    private fun ResponseBody.saveFile(destinationPath: String) {
        val file = File(destinationPath)
        var currentSize: Long = 0
        notifyStart()
        try {
            byteStream().use { inputStream ->
                file.outputStream().use { outputStream ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var bytes = inputStream.read(buffer)
                    while (bytes >= 0) {
                        if (isStopped) {
                            close()
                            return@use
                        }
                        outputStream.write(buffer, 0, bytes)
                        currentSize += bytes
                        bytes = inputStream.read(buffer)
                        notifyProgress(currentSize)
                    }
                }
                if (isStopped) {
                    close()
                    Logger.debug("Download stopped: ${_downloadTaskFlow.value}")
                    return
                }
            }
            notifyCompletion(currentSize)
        } catch (e: Exception) {
            Logger.error(e.message ?: "Download failed with exception")
            notifyFailure()
        }
    }

    private fun notifyStart() {
        Logger.entry()
        _downloadTaskFlow.update {
            it?.copy(
                downloadState = DownloadState.Started,
            )
        }
    }

    private fun notifyFailure() {
        Logger.entry()
        _downloadTaskFlow.update {
            it?.copy(
                downloadState = DownloadState.Failed,
            )
        }
    }

    private fun notifyCompletion(totalBytes: Long) {
        Logger.debug("totalBytes = [${totalBytes}]")
        _downloadTaskFlow.update {
            it?.copy(
                downloadState = DownloadState.Finished,
                totalSize = totalBytes,
            )
        }
    }
}