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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.FilePresent
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
import com.joshgm3z.ping.ui.common.getCameraLauncher
import com.joshgm3z.ping.ui.common.getGalleryLauncher
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.common.launchFilePicker
import com.joshgm3z.ping.ui.common.launchImagePicker
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.ChatInlineUiState
import com.joshgm3z.ping.ui.viewmodels.ChatInputUiState
import com.joshgm3z.ping.ui.viewmodels.ChatInputViewModel
import com.joshgm3z.utils.FileUtil
import java.io.File

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
    sheetState: PingSheetState,
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
        InputPreview(
            uiState = uiState?.value ?: ChatInputUiState.Empty,
            onDeleteClick = { viewModel?.clearPreviewState() },
        )
        MessageBox(
            isEnabled = (text.isNotEmpty() || uiState?.value != ChatInputUiState.Empty),
            text = text,
            onTextChange = { text = it },
            onSendClick = {
                viewModel?.onSendButtonClick(text)
                text = ""
            },
            onUriReady = { uri, fileType ->
                viewModel?.updatePreviewStateWithFile(uri, fileType)
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
    onUriReady: (Uri, FileType) -> Unit,
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
    onUriReady: (Uri, FileType) -> Unit = { _, _ -> },
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

enum class FileType {
    Image,
    Video,
    Audio,
    File,
}

@Composable
fun HandleClickEvent(click: PingSheetClick, onUriReady: (Uri, FileType) -> Unit) {
    val filePicker = getGalleryLauncher {
        onUriReady(it, FileType.File)
    }
    val imagePicker = getGalleryLauncher {
        onUriReady(it, FileType.Image)
    }
    val cameraUri =
        getIfNotPreview { FileUtil.getUri(LocalContext.current) } ?: Uri.parse("")
    val cameraLauncher = getCameraLauncher {
        onUriReady(cameraUri, FileType.Image)
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
    onDeleteClick: () -> Unit = {}
) {
    AnimatedVisibility(uiState !is ChatInputUiState.Empty) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = colorScheme.surface)
                    .padding(3.dp)
                    .clickable { onDeleteClick() },
                tint = colorScheme.onSurface
            )
            Column(
                modifier = Modifier
                    .padding(
                        bottom = 15.dp,
                        start = 10.dp, end = 10.dp,
                    )
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                with(uiState) {
                    when (this) {
                        is ChatInputUiState.Reply -> ReplyPreviewInline(chat, fromName)
                        is ChatInputUiState.Image -> ImagePreviewInline(imageUri)
                        is ChatInputUiState.File -> FilePreviewInline(fileUri)

                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun FilePreviewInline(fileUri: Uri) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(colorScheme.surfaceContainerHigh, RoundedCornerShape(10.dp))
            .padding(start = 10.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
    ) {
        Icon(
            Icons.Default.FilePresent, contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .background(
                    colorScheme.surface,
                    RoundedCornerShape(10.dp)
                )
                .padding(5.dp),
            tint = colorScheme.primary
        )
        Spacer(Modifier.size(15.dp))
        Column {
            val file = File(fileUri.path)
            Text(file.name.ifEmpty { "Unknown File" }, color = colorScheme.onSurface)
            Text(
                getFileSizeString(file.length()),
                fontSize = 15.sp,
                color = colorScheme.onSurface.copy(alpha = .5f)
            )
        }
    }
}

fun getFileSizeString(fileLength: Long): String {
    val fileSizeInKB = fileLength / 1024
    return if (fileSizeInKB >= 1024) {
        "${fileSizeInKB / 1024} MB"
    } else {
        "$fileSizeInKB KB"
    }
}

@Composable
fun ImagePreviewInline(imageUri: Uri) {
    Text(
        "Send image",
        fontWeight = FontWeight.Bold,
        color = colorScheme.primary
    )
    Spacer(Modifier.size(2.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            error = painterResource(R.drawable.wallpaper2),
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(chatBubbleRadius)),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.size(20.dp))
        AddImage()
    }
}

@Composable
fun ReplyPreviewInline(chat: Chat, fromName: String) {
    Text(
        text = fromName,
        fontWeight = FontWeight.Bold,
        color = colorScheme.primary
    )
    Text(
        text = chat.message,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = colorScheme.onSurface
    )
}

@Composable
fun AddImage(onClick: () -> Unit = {}) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .clip(CircleShape)
            .background(colorScheme.primary)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = colorScheme.onPrimary,
            modifier = Modifier.size(60.dp)
        )
    }
}

//@DarkPreview
@Composable
fun PreviewInputBox() {
    PingTheme {
        Box(Modifier.padding(10.dp)) {
            MessageBox("", true, {}, {}, { _, _ -> }, PingSheetState({}, PingSheetClick.Gallery))
        }
    }
}

//@DarkPreview
@Composable
fun PreviewInputBoxImage() {
    PingTheme {
        Box(Modifier.padding(10.dp)) {
            ReplyContent(ChatInlineUiState.Image(""))
        }
    }
}

@DarkPreview
@Composable
fun PreviewInputBoxFile() {
    PingTheme {
        InputPreview(ChatInputUiState.File(Uri.parse("")))
    }
}
