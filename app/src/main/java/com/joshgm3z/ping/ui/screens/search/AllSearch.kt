package com.joshgm3z.ping.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.data.model.User
import com.joshgm3z.data.util.randomUsers
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.Green40
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.AllSearchUiState
import com.joshgm3z.ping.ui.viewmodels.AllSearchViewModel
import com.joshgm3z.ping.ui.viewmodels.ChatData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AllSearchContainer(
    allSearchViewModel: AllSearchViewModel?
    = getIfNotPreview { hiltViewModel() }
) {
    AllSearch(allSearchViewModel?.uiState)
}

private val commonHorizontalPadding = 20.dp

@Composable
private fun subTextColor() = colorScheme.onSurface.copy(alpha = 0.5f)

@Composable
private fun AllSearch(
    uiState: StateFlow<AllSearchUiState>?,
    onSearchInputChanged: (String) -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            SearchBar(
                onTextChanged = onSearchInputChanged,
                onCloseClick = onCloseClick
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.size(5.dp))
            with(uiState?.collectAsState()?.value) {
                when (this) {
                    is AllSearchUiState.Initial -> InfoBox2(message)
                    is AllSearchUiState.SearchEmpty -> InfoBox(message)
                    is AllSearchUiState.SearchResult -> SearchResult(this)
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun InfoBox2(message: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 40.dp)
    ) {
        Icon(
            Icons.Default.Info,
            contentDescription = null,
            tint = colorScheme.primary
        )
        Spacer(Modifier.size(10.dp))
        Text(
            text = message,
        )
    }
}

@Composable
fun SearchResult(
    uiState: AllSearchUiState.SearchResult,
    onClick: (userId: String, chatId: String) -> Unit = { _, _ -> }
) {
    Column {
        SearchHeader()
        Spacer(Modifier.size(20.dp))
        ChatResult(uiState.query, uiState.chats) {
            onClick(it.otherGuyId, it.chatId)
        }
        Spacer(Modifier.height(20.dp))
        UserResult(uiState.users) {
            onClick(it, "")
        }
    }
}

@Composable
fun ChatResult(
    query: String,
    chats: List<ChatData>,
    onClick: (ChatData) -> Unit
) {
    Column {
        SearchTitle("Chats")
        LazyColumn {
            itemsIndexed(chats) { i, it ->
                ChatSearchItem(it, query) {
                    onClick(it)
                }
                if (i < chats.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun UserResult(
    users: List<User>,
    onClick: (String) -> Unit
) {
    Column {
        SearchTitle("Users")
        Spacer(Modifier.size(10.dp))
        LazyColumn {
            itemsIndexed(users) { i, it ->
                UserSearchItem(it) {
                    onClick(it.docId)
                }
                if (i < users.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun SearchTitle(title: String) {
    Text(
        title, fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = commonHorizontalPadding)
    )
}

@Composable
fun UserSearchItem(
    user: User,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = commonHorizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserImage(modifier = Modifier.size(30.dp))
        Spacer(Modifier.size(20.dp))
        Text(user.name, modifier = Modifier.weight(1f))
        ArrowForward()
    }
}

@Composable
fun ArrowForward() {
    Icon(
        Icons.AutoMirrored.Default.ArrowForwardIos,
        contentDescription = null,
        modifier = Modifier.size(18.dp),
        tint = subTextColor()
    )
}

@Composable
fun SearchHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SearchChip("Everything")
        SearchChip("Chats")
        SearchChip("Users")
    }
}

@Composable
fun SearchChip(
    title: String,
    onClick: () -> Unit = {}
) {
    Text(
        text = title,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .background(
                color = colorScheme.onPrimary,
            )
            .padding(horizontal = 10.dp, vertical = 3.dp),
        color = colorScheme.primary
    )
}

@Composable
private fun SearchBar(
    onTextChanged: (String) -> Unit = {},
    onCloseClick: () -> Unit,
) {
    val query = rememberSaveable { mutableStateOf("") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            horizontal = 10.dp,
            vertical = 10.dp
        )
    ) {
        IconButton(onCloseClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Close"
            )
        }
        AllSearchTextField(
            text = query.value,
            hintText = "Search for user",
            onTextChanged = {
                query.value = it
                onTextChanged(it)
            },
            isFocusNeeded = false,
        )
    }
}

@DarkPreview
@Composable
private fun PreviewAllSearch(
    uiState: AllSearchUiState
    = AllSearchUiState.SearchResult("What", getChatDataList(), randomUsers())
) {
    PingTheme {
        AllSearch(
            uiState = MutableStateFlow(uiState)
        )

    }
}

fun getChatDataList(): List<ChatData> = listOf(
    ChatData(isOutwards = true),
    ChatData(isOutwards = true),
    ChatData(message = "Some very long Whattt message that is sooooooo long you cant even read it in full long you cant even read it in full "),
    ChatData(),
    ChatData(),
    ChatData(isOutwards = true, message = "Whats"),
)

@DarkPreview
@Composable
private fun PreviewAllSearchInitial() {
    PreviewAllSearch(AllSearchUiState.Initial())
}

@DarkPreview
@Composable
private fun PreviewAllSearchEmpty() {
    PreviewAllSearch(AllSearchUiState.SearchEmpty())
}

@Composable
fun ChatSearchItem(
    chat: ChatData,
    query: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = commonHorizontalPadding),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                chat.name,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    chat.sentTime,
                    color = subTextColor(),
                    fontSize = 15.sp
                )
                Spacer(Modifier.size(5.dp))
                ArrowForward()
            }
        }
        Text(
            text = buildAnnotatedString {
                if (chat.isOutwards) {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("You: ")
                    }
                }
                val index = chat.message.indexOf(query)
                append(chat.message.substring(0, index))
                withStyle(style = SpanStyle(color = Green40)) {
                    append(chat.message.substring(index))
                }
                append(chat.message.substring(index + query.length))
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}