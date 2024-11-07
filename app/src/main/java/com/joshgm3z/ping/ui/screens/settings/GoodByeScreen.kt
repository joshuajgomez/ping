package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.PingButton
import com.joshgm3z.ping.graph.Frx
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.common.theme.Red20

@DarkPreview
@Composable
fun PreviewGoodByeScreen() {
    PingTheme {
        GoodByeScreen()
    }
}

@Composable
fun GoodByeScreen(
    navController: NavHostController = rememberNavController(),
    onClick: () -> Unit = {
        navController.navigate(Frx)
    }
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 30.dp,
                horizontal = 20.dp
            ),
    ) {
        Column {
            Text(
                text = "Signed out",
                color = Red20,
                fontSize = 45.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Good bye, come back any time.",
                color = colorScheme.onSurface,
                fontSize = 20.sp,
                lineHeight = 60.sp,
            )
        }
        PingButton(
            "Go to sign in",
            onClick = onClick
        )
    }
}