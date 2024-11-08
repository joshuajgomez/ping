package com.joshgm3z.common.home

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
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