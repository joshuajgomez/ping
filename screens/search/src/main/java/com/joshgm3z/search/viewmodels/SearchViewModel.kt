package com.joshgm3z.search.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.User
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SearchUiState {
    data class Info(val message: String) : SearchUiState()
    data class AllUsers(val users: List<User>) : SearchUiState()
    data class SearchResult(val users: List<User>) : SearchUiState()
}

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Info("Fetching..."))
    val uiState = _uiState.asStateFlow()

    val queryFlow = MutableStateFlow("")

    private var users: List<User> = emptyList()

    init {
        viewModelScope.launch {
            userRepository.syncUserListWithServer({})
            _uiState.value = with(userRepository.getUsers()) {
                users = this
                when {
                    isNotEmpty() -> SearchUiState.AllUsers(this)
                    else -> SearchUiState.Info("No users in server")
                }
            }
        }
    }

    fun onSearchInputChanged(query: String) {
        queryFlow.value = query
        if (query.isEmpty()) {
            _uiState.value = when{
                users.isEmpty() -> SearchUiState.Info("No users found")
                else -> SearchUiState.AllUsers(users)
            }
            return
        }
        users.filter {
            it.name.contains(query, ignoreCase = true)
        }.let { it ->
            Logger.debug("query=$query, result=${it.size}; ${it.map { it.name }}")
            _uiState.value = when {
                it.isEmpty() -> SearchUiState.Info("Sorry, no users with that name")
                else -> SearchUiState.SearchResult(it)
            }
        }
    }

}
