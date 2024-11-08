package com.joshgm3z.common.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.common.MessageBrief
import com.joshgm3z.common.StatusIcon
import com.joshgm3z.common.UserImage
import com.joshgm3z.data.model.HomeChat
import com.joshgm3z.utils.getPrettyTime

@Composable
fun HomeChatItem(
    homeChat: HomeChat = HomeChat.random(),
    onChatClick: (homeChat: HomeChat) -> Unit = {},
) {
    ConstraintLayout(modifier = Modifier.clickable { onChatClick(homeChat) }) {
        val (message, user, count, image, line, time) = createRefs()
        UserImage(
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(45.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 15.dp)
                },
            imageUrl = homeChat.otherGuy.imagePath
        )
        Text(
            text = homeChat.otherGuy.name,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .constrainAs(user) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(image.end, margin = 15.dp)
                }
                .widthIn(max = 260.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.constrainAs(message) {
                top.linkTo(user.bottom)
                start.linkTo(user.start)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = homeChat.lastChat.isOutwards) {
                StatusIcon(
                    status = homeChat.lastChat.status,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(17.dp)
                )
            }
            MessageBrief(homeChat.lastChat)
        }
        Text(
            text = getPrettyTime(homeChat.lastChat.sentTime),
            color = if (homeChat.count > 0) {
                colorScheme.primary
            } else {
                colorScheme.outline
            },
            fontSize = 13.sp,
            modifier = Modifier
                .constrainAs(time) {
                    top.linkTo(user.top)
                    end.linkTo(parent.end, margin = 15.dp)
                },
        )
        AnimatedVisibility(
            visible = homeChat.count > 0,
            modifier = Modifier.constrainAs(count) {
                top.linkTo(time.bottom, margin = 3.dp)
                end.linkTo(time.end)
            }
        ) {
            Text(
                text = "${homeChat.count}",
                fontSize = 15.sp,
                color = colorScheme.primary,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colorScheme.primaryContainer)
                    .padding(horizontal = 8.dp, vertical = 0.dp)
            )
        }
        HorizontalDivider(
            color = colorScheme.outlineVariant.copy(alpha = 0.4f),
            modifier = Modifier.constrainAs(line) {
                top.linkTo(image.bottom, margin = 10.dp)
            })
    }
}