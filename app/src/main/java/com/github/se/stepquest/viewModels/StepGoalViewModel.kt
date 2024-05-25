package com.github.se.stepquest.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class StepGoalsState(val newDailyStepGoal: String = "", val newWeeklyStepGoal: String = "")

class StepGoalsViewModel : ViewModel() {
  private val _state = MutableStateFlow(StepGoalsState())
  val state: StateFlow<StepGoalsState>
    get() = _state

  fun updateDailyStepGoal(newDailyStepGoal: String) {
    _state.value = _state.value.copy(newDailyStepGoal = newDailyStepGoal)
  }

  fun updateWeeklyStepGoal(newWeeklyStepGoal: String) {
    _state.value = _state.value.copy(newWeeklyStepGoal = newWeeklyStepGoal)
  }

  fun calculateAndConfirmGoals(
      onConfirm: (dailyStepGoal: Int, weeklyStepGoal: Int) -> Unit,
      onDismiss: () -> Unit
  ) {
    viewModelScope.launch {
      val (dailyStep, weeklyStep) =
          calculateStepGoals(_state.value.newDailyStepGoal, _state.value.newWeeklyStepGoal)
      onConfirm(dailyStep, weeklyStep)
      onDismiss()
    }
  }

  private fun calculateStepGoals(
      newDailyStepGoal: String,
      newWeeklyStepGoal: String
  ): Pair<Int, Int> {
    val dailyStep =
        newDailyStepGoal
            .filter { it.isDigit() }
            .take(5)
            .let {
              if (it.isBlank()) {
                5000 // Default value if blank
              } else {
                val parsedInput = it.toIntOrNull() ?: 0
                val roundedValue = (parsedInput + 249) / 250 * 250
                if (roundedValue < 1000) {
                  1000
                } else {
                  roundedValue
                }
              }
            }

    val weeklyStep = newWeeklyStepGoal.takeIf { it.isNotBlank() }?.toInt() ?: (dailyStep * 7)

    return Pair(dailyStep, weeklyStep)
  }
}
