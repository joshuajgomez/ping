package com.joshgm3z.ping.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.ping.navigation.ChatScreen
import com.joshgm3z.ping.ui.screens.search.UserContainer
import com.joshgm3z.ping.ui.screens.search.UserList
import com.joshgm3z.ping.ui.screens.settings.MainSettingsScreen
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Preview
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

@Preview
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

@Preview
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

@Preview
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

@Serializable
sealed class HomeRoute(
    @Transient val icon: ImageVector = Icons.Rounded.QuestionMark,
) {
    @Serializable
    data object ChatList : HomeRoute(Icons.Rounded.ChatBubble)

    @Serializable
    data object UserList : HomeRoute(Icons.Rounded.Group)

    @Serializable
    data object SettingsList : HomeRoute(Icons.Rounded.Settings)
}

val homeNavItems = listOf(
    HomeRoute.ChatList,
    HomeRoute.UserList,
    HomeRoute.SettingsList,
)

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
                        navController.navigate(com.joshgm3z.ping.navigation.ChatScreen(chat.otherGuy.docId))
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
                        navController.navigate(com.joshgm3z.ping.navigation.ChatScreen(user.docId))
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

@Preview
@Composable
fun PreviewPingBottomAppBar() {
    PingTheme {
        PingBottomAppBar()
    }
}

@Composable
fun PingBottomAppBar(navController: NavController = rememberNavController()) {
    BottomAppBar(tonalElevation = 1.dp) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        homeNavItems.forEach { route ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(route::class)
            } == true
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = route.icon, contentDescription = null,
                        tint = when {
                            isSelected -> colorScheme.primary
                            else -> colorScheme.onSurface.copy(alpha = 0.5f)
                        }
                    )
                },
                selected = false,
                onClick = {
                    navController.navigate(route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview
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
