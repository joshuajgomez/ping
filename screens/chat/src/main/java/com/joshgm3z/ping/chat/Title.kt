package com.joshgm3z.ping.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.data.model.User
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.data.util.randomUser

@DarkPreview
@Composable
fun PreviewChatAppBar() {
    PingTheme {
        ChatAppBar()
    }
}

@Composable
fun ChatAppBar(
    modifier: Modifier = Modifier,
    user: User = randomUser(),
    onUserInfoClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorScheme.surface,
            )
            .padding(5.dp)
    ) {
        IconButton(
            onBackClick,
            modifier = Modifier.padding(1.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "go to home",
                tint = colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(80.dp)
                    .padding(5.dp)
            )
        }
        Row(
            modifier
                .clickable { onUserInfoClick() }
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            com.joshgm3z.common.UserImage(
                modifier = Modifier
                    .size(45.dp),
                imageUrl = user.imagePath
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = user.name,
                color = colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.widthIn(80.dp)
            )
        }
        IconButton({}) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "more options",
                tint = colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp)
            )
        }
    }
}
