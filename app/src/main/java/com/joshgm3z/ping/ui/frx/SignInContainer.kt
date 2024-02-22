package com.joshgm3z.ping.ui.frx

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.theme.Purple60

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInContainer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Purple60),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_ping_foreground),
            contentDescription = "logo",
            modifier = Modifier
                .size(300.dp)
        )

        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Enter your name",
            color = Color.White,
            fontSize = 20.sp,
        )

        Spacer(modifier = Modifier.height(30.dp))

        var name by remember { mutableStateOf("") }
        TextField(value = name, onValueChange = { name = it })

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { },
        ) {
            Text(text = "sign in to ping", fontSize = 20.sp)
        }
    }
}
