package com.joshgm3z.ping.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.home.HomeChatItem
import com.joshgm3z.data.model.HomeChat
import com.joshgm3z.data.model.User
import com.joshgm3z.data.util.getHomeChatList
import com.joshgm3z.data.util.randomUsers
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.search.InfoBox
import com.joshgm3z.search.viewmodels.AllSearchUiState
import com.joshgm3z.search.viewmodels.AllSearchViewModel
import com.joshgm3z.search.viewmodels.Message
import com.joshgm3z.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AllSearchContainer(
    viewModel: AllSearchViewModel = hiltViewModel(),
    onCloseClick: () -> Unit,
    onSearchResultClick: (userId: String, chatId: String) -> Unit,
) {
    AllSearch(
        queryFlow = viewModel.queryFlow,
        uiState = viewModel.uiState,
        onSearchInputChanged = { viewModel.onSearchTextInput(it) },
        onCloseClick = onCloseClick,
        onSearchResultClick = onSearchResultClick,
    )
}

private val commonHorizontalPadding = 20.dp

@Composable
private fun subTextColor() = colorScheme.onSurface.copy(alpha = 0.5f)

@Composable
private fun AllSearch(
    queryFlow: StateFlow<String> = MutableStateFlow(""),
    uiState: StateFlow<AllSearchUiState>,
    onSearchInputChanged: (String) -> Unit = {},
    onCloseClick: () -> Unit = {},
    onSearchResultClick: (userId: String, chatId: String) -> Unit = { _, _ -> },
) {
    Scaffold(
        topBar = {
            SearchBar(
                queryFlow = queryFlow,
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
            with(uiState.collectAsState().value) {
                SearchResult(
                    uiState = this,
                    query = queryFlow.value,
                    onClick = onSearchResultClick
                )
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
    uiState: AllSearchUiState,
    query: String,
    onClick: (userId: String, chatId: String) -> Unit = { _, _ -> }
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var searchHeaderType by rememberSaveable { mutableStateOf(SearchHeaderType.All) }
        SearchHeader(searchHeaderType, uiState) {
            searchHeaderType = it
        }
        SearchResultContent(searchHeaderType, uiState, query, onClick)
        when {
            uiState.homeChats.isNotEmpty() -> {}
            uiState.messages.isNotEmpty() -> {}
            uiState.users.isNotEmpty() -> {}
            query.isEmpty() -> InfoBox2("Search for anything")
            else -> InfoBox("Sorry, I couldn't find anything")
        }
    }
}

@Composable
fun SearchResultContent(
    searchHeaderType: SearchHeaderType,
    uiState: AllSearchUiState,
    query: String,
    onClick: (userId: String, chatId: String) -> Unit,
) {
    if ((searchHeaderType == SearchHeaderType.All
                || searchHeaderType == SearchHeaderType.Chats)
        && uiState.homeChats.isNotEmpty()
    ) ChatResult(uiState.homeChats) {
        onClick(it.otherGuy.docId, "")
    }

    if ((searchHeaderType == SearchHeaderType.All
                || searchHeaderType == SearchHeaderType.Messages)
        && uiState.messages.isNotEmpty()
    ) MessagesResult(query, uiState.messages) {
        onClick(it.otherGuyId, it.chatId)
    }

    if ((searchHeaderType == SearchHeaderType.All
                || searchHeaderType == SearchHeaderType.Users)
        && uiState.users.isNotEmpty()
    ) UserResult(uiState.users) {
        onClick(it, "")
    }
}

@Composable
fun MessagesResult(
    query: String,
    chats: List<Message>,
    onClick: (Message) -> Unit
) {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        SearchTitle("Messages")
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
fun ChatResult(
    chats: List<HomeChat>,
    onClick: (HomeChat) -> Unit
) {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        SearchTitle("Chat")
        LazyColumn {
            itemsIndexed(chats) { i, it ->
                HomeChatItem(it) {
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
    Column(modifier = Modifier.padding(top = 20.dp)) {
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
        color = colorScheme.onSurface.copy(alpha = 0.7f),
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
        com.joshgm3z.common.UserImage(modifier = Modifier.size(30.dp))
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

enum class SearchHeaderType {
    All,
    Chats,
    Messages,
    Users,
    Photos,
}

@Composable
fun SearchHeader(
    selectedHeaderType: SearchHeaderType,
    uiState: AllSearchUiState,
    onSearchHeaderClick: (SearchHeaderType) -> Unit
) {
    val list = listOf(
        SearchHeaderType.All to (uiState.users.size + uiState.messages.size + uiState.homeChats.size),
        SearchHeaderType.Chats to uiState.homeChats.size,
        SearchHeaderType.Messages to uiState.messages.size,
        SearchHeaderType.Users to uiState.users.size,
        SearchHeaderType.Photos to 0,
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(list) {
            val title = when {
                it.second == 0 -> it.first.name
                else -> "${it.first} (${it.second})"
            }
            SearchChip(
                title = title,
                isSelected = it.first == selectedHeaderType
            ) {
                onSearchHeaderClick(it.first)
            }
        }
    }
}

@Composable
fun SearchChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    val textColor = when {
        isSelected -> colorScheme.primary
        else -> colorScheme.onSurface.copy(alpha = 0.5f)
    }
    val bgColor = when {
        isSelected -> colorScheme.onPrimary
        else -> colorScheme.surfaceContainer
    }
    Text(
        text = title,
        fontSize = 14.sp,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .background(
                color = bgColor,
            )
            .padding(horizontal = 10.dp, vertical = 3.dp),
        color = textColor
    )
}

@Composable
private fun SearchBar(
    queryFlow: StateFlow<String>,
    onTextChanged: (String) -> Unit = {},
    onCloseClick: () -> Unit,
) {
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
        val query = queryFlow.collectAsState()
        Logger.warn("query.value = [${query.value}]")
        AllSearchTextField(
            text = query.value,
            hintText = "Search for user",
            onTextChanged = {
                onTextChanged(it)
            },
            isFocusNeeded = true,
        )
    }
}

@DarkPreview
@Composable
private fun PreviewAllSearch(
    uiState: AllSearchUiState
    = AllSearchUiState(getHomeChatList(), AllSearchViewModel.getChatDataList(), randomUsers())
) {
    PingTheme {
        AllSearch(
            uiState = MutableStateFlow(uiState),
            queryFlow = MutableStateFlow("What"),
        )

    }
}

@DarkPreview
@Composable
private fun PreviewAllSearchInitial() {
    PreviewAllSearch(AllSearchUiState())
}

@DarkPreview
@Composable
private fun PreviewAllSearchEmpty() {
    PreviewAllSearch(AllSearchUiState())
}

@Composable
fun ChatSearchItem(
    message: Message,
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
                message.name,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    message.sentTime,
                    color = subTextColor(),
                    fontSize = 15.sp
                )
                Spacer(Modifier.size(5.dp))
                ArrowForward()
            }
        }
        Text(
            text = buildAnnotatedString {
                if (message.isOutwards) {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("You: ")
                    }
                }
                with(message.message) {
                    val color = colorScheme.primary
                    try {
                        indexOf(query).let {
                            append(substring(0, it))
                            withStyle(style = SpanStyle(color = color)) {
                                append(substring(it, it + query.length))
                            }
                            append(substring(it + query.length))
                        }
                    } catch (e: StringIndexOutOfBoundsException) {
                        Logger.error(e.stackTraceToString())
                        append(substring(0))
                    }
                }
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}