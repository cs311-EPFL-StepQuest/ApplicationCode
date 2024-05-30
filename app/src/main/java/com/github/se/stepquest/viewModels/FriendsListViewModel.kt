package com.github.se.stepquest.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.stepquest.Friend
import com.github.se.stepquest.services.fetchFriendsListFromDatabase
import com.github.se.stepquest.services.isOnline
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FriendsListState(
    val currentFriendsList: List<Friend>? = emptyList(),
    val showAddFriendScreen: Boolean = false,
    val showFriendProfile: Boolean = false,
    val selectedFriend: Friend? = null,
    val isOnline: Boolean = true
)

class FriendsViewModel : ViewModel() {

  val state_ = MutableStateFlow(FriendsListState())
  val state: StateFlow<FriendsListState> = state_

  fun fetchFriends(userId: String) {
    viewModelScope.launch {
      fetchFriendsListFromDatabase(userId) { friendsList ->
        state_.value = state_.value.copy(currentFriendsList = friendsList)
      }
    }
  }

  fun selectFriend(friend: Friend) {
    state_.value = state_.value.copy(selectedFriend = friend, showFriendProfile = true)
  }

  fun deselectFriend() {
    state_.value = state_.value.copy(selectedFriend = null, showFriendProfile = false)
  }

  fun toggleAddFriendScreen(show: Boolean) {
    state_.value = state_.value.copy(showAddFriendScreen = show)
  }

  fun checkOnlineStatus(context: Context) {
    val online = isOnline(context)
    state_.value = state_.value.copy(isOnline = online)
  }
}
