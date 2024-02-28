package com.joshgm3z.ping.ui.screens.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.theme.Comfortaa
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel

@Preview
@Composable
fun PreviewHomeAppBar() {
    PingTheme {
        HomeAppBar()
    }
}

@Composable
fun HomeAppBarContainer(homeViewModel: HomeViewModel) {
    val appTitleState = homeViewModel.appTitle.collectAsState()
    HomeAppBar(title = appTitleState.value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(title: String = "Chats") {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    )
}

@Composable
fun AppTitle() {
    Row {
        Text(
            text = "ping",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 30.sp,
            fontFamily = Comfortaa,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}