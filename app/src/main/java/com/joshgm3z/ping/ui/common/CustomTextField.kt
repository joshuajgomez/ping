package com.joshgm3z.ping.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshgm3z.ping.ui.theme.Purple60

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hintText: String = "Type something",
    onValueChange: (text: String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier
            .height(70.dp)
            .padding(all = 10.dp)
            .clip(RoundedCornerShape(20.dp)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent, //hide the indicator
            unfocusedIndicatorColor = Color.LightGray
        ),
        placeholder = {
            Text(text = hintText)
        },
        value = text,
        onValueChange = {
            onValueChange(it)
        },
        trailingIcon = {
            AnimatedVisibility(visible = text.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "clear message",
                    tint = Purple60,
                    modifier = Modifier.clickable { onValueChange("") }
                )
            }
        },
    )
}