package com.joshgm3z.ping.ui.screens.chat

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.joshgm3z.data.model.Chat
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.FilePreview
import com.joshgm3z.ping.ui.common.MessageBrief
import com.joshgm3z.ping.ui.common.getCameraLauncher
import com.joshgm3z.ping.ui.common.getGalleryLauncher
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.common.launchFilePicker
import com.joshgm3z.ping.ui.common.launchImagePicker
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.ChatInputUiState
import com.joshgm3z.ping.ui.viewmodels.ChatInputViewModel
import com.joshgm3z.utils.FileUtil

data class PingSheetState(
    val show: (Boolean) -> Unit = {},
    val click: PingSheetClick = PingSheetClick.Empty,
)

sealed class PingSheetClick {
    data object Empty : PingSheetClick()
    data object Camera : PingSheetClick()
    data object Gallery : PingSheetClick()
    data object File : PingSheetClick()
}

@Composable
fun InputBox(
    viewModel: ChatInputViewModel? = getIfNotPreview { hiltViewModel() },
    sheetState: PingSheetState = PingSheetState(),
    onPreviewClick: (ChatInputUiState) -> Unit = {},
) {
    var text by remember { mutableStateOf("") }
    val topRadius = 20.dp
    Column(
        Modifier
            .clip(RoundedCornerShape(topStart = topRadius, topEnd = topRadius))
            .fillMaxWidth()
            .background(colorScheme.surface)
            .padding(10.dp)
    ) {
        val uiState = viewModel?.uiState?.collectAsState()
        AnimatedVisibility(uiState?.value !is ChatInputUiState.Empty) {
            InputPreview(
                uiState = uiState?.value ?: ChatInputUiState.Empty,
                onDeleteClick = { viewModel?.clearPreviewState() },
                onClick = {
                    uiState?.value?.let {
                        onPreviewClick(it)
                    }
                }
            )
        }
        MessageBox(
            isEnabled = (text.isNotEmpty() || uiState?.value != ChatInputUiState.Empty),
            text = text,
            onTextChange = { text = it },
            onSendClick = {
                viewModel?.onSendButtonClick(text)
                text = ""
            },
            onUriReady = {
                viewModel?.updatePreviewStateWithFile(it)
            },
            sheetState = sheetState,
        )
    }
}

@Composable
private fun MessageBox(
    text: String,
    isEnabled: Boolean,
    onTextChange: (String) -> Unit,
    onSendClick: (String) -> Unit,
    onUriReady: (Uri) -> Unit,
    sheetState: PingSheetState = PingSheetState(),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.surfaceContainer, shape = RoundedCornerShape(30.dp))
            .padding(start = 15.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            textStyle = LocalTextStyle.current.copy(
                color = colorScheme.onSurface.copy(alpha = 0.8f),
            ),
            decorationBox = { innerTextField ->
                when {
                    text.isEmpty() -> Text(
                        "Type something",
                        color = colorScheme.onSurface.copy(alpha = 0.3f)
                    )

                    else -> innerTextField()
                }
            }

        )

        AttachIcon(
            onUriReady,
            sheetState = sheetState
        )
        AnimatedVisibility(isEnabled) {
            Spacer(Modifier.size(5.dp))
            IconButton(
                onClick = { onSendClick(text) },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(25.dp)
                    .background(colorScheme.primary)
                    .padding(3.dp)
            ) {
                Icon(
                    Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun AttachIcon(
    onUriReady: (Uri) -> Unit = {},
    sheetState: PingSheetState
) {
    HandleClickEvent(sheetState.click, onUriReady)
    IconButton({ sheetState.show(true) }) {
        Icon(
            Icons.Default.AttachFile,
            contentDescription = null,
            tint = colorScheme.primary
        )
    }
}

@Composable
fun HandleClickEvent(click: PingSheetClick, onUriReady: (Uri) -> Unit) {
    val filePicker = getGalleryLauncher {
        onUriReady(it)
    }
    val imagePicker = getGalleryLauncher {
        onUriReady(it)
    }
    val cameraUri =
        getIfNotPreview { FileUtil.getUri(LocalContext.current) } ?: Uri.parse("")
    val cameraLauncher = getCameraLauncher {
        onUriReady(cameraUri)
    }

    when (click) {
        is PingSheetClick.Camera -> {
            cameraLauncher.launch(cameraUri)
        }

        is PingSheetClick.Gallery -> {
            imagePicker.launchImagePicker()
        }

        is PingSheetClick.File -> {
            filePicker.launchFilePicker()
        }

        else -> {}
    }
}

@Composable
private fun InputPreview(
    uiState: ChatInputUiState,
    onDeleteClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp,bottom = 10.dp),
    ) {
        Box(modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }) {
            with(uiState) {
                when (this) {
                    is ChatInputUiState.Reply -> ReplyPreviewInline(chat, fromName)
                    is ChatInputUiState.Image -> ImagePreviewInline(imageUri)
                    is ChatInputUiState.File -> FilePreview(fileUri)

                    else -> {}
                }
            }
        }
        IconButton(
            onDeleteClick,
            modifier = Modifier.padding(2.dp)
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = null,
                tint = colorScheme.onSurface.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
fun ImagePreviewInline(imageUri: Uri) {
    AsyncImage(
        model = imageUri,
        contentDescription = null,
        error = painterResource(R.drawable.wallpaper2),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(chatBubbleRadius)),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun ReplyPreviewInline(chat: Chat, fromName: String) {
    Column(
        modifier = Modifier
            .background(colorScheme.surfaceContainerHigh, RoundedCornerShape(10.dp))
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = fromName,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            fontSize = 15.sp
        )
        MessageBrief(chat)
    }
}

//@DarkPreview
@Composable
fun PreviewInputPreviewImage() {
    PingTheme {
        Box(Modifier.padding(10.dp)) {
            InputPreview(ChatInputUiState.Image(Uri.parse("")))
        }
    }
}

//@DarkPreview
@Composable
fun PreviewInputPreviewFile() {
    PingTheme {
        InputPreview(ChatInputUiState.File(Uri.parse("")))
    }
}

//@DarkPreview
@Composable
fun PreviewInputPreviewReply() {
    PingTheme {
        InputPreview(ChatInputUiState.Reply(Chat(""), "This guy"))
    }
}
