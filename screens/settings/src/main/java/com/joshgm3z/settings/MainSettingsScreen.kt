package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.HomeAppBarContainer
import com.joshgm3z.common.Setting
import com.joshgm3z.common.SettingListCard
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
        Scaffold(
            topBar = {
                HomeAppBarContainer("Settings")
            },
            bottomBar = {
                PingBottomAppBar()
            },
        ) { paddingValues ->
            MainSettingsScreen(modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun MainSettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val settingRouteList1 = listOf(
        Setting(
            "Profile",
            "Edit profile details"
        ) {
            navController.navigate(Profile)
        },
        Setting(
            "Chat Settings",
            "Change chat settings"
        ) {
            navController.navigate(ChatSettings)
        },
        Setting(
            "Notifications",
            "Modify how often you wanna be notified"
        ) {
            navController.navigate(Notifications)
        },
    )

    val settingRouteList2 = listOf(
        Setting(
            "Sign out",
            icon = Icons.Default.Output
        ) {
            navController.navigate(SignOut)
        },
    )

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        ProfileView {
            navController.navigate(Profile)
        }
        Spacer(Modifier.height(20.dp))
        SettingListCard(settingRouteList1)
        Spacer(Modifier.height(20.dp))
        SettingListCard(settingRouteList2)
    }
}

@Composable
fun ProfileView(
    userViewModel: UserViewModel? = getIfNotPreview { hiltViewModel() },
    onClick: () -> Unit = {}
) {
    val user = userViewModel?.me ?: randomUser()
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = colorScheme.surface
        ),
        onClick = onClick
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            com.joshgm3z.common.UserImage(
                imageUrl = user.imagePath,
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape),
            )
            Spacer(Modifier.height(20.dp))
            Text(
                user.name, color = colorScheme.onSurface, fontSize = 25.sp
            )
            Spacer(Modifier.height(5.dp))
            Text(
                user.about,
                fontStyle = FontStyle.Italic,
                color = colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
        }
    }
}

