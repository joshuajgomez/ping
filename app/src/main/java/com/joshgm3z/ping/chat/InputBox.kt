package com.joshgm3z.ping.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.theme.Purple40
import com.joshgm3z.ping.ui.theme.Purple60

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputBox(onSendClick: (text: String) -> Unit = {}, defaultText: String = "") {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        var text by remember { mutableStateOf(defaultText) }

        TextField(
            placeholder = {
                Text(text = "Type a message...")
            },
            value = text,
            onValueChange = { text = it },
            trailingIcon = {
                AnimatedVisibility(visible = text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "clear message",
                        tint = Purple60,
                        modifier = Modifier.clickable { text = "" }
                    )
                }
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(all = 10.dp)
                .clip(RoundedCornerShape(20.dp)),
//            textStyle = TextStyle.Default.copy(fontSize = 20.sp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.LightGray
            )
        )

        Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "send message",
            tint = Color.White,
            modifier = Modifier
                .padding(end = 10.dp)
                .clip(CircleShape)
                .size(40.dp)
                .background(color = Purple60)
                .padding(all = 8.dp)
                .clickable { onSendClick(text) }
        )
    }
}

@Composable
@Preview
fun PreviewInputMessage() {
    InputBox(defaultText = "Hello ")
}

@Composable
@Preview
fun PreviewInputEmpty() {
    InputBox(defaultText = "")
}

@Composable
@Preview
fun PreviewVeryLongInput() {
    InputBox(defaultText = "Hello Hello Hello Hello Hello Hello Hello Hello Hello ")
}