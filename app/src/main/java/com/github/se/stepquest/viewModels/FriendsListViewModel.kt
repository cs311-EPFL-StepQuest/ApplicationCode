package com.github.se.stepquest.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.stepquest.Friend
import com.github.se.stepquest.services.fetchFriendsListFromDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FriendsListState(
    val currentFriendsList: List<Friend>? = emptyList(),
    val showAddFriendScreen: Boolean = false,
    val showFriendProfile: Boolean = false,
    val selectedFriend: Friend? = null
)

class FriendsViewModel : ViewModel() {

  private val _state = MutableStateFlow(FriendsListState())
  val state: StateFlow<FriendsListState> = _state

  fun fetchFriends(userId: String) {
    viewModelScope.launch {
      fetchFriendsListFromDatabase(userId) { friendsList ->
        _state.value = _state.value.copy(currentFriendsList = friendsList)
      }
    }
  }

  fun selectFriend(friend: Friend) {
    _state.value = _state.value.copy(selectedFriend = friend, showFriendProfile = true)
  }

  fun deselectFriend() {
    _state.value = _state.value.copy(selectedFriend = null, showFriendProfile = false)
  }

  fun toggleAddFriendScreen(show: Boolean) {
    _state.value = _state.value.copy(showAddFriendScreen = show)
  }
}
