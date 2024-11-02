package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.data.util.getChatList
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.ping.R
import com.joshgm3z.ping.graph.UserInfo
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Red20
import com.joshgm3z.ping.ui.viewmodels.UserInfoUiState
import com.joshgm3z.ping.ui.viewmodels.UserInfoViewModel

@DarkPreview
@Composable
private fun PreviewMediaCard() {
    PingTheme {
        MediaCard(mediaChats = emptyList()) {}
    }
}

@DarkPreview
@Composable
private fun PreviewUserInfo() {
    PingTheme {
        UserInfoContent(mediaChats = getChatList())
    }
}

fun NavController.navigateToUserInfo(
    userId: String,
) = navigate(
    UserInfo(userId)
)

@Composable
fun UserInfo(
    viewModel: UserInfoViewModel? = getIfNotPreview { hiltViewModel() },
    onGoBackClick: () -> Unit = {},
    showClearSuccess: () -> Unit = {},
    showClearError: () -> Unit = {},
    openImageViewer: (chat: Chat) -> Unit = {},
) {
    val uiState = viewModel?.uiState?.collectAsState()
    with(uiState?.value) {
        when (this) {
            is UserInfoUiState.Ready -> UserInfoContent(
                user,
                mediaChats,
                onGoBackClick,
                openImageViewer = openImageViewer,
                clearChats = {
                    viewModel?.clearChats(
                        onClear = showClearSuccess,
                        onError = showClearError
                    )
                },
            )

            else -> {}
        }
    }


}

@Composable
fun UserInfoContent(
    user: User = randomUser(),
    mediaChats: List<Chat> = getChatList(),
    onGoBackClick: () -> Unit = {},
    clearChats: () -> Unit = {},
    openImageViewer: (chat: Chat) -> Unit = {},
) {
    SettingContainer(
        "Contact info",
        onCloseClick = onGoBackClick
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            UserImage(
                imageUrl = user.imagePath,
                modifier = Modifier.size(150.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                user.name, fontSize = 25.sp,
                color = colorScheme.onSurface
            )
            if (user.about.isNotEmpty()) {
                Spacer(Modifier.height(5.dp))
                Text(
                    user.about, fontStyle = FontStyle.Italic,
                    color = colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Spacer(Modifier.height(30.dp))

            MediaCard(mediaChats, openImageViewer)
            Spacer(Modifier.height(20.dp))

            val settingList = listOf(
                Setting(
                    "Add to favorites", "Add user to favorite list",
                    icon = Icons.Default.StarBorder
                ),
            )
            val settingList2 = listOf(
                Setting(
                    "Clear chat",
                    icon = Icons.Default.Delete,
                    textColor = Red20,
                    action = clearChats,
                ),
                Setting(
                    "Report user",
                    icon = Icons.Default.Report,
                    textColor = Red20
                ),
            )

            SettingListCard(settingList)
            Spacer(Modifier.height(20.dp))

            SettingListCard(settingList2)
        }
    }
}

@Composable
fun MediaCard(
    mediaChats: List<Chat>,
    openImageViewer: (chat: Chat) -> Unit,
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(
                    vertical = 10.dp,
                    horizontal = 5.dp
                )
            ) {
                Icon(
                    Icons.Default.PermMedia,
                    contentDescription = null
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "Media in chat",
                    fontSize = 18.sp
                )
            }
            Spacer(Modifier.height(10.dp))
            if (mediaChats.isEmpty()) {
                Text(
                    "No media in chat",
                    color = colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(mediaChats) {
                        MediaItem(it) { openImageViewer(it) }
                    }
                }
            }
        }
    }
}

@Composable
fun MediaItem(
    chat: Chat,
    openImageViewer: () -> Unit,
) {
    Box(Modifier.clickable { openImageViewer() }) {
        AsyncImage(
            chat.imageUrl,
            error = painterResource(R.drawable.default_user),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(5.dp))
        )
    }
}
