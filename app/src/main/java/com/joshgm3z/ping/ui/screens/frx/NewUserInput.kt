package com.joshgm3z.ping.ui.screens.frx

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.CustomTextField3
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.ErrorText
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.common.dashedBorder
import com.joshgm3z.ping.ui.common.getCameraLauncher
import com.joshgm3z.ping.ui.common.getGalleryLauncher
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.screens.settings.Setting
import com.joshgm3z.ping.ui.screens.settings.SettingListCard
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.SignUpViewModel
import com.joshgm3z.utils.FileUtil

@DarkPreview
@Composable
fun PreviewNewUserInput() {
    PingTheme {
        SignUpContent()
    }
}

@Composable
fun SignUpContent(
    inputImage: String = "",
    inputName: String = "",
    signUpViewModel: SignUpViewModel? = getIfNotPreview { hiltViewModel() },
    onSignUpComplete: (String) -> Unit = {},
    showLoading: (Boolean) -> Unit = {},
    showPicker: () -> Unit = {},
) {
    Column {
        var name by remember { mutableStateOf(inputName) }
        var about by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }
        var isShowLoading by remember { mutableStateOf(false) }

        val onSignUpClick: () -> Unit = {
            when {
                name.isNotEmpty() -> {
                    isShowLoading = true
                    showLoading(true)
                    signUpViewModel?.onSignUpClick(
                        name,
                        inputImage,
                        about,
                        onSignUpComplete = onSignUpComplete,
                        onSignUpError = {
                            error = it
                            showLoading(false)
                        },
                    )
                }

                name.length <= 3 -> error = "Should be more that 3 letters"
                else -> error = "Name cannot be empty"
            }
        }

        Spacer(Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp, end = 40.dp)
        ) {
            ImageInput(inputImage, showPicker)
            Spacer(Modifier.width(30.dp))
            Text(
                "Set a profile picture",
                fontSize = 20.sp,
                color = colorScheme.onSurface
            )
        }
        Spacer(Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                    color = colorScheme.surfaceContainerHigh
                )
        ) {
            CustomTextField3(
                text = name,
                icon = Icons.Default.AlternateEmail,
                hintText = "enter your name",
                onTextChanged = {
                    name = it
                    error = ""
                })
            HorizontalDivider(thickness = 1.dp)
            CustomTextField3(
                text = password,
                icon = Icons.Default.LockOpen,
                hintText = "enter password",
                onTextChanged = {
                    password = it
                    error = ""
                })
            HorizontalDivider(thickness = 1.dp)
            CustomTextField3(
                text = confirmPassword,
                icon = Icons.Default.Lock,
                hintText = "confirm password",
                onTextChanged = {
                    confirmPassword = it
                    error = ""
                })
            HorizontalDivider(thickness = 1.dp)
            CustomTextField3(
                text = about,
                icon = Icons.Default.EditNote,
                hintText = "something about you",
                onTextChanged = {
                    about = it
                    error = ""
                })
            ErrorText(error)
            PingButton(
                "Create Account",
                onClick = onSignUpClick
            )
        }

    }
}

@Composable
fun PickerDropDownMenu(
    onImageSet: (String) -> Unit,
    showIconGrid: () -> Unit,
    closePicker: () -> Unit,
) {
    val galleryLauncher = getGalleryLauncher {
        onImageSet(it.toString())
    }
    val cameraUri = getIfNotPreview { FileUtil.getUri(LocalContext.current) }
    val cameraLauncher = getCameraLauncher {
        onImageSet(cameraUri.toString())
    }
    val list = listOf(
        Setting(
            "Take a photo",
            icon = Icons.Default.CameraAlt
        ) { cameraLauncher.launch(cameraUri!!) },
        Setting(
            "Open gallery",
            icon = Icons.Default.PermMedia
        ) { galleryLauncher.launch("image/*") },
        Setting(
            "Choose an icon",
            icon = Icons.Default.EmojiEmotions
        ) { showIconGrid() },
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(top = 150.dp)
            .clickable { closePicker() },
        contentAlignment = Alignment.Center
    ) {
        SettingListCard(
            list,
            Modifier
                .width(300.dp)
                .background(color = colorScheme.surface, RoundedCornerShape(20.dp))
        )
    }
}

@DarkPreview
@Composable
fun PreviewImageInput() {
    PingTheme {
        ImageInput()
    }
}

@Composable
fun ImageInput(
    imageUrl: String = "",
    onInputClick: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.size(80.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            /*when {
                imageUrl.isNotEmpty() -> UserImage(imageUrl = imageUrl)
                else -> ImagePlaceHolder()
            }*/
            UserImage(imageUrl = imageUrl)
            IconButton(
                onInputClick,
                modifier = Modifier
                    .background(colorScheme.primary, shape = CircleShape)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "edit image",
                    tint = colorScheme.onPrimary,
                )
            }
        }
    }
}

@Composable
fun ImagePlaceHolder() {
    Box(
        Modifier
            .fillMaxSize()
            .dashedBorder(color = colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Outlined.Person,
            contentDescription = "user",
            tint = colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
    }
}
