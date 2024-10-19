package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.joshgm3z.data.model.Chat
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.ImageViewerViewModel
import com.joshgm3z.ping.utils.getPrettyTime
import kotlinx.serialization.Serializable

@Serializable
data class ChatImageViewer(
    val chatId: String,
)

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
fun ImageBox(chat: Chat, modifier: Modifier) {
    AsyncImage(
        chat.imageUrl,
        contentDescription = null,
        error = painterResource(R.drawable.wallpaper2),
        modifier = Modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PingTopBar(
    title: String,
    subTitle: String,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    color = colorScheme.onSurface,
                    fontSize = 20.sp,
                )
                Text(
                    text = subTitle,
                    color = colorScheme.onSurface,
                    fontSize = 15.sp,
                )
            }
        },
        navigationIcon = {
            IconButton(onBackClick) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp)
                )
            }
        }
    )
}
