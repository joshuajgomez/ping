package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.data.model.User
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel

@DarkPreview
@Composable
fun PreviewUserInfo() {
    PingTheme {
        UserInfo()
    }
}

@Composable
fun UserInfo(
    user: User = randomUser(),
    onGoBackClick: () -> Unit = {}
) {
    SettingContainer(
        "Contact info",
        onCloseClick = onGoBackClick
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(20.dp))
            UserImage(
                imageUrl = user.imagePath,
                modifier = Modifier.size(150.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                user.name, fontSize = 25.sp,
                color = colorScheme.onSurface
            )
            Spacer(Modifier.height(5.dp))
            Text(
                user.about, fontStyle = FontStyle.Italic,
                color = colorScheme.onSurface.copy(alpha = 0.5f)
            )

            Spacer(Modifier.height(30.dp))

            val settingList = listOf(
                Setting(
                    "Add to favorites", "Add user to favorite list",
                    icon = Icons.Default.StarBorder
                ),
                Setting(
                    "Clear chat", "Clear all messages in this chat",
                    icon = Icons.Default.Delete
                ),
                Setting(
                    "Report user", "Report this user for breaking rules",
                    icon = Icons.Default.Report
                ),
            )

            SettingListCard(settingList)
        }
    }
}