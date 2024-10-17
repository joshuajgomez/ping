package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel

@DarkPreview
@Composable
private fun PreviewImagePreview() {
    PingTheme {
        ImagePreview()
    }
}

@Composable
fun ImagePreview(
    navController: NavController = rememberNavController(),
    chatViewModel: ChatViewModel? = getIfNotPreview { hiltViewModel() },
    name: String = "alien",
    userId: String = "alien",
    imageUrl: String = "",
    onBackClick: () -> Unit = {
        navController.popBackStack()
    },
    onSendClick: () -> Unit = {
        chatViewModel?.onSendButtonClick(imageUrl = imageUrl)
        navController.popBackStack()
    }
) {
    chatViewModel?.setUser(userId)
    SettingContainer(
        "Send image",
        onCloseClick = onBackClick
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Recipient(name)
                Spacer(Modifier.width(10.dp))
                SendButton(
                    onClick = onSendClick
                )
            }
        }
    }
}

@Composable
fun Recipient(name: String) {
    Text(
        name,
        fontSize = 20.sp,
        color = colorScheme.onSurface,
        modifier = Modifier
            .background(
                color = colorScheme.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp, vertical = 3.dp)
    )
}

@Composable
private fun SendButton(
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    IconButton(
        enabled = enabled,
        onClick = {
            onClick()
        },
        modifier = Modifier.padding(end = 5.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "send message",
            modifier = Modifier
                .clip(CircleShape)
                .size(35.dp)
                .background(color = colorScheme.onSecondary)
                .padding(all = 7.dp),
            tint = colorScheme.secondary
        )
    }
}