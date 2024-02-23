package com.joshgm3z.ping.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.R
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.ui.theme.Green40
import com.joshgm3z.ping.ui.theme.Purple40
import com.joshgm3z.ping.ui.theme.Purple60
import com.joshgm3z.ping.utils.randomUser

@Preview
@Composable
fun Title(user: User = randomUser()) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .background(Purple60)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "go to home",
            tint = Color.White,
            modifier = Modifier
                .size(60.dp)
                .padding(10.dp)
                .clip(CircleShape)
                .padding(5.dp)
                .clickable {}
        )
        Image(
            painter = painterResource(id = R.drawable.default_user),
            contentDescription = "default user",
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(40.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = user.name,
            fontSize = 22.sp,
            color = Color.White,
            modifier = Modifier.widthIn(80.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "more options",
            tint = Color.White,
            modifier = Modifier
                .size(60.dp)
                .padding(15.dp)
                .clickable {})
    }
}