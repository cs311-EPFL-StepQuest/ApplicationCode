package com.github.se.stepquest.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.stepquest.data.model.ChallengeData
import com.github.se.stepquest.services.getChallenges
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChallengesState(val challenges: List<ChallengeData> = listOf())

/** ViewModel handling the behaviour of the Challenges screen. */
class ChallengesViewModel : ViewModel() {
  private val _state = MutableStateFlow(ChallengesState())
  val state: StateFlow<ChallengesState> = _state

  /**
   * Loads the user's active challenges.
   *
   * @param userId the current user's database ID.
   */
  fun loadChallenges(userId: String) {
    viewModelScope.launch {
      getChallenges(userId) { receivedChallenges ->
        _state.value = _state.value.copy(challenges = receivedChallenges)
      }
    }
  }
}
