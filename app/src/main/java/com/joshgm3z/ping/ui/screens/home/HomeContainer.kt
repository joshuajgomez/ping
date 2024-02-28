package com.joshgm3z.ping.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.ping.model.data.HomeChat
import com.joshgm3z.ping.model.data.User
import com.joshgm3z.ping.ui.screens.search.UserContainer
import com.joshgm3z.ping.ui.screens.search.UserList
import com.joshgm3z.ping.ui.screens.settings.SettingScreenContainer
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.viewmodels.SignInViewModel
import com.joshgm3z.ping.ui.viewmodels.UserViewModel

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
            SettingScreenContainer(modifier = Modifier.padding(it))
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

const val navChatList = "Chats"
const val navUserList = "Users"
const val navSettings = "Settings"

sealed class HomeNavScreen(val route: String, val icon: ImageVector) {
    object ChatList : HomeNavScreen(navChatList, Icons.Rounded.ChatBubble)
    object UserList : HomeNavScreen(navUserList, Icons.Rounded.Group)
    object Settings : HomeNavScreen(navSettings, Icons.Rounded.Settings)
}

val homeNavItems = listOf(
    HomeNavScreen.ChatList,
    HomeNavScreen.UserList,
    HomeNavScreen.Settings,
)

@Composable
fun HomeScreenContainer(
    homeViewModel: HomeViewModel,
    userViewModel: UserViewModel,
    signInViewModel: SignInViewModel,
    onUserClick: (user: User) -> Unit = {},
    onChatClick: (homeChat: HomeChat) -> Unit = {},
    onLoggedOut: () -> Unit = {},
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            HomeAppBarContainer(homeViewModel = homeViewModel)
        },
        bottomBar = {
            PingBottomAppBar(navController)
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = navChatList,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = navChatList) {
                homeViewModel.setAppTitle(navChatList)
                HomeChatListContainer(
                    homeViewModel = homeViewModel,
                    onChatClick = { onChatClick(it) },
                    onGoToUsersClicked = {
                        navController.navigate(navUserList) {
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
                    },
                )
            }
            composable(route = navUserList) {
                homeViewModel.setAppTitle(navUserList)
                UserContainer(
                    userViewModel = userViewModel,
                    onUserClick = { onUserClick(it) })
            }
            composable(route = navSettings) {
                homeViewModel.setAppTitle("Settings")
                SettingScreenContainer(
                    name = signInViewModel.currentUser!!.name,
                    imagePath = signInViewModel.currentUser!!.imagePath,
                    onSignOutClick = {
                        signInViewModel.onSignOutClicked()
                        onLoggedOut()
                    },
                )
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
        homeNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
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
