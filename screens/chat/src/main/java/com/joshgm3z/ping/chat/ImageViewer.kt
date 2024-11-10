package com.joshgm3z.ping.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.OnlineImage
import com.joshgm3z.common.PingTopBar
import com.joshgm3z.common.defaultUserImage
import com.joshgm3z.common.getIfNotPreview
import com.joshgm3z.common.navigation.ChatImageViewer
import com.joshgm3z.data.model.Chat
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.ping.chat.viewmodels.ImageViewerViewModel
import com.joshgm3z.utils.getPrettyTime

fun NavController.navigateToImageViewer(
    chatId: String
) = navigate(ChatImageViewer(chatId))

@DarkPreview
@Composable
private fun PreviewImageViewer() {
    PingTheme {
        ImageViewer()
    }
}

@Composable
fun ImageViewer(
    onBackClick: () -> Unit = {},
    viewModel: ImageViewerViewModel? = getIfNotPreview { hiltViewModel() }
) {
    val chat = viewModel?.chatFlow?.collectAsState()?.value ?: Chat.random()
    val name = getIfNotPreview { viewModel?.name } ?: "Someone"
    Scaffold(
        topBar = {
            PingTopBar(
                name,
                getPrettyTime(chat.sentTime),
                onBackClick
            )
        },
        bottomBar = {
            ImageViewerBottomBar()
        }
    ) {
        ImageBox(chat, Modifier.padding(it))
    }
}

@Composable
fun ImageViewerBottomBar(
    onShareClick: () -> Unit = {},
    onForwardClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    BottomAppBar {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomIcon(Icons.Outlined.IosShare, onShareClick)
            BottomIcon(Icons.Outlined.ArrowOutward, onForwardClick)
            BottomIcon(Icons.Outlined.Delete, onDeleteClick)
        }
    }
}

@Composable
fun BottomIcon(
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    IconButton(onClick) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(5.dp),
            tint = colorScheme.primary
        )
    }
}

@Composable
fun ImageBox(
    chat: Chat,
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        OnlineImage(
            model = chat.fileOnlineUrl,
            error = defaultUserImage(),
            modifier = Modifier.fillMaxSize(),
        )
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            if (chat.message.isNotEmpty()) {
                Text(
                    text = chat.message,
                    fontSize = 20.sp,
                    color = colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorScheme.surface.copy(alpha = 0.5f))
                        .padding(20.dp)
                )
            }
        }
    }
}
