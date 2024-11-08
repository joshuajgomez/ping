package com.joshgm3z.ping.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.HomeAppBar
import com.joshgm3z.common.HomeAppBarContainer
import com.joshgm3z.common.home.HomeRoute
import com.joshgm3z.common.home.PingBottomAppBar
import com.joshgm3z.common.navigation.ChatScreen
import com.joshgm3z.search.UserContainer
import com.joshgm3z.search.UserList
import com.joshgm3z.settings.MainSettingsScreen
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.ping.home.viewmodels.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow

@DarkPreview
@Composable
private fun PreviewHomeScreenList() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
            bottomBar = { PingBottomAppBar() },
        ) {
            HomeChatList(modifier = Modifier.padding(it))
        }
    }
}

@DarkPreview
@Composable
private fun PreviewHomeScreenListEmpty() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
            bottomBar = { PingBottomAppBar() },
        ) {
            HomeChatList(
                modifier = Modifier.padding(it),
                uiState = MutableStateFlow(HomeUiState.Empty())
            )
        }
    }
}

@DarkPreview
@Composable
private fun PreviewHomeScreenSettings() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
            bottomBar = { PingBottomAppBar() },
        ) {
            MainSettingsScreen(modifier = Modifier.padding(it))
        }
    }
}

@DarkPreview
@Composable
fun PreviewHomeScreenUsers() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
            bottomBar = { PingBottomAppBar() },
        ) {
            UserList(modifier = Modifier.padding(it))
        }
    }
}

@Composable
fun HomeScreenContainer(
    navController: NavController = rememberNavController(),
    onSearchClick: () -> Unit = {},
) {
    val homeNavController = rememberNavController()
    val appTitleFlow = MutableStateFlow("Ping")
    Scaffold(
        topBar = {
            val title = appTitleFlow.collectAsState().value
            HomeAppBarContainer(title)
        },
        bottomBar = {
            PingBottomAppBar(homeNavController)
        },
    ) { paddingValues ->
        NavHost(
            navController = homeNavController,
            startDestination = HomeRoute.ChatList,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<HomeRoute.ChatList> {
                appTitleFlow.value = "Chats"
                HomeChatListContainer(
                    onChatClick = { chat ->
                        navController.navigate(ChatScreen(chat.otherGuy.docId))
                    },
                    onGoToUsersClicked = {
                        homeNavController.navigateToUsers()
                    },
                    onSearchClick = onSearchClick
                )
            }
            composable<HomeRoute.UserList> {
                appTitleFlow.value = "Users"
                UserContainer(
                    onUserClick = { user ->
                        navController.navigate(ChatScreen(user.docId))
                    })
            }
            composable<HomeRoute.SettingsList> {
                appTitleFlow.value = "Settings"
                MainSettingsScreen(navController = navController)
            }
        }
    }
}

private fun NavController.navigateToUsers() {
    navigate(HomeRoute.UserList) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}

@DarkPreview
@Composable
fun NavIcon(
    imageVector: ImageVector = Icons.Rounded.ChatBubble,
    isSelected: Boolean = false,
    onNavIconClick: () -> Unit = {},
) {
    IconButton(onClick = { onNavIconClick() }) {
        Icon(
            imageVector = imageVector, contentDescription = null,
            tint = if (isSelected) colorScheme.primaryContainer else colorScheme.outline
        )
    }
}
