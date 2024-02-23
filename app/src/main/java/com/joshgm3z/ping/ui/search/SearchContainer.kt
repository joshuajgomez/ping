package com.joshgm3z.ping.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.ping.R
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.ui.home.HomeViewModel
import com.joshgm3z.ping.utils.randomUserList

@Composable
fun SearchContainer(homeViewModel: HomeViewModel) {
    SearchScreen()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreen() {
    Column {
        SearchTitle()
        SearchList(randomUserList())
    }
}

@Composable
fun SearchList(userList: List<User>) {
    LazyColumn() {
        items(items = userList) {
            SearchItem(it)
        }
    }
}

@Composable
fun SearchItem(user: User) {
    ConstraintLayout {
        val (image, name) = createRefs()
        Icon(
            painter = painterResource(id = R.drawable.default_user),
            contentDescription = "profile picture",
            modifier = Modifier.constrainAs(image) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
fun SearchTitle() {
    Surface {
        Text(text = "search", fontSize = 20.sp)
    }
}
