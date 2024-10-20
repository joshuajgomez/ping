package com.joshgm3z.ping.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.data.model.User
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.CustomTextField
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.data.util.randomUsers
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.screens.home.HomeAppBarContainer
import com.joshgm3z.ping.ui.screens.home.PingBottomAppBar
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.ping.ui.viewmodels.UsersUiState
import com.joshgm3z.utils.Logger

@Preview
@Composable
fun PreviewSearchContainer() {
    PingTheme {
        Scaffold(
            topBar = {
                HomeAppBarContainer("Users")
            },
            bottomBar = {
                PingBottomAppBar()
            },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                SearchBar()
                UserList()
            }
        }
    }
}

@Preview
@Composable
fun PreviewSearchContainerEmpty() {
    PingTheme {
        Column {
            SearchBar()
            UserList(users = emptyList())
        }
    }
}

@Preview
@Composable
fun PreviewEmpty() {
    PingTheme {
        EmptyScreen()
    }
}

@Composable
fun EmptyScreen(message: String = "You are first!") {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 130.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.EmojiPeople,
            contentDescription = "empty list",
            modifier = Modifier.size(50.dp),
            tint = colorScheme.primary
        )
        Text(
            text = message, fontSize = 18.sp, color = Color.Gray, fontWeight = FontWeight.Bold
        )
        Text(
            text = "No other users yet", fontSize = 15.sp, color = Color.Gray
        )
    }
}

@Composable
fun UserContainer(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    onUserClick: (user: User) -> Unit,
) {
    Column(modifier = modifier) {
        SearchBar()
        val uiState = userViewModel.uiState.collectAsState()
        when (uiState.value) {
            is UsersUiState.Ready -> UserList(
                users = (uiState.value as UsersUiState.Ready).users
            ) {
                onUserClick(it)
            }
        }
    }
}

@Composable
fun UserList(
    modifier: Modifier = Modifier,
    users: List<User> = randomUsers(),
    onSearchItemClick: (user: User) -> Unit = {}
) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (users.isNotEmpty()) {
            LazyColumn(modifier = modifier) {
                items(items = users) { it ->
                    SearchItem(it) { onSearchItemClick(it) }
                }
            }
        } else {
            EmptyScreen()
        }
    }
}

@Composable
fun SearchItem(
    user: User = randomUser(),
    onSearchItemClick: (user: User) -> Unit = {}
) {
    ConstraintLayout(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .clickable { onSearchItemClick(user) }
            .padding(start = 20.dp)
    ) {
        Logger.debug("user = [$user]")
        val (image, name, about) = createRefs()
        UserImage(
            modifier = Modifier
                .size(50.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start, margin = 10.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            user = user
        )
        Text(
            text = user.name,
            fontSize = 20.sp,
            color = colorScheme.onSurface,
            modifier = Modifier.constrainAs(name) {
                start.linkTo(image.end, margin = 20.dp)
                top.linkTo(image.top, margin = 2.dp)
            })
        Text(
            text = user.about,
            fontSize = 15.sp,
            color = colorScheme.onSurface.copy(alpha = 0.5f),
            fontStyle = FontStyle.Italic,
            modifier = Modifier.constrainAs(about) {
                start.linkTo(name.start)
                top.linkTo(name.bottom, margin = 2.dp)
            })
    }
}

@Composable
fun SearchBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        var text by remember { mutableStateOf("") }
        CustomTextField(
            text = text,
            hintText = "Search for user",
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
            onTextChanged = { text = it },
            onEnterPressed = {},
            isFocusNeeded = false,
        )
    }
}
