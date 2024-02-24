package com.joshgm3z.ping.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.ping.data.HomeChat
import com.joshgm3z.ping.ui.search.EmptyScreen
import com.joshgm3z.ping.ui.theme.Comfortaa
import com.joshgm3z.ping.ui.theme.Gray40
import com.joshgm3z.ping.ui.theme.Purple60
import com.joshgm3z.ping.utils.getHomeChatList
import com.joshgm3z.ping.viewmodels.HomeUiState
import com.joshgm3z.ping.viewmodels.HomeViewModel

@Composable
fun HomeScreenContainer(
    homeViewModel: HomeViewModel,
    onSearchClick: () -> Unit = {},
    onChatClick: (homeChat: HomeChat) -> Unit = {},
) {
    val uiState = homeViewModel.uiState.collectAsState()
    when (uiState.value) {
        is HomeUiState.Empty -> EmptyScreen()
        is HomeUiState.Ready -> HomeScreen(
            (uiState.value as HomeUiState.Ready).homeChats,
            onSearchClick = { onSearchClick() },
            onChatClick = { onChatClick(it) },
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HomeScreen(
    homeChats: List<HomeChat> = getHomeChatList(),
    onSearchClick: () -> Unit = {},
    onChatClick: (homeChat: HomeChat) -> Unit = {},
) {
    Column {
        HomeTitle { onSearchClick() }
        HomeChatList(homeChats) {
            onChatClick(it)
        }
    }
}

@Composable
fun HomeTitle(onSearchClick: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .background(Purple60)
    ) {
        val (title, search) = createRefs()
        Text(
            text = "ping",
            color = Color.White,
            fontSize = 30.sp,
            fontFamily = Comfortaa,
            modifier = Modifier
                .padding(top = 5.dp)
                .constrainAs(title) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "search",
            tint = Gray40,
            modifier = Modifier
                .constrainAs(search) {
                    end.linkTo(parent.end, margin = 15.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clip(CircleShape)
                .clickable { onSearchClick() })
    }
}
