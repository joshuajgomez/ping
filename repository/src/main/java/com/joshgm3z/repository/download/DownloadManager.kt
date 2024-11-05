package com.joshgm3z.repository.download

import android.os.Environment
import com.joshgm3z.data.model.Chat
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadManager @Inject constructor(
    scope: CoroutineScope,
    private val downloadWorker: DownloadWorker,
    private val chatRepository: ChatRepository,
) {

    private var runningTask: DownloadTask? = null

    private val downloadTasksQueue = HashMap<String, DownloadTask>()

    private val destinationFolder = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOWNLOADS
    ).path + "/ping_downloads/"

    private fun addToDownloads(chatList: List<Chat>) {
        chatList.forEach {
            if (!downloadTasksQueue.contains(it.docId)) {
                val task = DownloadTask(
                    it.docId,
                    it.fileOnlineUrl,
                    destinationFolder + it.fileName,
                )
                downloadTasksQueue[it.docId] = task
            }
        }
    }

    init {
        Logger.entry()
        scope.launch {
            chatRepository.observeChatsForUserHomeLocal().collectLatest { it ->
                val newDownloads = it.filter {
                    it.fileOnlineUrl.isNotEmpty() && it.fileLocalUri.isEmpty()
                }
                addToDownloads(newDownloads)
                doNextTask()
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
                        chatRepository.updateLocalFileUrl(it.chatId, it.destinationUrl)
                        doNextTask()
                    }
                }
            }
        }
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