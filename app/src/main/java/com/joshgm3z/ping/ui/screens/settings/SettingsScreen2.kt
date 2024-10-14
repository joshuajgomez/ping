package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.theme.PingTheme

@DarkPreview
@Composable
fun PreviewSettingsScreen() {
    PingTheme {
        SettingsScreen2()
    }
}

data class Setting(
    val title: String,
    val subTitle: String = "",
    val action: () -> Unit = {},
)

val settings = listOf(
    Setting("Profile", "Edit profile details"),
    Setting("Chat", "Change chat settings"),
    Setting("Notifications", "Modify how often you wanna be notified"),
    Setting("Account", "Change account settings"),
    Setting("Storage", "See how ping is using phones memory"),
    Setting("Sign out", "Sign out from ping profile"),
)

@Composable
fun SettingsScreen2(
    modifier: Modifier = Modifier,
    userName: String = "Diddly",
    imageRes: Int = R.drawable.default_user,
) {
    Column(modifier.fillMaxSize()) {
        ProfileView(userName, imageRes)
        Spacer(Modifier.height(20.dp))
        ElevatedCard(
            modifier = Modifier.padding(horizontal = 15.dp),
            colors = CardDefaults.elevatedCardColors().copy(
                containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
            )
        ) {
            LazyColumn {
                itemsIndexed(settings) { index, item ->
                    SettingItem(item)
                    if (index < settings.lastIndex) HorizontalDivider(thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
fun ProfileView(userName: String, imageRes: Int) {
    Column(
        Modifier
            .padding(top = 30.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            userName,
            color = colorScheme.onSurface,
            fontSize = 25.sp
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "$userName@google.com",
            color = colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun SettingItem(setting: Setting) {
    Column(
        Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Text(setting.title, fontSize = 18.sp, color = colorScheme.onSurface)
        Spacer(Modifier.height(5.dp))
        Text(setting.subTitle, fontSize = 15.sp, color = colorScheme.onSurface.copy(alpha = 0.7f))
    }
}
