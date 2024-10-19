package com.joshgm3z.ping.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.joshgm3z.ping.R
import com.joshgm3z.ping.graph.Loading
import com.joshgm3z.ping.ui.theme.PingTheme

@Preview
@Composable
fun PreviewLoadingContainer() {
    PingTheme {
        LoadingContainer()
    }
}

fun NavController.navigateToLoading(message: String) =
    navigate(Loading(message))

@Composable
fun LoadingContainer(message: String = "Loading") {
    PingWallpaper {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(color = colorScheme.surface)
                .padding(30.dp)
                .width(200.dp)
                .height(150.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = colorScheme.primary,
                progress = { 8f }
            )
            Spacer(Modifier.height(40.dp))
            Text(
                text = message,
                fontSize = 20.sp,
                color = colorScheme.onSurface
            )
        }
    }
}

@Composable
fun PingWallpaper(
    content: @Composable ColumnScope.() -> Unit
) {
    Box {
        Image(
            painter = painterResource(R.drawable.chat_wallpaper),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            content = content,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        )
    }
}
