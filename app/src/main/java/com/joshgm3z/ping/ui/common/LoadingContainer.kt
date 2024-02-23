package com.joshgm3z.ping.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.theme.Purple10
import com.joshgm3z.ping.ui.theme.Purple60

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoadingContainer(message: String = "Loading") {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Purple60),
    ) {
        CircularProgressIndicator(
            trackColor = Purple10,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = message,
            color = Purple10,
            fontSize = 18.sp
        )
    }
}
