package com.joshgm3z.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.Setting
import com.joshgm3z.common.SettingContainer
import com.joshgm3z.common.SettingListCard
import com.joshgm3z.common.getIfNotPreview
import com.joshgm3z.data.model.User
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.settings.viewmodels.UserViewModel
import com.joshgm3z.utils.getPrettyTime

@DarkPreview
@Composable
private fun PreviewProfileSettings() {
    PingTheme {
        ProfileSettings()
    }
}

@Composable
fun ProfileSettings(
    userViewModel: UserViewModel? = getIfNotPreview { hiltViewModel() },
    openImagePicker: () -> Unit = {},
    openEditScreen: (String) -> Unit = {},
    onGoBackClick: () -> Unit = {}
) {
    val user: User = userViewModel?.me ?: randomUser()
    SettingContainer("Edit profile", onCloseClick = onGoBackClick) {
        ImageSetting(
            user,
            openImagePicker
        )
        val aboutSetting = when {
            user.about.isEmpty() -> Setting(
                "Add bio",
                icon = Icons.Default.Add,
                textColor = colorScheme.primary
            ) { openEditScreen("about") }

            else -> Setting(
                "Bio",
                user.about,
            ) { openEditScreen("about") }
        }
        val list = listOf(
            Setting(
                "Name",
                user.name
            ) { openEditScreen("name") },
            aboutSetting,
            Setting("Joined on", getPrettyTime(user.dateOfJoining)),
        )
        Spacer(Modifier.height(20.dp))
        SettingListCard(list)
    }
}

@Composable
fun ImageSetting(
    user: User,
    onImageClick: () -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
        ),
        onClick = onImageClick
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            com.joshgm3z.common.UserImage(
                imageUrl = user.imagePath,
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.width(15.dp))
            Column {
                Text("Set an optional profile picture")
                Spacer(Modifier.height(5.dp))
                Button(
                    onClick = onImageClick,
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = colorScheme.surfaceContainerHighest
                    )
                ) {
                    Text("Change", color = colorScheme.onSurface)
                }
            }
        }
    }
}
