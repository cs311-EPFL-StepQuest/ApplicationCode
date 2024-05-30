package com.github.se.stepquest.viewModels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.stepquest.Friend
import com.github.se.stepquest.data.model.ChallengeType
import com.github.se.stepquest.services.createChallengeItem
import com.github.se.stepquest.services.getUserId
import com.github.se.stepquest.services.getUsername
import com.github.se.stepquest.services.sendPendingChallenge
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FriendDialogState(
    val challengeMode: Boolean = false,
    val challengeSentVisible: Boolean = false,
    val friend: Friend? = null
)

class FriendDialogViewModel : ViewModel() {
  private val _state = MutableStateFlow(FriendDialogState())
  val state: StateFlow<FriendDialogState> = _state

  fun setFriend(friend: Friend) {
    _state.value = _state.value.copy(friend = friend)
  }

  fun toggleChallengeMode() {
    _state.value = _state.value.copy(challengeMode = !_state.value.challengeMode)
  }

  fun sendChallenge(userId: String, friendName: String, challengeType: ChallengeType) {
    viewModelScope.launch {
      getUsername(userId) { currentUsername ->
        getUserId(friendName) { friendUserId ->
          val challenge =
              createChallengeItem(userId, currentUsername, friendUserId, friendName, challengeType)
          sendPendingChallenge(challenge)
          _state.value = _state.value.copy(challengeSentVisible = true, challengeMode = false)
          runAfterDelay(2000) { _state.value = _state.value.copy(challengeSentVisible = false) }
        }
      }
    }
  }

  private fun runAfterDelay(delayMillis: Long, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(action, delayMillis)
  }
}
