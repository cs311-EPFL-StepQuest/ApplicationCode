package com.github.se.stepquest.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.stepquest.UserRepository
import com.github.se.stepquest.services.cacheDailyWeeklySteps
import com.github.se.stepquest.services.cacheStepGoals
import com.github.se.stepquest.services.getCachedStepInfo
import com.github.se.stepquest.services.getCachedSteps
import com.github.se.stepquest.services.isOnline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProgressionPageState(
    val dailyStepsMade: Int = 0,
    val weeklyStepsMade: Int = 0,
    val dailyStepGoal: Int = 5000,
    val weeklyStepGoal: Int = 35000,
    val dailyGoalAchieved: Boolean = false
)

class ProgressionPageViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProgressionPageState())
    val state: StateFlow<ProgressionPageState> get() = _state

    fun initialize(user: UserRepository, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val stepList = getCachedStepInfo(context)
            val dailyStepsMade = stepList["dailySteps"] ?: 0
            val weeklyStepsMade = stepList["weeklySteps"] ?: 0
            val dailyStepGoal = stepList["dailyStepGoal"] ?: 5000
            val weeklyStepGoal = stepList["weeklyStepGoal"] ?: 35000
            val dailyGoalAchieved = dailyStepsMade >= dailyStepGoal

            _state.value = ProgressionPageState(
                dailyStepsMade = dailyStepsMade,
                weeklyStepsMade = weeklyStepsMade,
                dailyStepGoal = dailyStepGoal,
                weeklyStepGoal = weeklyStepGoal,
                dailyGoalAchieved = dailyGoalAchieved
            )

            if (isOnline(context)) {
                fetchOnlineData(user, context)
            }
        }
    }

    private fun fetchOnlineData(user: UserRepository, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            user.getSteps { steps ->
                val onlineDailySteps = steps[0]
                val onlineWeeklySteps = steps[1]

                _state.value = _state.value.copy(
                    dailyStepsMade = onlineDailySteps,
                    weeklyStepsMade = onlineWeeklySteps,
                    dailyGoalAchieved = onlineDailySteps >= _state.value.dailyStepGoal
                )
                cacheDailyWeeklySteps(context, onlineDailySteps, onlineWeeklySteps)
            }
        }
    }

    fun updateSteps(dailySteps: Int, weeklySteps: Int, context: Context) {
        _state.value = _state.value.copy(dailyStepsMade = dailySteps, weeklyStepsMade = weeklySteps)
        cacheDailyWeeklySteps(context, dailySteps, weeklySteps)
    }

    fun updateGoals(dailyGoal: Int, weeklyGoal: Int, context: Context) {
        _state.value = _state.value.copy(dailyStepGoal = dailyGoal, weeklyStepGoal = weeklyGoal)
        cacheStepGoals(context, dailyGoal, weeklyGoal)
    }

    fun resetDailyGoalAchievement() {
        _state.value = _state.value.copy(dailyGoalAchieved = false)
    }
}
