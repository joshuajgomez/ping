package com.joshgm3z.ping.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.ping.R
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.ui.common.CustomTextField
import com.joshgm3z.ping.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.theme.Purple60
import com.joshgm3z.ping.utils.randomUser
import com.joshgm3z.ping.utils.randomUsers
import com.joshgm3z.ping.viewmodels.SearchUiState

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSearchContainer() {
    Column {
        SearchBar()
        SearchList()
//        EmptyScreen()
    }
}

@Composable
fun EmptyScreen(message: String = "No users found") {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
    ) {
        Icon(
            imageVector = Icons.Default.NoAccounts,
            contentDescription = "empty list",
            modifier = Modifier.size(50.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = message, fontSize = 18.sp, color = Color.Gray
        )
    }
}

@Composable
fun SearchContainer(
    homeViewModel: HomeViewModel,
    onSearchItemClick: (user: User) -> Unit,
    onCancelClick: () -> Unit,
) {
    Column {
        SearchBar { onCancelClick() }
        val uiState = homeViewModel.uiState.collectAsState()
        when (uiState.value) {
            is SearchUiState.Empty -> EmptyScreen()
            is SearchUiState.SearchResult -> TODO()
            is SearchUiState.Ready -> SearchList((uiState.value as SearchUiState.Ready).users) {
                onSearchItemClick(it)
            }
        }
    }
}

@Composable
fun SearchList(users: List<User> = randomUsers(), onSearchItemClick: (user: User) -> Unit = {}) {
    LazyColumn {
        items(items = users) { it ->
            SearchItem(it) { onSearchItemClick(it) }
        }
    }
}

@Composable
fun SearchItem(user: User = randomUser(), onSearchItemClick: (user: User) -> Unit = {}) {
    ConstraintLayout(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .clickable { onSearchItemClick(user) }
    ) {
        val (image, name) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.default_user),
            contentDescription = "profile picture",
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(40.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start, margin = 10.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        Text(
            text = user.name,
            fontSize = 18.sp,
            modifier = Modifier.constrainAs(name) {
                start.linkTo(image.end, margin = 20.dp)
                top.linkTo(image.top)
                bottom.linkTo(image.bottom)
            })
    }
}

@Composable
fun SearchBar(onCancelClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        var text by remember { mutableStateOf("") }
        CustomTextField(
            text = text,
            hintText = "Search for user",
            modifier = Modifier.background(Color.LightGray)
        ) {
            text = it
        }
        Text(
            text = "cancel",
            fontSize = 20.sp,
            color = Purple60,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable { onCancelClick() }
                .fillMaxWidth()
        )
    }
}
