package com.github.se.stepquest.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.stepquest.data.model.ChallengeData
import com.github.se.stepquest.services.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeScreenState(
    val leaderboard: List<Pair<String, Int>> = emptyList(),
    val topChallenge: ChallengeData? = null,
    val showChallengeCompletionPopUp: Boolean = false,
    val userScore: Int = 0,
    val username: String = "No name",
    val currentPosition: Int = 0,
    val isOnline: Boolean = false
)

class HomeViewModel : ViewModel() {
  private val _state = MutableStateFlow(HomeScreenState())
  val state: StateFlow<HomeScreenState> = _state.asStateFlow()

  fun initialize(userId: String, context: Context) {
    viewModelScope.launch {
      _state.value = _state.value.copy(isOnline = isOnline(context))

      if (_state.value.isOnline) {
        getUsername(userId) { username ->
          cacheUserInfo(context, userId, username)
          _state.value = _state.value.copy(username = username)

          getUserScore(username) { score -> _state.value = _state.value.copy(userScore = score) }

          getUserPlacement(username) { position ->
            _state.value = _state.value.copy(currentPosition = position!!)
          }
        }
      }

      getTopChallenge(userId) { challenge ->
        _state.value = _state.value.copy(topChallenge = challenge)
      }

      getTopLeaderboard(5) { leaderboard ->
        _state.value = _state.value.copy(leaderboard = leaderboard!!)
      }

      someChallengesCompleted(userId) { result ->
        if (result) {
          _state.value = _state.value.copy(showChallengeCompletionPopUp = true)
        }
      }
    }
  }

  fun dismissChallengeCompletionPopUp() {
    _state.value = _state.value.copy(showChallengeCompletionPopUp = false)
  }
}
