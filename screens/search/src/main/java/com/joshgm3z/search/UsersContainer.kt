package com.joshgm3z.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.common.CustomTextField
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.HomeAppBar
import com.joshgm3z.common.SmallCard
import com.joshgm3z.common.getIfNotPreview
import com.joshgm3z.data.model.User
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.data.util.randomUsers
import com.joshgm3z.search.viewmodels.SearchUiState
import com.joshgm3z.search.viewmodels.SearchViewModel
import com.joshgm3z.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@DarkPreview
@Composable
fun PreviewSearchContainer(
    uiState: SearchUiState = SearchUiState.AllUsers(randomUsers()),
    query: String = "",
) = PingTheme {
    Scaffold(
        topBar = {
            HomeAppBar()
        },
    ) { paddingValues ->
        SearchContent(
            Modifier.padding(paddingValues),
            MutableStateFlow(uiState),
            MutableStateFlow(query),
        )
    }
}

@DarkPreview
@Composable
fun PreviewSearchContainerUsersEmpty() {
    PreviewSearchContainer(
        SearchUiState.Info("Fetching..."), "qwerty"
    )
}

@Composable
fun EmptyScreen(message: String = "You are first!") {
    SmallCard {
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
    viewModel: SearchViewModel? = getIfNotPreview { hiltViewModel() },
    onUserClick: (user: User) -> Unit,
) {
    SearchContent(
        uiState = viewModel?.uiState ?: MutableStateFlow(SearchUiState.Info("Initial")),
        query = viewModel?.queryFlow ?: MutableStateFlow(""),
        onUserClick = onUserClick,
        onSearchInputChanged = viewModel!!::onSearchInputChanged,
    )
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    uiState: StateFlow<SearchUiState>,
    query: StateFlow<String>,
    onUserClick: (User) -> Unit = {},
    onSearchInputChanged: (String) -> Unit = {},
) {
    Column(modifier = modifier) {
        SearchBar(query) {
            onSearchInputChanged(it)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            with(uiState.collectAsState().value) {
                when (this) {
                    is SearchUiState.AllUsers -> UserList(
                        users = users,
                        onSearchItemClick = onUserClick
                    )

                    is SearchUiState.SearchResult -> UserList(
                        users = users,
                        onSearchItemClick = onUserClick
                    )

                    is SearchUiState.Info -> InfoBox(message)
                }
            }
        }
    }
}

@Composable
fun InfoBox(message: String) {
    Text(
        text = message,
        modifier = Modifier.padding(top = 40.dp)
    )
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
        com.joshgm3z.common.UserImage(
            modifier = Modifier
                .size(50.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start, margin = 10.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            imageUrl = user.imagePath
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
private fun SearchBar(
    query: StateFlow<String>,
    onTextChanged: (String) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val text = query.collectAsState().value
        CustomTextField(
            text = text,
            hintText = "Search for user",
            modifier = Modifier.padding(
                vertical = 10.dp,
                horizontal = 10.dp
            ),
            onTextChanged = {
                onTextChanged(it)
            },
            onEnterPressed = {},
            isFocusNeeded = false,
        )
    }
}
