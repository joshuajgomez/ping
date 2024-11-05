package com.joshgm3z.ping.ui.common

import android.graphics.Bitmap
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.joshgm3z.ping.graph.PdfViewerRoute
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.FileViewerUiState
import com.joshgm3z.ping.ui.viewmodels.FileViewerViewModel

@DarkPreview
@Composable
fun PreviewPdfViewer() {
    PingTheme {
        PdfViewer()
    }
}

@Composable
fun PdfViewer(
    onBackClick: () -> Unit = {},
    viewModel: FileViewerViewModel? = getIfNotPreview { hiltViewModel() }
) {
    Scaffold(
        topBar = {
            PingTopBar(
                "Someone",
                "9.00 am",
                onBackClick = onBackClick
            )
        }
    ) {
        Box(
            Modifier.padding(it),
            contentAlignment = Alignment.Center
        ) {
            viewModel?.fetchBitmapList(LocalContext.current)
            val uiState = viewModel?.uiState?.collectAsState()
            with(uiState?.value) {
                when (this) {
                    is FileViewerUiState.Ready -> PdfContent(bitmapList)
                    is FileViewerUiState.Empty -> ErrorContent(fileUrl)
                    else -> ErrorContent()
                }
            }
        }
    }
}

@Composable
fun ErrorContent(fileUrl: String = "") {
    Row(
        Modifier
            .padding(40.dp)
            .fillMaxWidth()
            .background(
                color = colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Icon(
            Icons.Default.Error,
            null,
            tint = colorScheme.error
        )
        Spacer(Modifier.size(10.dp))
        Column {
            Text("Error reading pdf")
            Spacer(Modifier.size(5.dp))
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("File URL:\n")
                    }
                    append(fileUrl)
                },
                color = colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 12.sp,
                lineHeight = 15.sp,
            )
        }
    }
}

@Composable
fun PdfContent(bitmapList: List<Bitmap>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items(items = bitmapList) {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

fun NavController.navigateToPdfViewer(fileUrl: String) {
    navigate(PdfViewerRoute(fileUrl))
}