package com.joshgm3z.frx

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.MaterialTheme.colorScheme
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
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.IconPicker
import com.joshgm3z.common.PingWallpaper
import com.joshgm3z.common.theme.PingTheme

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
        var disableControls by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp)
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
                        onSignInComplete = onSignInComplete,
                        disableControls = {
                            disableControls = it
                        }
                    )
                    if (!isSignIn) {
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
                        disableControls = {
                            disableControls = it
                        }
                    )
                }
            }
            when {
                showIconGrid -> IconPicker(
                    onIconPicked = {
                        imageUrl = it
                        showIconGrid = false
                        showDropDownMenu = false
                    },
                    onCloseClick = {
                        showDropDownMenu = false
                        showIconGrid = false
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
            if (disableControls) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clickable { }) {}
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
    onSignInComplete: (String) -> Unit,
    disableControls: (Boolean) -> Unit = {},
) {
    Column {
        FrxItem("Sign in", expanded, onClickExpand)
        AnimatedVisibility(
            expanded,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            SignInContent(
                onSignInComplete = onSignInComplete,
                disableControls = disableControls
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
    disableControls: (Boolean) -> Unit = {},
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
                showPicker = showPicker,
                disableControls = disableControls
            )
        }
    }
}

@Composable
fun FrxItem(title: String, enabled: Boolean, onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            enabled, onClick = onClick,
            colors = RadioButtonDefaults.colors().copy(
                selectedColor = colorScheme.primary,
                unselectedColor = colorScheme.primary
            )
        )
        Spacer(Modifier.width(5.dp))
        Text(
            title,
            color = colorScheme.primary
        )
    }
}
