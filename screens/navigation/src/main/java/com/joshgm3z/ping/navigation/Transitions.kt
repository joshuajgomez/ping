package com.joshgm3z.ping.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

val slideIn: (AnimatedContentTransitionScope<NavBackStackEntry>.() ->
@JvmSuppressWildcards EnterTransition?) = {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Start,
        tween(700)
    )
}

val slideOut: (AnimatedContentTransitionScope<NavBackStackEntry>.() ->
@JvmSuppressWildcards ExitTransition?) = {
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.End, tween(700)
    )
}