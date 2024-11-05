package com.joshgm3z.repository.download

import android.content.Context
import com.joshgm3z.data.model.Chat
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadManager @Inject constructor(
    scope: CoroutineScope,
    context: Context,
    private val downloadWorker: DownloadWorker,
    private val chatRepository: ChatRepository,
) {
    private var runningTask: DownloadTask? = null

    private val downloadTasksQueue = HashMap<String, DownloadTask>()

    private val destinationFolder = context.filesDir.absolutePath + "/ping_downloads"

    init {
        val folder = File(destinationFolder)
        if (!folder.exists() || !folder.isDirectory) {
            Files.createDirectories(Paths.get(destinationFolder));
        }

        scope.launch {
            chatRepository.observeChatsForFileDownload().collectLatest {
                Logger.debug("it = [$it]")
                addToDownloads(it)
            }
        }

        scope.launch {
            downloadWorker.downloadTaskFlow.collectLatest {
                when {
                    it == null -> {
                        Logger.warn("DownloadTask is null")
                    }

                    it.downloadState == DownloadState.Finished -> {
                        // prepare queue for next download
                        downloadTasksQueue.remove(it.chatId)
                        runningTask = null
                        chatRepository.updateLocalFileUrl(it.chatId, it.file.path)
                        doNextTask()
                    }
                }
            }
        }
    }

    private fun addToDownloads(chatList: List<Chat>) {
        chatList.forEach {
            with(it) {
                when {
                    downloadTasksQueue.contains(docId) -> {} // already in queue
                    fileName.isEmpty() -> {}// file name not set
                    fileOnlineUrl.isEmpty() -> {}// no url to download
                    fileLocalUri.isNotEmpty() -> {}// file already downloaded
                    else -> {
                        Logger.debug("adding $fileOnlineUrl")
                        downloadTasksQueue[docId] = DownloadTask(
                            docId,
                            fileOnlineUrl,
                            File(destinationFolder, "chat_file_$docId.$fileType"),
                        )
                    }
                }
            }
        }
        doNextTask()
    }

    private fun doNextTask() {
        Logger.entry()
        if (runningTask != null) {
            Logger.debug("Task already running: $runningTask")
            return
        }
        if (downloadTasksQueue.values.isEmpty()) {
            Logger.warn("Queue empty")
            return
        }
        val downloadTask = downloadTasksQueue.values.first()
        runningTask = downloadTask
        downloadWorker.download(downloadTask)
    }

    fun taskUpdates(): StateFlow<DownloadTask?> = downloadWorker.downloadTaskFlow
}