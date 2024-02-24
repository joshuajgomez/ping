package com.joshgm3z.ping.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.ping.R
import com.joshgm3z.ping.data.HomeChat
import com.joshgm3z.ping.ui.theme.Gray40
import com.joshgm3z.ping.ui.theme.Green40
import com.joshgm3z.ping.utils.getHomeChatList

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeChatList(
    homeChatList: List<HomeChat> = getHomeChatList(),
    onChatClick: (homeChat: HomeChat) -> Unit = {},
) {
    LazyColumn(modifier = Modifier.padding(top = 5.dp)) {
        items(items = homeChatList) { it ->
            HomeChatItem(it) {
                onChatClick(it)
            }
        }
    }
}

@Composable
fun HomeChatItem(
    homeChat: HomeChat = HomeChat.random(),
    onChatClick: (homeChat: HomeChat) -> Unit = {},
) {
    ConstraintLayout(modifier = Modifier.clickable { onChatClick(homeChat) }) {
        val (message, user, count, image, line) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.default_user),
            contentDescription = "default user",
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(45.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 10.dp)
                }
        )
        Text(
            text = homeChat.otherGuy.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .constrainAs(user) {
                    top.linkTo(image.top)
                    start.linkTo(image.end, margin = 15.dp)
                }
                .widthIn(max = 260.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = homeChat.lastChat.message,
            fontSize = 15.sp,
            modifier = Modifier
                .constrainAs(message) {
                    top.linkTo(user.bottom, margin = 3.dp)
                    start.linkTo(user.start)
                }
                .widthIn(max = 260.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        AnimatedVisibility(
            visible = homeChat.count > 0,
            modifier = Modifier.constrainAs(count) {
                top.linkTo(parent.top)
                end.linkTo(parent.end, margin = 10.dp)
                bottom.linkTo(parent.bottom)
            }
        ) {
            Text(
                text = "${homeChat.count}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Green40)
                    .padding(horizontal = 8.dp, vertical = 1.dp)
            )
        }
        Divider(
            color = Gray40,
            modifier = Modifier.constrainAs(line) {
                top.linkTo(image.bottom, margin = 10.dp)
            })
    }
}