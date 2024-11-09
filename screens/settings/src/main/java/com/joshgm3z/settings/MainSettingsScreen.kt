package com.joshgm3z.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.Setting
import com.joshgm3z.common.SettingContainer
import com.joshgm3z.common.SettingListCard
import com.joshgm3z.common.UserImage
import com.joshgm3z.common.getIfNotPreview
import com.joshgm3z.common.navigation.ChatSettings
import com.joshgm3z.common.navigation.Notifications
import com.joshgm3z.common.navigation.Profile
import com.joshgm3z.common.navigation.SignOut
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.settings.viewmodels.UserViewModel

@DarkPreview
@Composable
fun PreviewMainSettingsScreen() {
    PingTheme {
        MainSettingsScreen()
    }
}

@Composable
fun MainSettingsScreen(
    navigateTo: (Any) -> Unit = {},
) {
    val settingRouteList1 = getSettingList(navigateTo)
    val settingRouteList2 = listOf(
        Setting("Sign out", icon = Icons.Default.Output) {
            navigateTo(SignOut)
        },
    )
    SettingContainer("Settings") {
        ProfileView { navigateTo(Profile) }
        Spacer(Modifier.height(20.dp))
        SettingListCard(settingRouteList1)
        Spacer(Modifier.height(20.dp))
        SettingListCard(settingRouteList2)
    }
}

fun getSettingList(navigateTo: (Any) -> Unit) = listOf(
    Setting(
        "Profile",
        "Edit profile details"
    ) {
        navigateTo(Profile)
    },
    Setting(
        "Chat Settings",
        "Change chat settings"
    ) {
        navigateTo(ChatSettings)
    },
    Setting(
        "Notifications",
        "Modify how often you wanna be notified"
    ) {
        navigateTo(Notifications)
    },
)

@Composable
fun ProfileView(
    userViewModel: UserViewModel? = getIfNotPreview { hiltViewModel() },
    onClick: () -> Unit = {}
) {
    val user = userViewModel?.me ?: randomUser()
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = colorScheme.onPrimary
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = user.name,
                    fontSize = 18.sp,
                    color = colorScheme.primary,
                )
                Text(
                    user.about,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    color = colorScheme.onSurface.copy(alpha = 0.7f),
                )
            }
            UserImage(
                imageUrl = user.imagePath,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
            )
        }
    }
}

