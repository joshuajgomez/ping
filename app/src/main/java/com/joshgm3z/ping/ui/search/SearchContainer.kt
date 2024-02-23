package com.joshgm3z.ping.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.joshgm3z.ping.utils.randomUserList

@Composable
fun SearchContainer(
    homeViewModel: HomeViewModel,
    onSearchItemClick: (user: User) -> Unit,
    onCancelClick: () -> Unit,
) {
    Surface {
        SearchScreen(
            userList = homeViewModel.userList,
            onSearchItemClick = { onSearchItemClick(it) },
            onCancelClick = { onCancelClick() },
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreen(
    onSearchItemClick: (user: User) -> Unit = {},
    onCancelClick: () -> Unit = {},
    userList: List<User> = randomUserList(),
) {
    Column {
        SearchBar { onCancelClick() }
        SearchList(userList) { onSearchItemClick(it) }
    }
}

@Composable
fun SearchList(userList: List<User>, onSearchItemClick: (user: User) -> Unit) {
    LazyColumn {
        items(items = userList) {
            SearchItem(it) { onSearchItemClick(it) }
        }
    }
}

@Composable
fun SearchItem(user: User, onSearchItemClick: (user: User) -> Unit) {
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
fun SearchBar(onCancelClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        var text by remember { mutableStateOf("") }
        CustomTextField(
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
