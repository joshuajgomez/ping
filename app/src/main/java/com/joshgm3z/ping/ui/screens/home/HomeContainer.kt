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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.ping.graph.ChatScreen
import com.joshgm3z.ping.ui.screens.search.UserContainer
import com.joshgm3z.ping.ui.screens.search.UserList
import com.joshgm3z.ping.ui.screens.settings.MainSettingsScreen
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Preview
@Composable
fun PreviewHomeScreenList() {
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
fun PreviewHomeScreenListEmpty() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
            bottomBar = { PingBottomAppBar() },
        ) {
            HomeChatList(
                modifier = Modifier.padding(it),
                homeChats = emptyList()
            )
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreenSettings() {
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
                        homeNavController.navigate(HomeRoute.UserList) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(homeNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
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
            NavigationBarItem(
                icon = { Icon(imageVector = route.icon, contentDescription = null) },
                selected = currentDestination?.hierarchy?.any { it == route } == true,
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
