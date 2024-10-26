package com.joshgm3z.ping.ui.screens.frx

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.PingWallpaper
import com.joshgm3z.ping.ui.screens.settings.image.IconPicker
import com.joshgm3z.ping.ui.theme.Green40
import com.joshgm3z.ping.ui.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewFrxContainer() {
    PingTheme {
        FrxContainer(true)
    }
}

@DarkPreview
@Composable
private fun PreviewFrxContainer2() {
    PingTheme {
        FrxContainer(false)
    }
}

@Composable
fun FrxContainer(
    default: Boolean = true,
    onSignInComplete: (name: String) -> Unit = {}
) {
    var imageUrl by remember { mutableStateOf("") }
    var showIconGrid by remember { mutableStateOf(false) }
    var showDropDownMenu by remember { mutableStateOf(false) }

    PingWallpaper {
        var isSignIn by remember { mutableStateOf(default) }
        var showLoading by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 120.dp)
            ) {
                Logo()
                Spacer(Modifier.height(10.dp))
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    SignInCard(
                        isSignIn,
                        onClickExpand = { isSignIn = true },
                        showLoading = { showLoading = it },
                        onSignInComplete = onSignInComplete
                    )
                    AnimatedVisibility(!isSignIn) {
                        HorizontalDivider()
                    }
                    SignUpCard(
                        !isSignIn,
                        imageUrl,
                        onClickExpand = { isSignIn = false },
                        onSignInComplete = onSignInComplete,
                        showPicker = {
                            showDropDownMenu = true
                        },
                    )
                    LoadingBar(showLoading)
                }
            }
            when {
                showIconGrid -> IconPicker(
                    onIconPicked = {
                        imageUrl = it
                        showIconGrid = false
                        showDropDownMenu = false
                    }
                )

                showDropDownMenu -> PickerDropDownMenu(
                    onImageSet = {
                        imageUrl = it
                        showDropDownMenu = false
                    },
                    closePicker = {
                        showDropDownMenu = false
                        showIconGrid = false
                    },
                    showIconGrid = {
                        showIconGrid = true
                    })
            }
        }

    }
}

@Composable
fun LoadingBar(showLoading: Boolean) {
    Column(
        modifier = Modifier.height(5.dp)
    ) {
        AnimatedVisibility(showLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun SignInCard(
    expanded: Boolean,
    onClickExpand: () -> Unit,
    showLoading: (Boolean) -> Unit,
    onSignInComplete: (String) -> Unit,
) {
    Column {
        FrxItem("Sign in", expanded, onClickExpand)
        AnimatedVisibility(
            expanded,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            SignInContent(
                showLoading = showLoading,
                onSignInComplete = onSignInComplete
            )
        }
    }
}

@Composable
private fun SignUpCard(
    expanded: Boolean,
    imageUrl: String,
    onClickExpand: () -> Unit,
    showPicker: () -> Unit = {},
    onSignInComplete: (String) -> Unit = {},
) {
    Column {
        FrxItem("Create a new account", expanded, onClickExpand)
        AnimatedVisibility(
            expanded,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            SignUpContent(
                inputImage = imageUrl,
                onSignUpComplete = onSignInComplete,
                showPicker = showPicker
            )
        }
    }
}

@Composable
fun FrxItem(title: String, enabled: Boolean, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            enabled, onClick = onClick,
            colors = RadioButtonDefaults.colors().copy(
                selectedColor = Green40,
                unselectedColor = Green40
            )
        )
        Spacer(Modifier.width(5.dp))
        Text(
            title,
            color = Green40
        )
    }
}
